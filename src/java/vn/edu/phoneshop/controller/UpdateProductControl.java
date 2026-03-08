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

/**
 *
 * @author tqsan
 */
@WebServlet(name = "UpdateProductControl", urlPatterns = {"/update-product"})
public class UpdateProductControl extends HttpServlet {
    
    @Override
    protected void doPost(HttpServletRequest request, HttpServletResponse response)
            throws ServletException, IOException {
        
        request.setCharacterEncoding("UTF-8");
        
        try {
            // Lấy id (bắt buộc phải có để biết update dòng nào)
            String id = request.getParameter("id");

            // Lấy các dữ liệu khác
            String name = request.getParameter("name");
            String priceStr = request.getParameter("price");
            String quantityStr = request.getParameter("quantity");
            String desc = request.getParameter("description");
            String img = request.getParameter("image");
            String cateIdStr = request.getParameter("category");
            String suppIdStr = request.getParameter("supplier");
            String ram = request.getParameter("ram");
            String rom = request.getParameter("rom");
            String color = request.getParameter("color");

            // Ép kiểu
            double price = Double.parseDouble(priceStr);
            int quantity = Integer.parseInt(quantityStr);
            int cateId = Integer.parseInt(cateIdStr);
            int suppId = Integer.parseInt(suppIdStr);

            // Gọi DAO update
            ProductDAO dao = new ProductDAO();
            dao.updateProduct(Integer.parseInt(id), name, price, quantity, desc, img, cateId, suppId, ram, rom, color);
            
            response.sendRedirect("product-list");
            
        } catch (Exception e) {
            e.printStackTrace();
        }
    }
}
