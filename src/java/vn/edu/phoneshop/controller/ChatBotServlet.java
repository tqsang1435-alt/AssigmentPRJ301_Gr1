package vn.edu.phoneshop.controller;

import jakarta.servlet.ServletException;
import jakarta.servlet.annotation.WebServlet;
import jakarta.servlet.http.HttpServlet;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;
import jakarta.servlet.http.HttpSession;

import java.io.*;
import java.net.HttpURLConnection;
import java.net.URL;
import java.nio.charset.StandardCharsets;
import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.util.*;
import java.util.concurrent.ConcurrentHashMap;
import java.util.regex.Matcher;
import java.util.regex.Pattern;
import java.util.stream.Collectors;

import com.google.gson.Gson;
import com.google.gson.JsonObject;
import com.google.gson.JsonArray;
import com.google.gson.JsonElement;

import vn.edu.phoneshop.dao.ProductDAO;
import vn.edu.phoneshop.model.Product;

@WebServlet(name = "ChatBotServlet", urlPatterns = { "/chat-bot" })
public class ChatBotServlet extends HttpServlet {

    private static final long COOLDOWN_TIME = 60 * 1000;
    /**
     * Cooldown map dùng composite key "apiKey::modelName" để tránh "oan" model khác
     * khi 1 key chỉ bị giới hạn ở 1 model cụ thể.
     */
    private static final Map<String, Long> keyCooldowns = new ConcurrentHashMap<>();
    /** Sentinel trả về khi TẤT CẢ tổ hợp (key × model) đều bị rate-limit / lỗi */
    private static final String SENTINEL_ALL_429 = "__ALL_MODELS_RATE_LIMITED_429__";

    public static class ChatMessage implements java.io.Serializable {
        public String role;
        public String content;

        public ChatMessage(String role, String content) {
            this.role = role;
            this.content = content;
        }
    }

    public static class IntentData implements java.io.Serializable {
        private static final long serialVersionUID = 1L;
        public double minPrice = 0;
        public double maxPrice = 0;
        public String ram = "";
        public String rom = "";
        public boolean isComparison = false;
        public boolean isGaming = false;
        public boolean isCamera = false;
        public boolean isBattery = false;
        public boolean isBuying = false;
        public String exactPhrase = "";
        public List<String> keywords = new ArrayList<>();

        public boolean hasFullIntent() {
            return maxPrice > 0 && (!ram.isEmpty() || !rom.isEmpty() || isGaming || !keywords.isEmpty());
        }
    }

    /**
     * UserMemory: lưu hành vi người dùng trong session để cá nhân hóa gợi ý.
     * - clickedProductIds: danh sách ID sản phẩm user đã click xem
     * - purchasedBrands: thương hiệu user đã từng mua (từ DB đơn hàng)
     */
    public static class UserMemory implements java.io.Serializable {
        private static final long serialVersionUID = 1L;
        // Tên sản phẩm đã click (tối đa 20 gần nhất)
        public LinkedList<String> clickedProductNames = new LinkedList<>();
        // Thương hiệu đã mua (Apple, Samsung, Xiaomi, ...)
        public Set<String> purchasedBrands = new LinkedHashSet<>();
        // Tên sản phẩm đã mua
        public List<String> purchasedProductNames = new ArrayList<>();
        // Đã tải lịch sử từ DB cho userId này chưa?
        public String loadedForUserId = null;

        public void trackClick(String productName) {
            if (productName == null || productName.isBlank())
                return;
            clickedProductNames.remove(productName); // tránh trùng
            clickedProductNames.addFirst(productName);
            if (clickedProductNames.size() > 20)
                clickedProductNames.removeLast();
        }

        /** Trích thương hiệu từ tên sản phẩm (token đầu tiên) */
        public static String extractBrand(String productName) {
            if (productName == null)
                return "";
            String[] parts = productName.trim().split("\\s+");
            return parts.length > 0 ? parts[0].toLowerCase() : "";
        }

        public boolean isEmpty() {
            return clickedProductNames.isEmpty() && purchasedBrands.isEmpty();
        }
    }

    /**
     * doGet: Xử lý tracking hành vi (click sản phẩm) từ frontend.
     * Gọi: GET /chat-bot?action=track&productId=123&productName=iPhone%2015
     * Response: "ok" hoặc "skip"
     */
    @Override
    protected void doGet(HttpServletRequest request, HttpServletResponse response)
            throws ServletException, IOException {
        response.setContentType("text/plain;charset=UTF-8");
        String action = request.getParameter("action");
        if ("track".equals(action)) {
            String productName = request.getParameter("productName");
            HttpSession session = request.getSession();
            UserMemory mem = getOrCreateMemory(session);
            mem.trackClick(productName);
            System.out.println("[PERSONALIZATION] Click tracked: " + productName
                    + " | Total clicks: " + mem.clickedProductNames.size());
            response.getWriter().write("ok");
        } else {
            response.getWriter().write("skip");
        }
    }

    private UserMemory getOrCreateMemory(HttpSession session) {
        UserMemory mem = (UserMemory) session.getAttribute("userMemory_v1");
        if (mem == null) {
            mem = new UserMemory();
            session.setAttribute("userMemory_v1", mem);
        }
        return mem;
    }

    @Override
    protected void doPost(HttpServletRequest request, HttpServletResponse response)
            throws ServletException, IOException {
        response.setContentType("text/plain;charset=UTF-8");
        HttpSession session = request.getSession();

        // Anti-spam Rate Limit User
        Long lastReq = (Long) session.getAttribute("lastChatReqTs");
        long now = System.currentTimeMillis();
        if (lastReq != null && now - lastReq < 1500) {
            response.getWriter().write("Hệ thống phát hiện spam. Vui lòng nhắn tin chậm lại vài giây nhé!");
            return;
        }
        session.setAttribute("lastChatReqTs", now);

        String message = request.getParameter("message");
        String mode = request.getParameter("mode");

        System.out.println("\n========== CHATBOT NEW REQUEST ==========");
        System.out.println("MODE: " + mode + " | MESSAGE: " + message);

        if (message == null || message.trim().isEmpty()) {
            response.getWriter().write("Bạn chưa nhập câu hỏi.");
            return;
        }

        if (message.length() > 500)
            message = message.substring(0, 500) + "...";

        try {
            String reply = processChat(request, message, mode);
            response.getWriter().write(reply);
        } catch (Exception e) {
            e.printStackTrace();
            response.getWriter().write("Hệ thống AI đang bảo trì hoặc bận xử lý, vui lòng thử lại sau.");
        }
    }

    private String processChat(HttpServletRequest request, String message, String mode) {
        if ("admin".equals(mode)) {
            return processAdminChat(request, message);
        } else {
            return processUserChat(request, message);
        }
    }

