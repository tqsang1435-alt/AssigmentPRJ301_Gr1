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
import vn.edu.phoneshop.dao.SupplierDAO;
import vn.edu.phoneshop.model.Supplier;

/**
 *
 * @author tqsan
 */
@WebServlet(name = "SupplierControl", urlPatterns = {"/supplier-list"})
public class SupplierControl extends HttpServlet {

    // Chỉ giữ lại 1 hàm doGet duy nhất để xử lý
    @Override
    protected void doGet(HttpServletRequest request, HttpServletResponse response)
            throws ServletException, IOException {
        
        try {
            // 1. Gọi DAO lấy dữ liệu
            SupplierDAO dao = new SupplierDAO();
            List<Supplier> list = dao.getAllSuppliers();

            // 2. Đẩy dữ liệu vào request (đặt tên biến là "listS")
            request.setAttribute("listS", list);

            // 3. Chuyển trang sang file JSP để hiển thị
            // Lưu ý: File ManagerSupplier.jsp phải nằm ngay trong thư mục "Web Pages"
            request.getRequestDispatcher("ManagerSupplier.jsp").forward(request, response);
            
        } catch (Exception e) {
            e.printStackTrace();
        }
    }

    // Hàm doPost để xử lý khi thêm/sửa/xóa (sẽ dùng sau)
    @Override
    protected void doPost(HttpServletRequest request, HttpServletResponse response)
            throws ServletException, IOException {
        // Tạm thời chưa làm gì thì để trống hoặc gọi doGet
        doGet(request, response);
    }
}