package vn.edu.phoneshop.controller;

import java.io.IOException;
import jakarta.servlet.ServletException;
import jakarta.servlet.annotation.WebServlet;
import jakarta.servlet.http.HttpServlet;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;
import jakarta.servlet.http.HttpSession;
import vn.edu.phoneshop.model.Cart;

@WebServlet(name = "ViewCartControl", urlPatterns = { "/view-cart", "/cart" })
public class ViewCartControl extends HttpServlet {

    protected void doGet(HttpServletRequest request, HttpServletResponse response)
            throws ServletException, IOException {
        HttpSession session = request.getSession();
        Object cartObj = session.getAttribute("cart");
        Cart cart = null;
        if (cartObj != null && cartObj instanceof Cart) {
            cart = (Cart) cartObj;
            System.out.println("Cart found in session. Total items: " + cart.getTotalQuantity());
        } else {
            System.out.println("Cart NOT found in session (or invalid type). Creating new cart.");
            cart = new Cart();
            session.setAttribute("cart", cart);
            session.setAttribute("cartCount", 0);
        }

        request.setAttribute("cart", cart);
        request.getRequestDispatcher("/cart.jsp").forward(request, response);
    }

    protected void doPost(HttpServletRequest request, HttpServletResponse response)
            throws ServletException, IOException {
        doGet(request, response);
    }
}
