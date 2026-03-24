package vn.edu.phoneshop.controller;

import jakarta.servlet.ServletException;
import jakarta.servlet.annotation.WebServlet;
import jakarta.servlet.http.HttpServlet;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;
import jakarta.servlet.http.HttpSession;

import java.io.IOException;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.stream.Collectors;

import vn.edu.phoneshop.bot.BotApiUtils;
import vn.edu.phoneshop.bot.BotNLPUtils;
import vn.edu.phoneshop.bot.BotPromptUtils;
import vn.edu.phoneshop.bot.BotRAGUtils;
import vn.edu.phoneshop.dao.ProductDAO;
import vn.edu.phoneshop.model.Product;
import vn.edu.phoneshop.model.bot.ChatMessage;
import vn.edu.phoneshop.model.bot.IntentData;
import vn.edu.phoneshop.model.bot.UserMemory;

@WebServlet(name = "ChatBotServlet", urlPatterns = { "/chat-bot" })
public class ChatBotServlet extends HttpServlet {

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

        String message = request.getParameter("message");
        String mode = request.getParameter("mode");

        // Anti-spam Rate Limit: chỉ áp dụng cho user chat, KHÔNG áp dụng cho admin
        if (!"admin".equals(mode)) {
            Long lastReq = (Long) session.getAttribute("lastChatReqTs");
            long now = System.currentTimeMillis();
            if (lastReq != null && now - lastReq < 1500) {
                response.getWriter().write("Hệ thống phát hiện spam. Vui lòng nhắn tin chậm lại vài giây nhé!");
                return;
            }
            session.setAttribute("lastChatReqTs", now);
        }

        System.out.println("\n========== CHATBOT NEW REQUEST ==========");
        System.out.println("MODE: " + mode + " | MESSAGE: " + message);

        if (message == null || message.trim().isEmpty()) {
            response.getWriter().write("Bạn chưa nhập câu hỏi.");
            return;
        }

