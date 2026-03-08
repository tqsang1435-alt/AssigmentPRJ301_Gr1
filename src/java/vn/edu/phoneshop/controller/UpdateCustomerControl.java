package vn.edu.phoneshop.controller;

import jakarta.servlet.ServletException;
import jakarta.servlet.annotation.WebServlet;
import jakarta.servlet.http.HttpServlet;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;
import java.io.IOException;
import vn.edu.phoneshop.dao.CustomerDAO;
import vn.edu.phoneshop.model.User;

@WebServlet(name = "UpdateCustomerControl", urlPatterns = {"/update-customer"})
public class UpdateCustomerControl extends HttpServlet {

    @Override
    protected void doPost(HttpServletRequest request, HttpServletResponse response)
            throws ServletException, IOException {
        request.setCharacterEncoding("UTF-8");
        
        int id = Integer.parseInt(request.getParameter("id"));
        String fullName = request.getParameter("fullName");
        String email = request.getParameter("email");
        String phone = request.getParameter("phone");
        String address = request.getParameter("address");
        String password = request.getParameter("password"); // Can be empty
        int rewardPoints = Integer.parseInt(request.getParameter("rewardPoints"));
        String customerType = request.getParameter("customerType");

        User user = new User();
        user.setUserID(id);
        user.setFullName(fullName);
        user.setEmail(email);
        user.setPhoneNumber(phone);
        user.setAddress(address);
        user.setPassword(password);
        user.setRewardPoints(rewardPoints);
        user.setCustomerType(customerType);

        CustomerDAO dao = new CustomerDAO();
        dao.updateCustomer(user);
        
        response.sendRedirect("customer-list");
    }
}
