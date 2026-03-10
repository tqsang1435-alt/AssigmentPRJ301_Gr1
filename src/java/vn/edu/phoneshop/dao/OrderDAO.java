/*
 * Click nbfs://nbhost/SystemFileSystem/Templates/Licenses/license-default.txt to change this license
 * Click nbfs://nbhost/SystemFileSystem/Templates/Classes/Class.java to edit this template
 */
package vn.edu.phoneshop.dao;

/**
 *
 * @author Lenovo
 */
import java.sql.*;
import java.util.*;
import vn.edu.phoneshop.model.Order;
import vn.edu.phoneshop.model.OrderDetail;
import vn.edu.phoneshop.utils.DBContext;

import java.sql.Date;

public class OrderDAO {

    public int createOrder(int userID, double totalMoney, int status) {
        String sql = "INSERT INTO Orders (UserID, OrderDate, TotalMoney, Status) VALUES (?, GETDATE(), ?, ?)";
        try (Connection con = new vn.edu.phoneshop.utils.DBContext().getConnection();
                PreparedStatement ps = con.prepareStatement(sql, Statement.RETURN_GENERATED_KEYS)) {

            ps.setInt(1, userID);
            ps.setDouble(2, totalMoney);
            ps.setInt(3, status);

            ps.executeUpdate();
            ResultSet rs = ps.getGeneratedKeys();
            if (rs.next()) {
                return rs.getInt(1);
            }
        } catch (Exception e) {
            e.printStackTrace();
        }
        return 0;
    }

    public void updateOrderStatus(int orderId, int status) {
        String sql = "UPDATE Orders SET Status = ? WHERE OrderID = ?";
        try (Connection con = new vn.edu.phoneshop.utils.DBContext().getConnection();
                PreparedStatement ps = con.prepareStatement(sql)) {
            ps.setInt(1, status);
            ps.setInt(2, orderId);
            ps.executeUpdate();
        } catch (Exception e) {
            e.printStackTrace();
        }
    }

    public void updateOrderStatusAndAddress(int orderID, int status, String address) {
        // Ép trạng thái thành 4 (Hoàn thành) và lưu địa chỉ từ User vào bảng Orders
        String sql = "UPDATE Orders SET Status = ?, ShippingAddress = ? WHERE OrderID = ?";
        try (Connection con = new vn.edu.phoneshop.utils.DBContext().getConnection();
                PreparedStatement ps = con.prepareStatement(sql)) {
            ps.setInt(1, status); // Sẽ truyền vào số 4
            ps.setString(2, address); // Địa chỉ lấy từ User
            ps.setInt(3, orderID);
            ps.executeUpdate();
        } catch (Exception e) {
            e.printStackTrace();
        }
    }

    public List<Order> getOrdersByUserId(int userId) {
        List<Order> list = new ArrayList<>();
        String sql = "SELECT * FROM Orders WHERE UserID = ? ORDER BY OrderDate DESC";
        try (Connection con = new vn.edu.phoneshop.utils.DBContext().getConnection();
                PreparedStatement ps = con.prepareStatement(sql);) {
            ps.setInt(1, userId);
            ResultSet rs = ps.executeQuery();
            while (rs.next()) {
                Order o = new Order();
                o.setOrderID(rs.getInt("OrderID"));
                o.setUserID(rs.getInt("UserID"));
                o.setOrderDate(rs.getDate("OrderDate"));
                o.setTotalMoney(rs.getDouble("TotalMoney"));
                o.setShippingAddress(rs.getString("ShippingAddress"));
                o.setNote(rs.getString("Note"));
                o.setStatus(rs.getInt("Status"));
                list.add(o);
            }
        } catch (Exception e) {
            e.printStackTrace();
        }
        return list;
    }

