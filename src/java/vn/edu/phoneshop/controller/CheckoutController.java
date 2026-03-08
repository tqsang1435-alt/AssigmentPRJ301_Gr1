/*
 * Click nbfs://nbhost/SystemFileSystem/Templates/Licenses/license-default.txt to change this license
 * Click nbfs://nbhost/SystemFileSystem/Templates/Classes/Class.java to edit this template
 */
package vn.edu.phoneshop.controller;

import jakarta.servlet.ServletException;
import jakarta.servlet.annotation.WebServlet;
import jakarta.servlet.http.HttpServlet;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;
import jakarta.servlet.http.HttpSession;
import java.io.IOException;
import vn.edu.phoneshop.dao.OrderDAO;
import vn.edu.phoneshop.model.User;

/**
 *
 * @author Lenovo
 */
@WebServlet("/checkout")
public class CheckoutController extends HttpServlet {

    @Override
    protected void doPost(HttpServletRequest request, HttpServletResponse response) 
            throws ServletException, IOException {
        HttpSession session = request.getSession();
        String action = request.getParameter("action");
        
        User user = (User) session.getAttribute("user");
        Integer orderId = (Integer) session.getAttribute("currentOrderId");
        
        if (orderId == null || user == null) {
            response.sendRedirect("home");
            return;
        }

        OrderDAO oDao = new OrderDAO();

        if ("confirm".equals(action)) {
            // 1. Lấy địa chỉ trực tiếp từ bảng User (đã có trong Session)
            String userAddress = user.getAddress(); 
            
            // Nếu User chưa có địa chỉ, gán mặc định hoặc bắt cập nhật
            if (userAddress == null || userAddress.trim().isEmpty()) {
                userAddress = "Địa chỉ chưa xác định";
            }

            // 2. Cập nhật Status = 4 (Hoàn thành) và lưu Address vào đơn hàng
            oDao.updateOrderStatusAndAddress(orderId, 4, userAddress);
            
            // 3. Dọn dẹp giỏ hàng và session liên quan
            session.removeAttribute("cart");
            session.removeAttribute("cartItems");
            session.setAttribute("cartCount", 0);
            session.setAttribute("totalMoney", 0.0);
            session.removeAttribute("currentOrderId");
            
            response.sendRedirect("thanks.jsp");
            
        } else if ("cancel".equals(action)) {
            // Status = 3 (Đã hủy)
            oDao.updateOrderStatusAndAddress(orderId, 3, "Đơn hàng đã bị hủy");
            session.removeAttribute("currentOrderId");
            response.sendRedirect("home");
        }
    }
}