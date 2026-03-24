package vn.edu.phoneshop.util;

import java.util.*;
import java.util.regex.Matcher;
import java.util.regex.Pattern;
import vn.edu.phoneshop.model.Product;

/**
 * ProductDescriptionParser
 * 
 * Lớp tiện ích để phân tích và trích xuất thông tin quan trọng từ cột Description dài
 * của sản phẩm, nhằm tạo context ngắn gọn, chính xác cho ChatBot.
 */
public class ProductDescriptionParser {

    // Regex để trích xuất thông số kỹ thuật phổ biến
    private static final Map<String, Pattern> SPEC_PATTERNS = new LinkedHashMap<>();

    static {
        // RAM
        SPEC_PATTERNS.put("ram", Pattern.compile("(?i)(?:ram| bộ nhớ ram)\\s*[:\\-–]?\\s*(\\d+)\\s*(?:gb|g| )", Pattern.CASE_INSENSITIVE));
        
        // ROM / bộ nhớ trong
        SPEC_PATTERNS.put("rom", Pattern.compile("(?i)(?:rom|bộ nhớ trong|bộ nhớ|dung lượng)\\s*[:\\-–]?\\s*(\\d+)\\s*(?:gb|g|tb| )|(\\d+)\\s*(TB|GB)", Pattern.CASE_INSENSITIVE));
        
        // Pin
        SPEC_PATTERNS.put("battery", Pattern.compile("(?i)(?:pin|dung lượng pin|battery)\\s*[:\\-–]?\\s*(\\d+)\\s*(?:mah|mAh| )", Pattern.CASE_INSENSITIVE));
        
        // Sạc nhanh
        SPEC_PATTERNS.put("charging", Pattern.compile("(?i)(?:sạc|sạc nhanh|sạc siêu nhanh)\\s*[:\\-–]?\\s*(\\d+)\\s*(?:w|W| )", Pattern.CASE_INSENSITIVE));
        
        // Màn hình
        SPEC_PATTERNS.put("screen_size", Pattern.compile("(?i)(?:màn hình|screen)\\s*[:\\-–]?\\s*(\\d+\\.?\\d*)\\s*(?:inch|\")|(\\d+\\.?\\d*)\\s*inch", Pattern.CASE_INSENSITIVE));
        SPEC_PATTERNS.put("refresh_rate", Pattern.compile("(?i)(?:tần số quét|refresh rate)\\s*[:\\-–]?\\s*(\\d+)\\s*(?:hz|Hz)|(\\d+)\\s*Hz", Pattern.CASE_INSENSITIVE));
        
        // Camera chính
        SPEC_PATTERNS.put("camera_main", Pattern.compile("(?i)(?:camera chính|camera sau|camera)\\s*[:\\-–]?\\s*(\\d+)\\s*(?:mp|MP)", Pattern.CASE_INSENSITIVE));
        
        // Chip
        SPEC_PATTERNS.put("chipset", Pattern.compile("(?i)(?:chip|vi xử lý|processor|snapdragon|dimensity|exynos|helio|unisoc).*?([A-Za-z0-9\\s\\(\\)\\-]+)|(A\\d{2}\\s?Pro)", Pattern.CASE_INSENSITIVE));
    }

    /**
     * Phân tích Description và trả về một Map chứa thông tin đã trích xuất
     * @param product sản phẩm có trường description
     * @return Map<String, Object> với các key: highlights, specs, shortDesc, ...
     */
    public static Map<String, Object> parseForChatbot(Product product) {
        Map<String, Object> result = new LinkedHashMap<>();
        
        String desc = product.getDescription();
        if (desc == null || desc.trim().isEmpty()) {
            result.put("error", "Không có mô tả");
            return result;
        }

        // Lọc sạch dữ liệu trước khi parse để tránh nhiễu
        desc = cleanDescription(desc);

        // 1. Trích xuất phần đặc điểm nổi bật (thường nằm ở đầu)
        List<String> highlights = extractHighlights(desc);
        result.put("highlights", highlights);

        // 2. Trích xuất các thông số kỹ thuật bằng regex
        Map<String, String> specs = extractSpecifications(desc);
        
        // Bổ sung RAM/ROM từ DB vào specs nếu regex không quét được
        if (!specs.containsKey("ram") && product.getRam() != null && !product.getRam().trim().isEmpty()) {
            String r = product.getRam().trim();
            specs.put("ram", r + (r.matches("\\d+") ? " GB" : ""));
        }
        if (!specs.containsKey("rom") && product.getRom() != null && !product.getRom().trim().isEmpty()) {
            String r = product.getRom().trim();
            specs.put("rom", r + (r.matches("\\d+") ? " GB" : ""));
        }
        
        result.put("specs", specs);

        // 3. Tạo mô tả ngắn gọn (lấy 1-3 câu đầu hoặc phần tóm tắt)
        String shortDesc = extractShortDescription(desc);
        result.put("short_desc", shortDesc);

        // 4. Thêm một số thông tin cơ bản từ product
        result.put("product_id", product.getProductID());
        result.put("name", product.getProductName());
        result.put("price", product.getPrice());
        result.put("ram_db", product.getRam());   // so sánh với regex
        result.put("rom_db", product.getRom());

        return result;
    }

    /**
     * Lọc sạch dữ liệu trước khi parse
     */
    private static String cleanDescription(String text) {
        return text
            // remove bảng
            .replaceAll("(?is)Bảng so sánh.*", "")
            // remove giá
            .replaceAll("(?is)Giá bán.*", "")
            // remove FAQ
            .replaceAll("(?is)Những thông tin cần biết.*", "")
            // remove marketing dài
            .replaceAll("(?is)Vì sao .*?\\?", "")
            // remove nhiều space
            .replaceAll("\\s{2,}", " ")
            .trim();
    }