    private String processUserChat(HttpServletRequest request, String message) {
        HttpSession session = request.getSession();
        String sessionKey = "chatHistory_v4";
        @SuppressWarnings("unchecked")
        List<ChatMessage> history = (List<ChatMessage>) session.getAttribute(sessionKey);
        if (history == null) {
            history = new ArrayList<>();
        }

        // --- PERSONALIZATION: Load lịch sử mua hàng từ DB (1 lần / session) ---
        UserMemory userMemory = getOrCreateMemory(session);
        vn.edu.phoneshop.model.User loggedUser = (vn.edu.phoneshop.model.User) session.getAttribute("ACC");
        if (loggedUser != null) {
            String uid = String.valueOf(loggedUser.getUserID());
            if (!uid.equals(userMemory.loadedForUserId)) {
                loadPurchaseHistory(uid, userMemory);
                userMemory.loadedForUserId = uid;
                System.out.println("[PERSONALIZATION] Loaded purchase history for userId=" + uid
                        + " | brands=" + userMemory.purchasedBrands
                        + " | products=" + userMemory.purchasedProductNames);
            }
        }

        // 1. Phân tích Intent Nâng cao
        IntentData intent = analyzeIntent(message);

        System.out.println("--- PARSED INTENT ---");
        System.out.println("Giá: " + intent.minPrice + " - " + intent.maxPrice);
        System.out.println("RAM: " + intent.ram + " | ROM: " + intent.rom);
        System.out.println("Nhu cầu: Gaming=" + intent.isGaming + ", Camera=" + intent.isCamera + ", Battery="
                + intent.isBattery + ", Compare=" + intent.isComparison);
        System.out.println("Keywords: " + intent.keywords + " | ExactPhrase: " + intent.exactPhrase);

        // -- BỔ SUNG: KẾ THỪA INTENT (MEMORY) TỪ LẦN TRẦN CHUYỆN TRƯỚC --
        IntentData lastIntent = (IntentData) session.getAttribute("lastIntent_v4");
        if (lastIntent != null) {
            String msgLower = message.toLowerCase();
            // Cho phép người dùng reset bộ nhớ bằng các từ khoá tự nhiên
            if (!msgLower.contains("bỏ qua") && !msgLower.contains("thôi") && !msgLower.contains("hãng khác")) {
                if (intent.maxPrice == 0 && intent.minPrice == 0) {
                    intent.maxPrice = lastIntent.maxPrice;
                    intent.minPrice = lastIntent.minPrice;
                }
                if (intent.ram.isEmpty())
                    intent.ram = lastIntent.ram;
                if (intent.rom.isEmpty())
                    intent.rom = lastIntent.rom;

                if (!intent.isGaming)
                    intent.isGaming = lastIntent.isGaming;
                if (!intent.isCamera)
                    intent.isCamera = lastIntent.isCamera;
                if (!intent.isBattery)
                    intent.isBattery = lastIntent.isBattery;
                if (!intent.isComparison)
                    intent.isComparison = lastIntent.isComparison;
                if (!intent.isBuying)
                    intent.isBuying = lastIntent.isBuying;

                // Giữ tệp tìm kiếm cũ nếu hỏi thêm ("có mẫu nào", "còn con nào", "màu")
                if (intent.keywords.isEmpty() || msgLower.contains("mẫu nào") || msgLower.contains("con nào")
                        || msgLower.contains("khác")) {
                    for (String kw : lastIntent.keywords) {
                        if (!intent.keywords.contains(kw) && !kw.equals("mẫu") && !kw.equals("nào")
                                && !kw.equals("khác")) {
                            intent.keywords.add(kw);
                        }
                    }
                    if (intent.exactPhrase.isEmpty()) {
                        intent.exactPhrase = lastIntent.exactPhrase;
                    }
                }
            }
        }
        session.setAttribute("lastIntent_v4", intent);

        // 2. RAG Chuẩn xác (Hard Strict Filter & Cache Scoring)
        ProductDAO dao = new ProductDAO();
        List<Product> allProducts = dao.getAllActiveProducts();

        Map<Product, Integer> scoreMap = new HashMap<>();
        for (Product p : allProducts) {
            int base = scoreProduct(p, intent);
            int personal = scorePersonalization(p, userMemory); // +Personalization bonus
            scoreMap.put(p, base + personal);
        }

        List<Product> topProducts = allProducts.stream()
                .filter(p -> matchStrict(p, intent)) // Lọc cứng trước khi xếp điểm
                .filter(p -> scoreMap.get(p) > -50)
                .sorted((p1, p2) -> Integer.compare(scoreMap.get(p2), scoreMap.get(p1))) // Giảm dần
                .limit(8)
                .collect(Collectors.toList());

        System.out.println("--- SCORING RESULTS (TOP " + topProducts.size() + ") ---");
        for (Product p : topProducts) {
            System.out.println("  + ID:" + p.getProductID() + " | " + p.getProductName() + " | Score=" + scoreMap.get(p)
                    + " | RAM:" + p.getRam() + " | ROM:" + p.getRom() + " | Price:" + p.getPrice());
        }

        // Fallback RAG Rộng hơn (Nếu khách đòi cấu hình viển vông, tự nới budget/cấu
        // hình)
        if (topProducts.isEmpty() && (!intent.ram.isEmpty() || !intent.rom.isEmpty() || intent.maxPrice > 0)) {
            if (!intent.ram.isEmpty()) {
                intent.ram = ""; // Ưu tiên bỏ điều kiện RAM khắt khe
            } else if (!intent.rom.isEmpty()) {
                intent.rom = ""; // Hoặc bỏ ROM
            } else if (intent.maxPrice > 0) {
                intent.maxPrice = intent.maxPrice * 1.5; // Nới giá lên 1.5 lần nếu không có cấu hình
            }

            scoreMap.clear();
            for (Product p : allProducts) {
                int base = scoreProduct(p, intent);
                int personal = scorePersonalization(p, userMemory);
                scoreMap.put(p, base + personal);
            }

            topProducts = allProducts.stream()
                    .filter(p -> matchStrict(p, intent)) // Lọc lại theo đ/k nới lỏng
                    .filter(p -> scoreMap.get(p) > -50)
                    .sorted((p1, p2) -> Integer.compare(scoreMap.get(p2), scoreMap.get(p1)))
                    .limit(8)
                    .collect(Collectors.toList());

            System.out.println("--- FALLBACK RAG SCORING RESULTS (TOP " + topProducts.size() + ") ---");
            for (Product p : topProducts) {
                System.out.println(
                        "  + ID:" + p.getProductID() + " | " + p.getProductName() + " | Score=" + scoreMap.get(p)
                                + " | RAM:" + p.getRam() + " | ROM:" + p.getRom() + " | Price:" + p.getPrice());
            }
        }

        boolean isFinalFallback = false;
        if (topProducts.isEmpty()) {
            isFinalFallback = true;
            System.out.println("--- FINAL FALLBACK (NO STRICT FILTER) ---");
            topProducts = allProducts.stream()
                    .sorted((p1, p2) -> Integer.compare(scoreMap.get(p2), scoreMap.get(p1)))
                    .limit(5)
                    .collect(Collectors.toList());
        }

        boolean isComplex = intent.isComparison || message.length() > 100 || message.toLowerCase().contains("tại sao")
                || message.toLowerCase().contains("tư vấn kỹ");
        String targetModel = isComplex ? "gemini-2.5-pro" : "gemini-2.5-flash";

        // 3. Bypass AI hoàn toàn nếu đủ Intent (Đơn giản + Nhu cầu cao) - Giảm tải AI
        if (!isFinalFallback && !isComplex && intent.hasFullIntent() && !topProducts.isEmpty()) {
            System.out.println("--- BYPASSING AI (FULL INTENT) ---");
            StringBuilder directHtml = new StringBuilder();
            // Nếu user xin thêm model khác thì vẫn nhảy AI. Nếu ko, bypass luôn.
            if (!message.toLowerCase().contains("còn mẫu nào")) {
                directHtml.append(
                        "Dạ, dựa vào yêu cầu ngân sách và cấu hình anh/chị chọn, em thấy các mẫu máy cực chuẩn sau đây ạ:\n\n");
                for (Product p : topProducts) {
                    String pRam = p.getRam() != null ? p.getRam() : "N/A";
                    String pRom = p.getRom() != null ? p.getRom() : "N/A";
                    String encodedName = "";
                    try {
                        encodedName = java.net.URLEncoder.encode(p.getProductName(), "UTF-8");
                    } catch (Exception e) {
                    }
                    directHtml.append("- **[").append(p.getProductName()).append("](search?searchName=")
                            .append(encodedName).append(")** | Giá: ").append(String.format("%,.0f VNĐ", p.getPrice()))
                            .append(" | RAM: ").append(pRam).append(" | ROM: ").append(pRom).append("\n");
                }
                directHtml.append(
                        "\nAnh/chị ưng mẫu nào ở trên để em lên đơn hoặc cần em tư vấn sâu thêm về dòng nào ạ?");
                saveHistory(session, history, message, directHtml.toString(), sessionKey);
                return directHtml.toString();
            }
        }

        // 3.5. Bypass AI if specific product + Buying Intent
        if (!isFinalFallback && intent.isBuying && !topProducts.isEmpty()) {
            Product best = topProducts.get(0);
            // Nếu điểm số cao (đã tìm đúng máy) hoặc người dùng chốt con này (sau khi vừa
            // đc giới thiệu)
            if (scoreMap.get(best) >= 40 || message.toLowerCase().contains("chốt con này")) {
                System.out.println("--- BYPASSING AI (BUYING INTENT) ---");
                String encodedName = "";
                try {
                    encodedName = java.net.URLEncoder.encode(best.getProductName(), "UTF-8");
                } catch (Exception e) {
                }
                String directReply = "Dạ, em hiểu ạ. Anh/chị chốt sản phẩm **[" + best.getProductName()
                        + "](search?searchName=" + encodedName + ")** giá **"
                        + String.format("%,.0f VNĐ", best.getPrice()) + "** nhé. "
                        + "Để em xác nhận và gửi thông tin hướng dẫn thanh toán/giao hàng ngay cho anh/chị ạ!";
                saveHistory(session, history, message, directReply, sessionKey);
                return directReply;
            }
        }

        // 4. Intent Routing (Bypass AI giá cụ thể)
        boolean isPriceInquiry = message.toLowerCase().contains("giá") || message.toLowerCase().contains("bao nhiêu");
        if (!isFinalFallback && !isComplex && isPriceInquiry && topProducts.size() > 0
                && scoreMap.get(topProducts.get(0)) >= 20) {
            System.out.println("--- BYPASSING AI (PRICE INQUIRY EXACT MATCH) ---");
            Product best = topProducts.get(0);
            if (intent.keywords.stream().anyMatch(kw -> best.getProductName().toLowerCase().contains(kw))) {
                String encodedName = "";
                try {
                    encodedName = java.net.URLEncoder.encode(best.getProductName(), "UTF-8");
                } catch (Exception e) {
                }
                String directReply = "Dạ, mẫu **[" + best.getProductName() + "](search?searchName=" + encodedName
                        + ")** hiện có giá sập sàn là **" + String.format("%,.0f VNĐ", best.getPrice())
                        + "**. Máy cấu hình RAM " + best.getRam() + ", ROM " + best.getRom()
                        + ". Anh/chị xem có chốt gửi hàng không ạ?";
                saveHistory(session, history, message, directReply, sessionKey);
                return directReply;
            }
        }

        // 5. Intent Routing (Bypass AI chỉ tìm tên máy)
        boolean isOnlyName = intent.maxPrice == 0 && intent.minPrice == 0
                && intent.ram.isEmpty() && intent.rom.isEmpty()
                && !intent.isGaming && !intent.isCamera && !intent.isBattery
                && !intent.isComparison && !intent.exactPhrase.isEmpty()
                && !isPriceInquiry;

        if (!isFinalFallback && !isComplex && isOnlyName && topProducts.size() > 0
                && scoreMap.get(topProducts.get(0)) >= 20) {
            System.out.println("--- BYPASSING AI (ONLY NAME INQUIRY) ---");
            StringBuilder directReply = new StringBuilder();
            for (Product p : topProducts) {
                if (scoreMap.get(p) >= 5) {
                    String encodedName = "";
                    try {
                        encodedName = java.net.URLEncoder.encode(p.getProductName(), "UTF-8");
                    } catch (Exception e) {
                    }
                    directReply.append("- **[").append(p.getProductName()).append("](search?searchName=")
                            .append(encodedName).append(")**\n");
                }
            }
            if (directReply.length() > 0) {
                String replyStr = directReply.toString().trim();
                saveHistory(session, history, message, replyStr, sessionKey);
                return replyStr;
            }
        }

        // 6. Prompt Generation & System Instructions
        String contextData = buildProductContext(topProducts, isFinalFallback);
        String intentData = buildIntentContext(intent);
        String personalCtx = buildPersonalizationContext(userMemory);

        String systemRole = "Bạn là chuyên gia tư vấn Smartphone cao cấp của PhoneShop.\n" +
                "QUY TẮC NGHIÊM NGẶT (TUYỆT ĐỐI TUÂN THỦ):\n" +
                "1. DÙNG TIẾNG VIỆT tự nhiên, vô thẳng trọng tâm.\n" +
                "2. TUYỆT ĐỐI KHÔNG hỏi lại nhu cầu (ngân sách, RAM, ROM) vì Hệ thống đã phân tích rõ ở YÊU CẦU KHÁCH HÀNG. Đừng mở đầu chung chung ('Dạ em hiểu, anh cần máy dưới 7 triệu... Dạ rồi'). BỎ!!!\n"
                +
                "3. LÊN THẲNG GỢI Ý: Lấy NGAY LẬP TỨC 1 đến 3 mẫu điện thoại trong CONTEXT chốt sale luôn.\n" +
                "4. NẾU khách CHỈ yêu cầu TÊN SẢN PHẨM: CHỈ liệt kê TÊN SẢN PHẨM, TUYỆT ĐỐI KHÔNG kèm theo giá, cấu hình hay chào hỏi/giải thích thêm (trả về đúng tên thôi).\n"
                +
                "5. NO HALLUCINATION: Tuyệt đối không bịa điện thoại ngoài Context, không điêu cấu hình.\n" +
                "6. OUTPUT FORMAT: LUÔN LUÔN trả lời bằng Markdown chuẩn. Bắt buộc gắn link sản phẩm bằng dạng Markdown link như trong CONTEXT, ví dụ: **[Tên SP](search?searchName=tên_encode)**.\n"
                +
                "7. PERSONALIZATION: Nếu có hành vi khách (bên dưới), ưu tiên dòng máy/hãng khách yêu thích khi phù hợp.\n\n"
                +
                (personalCtx.isEmpty() ? "" : "--- HÀNH VI KHÁCH HÀNG (Personalization) ---\n" + personalCtx + "\n") +
                "--- YÊU CẦU ĐÃ PHÂN TÍCH TỪ KHÁCH HÀNG ---\n" + intentData + "\n" +
                "--- CONTEXT (Kho SP Tốt Nhất Khớp Thuật Toán) ---\n" + contextData
                + "\n--------------------------------\n";

        String jsonPayload = buildGeminiJsonPayload(systemRole, history, message);
        System.out.println("--- GEMINI AI CALL ---");
        System.out.println("Target Model: " + targetModel);
        System.out.println("JSON Payload: "
                + (jsonPayload.length() > 1000 ? jsonPayload.substring(0, 1000) + "... (truncated)" : jsonPayload));

        String reply = callGeminiJSON(jsonPayload, targetModel, true);

        // Khi tất cả model bị 429, tự build danh sách sản phẩm kèm thông báo
        if (SENTINEL_ALL_429.equals(reply)) {
            StringBuilder sb = new StringBuilder();
            sb.append("⚠️ **Máy chủ AI đang quá tải, em tạm thời không thể tư vấn chi tiết.** ");
            sb.append("Dưới đây là các sản phẩm phù hợp nhất với yêu cầu của anh/chị, anh/chị tham khảo nhé:\n\n");
            for (Product p : topProducts) {
                String pRam = p.getRam() != null ? p.getRam() : "";
                String pRom = p.getRom() != null ? p.getRom() : "";
                String encodedName = "";
                try {
                    encodedName = java.net.URLEncoder.encode(p.getProductName(), "UTF-8");
                } catch (Exception ignored) {
                }
                sb.append("- **[").append(p.getProductName()).append("](search?searchName=").append(encodedName)
                        .append(")** | ")
                        .append("Giá: ").append(String.format("%,.0f VNĐ", p.getPrice()));
                if (!pRam.isEmpty())
                    sb.append(" | RAM: ").append(pRam);
                if (!pRom.isEmpty())
                    sb.append(" | ROM: ").append(pRom);
                sb.append("\n");
            }
            sb.append("\n_Vui lòng thử lại sau 1 phút để được tư vấn đầy đủ hơn ạ._");
            reply = sb.toString();
        }

        // Post-process: gắn link trực tiếp vào tên sản phẩm nếu AI không tự làm
        reply = injectProductLinks(reply, topProducts);

        saveHistory(session, history, message, reply, sessionKey);
        return reply;
    }

