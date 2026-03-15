package vn.edu.phoneshop.controller;

import jakarta.servlet.ServletException;
import jakarta.servlet.annotation.WebServlet;
import jakarta.servlet.http.HttpServlet;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;
import jakarta.servlet.http.HttpSession;
import java.io.IOException;
import vn.edu.phoneshop.dao.UserDAO;
import vn.edu.phoneshop.model.User;

@WebServlet(name = "AdminLoginControl", urlPatterns = {"/admin-login"})
public class AdminLoginControl extends HttpServlet {

    @Override
    protected void doGet(HttpServletRequest request, HttpServletResponse response)
            throws ServletException, IOException {
        HttpSession session = request.getSession(false);
        if (session != null && session.getAttribute("ACC") != null) {
            User user = (User) session.getAttribute("ACC");
            if ("Admin".equalsIgnoreCase(user.getRole())) {
                response.sendRedirect("admin-dashboard");
                return;
            }
        }
        request.getRequestDispatcher("admin-login.jsp").forward(request, response);
    }

    @Override
    protected void doPost(HttpServletRequest request, HttpServletResponse response)
            throws ServletException, IOException {
        String email = request.getParameter("email");
        String pass = request.getParameter("password");

        UserDAO dao = new UserDAO();
        User user = dao.login(email, pass);

        if (user != null) {
            if ("Admin".equalsIgnoreCase(user.getRole())) {
                HttpSession session = request.getSession();
                session.setAttribute("ACC", user);
                session.setAttribute("ROLE", user.getRole());
                session.setMaxInactiveInterval(24 * 60 * 60);
                response.sendRedirect("admin-dashboard");
            } else {
                request.setAttribute("mess", "Tài khoản của bạn không có đặc quyền quản trị!");
                request.getRequestDispatcher("admin-login.jsp").forward(request, response);
            }
        } else {
            request.setAttribute("mess", "Sai email hoặc mật khẩu!");
            request.getRequestDispatcher("admin-login.jsp").forward(request, response);
        }
    }
}
