/*
 * Click nbfs://nbhost/SystemFileSystem/Templates/Licenses/license-default.txt to change this license
 * Click nbfs://nbhost/SystemFileSystem/Templates/Classes/Class.java to edit this template
 */
package vn.edu.phoneshop.controller;

/**
 *
 * @author Lenovo
 */
import jakarta.servlet.*;
import jakarta.servlet.http.*;
import jakarta.servlet.annotation.WebServlet;
import java.io.IOException;
import vn.edu.phoneshop.dao.RegisterDAO;

@WebServlet("/register")
public class RegisterController extends HttpServlet {

    private RegisterDAO dao = new RegisterDAO();

    @Override
    protected void doPost(HttpServletRequest request, HttpServletResponse response)
            throws ServletException, IOException {

        String fullName = request.getParameter("fullName");
        String email = request.getParameter("email");
        String phone = request.getParameter("phoneNumber");
        String address = request.getParameter("address");
        String password = request.getParameter("password");

        // ===== VALIDATE =====
        if (fullName == null || fullName.trim().length() < 6) {
            request.setAttribute("error", "Tên phải ít nhất 6 ký tự");
            request.getRequestDispatcher("register.jsp").forward(request, response);
            return;
        }

        if (email == null || !email.endsWith("@gmail.com")) {
            request.setAttribute("error", "Email phải có đuôi @gmail.com");
            request.getRequestDispatcher("register.jsp").forward(request, response);
            return;
        }

        if (password == null || password.length() < 6) {
            request.setAttribute("error", "Mật khẩu phải ≥ 6 ký tự");
            request.getRequestDispatcher("register.jsp").forward(request, response);
            return;
        }

        if (phone == null || !phone.matches("\\d{10}")) {
            request.setAttribute("error", "Số điện thoại phải đúng 10 số");
            request.getRequestDispatcher("register.jsp").forward(request, response);
            return;
        }

        if (dao.emailExists(email)) {
            request.setAttribute("error", "Email đã tồn tại");
            request.getRequestDispatcher("register.jsp").forward(request, response);
            return;
        }

        if (dao.phoneExists(phone)) {
            request.setAttribute("error", "Số điện thoại đã tồn tại");
            request.getRequestDispatcher("register.jsp").forward(request, response);
            return;
        }

        // ===== INSERT DB =====
        boolean ok = dao.register(fullName, email, phone, address, password);

        if (ok) {
            response.sendRedirect("login.jsp?register=success");
        } else {
            request.setAttribute("error", "Đăng ký thất bại");
            request.getRequestDispatcher("register.jsp").forward(request, response);
        }
    }
}
