package vn.edu.phoneshop.controller;

import java.io.IOException;
import jakarta.servlet.ServletException;
import jakarta.servlet.annotation.WebServlet;
import jakarta.servlet.http.HttpServlet;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;
import jakarta.servlet.http.HttpSession;
import vn.edu.phoneshop.dao.ProductDAO;
import vn.edu.phoneshop.model.Cart;
import vn.edu.phoneshop.model.Product;

@WebServlet(name = "AddToCartControl", urlPatterns = {"/add-to-cart"})
public class AddToCartControl extends HttpServlet {

    protected void doGet(HttpServletRequest request, HttpServletResponse response)
            throws ServletException, IOException {
        try {
            // Lấy productID từ request
            String productIDStr = request.getParameter("productID");
            String quantityStr = request.getParameter("quantity");

            if (productIDStr == null || productIDStr.isEmpty()) {
                response.sendRedirect(request.getContextPath() + "/index.jsp");
                return;
            }

            int productID = Integer.parseInt(productIDStr);
            int quantity = 1;
            if (quantityStr != null && !quantityStr.isEmpty()) {
                quantity = Integer.parseInt(quantityStr);
            }

            // Kiểm tra quantity hợp lệ
            if (quantity < 1) {
                quantity = 1;
            }

            // Lấy sản phẩm từ database
            ProductDAO productDAO = new ProductDAO();
            Product product = productDAO.getProductByID(productID);

            if (product != null) {
                // Lấy session
                HttpSession session = request.getSession();

                // Lấy cart từ session, nếu chưa có thì tạo cart mới
                Cart cart = (Cart) session.getAttribute("cart");
                if (cart == null) {
                    cart = new Cart();
                }

                // Thêm sản phẩm vào giỏ hàng
                cart.addProduct(product, quantity);

                // Lưu cart vào session
                session.setAttribute("cart", cart);
            }

            // Redirect về trang trước hoặc trang sản phẩm
            String returnURL = request.getParameter("returnURL");
            if (returnURL == null || returnURL.isEmpty()) {
                returnURL = request.getContextPath() + "/product-list";
            }
            response.sendRedirect(returnURL);

        } catch (NumberFormatException e) {
            response.sendRedirect(request.getContextPath() + "/index.jsp");
        }
    }

    protected void doPost(HttpServletRequest request, HttpServletResponse response)
            throws ServletException, IOException {
        doGet(request, response);
    }
}
