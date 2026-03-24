package vn.edu.phoneshop.bot;

import vn.edu.phoneshop.model.bot.IntentData;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.HashSet;
import java.util.List;
import java.util.Set;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

public class BotNLPUtils {

    // Danh sách brand được nhận diện từ tên sản phẩm (prefix đầu tiên)
    public static final List<String> KNOWN_BRANDS = Arrays.asList(
            "iphone", "apple", "samsung", "galaxy", "oppo", "vivo", "xiaomi", "redmi", "poco",
            "realme", "nokia", "sony", "honor", "motorola", "tcl", "asus", "itel", "tecno");

    // -- TEXT NORMALIZATION (NLP nhẹ cho Tiếng Việt thông tục) --
    public static String normalizePriceText(String lower) {
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
                "(?i)(\\b(?:ram|rom|chip|snapdragon|dimensity|exynos|helio|unisoc|bionic|iphone|samsung|galaxy|note|reno|pixel|oppo|vivo|xiaomi|redmi|poco|realme|nokia|honor|sony|vsmart|bphone|ipad|macbook|ít|nhiều|hơn|khoảng|tầm|giá|dưới|trên|cỡ|từ|chừng|dòng|mẫu|con|series|máy)\\s+)?(?<![\\w\\.])(\\d+(?:\\.\\d+)?)(?![\\w\\.])(?!\\s*(?:gb|g|tb|ram|rom|tr|triệu|củ|inch|hz|%|px|vnd|đ|k|pro|plus|max|ultra|mini|promax|lite|s|se|gen)\\b)(?!\\s*\\+)");
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
    public static String lowerPlusOne(String lower) {
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
    public static IntentData analyzeIntent(String msg) {
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
                .compile("(\\d+(\\.\\d+)?)\\s*(?:tr|triệu|củ)?\\s*(?:-|~|đến|xuống|về)\\s*(\\d+(\\.\\d+)?)\\s*(tr|triệu|củ)");
        Matcher mRange = rangePattern.matcher(lower);
        if (mRange.find()) {
            try {
                data.minPrice = Double.parseDouble(mRange.group(1)) * 1000000;
                data.maxPrice = Double.parseDouble(mRange.group(3)) * 1000000;
                if (data.minPrice > data.maxPrice) {
                    double temp = data.minPrice;
                    data.minPrice = data.maxPrice;
                    data.maxPrice = temp;
                }
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
                    boolean isOver = lower.matches(".*(?U)\\b(trên|từ|tối thiểu|ít nhất)\\b.*");

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

        Pattern ramPattern = Pattern
                .compile("(?i)(?:ram\\s*)(\\d+)\\s*(?:gb|g)?|\\b(?!(?:3|4|5)g\\b)(\\d+)\\s*(?:gb|g)\\b(?!\\s*rom)");
        Matcher mRam = ramPattern.matcher(lower);
        if (mRam.find()) {
            data.ram = mRam.group(1) != null ? mRam.group(1) : mRam.group(2);
        }

        Pattern romPattern = Pattern.compile("(?:rom\\s*)?(\\d{2,4})\\s*(?:gb|g)|(\\d+)\\s*tb");
        Matcher mRom = romPattern.matcher(lower);
        if (mRom.find()) {
            data.rom = mRom.group(1) != null ? mRom.group(1) : mRom.group(2);
        }

        // --- Bước 1: Xóa các cụm từ chức năng bằng regex (Unicode word-boundary để tránh cắt tiếng Việt) ---
        String cleaned = lower.replaceAll(
                "(?U)\\b(tôi|mình|bạn|cần|muốn|giúp|với|xin|chào|hi|hello|alo|dạ|vâng|ok|oke|hey|giá|bao nhiêu|tiền|tầm|dưới|trên|khoảng|mua|tìm|lấy|điện|thoại|loại|cái|nào|so|sánh|tư|vấn|chụp|ảnh|game|chơi|qua|pin|trâu|sạc|nhanh|có|sản|phẩm|từ|đến|ko|không|nhỉ|ạ|cho|hỏi|vừa|cho nên|vs|hay là|so sánh|so với|nên mua|nên chọn)\\b",
                " ");
        // Xóa số + đơn vị giá/dung lượng
        cleaned = cleaned.replaceAll(
                "(?i)(\\d+(\\.\\d+)?)\\s*(?:triệu|tr|củ)?\\s*(?:-|~|đến)\\s*(\\d+(\\.\\d+)?)\\s*(triệu|tr|củ|gb|tb|g\\b)",
                " ")
                .trim();
        cleaned = cleaned.replaceAll("(?i)\\b(?!(?:3|4|5)g\\b)(\\d+(\\.\\d+)?)\\s*(triệu|tr|củ|gb|tb|g\\b)", " ")
                .trim();
        cleaned = cleaned.replaceAll("(?i)ram\\s*\\d+|rom\\s*\\d+", " ").trim();
        cleaned = cleaned.replaceAll("\\s+", " ").trim();

        // --- Bước 2: Lọc token bằng Set stopwords Unicode ---
        // (cần thiết vì \b không hoạt động với ký tự Unicode có dấu
        // như "rẻ", "đẹp", "và", "hay" — \b chỉ nhận ASCII [a-zA-Z0-9_] là word
        // boundary)
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
                // Đại từ / trợ từ hỏi / từ chào hỏi
                "tôi", "mình", "bạn", "cần", "muốn", "giúp", "xin", "chào", "hi", "hello", "alo", "dạ", "vâng", "ok", "oke", "hey",
                "cho", "hỏi", "nào", "có", "không", "ko", "nhỉ", "ạ", "ad", "shop", "admin", "anh", "chị", "em", "ơi", "nha", "nhé",
                // Động từ chức năng
                "mua", "tìm", "lấy", "gợi", "đề", "xuất", "giới", "thiệu", "biết", "xem",
                // Danh từ chung và Nhu cầu (đã parse riêng)
                "điện", "thoại", "đt", "smartphone", "máy", "sản", "phẩm", "loại", "cái", "chiếc",
                "dòng", "mẫu", "con", "hàng", "giá", "tiền", "game", "pubg", "liên", "quân",
                "chụp", "ảnh", "camera", "quay", "video", "selfie", "pin", "sạc", "nhanh", "zoom",
                "ram", "rom", "cấu", "hình", "thông", "số", "chi", "tiết",
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
                // Bỏ token nếu là stopword, cho phép token 1 ký tự nếu là chữ/số (như S, Z, 8)
                if (!t.isEmpty() && (t.length() > 1 || t.matches("[a-z0-9]")) && !viStopwords.contains(t)) {
                    filteredTokens.add(t);
                }
            }
            if (!filteredTokens.isEmpty()) {
                data.exactPhrase = String.join(" ", filteredTokens);
                data.keywords.addAll(filteredTokens);
            }
        }

        // --- Bước 3: Nhận diện brand cụ thể mà user yêu cầu ---
        // Nếu user hỏi "iphone 18 pro max" → requestedBrand = "iphone"
        // Nếu user so sánh 2+ brand → comparisonBrands chứa tất cả, requestedBrand = null
        String[] brandKeywords = {
                "iphone", "apple", "samsung", "galaxy", "oppo", "vivo", "xiaomi", "redmi", "poco",
                "realme", "nokia", "sony", "honor", "motorola", "tcl", "asus", "itel", "tecno"
        };
        String lowerNoAccent = java.text.Normalizer.normalize(lower, java.text.Normalizer.Form.NFD)
                .replaceAll("\\p{M}", "").replace('đ', 'd').replace('Đ', 'D');

        List<String> detectedBrands = new ArrayList<>();
        for (String bk : brandKeywords) {
            if (lowerNoAccent.contains(bk) || lower.contains(bk)) {
                // Tránh thêm alias trùng (samsung + galaxy đều chỉ 1 brand)
                boolean alreadyCovered = false;
                for (String existing : detectedBrands) {
                    if ((existing.equals("samsung") && bk.equals("galaxy"))
                            || (existing.equals("galaxy") && bk.equals("samsung"))
                            || (existing.equals("iphone") && bk.equals("apple"))
                            || (existing.equals("apple") && bk.equals("iphone"))) {
                        alreadyCovered = true;
                        break;
                    }
                }
                if (!alreadyCovered) {
                    detectedBrands.add(bk);
                    System.out.println("[NLP] Detected brand: " + bk);
                }
            }
        }

        if (data.isComparison && detectedBrands.size() >= 2) {
            // So sánh nhiều brand → không lọc cứng theo 1 brand, lưu tất cả để scoring
            data.comparisonBrands.addAll(detectedBrands);
            data.requestedBrand = null;
            System.out.println("[NLP] Comparison mode: brands=" + detectedBrands);
        } else if (!detectedBrands.isEmpty()) {
            data.requestedBrand = detectedBrands.get(0);
            System.out.println("[NLP] Detected requestedBrand: " + data.requestedBrand);
        }

        // --- Bước 4: Log wantsValue để debug (không cần lưu vào IntentData) ---
        System.out.println("[NLP] wantsValue=" + wantsValue + " | isComparison=" + data.isComparison
                + " | requestedBrand=" + data.requestedBrand);

        return data;
    }
}
