package vn.edu.phoneshop.bot;

import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.util.Arrays;
import java.util.List;

import vn.edu.phoneshop.model.Product;
import vn.edu.phoneshop.model.bot.IntentData;
import vn.edu.phoneshop.model.bot.UserMemory;
import vn.edu.phoneshop.utils.DBContext;

public class BotRAGUtils {

    public static boolean matchStrict(Product p, IntentData intent) {
        // Đối chiếu thêm nếu là comparison có keywords cụ thể thì không filter cứng theo 1 brand
        // Để không lọc mất 1 trong các máy cần so sánh
        if (intent.isComparison && !intent.keywords.isEmpty()) {
            return true;
        }

        // FIX: Hard-filter theo brand nếu user yêu cầu brand cụ thể
        // Ví dụ: user hỏi "iphone 18 pro max" → chỉ cho qua các sản phẩm có tên chứa
        // "iphone"
        if (intent.requestedBrand != null && !intent.requestedBrand.isEmpty()) {
            String pname = p.getProductName().toLowerCase();
            String reqBrand = intent.requestedBrand.toLowerCase();
            // Alias: "iphone" → cũng match "apple"
            boolean brandMatch = pname.contains(reqBrand);
            if (reqBrand.equals("iphone") || reqBrand.equals("apple")) {
                brandMatch = pname.contains("iphone") || pname.startsWith("apple");
            } else if (reqBrand.equals("samsung") || reqBrand.equals("galaxy")) {
                brandMatch = pname.contains("samsung") || pname.contains("galaxy");
            }
            if (!brandMatch)
                return false;
        }

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

    public static int scoreProduct(Product p, IntentData intent) {
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

        // --- 3. NAME & SPEC MATCHING ---
        if (intent.exactPhrase != null && !intent.exactPhrase.isEmpty()) {
            String pname = p.getProductName().toLowerCase();
            
            java.util.Map<String, Object> parsed = vn.edu.phoneshop.util.ProductDescriptionParser.parseForChatbot(p);
            @SuppressWarnings("unchecked")
            java.util.Map<String, String> specs = (java.util.Map<String, String>) parsed.get("specs");
            String specsStr = specs != null ? specs.values().toString().toLowerCase() : "";
            
            boolean containsPhrase = pname.contains(intent.exactPhrase.toLowerCase()) || specsStr.contains(intent.exactPhrase.toLowerCase());

            if (pname.equals(intent.exactPhrase.toLowerCase())) {
                score += 100;
            } else if (containsPhrase) {
                score += 50;
                if (pname.startsWith(intent.exactPhrase.toLowerCase()))
                    score += 20;
            } else {
                // Penalty nếu exactPhrase chứa brand/model cụ thể nhưng sản phẩm không match
                String phraseNoAccent = java.text.Normalizer
                        .normalize(intent.exactPhrase.toLowerCase(), java.text.Normalizer.Form.NFD)
                        .replaceAll("\\p{M}", "").replace('đ', 'd').replace('Đ', 'D');
                if (!intent.isComparison && phraseNoAccent.matches(
                        ".*(iphone|samsung|oppo|vivo|xiaomi|realme|nokia|poco|sony|ipad|macbook|laptop|watch).*")) {
                    score -= 100; // Tăng penalty mạnh để không lọt qua ngưỡng -50
                }
            }

            // Keyword bonus
            List<String> pWords = Arrays.asList(pname.split("[\\s\\-\\_]+"));
            int matchCount = 0;
            String rawSpecs = specsStr.replace(" ", ""); // To match "120hz" with "120 Hz"
            for (String kw : intent.keywords) {
                if (kw.length() >= 1) { // Accept length 1 as well (e.g. S, 8)
                    if (pWords.contains(kw) || pname.contains(kw) || specsStr.contains(kw) || rawSpecs.contains(kw)) {
                        matchCount++;
                        score += 8;
                        if (kw.equals("snapdragon") || kw.equals("dimensity") || kw.equals("apple") || kw.equals("bionic")) 
                            score += 10; // Extra bonus for chip brand
                    }
                }
            }
            if (!intent.isComparison && matchCount == intent.keywords.size() && matchCount > 1) {
                score += 15;
            }
        }

        // --- 4. COMPARISON BRAND BONUS: ưu tiên sản phẩm thuộc brand đang so sánh ---
        if (intent.isComparison && !intent.comparisonBrands.isEmpty()) {
            String pname2 = p.getProductName().toLowerCase();
            for (String brand : intent.comparisonBrands) {
                boolean brandMatch = pname2.contains(brand);
                if (brand.equals("iphone") || brand.equals("apple")) {
                    brandMatch = pname2.contains("iphone") || pname2.startsWith("apple");
                } else if (brand.equals("samsung") || brand.equals("galaxy")) {
                    brandMatch = pname2.contains("samsung") || pname2.contains("galaxy");
                }
                if (brandMatch) {
                    score += 15; // Bonus để đảm bảo sản phẩm của brand được so sánh lọt top
                    break;
                }
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
    public static int scorePersonalization(Product p, UserMemory mem) {
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
    public static void loadPurchaseHistory(String userId, UserMemory mem) {
        String sql = "SELECT DISTINCT p.ProductName " +
                "FROM OrderDetails od " +
                "JOIN Products p ON od.ProductID = p.ProductID " +
                "JOIN Orders o ON od.OrderID = o.OrderID " +
                "WHERE o.UserID = ? AND o.Status IN (1,2,3,4) " +
                "ORDER BY o.OrderDate DESC";
        try {
            DBContext db = new DBContext();
            try (Connection conn = db.getConnection();
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
}
