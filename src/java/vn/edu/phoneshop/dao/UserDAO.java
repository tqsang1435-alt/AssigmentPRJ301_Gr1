package vn.edu.phoneshop.dao;

import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.util.ArrayList;
import java.util.List;
import vn.edu.phoneshop.model.User;
import vn.edu.phoneshop.utils.DBContext;

public class UserDAO extends DBContext {

    Connection conn = null;
    PreparedStatement ps = null;
    ResultSet rs = null;

    // 1. READ: Lấy danh sách khách hàng
    public List<User> getAllUsers() {
        List<User> list = new ArrayList<>();
        String query = "SELECT * FROM Users";
        try {
            conn = getConnection();
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
        String query = "INSERT INTO Users (FullName, Email, PhoneNumber, Address, Password, Role, RewardPoints, CustomerType) VALUES (?, ?, ?, ?, ?, 'Customer', 0, 'New Member')";
        try {
            conn = getConnection();
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
            conn = getConnection();
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
        String getPointSql = "SELECT RewardPoints FROM Users WHERE UserID = ?";
        String updateSql = "UPDATE Users SET RewardPoints = ?, CustomerType = ? WHERE UserID = ?";

        try (Connection conn = getConnection();
                PreparedStatement psGet = conn.prepareStatement(getPointSql);
                PreparedStatement psUpdate = conn.prepareStatement(updateSql)) {

            // Quy tắc: 100,000 VNĐ = 1 điểm
            int pointsEarned = (int) (totalOrderMoney / 100000);

            // Lấy điểm hiện tại
            psGet.setInt(1, userId);
            try (ResultSet rs = psGet.executeQuery()) {
                int currentPoints = 0;
                if (rs.next()) {
                    currentPoints = rs.getInt("RewardPoints");
                }

                int newTotalPoints = currentPoints + pointsEarned;

                // Logic Phân loại khách hàng
                String newType = "New Member";
                if (newTotalPoints >= 2000) {
                    newType = "Diamond";
                } else if (newTotalPoints >= 1000) {
                    newType = "Gold";
                } else if (newTotalPoints >= 500) {
                    newType = "Silver";
                } else if (newTotalPoints >= 100) {
                    newType = "Bronze";
                }

                // Cập nhật vào DB
                psUpdate.setInt(1, newTotalPoints);
                psUpdate.setString(2, newType);
                psUpdate.setInt(3, userId);
                psUpdate.executeUpdate();

                System.out.println(
                        "User " + userId + " tích lũy thêm " + pointsEarned + " điểm. Hạng hiện tại: " + newType);
            }
        } catch (Exception e) {
            e.printStackTrace();
        }
    }

    // 5. DELETE: Xóa user
    public void deleteUser(int userId) {
        String query = "DELETE FROM Users WHERE UserID = ?";
        try {
            conn = getConnection();
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
            conn = getConnection();
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
            conn = getConnection();
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

    public boolean updateUserProfileDat(int userID, String fullName, String phone, String address) {
        String sql = "UPDATE Users SET FullName = ?, PhoneNumber = ?, Address = ? WHERE UserID = ?";
        try (Connection con = getConnection();
                PreparedStatement ps = con.prepareStatement(sql)) {
            ps.setString(1, fullName);
            ps.setString(2, phone);
            ps.setString(3, address);
            ps.setInt(4, userID);
            return ps.executeUpdate() > 0;
        } catch (Exception e) {
            e.printStackTrace();
        }
        return false;
    }

    // 7. Lấy User theo ID (Hỗ trợ refresh session)
    public User getUserByID(int userId) {
        String query = "SELECT * FROM Users WHERE UserID = ?";
        try {
            conn = getConnection();
            ps = conn.prepareStatement(query);
            ps.setInt(1, userId);
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

    public User getUserById(int userId) {
        String sql = "SELECT * FROM Users WHERE UserID = ?";
        try (Connection conn = getConnection();
                PreparedStatement ps = conn.prepareStatement(sql)) {
            ps.setInt(1, userId);
            try (ResultSet rs = ps.executeQuery()) {
                if (rs.next()) {
                    User user = new User();
                    user.setUserID(rs.getInt("UserID"));
                    user.setFullName(rs.getString("FullName"));
                    user.setEmail(rs.getString("Email"));
                    user.setPhoneNumber(rs.getString("PhoneNumber"));
                    user.setAddress(rs.getString("Address"));
                    user.setPassword(rs.getString("Password"));
                    user.setRole(rs.getString("Role"));
                    user.setCreateDate(rs.getDate("CreateDate"));
                    user.setRewardPoints(rs.getInt("RewardPoints"));
                    user.setCustomerType(rs.getString("CustomerType"));
                    return user;
                }
            }
        } catch (Exception e) {
            e.printStackTrace();
        }
        return null;
    }

    // Cập nhật thông tin User (dùng cho Admin)
    public void updateUser(User user) {
        String sql = "UPDATE Users SET FullName = ?, Email = ?, PhoneNumber = ?, Address = ?, Role = ? WHERE UserID = ?";
        try (Connection conn = getConnection();
                PreparedStatement ps = conn.prepareStatement(sql)) {
            ps.setString(1, user.getFullName());
            ps.setString(2, user.getEmail());
            ps.setString(3, user.getPhoneNumber());
            ps.setString(4, user.getAddress());
            ps.setString(5, user.getRole());
            ps.setInt(6, user.getUserID());
            ps.executeUpdate();
        } catch (Exception e) {
            e.printStackTrace();
        }
    }
}
