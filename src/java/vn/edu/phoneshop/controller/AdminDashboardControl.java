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

@WebServlet(name = "AdminDashboardControl", urlPatterns = {"/admin-dashboard", "/admin"})
public class AdminDashboardControl extends HttpServlet {

    @Override
    protected void doGet(HttpServletRequest request, HttpServletResponse response)
            throws ServletException, IOException {
        HttpSession session = request.getSession();
        User user = (User) session.getAttribute("ACC");

        // Kiểm tra quyền Admin
        if (user == null || !"Admin".equalsIgnoreCase(user.getRole())) {
            response.sendRedirect("user-login");
            return;
        }

        // Lấy dữ liệu thống kê cho dashboard
        StatisticDAO dao = new StatisticDAO();
        int totalCustomers = dao.getTotalCustomers();
        int totalProducts = dao.getTotalProducts();
        int newOrders = dao.getNewOrders();
        double monthRevenue = dao.getCurrentMonthRevenue();

        request.setAttribute("totalCustomers", totalCustomers);
        request.setAttribute("totalProducts", totalProducts);
        request.setAttribute("newOrders", newOrders);
        request.setAttribute("monthRevenue", monthRevenue);

        // --- PHẦN THỐNG KÊ DOANH THU ---
        List<RevenueStat> monthlyRevenue = dao.getMonthlyRevenue();
        List<RevenueStat> dailyRevenue = dao.getDailyRevenue();
        List<RevenueStat> productRevenue = dao.getProductRevenue();

        request.setAttribute("monthlyRevenue", monthlyRevenue);
        request.setAttribute("dailyRevenue", dailyRevenue);
        request.setAttribute("productRevenue", productRevenue);

        // --- Xử lý dữ liệu JSON cho biểu đồ (Chart.js) ---
        // 1. Biểu đồ doanh thu theo NGÀY (Line Chart)
        List<RevenueStat> chartDailyList = new ArrayList<>(dailyRevenue);
        Collections.reverse(chartDailyList); // Đảo ngược để hiển thị từ cũ -> mới

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

        // 2. Biểu đồ doanh thu theo THÁNG (Bar Chart)
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

        // 3. Biểu đồ doanh thu theo SẢN PHẨM (Pie/Doughnut Chart)
        int limit = Math.min(productRevenue.size(), 5); // Top 5
        StringBuilder pLabels = new StringBuilder("[");
        StringBuilder pData = new StringBuilder("[");
        for (int i = 0; i < limit; i++) {
            RevenueStat s = productRevenue.get(i);
            String pName = s.getProductName().replace("\"", "\\\""); // Escape ngoặc kép
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

        // Đánh dấu trang đang active cho sidebar (dùng chung cho dashboard)
        request.setAttribute("activePage", "dashboard");

        request.getRequestDispatcher("admin-dashboard.jsp").forward(request, response);
    }
}
