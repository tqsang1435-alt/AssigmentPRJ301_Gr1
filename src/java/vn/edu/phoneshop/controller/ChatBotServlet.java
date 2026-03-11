package vn.edu.phoneshop.controller;

import jakarta.servlet.ServletException;
import jakarta.servlet.annotation.WebServlet;
import jakarta.servlet.http.HttpServlet;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;
import java.io.IOException;
import java.io.BufferedReader;
import java.io.InputStreamReader;
import java.io.OutputStream;
import java.net.HttpURLConnection;
import java.net.URL;
import java.nio.charset.StandardCharsets;
import java.io.PrintWriter;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

@WebServlet(name = "ChatBotServlet", urlPatterns = { "/chat-bot" })
public class ChatBotServlet extends HttpServlet {

    @Override
    protected void doPost(HttpServletRequest request, HttpServletResponse response)
            throws ServletException, IOException {
        response.setContentType("text/plain;charset=UTF-8");
        String message = request.getParameter("message");

        // Gọi hàm xử lý qua Gemini API
        String reply = callGeminiAPI(message);

        try (PrintWriter out = response.getWriter()) {
            out.write(reply);
        }
    }

    private String callGeminiAPI(String message) {
        if (message == null || message.trim().isEmpty()) {
            return "Bạn chưa nhập câu hỏi.";
        }

        String apiKey = "AIzaSyDbva3Jx9Q9w4BfTHGU_Ku3lNWuJiZ2e8U".trim();

        String modelName = "gemini-1.5-flash";
        String apiUrl = "https://generativelanguage.googleapis.com/v1beta/models/" + modelName + ":generateContent?key="
                + apiKey;

        try {
            URL url = new URL(apiUrl);
            HttpURLConnection conn = (HttpURLConnection) url.openConnection();
            conn.setRequestMethod("POST");
            conn.setRequestProperty("Content-Type", "application/json; charset=UTF-8");

            conn.setConnectTimeout(10000);
            conn.setReadTimeout(30000);

            conn.setDoOutput(true);
            String escapedMessage = message.replace("\\", "\\\\")
                    .replace("\"", "\\\"")
                    .replace("\n", "\\n")
                    .replace("\r", "");
            String jsonInputString = "{\"contents\": [{\"parts\": [{\"text\": \"" + escapedMessage + "\"}]}]}";

            try (OutputStream os = conn.getOutputStream()) {
                byte[] input = jsonInputString.getBytes(StandardCharsets.UTF_8);
                os.write(input, 0, input.length);
            }

            // 3. Đọc phản hồi
            int responseCode = conn.getResponseCode();
            if (responseCode == 200) {
                try (BufferedReader br = new BufferedReader(
                        new InputStreamReader(conn.getInputStream(), StandardCharsets.UTF_8))) {
                    StringBuilder responseBuilder = new StringBuilder();
                    String responseLine;
                    while ((responseLine = br.readLine()) != null) {
                        responseBuilder.append(responseLine.trim());
                    }
                    return extractTextFromGeminiResponse(responseBuilder.toString());
                }
            } else {
                // Đọc chi tiết lỗi từ Google để debug
                try (BufferedReader br = new BufferedReader(
                        new InputStreamReader(conn.getErrorStream(), StandardCharsets.UTF_8))) {
                    StringBuilder errorResponse = new StringBuilder();
                    String line;
                    while ((line = br.readLine()) != null) {
                        errorResponse.append(line.trim());
                    }
                    System.err.println("Gemini API Error: " + errorResponse.toString());
                    // Hiển thị chi tiết lỗi để dễ debug
                    return "Lỗi kết nối AI (" + responseCode + "): " + errorResponse.toString();
                }
            }
        } catch (Exception e) {
            e.printStackTrace();
            return "Có lỗi xảy ra khi kết nối với AI: " + e.getMessage();
        }
    }

    // ĐÃ FIX: Dùng Regex để bóc tách JSON thay vì dùng indexOf dễ gây lỗi
    private String extractTextFromGeminiResponse(String json) {
        try {
            // Regex tìm chuỗi nằm giữa "text": " và " không bị escape
            Pattern pattern = Pattern.compile("\"text\":\\s*\"(.*?)(?<!\\\\)\"");
            Matcher matcher = pattern.matcher(json);

            if (matcher.find()) {
                String rawText = matcher.group(1);

                // Giải mã các ký tự escape ngược lại thành chữ bình thường để hiện lên Web
                return rawText.replace("\\n", "\n")
                        .replace("\\\"", "\"")
                        .replace("\\\\", "\\")
                        .replace("\\*", "*"); // Xử lý lỗi in hoa dấu sao của Markdown (tuỳ chọn)
            }
        } catch (Exception e) {
            e.printStackTrace();
        }
        return "Xin lỗi, không thể đọc được câu trả lời từ AI lúc này.";
    }
}