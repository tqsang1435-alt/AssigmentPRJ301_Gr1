package vn.edu.phoneshop.bot;

import java.sql.Connection;
import java.util.List;
import java.util.regex.Matcher;
import java.util.regex.Pattern;
import java.util.stream.Collectors;

import vn.edu.phoneshop.model.Product;
import vn.edu.phoneshop.model.bot.ChatMessage;
import vn.edu.phoneshop.model.bot.IntentData;
import vn.edu.phoneshop.model.bot.UserMemory;
import vn.edu.phoneshop.utils.DBContext;

public class BotPromptUtils {

    /**
     * Tạo đoạn text ngắn mô tả hành vi user để inject vào System Prompt AI.
     */
    public static String buildPersonalizationContext(UserMemory mem) {
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
            List<String> recent = mem.clickedProductNames.subList(0, Math.min(3, mem.clickedProductNames.size()));
            sb.append("- Sản phẩm xem gần đây: ").append(String.join(", ", recent)).append("\n");
        }
        return sb.toString();
    }

    public static String buildIntentContext(IntentData intent) {
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
        if (intent.isComparison)
            sb.append("- Nhu cầu: So sánh cấu hình, đánh giá mạnh yếu giữa các dòng máy\n");
        if (!intent.exactPhrase.isEmpty()) {
            sb.append("- Dòng máy muốn tìm (Chuỗi cụm nguyên gốc): \"").append(intent.exactPhrase).append("\"\n");
        }
        return sb.length() == 0 ? "(Không yêu cầu đặc biệt, tư vấn tự chọn)\n" : sb.toString();
    }

    public static String buildProductContext(List<Product> products, boolean isFinalFallback, IntentData intent) {
        if (products == null || products.isEmpty())
            return "Cửa hàng đang cháy hàng dòng này, em xin lỗi ạ.";
        StringBuilder sb = new StringBuilder();
        if (isFinalFallback) {
            sb.append(
                    "[SYSTEM NOTE: Hiện hệ thống KHÔNG TÌM THẤY sản phẩm khớp chính xác yêu cầu của khách (có thể do cháy hàng hoặc ngân sách/cấu hình chưa phù hợp). BẠN PHẢI MỞ ĐẦU xin lỗi khéo léo (ví dụ: 'Dạ mẫu máy/yêu cầu anh/chị tìm bên em đang tạm hết hoặc chưa có sẵn'), SAU ĐÓ TƯ VẤN SANG CÁC SẢN PHẨM GỢI Ý THAY THẾ DƯỚI ĐÂY:]\n");
        }
        
        // Xác định khách hỏi thông số cụ thể nào để tiết kiệm token
        boolean focusChip = intent.keywords.contains("chip") || intent.keywords.contains("snapdragon") || intent.keywords.contains("dimensity") || intent.keywords.contains("exynos") || intent.keywords.contains("bionic");
        boolean focusCamera = intent.isCamera || intent.keywords.contains("camera") || intent.keywords.contains("chụp") || intent.keywords.contains("ảnh");
        boolean focusBattery = intent.isBattery || intent.keywords.contains("pin") || intent.keywords.contains("sạc");
        boolean focusScreen = intent.keywords.contains("màn") || intent.keywords.contains("hz") || intent.keywords.contains("inch");
        boolean isSpecificChoice = focusChip || focusCamera || focusBattery || focusScreen;
        
        for (Product p : products) {
            java.util.Map<String, Object> parsed = vn.edu.phoneshop.util.ProductDescriptionParser.parseForChatbot(p);
            sb.append(String.format("-[ID:%d] **[%s](detail?id=%d)**\n", p.getProductID(), p.getProductName(), p.getProductID()));
            sb.append("Giá: ").append(String.format("%,.0f VNĐ\n", parsed.get("price")));
            
            @SuppressWarnings("unchecked")
            java.util.Map<String, String> specs = (java.util.Map<String, String>) parsed.get("specs");
            if (specs != null && !specs.isEmpty()) {
                sb.append("Thông số:\n");
                specs.forEach((k, v) -> {
                    // Cắt bớt thông số không liên quan nếu khách chỉ hỏi 1 thứ
                    if (isSpecificChoice && !intent.isComparison) {
                        boolean keep = false;
                        if (focusChip && (k.equals("chipset") || k.equals("ram") || k.equals("rom"))) keep = true;
                        if (focusCamera && k.equals("camera_main")) keep = true;
                        if (focusBattery && (k.equals("battery") || k.equals("charging"))) keep = true;
                        if (focusScreen && (k.equals("screen_size") || k.equals("refresh_rate"))) keep = true;
                        if (!keep) return;
                    }
                    String label;
                    switch (k) {
                        case "ram": label = "RAM"; break;
                        case "rom": label = "ROM"; break;
                        case "battery": label = "Pin"; break;
                        case "charging": label = "Sạc nhanh"; break;
                        case "screen_size": label = "Màn hình"; break;
                        case "refresh_rate": label = "Tần số quét"; break;
                        case "camera_main": label = "Camera chính"; break;
                        case "chipset": label = "Chip"; break;
                        default: label = k; break;
                    }
                    sb.append("- ").append(label).append(": ").append(v).append("\n");
                });
            }
            
            // Cắt bớt mô tả và điểm nổi bật dài dòng để tiết kiệm token
            if (!isSpecificChoice || intent.isComparison) {
                @SuppressWarnings("unchecked")
                java.util.List<String> highs = (java.util.List<String>) parsed.get("highlights");
                if (highs != null && !highs.isEmpty()) {
                    sb.append("Nổi bật:\n");
                    int maxHighlights = intent.isComparison ? 5 : 3; // So sánh: hiện 5 điểm nổi bật
                    for (int i=0; i<Math.min(maxHighlights, highs.size()); i++) {
                        sb.append("- ").append(highs.get(i)).append("\n");
                    }
                }
                String shortDesc = (String) parsed.get("short_desc");
                if (shortDesc != null && !shortDesc.isEmpty()) {
                    int maxDesc = intent.isComparison ? 300 : 100; // So sánh: mô tả dài hơn
                    if (shortDesc.length() > maxDesc) shortDesc = shortDesc.substring(0, maxDesc) + "..."; // Rút gọn mô tả
                    sb.append("Mô tả: ").append(shortDesc).append("\n");
                }
            }
            sb.append("\n");
        }
        return sb.toString();
    }

    public static String getAdminContext(String message, List<ChatMessage> history) {
        StringBuilder sb = new StringBuilder();

        try {
            DBContext db = new DBContext();
            try (Connection conn = db.getConnection()) {
                sb.append("DANH SÁCH 5 KHÁCH HÀNG MỚI ĐĂNG KÝ:\n");
                try (java.sql.Statement stmt = conn.createStatement();
                        java.sql.ResultSet rs = stmt.executeQuery(
                                "SELECT TOP 5 UserID, FullName, Email, PhoneNumber FROM Users ORDER BY UserID DESC")) {
                    while (rs.next())
                        sb.append(String.format(" + ID: %s | Tên: %s | SĐT: %s\n", rs.getString("UserID"),
                                rs.getString("FullName"), rs.getString("PhoneNumber")));
                } catch (Exception e) {
                }

                sb.append("\nTOP 5 KHÁCH HÀNG CHI TIÊU NHIỀU NHẤT (VIP):\n");
                try (java.sql.Statement stmt = conn.createStatement();
                        java.sql.ResultSet rs = stmt.executeQuery(
                                "SELECT TOP 5 u.FullName, u.PhoneNumber, COUNT(o.OrderID) as TotalOrders, SUM(o.TotalMoney) as TotalSpent "
                                        +
                                        "FROM Users u JOIN Orders o ON u.UserID = o.UserID " +
                                        "GROUP BY u.UserID, u.FullName, u.PhoneNumber " +
                                        "ORDER BY TotalSpent DESC")) {
                    while (rs.next())
                        sb.append(String.format(" + KH: %s | SĐT: %s | Số đơn: %d | Tổng chi tiêu: %,.0f VNĐ\n",
                                rs.getString("FullName"), rs.getString("PhoneNumber"),
                                rs.getInt("TotalOrders"), rs.getDouble("TotalSpent")));
                } catch (Exception e) {
                }

                sb.append("\nDANH SÁCH 5 ĐƠN HÀNG MỚI NHẤT:\n");
                try (java.sql.Statement stmt = conn.createStatement();
                        java.sql.ResultSet rs = stmt.executeQuery(
                                "SELECT TOP 5 o.OrderID, u.FullName, o.TotalMoney FROM Orders o JOIN Users u ON o.UserID = u.UserID ORDER BY o.OrderDate DESC")) {
                    while (rs.next())
                        sb.append(String.format(" + Đơn ID: %d | KH: %s | Trị giá: %,.0f\n", rs.getInt("OrderID"),
                                rs.getString("FullName"), rs.getDouble("TotalMoney")));
                } catch (Exception e) {
                }

                sb.append("\nTOP 5 SẢN PHẨM BÁN CHẠY NHẤT:\n");
                try (java.sql.Statement stmt = conn.createStatement();
                        java.sql.ResultSet rs = stmt.executeQuery(
                                "SELECT TOP 5 p.ProductName, SUM(od.Quantity) as TotalSold " +
                                        "FROM Products p JOIN OrderDetails od ON p.ProductID = od.ProductID " +
                                        "GROUP BY p.ProductID, p.ProductName " +
                                        "ORDER BY TotalSold DESC")) {
                    while (rs.next())
                        sb.append(String.format(" + SP: %s | Đã bán: %d chiếc\n",
                                rs.getString("ProductName"), rs.getInt("TotalSold")));
                } catch (Exception e) {
                }

                sb.append("\nDANH SÁCH 5 SẢN PHẨM KHAN HÀNG (TỒN KHO THẤP):\n");
                try (java.sql.Statement stmt = conn.createStatement();
                        java.sql.ResultSet rs = stmt.executeQuery(
                                "SELECT TOP 5 ProductID, ProductName, StockQuantity FROM Products WHERE Status = 1 ORDER BY StockQuantity ASC")) {
                    while (rs.next())
                        sb.append(String.format(" + SP ID: %d | %s | Tồn: %d\n", rs.getInt("ProductID"),
                                rs.getString("ProductName"), rs.getInt("StockQuantity")));
                } catch (Exception e) {
                }
            }
        } catch (Exception e) {
        }
        return sb.toString();
    }

    public static String injectProductLinks(String reply, List<Product> products) {
        if (reply == null || products == null || products.isEmpty())
            return reply;

        // Sắp xếp sản phẩm theo độ dài tên giảm dần để tránh match sai
        List<Product> sorted = new java.util.ArrayList<>(products);
        sorted.sort((a, b) -> b.getProductName().length() - a.getProductName().length());

        for (Product p : sorted) {
            String name = p.getProductName();
            if (name == null || name.isBlank())
                continue;

            String linkMarkdown = "**[" + name + "](detail?id=" + p.getProductID() + ")**";

            // Kiểm tra nếu reply đã chứa URL tìm kiếm sản phẩm này thì bỏ qua
            if (reply.contains("detail?id=" + p.getProductID()))
                continue;

            String escaped = Pattern.quote(name);

            // Regex an toàn
            String regex = "(?i)(?<![\\p{L}\\p{N}])" + escaped + "(?![\\p{L}\\p{N}])(?![^\\[]*\\])(?![^\\(]*\\))";

            reply = reply.replaceAll(regex, Matcher.quoteReplacement(linkMarkdown));
        }
        return reply;
    }

    /**
     * Post-process: Nếu AI trả về một danh sách "trơ trụi" (chỉ có các dấu gạch đầu
     * dòng),
     * tự động bọc thêm lời dẫn và kết thúc để UI đẹp hơn.
     */
    public static String wrapAiResponse(String reply) {
        if (reply == null || reply.trim().isEmpty())
            return reply;

        String trimmed = reply.trim();
        // Kiểm tra xem có phải "naked list" không (bắt đầu bằng -, *, hoặc số)
        boolean isListStart = trimmed.startsWith("-") || trimmed.startsWith("*") || trimmed.startsWith("1.");

        // Nếu là danh sách mà ngắn (không có nhiều text giải thích phía trước)
        if (isListStart && !trimmed.contains("\n\n") && trimmed.split("\n").length >= 1) {
            System.out.println("[POST-PROCESS] Wrapping naked AI list");
            return "### Gợi ý chi tiết từ AI\n\n" +
                    "Dạ, PhoneShop xin gửi đến anh/chị một số lựa chọn nổi bật nhất:\n\n" +
                    reply +
                    "\n\n---\n" +
                    "*Anh/chị cần em so sánh kỹ hơn mẫu nào trong danh sách trên không ạ?*";
        }

        return reply;
    }
}
