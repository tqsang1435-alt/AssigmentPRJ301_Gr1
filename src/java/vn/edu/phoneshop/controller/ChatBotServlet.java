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
        String mode = request.getParameter("mode");

        String reply;
        if ("admin".equals(mode)) {
            reply = callGeminiAPIForAdmin(message);
        } else {
            reply = callGeminiAPI(message);
        }

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

    private String callGeminiAPIForAdmin(String message) {
        if (message == null || message.trim().isEmpty()) {
            return "Không có dữ liệu để phân tích.";
        }

        String apiKey = System.getenv("GEMINI_API_KEY");
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

            String context = getAdminContext();
            String fullPrompt = context + "\n\nAdmin hỏi: " + message;

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
                return "Lỗi kết nối AI (" + responseCode + ")";
            }
        } catch (Exception e) {
            e.printStackTrace();
            return "Có lỗi xảy ra khi kết nối với AI: " + e.getMessage();
        }
    }

    private String getAdminContext() {
        StringBuilder sb = new StringBuilder();
        sb.append(
                "Bạn là trợ lý AI thông minh dành riêng cho Admin của PhoneShop. Dưới đây là dữ liệu hệ thống hiện tại:\n");
        try {
            vn.edu.phoneshop.dao.ProductDAO dao = new vn.edu.phoneshop.dao.ProductDAO();
            java.sql.Connection conn = null;
            dao.getAllActiveProducts(); // Kích hoạt kết nối (nếu cần)
            Class<?> clazz = dao.getClass();
            while (clazz != null) {
                for (java.lang.reflect.Field field : clazz.getDeclaredFields()) {
                    if (field.getType() == java.sql.Connection.class) {
                        field.setAccessible(true);
                        conn = (java.sql.Connection) field.get(dao);
                        break;
                    }
                }
                if (conn != null)
                    break;
                clazz = clazz.getSuperclass();
            }

            if (conn != null && !conn.isClosed()) {
                sb.append("- TOP KHÁCH HÀNG MUA NHIỀU NHẤT (dựa trên điểm tích lũy):\n");
                try (java.sql.Statement stmt = conn.createStatement();
                        java.sql.ResultSet rs = stmt.executeQuery(
                                "SELECT TOP 5 UserID, FullName, RewardPoints FROM Users ORDER BY RewardPoints DESC")) {
                    while (rs.next()) {
                        sb.append(String.format("  + ID: %s | Tên: %s | Điểm: %d\n", rs.getString("UserID"),
                                rs.getString("FullName"), rs.getInt("RewardPoints")));
                    }
                } catch (Exception e) {
                    try (java.sql.Statement stmt = conn.createStatement();
                            java.sql.ResultSet rs = stmt.executeQuery(
                                    "SELECT TOP 5 UserID, FullName, RewardPoints FROM [User] ORDER BY RewardPoints DESC")) {
                        while (rs.next()) {
                            sb.append(String.format("  + ID: %s | Tên: %s | Điểm: %d\n", rs.getString("UserID"),
                                    rs.getString("FullName"), rs.getInt("RewardPoints")));
                        }
                    } catch (Exception ex) {
                        sb.append("  (Không thể tải dữ liệu khách hàng do sai tên bảng)\n");
                    }
                }
            }
        } catch (Exception e) {
        }

        sb.append(
                "\nHãy dùng dữ liệu trên để trả lời câu hỏi của Admin. Nếu admin yêu cầu thực hiện thao tác (thêm/sửa/xóa), hãy lịch sự từ chối và báo rằng bạn hiện tại chỉ có quyền đọc dữ liệu (Read-only).\n");
        return sb.toString();
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