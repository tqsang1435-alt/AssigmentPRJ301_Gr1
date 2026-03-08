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
                response.sendRedirect("index.html");
            }
        } else {
            request.setAttribute("mess", "Sai email hoặc mật khẩu!");
            request.getRequestDispatcher("login.jsp").forward(request, response);
        }
    }

    // Xử lý Đăng ký
    private void handleRegister(HttpServletRequest request, HttpServletResponse response, UserDAO dao)
            throws ServletException, IOException {
        String user = request.getParameter("user");
        String pass = request.getParameter("pass");
        String re_pass = request.getParameter("re_pass");
        String email = request.getParameter("email");
        String phone = request.getParameter("phone");
        String address = request.getParameter("address");

        if (!pass.equals(re_pass)) {
            request.setAttribute("mess", "Mật khẩu nhập lại không khớp!");
            request.getRequestDispatcher("register.jsp").forward(request, response);
        } else {
            // Cần tạo hàm checkUserExist(String email) trong UserDAO để kiểm tra email đã
            // tồn tại chưa
            User u = dao.checkUserExist(email);
            if (u == null) {
                dao.registerUser(user, email, phone, address, pass);
                request.setAttribute("mess", "Đăng ký thành công! Vui lòng đăng nhập.");
                request.getRequestDispatcher("login.jsp").forward(request, response);
            } else {
                request.setAttribute("mess", "Email đã tồn tại!");
                request.getRequestDispatcher("register.jsp").forward(request, response);
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
            dao.updateUserProfile(acc.getUserID(), fullName, phone, address);
            acc.setFullName(fullName);
            acc.setPhoneNumber(phone);
            acc.setAddress(address);
            session.setAttribute("ACC", acc);

            request.setAttribute("mess", "Cập nhật thông tin thành công!");
            request.getRequestDispatcher("profile.jsp").forward(request, response);
        } else {
            response.sendRedirect("login.jsp");
        }
    }

    @Override
    protected void doGet(HttpServletRequest request, HttpServletResponse response)
            throws ServletException, IOException {
        response.setContentType("text/html;charset=UTF-8");
        request.setCharacterEncoding("UTF-8");
        String action = request.getServletPath();
        HttpSession session = request.getSession(false);

        try {
            switch (action) {
                case "/user-login":
                    if (session != null && session.getAttribute("ACC") != null) {
                        // Nếu đã đăng nhập, chuyển hướng đến trang profile
                        response.sendRedirect("user-profile");
                    } else {
                        // Nếu chưa đăng nhập, hiển thị trang login
                        request.getRequestDispatcher("login.jsp").forward(request, response);
                    }
                    break;
                case "/user-register":
                    request.getRequestDispatcher("register.jsp").forward(request, response);
                    break;
                case "/user-profile":
                    if (session != null && session.getAttribute("ACC") != null) {
                        request.getRequestDispatcher("profile.jsp").forward(request, response);
                    } else {
                        response.sendRedirect("login.jsp");
                    }
                    break;
                case "/user-logout":
                    if (session != null) {
                        session.invalidate();
                    }
                    response.sendRedirect("index.html");
                    break;
                default:
                    response.sendRedirect("index.html");
                    break;
            }
        } catch (Exception e) {
            e.printStackTrace();
        }
    }

    @Override
    protected void doPost(HttpServletRequest request, HttpServletResponse response)
            throws ServletException, IOException {
        response.setContentType("text/html;charset=UTF-8");
        request.setCharacterEncoding("UTF-8");
        String action = request.getServletPath();
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
                default:
                    response.sendRedirect("index.html");
                    break;
            }
        } catch (Exception e) {
            e.printStackTrace();
        }
    }
}
