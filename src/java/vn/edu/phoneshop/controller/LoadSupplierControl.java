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
import vn.edu.phoneshop.model.Supplier;

/**
 *
 * @author tqsan
 */
@WebServlet(name = "LoadSupplierControl", urlPatterns = {"/load-supplier"})
public class LoadSupplierControl extends HttpServlet {

    @Override
    protected void doGet(HttpServletRequest request, HttpServletResponse response)
            throws ServletException, IOException {
        
        // 1. Lấy id từ đường dẫn
        String id = request.getParameter("pid");
        
        // 2. Gọi DAO lấy thông tin chi tiết
        SupplierDAO dao = new SupplierDAO();
        Supplier s = dao.getSupplierByID(id);
        
        // 3. Đẩy thông tin qua trang Edit
        request.setAttribute("detail", s);
        request.getRequestDispatcher("EditSupplier.jsp").forward(request, response);
    }
}
