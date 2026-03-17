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

    public static Connection getConnection() {
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
    }
}
