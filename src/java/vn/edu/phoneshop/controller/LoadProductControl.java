package vn.edu.phoneshop.controller;

import jakarta.servlet.ServletException;
import jakarta.servlet.annotation.WebServlet;
import jakarta.servlet.http.HttpServlet;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;
import jakarta.servlet.http.HttpSession;
import java.io.IOException;
import vn.edu.phoneshop.dao.ProductDAO;
import vn.edu.phoneshop.model.Product;
import vn.edu.phoneshop.model.User;

@WebServlet(name = "LoadProductControl", urlPatterns = { "/loadProduct" })
public class LoadProductControl extends HttpServlet {

    @Override
    protected void doGet(HttpServletRequest request, HttpServletResponse response)
            throws ServletException, IOException {
        HttpSession session = request.getSession();
        User user = (User) session.getAttribute("ACC");
        if (user == null || !"Admin".equalsIgnoreCase(user.getRole())) {
            response.sendRedirect("user-login");
            return;
        }

        String id = request.getParameter("pid");
        ProductDAO dao = new ProductDAO();
        Product product = dao.getProductByID(Integer.parseInt(id));

        request.setAttribute("detail", product);
        request.getRequestDispatcher("EditProduct.jsp").forward(request, response);
    }
}