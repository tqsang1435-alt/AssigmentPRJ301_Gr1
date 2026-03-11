package vn.edu.phoneshop.controller;

import jakarta.servlet.ServletException;
import jakarta.servlet.annotation.WebServlet;
import jakarta.servlet.http.HttpServlet;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;
import jakarta.servlet.http.HttpSession;
import java.io.IOException;
import java.util.ArrayList;
import java.util.Collections;
import java.util.List;
import vn.edu.phoneshop.dao.StatisticDAO;
import vn.edu.phoneshop.model.RevenueStat;
import vn.edu.phoneshop.model.User;

@WebServlet(name = "RevenueControl", urlPatterns = { "/revenue-stats" })
public class RevenueControl extends HttpServlet {
    @Override
    protected void doGet(HttpServletRequest request, HttpServletResponse response)
            throws ServletException, IOException {

        // 1. Kiểm tra quyền Admin
        HttpSession session = request.getSession();
        User user = (User) session.getAttribute("ACC");
        if (user == null || !"Admin".equalsIgnoreCase(user.getRole())) {
            response.sendRedirect("user-login");
            return;
        }

        // 2. Gọi DAO lấy dữ liệu thống kê
        StatisticDAO dao = new StatisticDAO();
        List<RevenueStat> monthlyRevenue = dao.getMonthlyRevenue();
        List<RevenueStat> dailyRevenue = dao.getDailyRevenue();
        List<RevenueStat> productRevenue = dao.getProductRevenue();

        // 3. Đẩy dữ liệu qua JSP
        request.setAttribute("monthlyRevenue", monthlyRevenue);
        request.setAttribute("dailyRevenue", dailyRevenue);
        request.setAttribute("productRevenue", productRevenue);

        // 4. Xử lý dữ liệu JSON cho biểu đồ (Chart.js)

        // --- Biểu đồ doanh thu theo NGÀY (Line Chart) ---
        // Đảo ngược danh sách để hiển thị thời gian từ cũ -> mới (trái qua phải)
        List<RevenueStat> chartDailyList = new ArrayList<>(dailyRevenue);
        Collections.reverse(chartDailyList);

        StringBuilder dLabels = new StringBuilder("[");
        StringBuilder dData = new StringBuilder("[");
        for (int i = 0; i < chartDailyList.size(); i++) {
            RevenueStat s = chartDailyList.get(i);
            dLabels.append("\"").append(s.getDate()).append("\"");
            dData.append(s.getTotalRevenue());
            if (i < chartDailyList.size() - 1) {
                dLabels.append(",");
                dData.append(",");
            }
        }
        dLabels.append("]");
        dData.append("]");
        request.setAttribute("dailyLabels", dLabels.toString());
        request.setAttribute("dailyData", dData.toString());

        // --- Biểu đồ doanh thu theo THÁNG (Bar Chart) ---
        List<RevenueStat> chartMonthlyList = new ArrayList<>(monthlyRevenue);
        Collections.reverse(chartMonthlyList);

        StringBuilder mLabels = new StringBuilder("[");
        StringBuilder mData = new StringBuilder("[");
        for (int i = 0; i < chartMonthlyList.size(); i++) {
            RevenueStat s = chartMonthlyList.get(i);
            mLabels.append("\"").append(s.getMonth()).append("/").append(s.getYear()).append("\"");
            mData.append(s.getTotalRevenue());
            if (i < chartMonthlyList.size() - 1) {
                mLabels.append(",");
                mData.append(",");
            }
        }
        mLabels.append("]");
        mData.append("]");
        request.setAttribute("monthlyLabels", mLabels.toString());
        request.setAttribute("monthlyData", mData.toString());

        // --- Biểu đồ doanh thu theo SẢN PHẨM (Pie/Doughnut Chart) ---
        // Lấy Top 5 sản phẩm bán chạy nhất để biểu đồ đẹp hơn
        int limit = Math.min(productRevenue.size(), 5);
        StringBuilder pLabels = new StringBuilder("[");
        StringBuilder pData = new StringBuilder("[");
        for (int i = 0; i < limit; i++) {
            RevenueStat s = productRevenue.get(i);
            // Xử lý tên sản phẩm có dấu ngoặc kép để tránh lỗi JS
            String pName = s.getProductName().replace("\"", "\\\"");
            pLabels.append("\"").append(pName).append("\"");
            pData.append(s.getTotalRevenue());
            if (i < limit - 1) {
                pLabels.append(",");
                pData.append(",");
            }
        }
        pLabels.append("]");
        pData.append("]");
        request.setAttribute("productLabels", pLabels.toString());
        request.setAttribute("productData", pData.toString());

        // Chuyển hướng đến trang hiển thị (bạn cần tạo file revenue.jsp)
        request.getRequestDispatcher("revenue.jsp").forward(request, response);
    }
}
