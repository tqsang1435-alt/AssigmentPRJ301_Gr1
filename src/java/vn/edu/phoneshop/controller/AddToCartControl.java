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

@WebServlet(name = "AddToCartControl", urlPatterns = { "/add-to-cart" })
public class AddToCartControl extends HttpServlet {

    protected void doGet(HttpServletRequest request, HttpServletResponse response)
            throws ServletException, IOException {
        try {
            String productIDStr = request.getParameter("productID");
            String quantityStr = request.getParameter("quantity");
            if (productIDStr == null) {
                productIDStr = request.getParameter("pid");
            }
            if (productIDStr == null || productIDStr.isEmpty()) {
                response.sendRedirect(request.getContextPath() + "/index.jsp");
                return;
            }
            System.out.println("Input PID: " + productIDStr + ", Quantity: " + quantityStr);
            int productID = Integer.parseInt(productIDStr);
            int quantity = 1;
            if (quantityStr != null && !quantityStr.isEmpty()) {
                quantity = Integer.parseInt(quantityStr);
            }
            if (quantity < 1) {
                quantity = 1;
            }
            ProductDAO productDAO = new ProductDAO();
            Product product = productDAO.getProductByID(productID);
            if (product != null) {
                System.out.println("Product Found: " + product.getProductName());
                HttpSession session = request.getSession();
                Object cartObj = session.getAttribute("cart");
                Cart cart = null;
                if (cartObj != null && cartObj instanceof Cart) {
                    cart = (Cart) cartObj;
                } else {
                    cart = new Cart();
                }
                cart.addProduct(product, quantity);
                session.setAttribute("cart", cart);
                session.setAttribute("cartCount", cart.getTotalQuantity());
                System.out.println("Added to cart. Current Total Quantity: " + cart.getTotalQuantity());
            } else {
                System.out.println("ERROR: Product is NULL for ID = " + productID);
            }

            String returnURL = request.getParameter("returnURL");
            if (returnURL == null || returnURL.isEmpty()) {
                String referer = request.getHeader("Referer");
                returnURL = (referer != null && !referer.isEmpty()) ? referer
                        : request.getContextPath() + "/product-list";
            }
            response.sendRedirect(returnURL);
            System.out.println("--- DEBUG: AddToCartControl END ---");
        } catch (NumberFormatException e) {
            System.out.println("ERROR: NumberFormatException " + e.getMessage());
            response.sendRedirect(request.getContextPath() + "/index.jsp");
        }
    }

    protected void doPost(HttpServletRequest request, HttpServletResponse response)
            throws ServletException, IOException {
        doGet(request, response);
    }
}
