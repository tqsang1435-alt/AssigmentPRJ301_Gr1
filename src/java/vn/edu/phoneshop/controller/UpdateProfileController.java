/*
 * This controller is deprecated and merged into UserControl.java to avoid URL
 * conflicts.
 *
 * package vn.edu.phoneshop.controller;
 *
 * import jakarta.servlet.ServletException;
 * import jakarta.servlet.annotation.WebServlet;
 * import jakarta.servlet.http.HttpServlet;
 * import jakarta.servlet.http.HttpServletRequest;
 * import jakarta.servlet.http.HttpServletResponse;
 * import java.io.IOException;
 * import vn.edu.phoneshop.dao.UserDAO;
 * import vn.edu.phoneshop.model.User;
 *
 * @WebServlet(name = "UpdateProfileController", urlPatterns =
 * {"/update-profile", "/edit-profile"})
 * public class UpdateProfileController extends HttpServlet {
 *
 * @Override
 * protected void doGet(HttpServletRequest request, HttpServletResponse
 * response)
 * throws ServletException, IOException {
 * User user = (User) request.getSession().getAttribute("ACC");
 * if (user == null) {
 * response.sendRedirect("login");
 * return;
 * }
 * request.getRequestDispatcher("edit-profile.jsp").forward(request, response);
 * }
 *
 * @Override
 * protected void doPost(HttpServletRequest request, HttpServletResponse
 * response) throws ServletException, IOException {
 * request.setCharacterEncoding("UTF-8");
 * String fullName = request.getParameter("fullName");
 * String phone = request.getParameter("phone");
 * String address = request.getParameter("address");
 * User user = (User) request.getSession().getAttribute("ACC");
 * if (user != null) {
 * UserDAO dao = new UserDAO();
 * dao.updateUserProfile(user.getUserID(), fullName, phone, address);
 * user.setFullName(fullName);
 * user.setPhoneNumber(phone);
 * user.setAddress(address);
 * request.getSession().setAttribute("ACC", user);
 * response.sendRedirect("profile?message=success");
 * } else {
 * response.sendRedirect("login");
 * }
 * }
 * }
 */
