package vn.edu.phoneshop.dao;

import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.util.ArrayList;
import java.util.List;
import vn.edu.phoneshop.model.User;
import vn.edu.phoneshop.utils.DBContext;

public class UserDAO {

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
                        rs.getInt("UserID"),
                        rs.getString("FullName"),
                        rs.getString("Email"),
                        rs.getString("PhoneNumber"),
                        rs.getString("Address"),
                        rs.getString("Password"),
                        rs.getString("Role"),
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
    }
}
