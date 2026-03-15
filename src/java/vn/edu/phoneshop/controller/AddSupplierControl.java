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
@WebServlet(name = "AddSupplierControl", urlPatterns = { "/add-supplier" })
public class AddSupplierControl extends HttpServlet {

    @Override
    protected void doGet(HttpServletRequest request, HttpServletResponse response)
            throws ServletException, IOException {
        request.getRequestDispatcher("AddSupplier.jsp").forward(request, response);
    }

    @Override
    protected void doPost(HttpServletRequest request, HttpServletResponse response)
            throws ServletException, IOException {
        // 1. Lấy tiếng Việt không bị lỗi font
        request.setCharacterEncoding("UTF-8");

        // 2. Lấy dữ liệu từ form
        String name = request.getParameter("name");
        String phone = request.getParameter("phone");
        String email = request.getParameter("email");
        String address = request.getParameter("address");

        try {
            SupplierDAO dao = new SupplierDAO();
            dao.insertSupplier(name, phone, email, address);
        } catch (Exception e) {
            e.printStackTrace();
        }

        response.sendRedirect("supplier-list");
    }
}