    public void insertOrderDetail(int orderId, int productId, int quantity, double price) {
        String sql = "INSERT INTO OrderDetails (OrderID, ProductID, Quantity, Price) VALUES (?, ?, ?, ?)";
        try (Connection con = new DBContext().getConnection(); PreparedStatement ps = con.prepareStatement(sql)) {
            ps.setInt(1, orderId);
            ps.setInt(2, productId);
            ps.setInt(3, quantity);
            ps.setDouble(4, price);
            ps.executeUpdate();
        } catch (Exception e) {
            e.printStackTrace();
        }
    }

    public Map<Integer, String> getProductNamesByOrderID(int orderID) {
        Map<Integer, String> map = new HashMap<>();
        String sql = "SELECT p.ProductID, p.ProductName FROM OrderDetails od "
                + "JOIN Products p ON od.ProductID = p.ProductID "
                + "WHERE od.OrderID = ?";
        try (Connection con = new DBContext().getConnection(); PreparedStatement ps = con.prepareStatement(sql)) {
            ps.setInt(1, orderID);
            ResultSet rs = ps.executeQuery();
            while (rs.next()) {
                map.put(rs.getInt("ProductID"), rs.getString("ProductName"));
            }
        } catch (Exception e) {
            e.printStackTrace();
        }
        return map;
    }

    public List<OrderDetail> getOrderDetailByOrderID(int orderID) {
        List<OrderDetail> list = new ArrayList<>();
        String sql = "SELECT od.*, p.ProductName FROM OrderDetails od "
                + "JOIN Products p ON od.ProductID = p.ProductID "
                + "WHERE od.OrderID = ?";
        try (Connection con = new vn.edu.phoneshop.utils.DBContext().getConnection();
                PreparedStatement ps = con.prepareStatement(sql)) {

            ps.setInt(1, orderID);
            ResultSet rs = ps.executeQuery();

            while (rs.next()) {
                OrderDetail od = new OrderDetail();
                od.setOrderID(rs.getInt("OrderID"));
                od.setProductID(rs.getInt("ProductID"));
                od.setQuantity(rs.getInt("Quantity"));
                od.setPrice(rs.getDouble("Price"));

                list.add(od);
            }
        } catch (Exception e) {
            e.printStackTrace();
        }
        return list;
    }

    public Map<Integer, String> getProductNamesMap(int orderID) {
        Map<Integer, String> map = new HashMap<>();
        String sql = "SELECT p.ProductID, p.ProductName FROM OrderDetails od "
                + "JOIN Products p ON od.ProductID = p.ProductID WHERE od.OrderID = ?";
        try (Connection con = new vn.edu.phoneshop.utils.DBContext().getConnection();
                PreparedStatement ps = con.prepareStatement(sql)) {
            ps.setInt(1, orderID);
            ResultSet rs = ps.executeQuery();
            while (rs.next()) {
                map.put(rs.getInt("ProductID"), rs.getString("ProductName"));
            }
        } catch (Exception e) {
            e.printStackTrace();
        }
        return map;
    }

    // Lấy thông tin đơn hàng để biết UserID và Tổng tiền
    public Order getOrderById(int orderId) {
        String query = "SELECT * FROM Orders WHERE OrderID = ?";
        try (Connection con = new vn.edu.phoneshop.utils.DBContext().getConnection();
                PreparedStatement ps = con.prepareStatement(query)) {
            ps.setInt(1, orderId);
            ResultSet rs = ps.executeQuery();
            if (rs.next()) {
                Order o = new Order();
                o.setOrderID(rs.getInt("OrderID"));
                o.setUserID(rs.getInt("UserID"));
                o.setOrderDate(rs.getDate("OrderDate"));
                o.setTotalMoney(rs.getDouble("TotalMoney"));
                o.setShippingAddress(rs.getString("ShippingAddress"));
                o.setNote(rs.getString("Note"));
                o.setStatus(rs.getInt("Status"));
                return o;
            }
        } catch (Exception e) {
            e.printStackTrace();
        }
        return null;
    }

}
