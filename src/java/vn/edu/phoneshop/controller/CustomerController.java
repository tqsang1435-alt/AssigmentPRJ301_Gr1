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

@WebServlet(name = "CustomerController", urlPatterns = { 
    "/customer-list", 
    "/add-customer", 
    "/edit-customer", 
    "/update-customer", 
    "/delete-customer",
    "/restore-customer"
})
public class CustomerController extends HttpServlet {

    private final CustomerDAO customerDAO = new CustomerDAO();

    @Override
    protected void doGet(HttpServletRequest request, HttpServletResponse response)
            throws ServletException, IOException {
        String path = request.getServletPath();

        switch (path) {
            case "/customer-list":
                listCustomers(request, response);
                break;
            case "/add-customer":
                showAddForm(request, response);
                break;
            case "/edit-customer":
                showEditForm(request, response);
                break;
            case "/delete-customer":
                deleteCustomer(request, response);
                break;
            case "/restore-customer":
                restoreCustomer(request, response);
                break;
            default:
                response.sendRedirect("customer-list");
                break;
        }
    }

    @Override
    protected void doPost(HttpServletRequest request, HttpServletResponse response)
            throws ServletException, IOException {
        String path = request.getServletPath();
        
        switch (path) {
            case "/add-customer":
                addCustomer(request, response);
                break;
            case "/update-customer":
                updateCustomer(request, response);
                break;
            default:
                doGet(request, response);
                break;
        }
    }

    private void listCustomers(HttpServletRequest request, HttpServletResponse response)
            throws ServletException, IOException {
        HttpSession session = request.getSession();
        User user = (User) session.getAttribute("ACC");
        // Kiểm tra quyền Admin
        if (user == null || !"Admin".equalsIgnoreCase(user.getRole())) {
            response.sendRedirect("user-login");
            return;
        }

        int pageSize = 10;
        int currentPage = 1;
        String pageParam = request.getParameter("page");
        if (pageParam != null) {
            try {
                currentPage = Integer.parseInt(pageParam);
                if (currentPage < 1) currentPage = 1;
            } catch (NumberFormatException e) {
                currentPage = 1;
            }
        }

        List<User> allList = customerDAO.getCustomers(true); // hiển thị cả tài khoản bị vô hiệu hóa
        int totalRecords = allList.size();
        int totalPages = (int) Math.ceil((double) totalRecords / pageSize);
        if (currentPage > totalPages && totalPages > 0) currentPage = totalPages;

        int fromIndex = (currentPage - 1) * pageSize;
        int toIndex = Math.min(fromIndex + pageSize, totalRecords);
        List<User> pagedList = (totalRecords > 0) ? allList.subList(fromIndex, toIndex) : allList;

        request.setAttribute("listC", pagedList);
        request.setAttribute("currentPage", currentPage);
        request.setAttribute("totalPages", totalPages);
        request.setAttribute("pageOffset", fromIndex);
        request.getRequestDispatcher("CustomerList.jsp").forward(request, response);
    }

    private void showAddForm(HttpServletRequest request, HttpServletResponse response)
            throws ServletException, IOException {
        request.getRequestDispatcher("add-customer.jsp").forward(request, response);
    }

    private void addCustomer(HttpServletRequest request, HttpServletResponse response)
            throws ServletException, IOException {
        request.setCharacterEncoding("UTF-8");
        String fullName = request.getParameter("fullName");
        String email = request.getParameter("email");
        String password = request.getParameter("password");
        String phone = request.getParameter("phone");
        String address = request.getParameter("address");

        customerDAO.addCustomer(fullName, email, password, phone, address);
        
        response.sendRedirect("customer-list");
    }

    private void showEditForm(HttpServletRequest request, HttpServletResponse response)
            throws ServletException, IOException {
        String id = request.getParameter("id");
        User customer = customerDAO.getCustomerByID(id);
        
        request.setAttribute("customer", customer);
        request.getRequestDispatcher("edit-customer.jsp").forward(request, response);
    }

    private void updateCustomer(HttpServletRequest request, HttpServletResponse response)
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

        customerDAO.updateCustomer(user);
        
        response.sendRedirect("customer-list");
    }

    private void deleteCustomer(HttpServletRequest request, HttpServletResponse response)
            throws ServletException, IOException {
        String id = request.getParameter("id");
        customerDAO.deleteCustomer(id);
        response.sendRedirect("customer-list");
    }

    private void restoreCustomer(HttpServletRequest request, HttpServletResponse response)
            throws ServletException, IOException {
        String id = request.getParameter("id");
        customerDAO.restoreCustomer(id);
        response.sendRedirect("customer-list");
    }
}
