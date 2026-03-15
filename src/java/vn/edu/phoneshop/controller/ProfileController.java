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

@WebServlet(name = "ProfileController", urlPatterns = {"/profile"})
public class ProfileController extends HttpServlet {

    @Override
    protected void doGet(HttpServletRequest request, HttpServletResponse response)
            throws ServletException, IOException {

        HttpSession session = request.getSession();
        User user = (User) session.getAttribute("ACC");

        if (user == null) {
            response.sendRedirect("login");
            return;
        }

        // Cập nhật thông tin mới nhất từ DB (để hiển thị đúng điểm và hạng)
        UserDAO dao = new UserDAO();
        User latestUser = dao.getUserByID(user.getUserID());
        if (latestUser != null) {
            session.setAttribute("ACC", latestUser);
        }

        request.getRequestDispatcher("profile.jsp").forward(request, response);
    }
}
