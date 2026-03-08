package vn.edu.phoneshop.controller;

import java.io.IOException;
import jakarta.servlet.ServletException;
import jakarta.servlet.annotation.WebServlet;
import jakarta.servlet.http.HttpServlet;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;
import jakarta.servlet.http.HttpSession;
import vn.edu.phoneshop.model.Cart;

@WebServlet(name = "RemoveFromCartControl", urlPatterns = {"/remove-from-cart"})
public class RemoveFromCartControl extends HttpServlet {

    protected void doGet(HttpServletRequest request, HttpServletResponse response)
            throws ServletException, IOException {
        try {
            // Lấy productID từ request
            String productIDStr = request.getParameter("productID");

            if (productIDStr == null || productIDStr.isEmpty()) {
                response.sendRedirect(request.getContextPath() + "/view-cart");
                return;
            }

            int productID = Integer.parseInt(productIDStr);

            // Lấy session
            HttpSession session = request.getSession();

            // Lấy cart từ session
            Cart cart = (Cart) session.getAttribute("cart");
            if (cart != null) {
                // Xóa sản phẩm khỏi giỏ hàng
                cart.removeProduct(productID);

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
