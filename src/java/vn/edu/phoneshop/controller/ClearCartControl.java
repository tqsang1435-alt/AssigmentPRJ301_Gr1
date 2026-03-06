package vn.edu.phoneshop.controller;

import java.io.IOException;
import jakarta.servlet.ServletException;
import jakarta.servlet.annotation.WebServlet;
import jakarta.servlet.http.HttpServlet;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;
import jakarta.servlet.http.HttpSession;
import vn.edu.phoneshop.model.Cart;

@WebServlet(name = "ClearCartControl", urlPatterns = {"/clear-cart"})
public class ClearCartControl extends HttpServlet {

    protected void doGet(HttpServletRequest request, HttpServletResponse response)
            throws ServletException, IOException {
        // Lấy session
        HttpSession session = request.getSession();

        // Tạo cart mới để xóa toàn bộ giỏ hàng
        Cart cart = new Cart();
        session.setAttribute("cart", cart);

        // Redirect về trang chủ
        response.sendRedirect(request.getContextPath() + "/view-cart");
    }

    protected void doPost(HttpServletRequest request, HttpServletResponse response)
            throws ServletException, IOException {
        doGet(request, response);
    }
}
