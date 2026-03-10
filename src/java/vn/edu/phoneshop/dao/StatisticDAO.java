package vn.edu.phoneshop.dao;

import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.util.ArrayList;
import java.util.List;
import vn.edu.phoneshop.model.RevenueStat;
import vn.edu.phoneshop.utils.DBContext;

public class StatisticDAO extends DBContext {

    private int countFromTable(String tableName, String condition) {
        String sql = "SELECT COUNT(*) FROM " + tableName;
        if (condition != null && !condition.isEmpty()) {
            sql += " WHERE " + condition;
        }
        try (Connection conn = getConnection();
                PreparedStatement ps = conn.prepareStatement(sql);
                ResultSet rs = ps.executeQuery()) {
            if (rs.next()) {
                return rs.getInt(1);
            }
        } catch (Exception e) {
            e.printStackTrace();
        }
        return 0;
    }

    public int getTotalCustomers() {
        return countFromTable("Users", "Role = 'Customer'");
    }

    public int getTotalProducts() {
        return countFromTable("Products", null);
    }

    public int getNewOrders() {
        return countFromTable("Orders", "Status = 1"); // Status 1: Chờ xác nhận
    }

    public double getCurrentMonthRevenue() {
        String sql = "SELECT SUM(TotalMoney) FROM Orders WHERE Status = 4 AND MONTH(OrderDate) = MONTH(GETDATE()) AND YEAR(OrderDate) = YEAR(GETDATE())";
        try (Connection conn = getConnection();
                PreparedStatement ps = conn.prepareStatement(sql);
                ResultSet rs = ps.executeQuery()) {
            if (rs.next()) {
                return rs.getDouble(1);
            }
        } catch (Exception e) {
            e.printStackTrace();
        }
        return 0;
    }

    public List<RevenueStat> getMonthlyRevenue() {
        List<RevenueStat> list = new ArrayList<>();
        // Chỉ tính doanh thu từ các đơn hàng đã hoàn thành (Status = 4)
        String sql = "SELECT YEAR(OrderDate) AS [Nam], MONTH(OrderDate) AS [Thang], COUNT(OrderID) AS [SoLuongDon], SUM(TotalMoney) AS [TongDoanhThu] FROM Orders WHERE Status = 4 GROUP BY YEAR(OrderDate), MONTH(OrderDate) ORDER BY YEAR(OrderDate) DESC, MONTH(OrderDate) DESC";
        try (Connection conn = getConnection();
                PreparedStatement ps = conn.prepareStatement(sql);
                ResultSet rs = ps.executeQuery()) {
            while (rs.next()) {
                RevenueStat stat = new RevenueStat();
                stat.setYear(rs.getInt("Nam"));
                stat.setMonth(rs.getInt("Thang"));
                stat.setOrderCount(rs.getInt("SoLuongDon"));
                stat.setTotalRevenue(rs.getDouble("TongDoanhThu"));
                list.add(stat);
            }
        } catch (Exception e) {
            e.printStackTrace();
        }
        return list;
    }
}