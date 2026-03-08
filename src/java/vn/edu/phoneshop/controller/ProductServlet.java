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

        String name = request.getParameter("name");
        double price = Double.parseDouble(request.getParameter("price"));
        int quantity = Integer.parseInt(request.getParameter("quantity"));
        String description = request.getParameter("description");
        String image = request.getParameter("image");

        Product p = new Product();
        p.setProductName(name);
        p.setPrice(price);
        p.setQuantity(quantity);
        p.setDescription(description);
        p.setImage(image);

        dao.insertProduct(p);

        response.sendRedirect("products");
    }
}