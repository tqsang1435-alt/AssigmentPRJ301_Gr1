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
import java.util.List;
import vn.edu.phoneshop.dao.OrderDAO;
import vn.edu.phoneshop.model.Order;
import vn.edu.phoneshop.model.User;

@WebServlet("/order-history")
public class OrderHistoryController extends HttpServlet {

    protected void doGet(HttpServletRequest request, HttpServletResponse response)
            throws ServletException, IOException {
        HttpSession session = request.getSession();
        User user = (User) session.getAttribute("user");

        if (user == null) {
            response.sendRedirect("login.jsp");
            return;
        }

        OrderDAO oDao = new OrderDAO();
        // Sử dụng getUserID() đã được cập nhật trong Model User
        List<Order> list = oDao.getOrdersByUserId(user.getUserID());

        request.setAttribute("orderList", list);
        request.getRequestDispatcher("order-history.jsp").forward(request, response);
    }
}
