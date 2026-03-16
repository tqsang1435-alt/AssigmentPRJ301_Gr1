package vn.edu.phoneshop.controller;

import jakarta.servlet.ServletException;
import jakarta.servlet.annotation.WebServlet;
import jakarta.servlet.http.HttpServlet;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;
import jakarta.servlet.http.HttpSession;
import java.io.IOException;
import java.util.List;
import vn.edu.phoneshop.dao.ProductDAO;
import vn.edu.phoneshop.model.Product;
import vn.edu.phoneshop.model.User;

@WebServlet(name = "ProductControl", urlPatterns = { "/admin-product-list" })
public class ProductControl extends HttpServlet {

    @Override
    protected void doGet(HttpServletRequest request, HttpServletResponse response)
            throws ServletException, IOException {

        HttpSession session = request.getSession();
        User user = (User) session.getAttribute("ACC");
        if (user == null || !"Admin".equalsIgnoreCase(user.getRole())) {
            response.sendRedirect("user-login");
            return;
        }

        try {
            ProductDAO dao = new ProductDAO();
            List<String> listRAM = dao.getAllRAMs();
            List<String> listROM = dao.getAllROMs();
            request.setAttribute("listRAM", listRAM);
            request.setAttribute("listROM", listROM);
            String search = request.getParameter("searchName");
            String ram = request.getParameter("ramFilter");
            String rom = request.getParameter("romFilter");

            // Pagination logic
            int page = 1;
            int pageSize = 6;
            String pageStr = request.getParameter("page");
            if (pageStr != null && !pageStr.trim().isEmpty()) {
                try {
                    page = Integer.parseInt(pageStr);
                    if (page < 1) page = 1;
                } catch (NumberFormatException e) {
                    page = 1;
                }
            }

            int totalProducts = dao.getTotalProductsAdmin(search, ram, rom);
            int totalPages = (int) Math.ceil((double) totalProducts / pageSize);
            if (page > totalPages && totalPages > 0) {
                page = totalPages;
            }

            List<Product> list = dao.getProductsAdminPaginated(search, ram, rom, page, pageSize);

            request.setAttribute("listP", list);
            request.setAttribute("searchName", search);
            request.setAttribute("selectedRam", ram);
            request.setAttribute("selectedRom", rom);
            
            // Pagination attributes
            request.setAttribute("currentPage", page);
            request.setAttribute("totalPages", totalPages);

            request.getRequestDispatcher("ManagerProduct.jsp").forward(request, response);
        } catch (Exception e) {
            e.printStackTrace();
        }
    }

    @Override
    protected void doPost(HttpServletRequest request, HttpServletResponse response)
            throws ServletException, IOException {
        doGet(request, response);
    }
}
