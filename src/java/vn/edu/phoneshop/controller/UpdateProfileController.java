/*
 * Click nbfs://nbhost/SystemFileSystem/Templates/Licenses/license-default.txt to change this license
 * Click nbfs://nbhost/SystemFileSystem/Templates/Classes/Class.java to edit this template
 */
package vn.edu.phoneshop.controller;

import jakarta.servlet.ServletException;
import jakarta.servlet.annotation.WebServlet;
import jakarta.servlet.http.HttpServlet;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;
import java.io.IOException;
import vn.edu.phoneshop.dao.UserDAO;
import vn.edu.phoneshop.model.User;

/**
 *
 * @author Lenovo
 */
@WebServlet(name = "UpdateProfileController", urlPatterns = { "/update-profile", "/edit-profile" })
public class UpdateProfileController extends HttpServlet {

    // Hàm này dùng để MỞ trang edit-profile.jsp khi bạn nhấn nút Sửa
    @Override
    protected void doGet(HttpServletRequest request, HttpServletResponse response)
            throws ServletException, IOException {
        User user = (User) request.getSession().getAttribute("user");
        if (user == null) {
            response.sendRedirect("login");
            return;
        }
        // Đảm bảo file edit-profile.jsp nằm trong thư mục web (ngang hàng index.jsp)
        request.getRequestDispatcher("edit-profile.jsp").forward(request, response);
    }

    // Hàm này dùng để LƯU dữ liệu khi bạn nhấn nút "Lưu thay đổi"
    @Override
    protected void doPost(HttpServletRequest request, HttpServletResponse response)
            throws ServletException, IOException {
        request.setCharacterEncoding("UTF-8");

        String fullName = request.getParameter("fullName");
        String phone = request.getParameter("phone");
        String address = request.getParameter("address");

        User user = (User) request.getSession().getAttribute("user");

        if (user != null) {
            UserDAO dao = new UserDAO();
            // Gọi hàm từ UserDAO mà bạn đã định nghĩa
            boolean success = dao.updateUserProfileDat(user.getUserID(), fullName, phone,
                    address);

            if (success) {
                // Cập nhật lại Session để giao diện thay đổi ngay lập tức
                user.setFullName(fullName);
                user.setPhoneNumber(phone);
                user.setAddress(address);
                request.getSession().setAttribute("user", user);
                response.sendRedirect("profile?message=success");
            } else {
                response.sendRedirect("edit-profile?error=fail");
            }
        } else {
            response.sendRedirect("login");
        }
    }
}