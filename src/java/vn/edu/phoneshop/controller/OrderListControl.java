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

        // --- Pagination Logic ---
        int page = 1;
        int pageSize = 10; // So don hang moi trang

        String pageParam = request.getParameter("page");
        if (pageParam != null && !pageParam.isEmpty()) {
            try {
                page = Integer.parseInt(pageParam);
            } catch (NumberFormatException e) {
                page = 1;
            }
        }

        int totalOrders = orderDAO.getTotalOrders();
        int totalPages = (int) Math.ceil((double) totalOrders / pageSize);

        if (page < 1) {
            page = 1;
        } else if (page > totalPages && totalPages > 0) {
            page = totalPages;
        }

        int offset = (page - 1) * pageSize;
        List<Order> orderList = orderDAO.getOrdersWithCustomerNamePaginated(offset, pageSize);

        request.setAttribute("listOrders", orderList);
        request.setAttribute("currentPage", page);
        request.setAttribute("totalPages", totalPages);

        // Đánh dấu trang đang active cho sidebar
        request.setAttribute("activePage", "order-management");

        request.getRequestDispatcher("admin-orders.jsp").forward(request, response);
    }
}