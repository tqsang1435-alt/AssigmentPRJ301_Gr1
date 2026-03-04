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
@WebServlet(name = "AddProductControl", urlPatterns = {"/add-product"})
public class AddProductControl extends HttpServlet {

    // Hiển thị form thêm sản phẩm
    @Override
    protected void doGet(HttpServletRequest request, HttpServletResponse response)
            throws ServletException, IOException {
        request.getRequestDispatcher("AddProduct.jsp").forward(request, response);
    }

    // Xử lý dữ liệu khi bấm nút "Lưu lại"
    @Override
    protected void doPost(HttpServletRequest request, HttpServletResponse response)
            throws ServletException, IOException {
        
        request.setCharacterEncoding("UTF-8"); // Xử lý tiếng Việt

        try {
            // 1. Lấy dữ liệu dạng chuỗi từ form
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

            // 2. Chuyển đổi kiểu dữ liệu (String -> double, int)
            double price = Double.parseDouble(priceStr);
            int quantity = Integer.parseInt(quantityStr);
            int cateId = Integer.parseInt(cateIdStr);
            int suppId = Integer.parseInt(suppIdStr);

            // 3. Gọi DAO lưu vào Database
            ProductDAO dao = new ProductDAO();
            dao.insertProduct(name, price, quantity, desc, img, cateId, suppId, ram, rom, color);

            // 4. Lưu thành công thì quay về trang danh sách
            response.sendRedirect("product-list");

        } catch (Exception e) {
            e.printStackTrace();
            // Nếu lỗi (ví dụ nhập chữ vào ô số tiền) thì báo lỗi hoặc quay lại form
            response.getWriter().println("Lỗi nhập liệu! Vui lòng kiểm tra lại các trường số.");
        }
    }
}