    private String processAdminChat(HttpServletRequest request, String message) {
        HttpSession session = request.getSession();
        String sessionKey = "chatHistoryAdmin_v4";
        @SuppressWarnings("unchecked")
        List<ChatMessage> history = (List<ChatMessage>) session.getAttribute(sessionKey);
        if (history == null) {
            history = new ArrayList<>();
        }

        String contextData = getAdminContext(message);
        String systemRole = "Bạn là trợ lý AI (Read-Only) cho Admin PhoneShop.\n" +
                "QUY TẮC:\n" +
                "1. Phân tích Dữ liệu Context bên dưới.\n" +
                "2. TỪ CHỐI cập nhật CSDL(UPDATE/DELETE/INSERT).\n" +
                "3. Báo cáo bằng tiếng Việt mạch lạc cấu trúc chuẩn.\n" +
                "4. OUTPUT FORMAT: LUÔN LUÔN trả lời bằng Markdown (dùng bảng biểu, in đậm, gạch đầu dòng).\n\n" +
                "--- CONTEXT (SQL Data Fetch) ---\n" + contextData + "\n--------------------------------\n";

        String jsonPayload = buildGeminiJsonPayload(systemRole, history, message);
        String targetModel = message.length() > 100 ? "gemini-2.5-pro" : "gemini-2.5-flash";
        String reply = callGeminiJSON(jsonPayload, targetModel, true);

        saveHistory(session, history, message, reply, sessionKey);
        return reply;
    }

