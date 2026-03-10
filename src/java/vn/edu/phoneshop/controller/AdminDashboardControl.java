package vn.edu.phoneshop.controller;

import jakarta.servlet.ServletException;
import jakarta.servlet.annotation.WebServlet;
import jakarta.servlet.http.HttpServlet;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;
import jakarta.servlet.http.HttpSession;
import java.io.IOException;
import vn.edu.phoneshop.model.User;

@WebServlet(name = "AdminDashboardControl", urlPatterns = { "/admin-dashboard", "/admin", "/dashboard", "/admin-home" })
public class AdminDashboardControl extends HttpServlet {

    @Override
    protected void doGet(HttpServletRequest request, HttpServletResponse response)
            throws ServletException, IOException {
        HttpSession session = request.getSession();
        User user = (User) session.getAttribute("ACC");

        // Kiểm tra quyền Admin
        if (user == null || !"Admin".equalsIgnoreCase(user.getRole())) {
            response.sendRedirect("user-login");
            return;
        }

        // Chuyển hướng đến trang Dashboard JSP
        request.getRequestDispatcher("admin-dashboard.jsp").forward(request, response);
    }
}