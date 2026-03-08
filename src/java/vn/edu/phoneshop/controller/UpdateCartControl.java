package vn.edu.phoneshop.controller;

import java.io.IOException;
import jakarta.servlet.ServletException;
import jakarta.servlet.annotation.WebServlet;
import jakarta.servlet.http.HttpServlet;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;
import jakarta.servlet.http.HttpSession;
import vn.edu.phoneshop.model.Cart;

@WebServlet(name = "UpdateCartControl", urlPatterns = {"/update-cart"})
public class UpdateCartControl extends HttpServlet {

    protected void doGet(HttpServletRequest request, HttpServletResponse response)
            throws ServletException, IOException {
        try {
            // Lấy productID và quantity từ request
            String productIDStr = request.getParameter("productID");
            String quantityStr = request.getParameter("quantity");

            if (productIDStr == null || productIDStr.isEmpty() || quantityStr == null || quantityStr.isEmpty()) {
                response.sendRedirect(request.getContextPath() + "/view-cart");
                return;
            }

            int productID = Integer.parseInt(productIDStr);
            int quantity = Integer.parseInt(quantityStr);

            // Kiểm tra quantity hợp lệ
            if (quantity < 1) {
                // Nếu quantity < 1, xóa sản phẩm
                response.sendRedirect(request.getContextPath() + "/remove-from-cart?productID=" + productID);
                return;
            }

            // Lấy session
            HttpSession session = request.getSession();

            // Lấy cart từ session
            Cart cart = (Cart) session.getAttribute("cart");
            if (cart != null) {
                // Cập nhật số lượng
                cart.updateQuantity(productID, quantity);

                // Lưu cart vào session
                session.setAttribute("cart", cart);
            }

            // Redirect về trang giỏ hàng
            response.sendRedirect(request.getContextPath() + "/view-cart");

        } catch (NumberFormatException e) {
            response.sendRedirect(request.getContextPath() + "/view-cart");
        }
    }

    protected void doPost(HttpServletRequest request, HttpServletResponse response)
            throws ServletException, IOException {
        doGet(request, response);
    }
}
