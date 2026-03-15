package vn.edu.phoneshop.controller;

import jakarta.servlet.ServletException;
import jakarta.servlet.annotation.WebServlet;
import jakarta.servlet.http.HttpServlet;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;
import java.io.IOException;
import java.util.List;
import vn.edu.phoneshop.dao.CategoryDAO;
import vn.edu.phoneshop.model.Category;

@WebServlet(name = "CategoryServlet", urlPatterns = { "/category-list" })
public class CategoryServlet extends HttpServlet {

    @Override
    protected void doGet(HttpServletRequest request, HttpServletResponse response)
            throws ServletException, IOException {

        CategoryDAO dao = new CategoryDAO();
        List<Category> list = dao.getAllCategories();

        request.setAttribute("listC", list);
        request.setAttribute("activePage", "category-management");
        request.getRequestDispatcher("category-list.jsp").forward(request, response);
    }
}