    /**
     * Trích xuất danh sách các điểm mạnh nổi bật bằng NLP theo câu
     */
    private static List<String> extractHighlights(String text) {
        List<String> result = new ArrayList<>();

        String[] sentences = text.split("(?<=[.!?])\\s+");

        for (String s : sentences) {
            String lower = s.toLowerCase();

            if (
                lower.contains("chip") ||
                lower.contains("camera") ||
                lower.contains("màn hình") ||
                lower.contains("pin") ||
                lower.contains("hiệu năng") ||
                lower.contains("titan")
            ) {
                result.add(s.trim());
            }

            if (result.size() >= 5) break;
        }

        return result.isEmpty()
            ? Collections.singletonList("Không trích xuất được")
            : result;
    }

    /**
     * Trích xuất thông số kỹ thuật bằng regex
     */
    private static Map<String, String> extractSpecifications(String text) {
        Map<String, String> specs = new LinkedHashMap<>();
        
        for (Map.Entry<String, Pattern> entry : SPEC_PATTERNS.entrySet()) {
            Matcher matcher = entry.getValue().matcher(text);
            if (matcher.find()) {
                String k = entry.getKey();
                String value = "";
                
                // Lấy group đầu tiên có giá trị
                for (int i = 1; i <= matcher.groupCount(); i++) {
                    if (matcher.group(i) != null) {
                        value = matcher.group(i).trim();
                        // Gộp group kế tiếp (nếu là đơn vị TB/GB/inch/Hz được define trong regex riêng)
                        if (i + 1 <= matcher.groupCount() && matcher.group(i + 1) != null && (k.equals("rom") || k.equals("screen_size") || k.equals("refresh_rate"))) {
                            value += " " + matcher.group(i + 1).trim();
                        }
                        break;
                    }
                }
                
                if (value.isEmpty()) {
                    value = matcher.group().trim();
                }
                
                // Chuẩn hóa một chút
                if (k.equals("chipset") && value.length() > 40) {
                    value = value.substring(0, 40) + "...";
                }
                
                // Xử lý riêng biệt cho các format đặc thù (TB của Apple)
                if (value.toLowerCase().contains("tb")) {
                    value = value.replace(" ", "");
                }
                
                // Chuẩn hóa đơn vị
                if ((k.equals("ram") || k.equals("rom") || k.equals("rom_db") || k.equals("ram_db")) && value.matches("\\d+")) value = value + " GB";
                if (k.equals("battery") && value.matches("\\d+")) value = value + " mAh";
                if (k.equals("charging") && value.matches("\\d+")) value = value + " W";
                if (k.equals("camera_main") && value.matches("\\d+")) value = value + " MP";
                if (k.equals("screen_size") && value.matches("\\d+\\.?\\d*")) value = value + " inch";
                if (k.equals("refresh_rate") && value.matches("\\d+")) value = value + " Hz";
                
                specs.put(k, value);
            }
        }
        
        return specs;
    }

    /**
     * Tạo mô tả ngắn gọn (dùng NLP đơn giản)
     */
    private static String extractShortDescription(String text) {
        String[] sentences = text.split("(?<=[.!?])\\s+");

        for (String s : sentences) {
            String lower = s.toLowerCase();
            if (
                lower.contains("chip") ||
                lower.contains("hiệu năng") ||
                lower.contains("camera")
            ) {
                return s.trim();
            }
        }

        return sentences.length > 0 ? sentences[0].trim() : "";
    }

    // -------------------------------------------------------------------------
    // Ví dụ sử dụng trong ChatBotServlet
    // -------------------------------------------------------------------------
    public static String buildShortContextForAI(Product p) {
        Map<String, Object> parsed = parseForChatbot(p);
        
        StringBuilder sb = new StringBuilder();
        sb.append("Sản phẩm: ").append(parsed.get("name")).append("\n");
        sb.append("Giá: ").append(String.format("%,.0f VNĐ\n", parsed.get("price")));
        
        @SuppressWarnings("unchecked")
        List<String> highs = (List<String>) parsed.get("highlights");
        if (highs != null && !highs.isEmpty()) {
            sb.append("Điểm nổi bật:\n");
            for (String h : highs) {
                sb.append("- ").append(h).append("\n");
            }
        }
        
        @SuppressWarnings("unchecked")
        Map<String, String> specs = (Map<String, String>) parsed.get("specs");
        if (specs != null && !specs.isEmpty()) {
            sb.append("Thông số chính:\n");
            specs.forEach((k, v) -> {
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
        
        sb.append("Mô tả ngắn: ").append(parsed.get("short_desc"));
        
        return sb.toString();
    }

    // Test nhanh
    public static void main(String[] args) {
        // Giả lập product
        Product p = new Product();
        p.setProductID(19);
        p.setProductName("HONOR Play 10 3GB/64GB");
        p.setPrice(1990000);
        p.setDescription("Đặc điểm nổi bật HONOR Play 10... Bộ xử lý MediaTek Helio G81... Màn hình 6.74 inch 90 Hz... Pin 5000 mAh..."); // paste description thật vào đây
        
        Map<String, Object> result = parseForChatbot(p);
        System.out.println("Kết quả trích xuất:");
        System.out.println(result);
        
        System.out.println("\nContext ngắn cho AI:\n" + buildShortContextForAI(p));
    }
}
