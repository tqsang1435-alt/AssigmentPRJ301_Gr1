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

@WebServlet(name = "PurchaseHistoryControl", urlPatterns = {"/purchase-history"})
public class PurchaseHistoryControl extends HttpServlet {

    protected void processRequest(HttpServletRequest request, HttpServletResponse response)
            throws ServletException, IOException {
        response.setContentType("text/html;charset=UTF-8");

        HttpSession session = request.getSession();
        User user = (User) session.getAttribute("ACC");

        if (user == null) {
            response.sendRedirect("user-login");
            return;
        }

        OrderDAO orderDao = new OrderDAO();
        List<Order> listOrders = orderDao.getOrdersByUserId(user.getUserID());

        request.setAttribute("listOrders", listOrders);
        request.getRequestDispatcher("purchase-history.jsp").forward(request, response);
    }

    @Override
    protected void doGet(HttpServletRequest request, HttpServletResponse response)
            throws ServletException, IOException {
        processRequest(request, response);
    }

    @Override
    protected void doPost(HttpServletRequest request, HttpServletResponse response)
            throws ServletException, IOException {
        processRequest(request, response);
    }
}
