package vn.edu.phoneshop.dao;

import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.util.ArrayList;
import java.util.List;
import vn.edu.phoneshop.model.RevenueStat;
import vn.edu.phoneshop.utils.DBContext;

public class StatisticDAO extends DBContext {

    // Lấy thống kê doanh thu theo tháng (Yêu cầu của bạn)
    public List<RevenueStat> getMonthlyRevenue() {
        List<RevenueStat> list = new ArrayList<>();
        String sql = "SELECT YEAR(OrderDate) AS [Year], MONTH(OrderDate) AS [Month], "
                + "COUNT(OrderID) AS [OrderCount], SUM(TotalMoney) AS [TotalRevenue] "
                + "FROM Orders WHERE Status != 0 "
                + "GROUP BY YEAR(OrderDate), MONTH(OrderDate) "
                + "ORDER BY YEAR(OrderDate) DESC, MONTH(OrderDate) DESC";
        try (Connection conn = getConnection();
                PreparedStatement ps = conn.prepareStatement(sql);
                ResultSet rs = ps.executeQuery()) {
            while (rs.next()) {
                RevenueStat stat = new RevenueStat();
                stat.setYear(rs.getInt("Year"));
                stat.setMonth(rs.getInt("Month"));
                stat.setOrderCount(rs.getInt("OrderCount"));
                stat.setTotalRevenue(rs.getDouble("TotalRevenue"));
                list.add(stat);
            }
        } catch (Exception e) {
            e.printStackTrace();
        }
        return list;
    }

    // Lấy thống kê doanh thu theo NGÀY
    public List<RevenueStat> getDailyRevenue() {
        List<RevenueStat> list = new ArrayList<>();
        String sql = "SELECT CAST(OrderDate AS DATE) AS [Date], "
                + "COUNT(OrderID) AS [OrderCount], SUM(TotalMoney) AS [TotalRevenue] "
                + "FROM Orders WHERE Status != 0 "
                + "GROUP BY CAST(OrderDate AS DATE) "
                + "ORDER BY CAST(OrderDate AS DATE) DESC";
        try (Connection conn = getConnection();
                PreparedStatement ps = conn.prepareStatement(sql);
                ResultSet rs = ps.executeQuery()) {
            while (rs.next()) {
                RevenueStat stat = new RevenueStat();
                stat.setDate(rs.getDate("Date"));
                stat.setOrderCount(rs.getInt("OrderCount"));
                stat.setTotalRevenue(rs.getDouble("TotalRevenue"));
                list.add(stat);
            }
        } catch (Exception e) {
            e.printStackTrace();
        }
        return list;
    }

    // Lấy thống kê doanh thu theo SẢN PHẨM
    public List<RevenueStat> getProductRevenue() {
        List<RevenueStat> list = new ArrayList<>();
        String sql = "SELECT p.ProductName, COUNT(DISTINCT o.OrderID) AS [OrderCount], "
                + "SUM(od.Quantity) AS [TotalQuantity], SUM(od.Quantity * od.Price) AS [TotalRevenue] "
                + "FROM Products p JOIN OrderDetails od ON p.ProductID = od.ProductID "
                + "JOIN Orders o ON od.OrderID = o.OrderID WHERE o.Status != 0 "
                + "GROUP BY p.ProductID, p.ProductName ORDER BY SUM(od.Quantity * od.Price) DESC";
        try (Connection conn = getConnection();
                PreparedStatement ps = conn.prepareStatement(sql);
                ResultSet rs = ps.executeQuery()) {
            while (rs.next()) {
                RevenueStat stat = new RevenueStat();
                stat.setProductName(rs.getString("ProductName"));
                stat.setOrderCount(rs.getInt("OrderCount"));
                stat.setQuantity(rs.getInt("TotalQuantity"));
                stat.setTotalRevenue(rs.getDouble("TotalRevenue"));
                list.add(stat);
            }
        } catch (Exception e) {
            e.printStackTrace();
        }
        return list;
    }

    // Tổng số khách hàng (cho Admin Dashboard)
    public int getTotalCustomers() {
        String sql = "SELECT COUNT(*) FROM Users WHERE Role = 'Customer'";
        try (Connection conn = getConnection();
                PreparedStatement ps = conn.prepareStatement(sql);
                ResultSet rs = ps.executeQuery()) {
            if (rs.next())
                return rs.getInt(1);
        } catch (Exception e) {
            e.printStackTrace();
        }
        return 0;
    }

    // Tổng số sản phẩm (cho Admin Dashboard)
    public int getTotalProducts() {
        String sql = "SELECT COUNT(*) FROM Products";
        try (Connection conn = getConnection();
                PreparedStatement ps = conn.prepareStatement(sql);
                ResultSet rs = ps.executeQuery()) {
            if (rs.next())
                return rs.getInt(1);
        } catch (Exception e) {
            e.printStackTrace();
        }
        return 0;
    }

    // Số đơn hàng mới chưa xử lý (Status = 1)
    public int getNewOrders() {
        String sql = "SELECT COUNT(*) FROM Orders WHERE Status = 1";
        try (Connection conn = getConnection();
                PreparedStatement ps = conn.prepareStatement(sql);
                ResultSet rs = ps.executeQuery()) {
            if (rs.next())
                return rs.getInt(1);
        } catch (Exception e) {
            e.printStackTrace();
        }
        return 0;
    }

    // Doanh thu tháng hiện tại
    public double getCurrentMonthRevenue() {
        String sql = "SELECT SUM(TotalMoney) FROM Orders WHERE Status != 0 AND MONTH(OrderDate) = MONTH(GETDATE()) AND YEAR(OrderDate) = YEAR(GETDATE())";
        try (Connection conn = getConnection();
                PreparedStatement ps = conn.prepareStatement(sql);
                ResultSet rs = ps.executeQuery()) {
            if (rs.next())
                return rs.getDouble(1);
        } catch (Exception e) {
            e.printStackTrace();
        }
        return 0;
    }
}