    // -- TEXT NORMALIZATION (NLP nhẹ cho Tiếng Việt thông tục) --
    private String normalizePriceText(String lower) {
        // --- Bước 1: Chuẩn hóa số viết tắt dân gian ---
        // "7 củ", "7 triệu", "7tr" giữ nguyên (đã có regex)
        // "1 lít" = 1 triệu (slang miền Nam)
        lower = lower.replaceAll("(\\d+(\\.\\d+)?)\\s*lít(?!r)", "$1tr");

        // "7 rưỡi", "7 triệu rưỡi", "7 củ rưỡi" -> "7.5tr"
        lower = lower.replaceAll("(\\d+)\\s*(?:tr|triệu|củ)?\\s*rưỡi", "$1.5tr");

        // "7tr5", "7 triệu 5" -> "7.5tr", "1tr2" -> "1.2tr", "1tr250" -> "1.250tr"
        lower = lower.replaceAll("(\\d+)\\s*(tr|triệu|củ)\\s*([1-9][0-9]*)\\b", "$1.$3tr");

        // --- Bước 2: Normalize "Xtr hơn xíu" → dải giá "Xtr-(X+1)tr" ---
        // Dùng Matcher để tính X+1 động (replaceAll không hỗ trợ tính toán nhóm)
        lower = lowerPlusOne(lower);

        // "trở lên / đổ lên" -> "trên X"
        lower = lower.replaceAll(
                "(\\d+(\\.\\d+)?)\\s*(tr|triệu|củ)\\s+(?:trở\\s+lên|đổ\\s+lên)",
                "trên $1$3 ");

        // "đổ lại / trở lại / trở xuống / đổ xuống / trở về / đua lại / đổ lại đây"
        lower = lower.replaceAll(
                "(\\d+(\\.\\d+)?)\\s*(tr|triệu|củ)\\s+(?:đổ(?!\\s+lên)|trở(?!\\s+lên)|đổ\\s+về|trở\\s+về|đổ\\s+xuống|trở\\s+xuống|trở\\s+lại|đổ\\s+lại)",
                "dưới $1$3 ");

        // "tầm tầm / tầm khoảng / khoảng chừng / khoảng tầm" → "khoảng"
        lower = lower.replaceAll("tầm\\s+tầm|tầm\\s+khoảng|khoảng\\s+chừng|khoảng\\s+tầm|tầm\\s+tiền", "khoảng");

        // "xíu / chút / tý / tí" sau con số (đã xử lý ở trên, xóa dư)
        lower = lower.replaceAll("(?<=[0-9])\\s+(?:xíu|tí|tý|chút)\\b", "");

        // --- Bước 3: Xử lý các con số mồ côi (naked numbers) ---
        // 3a. Số mồ côi đứng sau từ khóa giá cả ("tầm 12", "giá 8.5") -> "tầm 12tr",
        // "giá 8.5tr"
        // Loại trừ các số có đơn vị gb/ram/inch/Hz ngay sau đó
        lower = lower.replaceAll(
                "(?i)\\b(tầm|khoảng|dưới|trên|giá|cỡ|từ|chừng)\\s+(\\d+(\\.\\d+)?)(?!\\s*(gb|g|tb|ram|rom|tr|triệu|củ|inch|hz|%|px))\\b",
                "$1 $2tr");

        // 3b. Số mồ côi ngẫu nhiên (chỉ xuất hiện "12" hoặc "8.5") nằm trong khoảng 1
        // đến 99
        // Loại trừ nếu phía trước là tên hãng/dòng hoặc phía sau là đơn vị cấu hình /
        // model
        Pattern pNaked = Pattern.compile(
                "(?i)(\\b(?:ram|rom|iphone|samsung|galaxy|oppo|vivo|xiaomi|redmi|poco|realme|nokia|ít|nhiều|hơn|khoảng|tầm|giá|dưới|trên|cỡ|từ|chừng|dòng|mẫu|con|series|máy)\\s+)?(?<![\\w\\.])(\\d+(?:\\.\\d+)?)(?![\\w\\.])(?!\\s*(?:gb|g|tb|ram|rom|tr|triệu|củ|inch|hz|%|px|vnd|đ|k|pro|plus|max|ultra|mini|promax|lite|s|se)\\b)");
        Matcher mNaked = pNaked.matcher(lower);
        StringBuffer sbNaked = new StringBuffer();
        while (mNaked.find()) {
            String prefix = mNaked.group(1);
            String numberStr = mNaked.group(2);
            if (prefix != null && !prefix.trim().isEmpty()) {
                mNaked.appendReplacement(sbNaked, Matcher.quoteReplacement(mNaked.group(0)));
            } else {
                try {
                    double val = Double.parseDouble(numberStr);
                    // Giả định giá trị từ 1 đến 99 là triệu VNĐ (vd: 12 -> 12tr, 8.5 -> 8.5tr)
                    if (val >= 1 && val <= 99) {
                        mNaked.appendReplacement(sbNaked, Matcher.quoteReplacement(numberStr + "tr"));
                    } else {
                        mNaked.appendReplacement(sbNaked, Matcher.quoteReplacement(mNaked.group(0)));
                    }
                } catch (Exception e) {
                    mNaked.appendReplacement(sbNaked, Matcher.quoteReplacement(mNaked.group(0)));
                }
            }
        }
        mNaked.appendTail(sbNaked);
        lower = sbNaked.toString();

        return lower;
    }

    /** Xử lý pattern "Xtr hơn xíu" → minPrice=X, maxPrice=X+1 */
    private String lowerPlusOne(String lower) {
        Pattern p = Pattern.compile(
                "(\\d+(?:\\.\\d+)?)\\s*(?:tr|triệu|củ)?\\s+(?:hơn\\s*(?:xíu|chút|tí|tý|một chút)?|nhỉnh hơn)",
                Pattern.CASE_INSENSITIVE);
        Matcher m = p.matcher(lower);
        StringBuffer sb2 = new StringBuffer();
        while (m.find()) {
            double val = Double.parseDouble(m.group(1));
            // Tránh nhầm "ram 8 hơn" hay "rom 128 hơn" -> mặc định giá < 100
            if (m.group().toLowerCase().matches(".*(tr|triệu|củ).*") || val < 100) {
                double upperVal = val + 1;
                // Viết lại thành dải: "Xtr-Y.0tr" để rangePattern bắt được
                String replacement = val + "tr-" + upperVal + "tr";
                m.appendReplacement(sb2, Matcher.quoteReplacement(replacement));
            }
        }
        m.appendTail(sb2);
        return sb2.toString();
    }

