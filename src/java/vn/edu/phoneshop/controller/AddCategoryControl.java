package vn.edu.phoneshop.controller;

import jakarta.servlet.ServletException;
import jakarta.servlet.annotation.WebServlet;
import jakarta.servlet.http.HttpServlet;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;
import java.io.IOException;
import vn.edu.phoneshop.dao.CategoryDAO;

@WebServlet(name = "AddCategoryControl", urlPatterns = { "/add-category" })
public class AddCategoryControl extends HttpServlet {

    @Override
    protected void doGet(HttpServletRequest request, HttpServletResponse response)
            throws ServletException, IOException {
        request.getRequestDispatcher("AddCategory.jsp").forward(request, response);
    }

    @Override
    protected void doPost(HttpServletRequest request, HttpServletResponse response)
            throws ServletException, IOException {
        request.setCharacterEncoding("UTF-8");

        String name = request.getParameter("name");

        try {
            CategoryDAO dao = new CategoryDAO();
            dao.insertCategory(name);
        } catch (Exception e) {
            e.printStackTrace();
        }

        response.sendRedirect("category-list");
    }
}
