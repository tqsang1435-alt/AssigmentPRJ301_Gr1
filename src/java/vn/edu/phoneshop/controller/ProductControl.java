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
import jakarta.servlet.http.HttpSession;
import java.io.IOException;
import java.util.List;
import vn.edu.phoneshop.dao.ProductDAO;
import vn.edu.phoneshop.model.Product;
import vn.edu.phoneshop.model.User;

/**
 *
 * @author tqsan
 */
@WebServlet(name = "ProductControl", urlPatterns = { "/product-list" })
public class ProductControl extends HttpServlet {

    @Override
    protected void doGet(HttpServletRequest request, HttpServletResponse response)
            throws ServletException, IOException {

        HttpSession session = request.getSession();
        User user = (User) session.getAttribute("ACC");
        if (user == null || !"Admin".equalsIgnoreCase(user.getRole())) {
            response.sendRedirect("user-login");
            return;
        }

        try {
            ProductDAO dao = new ProductDAO();
            List<String> listRAM = dao.getAllRAMs();
            List<String> listROM = dao.getAllROMs();
            request.setAttribute("listRAM", listRAM);
            request.setAttribute("listROM", listROM);
            String search = request.getParameter("txtSearch");
            String ram = request.getParameter("ramFilter");
            String rom = request.getParameter("romFilter");
            List<Product> list = dao.searchAndFilterProducts(search, ram, rom);
            request.setAttribute("listP", list);
            request.setAttribute("txtSearch", search);
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
