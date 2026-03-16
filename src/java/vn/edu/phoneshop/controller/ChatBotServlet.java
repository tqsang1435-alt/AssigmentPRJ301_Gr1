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
import java.util.List;
import vn.edu.phoneshop.dao.ProductDAO;
import vn.edu.phoneshop.model.Product;

@WebServlet(name = "ChatBotServlet", urlPatterns = { "/chat-bot" })
public class ChatBotServlet extends HttpServlet {

    @Override
    protected void doPost(HttpServletRequest request, HttpServletResponse response)
            throws ServletException, IOException {
        response.setContentType("text/plain;charset=UTF-8");
        String message = request.getParameter("message");
        String reply = callGeminiAPI(message);

        try (PrintWriter out = response.getWriter()) {
            out.write(reply);
        }
    }

    private String callGeminiAPI(String message) {
        if (message == null || message.trim().isEmpty()) {
            return "Bạn chưa nhập câu hỏi.";
        }

        // Đọc API Key từ biến môi trường của hệ điều hành
        String apiKey = System.getenv("GEMINI_API_KEY");
        // Kiểm tra xem đã lấy được key chưa (chống lỗi sập web nếu quên cài biến)
        if (apiKey == null || apiKey.trim().isEmpty()) {
            System.err.println("LỖI: Chưa cấu hình biến môi trường GEMINI_API_KEY!");
            return "Hệ thống đang bảo trì (Lỗi thiếu API Key).";
        }

        apiKey = apiKey.trim();

        String modelName = "gemini-2.5-flash";
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

            String context = getProductContext();
            String fullPrompt = context + "\n\nKhách hàng hỏi: " + message;
            String escapedMessage = fullPrompt.replace("\\", "\\\\")
                    .replace("\"", "\\\"")
                    .replace("\n", "\\n")
                    .replace("\r", "");
            String jsonInputString = "{\"contents\": [{\"parts\": [{\"text\": \"" + escapedMessage + "\"}]}]}";

            try (OutputStream os = conn.getOutputStream()) {
                byte[] input = jsonInputString.getBytes(StandardCharsets.UTF_8);
                os.write(input, 0, input.length);
            }

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
                try (BufferedReader br = new BufferedReader(
                        new InputStreamReader(conn.getErrorStream(), StandardCharsets.UTF_8))) {
                    StringBuilder errorResponse = new StringBuilder();
                    String line;
                    while ((line = br.readLine()) != null) {
                        errorResponse.append(line.trim());
                    }
                    System.err.println("Gemini API Error: " + errorResponse.toString());
                    return "Lỗi kết nối AI (" + responseCode + "): " + errorResponse.toString();
                }
            }
        } catch (Exception e) {
            e.printStackTrace();
            return "Có lỗi xảy ra khi kết nối với AI: " + e.getMessage();
        }
    }

    private String getProductContext() {
        try {
            ProductDAO dao = new ProductDAO();
            List<Product> list = dao.getAllActiveProducts();
            StringBuilder sb = new StringBuilder();
            sb.append(
                    "Bạn là nhân viên tư vấn nhiệt tình của PhoneShop. Dưới đây là danh sách điện thoại đang bán (Tên, Giá, Cấu hình):\n");

            for (Product p : list) {
                sb.append(String.format("- %s: Giá %,.0f VNĐ. (RAM: %s, ROM: %s, Màu: %s)\n",
                        p.getProductName(), p.getPrice(), p.getRam(), p.getRom(), p.getColor()));
            }
            sb.append(
                    "\nHãy dùng thông tin trên để tư vấn. Nếu khách hỏi sản phẩm không có trong danh sách, hãy báo cửa hàng chưa kinh doanh dòng đó.");
            return sb.toString();
        } catch (Exception e) {
            e.printStackTrace();
            return "";
        }
    }

    private String extractTextFromGeminiResponse(String json) {
        try {
            Pattern pattern = Pattern.compile("\"text\":\\s*\"(.*?)(?<!\\\\)\"");
            Matcher matcher = pattern.matcher(json);

            if (matcher.find()) {
                String rawText = matcher.group(1);
                return rawText.replace("\\n", "\n")
                        .replace("\\\"", "\"")
                        .replace("\\\\", "\\")
                        .replace("\\*", "*");
            }
        } catch (Exception e) {
            e.printStackTrace();
        }
        return "Xin lỗi, không thể đọc được câu trả lời từ AI lúc này.";
    }
}