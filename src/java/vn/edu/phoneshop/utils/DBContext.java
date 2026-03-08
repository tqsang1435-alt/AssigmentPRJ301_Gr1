<<<<<<< HEAD
package vn.edu.phoneshop.utils;

import java.sql.Connection;
import java.sql.DriverManager;

public class DBContext {

    private static final String URL =
            "jdbc:sqlserver://localhost:1433;"
            + "databaseName=PhoneShopDB;"
            + "encrypt=false;"
            + "trustServerCertificate=true;";

    private static final String USER = "sa";
    private static final String PASSWORD = "123";

    public static Connection getConnection() throws Exception {
        return DriverManager.getConnection(URL, USER, PASSWORD);
=======
/*
 * Click nbfs://nbhost/SystemFileSystem/Templates/Licenses/license-default.txt to change this license
 * Click nbfs://nbhost/SystemFileSystem/Templates/Classes/Class.java to edit this template
 */
package vn.edu.phoneshop.utils;
import java.sql.Connection;
import java.sql.DriverManager;
import java.sql.SQLException;
/**
 *
 * @author tqsan
 */
public class DBContext {
    private static final String USER = "sa";
    private static final String PASS = "123";
    private static final String SERVER = "localhost";
    private static final String PORT = "1433";
    private static final String DB_NAME = "PhoneShopDB";

    public Connection getConnection() {
        try {
            Class.forName("com.microsoft.sqlserver.jdbc.SQLServerDriver");
            String connectionUrl = "jdbc:sqlserver://" + SERVER + ":" + PORT + ";databaseName=" + DB_NAME + ";encrypt=false;trustServerCertificate=true;";
            return DriverManager.getConnection(connectionUrl, USER, PASS);
        } catch (ClassNotFoundException | SQLException e) {
            e.printStackTrace();
            System.out.println("Lỗi kết nối CSDL: " + e.getMessage());
        }
        return null;
    }
public static void main(String[] args) {
        System.out.println("Đang kiểm tra kết nối...");
        if (new DBContext().getConnection() != null) {
            System.out.println("Kết nối thành công!");
        } else {
            System.out.println("Kết nối thất bại! Kiểm tra lại User/Pass/Tên DB.");
        }
>>>>>>> e7b45a906d28bff2867a45721aa3c05ab0f4cc4d
    }
}