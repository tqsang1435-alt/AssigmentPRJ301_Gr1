package vn.edu.phoneshop.bot;

import java.io.BufferedReader;
import java.io.InputStreamReader;
import java.io.OutputStream;
import java.net.HttpURLConnection;
import java.net.URL;
import java.nio.charset.StandardCharsets;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.Collections;
import java.util.List;
import java.util.Map;
import java.util.concurrent.ConcurrentHashMap;
import java.util.stream.Collectors;

import com.google.gson.Gson;
import com.google.gson.JsonArray;
import com.google.gson.JsonElement;
import com.google.gson.JsonObject;

import vn.edu.phoneshop.model.bot.ChatMessage;

public class BotApiUtils {

    // Cache để tối ưu token: lưu lại các câu trả lời cho cùng một payload (cùng câu hỏi/context/dữ liệu)
    private static final int MAX_CACHE_SIZE = 200;
    private static final Map<String, String> responseCache = Collections.synchronizedMap(new java.util.LinkedHashMap<String, String>(MAX_CACHE_SIZE, 0.75f, true) {
        @Override
        protected boolean removeEldestEntry(Map.Entry<String, String> eldest) {
            return size() > MAX_CACHE_SIZE;
        }
    });

    // Tùy chọn xóa cache chủ động (trường hợp muốn force AI chạy lại)
    public static void clearCache() {
        if (responseCache != null) {
            responseCache.clear();
            System.out.println("[GEMINI CACHE] Đã xoá toàn bộ cache theo yêu cầu.");
        }
    }

    private static final long COOLDOWN_TIME = 60 * 1000;
    /**
     * Cooldown map dùng composite key "apiKey::modelName" để tránh "oan" model khác
     * khi 1 key chỉ bị giới hạn ở 1 model cụ thể.
     */
    private static final Map<String, Long> keyCooldowns = new ConcurrentHashMap<>();
    /** Sentinel trả về khi TẤT CẢ tổ hợp (key × model) đều bị rate-limit / lỗi */
    public static final String SENTINEL_ALL_429 = "__ALL_MODELS_RATE_LIMITED_429__";

    public static List<String> getApiKeys() {
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
    public static String buildCooldownKey(String apiKey, String model) {
        return apiKey + "::" + model;
    }

    public static String escapeJson(String raw) {
        if (raw == null)
            return "";
        return raw.replace("\\", "\\\\").replace("\"", "\\\"").replace("\b", "\\b").replace("\f", "\\f")
                .replace("\n", "\\n").replace("\r", "\\r").replace("\t", "\\t");
    }

    public static String buildGeminiJsonPayload(String systemInstruction, List<ChatMessage> history, String currentMessage) {
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

    public static String extractRobustJsonText(String json) {
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
    public static String callGeminiJSON(String jsonPayload, String preferredModel, boolean allowFallback) {
        // --- Kiểm tra Cache (Tối ưu token nếu không có dữ liệu/câu hỏi mới) ---
        if (responseCache.containsKey(jsonPayload)) {
            System.out.println("[GEMINI CACHE HIT] Trả về kết quả từ cache (Tối ưu token)");
            return responseCache.get(jsonPayload);
        }

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
                            String result = extractRobustJsonText(sb.toString());
                            // Lưu vào cache nếu kết quả hợp lệ (không phải lỗi)
                            if (!result.startsWith("[Lỗi") && !result.startsWith("Xin lỗi")) {
                                responseCache.put(jsonPayload, result);
                            }
                            return result;
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
}
