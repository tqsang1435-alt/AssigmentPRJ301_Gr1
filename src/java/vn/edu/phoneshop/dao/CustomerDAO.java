package vn.edu.phoneshop.dao;

import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.util.ArrayList;
import java.util.List;
import vn.edu.phoneshop.model.User;
import vn.edu.phoneshop.utils.DBContext;
import java.sql.Connection;

public class CustomerDAO extends DBContext {

    public List<User> getCustomers() {
        List<User> list = new ArrayList<>();
        String sql = "SELECT * FROM Users WHERE Role = 'Customer'";
        try {
            Connection connection = getConnection();
            PreparedStatement ps = connection.prepareStatement(sql);
            ResultSet rs = ps.executeQuery();
            while (rs.next()) {
                User u = new User();
                u.setUserID(rs.getInt("UserID"));
                u.setFullName(rs.getString("FullName"));
                u.setEmail(rs.getString("Email"));
                u.setPhoneNumber(rs.getString("PhoneNumber"));
                u.setAddress(rs.getString("Address"));
                u.setPassword(rs.getString("Password"));
                u.setRole(rs.getString("Role"));
                u.setRewardPoints(rs.getInt("RewardPoints"));
                u.setCustomerType(rs.getString("CustomerType"));
                list.add(u);
            }
        } catch (Exception e) {
            e.printStackTrace();
        }
        return list;
    }

    public void deleteCustomer(String id) {
        String sql = "DELETE FROM Users WHERE UserID = ? AND Role = 'Customer'";
        try {
            Connection connection = getConnection();
            PreparedStatement ps = connection.prepareStatement(sql);
            ps.setString(1, id);
            ps.executeUpdate();
        } catch (Exception e) {
            e.printStackTrace();
        }
    }

    public void addCustomer(String fullName, String email, String password, String phone, String address) {
        String sql = "INSERT INTO Users (FullName, Email, Password, PhoneNumber, Address, Role, RewardPoints, CustomerType) VALUES (?, ?, ?, ?, ?, 'Customer', 0, 'Regular')";
        try {
            Connection connection = getConnection();
            PreparedStatement ps = connection.prepareStatement(sql);
            ps.setString(1, fullName);
            ps.setString(2, email);
            ps.setString(3, password); // Note: Password should be hashed in a real application
            ps.setString(4, phone);
            ps.setString(5, address);
            ps.executeUpdate();
        } catch (Exception e) {
            e.printStackTrace();
        }
    }

    public User getCustomerByID(String id) {
        String sql = "SELECT * FROM Users WHERE UserID = ? AND Role = 'Customer'";
        try {
            Connection connection = getConnection();
            PreparedStatement ps = connection.prepareStatement(sql);
            ps.setString(1, id);
            ResultSet rs = ps.executeQuery();
            if (rs.next()) {
                User u = new User();
                u.setUserID(rs.getInt("UserID"));
                u.setFullName(rs.getString("FullName"));
                u.setEmail(rs.getString("Email"));
                u.setPhoneNumber(rs.getString("PhoneNumber"));
                u.setAddress(rs.getString("Address"));
                u.setPassword(rs.getString("Password"));
                u.setRole(rs.getString("Role"));
                u.setRewardPoints(rs.getInt("RewardPoints"));
                u.setCustomerType(rs.getString("CustomerType"));
                return u;
            }
        } catch (Exception e) {
            e.printStackTrace();
        }
        return null;
    }

    public void updateCustomer(User user) {
        try {
            StringBuilder sql = new StringBuilder("UPDATE Users SET FullName = ?, Email = ?, PhoneNumber = ?, Address = ?, RewardPoints = ?, CustomerType = ?");
            boolean passwordChanged = user.getPassword() != null && !user.getPassword().isEmpty();
            if (passwordChanged) {
                sql.append(", Password = ?");
            }
            sql.append(" WHERE UserID = ?");

            Connection connection = getConnection();
            PreparedStatement ps = connection.prepareStatement(sql.toString());
            
            ps.setString(1, user.getFullName());
            ps.setString(2, user.getEmail());
            ps.setString(3, user.getPhoneNumber());
            ps.setString(4, user.getAddress());
            ps.setInt(5, user.getRewardPoints());
            ps.setString(6, user.getCustomerType());
            
            if (passwordChanged) {
                ps.setString(7, user.getPassword());
                ps.setInt(8, user.getUserID());
            } else {
                ps.setInt(7, user.getUserID());
            }
            
            ps.executeUpdate();
        } catch (Exception e) {
            e.printStackTrace();
        }
    }
}
