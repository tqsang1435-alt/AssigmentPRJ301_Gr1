// package vn.edu.phoneshop.controller;

// import jakarta.servlet.ServletException;
// import jakarta.servlet.annotation.WebServlet;
// import jakarta.servlet.http.HttpServlet;
// import jakarta.servlet.http.HttpServletRequest;
// import jakarta.servlet.http.HttpServletResponse;
// import jakarta.servlet.http.HttpSession;
// import java.io.IOException;
// import java.util.List;
// import vn.edu.phoneshop.dao.UserDAO;
// import vn.edu.phoneshop.model.User;

// @WebServlet(name = "CustomerListControl", urlPatterns = {
// "/admin-customer-list" })
// public class CustomerListControl extends HttpServlet {

// @Override
// protected void doGet(HttpServletRequest request, HttpServletResponse
// response)
// throws ServletException, IOException {
// HttpSession session = request.getSession();
// User user = (User) session.getAttribute("ACC");
// if (user == null || !"Admin".equalsIgnoreCase(user.getRole())) {
// response.sendRedirect("user-login");
// return;
// }

// UserDAO userDAO = new UserDAO();
// List<User> customerList = userDAO.getAllCustomers();
// request.setAttribute("listC", customerList);

// // Đánh dấu trang đang active cho sidebar
// request.setAttribute("activePage", "customer-management");

// request.getRequestDispatcher("ManagerCustomer.jsp").forward(request,
// response);
// }
// }