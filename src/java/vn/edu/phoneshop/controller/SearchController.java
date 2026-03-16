package vn.edu.phoneshop.controller;

import jakarta.servlet.ServletException;
import jakarta.servlet.annotation.WebServlet;
import jakarta.servlet.http.HttpServlet;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;
import java.io.IOException;
import java.util.ArrayList;
import java.util.List;
import vn.edu.phoneshop.dao.ProductDAO;
import vn.edu.phoneshop.model.Product;

@WebServlet(name = "SearchController", urlPatterns = { "/search" })
public class SearchController extends HttpServlet {

    @Override
    protected void doGet(HttpServletRequest request, HttpServletResponse response)
            throws ServletException, IOException {

        // Lấy từ khóa tìm kiếm từ Header (thử các tên phổ biến để đảm bảo bắt được)
        String search = request.getParameter("txtSearch");
        if (search == null) {
            search = request.getParameter("searchName");
        }
        if (search == null) {
            search = request.getParameter("search");
        }

        // Lấy tham số lọc RAM và ROM
        String ramFilter = request.getParameter("ramFilter");
        String romFilter = request.getParameter("romFilter");
        String pageStr = request.getParameter("page");

        ProductDAO dao = new ProductDAO();

        // Lấy danh sách các tùy chọn RAM và ROM để hiển thị lên dropdown filter
        List<String> listRAM = dao.getAllRAMs();
        List<String> listROM = dao.getAllROMs();

        // Thực hiện tìm kiếm và lọc sản phẩm (Sử dụng logic của Admin như yêu cầu)
        List<Product> fullList = dao.searchAndFilterProducts(search, ramFilter, romFilter);
        if (fullList == null)
            fullList = new ArrayList<>();

        // Xử lý phân trang thủ công trên danh sách kết quả
        int page = 1;
        int pageSize = 10; // Số sản phẩm trên 1 trang
        if (pageStr != null && !pageStr.isEmpty()) {
            try {
                page = Integer.parseInt(pageStr);
            } catch (NumberFormatException e) {
                page = 1;
            }
        }

        int totalProducts = fullList.size();
        int totalPages = (int) Math.ceil((double) totalProducts / pageSize);
        if (page > totalPages && totalPages > 0)
            page = totalPages;
        if (page < 1)
            page = 1;

        int start = (page - 1) * pageSize;
        int end = Math.min(start + pageSize, totalProducts);
        List<Product> listP = (start <= end) ? fullList.subList(start, end) : new ArrayList<>();

        request.setAttribute("listP", listP);
        request.setAttribute("totalPages", totalPages);
        request.setAttribute("currentPage", page);
        request.setAttribute("listRAM", listRAM);
        request.setAttribute("listROM", listROM);

        // Giữ lại giá trị đã chọn để hiển thị trên giao diện
        request.setAttribute("searchName", search);
        request.setAttribute("selectedRam", ramFilter);
        request.setAttribute("selectedRom", romFilter);

        request.getRequestDispatcher("search.jsp").forward(request, response);
    }

    @Override
    protected void doPost(HttpServletRequest request, HttpServletResponse response)
            throws ServletException, IOException {
        doGet(request, response);
    }
}
