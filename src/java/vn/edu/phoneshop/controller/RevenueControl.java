package vn.edu.phoneshop.controller;

import jakarta.servlet.ServletException;
import jakarta.servlet.annotation.WebServlet;
import jakarta.servlet.http.HttpServlet;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;
import jakarta.servlet.http.HttpSession;
import java.io.IOException;
import java.util.List;
import vn.edu.phoneshop.dao.StatisticDAO;
import vn.edu.phoneshop.model.RevenueStat;
import vn.edu.phoneshop.model.User;

@WebServlet(name = "RevenueControl", urlPatterns = { "/revenue-stats" })
public class RevenueControl extends HttpServlet {
    @Override
    protected void doGet(HttpServletRequest request, HttpServletResponse response)
            throws ServletException, IOException {
        HttpSession session = request.getSession();
        User user = (User) session.getAttribute("ACC");
        if (user == null || !"Admin".equalsIgnoreCase(user.getRole())) {
            response.sendRedirect("user-login");
            return;
        }

        StatisticDAO dao = new StatisticDAO();
        List<RevenueStat> monthlyRevenue = dao.getMonthlyRevenue();
        request.setAttribute("monthlyRevenue", monthlyRevenue);

        request.getRequestDispatcher("revenue.jsp").forward(request, response);
    }
}