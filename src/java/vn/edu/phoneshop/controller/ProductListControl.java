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

@WebServlet(name = "ProductListControl", urlPatterns = { "/admin-product-list" })
public class ProductListControl extends HttpServlet {

    @Override
    protected void doGet(HttpServletRequest request, HttpServletResponse response)
            throws ServletException, IOException {
        HttpSession session = request.getSession();
        User user = (User) session.getAttribute("ACC");
        if (user == null || !"Admin".equalsIgnoreCase(user.getRole())) {
            response.sendRedirect("user-login");
            return;
        }

        ProductDAO dao = new ProductDAO();

        // Lấy các tham số lọc và tìm kiếm từ request
        String searchName = request.getParameter("searchName");
        String ramFilter = request.getParameter("ramFilter");
        String romFilter = request.getParameter("romFilter");
        String colorFilter = request.getParameter("colorFilter");

        // Lấy danh sách cho các bộ lọc dropdown
        List<String> listRAM = dao.getAllRAMs();
        List<String> listROM = dao.getAllROMs();

        // Lấy danh sách sản phẩm đã được lọc và tìm kiếm
        List<Product> listP = dao.searchAndFilterProducts(searchName, ramFilter, romFilter);

        // Đặt các thuộc tính để gửi sang JSP
        request.setAttribute("listP", listP);
        request.setAttribute("listRAM", listRAM);
        request.setAttribute("listROM", listROM);
        request.setAttribute("searchName", searchName);
        request.setAttribute("selectedRam", ramFilter);
        request.setAttribute("selectedRom", romFilter);
        request.setAttribute("selectedColor", colorFilter);

        // Đánh dấu trang đang active cho sidebar
        request.setAttribute("activePage", "product-management");

        request.getRequestDispatcher("ManagerProduct.jsp").forward(request, response);
    }
}
