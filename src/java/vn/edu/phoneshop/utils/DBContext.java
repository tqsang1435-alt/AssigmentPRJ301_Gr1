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
    }
}