    // -- SMART PARSING & RAG LÕI --
    private IntentData analyzeIntent(String msg) {
        IntentData data = new IntentData();
        // Normalize trước khi parse để hiểu tiếng Việt thông tục
        String lower = normalizePriceText(msg.toLowerCase());

        System.out.println("[NLP Normalize] Input: \"" + msg + "\" → \"" + lower + "\"");

        // --- Nhận dạng ý định So sánh (mở rộng) ---
        data.isComparison = lower.contains("so sánh") || lower.contains("vs")
                || lower.contains("hay là") || lower.contains("so với")
                || lower.contains("tốt hơn") || lower.contains("khác gì")
                || lower.contains("khác nhau") || lower.contains("nên chọn")
                || lower.contains("nên mua") || lower.contains("chọn cái nào")
                || lower.contains("cái nào tốt") || lower.contains("cái nào ngon")
                || lower.matches(".*giữa.*(và|vs|hay).*");

        data.isGaming = lower.contains("game") || lower.contains("pubg") || lower.contains("liên quân")
                || lower.contains("mượt") || lower.contains("fps") || lower.contains("gaming")
                || lower.contains("chơi game");
        data.isCamera = lower.contains("chụp ảnh") || lower.contains("camera") || lower.contains("quay video")
                || lower.contains("đẹp") || lower.contains("selfie") || lower.contains("zoom");
        data.isBattery = lower.contains("pin trâu") || lower.contains("pin") || lower.contains("sạc nhanh")
                || lower.contains("trâu") || lower.contains("dung lượng pin") || lower.contains("bền pin");
        data.isBuying = lower.contains("chốt") || lower.contains("mua") || lower.contains("lên đơn")
                || lower.contains("đặt hàng") || lower.contains("gửi hàng");

        // --- Nhận dạng "ngon rẻ" / "giá trị" → đánh dấu để scoring ưu tiên ---
        // Không dùng \b vì Unicode → kiểm tra thủ công
        boolean wantsValue = lower.contains("ngon rẻ") || lower.contains("ngon và rẻ")
                || lower.contains("rẻ mà ngon") || lower.contains("giá trị")
                || lower.contains("tiết kiệm") || lower.contains("phù hợp tầm tiền")
                || lower.contains("đáng tiền") || lower.contains("cost") || lower.contains("giá hợp lý");

        // --- Parse dải giá (range) ---
        Pattern rangePattern = Pattern
                .compile("(\\d+(\\.\\d+)?)\\s*(?:tr|triệu|củ)?\\s*(?:-|~|đến)\\s*(\\d+(\\.\\d+)?)\\s*(tr|triệu|củ)");
        Matcher mRange = rangePattern.matcher(lower);
        if (mRange.find()) {
            try {
                data.minPrice = Double.parseDouble(mRange.group(1)) * 1000000;
                data.maxPrice = Double.parseDouble(mRange.group(3)) * 1000000;
            } catch (Exception ignored) {
            }
        } else {
            // --- Parse giá đơn + modifier (dưới / trên / khoảng / tầm) ---
            Pattern pricePattern = Pattern.compile(
                    "(?:(dưới|dưới đây|tối đa|không quá|max)\\s*)?" +
                            "(\\d+(\\.\\d+)?)\\s*(tr|triệu|củ)" +
                            "(?:\\s*(trở lại|đổ lại|trở xuống|đổ xuống|trở về))?");
            Matcher mPrice = pricePattern.matcher(lower);
            if (mPrice.find()) {
                try {
                    double val = Double.parseDouble(mPrice.group(2)) * 1000000;
                    String prefix = mPrice.group(1); // "dưới" etc. (có thể null)
                    String suffix = mPrice.group(5); // "trở lại" etc. (có thể null)
                    boolean isUnder = (prefix != null) || (suffix != null)
                            || lower.contains("không quá") || lower.contains("tối đa")
                            || lower.contains("max");
                    boolean isOver = lower.matches(".*\\b(trên|từ|tối thiểu|ít nhất)\\b.*");

                    if (isOver) {
                        data.minPrice = val;
                        // maxPrice để 0 = không giới hạn trên
                    } else {
                        // "dưới 7tr", "tầm 7tr", "khoảng 7tr", "7tr đổ lại"
                        // Tất cả đều hiểu là maxPrice, minPrice=0 (không giới hạn dưới)
                        data.maxPrice = val;
                    }
                } catch (Exception ignored) {
                }
            }
        }

        Pattern ramPattern = Pattern.compile("(?:ram\\s*)?(\\d+)\\s*(?:gb|g)(?!\\s*rom)|ram\\s*(\\d+)");
        Matcher mRam = ramPattern.matcher(lower);
        if (mRam.find()) {
            data.ram = mRam.group(1) != null ? mRam.group(1) : mRam.group(2);
        }

        Pattern romPattern = Pattern.compile("(?:rom\\s*)?(\\d{2,4})\\s*(?:gb|g)|(\\d+)\\s*tb");
        Matcher mRom = romPattern.matcher(lower);
        if (mRom.find()) {
            data.rom = mRom.group(1) != null ? mRom.group(1) : mRom.group(2);
        }

        // --- Bước 1: Xóa các cụm từ chức năng bằng regex (ASCII word-boundary) ---
        String cleaned = lower.replaceAll(
                "\\b(tôi|mình|bạn|cần|muốn|giúp|với|xin|chào|giá|bao nhiêu|tiền|tầm|dưới|trên|khoảng|mua|tìm|lấy|điện|thoại|loại|cái|nào|so|sánh|tư|vấn|chụp|ảnh|game|chơi|qua|pin|trâu|sạc|nhanh|có|sản|phẩm|từ|đến|ko|không|nhỉ|ạ|cho|hỏi|vừa|cho nên|vs|hay là|so sánh|so với|nên mua|nên chọn)\\b",
                " ");
        // Xóa số + đơn vị giá/dung lượng
        cleaned = cleaned.replaceAll(
                "(\\d+(\\.\\d+)?)\\s*(?:tr|triệu|củ)?\\s*(?:-|~|đến)\\s*(\\d+(\\.\\d+)?)\\s*(tr|triệu|củ|gb|g|tb)", " ")
                .trim();
        cleaned = cleaned.replaceAll("(\\d+(\\.\\d+)?)\\s*(tr|triệu|củ|gb|g|tb)", " ").trim();
        cleaned = cleaned.replaceAll("ram\\s*\\d+|rom\\s*\\d+", " ").trim();
        cleaned = cleaned.replaceAll("\\s+", " ").trim();

        // --- Bước 2: Lọc token bằng Set stopwords Unicode ---
        // (cần thiết vì \b không hoạt động với ký tự Unicode có dấu
        // như "rẻ", "đẹp", "và", "hay" — \b chỉ nhận ASCII [a-zA-Z0-9_] là word boundary)
        Set<String> viStopwords = new HashSet<>(Arrays.asList(
                // Tính từ chất lượng / cảm tính — KHÔNG phải tên sản phẩm
                "ngon", "rẻ", "đẹp", "mạnh", "mượt", "tốt", "nhất", "trâu", "xịn", "ngầu",
                "chất", "ổn", "kém", "tệ", "ngon rẻ", "giá trị", "đáng tiền",
                // Liên từ / trợ từ
                "và", "hay", "hoặc", "cùng", "nên", "thì", "mà", "nhưng", "lại",
                "để", "vì", "nữa", "thêm", "vậy", "thế", "cũng", "còn", "với",
                // Từ chỉ mức độ
                "rất", "khá", "hơi", "quá", "thật", "thực", "siêu", "cực", "vô",
                "hẳn", "luôn", "thậm", "chí",
                // Đại từ / trợ từ hỏi
                "tôi", "mình", "bạn", "cần", "muốn", "giúp", "xin", "chào",
                "cho", "hỏi", "nào", "có", "không", "ko", "nhỉ", "ạ",
                // Động từ chức năng
                "mua", "tìm", "lấy", "gợi", "đề", "xuất", "giới", "thiệu",
                // Danh từ chung
                "điện", "thoại", "máy", "sản", "phẩm", "loại", "cái", "chiếc",
                "dòng", "mẫu", "con", "hàng", "giá",
                // Giới từ / liên từ phụ
                "từ", "đến", "tầm", "dưới", "trên", "khoảng", "về", "của", "trong",
                "ngoài", "trước", "sau", "bên", "phía",
                // Từ so sánh
                "so", "sánh", "vs", "hay", "khác",
                // Số từ
                "một", "hai", "ba", "bốn", "năm", "sáu", "bảy", "tám", "chín", "mười"));

        if (!cleaned.isEmpty()) {
            String[] tokens = cleaned.split("\\s+");
            List<String> filteredTokens = new ArrayList<>();
            for (String tok : tokens) {
                String t = tok.trim().toLowerCase();
                // Bỏ token quá ngắn (1 ký tự) hoặc là stopword
                if (!t.isEmpty() && t.length() > 1 && !viStopwords.contains(t)) {
                    filteredTokens.add(t);
                }
            }
            if (!filteredTokens.isEmpty()) {
                data.exactPhrase = String.join(" ", filteredTokens);
                data.keywords.addAll(filteredTokens);
            }
        }

        // --- Bước 3: Log wantsValue để debug (không cần lưu vào IntentData) ---
        System.out.println("[NLP] wantsValue=" + wantsValue + " | isComparison=" + data.isComparison);

        return data;
    }

    private boolean matchStrict(Product p, IntentData intent) {
        if (intent.maxPrice > 0) {
            if (intent.minPrice > 0) {
                // Trong tầm giá, filter nhẹ (tới 1.5x) để scoring quyết định
                if (p.getPrice() < intent.minPrice * 0.80 || p.getPrice() > intent.maxPrice * 1.50)
                    return false;
            } else {
                // Dưới ngân sách trần, filter nhẹ (tới 1.5x) để scoring quyết định
                if (p.getPrice() > intent.maxPrice * 1.50)
                    return false;
            }
        }

        // FIX: chỉ có minPrice (từ X trở lên)
        if (intent.minPrice > 0 && intent.maxPrice == 0) {
            if (p.getPrice() < intent.minPrice * 0.80) {
                return false;
            }
        }

        if (!intent.ram.isEmpty() && p.getRam() != null) {
            String pRam = p.getRam().toLowerCase().replaceAll("\\s+", "");
            boolean matchRam = pRam.matches(".*(?:^|\\D)" + intent.ram + "(?:g|gb)?(?:$|\\D).*");
            if (!matchRam)
                return false;
        }
        if (!intent.rom.isEmpty() && p.getRom() != null) {
            String pRom = p.getRom().toLowerCase().replaceAll("\\s+", "");
            boolean matchRom = pRom.matches(".*(?:^|\\D)" + intent.rom + "(?:g|gb|t|tb)?(?:$|\\D).*");
            if (!matchRom)
                return false;
        }
        return true;
    }

    private int scoreProduct(Product p, IntentData intent) {
        int score = 0;
        double pPrice = p.getPrice();

        // --- 1. PRICE SCORING (Proximity based) ---
        if (intent.maxPrice > 0) {
            if (intent.minPrice > 0) {
                // Range match
                if (pPrice >= intent.minPrice && pPrice <= intent.maxPrice) {
                    score += 20;
                    // Proximity within range (prefer higher end of range if user says "ngon/rẻ"
                    // implicitly)
                    score += (int) (10 * (pPrice / intent.maxPrice));
                }
            } else {
                // Up to maxPrice
                if (pPrice <= intent.maxPrice) {
                    score += 15;
                    // PROXIMITY WEIGHTING:
                    // If budget is 7M, a 6.5M phone is much better than a 400k phone.
                    // Bonus up to 15 points for being close to the budget.
                    double ratio = pPrice / intent.maxPrice;
                    score += (int) (15 * ratio);

                    if (pPrice < intent.maxPrice * 0.5)
                        score -= 20;
                } else if (pPrice <= intent.maxPrice * 1.15) {
                    score += 5; // Slight over-budget allowed but lower score
                } else {
                    score -= 10; // Over-budget > 15%, phạt điểm nhưng không loại hẳn để spec có thể bù đắp
                }
            }
        } else if (intent.minPrice > 0) {
            // Only minPrice (Từ X trở lên)
            if (pPrice >= intent.minPrice) {
                score += 20;
                // Proximity weighting: favor phones closer to minPrice so we don't jump to the
                // most expensive ones immediately
                double ratio = intent.minPrice / pPrice;
                score += (int) (15 * ratio);
            } else if (pPrice >= intent.minPrice * 0.8) {
                score += 5; // Slight under-budget allowed
            } else {
                score -= 20; // Too cheap
            }
        }

        // --- 2. SPEC WEIGHTING (Smart Choice) ---
        String r = p.getRam() != null ? p.getRam() : "0";
        int ramVal = 0;
        try {
            ramVal = Integer.parseInt(r.replaceAll("[^0-9]", ""));
        } catch (Exception ignored) {
        }

        if (intent.isGaming) {
            if (ramVal >= 12)
                score += 20;
            else if (ramVal >= 8)
                score += 10;
        }

        // Quality bonus (general)
        if (ramVal >= 8)
            score += 5;
        if (ramVal >= 4)
            score += 2;

        // --- 3. NAME MATCHING ---
        if (intent.exactPhrase != null && !intent.exactPhrase.isEmpty()) {
            String pname = p.getProductName().toLowerCase();
            boolean containsPhrase = pname.contains(intent.exactPhrase.toLowerCase());

            if (pname.equals(intent.exactPhrase.toLowerCase())) {
                score += 100;
            } else if (containsPhrase) {
                score += 50;
                if (pname.startsWith(intent.exactPhrase.toLowerCase()))
                    score += 20;
            } else {
                // Penalty only if exactPhrase looks like a specific brand/model
                if (intent.exactPhrase.toLowerCase().matches(
                        ".*(iphone|samsung|oppo|vivo|xiaomi|realme|nokia|poco|sony|ipad|macbook|laptop|watch).*")) {
                    score -= 40;
                }
            }

            // Keyword bonus
            if (containsPhrase || intent.exactPhrase.length() < 3) {
                List<String> pWords = Arrays.asList(pname.split("[\\s\\-\\_]+"));
                int matchCount = 0;
                for (String kw : intent.keywords) {
                    if (kw.length() >= 2) {
                        if (pWords.contains(kw)) {
                            matchCount++;
                            score += 8;
                        }
                    }
                }
                if (matchCount == intent.keywords.size() && matchCount > 1)
                    score += 15;
            }
        }
        return score;
    }

