package vn.edu.phoneshop.controller;

import jakarta.servlet.ServletException;
import jakarta.servlet.annotation.WebServlet;
import jakarta.servlet.http.HttpServlet;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;
import jakarta.servlet.http.HttpSession;
import java.io.IOException;
import java.util.List;
import java.util.Map;
import vn.edu.phoneshop.dao.OrderDAO;
import vn.edu.phoneshop.model.Order;
import vn.edu.phoneshop.model.OrderDetail;
import vn.edu.phoneshop.model.User;

@WebServlet(name = "OrderController", urlPatterns = { "/order-history", "/order-detail" })
public class OrderController extends HttpServlet {

    @Override
    protected void doGet(HttpServletRequest request, HttpServletResponse response)
            throws ServletException, IOException {
        HttpSession session = request.getSession();
        User user = (User) session.getAttribute("ACC");

        // 1. Kiểm tra đăng nhập, điều hướng về trang login chuẩn
        if (user == null) {
            response.sendRedirect("user-login");
            return;
        }

        String path = request.getServletPath();
        OrderDAO orderDAO = new OrderDAO();

        if ("/order-detail".equals(path)) {
            String idRaw = request.getParameter("id");
            if (idRaw == null) {
                response.sendRedirect("order-history");
                return;
            }

            try {
                int orderId = Integer.parseInt(idRaw);
                Order order = orderDAO.getOrderById(orderId);

                // 2. Bảo mật: Đảm bảo người dùng chỉ xem được đơn hàng của chính mình
                if (order != null && order.getUserId() == user.getUserID()) {
                    List<OrderDetail> details = orderDAO.getOrderDetailByOrderID(orderId);
                    Map<Integer, String> pNames = orderDAO.getProductNamesMap(orderId);

                    request.setAttribute("order", order);
                    request.setAttribute("detailList", details);
                    request.setAttribute("productNames", pNames);
                    request.getRequestDispatcher("order-detail.jsp").forward(request, response);
                } else {
                    // Đơn hàng không tồn tại hoặc không thuộc quyền sở hữu
                    response.sendRedirect("order-history");
                }
            } catch (NumberFormatException e) {
                response.sendRedirect("order-history");
            }
        } else { // Mặc định là trang lịch sử đơn hàng
            List<Order> listOrders = orderDAO.getOrdersByUserId(user.getUserID());
            request.setAttribute("listOrders", listOrders);
            request.getRequestDispatcher("order-history.jsp").forward(request, response);
        }
    }
}