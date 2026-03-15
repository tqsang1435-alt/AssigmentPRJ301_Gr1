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

@WebServlet("/detail")
public class DetailController extends HttpServlet {
    ProductDAO dao = new ProductDAO();

    protected void doGet(HttpServletRequest request, HttpServletResponse response)
            throws ServletException, IOException {
        try {
            String idRaw = request.getParameter("id");
            if (idRaw == null || idRaw.isEmpty()) {
                response.sendRedirect("home");
                return;
            }

            int id = Integer.parseInt(idRaw);
            Product p = dao.getProductByID(id);

            if (p != null) {
                request.setAttribute("product", p);
                request.getRequestDispatcher("detail.jsp").forward(request, response);
            } else {
                response.sendRedirect("home");
            }
        } catch (Exception e) {
            e.printStackTrace();
            response.sendRedirect("home");
        }
    }
}