    /**
     * Cộng điểm cá nhân hóa dựa trên hành vi user:
     * - +8: sản phẩm cùng thương hiệu với sản phẩm đã click gần đây
     * - +12: sản phẩm cùng thương hiệu với thương hiệu đã mua
     * - +6: tên sản phẩm khớp một phần với sản phẩm đã xem/mua
     */
    private int scorePersonalization(Product p, UserMemory mem) {
        if (mem == null || mem.isEmpty())
            return 0;
        int bonus = 0;
        String pname = p.getProductName().toLowerCase();
        String pBrand = UserMemory.extractBrand(p.getProductName());

        // Bonus từ sản phẩm đã click xem
        for (String clicked : mem.clickedProductNames) {
            String cBrand = UserMemory.extractBrand(clicked);
            if (!pBrand.isEmpty() && pBrand.equals(cBrand)) {
                bonus += 8;
                break; // chỉ cộng 1 lần / thương hiệu click
            }
            if (pname.contains(clicked.toLowerCase().trim())) {
                bonus += 6;
                break;
            }
        }

        // Bonus từ thương hiệu đã mua
        for (String brand : mem.purchasedBrands) {
            if (!pBrand.isEmpty() && pBrand.equalsIgnoreCase(brand)) {
                bonus += 12;
                break;
            }
        }

        if (bonus > 0) {
            System.out.println("[PERSONALIZATION] Bonus +" + bonus + " for: " + p.getProductName());
        }
        return bonus;
    }

    /**
     * Load lịch sử mua hàng từ DB để điền vào UserMemory.
     * Truy vấn tên sản phẩm từ OrderDetails của user.
     */
    private void loadPurchaseHistory(String userId, UserMemory mem) {
        String sql = "SELECT DISTINCT p.ProductName " +
                "FROM OrderDetails od " +
                "JOIN Products p ON od.ProductID = p.ProductID " +
                "JOIN Orders o ON od.OrderID = o.OrderID " +
                "WHERE o.UserID = ? AND o.Status IN (1,2,3,4) " +
                "ORDER BY o.OrderDate DESC";
        try {
            class LocalDB extends vn.edu.phoneshop.utils.DBContext {
                public Connection getCon() throws Exception {
                    return getConnection();
                }
            }
            LocalDB db = new LocalDB();
            try (Connection conn = db.getCon();
                    PreparedStatement ps = conn.prepareStatement(sql)) {
                ps.setString(1, userId);
                try (ResultSet rs = ps.executeQuery()) {
                    while (rs.next()) {
                        String name = rs.getString("ProductName");
                        if (name != null && !name.isBlank()) {
                            mem.purchasedProductNames.add(name);
                            mem.purchasedBrands.add(UserMemory.extractBrand(name));
                        }
                    }
                }
            }
        } catch (Exception e) {
            System.err.println("[PERSONALIZATION] loadPurchaseHistory error: " + e.getMessage());
        }
    }

    /**
     * Tạo đoạn text ngắn mô tả hành vi user để inject vào System Prompt AI.
     */
    private String buildPersonalizationContext(UserMemory mem) {
        if (mem == null || mem.isEmpty())
            return "";
        StringBuilder sb = new StringBuilder();
        if (!mem.purchasedBrands.isEmpty()) {
            sb.append("- Khách đã từng mua: ")
                    .append(String.join(", ", mem.purchasedProductNames.stream().limit(3).collect(Collectors.toList())))
                    .append("\n");
            sb.append("- Hãng yêu thích (mua): ").append(String.join(", ", mem.purchasedBrands)).append("\n");
        }
        if (!mem.clickedProductNames.isEmpty()) {
            List<String> recent = new ArrayList<>(mem.clickedProductNames).subList(0,
                    Math.min(3, mem.clickedProductNames.size()));
            sb.append("- Sản phẩm xem gần đây: ").append(String.join(", ", recent)).append("\n");
        }
        return sb.toString();
    }

    private String buildIntentContext(IntentData intent) {
        StringBuilder sb = new StringBuilder();
        if (intent.maxPrice > 0) {
            sb.append("- Giá: ")
                    .append(intent.minPrice > 0 ? String.format("%,.0f đến ", intent.minPrice) : "Tối đa khoảng ")
                    .append(String.format("%,.0f VNĐ\n", intent.maxPrice));
        }
        if (!intent.ram.isEmpty())
            sb.append("- RAM cần: ").append(intent.ram).append("GB\n");
        if (!intent.rom.isEmpty())
            sb.append("- ROM cần: ").append(intent.rom).append("GB\n");
        if (intent.isGaming)
            sb.append("- Nhu cầu: Gaming / Chơi game mượt\n");
        if (intent.isCamera)
            sb.append("- Nhu cầu: Chụp ảnh đẹp / Quay camera\n");
        if (intent.isBattery)
            sb.append("- Nhu cầu: Pin trâu / Dung lượng cao / Sạc nhanh\n");
        if (intent.isBuying)
            sb.append("- Nhu cầu: Mua hàng / Chốt đơn / Lấy máy\n");
        if (!intent.exactPhrase.isEmpty()) {
            sb.append("- Dòng máy muốn tìm (Chuỗi cụm nguyên gốc): \"").append(intent.exactPhrase).append("\"\n");
        }
        return sb.length() == 0 ? "(Không yêu cầu đặc biệt, tư vấn tự chọn)\n" : sb.toString();
    }

    private String buildProductContext(List<Product> products, boolean isFinalFallback) {
        if (products == null || products.isEmpty())
            return "Cửa hàng đang cháy hàng dòng này, em xin lỗi ạ.";
        StringBuilder sb = new StringBuilder();
        if (isFinalFallback) {
            sb.append(
                    "[SYSTEM NOTE: Hiện hệ thống KHÔNG TÌM THẤY sản phẩm khớp chính xác yêu cầu của khách (có thể do cháy hàng hoặc ngân sách/cấu hình chưa phù hợp). BẠN PHẢI MỞ ĐẦU xin lỗi khéo léo (ví dụ: 'Dạ mẫu máy/yêu cầu anh/chị tìm bên em đang tạm hết hoặc chưa có sẵn'), SAU ĐÓ TƯ VẤN SANG CÁC SẢN PHẨM GỢI Ý THAY THẾ DƯỚI ĐÂY:]\n");
        }
        for (Product p : products) {
            // Chọn 1 điểm mạnh nổi bật nhất để tránh prompt quá dài
            String highlight = pickHighlight(p);
            String encodedName = "";
            try {
                encodedName = java.net.URLEncoder.encode(p.getProductName(), "UTF-8");
            } catch (Exception e) {
            }
            sb.append(String.format("-[ID:%d] **[%s](search?searchName=%s)** | Giá: %,.0f đ | %s\n",
                    p.getProductID(), p.getProductName(), encodedName, p.getPrice(), highlight));
        }
        return sb.toString();
    }

