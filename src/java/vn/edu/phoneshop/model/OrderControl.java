package vn.edu.phoneshop.controller;

import jakarta.servlet.ServletException;
import jakarta.servlet.annotation.WebServlet;
import jakarta.servlet.http.HttpServlet;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;
import java.io.IOException;
import vn.edu.phoneshop.dao.OrderDAO;
import vn.edu.phoneshop.dao.UserDAO;
import vn.edu.phoneshop.model.Order;

@WebServlet(name = "OrderControl", urlPatterns = { "/update-order-status" })
public class OrderControl extends HttpServlet {

    protected void processRequest(HttpServletRequest request, HttpServletResponse response)
            throws ServletException, IOException {
        response.setContentType("text/html;charset=UTF-8");

        try {
            int orderId = Integer.parseInt(request.getParameter("orderId"));
            int status = Integer.parseInt(request.getParameter("status"));

            // 2. Cập nhật trạng thái đơn hàng trong DB
            OrderDAO orderDAO = new OrderDAO();
            orderDAO.updateOrderStatus(orderId, status);

            // 4. Quay lại trang quản lý đơn hàng
            response.sendRedirect("admin-orders.jsp");

        } catch (Exception e) {
            e.printStackTrace();
            response.sendRedirect("error.jsp");
        }
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
