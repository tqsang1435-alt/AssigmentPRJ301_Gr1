package vn.edu.phoneshop.controller;

import jakarta.servlet.ServletException;
import jakarta.servlet.annotation.WebServlet;
import jakarta.servlet.http.HttpServlet;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;
import jakarta.servlet.http.HttpSession;
import java.io.IOException;
import java.util.List;
import vn.edu.phoneshop.dao.UserDAO;
import vn.edu.phoneshop.model.User;

@WebServlet(name = "UserControl", urlPatterns = { "/user-login", "/user-register", "/user-profile", "/user-logout",
        "/edit-profile",
        "/update-profile", "/admin-delete-user", "/admin-customer-list" })
public class UserControl extends HttpServlet {

    // Xử lý Đăng nhập
    private void handleLogin(HttpServletRequest request, HttpServletResponse response, UserDAO dao)
            throws ServletException, IOException {
        String email = request.getParameter("email");
        String pass = request.getParameter("password");

        User user = dao.login(email, pass);

        if (user != null) {
            if ("Admin".equalsIgnoreCase(user.getRole())) {
                request.setAttribute("mess", "Bạn không có quyền truy cập tài khoản quản trị này.");
                request.getRequestDispatcher("login.jsp").forward(request, response);
                return;
            }

            HttpSession session = request.getSession();
            session.setAttribute("ACC", user);
            session.setAttribute("ROLE", user.getRole());
            session.setMaxInactiveInterval(24 * 60 * 60);
            response.sendRedirect("home");
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
            String fullName = request.getParameter("fullName");
            String phone = request.getParameter("phone");
            String address = request.getParameter("address");
            dao.updateUserProfile(acc.getUserID(), fullName, phone, address);

            // Cập nhật lại thông tin trong session
            acc.setFullName(fullName);
            acc.setPhoneNumber(phone);
            acc.setAddress(address);
            session.setAttribute("ACC", acc);

            // Chuyển hướng về trang profile với thông báo thành công
            response.sendRedirect("user-profile?message=success");
        } else {
            response.sendRedirect("user-login");
        }
    }

    // Xử lý Xóa người dùng (Admin)
    private void handleDeleteUser(HttpServletRequest request, HttpServletResponse response, UserDAO dao)
            throws ServletException, IOException {
        HttpSession session = request.getSession(false);
        User adminUser = (session != null) ? (User) session.getAttribute("ACC") : null;

        // Chỉ Admin mới có quyền xóa
        if (adminUser == null || !"Admin".equalsIgnoreCase(adminUser.getRole())) {
            response.sendRedirect("user-login");
            return;
        }

        try {
            int userIDToDelete = Integer.parseInt(request.getParameter("userID"));

            // Admin không thể tự xóa chính mình
            if (userIDToDelete == adminUser.getUserID()) {
                response.sendRedirect("admin-customer-list?delete_error=self");
                return;
            }

            dao.deleteUser(userIDToDelete); // Giả định hàm này tồn tại trong UserDAO và sẽ xóa user
            response.sendRedirect("admin-customer-list?delete_success=true");
        } catch (Exception e) {
            e.printStackTrace();
            response.sendRedirect("admin-customer-list?delete_error=true");
        }
    }

    @Override
    protected void doGet(HttpServletRequest request, HttpServletResponse response)
            throws ServletException, IOException {
        response.setContentType("text/html;charset=UTF-8");
        request.setCharacterEncoding("UTF-8");
        String action = request.getServletPath();
        HttpSession session = request.getSession(false);
        UserDAO dao = new UserDAO();

        try {
            switch (action) {
                case "/user-login":
                    if (session != null && session.getAttribute("ACC") != null) {
                        User loggedInUser = (User) session.getAttribute("ACC");
                        if ("Admin".equalsIgnoreCase(loggedInUser.getRole())) {
                            response.sendRedirect("admin-dashboard");
                        } else {
                            response.sendRedirect("user-profile");
                        }
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
                        response.sendRedirect("user-login");
                    }
                    break;
                case "/edit-profile":
                    if (session != null && session.getAttribute("ACC") != null) {
                        request.getRequestDispatcher("edit-profile.jsp").forward(request, response);
                    } else {
                        response.sendRedirect("user-login");
                    }
                    break;
                case "/user-logout":
                    if (session != null) {
                        User user = (User) session.getAttribute("ACC");
                        boolean wasAdmin = (user != null && "Admin".equalsIgnoreCase(user.getRole()));
                        session.invalidate();
                        if (wasAdmin) {
                            response.sendRedirect("admin-login");
                            return;
                        }
                    }
                    response.sendRedirect("home");
                    break;
                case "/admin-delete-user":
                    handleDeleteUser(request, response, dao);
                    break;
                case "/admin-customer-list":
                    if (session != null && session.getAttribute("ACC") != null) {
                        User u = (User) session.getAttribute("ACC");
                        if ("Admin".equalsIgnoreCase(u.getRole())) {
                            int pageSize = 10;
                            int currentPage = 1;
                            String pageParam = request.getParameter("page");
                            if (pageParam != null) {
                                try {
                                    currentPage = Integer.parseInt(pageParam);
                                    if (currentPage < 1) currentPage = 1;
                                } catch (NumberFormatException ex) {
                                    currentPage = 1;
                                }
                            }
                            List<User> allList = dao.getAllCustomers();
                            int totalRecords = allList.size();
                            int totalPages = (int) Math.ceil((double) totalRecords / pageSize);
                            if (totalPages < 1) totalPages = 1;
                            if (currentPage > totalPages) currentPage = totalPages;
                            int fromIndex = (currentPage - 1) * pageSize;
                            int toIndex = Math.min(fromIndex + pageSize, totalRecords);
                            List<User> pagedList = (totalRecords > 0) ? allList.subList(fromIndex, toIndex) : allList;
                            request.setAttribute("listC", pagedList);
                            request.setAttribute("currentPage", currentPage);
                            request.setAttribute("totalPages", totalPages);
                            request.setAttribute("pageOffset", fromIndex);
                            request.getRequestDispatcher("CustomerList.jsp").forward(request, response);
                        } else {
                            response.sendRedirect("home");
                        }
                    } else {
                        response.sendRedirect("user-login");
                    }
                    break;

                default:
                    response.sendRedirect("home");
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
                case "/update-profile":
                    handleUpdateProfile(request, response, dao);
                    break;
                default:
                    response.sendRedirect("home");
                    break;
            }
        } catch (Exception e) {
            e.printStackTrace();
        }
    }
}
