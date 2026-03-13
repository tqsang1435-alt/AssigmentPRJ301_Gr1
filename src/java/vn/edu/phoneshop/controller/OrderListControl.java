package vn.edu.phoneshop.controller;

import jakarta.servlet.ServletException;
import jakarta.servlet.annotation.WebServlet;
import jakarta.servlet.http.HttpServlet;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;
import jakarta.servlet.http.HttpSession;
import java.io.IOException;
import java.util.List;
import vn.edu.phoneshop.dao.OrderDAO;
import vn.edu.phoneshop.model.Order;
import vn.edu.phoneshop.model.User;

@WebServlet(name = "OrderListControl", urlPatterns = { "/order-list" })
public class OrderListControl extends HttpServlet {

    @Override
    protected void doGet(HttpServletRequest request, HttpServletResponse response)
            throws ServletException, IOException {
        HttpSession session = request.getSession();
        User user = (User) session.getAttribute("ACC");
        if (user == null || !"Admin".equalsIgnoreCase(user.getRole())) {
            response.sendRedirect("user-login");
            return;
        }

        OrderDAO orderDAO = new OrderDAO();
        List<Order> orderList = orderDAO.getAllOrdersWithCustomerName();
        request.setAttribute("listOrders", orderList);

        // Đánh dấu trang đang active cho sidebar
        request.setAttribute("activePage", "order-management");

        request.getRequestDispatcher("admin-orders.jsp").forward(request, response);
    }
}