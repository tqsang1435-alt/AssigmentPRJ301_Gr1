package vn.edu.phoneshop.dao;

import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import vn.edu.phoneshop.model.Order;
import vn.edu.phoneshop.utils.DBContext;

public class OrderDAO {
    Connection conn = null;
    PreparedStatement ps = null;
    ResultSet rs = null;

    // Lấy thông tin đơn hàng để biết UserID và Tổng tiền
    public Order getOrderById(int orderId) {
        String query = "SELECT * FROM Orders WHERE OrderID = ?";
        try {
            conn = new DBContext().getConnection();
            ps = conn.prepareStatement(query);
            ps.setInt(1, orderId);
            rs = ps.executeQuery();
            if (rs.next()) {
                return new Order(
                        rs.getInt("OrderID"),
                        rs.getInt("UserID"),
                        rs.getDate("OrderDate"),
                        rs.getDouble("TotalMoney"),
                        rs.getString("ShippingAddress"),
                        rs.getString("Note"),
                        rs.getInt("Status"));
            }
        } catch (Exception e) {
            e.printStackTrace();
        }
        return null;
    }

    // Cập nhật trạng thái đơn hàng (Ví dụ: Từ Chờ xác nhận -> Hoàn thành)
    public void updateOrderStatus(int orderId, int status) {
        String query = "UPDATE Orders SET Status = ? WHERE OrderID = ?";
        try {
            conn = new DBContext().getConnection();
            ps = conn.prepareStatement(query);
            ps.setInt(1, status);
            ps.setInt(2, orderId);
            ps.executeUpdate();
        } catch (Exception e) {
            e.printStackTrace();
        }
    }
}