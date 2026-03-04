package vn.edu.phoneshop.controller;

import jakarta.servlet.ServletException;
import jakarta.servlet.annotation.WebServlet;
import jakarta.servlet.http.HttpServlet;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;
import jakarta.servlet.http.HttpSession;
import java.io.IOException;
import vn.edu.phoneshop.dao.UserDAO;
import vn.edu.phoneshop.model.User;

@WebServlet(name = "UserControl", urlPatterns = { "/user-login", "/user-register", "/user-profile", "/user-logout" })
public class UserControl extends HttpServlet {

    protected void processRequest(HttpServletRequest request, HttpServletResponse response)
            throws ServletException, IOException {
        response.setContentType("text/html;charset=UTF-8");
        request.setCharacterEncoding("UTF-8");

        String action = request.getServletPath(); // Lấy đường dẫn URL để biết user muốn làm gì
        UserDAO dao = new UserDAO();

        try {
            switch (action) {
                case "/user-login":
                    handleLogin(request, response, dao);
                    break;
                case "/user-register":
                    handleRegister(request, response, dao);
                    break;
                case "/user-profile":
                    handleUpdateProfile(request, response, dao);
                    break;
                case "/user-logout":
                    HttpSession session = request.getSession();
                    session.invalidate(); // Xóa session
                    response.sendRedirect("/PhoneShop_Management/"); // Quay về trang chủ (giả sử có servlet home)
                    break;
                default:
                    response.sendRedirect("Login.jsp");
                    break;
            }
        } catch (Exception e) {
            e.printStackTrace();
        }
    }

    // Xử lý Đăng nhập
    private void handleLogin(HttpServletRequest request, HttpServletResponse response, UserDAO dao)
            throws ServletException, IOException {
        String email = request.getParameter("email");
        String pass = request.getParameter("password");

        User user = dao.login(email, pass);

        if (user != null) {
            HttpSession session = request.getSession();
            session.setAttribute("ACC", user); // Lưu user vào session
            session.setAttribute("ROLE", user.getRole()); // Lưu quyền

            // Nếu là Admin thì vào trang quản lý, khách thì về trang chủ
            if ("Admin".equalsIgnoreCase(user.getRole())) {
                response.sendRedirect("admin-dashboard");
            } else {
                response.sendRedirect("home");
            }
        } else {
            request.setAttribute("mess", "Sai email hoặc mật khẩu!");
            request.getRequestDispatcher("Login.jsp").forward(request, response);
        }
    }

    // Xử lý Đăng ký
    private void handleRegister(HttpServletRequest request, HttpServletResponse response, UserDAO dao)
            throws ServletException, IOException {
        String user = request.getParameter("user"); // FullName
        String pass = request.getParameter("pass");
        String re_pass = request.getParameter("re_pass");
        String email = request.getParameter("email");
        String phone = request.getParameter("phone");
        String address = request.getParameter("address");

        if (!pass.equals(re_pass)) {
            request.setAttribute("mess", "Mật khẩu nhập lại không khớp!");
            request.getRequestDispatcher("Register.jsp").forward(request, response);
        } else {
            User u = dao.login(email, pass); // Kiểm tra email tồn tại chưa (logic này nên viết hàm checkEmail riêng)
            if (u == null) {
                dao.registerUser(user, email, phone, address, pass);
                request.setAttribute("mess", "Đăng ký thành công! Vui lòng đăng nhập.");
                request.getRequestDispatcher("Login.jsp").forward(request, response);
            } else {
                request.setAttribute("mess", "Email đã tồn tại!");
                request.getRequestDispatcher("Register.jsp").forward(request, response);
            }
        }
    }

    // Xử lý Cập nhật hồ sơ
    private void handleUpdateProfile(HttpServletRequest request, HttpServletResponse response, UserDAO dao)
            throws ServletException, IOException {
        HttpSession session = request.getSession();
        User acc = (User) session.getAttribute("ACC");

        if (acc != null) {
            String fullName = request.getParameter("fullname");
            String phone = request.getParameter("phone");
            String address = request.getParameter("address");

            // Gọi hàm DAO đã có
            dao.updateUserProfile(acc.getUserID(), fullName, phone, address);

            // Cập nhật lại session
            acc.setFullName(fullName);
            acc.setPhoneNumber(phone);
            acc.setAddress(address);
            session.setAttribute("ACC", acc);

            request.setAttribute("mess", "Cập nhật thông tin thành công!");
            request.getRequestDispatcher("UserProfile.jsp").forward(request, response);
        } else {
            response.sendRedirect("Login.jsp");
        }
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
