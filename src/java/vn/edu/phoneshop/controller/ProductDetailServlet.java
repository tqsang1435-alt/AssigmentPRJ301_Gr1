/*
 * Click nbfs://nbhost/SystemFileSystem/Templates/Licenses/license-default.txt to change this license
 * Click nbfs://nbhost/SystemFileSystem/Templates/Classes/Class.java to edit this template
 */
package vn.edu.phoneshop.controller;

/**
 *
 * @author Lenovo
 */
import jakarta.servlet.*;
import jakarta.servlet.http.*;
import jakarta.servlet.annotation.WebServlet;
import java.io.IOException;
import vn.edu.phoneshop.dao.ProductDAO;
import vn.edu.phoneshop.model.Product;

@WebServlet("/product-detail")
public class ProductDetailServlet extends HttpServlet {

    @Override
    protected void doGet(HttpServletRequest request,
            HttpServletResponse response)
            throws ServletException, IOException {

        String idRaw = request.getParameter("id");

        // kiểm tra id hợp lệ
        if (idRaw == null || idRaw.isEmpty()) {
            response.sendRedirect("home");
            return;
        }

        int id;
        try {
            id = Integer.parseInt(idRaw);
        } catch (NumberFormatException e) {
            response.sendRedirect("home");
            return;
        }

        ProductDAO dao = new ProductDAO();
        Product p = dao.getProductByID(id);

        // không có sản phẩm
        if (p == null) {
            response.sendRedirect("home");
            return;
        }

        request.setAttribute("product", p);

        // JSP đúng của bạn
        request.getRequestDispatcher("detail.jsp")
                .forward(request, response);
    }
}
