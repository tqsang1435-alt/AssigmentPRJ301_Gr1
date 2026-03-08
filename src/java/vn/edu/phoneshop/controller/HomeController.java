package vn.edu.phoneshop.controller;

import jakarta.servlet.ServletException;
import jakarta.servlet.annotation.WebServlet;
import jakarta.servlet.http.HttpServlet;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;
import java.io.IOException;
import java.util.List;
import vn.edu.phoneshop.dao.ProductDAO;
import vn.edu.phoneshop.model.Product;

@WebServlet(name = "HomeController", urlPatterns = {"/home", ""})
public class HomeController extends HttpServlet {

    @Override
    protected void doGet(HttpServletRequest request, HttpServletResponse response)
            throws ServletException, IOException {

        String cidRaw = request.getParameter("cid");
        String pageRaw = request.getParameter("page");

        ProductDAO dao = new ProductDAO();

        int page = 1;
        int pageSize = 10;

        if (pageRaw != null && !pageRaw.isEmpty()) {
            try {
                page = Integer.parseInt(pageRaw);
            } catch (NumberFormatException e) {
                page = 1;
            }
        }

        int totalProducts = dao.getTotalProducts(cidRaw);
        int totalPages = (int) Math.ceil((double) totalProducts / pageSize);

        List<Product> list = dao.getProductsWithPagination(cidRaw, page, pageSize);

        request.setAttribute("listP", list);
        request.setAttribute("currentPage", page);
        request.setAttribute("totalPages", totalPages);
        request.setAttribute("cid", cidRaw);

        request.getRequestDispatcher("home.jsp").forward(request, response);
    }
}
