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
import java.util.List;
import vn.edu.phoneshop.dao.ProductDAO;
import vn.edu.phoneshop.model.Product;

/**
 *
 * @author tqsan
 */
@WebServlet(name = "ProductControl", urlPatterns = {"/product-list"})
public class ProductControl extends HttpServlet {

    @Override
    protected void doGet(HttpServletRequest request, HttpServletResponse response)
            throws ServletException, IOException {
        
        try {
            // 1. Lấy thông tin người dùng chọn từ thanh Lọc (nếu có)
            String ram = request.getParameter("ramFilter");
            String rom = request.getParameter("romFilter");
            
            ProductDAO dao = new ProductDAO();
            List<Product> list;

            // 2. Kiểm tra xem người dùng có chọn Lọc hay không
            // Nếu cả 2 ô đều trống (hoặc null) -> Lấy tất cả
            if ((ram == null || ram.isEmpty()) && (rom == null || rom.isEmpty())) {
                list = dao.getAllProducts();
            } else {
                // Nếu có chọn ít nhất 1 ô -> Gọi hàm Lọc động
                list = dao.filterProducts(ram, rom);
            }

            // 3. Đẩy dữ liệu sang JSP và giữ lại lựa chọn của người dùng trên thanh Filter
            request.setAttribute("listP", list);
            request.setAttribute("selectedRam", ram);
            request.setAttribute("selectedRom", rom);
            
            request.getRequestDispatcher("ManagerProduct.jsp").forward(request, response);
            
        } catch (Exception e) {
            e.printStackTrace();
        }
    }

    @Override
    protected void doPost(HttpServletRequest request, HttpServletResponse response)
            throws ServletException, IOException {
        doGet(request, response);
    }
}
