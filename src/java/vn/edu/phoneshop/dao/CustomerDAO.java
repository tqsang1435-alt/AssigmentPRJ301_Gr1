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
}
