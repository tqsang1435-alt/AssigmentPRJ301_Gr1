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

@WebServlet(name = "UpdateCartControl", urlPatterns = { "/update-cart" })
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

            if (quantity < 1) {
                response.sendRedirect(request.getContextPath() + "/remove-from-cart?productID=" + productID);
                return;
            }

            HttpSession session = request.getSession();

            Cart cart = (Cart) session.getAttribute("cart");
            if (cart != null) {
                ProductDAO dao = new ProductDAO();
                Product p = dao.getProductByID(productID);
                if (p != null) {
                    if (quantity > p.getStockQuantity()) {
                        quantity = p.getStockQuantity();
                    }
                }

                cart.updateQuantity(productID, quantity);
                session.setAttribute("cart", cart);
                session.setAttribute("cartCount", cart.getTotalQuantity());
            }

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
