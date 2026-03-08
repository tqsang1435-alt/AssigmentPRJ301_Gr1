package vn.edu.phoneshop.dao;

import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.util.ArrayList;
import java.util.List;

import vn.edu.phoneshop.model.Product;
import vn.edu.phoneshop.utils.DBContext;

public class ProductDAO {

    // ==============================
    // LẤY TẤT CẢ SẢN PHẨM
    // ==============================
    public List<Product> getAllProducts() {
        List<Product> list = new ArrayList<>();
        String sql = "SELECT ProductID, ProductName, Price, StockQuantity, Description, ImageURL FROM Products";

        try (Connection con = DBContext.getConnection();
             PreparedStatement ps = con.prepareStatement(sql);
             ResultSet rs = ps.executeQuery()) {
   System.out.println("DB = " + con.getCatalog());
            while (rs.next()) {

                Product p = new Product();
                p.setProductId(rs.getInt("ProductID"));
                p.setProductName(rs.getString("ProductName"));
                p.setPrice(rs.getDouble("Price"));
                p.setQuantity(rs.getInt("StockQuantity"));
                p.setDescription(rs.getString("Description"));
                p.setImage(rs.getString("ImageURL"));

                list.add(p);
            }

            System.out.println("Total products: " + list.size()); // debug

        } catch (Exception e) {
            e.printStackTrace();
        }

        return list;
    }

    // ==============================
    // THÊM SẢN PHẨM
    // ==============================
    public void insertProduct(Product p) {
        String sql = "INSERT INTO Products(ProductName, Price, StockQuantity, Description, ImageURL) VALUES (?, ?, ?, ?, ?)";

        try (Connection con = DBContext.getConnection();
             PreparedStatement ps = con.prepareStatement(sql)) {

            ps.setString(1, p.getProductName());
            ps.setDouble(2, p.getPrice());
            ps.setInt(3, p.getQuantity());
            ps.setString(4, p.getDescription());
            ps.setString(5, p.getImage());

            ps.executeUpdate();

        } catch (Exception e) {
            e.printStackTrace();
        }
    }

    // ==============================
    // XOÁ SẢN PHẨM
    // ==============================
    public void deleteProduct(int id) {
        String sql = "DELETE FROM Products WHERE ProductID = ?";

        try (Connection con = DBContext.getConnection();
             PreparedStatement ps = con.prepareStatement(sql)) {

            ps.setInt(1, id);
            ps.executeUpdate();

        } catch (Exception e) {
            e.printStackTrace();
        }
    }
}