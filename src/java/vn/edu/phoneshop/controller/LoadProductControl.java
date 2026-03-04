/*
 * Click nbfs://nbhost/SystemFileSystem/Templates/Licenses/license-default.txt to change this license
 * Click nbfs://nbhost/SystemFileSystem/Templates/JSP_Servlet/Servlet.java to edit this template
 */
package vn.edu.phoneshop.controller;

import jakarta.servlet.ServletException;
import jakarta.servlet.annotation.WebServlet;
import jakarta.servlet.http.HttpServlet;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;
import java.io.IOException;
import vn.edu.phoneshop.dao.ProductDAO;
import vn.edu.phoneshop.model.Product;

/**
 *
 * @author tqsan
 */
@WebServlet(name = "LoadProductControl", urlPatterns = {"/load-product"})
public class LoadProductControl extends HttpServlet {

    @Override
    protected void doGet(HttpServletRequest request, HttpServletResponse response)
            throws ServletException, IOException {
        String id = request.getParameter("pid");
        ProductDAO dao = new ProductDAO();
        Product p = dao.getProductByID(id);
        
        request.setAttribute("detail", p);
        request.getRequestDispatcher("EditProduct.jsp").forward(request, response);
    }
}