    /**
     * Trả về 1 điểm mạnh nổi bật nhất của sản phẩm (RAM lớn > ROM lớn > pin >
     * camera > mặc định ROM)
     */
    private String pickHighlight(Product p) {
        String ram = p.getRam() != null ? p.getRam().trim() : "";
        String rom = p.getRom() != null ? p.getRom().trim() : "";

        // Ưu tiên RAM cao (12GB trở lên)
        try {
            int ramVal = Integer.parseInt(ram.replaceAll("[^0-9]", ""));
            if (ramVal >= 12)
                return "RAM " + ram + " (hiệu năng mạnh)";
        } catch (Exception ignored) {
        }

        // ROM lớn (256GB trở lên)
        try {
            int romVal = Integer.parseInt(rom.replaceAll("[^0-9]", ""));
            if (romVal >= 256)
                return "Bộ nhớ " + rom;
        } catch (Exception ignored) {
        }

        // Fallback: ghi RAM + ROM ngắn gọn
        if (!ram.isEmpty() && !rom.isEmpty())
            return "RAM " + ram + " / " + rom;
        if (!rom.isEmpty())
            return "Bộ nhớ " + rom;
        if (!ram.isEmpty())
            return "RAM " + ram;
        return "Cấu hình cơ bản";
    }

    // -- NATIVE JSON GEMINI PAYLOAD & PARSER CHUẨN --
    private String buildGeminiJsonPayload(String systemInstruction, List<ChatMessage> history, String currentMessage) {
        StringBuilder json = new StringBuilder();
        json.append("{");
        json.append("\"system_instruction\": {\"parts\": [{\"text\": \"").append(escapeJson(systemInstruction))
                .append("\"}]},");

        json.append("\"contents\": [");
        for (int i = 0; i < history.size(); i++) {
            ChatMessage msg = history.get(i);
            json.append("{\"role\": \"").append(msg.role).append("\", \"parts\": [{\"text\": \"")
                    .append(escapeJson(msg.content)).append("\"}]}");
            json.append(",");
        }

        json.append("{\"role\": \"user\", \"parts\": [{\"text\": \"").append(escapeJson(currentMessage))
                .append("\"}]}");
        json.append("], ");

        json.append("\"generationConfig\": {\"temperature\": 0.3, \"maxOutputTokens\": 2048}");
        json.append("}");
        return json.toString();
    }

    private String escapeJson(String raw) {
        if (raw == null)
            return "";
        return raw.replace("\\", "\\\\").replace("\"", "\\\"").replace("\b", "\\b").replace("\f", "\\f")
                .replace("\n", "\\n").replace("\r", "\\r").replace("\t", "\\t");
    }

    private String extractRobustJsonText(String json) {
        System.out.println("=== AI RAW JSON RESPONSE ===");
        System.out.println(json != null && json.length() > 2000 ? json.substring(0, 2000) + "... (truncated)" : json);
        if (json == null || json.trim().isEmpty()) {
            return "[Lỗi Kết Nối AI]: Phản hồi từ Google rỗng (Empty Response).";
        }
        try {
            Gson gson = new Gson();
            JsonObject root = gson.fromJson(json, JsonObject.class);

            if (root == null) {
                return "[Lỗi Parse JSON]: JsonObject root là null.";
            }

            if (!root.has("candidates") || root.get("candidates").isJsonNull()) {
                if (root.has("error") && !root.get("error").isJsonNull()) {
                    return "[Lỗi AI]: " + root.getAsJsonObject("error").get("message").getAsString();
                }
                return "[Lỗi AI]: Dịch vụ không hồi báo candidates. API có thể đang lỗi.";
            }

            JsonArray candidates = root.getAsJsonArray("candidates");
            if (candidates == null || candidates.size() == 0) {
                return "[Lỗi AI]: Mảng candidates rỗng.";
            }

            StringBuilder finalText = new StringBuilder();

            for (JsonElement candidateElem : candidates) {
                if (!candidateElem.isJsonObject())
                    continue;
                JsonObject candidate = candidateElem.getAsJsonObject();

                if (candidate.has("content") && !candidate.get("content").isJsonNull()) {
                    JsonObject content = candidate.getAsJsonObject("content");
                    if (content.has("parts") && !content.get("parts").isJsonNull()) {
                        JsonArray parts = content.getAsJsonArray("parts");
                        for (JsonElement partElem : parts) {
                            if (!partElem.isJsonObject())
                                continue;
                            JsonObject part = partElem.getAsJsonObject();
                            if (part.has("text") && !part.get("text").isJsonNull()) {
                                finalText.append(part.get("text").getAsString());
                            }
                        }
                    }
                }
            }

            if (finalText.length() == 0) {
                JsonObject firstCandidate = candidates.get(0).getAsJsonObject();
                if (firstCandidate.has("finishReason") && !firstCandidate.get("finishReason").isJsonNull()) {
                    String reason = firstCandidate.get("finishReason").getAsString();
                    if ("SAFETY".equalsIgnoreCase(reason)) {
                        return "Xin lỗi, AI không thể trả lời câu hỏi này vì lý do an toàn và nhạy cảm.";
                    }
                }
                return "[Lỗi Cấu Trúc AI]: Phản hồi bị cụt hoặc null định dạng.";
            }

            return finalText.toString().trim();
        } catch (Exception e) {
            System.err.println("=== AI JSON PARSE ERROR ===");
            e.printStackTrace();
            return "[Lỗi Parse JSON]: Định dạng JSON trả về không hợp lệ. Nguyên nhân: " + e.getMessage();
        }
    }

    private void saveHistory(HttpSession session, List<ChatMessage> history, String userMsg, String aiReply,
            String roleKey) {
        // Tối ưu RAM (Fix Session Bloat khi có nhiều user)
        // TODO: (Production) Chuyển sang lưu DB / Redis (theo SessionID) để giải phóng
        // hoàn toàn Session Memory

        String memUser = userMsg;
        if (memUser != null && memUser.length() > 200) {
            memUser = memUser.substring(0, 200) + "...";
        }

        String memAi = aiReply;
        // AI có thể trả lời rất dài, cắt ngắn để tối ưu bộ nhớ. 500 ký tự là đủ để AI
        // giữ context.
        if (memAi != null && memAi.length() > 500) {
            memAi = memAi.substring(0, 500) + "...";
        }

        history.add(new ChatMessage("user", memUser));
        history.add(new ChatMessage("model", memAi)); // model role in gemini JSON

        // Giảm từ 10 xuống 6 để tiết kiệm thêm RAM (giữ 3 lượt Q&A gần nhất)
        if (history.size() > 6) {
            history = new ArrayList<>(history.subList(history.size() - 6, history.size()));
        }
        session.setAttribute(roleKey, history);
    }

    private List<String> getApiKeys() {
        String keysEnv = System.getenv("GEMINI_API_KEY");
        if (keysEnv == null || keysEnv.isEmpty()) {
            return Collections.emptyList();
        }
        // Trim từng key để tránh khoảng trắng thừa khi set env
        return Arrays.stream(keysEnv.split(","))
                .map(String::trim)
                .filter(k -> !k.isEmpty())
                .collect(Collectors.toList());
    }

    /** Tạo composite key "apiKey::model" cho cooldown map */
    private String buildCooldownKey(String apiKey, String model) {
        return apiKey + "::" + model;
    }

