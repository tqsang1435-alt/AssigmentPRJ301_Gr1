/*
 * Click nbfs://nbhost/SystemFileSystem/Templates/Licenses/license-default.txt to change this license
 * Click nbfs://nbhost/SystemFileSystem/Templates/Classes/Class.java to edit this template
 */
package vn.edu.phoneshop.dao;

/**
 *
 * @author Lenovo
 */

import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import vn.edu.phoneshop.model.User;
import vn.edu.phoneshop.utils.DBContext;

public class LoginDAO {

    public User checkLogin(String email, String password) {
        String sql = "SELECT * FROM Users WHERE Email=? AND Password=?";

        try {
            DBContext db = new DBContext();
            Connection con = db.getConnection();

            PreparedStatement ps = con.prepareStatement(sql);
            ps.setString(1, email);
            ps.setString(2, password);

            ResultSet rs = ps.executeQuery();

            if (rs.next()) {
                // return new User(
                // rs.getInt("UserID"),
                // rs.getString("FullName"),
                // rs.getString("Email"),
                // rs.getString("PhoneNumber"),
                // rs.getString("Address"),
                // rs.getString("Password"),
                // rs.getString("Role"),
                // rs.getDate("CreateDate")
                // );
            }

        } catch (Exception e) {
            e.printStackTrace();
        }
        return null;
    }
}
