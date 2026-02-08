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
@WebServlet(name = "UpdateSupplierControl", urlPatterns = {"/update-supplier"})
public class UpdateSupplierControl extends HttpServlet {

    @Override
    protected void doPost(HttpServletRequest request, HttpServletResponse response)
            throws ServletException, IOException {
        
        request.setCharacterEncoding("UTF-8");
        
        // 1. Lấy dữ liệu từ form Edit
        String id = request.getParameter("id");
        String name = request.getParameter("name");
        String phone = request.getParameter("phone");
        String email = request.getParameter("email");
        String address = request.getParameter("address");
        
        // 2. Gọi DAO update
        SupplierDAO dao = new SupplierDAO();
        dao.updateSupplier(id, name, phone, email, address);
        
        // 3. Quay về trang danh sách
        response.sendRedirect("supplier-list");
    }
}