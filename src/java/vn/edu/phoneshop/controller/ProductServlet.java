package vn.edu.phoneshop.controller;

import jakarta.servlet.ServletException;
import jakarta.servlet.annotation.WebServlet;
import jakarta.servlet.http.*;
import java.io.IOException;
import java.util.List;

import vn.edu.phoneshop.dao.ProductDAO;
import vn.edu.phoneshop.model.Product;

@WebServlet("/products")
public class ProductServlet extends HttpServlet {

    private ProductDAO dao = new ProductDAO();

    // ==============================
    // HIỂN THỊ DANH SÁCH + XOÁ
    // ==============================
    @Override
    protected void doGet(HttpServletRequest request, HttpServletResponse response)
            throws ServletException, IOException {

        String action = request.getParameter("action");

        // ===== XOÁ SẢN PHẨM =====
        if ("delete".equals(action)) {
            int id = Integer.parseInt(request.getParameter("id"));
            dao.deleteProduct(id);
            response.sendRedirect("products");
            return;
        }

        // ===== HIỂN THỊ DANH SÁCH =====
        List<Product> list = dao.getAllProducts();

        request.setAttribute("list", list);   // ⚠ phải trùng với JSP
        request.getRequestDispatcher("product-list.jsp")
               .forward(request, response);
    }

    // ==============================
    // THÊM SẢN PHẨM
    // ==============================
    @Override
    protected void doPost(HttpServletRequest request, HttpServletResponse response)
            throws ServletException, IOException {
        
        // This method seems to be deprecated or incomplete. 
        // The main functionality for adding a product is in AddProductControl.
        // Redirecting to the product list to avoid errors.
        response.sendRedirect("products");
    }
}