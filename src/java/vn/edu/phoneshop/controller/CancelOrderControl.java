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

@WebServlet(name = "CancelOrderControl", urlPatterns = { "/cancel-order" })
public class CancelOrderControl extends HttpServlet {

    protected void processRequest(HttpServletRequest request, HttpServletResponse response)
            throws ServletException, IOException {
        response.setContentType("text/html;charset=UTF-8");

        HttpSession session = request.getSession();
        User user = (User) session.getAttribute("ACC");
        String fromAdmin = request.getParameter("from");

        if (user == null) {
            response.sendRedirect("user-login");
            return;
        }

        // Xác định trang để chuyển hướng về sau khi xử lý
        String redirectURL = "admin".equals(fromAdmin) ? "order-list" : "order-history";

        try {
            int orderId = Integer.parseInt(request.getParameter("id"));
            OrderDAO dao = new OrderDAO();
            Order order = dao.getOrderById(orderId);

            if (order == null) {
                session.setAttribute("errorMessage", "Đơn hàng không tồn tại.");
                response.sendRedirect(redirectURL);
                return;
            }

            boolean canCancel = false;
            // Admin có thể hủy đơn hàng đang chờ, đóng gói hoặc đang giao
            if ("Admin".equalsIgnoreCase(user.getRole())) {
                if (order.getStatus() >= 1 && order.getStatus() <= 3) {
                    canCancel = true;
                }
            }
            // Khách hàng chỉ có thể hủy đơn của chính mình khi đang ở trạng thái "Chờ xác
            // nhận" (status = 1)
            else if (order.getUserId() == user.getUserID() && order.getStatus() == 1) {
                canCancel = true;
            }

            if (canCancel) {
                dao.updateOrderStatus(orderId, 0); // 0 = Đã hủy
                session.setAttribute("successMessage", "Đã hủy thành công đơn hàng #" + orderId);
            } else {
                session.setAttribute("errorMessage",
                        "Không thể hủy đơn hàng #" + orderId + ". Đơn hàng đã được xử lý hoặc bạn không có quyền.");
            }
        } catch (Exception e) {
            session.setAttribute("errorMessage", "Đã xảy ra lỗi khi hủy đơn hàng: " + e.getMessage());
        }

        response.sendRedirect(redirectURL);
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
