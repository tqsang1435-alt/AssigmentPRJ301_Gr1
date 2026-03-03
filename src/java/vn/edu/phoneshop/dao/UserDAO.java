package vn.edu.phoneshop.dao;

import vn.edu.phoneshop.model.User;
import vn.edu.phoneshop.utils.DBContext;
import java.sql.*;
import java.util.*;
import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.ArrayList;
import java.util.List;

public class UserDAO {

    private DBContext dbContext = new DBContext();

    public User getUserByID(int userID) {
        String sql = "SELECT UserID, FullName, Email, PhoneNumber, Address, Password, Role, CreateDate FROM Users WHERE UserID = ?";
        User user = null;
        try (Connection conn = dbContext.getConnection(); PreparedStatement pstmt = conn.prepareStatement(sql);) {
            pstmt.setInt(1, userID);
            ResultSet rs = pstmt.executeQuery();
            if (rs.next()) {
                user = new User(rs.getInt("UserID"), rs.getString("FullName"), rs.getString("Email"),
                        rs.getString("PhoneNumber"), rs.getString("Address"), rs.getString("Password"),
                        rs.getString("Role"), new java.util.Date(rs.getTimestamp("CreateDate").getTime()));
            }
            rs.close();
        } catch (SQLException e) {
            e.printStackTrace();
            System.out.println("Lỗi lấy người dùng theo ID: " + e.getMessage());
        }
        return user;
    }

    public User getUserByEmail(String email) {
        String sql = "SELECT UserID, FullName, Email, PhoneNumber, Address, Password, Role, CreateDate FROM Users WHERE Email = ?";
        User user = null;
        try (Connection conn = dbContext.getConnection(); PreparedStatement pstmt = conn.prepareStatement(sql);) {
            pstmt.setString(1, email);
            ResultSet rs = pstmt.executeQuery();
            if (rs.next()) {
                user = new User(
                        rs.getInt("UserID"),
                        rs.getString("FullName"),
                        rs.getString("Email"),
                        rs.getString("PhoneNumber"),
                        rs.getString("Address"),
                        rs.getString("Password"),
                        rs.getString("Role"),
                        new java.util.Date(rs.getTimestamp("CreateDate").getTime()));
            }
            rs.close();
        } catch (SQLException e) {
            e.printStackTrace();
            System.out.println("Lỗi lấy người dùng theo Email: " + e.getMessage());
        }
        return user;
    }

    public List<User> getAllUsers() {
        String sql = "SELECT UserID, FullName, Email, PhoneNumber, Address, Password, Role, CreateDate FROM Users";
        List<User> userList = new ArrayList<>();
        try (Connection conn = dbContext.getConnection(); PreparedStatement pstmt = conn.prepareStatement(sql);) {
            ResultSet rs = pstmt.executeQuery();
            while (rs.next()) {
                User user = new User(
                        rs.getInt("UserID"),
                        rs.getString("FullName"),
                        rs.getString("Email"),
                        rs.getString("PhoneNumber"),
                        rs.getString("Address"),
                        rs.getString("Password"),
                        rs.getString("Role"),
                        new java.util.Date(rs.getTimestamp("CreateDate").getTime()));
                userList.add(user);
            }
            rs.close();
        } catch (SQLException e) {
            e.printStackTrace();
            System.out.println("Lỗi lấy danh sách người dùng: " + e.getMessage());
        }
        return userList;
    }

    public boolean insertUser(User user) {
        String sql = "INSERT INTO Users (FullName, Email, PhoneNumber, Address, Password, Role, CreateDate) " + "VALUES (?, ?, ?, ?, ?, ?, ?)";

        try (Connection conn = dbContext.getConnection(); PreparedStatement pstmt = conn.prepareStatement(sql)) {

            pstmt.setString(1, user.getFullName());
            pstmt.setString(2, user.getEmail());
            pstmt.setString(3, user.getPhoneNumber());
            pstmt.setString(4, user.getAddress());
            pstmt.setString(5, user.getPassword());
            pstmt.setString(6, user.getRole());
            pstmt.setTimestamp(7, new Timestamp(user.getCreateDate().getTime()));

            int rowsAffected = pstmt.executeUpdate();
            return rowsAffected > 0;
        } catch (SQLException e) {
            e.printStackTrace();
            System.out.println("Lỗi thêm người dùng: " + e.getMessage());
        }

        return false;
    }

