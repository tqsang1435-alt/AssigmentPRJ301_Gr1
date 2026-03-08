package vn.edu.phoneshop.controller;

import java.io.IOException;
import jakarta.servlet.annotation.WebServlet;
import jakarta.servlet.http.HttpServlet;
import vn.edu.phoneshop.dao.ProductDAO;
import vn.edu.phoneshop.model.Cart;
import vn.edu.phoneshop.model.Product;
import jakarta.servlet.ServletException;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;
import jakarta.servlet.http.HttpSession;

@WebServlet(name = "AddToCartControl", urlPatterns = {"/add-to-cart"})
public class AddToCartControl extends HttpServlet {

    protected void doGet(HttpServletRequest request, HttpServletResponse response) throws ServletException, IOException {
        try {
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
            if (quantity < 1) {
                quantity = 1;
            }
            ProductDAO productDAO = new ProductDAO();
            Product product = productDAO.getProductByID(String.valueOf(productID));
            if (product != null) {
                HttpSession session = request.getSession();
                Cart cart = (Cart) session.getAttribute("cart");
                if (cart == null) {
                    cart = new Cart();
                }
                cart.addProduct(product, quantity);
                session.setAttribute("cart", cart);
            }
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