        // Admin mode cần prompt dài hơn (chứa dữ liệu ngày/tháng), nâng limit lên 3000
        if ("admin".equals(mode)) {
            if (message.length() > 3000)
                message = message.substring(0, 3000) + "...";
        } else {
            if (message.length() > 500)
                message = message.substring(0, 500) + "...";
        }

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
                BotRAGUtils.loadPurchaseHistory(uid, userMemory);
                userMemory.loadedForUserId = uid;
                System.out.println("[PERSONALIZATION] Loaded purchase history for userId=" + uid
                        + " | brands=" + userMemory.purchasedBrands
                        + " | products=" + userMemory.purchasedProductNames);
            }
        }

        // 1. Phân tích Intent Nâng cao
        IntentData intent = BotNLPUtils.analyzeIntent(message);

        System.out.println("--- PARSED INTENT ---");
        System.out.println("Giá: " + intent.minPrice + " - " + intent.maxPrice);
        System.out.println("RAM: " + intent.ram + " | ROM: " + intent.rom);
        System.out.println("Nhu cầu: Gaming=" + intent.isGaming + ", Camera=" + intent.isCamera + ", Battery="
                + intent.isBattery + ", Compare=" + intent.isComparison);
        System.out.println("Keywords: " + intent.keywords + " | ExactPhrase: " + intent.exactPhrase);

        // --- KIỂM TRA SẢN PHẨM KHÔNG LIÊN QUAN (NGOÀI LỀ) ---
        if (message.toLowerCase().matches(".*(máy giặt|tủ lạnh|tivi|ti vi|điều hòa|máy lạnh|quạt|lò vi sóng|máy sấy|nồi cơm|bếp|máy hút bụi|laptop|máy tính|chuột|bàn phím|rạp|phim rạp|chiếu phim|đi rạp|xem phim thì|quần áo|giày|dép|mỹ phẩm|xe máy|ô tô|nhà hàng|khách sạn|du lịch|đồ ăn|thức uống|thời trang|đồng hồ|vé số|bún|phở|xổ số|đánh đề).*")) {
            String rejectReply = "Dạ, hiện tại cửa hàng bên em chỉ chuyên cung cấp các dòng **điện thoại thông minh** thôi ạ. Mong anh/chị thông cảm và tham khảo các mẫu điện thoại bên em nhé!";
            saveHistory(session, history, message, rejectReply, sessionKey);
            return rejectReply;
        }

        // --- KIỂM TRA HỎI VỀ GIẢM GIÁ / ƯU ĐÃI / ĐIỂM ---
        if (message.toLowerCase().matches(".*(giảm giá|ưu đãi|khuyến mãi|sale|điểm).*")) {
            String discountReply = "Dạ, cửa hàng đang có giảm giá theo bậc thành viên có thể giảm tới 15% (mức giảm giá cụ thể đã được ghi trong hồ sơ người dùng của anh/chị) ạ. Lưu ý điểm tích lũy sẽ reset sau khi sang năm mới. Anh/chị cần em hỗ trợ tư vấn mẫu máy nào không ạ?";
            saveHistory(session, history, message, discountReply, sessionKey);
            return discountReply;
        }

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

        // --- BỔ SUNG YÊU CẦU: Nếu không lọc được bất kỳ từ khóa nào, gửi thẳng cho AI xử lý ---
        boolean isIntentEmpty = intent.minPrice == 0 && intent.maxPrice == 0 &&
                intent.ram.isEmpty() && intent.rom.isEmpty() &&
                !intent.isGaming && !intent.isCamera && !intent.isBattery &&
                !intent.isComparison && !intent.isBuying &&
                intent.keywords.isEmpty() &&
                intent.requestedBrand == null &&
                intent.comparisonBrands.isEmpty();

        if (isIntentEmpty) {
            System.out.println("--- BYPASSING RAG & INTENT (EMPTY INTENT - RAW MESSAGE) ---");
            String systemRole = "Bạn là trợ lý AI thông minh và thân thiện của PhoneShop. Hãy trò chuyện, trả lời trực tiếp tin nhắn của khách hàng một cách tự nhiên. Nếu khách hàng chỉ chào hỏi, hãy chào lại và đề nghị tư vấn điện thoại. Nếu khách hàng hỏi chuyện ngoài lề, hãy trả lời vui vẻ và duyên dáng. KHÔNG bịa đặt thông số điện thoại.";
            String targetModel = message.length() > 100 ? "gemini-2.5-pro" : "gemini-2.5-flash";
            String jsonPayload = BotApiUtils.buildGeminiJsonPayload(systemRole, history, message);
            String directReply = BotApiUtils.callGeminiJSON(jsonPayload, targetModel, true);
            saveHistory(session, history, message, directReply, sessionKey);
            return directReply;
        }

        // 2. RAG Chuẩn xác (Hard Strict Filter & Cache Scoring)
        ProductDAO dao = new ProductDAO();
        List<Product> allProducts = dao.getAllActiveProducts();

        Map<Product, Integer> scoreMap = new HashMap<>();
        for (Product p : allProducts) {
            int base = BotRAGUtils.scoreProduct(p, intent);
            int personal = BotRAGUtils.scorePersonalization(p, userMemory); // +Personalization bonus
            scoreMap.put(p, base + personal);
        }

        List<Product> topProducts;
        if (intent.isComparison && intent.comparisonBrands.size() >= 2) {
            // SO SÁNH NHIỀU BRAND → chia slot đều để mỗi brand đều có đại diện
            int slotsPerBrand = Math.max(2, 8 / intent.comparisonBrands.size());
            topProducts = new ArrayList<>();
            for (String brand : intent.comparisonBrands) {
                final String b = brand.toLowerCase();
                List<Product> brandProducts = allProducts.stream()
                        .filter(p -> BotRAGUtils.matchStrict(p, intent))
                        .filter(p -> scoreMap.get(p) > -50)
                        .filter(p -> {
                            String pname = p.getProductName().toLowerCase();
                            if (b.equals("iphone") || b.equals("apple"))
                                return pname.contains("iphone") || pname.startsWith("apple");
                            if (b.equals("samsung") || b.equals("galaxy"))
                                return pname.contains("samsung") || pname.contains("galaxy");
                            return pname.contains(b);
                        })
                        .sorted((p1, p2) -> Integer.compare(scoreMap.get(p2), scoreMap.get(p1)))
                        .limit(slotsPerBrand)
                        .collect(Collectors.toList());
                topProducts.addAll(brandProducts);
                System.out.println("[COMPARISON] Brand=" + brand + " → " + brandProducts.size() + " products");
            }
            // Sắp xếp lại theo score để AI đọc nhất quán
            topProducts.sort((p1, p2) -> Integer.compare(scoreMap.get(p2), scoreMap.get(p1)));
        } else {
            topProducts = allProducts.stream()
                    .filter(p -> BotRAGUtils.matchStrict(p, intent))
                    .filter(p -> scoreMap.get(p) > -50)
                    .sorted((p1, p2) -> Integer.compare(scoreMap.get(p2), scoreMap.get(p1)))
                    .limit(8)
                    .collect(Collectors.toList());
        }

        System.out.println("--- SCORING RESULTS (TOP " + topProducts.size() + ") ---");
        for (Product p : topProducts) {
            System.out.println("  + ID:" + p.getProductID() + " | " + p.getProductName() + " | Score=" + scoreMap.get(p)
                    + " | RAM:" + p.getRam() + " | ROM:" + p.getRom() + " | Price:" + p.getPrice());
        }

        // --- KIỂM TRA SO SÁNH NHIỀU BRAND: Brand nào không có trong kho? ---
        if (intent.isComparison && intent.comparisonBrands.size() >= 2) {
            List<String> missingBrands = new ArrayList<>();
            List<String> availableBrands = new ArrayList<>();
            for (String brand : intent.comparisonBrands) {
                String b = brand.toLowerCase();
                boolean hasBrand = allProducts.stream().anyMatch(p -> {
                    String pname = p.getProductName().toLowerCase();
                    if (b.equals("iphone") || b.equals("apple"))
                        return pname.contains("iphone") || pname.startsWith("apple");
                    if (b.equals("samsung") || b.equals("galaxy"))
                        return pname.contains("samsung") || pname.contains("galaxy");
                    return pname.contains(b);
                });
                if (hasBrand) {
                    availableBrands.add(brand);
                } else {
                    missingBrands.add(brand);
                }
            }

            if (!missingBrands.isEmpty()) {
                // Có ít nhất 1 brand không có trong kho → thông báo rõ ràng
                StringBuilder compMsg = new StringBuilder();
                for (String mb : missingBrands) {
                    String display = mb.substring(0, 1).toUpperCase() + mb.substring(1);
                    compMsg.append("Dạ, rất tiếc hiện tại PhoneShop chưa kinh doanh dòng **")
                            .append(display).append("** ạ. ");
                }
                compMsg.append("\n\n");

                if (!availableBrands.isEmpty()) {
                    // Có ít nhất 1 brand trong kho → gợi ý sản phẩm của brand đó
                    String avail = availableBrands.get(0);
                    String availDisplay = avail.substring(0, 1).toUpperCase() + avail.substring(1);
                    compMsg.append("Hiện PhoneShop chỉ đang kinh doanh **").append(availDisplay)
                            .append("** và các thương hiệu Android khác. Anh/chị có muốn em tư vấn các mẫu **")
                            .append(availDisplay).append("** tốt nhất hiện tại không ạ?\n\n");

                    // Gợi ý 4 sản phẩm đại diện của brand còn lại
                    String availBrandKey = avail.toLowerCase();
                    List<Product> availProducts = allProducts.stream()
                            .filter(p -> {
                                String pname = p.getProductName().toLowerCase();
                                if (availBrandKey.equals("samsung") || availBrandKey.equals("galaxy"))
                                    return pname.contains("samsung") || pname.contains("galaxy");
                                return pname.contains(availBrandKey);
                            })
                            .sorted((p1, p2) -> Double.compare(p2.getPrice(), p1.getPrice()))
                            .limit(4)
                            .collect(Collectors.toList());

                    if (!availProducts.isEmpty()) {
                        compMsg.append("**Một số mẫu nổi bật của ").append(availDisplay).append(":**\n\n");
                        for (Product p : availProducts) {
                            compMsg.append("- **[").append(p.getProductName()).append("](detail?id=")
                                    .append(p.getProductID()).append(")** | Giá: ")
                                    .append(String.format("%,.0f VNĐ", p.getPrice()));
                            if (p.getRam() != null) compMsg.append(" | RAM: ").append(p.getRam());
                            if (p.getRom() != null) compMsg.append(" | ROM: ").append(p.getRom());
                            compMsg.append("\n");
                        }
                    }
                } else {
                    compMsg.append("Anh/chị có muốn em gợi ý các dòng điện thoại khác đang có tại PhoneShop không ạ?");
                }

                String compStr = compMsg.toString();
                saveHistory(session, history, message, compStr, sessionKey);
                return compStr;
            }
        }

        // Fallback RAG Rộng hơn (Nếu khách đòi cấu hình viển vông, tự nới budget/cấu hình)
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
                int base = BotRAGUtils.scoreProduct(p, intent);
                int personal = BotRAGUtils.scorePersonalization(p, userMemory);
                scoreMap.put(p, base + personal);
            }

            topProducts = allProducts.stream()
                    .filter(p -> BotRAGUtils.matchStrict(p, intent)) // Lọc lại theo đ/k nới lỏng
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
            if (!intent.isComparison && intent.requestedBrand != null && !intent.requestedBrand.isEmpty()) {
                String brandDisplay = intent.requestedBrand.substring(0, 1).toUpperCase()
                        + intent.requestedBrand.substring(1);
                String reqBrand = intent.requestedBrand.toLowerCase();
                List<Product> sameBrandProducts = allProducts.stream()
                        .filter(p -> p.getProductName().toLowerCase().contains(reqBrand))
                        .sorted((p1, p2) -> Double.compare(p2.getPrice(), p1.getPrice()))
                        .limit(5)
                        .collect(Collectors.toList());

                StringBuilder notFoundMsg = new StringBuilder();
                if (intent.exactPhrase != null && !intent.exactPhrase.isEmpty()) {
                    notFoundMsg.append("Dạ, hiện tại PhoneShop chưa có **").append(intent.exactPhrase.toUpperCase())
                            .append("** trong kho ạ. Mẫu máy này có thể chưa ra mắt hoặc chưa được phân phối tại VN.\n\n");
                } else {
                    notFoundMsg.append("Dạ, hiện tại PhoneShop chưa có dòng **").append(brandDisplay)
                            .append("** phù hợp với yêu cầu của anh/chị trong kho ạ.\n\n");
                }

                if (!sameBrandProducts.isEmpty()) {
                    notFoundMsg.append("Tuy nhiên, PhoneShop hiện đang có các mẫu **").append(brandDisplay)
                            .append("** sau, anh/chị tham khảo nhé:\n\n");
                    for (Product p : sameBrandProducts) {
                        notFoundMsg.append("- **[").append(p.getProductName()).append("](detail?id=")
                                .append(p.getProductID()).append(")** | Giá: ")
                                .append(String.format("%,.0f VNĐ", p.getPrice()));
                        if (p.getRam() != null)
                            notFoundMsg.append(" | RAM: ").append(p.getRam());
                        if (p.getRom() != null)
                            notFoundMsg.append(" | ROM: ").append(p.getRom());
                        notFoundMsg.append("\n");
                    }
                    notFoundMsg.append("\nAnh/chị muốn tư vấn thêm về mẫu nào không ạ?");
                } else {
                    notFoundMsg.append(
                            "Anh/chị có muốn em gợi ý các dòng điện thoại khác có cấu hình tương đương không ạ?");
                }

                String notFoundStr = notFoundMsg.toString();
                saveHistory(session, history, message, notFoundStr, sessionKey);
                return notFoundStr;
            }

            // Không có brand cụ thể → fallback thông thường (top sản phẩm bất kỳ)
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

        // 3. Bypass AI hoàn toàn nếu đủ Intent (Đơn giản + Nhu cầu rõ ràng)
        if (!isFinalFallback && !isComplex && intent.hasFullIntent() && !intent.keywords.isEmpty()
                && !topProducts.isEmpty()) {
            System.out.println("--- BYPASSING AI (FULL INTENT) ---");
            if (!message.toLowerCase().contains("còn mẫu nào") && !message.toLowerCase().contains("khác")) {
                StringBuilder directHtml = new StringBuilder();
                directHtml.append("### Gợi ý từ PhoneShop AI\n\n");
                directHtml.append("Dựa trên yêu cầu của anh/chị, em đã tìm thấy các mẫu máy **tốt nhất** hiện có ạ:\n\n");

                for (Product p : topProducts) {
                    String pRam = p.getRam() != null ? p.getRam() : "N/A";
                    String pRom = p.getRom() != null ? p.getRom() : "N/A";

                    java.util.Map<String, Object> parsed = vn.edu.phoneshop.util.ProductDescriptionParser.parseForChatbot(p);
                    @SuppressWarnings("unchecked")
                    java.util.Map<String, String> specs = (java.util.Map<String, String>) parsed.get("specs");
                    String batteryInfo = "";
                    String cameraInfo = "";
                    String chipInfo = "";
                    String screenInfo = "";
                    String hzInfo = "";
                    String chargingInfo = "";
                    String msgLower = message.toLowerCase();
                    if (specs != null) {
                        if (specs.containsKey("battery")) batteryInfo = " / Pin " + specs.get("battery");
                        if (specs.containsKey("camera_main")) cameraInfo = " / Camera " + specs.get("camera_main");
                        if (specs.containsKey("chipset") && msgLower.matches(".*(chip|snapdragon|dimensity|exynos|helio|unisoc|bionic|vi xử lý|processor).*")) chipInfo = " / Chip " + specs.get("chipset");
                        if (specs.containsKey("screen_size") && msgLower.matches(".*(màn|inch|to|nhỏ).*")) screenInfo = " / Màn hình " + specs.get("screen_size");
                        if (specs.containsKey("refresh_rate") && msgLower.matches(".*(hz|tần số quét|mượt).*")) hzInfo = " / Tần số quét " + specs.get("refresh_rate");
                        if (specs.containsKey("charging") && msgLower.matches(".*(sạc|w).*")) chargingInfo = " / Sạc " + specs.get("charging");
                    }

                    directHtml.append("- **[").append(p.getProductName()).append("](detail?id=")
                            .append(p.getProductID()).append(")**\n")
                            .append("   - Giá: **").append(String.format("%,.0f VNĐ", p.getPrice())).append("**\n")
                            .append("   - Cấu hình: ").append(pRam).append(" RAM / ").append(pRom)
                            .append(" ROM").append(batteryInfo).append(cameraInfo).append(chipInfo).append(screenInfo).append(hzInfo).append(chargingInfo).append("\n\n");
                }

                directHtml.append("--- \n*Anh/chị có muốn tham khảo kỹ hơn về mẫu nào ở trên không ạ?*");
                saveHistory(session, history, message, directHtml.toString(), sessionKey);
                return directHtml.toString();
            }
        }

        // 3.5. Bypass AI if specific product + Buying Intent
        if (!isFinalFallback && intent.isBuying && !topProducts.isEmpty()) {
            Product best = topProducts.get(0);
            if (scoreMap.get(best) >= 40 || message.toLowerCase().contains("chốt con này")) {
                System.out.println("--- BYPASSING AI (BUYING INTENT) ---");
                String directReply = "Dạ, em hiểu ạ. Anh/chị chốt sản phẩm **[" + best.getProductName()
                        + "](detail?id=" + best.getProductID() + ")** giá **"
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
                java.util.Map<String, Object> parsed = vn.edu.phoneshop.util.ProductDescriptionParser.parseForChatbot(best);
                @SuppressWarnings("unchecked")
                java.util.Map<String, String> specs = (java.util.Map<String, String>) parsed.get("specs");
                String batteryInfo = ""; String cameraInfo = ""; String chipInfo = ""; String screenInfo = ""; String hzInfo = ""; String chargingInfo = "";
                String msgLower = message.toLowerCase();
                if (specs != null) {
                    if (specs.containsKey("battery")) batteryInfo = ", Pin " + specs.get("battery");
                    if (specs.containsKey("camera_main")) cameraInfo = ", Camera " + specs.get("camera_main");
                    if (specs.containsKey("chipset") && msgLower.matches(".*(chip|snapdragon|dimensity|exynos|helio|unisoc|bionic|vi xử lý|processor).*")) chipInfo = ", Chip " + specs.get("chipset");
                    if (specs.containsKey("screen_size") && msgLower.matches(".*(màn|inch|to|nhỏ).*")) screenInfo = ", Màn hình " + specs.get("screen_size");
                    if (specs.containsKey("refresh_rate") && msgLower.matches(".*(hz|tần số quét|mượt).*")) hzInfo = ", Tần số quét " + specs.get("refresh_rate");
                    if (specs.containsKey("charging") && msgLower.matches(".*(sạc|w).*")) chargingInfo = ", Sạc " + specs.get("charging");
                }

                String directReply = "Dạ, mẫu **[" + best.getProductName() + "](detail?id=" + best.getProductID()
                        + ")** hiện có giá sập sàn là **" + String.format("%,.0f VNĐ", best.getPrice())
                        + "**. Máy cấu hình RAM " + best.getRam() + ", ROM " + best.getRom()
                        + batteryInfo + cameraInfo + chipInfo + screenInfo + hzInfo + chargingInfo + ". Anh/chị xem có chốt gửi hàng không ạ?";
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
                    directReply.append("- **[").append(p.getProductName()).append("](detail?id=")
                            .append(p.getProductID()).append(")**\n");
                }
            }
            if (directReply.length() > 0) {
                String replyStr = directReply.toString().trim();
                saveHistory(session, history, message, replyStr, sessionKey);
                return replyStr;
            }
        }

        // 6. Prompt Generation & System Instructions
        String contextData = BotPromptUtils.buildProductContext(topProducts, isFinalFallback, intent);
        String intentData = BotPromptUtils.buildIntentContext(intent);
        String personalCtx = BotPromptUtils.buildPersonalizationContext(userMemory);

        String systemRole;
        if (intent.isComparison) {
            systemRole = "Bạn là chuyên gia tư vấn Smartphone cao cấp của PhoneShop.\n" +
                    "QUY TẮC NGHIÊM NGẶT (TUYỆT ĐỐI TUÂN THỦ):\n" +
                    "1. DÙNG TIẾNG VIỆT tự nhiên, thân thiện.\n" +
                    "2. YÊU CẦU NÀY LÀ MỘT YÊU CẦU SO SÁNH: Bạn hãy tiến hành SO SÁNH thật chi tiết sự khác biệt (như thiết kế, RAM, ROM, giá, v.v.) của các sản phẩm có trong CONTEXT, đánh giá ưu điểm và khuyết điểm thay vì chỉ liệt kê suông.\n" +
                    "3. ĐƯA RA KẾT LUẬN: Đưa ra lời khuyên cuối cùng xem mỗi dòng máy sẽ phù hợp nhất cho nhu cầu của người dùng nào, hoặc chiếc nào tối ưu tầm giá hơn.\n" +
                    "4. NO HALLUCINATION: Tuyệt đối không bịa thông tin sản phẩm ngoài Context, không điêu cấu hình. Chỉ sử dụng thông tin và giá từ CONTEXT.\n" +
                    "5. OUTPUT FORMAT: LUÔN LUÔN trả lời bằng Markdown chuẩn. Bắt buộc gắn link sản phẩm bằng dạng Markdown link như trong CONTEXT, ví dụ: **[Tên SP](detail?id=123)**.\n\n" +
                    (personalCtx.isEmpty() ? "" : "--- HÀNH VI KHÁCH HÀNG (Personalization) ---\n" + personalCtx + "\n") +
                    "--- YÊU CẦU ĐÃ PHÂN TÍCH TỪ KHÁCH HÀNG ---\n" + intentData + "\n" +
                    "--- CONTEXT (Kho SP Tốt Nhất Khớp Thuật Toán) ---\n" + contextData
                    + "\n--------------------------------\n";
        } else {
            systemRole = "Bạn là chuyên gia tư vấn Smartphone cao cấp của PhoneShop.\n" +
                    "QUY TẮC NGHIÊM NGẶT (TUYỆT ĐỐI TUÂN THỦ):\n" +
                    "1. DÙNG TIẾNG VIỆT tự nhiên, vô thẳng trọng tâm.\n" +
                    "2. TUYỆT ĐỐI KHÔNG hỏi lại nhu cầu (ngân sách, RAM, ROM) vì Hệ thống đã phân tích rõ ở YÊU CẦU KHÁCH HÀNG. Đừng mở đầu chung chung ('Dạ em hiểu, anh cần máy dưới 7 triệu... Dạ rồi'). BỎ!!!\n"
                    +
                    "3. LÊN THẲNG GỢI Ý: Lấy NGAY LẬP TỨC 1 đến 3 mẫu điện thoại trong CONTEXT chốt sale luôn.\n" +
                    "4. NẾU khách CHỈ yêu cầu TÊN SẢN PHẨM: CHỈ liệt kê TÊN SẢN PHẨM, TUYỆT ĐỐI KHÔNG kèm theo giá, cấu hình hay chào hỏi/giải thích thêm (trả về đúng tên thôi).\n"
                    +
                    "5. NO HALLUCINATION: Tuyệt đối không bịa điện thoại ngoài Context, không điêu cấu hình.\n" +
                    "6. OUTPUT FORMAT: LUÔN LUÔN trả lời bằng Markdown chuẩn. Bắt buộc gắn link sản phẩm bằng dạng Markdown link như trong CONTEXT, ví dụ: **[Tên SP](detail?id=123)**.\n"
                    +
                    "7. PERSONALIZATION: Nếu có hành vi khách (bên dưới), ưu tiên dòng máy/hãng khách yêu thích khi phù hợp.\n\n"
                    +
                    (personalCtx.isEmpty() ? "" : "--- HÀNH VI KHÁCH HÀNG (Personalization) ---\n" + personalCtx + "\n") +
                    "--- YÊU CẦU ĐÃ PHÂN TÍCH TỪ KHÁCH HÀNG ---\n" + intentData + "\n" +
                    "--- CONTEXT (Kho SP Tốt Nhất Khớp Thuật Toán) ---\n" + contextData
                    + "\n--------------------------------\n";
        }

        String jsonPayload = BotApiUtils.buildGeminiJsonPayload(systemRole, history, message);
        System.out.println("--- GEMINI AI CALL ---");
        System.out.println("Target Model: " + targetModel);
        System.out.println("JSON Payload: "
                + (jsonPayload.length() > 1000 ? jsonPayload.substring(0, 1000) + "... (truncated)" : jsonPayload));

        String reply = BotApiUtils.callGeminiJSON(jsonPayload, targetModel, true);

        // Khi tất cả model bị 429, tự build danh sách sản phẩm kèm thông báo
        if (BotApiUtils.SENTINEL_ALL_429.equals(reply)) {
            StringBuilder sb = new StringBuilder();
            sb.append("**Máy chủ AI đang quá tải, em tạm thời không thể tư vấn chi tiết.** ");
            sb.append("Dưới đây là các sản phẩm phù hợp nhất với yêu cầu của anh/chị, anh/chị tham khảo nhé:\n\n");
            for (Product p : topProducts) {
                String pRam = p.getRam() != null ? p.getRam() : "";
                String pRom = p.getRom() != null ? p.getRom() : "";

                java.util.Map<String, Object> parsed = vn.edu.phoneshop.util.ProductDescriptionParser.parseForChatbot(p);
                @SuppressWarnings("unchecked")
                java.util.Map<String, String> specs = (java.util.Map<String, String>) parsed.get("specs");
                String batteryInfo = ""; String cameraInfo = ""; String chipInfo = ""; String screenInfo = ""; String hzInfo = ""; String chargingInfo = "";
                String msgLower = message.toLowerCase();
                if (specs != null) {
                    if (specs.containsKey("battery")) batteryInfo = specs.get("battery");
                    if (specs.containsKey("camera_main")) cameraInfo = specs.get("camera_main");
                    if (specs.containsKey("chipset") && msgLower.matches(".*(chip|snapdragon|dimensity|exynos|helio|unisoc|bionic|vi xử lý|processor).*")) chipInfo = specs.get("chipset");
                    if (specs.containsKey("screen_size") && msgLower.matches(".*(màn|inch|to|nhỏ).*")) screenInfo = specs.get("screen_size");
                    if (specs.containsKey("refresh_rate") && msgLower.matches(".*(hz|tần số quét|mượt).*")) hzInfo = specs.get("refresh_rate");
                    if (specs.containsKey("charging") && msgLower.matches(".*(sạc|w).*")) chargingInfo = specs.get("charging");
                }

                sb.append("- **[").append(p.getProductName()).append("](detail?id=").append(p.getProductID())
                        .append(")** | Giá: ").append(String.format("%,.0f VNĐ", p.getPrice()));
                if (!pRam.isEmpty()) sb.append(" | RAM: ").append(pRam);
                if (!pRom.isEmpty()) sb.append(" | ROM: ").append(pRom);
                if (!batteryInfo.isEmpty()) sb.append(" | Pin: ").append(batteryInfo);
                if (!cameraInfo.isEmpty()) sb.append(" | Camera: ").append(cameraInfo);
                if (!chipInfo.isEmpty()) sb.append(" | Chip: ").append(chipInfo);
                if (!screenInfo.isEmpty()) sb.append(" | Màn hình: ").append(screenInfo);
                if (!hzInfo.isEmpty()) sb.append(" | Tần số quét: ").append(hzInfo);
                if (!chargingInfo.isEmpty()) sb.append(" | Sạc: ").append(chargingInfo);
                sb.append("\n");
            }
            sb.append("\n_Vui lòng thử lại sau 1 phút để được tư vấn đầy đủ hơn ạ._");
            reply = sb.toString();
        }

        // Post-process: gắn link trực tiếp vào tên sản phẩm nếu AI không tự làm
        reply = BotPromptUtils.injectProductLinks(reply, topProducts);
        // Post-process: Wrap "naked list" (Nếu AI trả danh sách mà không có lời dẫn)
        reply = BotPromptUtils.wrapAiResponse(reply);

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

        String contextData = BotPromptUtils.getAdminContext(message, history);
        String systemRole = "Bạn là trợ lý AI (Read-Only) cho Admin PhoneShop.\n" +
                "QUY TẮC:\n" +
                "1. Phân tích Dữ liệu Context bên dưới.\n" +
                "2. TỪ CHỐI cập nhật CSDL(UPDATE/DELETE/INSERT).\n" +
                "3. Báo cáo bằng tiếng Việt mạch lạc cấu trúc chuẩn.\n" +
                "4. OUTPUT FORMAT: LUÔN LUÔN trả lời bằng Markdown (dùng bảng biểu, in đậm, gạch đầu dòng).\n\n" +
                "--- CONTEXT (SQL Data Fetch) ---\n" + contextData + "\n--------------------------------\n";

        String jsonPayload = BotApiUtils.buildGeminiJsonPayload(systemRole, history, message);
        String targetModel = message.length() > 100 ? "gemini-2.5-pro" : "gemini-2.5-flash";
        String reply = BotApiUtils.callGeminiJSON(jsonPayload, targetModel, true);

        saveHistory(session, history, message, reply, sessionKey);
        return reply;
    }

    private void saveHistory(HttpSession session, List<ChatMessage> history, String userMsg, String aiReply,
            String roleKey) {
        String memUser = userMsg;
        if (memUser != null && memUser.length() > 1000) {
            memUser = memUser.substring(0, 1000) + "...";
        }

        String memAi = aiReply;
        if (memAi != null && memAi.length() > 4000) {
            memAi = memAi.substring(0, 4000) + "...";
        }

        history.add(new ChatMessage("user", memUser));
        history.add(new ChatMessage("model", memAi));

        if (history.size() > 6) {
            history = new ArrayList<>(history.subList(history.size() - 6, history.size()));
        }
        session.setAttribute(roleKey, history);
    }
}