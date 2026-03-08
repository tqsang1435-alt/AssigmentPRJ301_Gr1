package vn.edu.phoneshop.dao;

<<<<<<< HEAD
import vn.edu.phoneshop.model.User;
import java.sql.*;
import java.util.*;
import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.ArrayList;
import java.util.List;
import java.util.logging.Level;
import java.util.logging.Logger;
=======
import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.util.ArrayList;
import java.util.List;
import vn.edu.phoneshop.model.User;
>>>>>>> e7b45a906d28bff2867a45721aa3c05ab0f4cc4d
import vn.edu.phoneshop.utils.DBContext;

public class UserDAO {

<<<<<<< HEAD
    private DBContext dbContext = new DBContext();

    public User getUserByID(int userID) throws Exception {
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

    public User getUserByEmail(String email) throws Exception {
        String sql = "SELECT UserID, FullName, Email, PhoneNumber, Address, Password, Role, CreateDate FROM Users WHERE Email = ?";
        User user = null;
        try (Connection conn = dbContext.getConnection(); PreparedStatement pstmt = conn.prepareStatement(sql);) {
            pstmt.setString(1, email);
            ResultSet rs = pstmt.executeQuery();
            if (rs.next()) {
                user = new User(
=======
    Connection conn = null;
    PreparedStatement ps = null;
    ResultSet rs = null;

    // 1. READ: Lấy danh sách khách hàng
    public List<User> getAllUsers() {
        List<User> list = new ArrayList<>();
        String query = "SELECT * FROM Users";
        try {
            conn = new DBContext().getConnection();
            ps = conn.prepareStatement(query);
            rs = ps.executeQuery();
            while (rs.next()) {
                list.add(new User(
>>>>>>> e7b45a906d28bff2867a45721aa3c05ab0f4cc4d
                        rs.getInt("UserID"),
                        rs.getString("FullName"),
                        rs.getString("Email"),
                        rs.getString("PhoneNumber"),
                        rs.getString("Address"),
                        rs.getString("Password"),
                        rs.getString("Role"),
<<<<<<< HEAD
                        new java.util.Date(rs.getTimestamp("CreateDate").getTime()));
            }
            rs.close();
        } catch (SQLException e) {
            e.printStackTrace();
            System.out.println("Lỗi lấy người dùng theo Email: " + e.getMessage());
        }
        return user;
    }

    public List<User> getAllUsers() throws Exception {
        String sql = "SELECT UserID, FullName, Email, PhoneNumber, Address, Password, Role, CreateDate FROM Users";
        List<User> userList = new ArrayList<>();
        try (Connection conn = dbContext.getConnection(); PreparedStatement pstmt = conn.prepareStatement(sql);) {
            ResultSet rs = pstmt.executeQuery();
            while (rs.next()) {
                User user = new User(
=======
                        rs.getDate("CreateDate"),
                        rs.getInt("RewardPoints"),
                        rs.getString("CustomerType")));
            }
        } catch (Exception e) {
            e.printStackTrace();
        }
        return list;
    }

    // 2. CREATE: Thêm khách hàng mới (Đăng ký)
    public void registerUser(String fullName, String email, String phone, String address, String password) {
        String query = "INSERT INTO Users (FullName, Email, PhoneNumber, Address, Password, Role, RewardPoints, CustomerType) VALUES (?, ?, ?, ?, ?, 'Customer', 0, 'Regular')";
        try {
            conn = new DBContext().getConnection();
            ps = conn.prepareStatement(query);
            ps.setString(1, fullName);
            ps.setString(2, email);
            ps.setString(3, phone);
            ps.setString(4, address);
            ps.setString(5, password);
            ps.executeUpdate();
        } catch (Exception e) {
            e.printStackTrace();
        }
    }

    // 3. UPDATE: Cập nhật thông tin cá nhân
    public void updateUserProfile(int userId, String fullName, String phone, String address) {
        String query = "UPDATE Users SET FullName = ?, PhoneNumber = ?, Address = ? WHERE UserID = ?";
        try {
            conn = new DBContext().getConnection();
            ps = conn.prepareStatement(query);
            ps.setString(1, fullName);
            ps.setString(2, phone);
            ps.setString(3, address);
            ps.setInt(4, userId);
            ps.executeUpdate();
        } catch (Exception e) {
            e.printStackTrace();
        }
    }

    // 4. LOGIC TÍCH ĐIỂM & PHÂN LOẠI KHÁCH HÀNG
    // Hàm này sẽ được gọi sau khi đơn hàng (Order) hoàn tất (Status = 4)
    public void updateRewardPoints(int userId, double totalOrderMoney) {
        try {
            conn = new DBContext().getConnection();

            // Quy tắc: 100,000 VNĐ = 1 điểm
            int pointsEarned = (int) (totalOrderMoney / 100000);

            // Lấy điểm hiện tại
            String getPointSql = "SELECT RewardPoints FROM Users WHERE UserID = ?";
            ps = conn.prepareStatement(getPointSql);
            ps.setInt(1, userId);
            rs = ps.executeQuery();

            int currentPoints = 0;
            if (rs.next()) {
                currentPoints = rs.getInt("RewardPoints");
            }

            int newTotalPoints = currentPoints + pointsEarned;

            // Logic Phân loại khách hàng
            String newType = "Regular";
            if (newTotalPoints >= 1000) {
                newType = "Premium";
            } else if (newTotalPoints >= 500) {
                newType = "VIP";
            } else {
                newType = "Regular";
            }

            // Cập nhật vào DB
            String updateSql = "UPDATE Users SET RewardPoints = ?, CustomerType = ? WHERE UserID = ?";
            ps = conn.prepareStatement(updateSql);
            ps.setInt(1, newTotalPoints);
            ps.setString(2, newType);
            ps.setInt(3, userId);
            ps.executeUpdate();

            System.out
                    .println("User " + userId + " tích lũy thêm " + pointsEarned + " điểm. Hạng hiện tại: " + newType);

        } catch (Exception e) {
            e.printStackTrace();
        }
    }

    // 5. DELETE: Xóa user
    public void deleteUser(int userId) {
        String query = "DELETE FROM Users WHERE UserID = ?";
        try {
            conn = new DBContext().getConnection();
            ps = conn.prepareStatement(query);
            ps.setInt(1, userId);
            ps.executeUpdate();
        } catch (Exception e) {
            e.printStackTrace();
        }
    }

    // Hàm hỗ trợ Login
    public User login(String email, String password) {
        String query = "SELECT * FROM Users WHERE Email = ? AND Password = ?";
        try {
            conn = new DBContext().getConnection();
            ps = conn.prepareStatement(query);
            ps.setString(1, email);
            ps.setString(2, password);
            rs = ps.executeQuery();
            if (rs.next()) {
                return new User(
>>>>>>> e7b45a906d28bff2867a45721aa3c05ab0f4cc4d
                        rs.getInt("UserID"),
                        rs.getString("FullName"),
                        rs.getString("Email"),
                        rs.getString("PhoneNumber"),
                        rs.getString("Address"),
                        rs.getString("Password"),
                        rs.getString("Role"),
<<<<<<< HEAD
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

    public boolean insertUser(User user) throws Exception {
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
        } catch (Exception ex) {
            Logger.getLogger(UserDAO.class.getName()).log(Level.SEVERE, null, ex);
        }

        return false;
    }

    public boolean deleteUser(int userID) throws Exception {
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

    public List<Map<String, Object>> getPurchaseHistory(int userID) throws Exception {
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

    public boolean isEmailExists(String email) throws Exception {
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
=======
                        rs.getDate("CreateDate"),
                        rs.getInt("RewardPoints"),
                        rs.getString("CustomerType"));
            }
        } catch (Exception e) {
            e.printStackTrace();
        }
        return null;
    }

    // Hàm kiểm tra email tồn tại
    public User checkUserExist(String email) {
        String query = "SELECT * FROM Users WHERE Email = ?";
        try {
            conn = new DBContext().getConnection();
            ps = conn.prepareStatement(query);
            ps.setString(1, email);
            rs = ps.executeQuery();
            if (rs.next()) {
                return new User(
                        rs.getInt("UserID"),
                        rs.getString("FullName"),
                        rs.getString("Email"),
                        rs.getString("PhoneNumber"),
                        rs.getString("Address"),
                        rs.getString("Password"),
                        rs.getString("Role"),
                        rs.getDate("CreateDate"),
                        rs.getInt("RewardPoints"),
                        rs.getString("CustomerType"));
            }
        } catch (Exception e) {
            e.printStackTrace();
        }
        return null;
>>>>>>> e7b45a906d28bff2867a45721aa3c05ab0f4cc4d
    }
}
