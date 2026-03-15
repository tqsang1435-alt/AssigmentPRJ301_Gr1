package vn.edu.phoneshop.controller;

import jakarta.servlet.ServletException;
import jakarta.servlet.annotation.WebServlet;
import jakarta.servlet.http.HttpServlet;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;
import jakarta.servlet.http.HttpSession;
import java.io.IOException;
import java.util.List;
import vn.edu.phoneshop.dao.CustomerDAO;
import vn.edu.phoneshop.model.User;

@WebServlet(name = "CustomerControl", urlPatterns = { "/customer-list" })
public class CustomerControl extends HttpServlet {
    @Override
    protected void doGet(HttpServletRequest request, HttpServletResponse response)
            throws ServletException, IOException {
        HttpSession session = request.getSession();
        User user = (User) session.getAttribute("ACC");
        // Kiểm tra quyền Admin
        if (user == null || !"Admin".equalsIgnoreCase(user.getRole())) {
            response.sendRedirect("user-login");
            return;
        }

        CustomerDAO dao = new CustomerDAO();
        List<User> list = dao.getCustomers();
        request.setAttribute("listC", list);
        request.getRequestDispatcher("CustomerList.jsp").forward(request, response);
    }
}