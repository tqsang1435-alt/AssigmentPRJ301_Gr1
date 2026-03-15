/*
 * Click nbfs://nbhost/SystemFileSystem/Templates/Licenses/license-default.txt to change this license
 * Click nbfs://nbhost/SystemFileSystem/Templates/Classes/Class.java to edit this template
 */
package vn.edu.phoneshop.controller;

/**
 *
 * @author Lenovo
 */
import jakarta.servlet.*;
import jakarta.servlet.http.*;
import jakarta.servlet.annotation.WebServlet;
import java.io.IOException;
import java.util.*;
import vn.edu.phoneshop.dao.OrderDAO;
import vn.edu.phoneshop.dao.ProductDAO;
import vn.edu.phoneshop.model.Product;

@WebServlet("/add-cart")
public class AddCartController extends HttpServlet {

    protected void doGet(HttpServletRequest request, HttpServletResponse response)
            throws ServletException, IOException {

        String productIdStr = request.getParameter("id");
        String action = request.getParameter("action");
        HttpSession session = request.getSession();
        ProductDAO dao = new ProductDAO();
        OrderDAO oDao = new OrderDAO();

        vn.edu.phoneshop.model.User user = (vn.edu.phoneshop.model.User) session.getAttribute("ACC");
        if (user == null) {
            response.sendRedirect("login.jsp");
            return;
        }
        if ("buynow".equals(action)) {
            double totalAmount = 0;

            if (productIdStr != null) {
                int id = Integer.parseInt(productIdStr);
                Product p = dao.getProductByID(id);
                if (p != null) {
                    totalAmount = p.getPrice();
                }
            } else {
                Object totalMoneyObj = session.getAttribute("totalMoney");
                if (totalMoneyObj != null) {
                    totalAmount = (Double) totalMoneyObj;
                }
            }
            int orderId = oDao.createOrder(user.getUserID(), totalAmount, 1);
            session.setAttribute("currentOrderId", orderId);

            if (productIdStr != null) {
                int id = Integer.parseInt(productIdStr);
                Product p = dao.getProductByID(id);
                oDao.insertOrderDetail(orderId, id, 1, p.getPrice());

                request.setAttribute("quickProduct", p);
                request.setAttribute("payMode", "single");
            } else {
                Map<Integer, Integer> cart = (Map<Integer, Integer>) session.getAttribute("cart");
                if (cart != null) {
                    for (Integer pId : cart.keySet()) {
                        Product p = dao.getProductByID(pId);
                        oDao.insertOrderDetail(orderId, pId, cart.get(pId), p.getPrice());
                    }
                }
                request.setAttribute("payMode", "cart");
            }
            request.getRequestDispatcher("quick-pay.jsp").forward(request, response);
            return;
        }
        try {
            if (productIdStr != null) {
                int id = Integer.parseInt(productIdStr);
                Map<Integer, Integer> cart = (Map<Integer, Integer>) session.getAttribute("cart");
                if (cart == null) {
                    cart = new HashMap<>();
                }

                if ("sub".equals(action)) {
                    if (cart.containsKey(id)) {
                        int newQty = cart.get(id) - 1;
                        if (newQty <= 0) {
                            cart.remove(id);
                        } else {
                            cart.put(id, newQty);
                        }
                    }
                } else if ("remove".equals(action)) {
                    cart.remove(id);
                } else {
                    cart.put(id, cart.getOrDefault(id, 0) + 1);
                }
                updateCartSession(session, cart, dao);
            }
        } catch (Exception e) {
            e.printStackTrace();
        }

        // Điều hướng quay lại
        String referer = request.getHeader("referer");
        if (referer != null && referer.contains("cart")) {
            response.sendRedirect("cart.jsp");
        } else {
            response.sendRedirect("home");
        }
    }

    private void updateCartSession(HttpSession session, Map<Integer, Integer> cart, ProductDAO dao) {
        List<Map<String, Object>> cartItems = new ArrayList<>();
        int totalCount = 0;
        double totalMoney = 0;

        for (Map.Entry<Integer, Integer> entry : cart.entrySet()) {
            Product p = dao.getProductByID(entry.getKey());
            if (p != null) {
                Map<String, Object> item = new HashMap<>();
                item.put("product", p);
                item.put("quantity", entry.getValue());
                cartItems.add(item);
                totalCount += entry.getValue();
                totalMoney += p.getPrice() * entry.getValue();
            }
        }
        session.setAttribute("cart", cart);
        session.setAttribute("cartItems", cartItems);
        session.setAttribute("cartCount", totalCount);
        session.setAttribute("totalMoney", totalMoney);
    }
}
