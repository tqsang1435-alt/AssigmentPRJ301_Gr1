package vn.edu.phoneshop.controller;

import jakarta.servlet.ServletException;
import jakarta.servlet.annotation.WebServlet;
import jakarta.servlet.http.HttpServlet;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;
import java.io.IOException;
import java.io.PrintWriter;

@WebServlet(urlPatterns = { "/handle-error" })
public class ErrorHandler extends HttpServlet {

    @Override
    protected void doGet(HttpServletRequest req, HttpServletResponse resp) throws ServletException, IOException {
        resp.setContentType("text/html;charset=UTF-8");
        PrintWriter out = resp.getWriter();
        String contextPath = req.getContextPath();
        out.println("<!DOCTYPE html>");
        out.println("<html lang='vi'>");
        out.println("<head>");
        out.println("    <meta charset='UTF-8'>");
        out.println("    <title>Đã xảy ra lỗi - PhoneShop</title>");
        out.println("    <link rel='stylesheet' href='" + contextPath + "/assets/css/cssreset.css'>");
        out.println("    <link rel='stylesheet' href='" + contextPath + "/assets/css/base.css'>");
        out.println("    <link rel='stylesheet' href='" + contextPath + "/assets/css/error.css'>");
        out.println(
                "    <link rel='stylesheet' href='https://cdn.jsdelivr.net/gh/lykmapipo/themify-icons@0.1.2/css/themify-icons.css'>");
        out.println("</head>");
        out.println("<body>");
        out.println("    <div class='error-wrapper'>");
        out.println("        <div class='error-card'>");
        out.println("            <i class='ti-face-sad error-icon'></i>");
        out.println("            <div class='error-title'>Ôi hỏng! Có lỗi xảy ra</div>");
        out.println(
                "            <div class='error-desc'>Trang bạn tìm kiếm không tồn tại hoặc hệ thống đang gặp sự cố. Mong bạn thông cảm!</div>");
        out.println("            <a href='" + contextPath + "/' class='btn btn--primary'>");
        out.println("                <i class='ti-home'></i> Về Trang Chủ");
        out.println("            </a>");
        out.println("        </div>");
        out.println("    </div>");
        out.println("</body>");
        out.println("</html>");
    }

    @Override
    protected void doPost(HttpServletRequest req, HttpServletResponse resp) throws ServletException, IOException {
        doGet(req, resp);
    }
}