    /**
     * Gọi Gemini API với chiến lược fallback thông minh:
     * - Vòng lặp model-first × key: thử tất cả key ở model nhỏ trước,
     * rồi mới leo lên model lớn hơn.
     * - Cooldown per (key, model): key A bị 429 ở model M1 KHÔNG ảnh hưởng
     * đến việc dùng key A ở model M2.
     * - Không dừng sớm: chỉ trả SENTINEL khi đã hết TẤT CẢ tổ hợp (key × model).
     *
     * Thứ tự model (nhỏ → to) tiết kiệm quota:
     * lite → standard-flash → preferred → (pro nếu cần)
     */
    private String callGeminiJSON(String jsonPayload, String preferredModel, boolean allowFallback) {
        // --- Xây dựng danh sách model theo thứ tự ưu tiên ---
        // Nguyên tắc: model nhỏ (lite) trước để tiết kiệm quota,
        // leo dần lên model preferred/pro khi các model nhỏ hết quota.
        List<String> modelList = new ArrayList<>();
        if (allowFallback) {
            // Thêm các model lite trước (ít tốn quota nhất)
            if (!"gemini-2.0-flash-lite".equals(preferredModel))
                modelList.add("gemini-2.0-flash-lite");
            if (!"gemini-2.5-flash-lite".equals(preferredModel))
                modelList.add("gemini-2.5-flash-lite");
            // Thêm standard flash
            if (!"gemini-2.0-flash".equals(preferredModel))
                modelList.add("gemini-2.0-flash");
            if (!"gemini-2.5-flash".equals(preferredModel))
                modelList.add("gemini-2.5-flash");
            // Preferred model luôn nằm CUỐI cùng trong danh sách lite→standard,
            // nhưng nếu preferred là pro thì nó nằm sau tất cả flash.
            if (!modelList.contains(preferredModel))
                modelList.add(preferredModel);
        } else {
            modelList.add(preferredModel);
        }

        List<String> allKeys = getApiKeys();
        if (allKeys.isEmpty()) {
            System.err.println("[GEMINI] No API keys configured (GEMINI_API_KEY env not set).");
            return SENTINEL_ALL_429;
        }

        long now = System.currentTimeMillis();

        // --- Vòng lặp model-first × key ---
        // Với mỗi model, shuffle key và thử tất cả key chưa bị cooldown.
        // Chỉ nhảy sang model lớn hơn khi TẤT CẢ key đều bị 429 ở model hiện tại.
        for (String model : modelList) {
            // Shuffle key để phân tải đều giữa các key
            List<String> shuffledKeys = new ArrayList<>(allKeys);
            Collections.shuffle(shuffledKeys);

            for (String apiKey : shuffledKeys) {
                String cdKey = buildCooldownKey(apiKey, model);
                Long cooldownEnd = keyCooldowns.get(cdKey);
                if (cooldownEnd != null && now < cooldownEnd) {
                    // Key này đang cooldown VỚI model này → skip, thử key khác
                    System.out.println(
                            "[GEMINI] Skip " + model + " key=..." + apiKey.substring(Math.max(0, apiKey.length() - 6))
                                    + " (cooldown " + ((cooldownEnd - now) / 1000) + "s left)");
                    continue;
                }

                String apiUrl = "https://generativelanguage.googleapis.com/v1beta/models/" + model
                        + ":generateContent?key=" + apiKey;
                try {
                    URL url = new URL(apiUrl);
                    HttpURLConnection conn = (HttpURLConnection) url.openConnection();
                    conn.setRequestMethod("POST");
                    conn.setRequestProperty("Content-Type", "application/json; charset=UTF-8");
                    conn.setConnectTimeout(8000);
                    conn.setReadTimeout(25000);
                    conn.setDoOutput(true);

                    try (OutputStream os = conn.getOutputStream()) {
                        byte[] input = jsonPayload.getBytes(StandardCharsets.UTF_8);
                        os.write(input, 0, input.length);
                    }

                    int code = conn.getResponseCode();
                    System.out.println("[GEMINI] HTTP " + code + " | model=" + model
                            + " | key=..." + apiKey.substring(Math.max(0, apiKey.length() - 6)));

                    if (code == 200) {
                        try (BufferedReader br = new BufferedReader(
                                new InputStreamReader(conn.getInputStream(), StandardCharsets.UTF_8))) {
                            StringBuilder sb = new StringBuilder();
                            String line;
                            while ((line = br.readLine()) != null)
                                sb.append(line);
                            return extractRobustJsonText(sb.toString());
                        }
                    } else if (code == 429) {
                        // Chỉ cooldown TỔ HỢP (key + model) này, không ảnh hưởng key ở model khác
                        keyCooldowns.put(cdKey, System.currentTimeMillis() + COOLDOWN_TIME);
                        System.err.println("[GEMINI] 429 RateLimit: " + model
                                + " key=..." + apiKey.substring(Math.max(0, apiKey.length() - 6))
                                + " → cooldown " + (COOLDOWN_TIME / 1000) + "s");
                        // continue → thử key tiếp theo cùng model
                    } else if (code == 404) {
                        // Model không tồn tại → không thử key khác ở model này, nhảy ngay model tiếp
                        System.err
                                .println("[GEMINI] 404 Model not found: " + model + " → skip all keys for this model");
                        break; // break inner key-loop, continue model-loop
                    } else if (code >= 500) {
                        // Lỗi server tạm thời → thử key khác (không cooldown)
                        System.err.println("[GEMINI] " + code + " Server Error: " + model + " → try next key");
                    } else {
                        // 400 bad request, 403 permission, v.v. → bỏ qua key này
                        System.err.println("[GEMINI] HTTP " + code + " from " + model + " → try next key");
                    }
                } catch (Exception e) {
                    System.err.println("[GEMINI] Network error on " + model + ": " + e.getMessage());
                    // Không cooldown khi lỗi mạng, thử key khác
                }
            } // end key loop
        } // end model loop

        // Tất cả tổ hợp (key × model) đã thử hết → trả sentinel
        System.err.println("[GEMINI] SENTINEL: All (key × model) combinations exhausted or rate-limited.");
        return SENTINEL_ALL_429;
    }

    private String getAdminContext(String message) {
        StringBuilder sb = new StringBuilder();
        String lowerCaseMessage = (message != null) ? message.toLowerCase() : "";

        boolean hasKeywords = lowerCaseMessage.contains("khách") || lowerCaseMessage.contains("user")
                || lowerCaseMessage.contains("người dùng")
                || lowerCaseMessage.contains("đơn") || lowerCaseMessage.contains("order")
                || lowerCaseMessage.contains("sản phẩm") || lowerCaseMessage.contains("product")
                || lowerCaseMessage.contains("kho") || lowerCaseMessage.contains("hết hàng");

        try {
            class AdminDB extends vn.edu.phoneshop.utils.DBContext {
                public java.sql.Connection getDbConnection() throws Exception {
                    return getConnection();
                }
            }
            AdminDB db = new AdminDB();
            try (java.sql.Connection conn = db.getDbConnection()) {
                if (!hasKeywords || lowerCaseMessage.contains("khách")) {
                    sb.append("DANH SÁCH 5 KHÁCH HÀNG:\n");
                    try (java.sql.Statement stmt = conn.createStatement();
                            java.sql.ResultSet rs = stmt.executeQuery(
                                    "SELECT TOP 5 UserID, FullName, Email, PhoneNumber FROM Users ORDER BY UserID DESC")) {
                        while (rs.next())
                            sb.append(String.format(" + ID: %s | Tên: %s | SĐT: %s\n", rs.getString("UserID"),
                                    rs.getString("FullName"), rs.getString("PhoneNumber")));
                    } catch (Exception e) {
                    }
                }
                if (!hasKeywords || lowerCaseMessage.contains("đơn")) {
                    sb.append("\nDANH SÁCH 5 ĐƠN HÀNG:\n");
                    try (java.sql.Statement stmt = conn.createStatement();
                            java.sql.ResultSet rs = stmt.executeQuery(
                                    "SELECT TOP 5 o.OrderID, u.FullName, o.TotalMoney FROM Orders o JOIN Users u ON o.UserID = u.UserID ORDER BY o.OrderDate DESC")) {
                        while (rs.next())
                            sb.append(String.format(" + Đơn ID: %d | KH: %s | Trị giá: %,.0f\n", rs.getInt("OrderID"),
                                    rs.getString("FullName"), rs.getDouble("TotalMoney")));
                    } catch (Exception e) {
                    }
                }
                if (!hasKeywords || lowerCaseMessage.contains("kho") || lowerCaseMessage.contains("sản phẩm")) {
                    sb.append("\nDANH SÁCH 5 SẢN PHẨM KHAN HÀNG:\n");
                    try (java.sql.Statement stmt = conn.createStatement();
                            java.sql.ResultSet rs = stmt.executeQuery(
                                    "SELECT TOP 5 ProductID, ProductName, StockQuantity FROM Products WHERE Status = 1 ORDER BY StockQuantity ASC")) {
                        while (rs.next())
                            sb.append(String.format(" + SP ID: %d | %s | Tồn: %d\n", rs.getInt("ProductID"),
                                    rs.getString("ProductName"), rs.getInt("StockQuantity")));
                    } catch (Exception e) {
                    }
                }
            }
        } catch (Exception e) {
        }
        return sb.toString();
    }

    private String injectProductLinks(String reply, List<Product> products) {
        if (reply == null || products == null || products.isEmpty())
            return reply;

        // Sắp xếp sản phẩm theo độ dài tên giảm dần để tránh match sai
        List<Product> sorted = new ArrayList<>(products);
        sorted.sort((a, b) -> b.getProductName().length() - a.getProductName().length());

        for (Product p : sorted) {
            String name = p.getProductName();
            if (name == null || name.isBlank())
                continue;

            String encodedName = "";
            try {
                encodedName = java.net.URLEncoder.encode(name, "UTF-8");
            } catch (Exception ignored) {
            }

            String linkMarkdown = "**[" + name + "](search?searchName=" + encodedName + ")**";

            // Kiểm tra nếu reply đã chứa URL tìm kiếm sản phẩm này thì bỏ qua
            if (reply.contains("searchName=" + encodedName))
                continue;

            String escaped = Pattern.quote(name);

            // Regex an toàn:
            // 1. (?<![\p{L}\p{N}]) và (?![\p{L}\p{N}]): Đảm bảo match nguyên cụm từ (word
            // boundary hỗ trợ Unicode).
            // 2. (?![^\[]*\]): Đảm bảo không nằm trong ngoặc vuông (tránh thay thế text của
            // link có sẵn hoặc double link).
            // 3. (?![^\(]*\)): Đảm bảo không nằm trong ngoặc tròn (tránh phá vỡ nội dung
            // URL).
            String regex = "(?i)(?<![\\p{L}\\p{N}])" + escaped + "(?![\\p{L}\\p{N}])(?![^\\[]*\\])(?![^\\(]*\\))";

            reply = reply.replaceAll(regex, Matcher.quoteReplacement(linkMarkdown));
        }
        return reply;
    }
}