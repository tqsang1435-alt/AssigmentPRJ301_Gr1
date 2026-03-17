package vn.edu.phoneshop.controller;

import jakarta.servlet.ServletException;
import jakarta.servlet.annotation.WebServlet;
import jakarta.servlet.http.HttpServlet;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;
import jakarta.servlet.http.HttpSession;
import java.io.IOException;
import vn.edu.phoneshop.dao.OrderDAO;
import vn.edu.phoneshop.model.Order;
import vn.edu.phoneshop.model.User;

@WebServlet(name = "CancelOrderControl", urlPatterns = {"/cancel-order"})
public class CancelOrderControl extends HttpServlet {

    protected void processRequest(HttpServletRequest request, HttpServletResponse response)
            throws ServletException, IOException {
        response.setContentType("text/html;charset=UTF-8");
        
        HttpSession session = request.getSession();
        User user = (User) session.getAttribute("ACC");

        if (user == null) {
            response.sendRedirect("user-login");
            return;
        }

        try {
            int orderId = Integer.parseInt(request.getParameter("id"));
            OrderDAO dao = new OrderDAO();
            Order order = dao.getOrderById(orderId);
            
            if (order != null && order.getUserId() == user.getUserID() && order.getStatus() == 0) {
                dao.updateOrderStatus(orderId, 3); // 3 = Đã hủy
                session.setAttribute("successMessage", "Hủy đơn hàng #" + orderId + " thành công.");
            } else {
                session.setAttribute("errorMessage", "Không thể hủy đơn hàng này.");
            }
        } catch (Exception e) {
            session.setAttribute("errorMessage", "Lỗi: " + e.getMessage());
        }
        
        response.sendRedirect("purchase-history");
    }

    @Override
    protected void doGet(HttpServletRequest request, HttpServletResponse response)
            throws ServletException, IOException {
        processRequest(request, response);
    }

    @Override
    protected void doPost(HttpServletRequest request, HttpServletResponse response)
            throws ServletException, IOException {
        processRequest(request, response);
    }
}
