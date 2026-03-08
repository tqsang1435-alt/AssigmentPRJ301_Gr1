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
import vn.edu.phoneshop.dao.SupplierDAO;

/**
 *
 * @author tqsan
 */
@WebServlet(name = "DeleteSupplierControl", urlPatterns = { "/delete-supplier" })
public class DeleteSupplierControl extends HttpServlet {

    @Override
    protected void doGet(HttpServletRequest request, HttpServletResponse response)
            throws ServletException, IOException {
        String id = request.getParameter("pid");

        try {
            int pid = Integer.parseInt(id);
            SupplierDAO dao = new SupplierDAO();
            dao.deleteSupplier(pid);
        } catch (Exception e) {
            e.printStackTrace();
        }
        response.sendRedirect("supplier-list");
    }
}
