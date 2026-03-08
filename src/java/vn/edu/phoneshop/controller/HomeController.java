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
import java.util.List;
import vn.edu.phoneshop.dao.ProductDAO;
import vn.edu.phoneshop.model.Product;

@WebServlet("/home")
public class HomeController extends HttpServlet {

    private ProductDAO dao = new ProductDAO();

   protected void doGet(HttpServletRequest request, HttpServletResponse response) 
        throws ServletException, IOException {
    String cidRaw = request.getParameter("cid");
    ProductDAO dao = new ProductDAO();
    List<Product> list;

    if (cidRaw == null || cidRaw.isEmpty()) {
        list = dao.getAllActiveProducts();
    } else {
        int cid = Integer.parseInt(cidRaw);
        list = dao.getProductsByCategoryId(cid);
    }

    request.setAttribute("listP", list);
    request.getRequestDispatcher("home.jsp").forward(request, response);
}
}