    public boolean updateUser(User user) {
        String sql = "UPDATE Users SET FullName = ?, Email = ?, PhoneNumber = ?, Address = ?, Password = ?, Role = ? " + "WHERE UserID = ?";

        try (Connection conn = dbContext.getConnection(); PreparedStatement pstmt = conn.prepareStatement(sql)) {

            pstmt.setString(1, user.getFullName());
            pstmt.setString(2, user.getEmail());
            pstmt.setString(3, user.getPhoneNumber());
            pstmt.setString(4, user.getAddress());
            pstmt.setString(5, user.getPassword());
            pstmt.setString(6, user.getRole());
            pstmt.setInt(7, user.getUserID());

            int rowsAffected = pstmt.executeUpdate();
            return rowsAffected > 0;
        } catch (SQLException e) {
            e.printStackTrace();
            System.out.println("Lỗi cập nhật người dùng: " + e.getMessage());
        }

        return false;
    }

    public boolean deleteUser(int userID) {
        String sql = "DELETE FROM Users WHERE UserID = ?";

        try (Connection conn = dbContext.getConnection(); PreparedStatement pstmt = conn.prepareStatement(sql)) {

            pstmt.setInt(1, userID);

            int rowsAffected = pstmt.executeUpdate();
            return rowsAffected > 0;
        } catch (SQLException e) {
            e.printStackTrace();
            System.out.println("Lỗi xóa người dùng: " + e.getMessage());
        }

        return false;
    }

    public List<Map<String, Object>> getPurchaseHistory(int userID) {
        String sql = "SELECT o.OrderID, o.OrderDate, o.TotalMoney, o.ShippingAddress, o.Status, o.Note, "
                + "       od.OrderDetailID, od.ProductID, od.Quantity, od.Price, p.ProductName "
                + "FROM Orders o "
                + "LEFT JOIN OrderDetails od ON o.OrderID = od.OrderID "
                + "LEFT JOIN Products p ON od.ProductID = p.ProductID "
                + "WHERE o.UserID = ? "
                + "ORDER BY o.OrderDate DESC, o.OrderID";

        List<Map<String, Object>> purchaseHistory = new ArrayList<>();

        try (Connection conn = dbContext.getConnection(); PreparedStatement pstmt = conn.prepareStatement(sql)) {

            pstmt.setInt(1, userID);
            ResultSet rs = pstmt.executeQuery();

            while (rs.next()) {
                Map<String, Object> record = new LinkedHashMap<>();
                record.put("OrderID", rs.getInt("OrderID"));
                record.put("OrderDate", rs.getTimestamp("OrderDate"));
                record.put("TotalMoney", rs.getBigDecimal("TotalMoney"));
                record.put("ShippingAddress", rs.getString("ShippingAddress"));
                record.put("Status", rs.getString("Status"));
                record.put("Note", rs.getString("Note"));
                record.put("OrderDetailID", rs.getInt("OrderDetailID"));
                record.put("ProductID", rs.getInt("ProductID"));
                record.put("ProductName", rs.getString("ProductName"));
                record.put("Quantity", rs.getInt("Quantity"));
                record.put("Price", rs.getBigDecimal("Price"));

                purchaseHistory.add(record);
            }
            rs.close();
        } catch (SQLException e) {
            e.printStackTrace();
            System.out.println("Lỗi lấy lịch sử mua hàng: " + e.getMessage());
        }

        return purchaseHistory;
    }

    public boolean isEmailExists(String email) {
        String sql = "SELECT COUNT(*) FROM Users WHERE Email = ?";

        try (Connection conn = dbContext.getConnection(); PreparedStatement pstmt = conn.prepareStatement(sql)) {

            pstmt.setString(1, email);
            ResultSet rs = pstmt.executeQuery();

            if (rs.next()) {
                return rs.getInt(1) > 0;
            }
            rs.close();
        } catch (SQLException e) {
            e.printStackTrace();
            System.out.println("Lỗi kiểm tra email: " + e.getMessage());
        }
        return false;
    }
}
