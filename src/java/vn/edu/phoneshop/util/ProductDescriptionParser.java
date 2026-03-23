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

    // Các từ khóa thường xuất hiện trong phần "Đặc điểm nổi bật"
    private static final List<String> HIGHLIGHT_STARTERS = Arrays.asList(
        "Đặc điểm nổi bật", "Điểm nổi bật", "Nổi bật", "Ưu điểm", 
        "Thiết kế", "Màn hình", "Hiệu năng", "Camera", "Pin", "Sạc"
    );

    // Regex để trích xuất thông số kỹ thuật phổ biến
    private static final Map<String, Pattern> SPEC_PATTERNS = new LinkedHashMap<>();

    static {
        // RAM
        SPEC_PATTERNS.put("ram", Pattern.compile("(?i)(?:ram| bộ nhớ ram)\\s*[:\\-–]?\\s*(\\d+)\\s*(?:gb|g| )", Pattern.CASE_INSENSITIVE));
        
        // ROM / bộ nhớ trong
        SPEC_PATTERNS.put("rom", Pattern.compile("(?i)(?:rom|bộ nhớ trong|bộ nhớ)\\s*[:\\-–]?\\s*(\\d+)\\s*(?:gb|g|tb| )", Pattern.CASE_INSENSITIVE));
        
        // Pin
        SPEC_PATTERNS.put("battery", Pattern.compile("(?i)(?:pin|dung lượng pin)\\s*[:\\-–]?\\s*(\\d+)\\s*(?:mah|mAh| )", Pattern.CASE_INSENSITIVE));
        
        // Sạc nhanh
        SPEC_PATTERNS.put("charging", Pattern.compile("(?i)(?:sạc|sạc nhanh|sạc siêu nhanh)\\s*[:\\-–]?\\s*(\\d+)\\s*(?:w|W| )", Pattern.CASE_INSENSITIVE));
        
        // Màn hình
        SPEC_PATTERNS.put("screen_size", Pattern.compile("(?i)(?:màn hình|screen)\\s*[:\\-–]?\\s*(\\d+\\.?\\d*)\\s*(?:inch|\")", Pattern.CASE_INSENSITIVE));
        SPEC_PATTERNS.put("refresh_rate", Pattern.compile("(?i)(?:tần số quét|refresh rate)\\s*[:\\-–]?\\s*(\\d+)\\s*(?:hz|Hz)", Pattern.CASE_INSENSITIVE));
        
        // Camera chính
        SPEC_PATTERNS.put("camera_main", Pattern.compile("(?i)(?:camera chính|camera sau|camera)\\s*[:\\-–]?\\s*(\\d+)\\s*(?:mp|MP)", Pattern.CASE_INSENSITIVE));
        
        // Chip
        SPEC_PATTERNS.put("chipset", Pattern.compile("(?i)(?:chip|vi xử lý|processor|snapdragon|dimensity|exynos|helio|unisoc).*?([A-Za-z0-9\\s\\(\\)\\-]+)", Pattern.CASE_INSENSITIVE));
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

        // Lọc bảng so sánh ra trước khi parse để tránh nhiễu
        desc = desc.replaceAll("(?is)Bảng so sánh.*", "");

        // 1. Trích xuất phần đặc điểm nổi bật (thường nằm ở đầu)
        List<String> highlights = extractHighlights(desc);
        result.put("highlights", highlights);

        // 2. Trích xuất các thông số kỹ thuật bằng regex
        Map<String, String> specs = extractSpecifications(desc);
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
     * Trích xuất danh sách các điểm mạnh nổi bật (bullet points)
     */
    private static List<String> extractHighlights(String text) {
        List<String> highlights = new ArrayList<>();
        
        // Tìm đoạn bắt đầu bằng "Đặc điểm nổi bật" hoặc tương tự
        String lower = text.toLowerCase();
        int startIdx = -1;
        
        for (String starter : HIGHLIGHT_STARTERS) {
            int idx = lower.indexOf(starter.toLowerCase());
            if (idx >= 0) {
                startIdx = idx;
                break;
            }
        }
        
        if (startIdx < 0) {
            // Không tìm thấy → lấy 4-8 dòng đầu có dấu đầu dòng hoặc số
            String[] lines = text.split("\n");
            for (String line : lines) {
                String trimmed = line.trim();
                String tLower = trimmed.toLowerCase();
                boolean hasKeywords = tLower.contains("mang đến") || tLower.contains("đột phá") || tLower.contains("siêu việt");
                
                if (trimmed.startsWith("-") || trimmed.startsWith("•") || trimmed.matches("^\\d+\\.\\s.*") || hasKeywords) {
                    String clean = trimmed.replaceAll("^[\\-\\•\\d\\.\\s]+", "").trim();
                    if (!clean.isEmpty() && (clean.length() > 15 || hasKeywords)) {
                        highlights.add(clean);
                    }
                }
                if (highlights.size() >= 8) break;
            }
        } else {
            // Có phần đặc điểm nổi bật → lấy các dòng sau đó
            String sub = text.substring(startIdx);
            String[] lines = sub.split("\n");
            boolean inHighlight = true;
            
            for (String line : lines) {
                String trimmed = line.trim();
                if (trimmed.isEmpty()) continue;
                
                // Dừng khi gặp tiêu đề khác hoặc đoạn văn dài
                if (trimmed.length() > 100 || trimmed.matches("(?i)(xem thêm|thông số kỹ thuật|tóm lại)")) {
                    inHighlight = false;
                }
                
                String tLower = trimmed.toLowerCase();
                boolean hasKeywords = tLower.contains("mang đến") || tLower.contains("đột phá") || tLower.contains("siêu việt");
                
                if (inHighlight && (trimmed.startsWith("-") || trimmed.startsWith("•") || trimmed.matches("^\\d+\\.\\s.*") || hasKeywords)) {
                    String clean = trimmed.replaceAll("^[\\-\\•\\d\\.\\s]+", "").trim();
                    if (!clean.isEmpty()) {
                        highlights.add(clean);
                    }
                }
                
                if (highlights.size() >= 8) break;
            }
        }
        
        return highlights.size() > 0 ? highlights : Collections.singletonList("Không trích xuất được điểm nổi bật");
    }

    /**
     * Trích xuất thông số kỹ thuật bằng regex
     */
    private static Map<String, String> extractSpecifications(String text) {
        Map<String, String> specs = new LinkedHashMap<>();
        
        for (Map.Entry<String, Pattern> entry : SPEC_PATTERNS.entrySet()) {
            Matcher matcher = entry.getValue().matcher(text);
            if (matcher.find()) {
                String value = matcher.group(1).trim();
                String k = entry.getKey();
                
                // Chuẩn hóa một chút
                if (k.equals("chipset") && value.length() > 40) {
                    value = value.substring(0, 40) + "...";
                }
                
                // Chuẩn hóa đơn vị
                if (k.equals("ram") || k.equals("rom") || k.equals("rom_db") || k.equals("ram_db")) value = value + " GB";
                if (k.equals("battery")) value = value + " mAh";
                if (k.equals("charging")) value = value + " W";
                if (k.equals("camera_main")) value = value + " MP";
                
                specs.put(k, value);
            }
        }
        
        return specs;
    }

    /**
     * Tạo mô tả ngắn gọn (1-2 câu đầu hoặc phần tóm tắt)
     */
    private static String extractShortDescription(String text) {
        String[] paragraphs = text.split("\n\\n+");
        if (paragraphs.length == 0) return "";
        
        String firstPara = paragraphs[0].trim();
        if (firstPara.length() > 300) {
            firstPara = firstPara.substring(0, 280) + "...";
        }
        
        // Ưu tiên đoạn có từ "là lựa chọn", "mang đến", "đáp ứng"
        for (String p : paragraphs) {
            if (p.contains("là lựa chọn") || p.contains("mang đến") || p.contains("phù hợp")) {
                String candidate = p.trim();
                if (candidate.length() < 350) {
                    return candidate;
                }
            }
        }
        
        return firstPara;
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
