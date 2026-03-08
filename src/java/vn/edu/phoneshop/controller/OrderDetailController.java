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
import java.io.IOException;
import java.util.List;
import java.util.Map;
import vn.edu.phoneshop.dao.OrderDAO;
import vn.edu.phoneshop.model.OrderDetail;

/**
 *
 * @author Lenovo
 */
@WebServlet("/order-detail")
public class OrderDetailController extends HttpServlet {
    protected void doGet(HttpServletRequest request, HttpServletResponse response) 
        throws ServletException, IOException {
    String idRaw = request.getParameter("id");
    if (idRaw != null) {
        int orderID = Integer.parseInt(idRaw);
        OrderDAO dao = new OrderDAO();
        List<OrderDetail> details = dao.getOrderDetailByOrderID(orderID);
        
        Map<Integer, String> pNames = dao.getProductNamesMap(orderID);

        request.setAttribute("detailList", details);
        request.setAttribute("productNames", pNames); 
        request.setAttribute("orderID", orderID);
        request.getRequestDispatcher("order-detail.jsp").forward(request, response);
    } else {
        response.sendRedirect("order-history");
    }
}
}

