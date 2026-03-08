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
import vn.edu.phoneshop.dao.LoginDAO;
import vn.edu.phoneshop.model.User;

@WebServlet("/login")
public class LoginController extends HttpServlet {
    private LoginDAO loginDAO = new LoginDAO();

    @Override
    protected void doPost(HttpServletRequest request, HttpServletResponse response)
            throws ServletException, IOException {

        String email = request.getParameter("email");
        String password = request.getParameter("password");

        User user = loginDAO.checkLogin(email, password);

        if (user == null) {
            request.setAttribute("error", "Email hoặc mật khẩu không đúng!");
            request.getRequestDispatcher("login.jsp").forward(request, response);
        } else {
            HttpSession session = request.getSession();
            session.setAttribute("user", user); 
            
            session.setAttribute("username", user.getFullName()); 
            session.setAttribute("role", user.getRole());

            response.sendRedirect("home");
        }
    }
}
