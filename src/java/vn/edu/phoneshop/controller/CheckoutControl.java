package vn.edu.phoneshop.controller;

import java.io.IOException;
import jakarta.servlet.ServletException;
import jakarta.servlet.annotation.WebServlet;
import jakarta.servlet.http.HttpServlet;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;
import jakarta.servlet.http.HttpSession;
import vn.edu.phoneshop.dao.OrderDAO;
import vn.edu.phoneshop.dao.UserDAO;
import vn.edu.phoneshop.model.Cart;
import vn.edu.phoneshop.model.CartItem;
import vn.edu.phoneshop.model.User;

@WebServlet(name = "CheckoutControl", urlPatterns = { "/checkout" })
public class CheckoutControl extends HttpServlet {

    /**
     * Handles the HTTP <code>GET</code> method.
     * This method is for displaying the checkout page.
     */
    @Override
    protected void doGet(HttpServletRequest request, HttpServletResponse response)
            throws ServletException, IOException {
        HttpSession session = request.getSession();

        User account = (User) session.getAttribute("ACC");
        if (account == null) {
            response.sendRedirect("user-login"); // Redirect to login page
            return;
        }

        // Check if cart is empty
        Cart cart = (Cart) session.getAttribute("cart");
        if (cart == null || cart.getTotalQuantity() == 0) {
            // Redirect to cart page with an error message
            request.setAttribute("mess", "Giỏ hàng của bạn đang trống, không thể thanh toán!");
            request.getRequestDispatcher("/view-cart").forward(request, response);
            return;
        }

        // Forward to the checkout page to display it
        request.getRequestDispatcher("checkout.jsp").forward(request, response);
    }

    /**
     * Handles the HTTP <code>POST</code> method.
     * This method is for processing the order when the user submits the checkout
     * form.
     */
    @Override
    protected void doPost(HttpServletRequest request, HttpServletResponse response)
            throws ServletException, IOException {
        request.setCharacterEncoding("UTF-8");
        HttpSession session = request.getSession();

        User user = (User) session.getAttribute("ACC");
        Cart cart = (Cart) session.getAttribute("cart");

        if (user == null) {
            response.sendRedirect("user-login");
            return;
        }

        if (cart == null || cart.isEmpty()) {
            response.sendRedirect("view-cart");
            return;
        }

        String name = request.getParameter("name");
        String phone = request.getParameter("phone");
        String address = request.getParameter("address");

        // 1. Kiểm tra dữ liệu nhập vào có bị trống không
        if (name == null || name.trim().isEmpty() || phone == null || phone.trim().isEmpty() || address == null
                || address.trim().isEmpty()) {
            request.setAttribute("mess", "Vui lòng điền đầy đủ thông tin nhận hàng (Họ tên, SĐT, Địa chỉ)!");
            request.getRequestDispatcher("checkout.jsp").forward(request, response);
            return;
        }

        // 2. Kiểm tra định dạng số điện thoại (10-11 chữ số)
        if (!phone.matches("\\d{10,11}")) {
            request.setAttribute("mess", "Số điện thoại không hợp lệ (phải là 10-11 số)!");
            request.getRequestDispatcher("checkout.jsp").forward(request, response);
            return;
        }

        double totalMoney = cart.getTotalPrice();

        OrderDAO dao = new OrderDAO();
        // Tạo đơn hàng mới với trạng thái 1 (Chờ xác nhận)
        int orderId = dao.createOrder(user.getUserID(), totalMoney, 1);

        if (orderId > 0) {
            dao.updateOrderStatusAndAddress(orderId, 1, address);
            for (CartItem item : cart.getCartItems()) {
                dao.insertOrderDetail(orderId, item.getProduct().getProductID(), item.getQuantity(),
                        item.getProduct().getPrice());
            }
            session.removeAttribute("cart");
            session.setAttribute("cartCount", 0);
            int pointsEarned = (int) (totalMoney / 100000);
            UserDAO userDAO = new UserDAO();
            userDAO.updateRewardPoints(user.getUserID(), totalMoney);
            user.setRewardPoints(user.getRewardPoints() + pointsEarned);
            int newPoints = user.getRewardPoints();
            if (newPoints >= 2000)
                user.setCustomerType("Diamond");
            else if (newPoints >= 1000)
                user.setCustomerType("Gold");
            else if (newPoints >= 500)
                user.setCustomerType("Silver");
            else if (newPoints >= 100)
                user.setCustomerType("Bronze");
            else
                user.setCustomerType("New Member");

            session.setAttribute("ACC", user);

            request.setAttribute("message", "Đơn hàng của bạn đã được gửi đến địa chỉ: " + address
                    + ". Bạn đã tích lũy được " + pointsEarned + " điểm.");
            request.getRequestDispatcher("thanks.jsp").forward(request, response);
        } else {
            response.sendRedirect("home");
        }
    }
}