package vn.edu.phoneshop.dao;

import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.util.ArrayList;
import java.util.List;

import vn.edu.phoneshop.model.Product;
import vn.edu.phoneshop.utils.DBContext;

public class ProductDAO {

    // Hàm phụ trợ: Map dữ liệu từ ResultSet sang Object Product
    // Giúp code gọn hơn, không phải lặp lại đoạn lấy dữ liệu nhiều lần
    private Product mapResultSetToProduct(ResultSet rs) throws Exception {
        return new Product(
                rs.getInt("ProductID"),
                rs.getString("ProductName"),
                rs.getDouble("Price"),
                rs.getInt("StockQuantity"),
                rs.getString("Description"),
                rs.getString("ImageURL"),
                rs.getInt("CategoryID"),
                rs.getInt("SupplierID"),
                rs.getBoolean("Status"), // Cột Status trong SQL là bit -> getBoolean
                rs.getString("RAM"),
                rs.getString("ROM"),
                rs.getString("Color"));
    }

    // 1. Hàm lấy tất cả sản phẩm đang kinh doanh (Status = 1)
    public List<Product> getAllProducts() {
        List<Product> list = new ArrayList<>();
        String sql = "SELECT * FROM Products WHERE Status = 1";

        try (Connection conn = DBContext.getConnection();
                PreparedStatement ps = conn.prepareStatement(sql);
                ResultSet rs = ps.executeQuery()) {

            while (rs.next()) {
                list.add(mapResultSetToProduct(rs));
            }
        } catch (Exception e) {
            e.printStackTrace();
        }
        return list;
    }

    // 2. HÀM LỌC SẢN PHẨM THEO RAM VÀ ROM (Chức năng quan trọng)
    public List<Product> filterProducts(String ram, String rom) {
        List<Product> list = new ArrayList<>();

        // Khởi tạo câu lệnh SQL gốc
        String sql = "SELECT * FROM Products WHERE Status = 1";

        // Nếu người dùng có chọn RAM thì nối thêm điều kiện lọc RAM
        if (ram != null && !ram.trim().isEmpty()) {
            sql += " AND RAM = ?";
        }
        // Nếu người dùng có chọn ROM thì nối thêm điều kiện lọc ROM
        if (rom != null && !rom.trim().isEmpty()) {
            sql += " AND ROM = ?";
        }

        try (Connection conn = DBContext.getConnection();
                PreparedStatement ps = conn.prepareStatement(sql)) {

            // Truyền giá trị vào các dấu '?' tương ứng
            int paramIndex = 1;
            if (ram != null && !ram.trim().isEmpty()) {
                ps.setString(paramIndex++, ram);
            }
            if (rom != null && !rom.trim().isEmpty()) {
                ps.setString(paramIndex++, rom);
            }

            try (ResultSet rs = ps.executeQuery()) {
                while (rs.next()) {
                    list.add(mapResultSetToProduct(rs));
                }
            }
        } catch (Exception e) {
            e.printStackTrace();
        }
        return list;
    }

    // 3. Hàm Thêm mới Sản phẩm
    public void insertProduct(String name, double price, int quantity, String desc, String img, int cateID, int suppID,
            String ram, String rom, String color) {
        String sql = "INSERT INTO Products (ProductName, Price, StockQuantity, Description, ImageURL, CategoryID, SupplierID, Status, RAM, ROM, Color) "
                + "VALUES (?, ?, ?, ?, ?, ?, ?, 1, ?, ?, ?)";
        try (Connection conn = DBContext.getConnection();
                PreparedStatement ps = conn.prepareStatement(sql)) {

            ps.setString(1, name);
            ps.setDouble(2, price);
            ps.setInt(3, quantity);
            ps.setString(4, desc);
            ps.setString(5, img);
            ps.setInt(6, cateID);
            ps.setInt(7, suppID);
            ps.setString(8, ram);
            ps.setString(9, rom);
            ps.setString(10, color);
            ps.executeUpdate();
        } catch (Exception e) {
            e.printStackTrace();
        }
    }

    // 4. Hàm Lấy 1 Sản phẩm theo ID (Dùng để Sửa)
    public Product getProductByID(String id) {
        String sql = "SELECT * FROM Products WHERE ProductID = ?";
        try (Connection conn = DBContext.getConnection();
                PreparedStatement ps = conn.prepareStatement(sql)) {

            ps.setString(1, id);
            try (ResultSet rs = ps.executeQuery()) {
                if (rs.next()) {
                    return mapResultSetToProduct(rs);
                }
            }
        } catch (Exception e) {
            e.printStackTrace();
        }
        return null;
    }

    // 5. Hàm Xóa mềm (Update Status = 0)
    public void deleteProduct(int id) {
        String sql = "UPDATE Products SET Status = 0 WHERE ProductID = ?";
        try (Connection conn = DBContext.getConnection();
                PreparedStatement ps = conn.prepareStatement(sql)) {

            ps.setInt(1, id);
            ps.executeUpdate();
        } catch (Exception e) {
            e.printStackTrace();
        }
    }

    // 6. Hàm Cập nhật Sản phẩm
    public void updateProduct(String id, String name, double price, int quantity, String desc, String img, int cateID,
            int suppID, String ram, String rom, String color) {
        String sql = "UPDATE Products SET ProductName = ?, Price = ?, StockQuantity = ?, Description = ?, ImageURL = ?, CategoryID = ?, SupplierID = ?, RAM = ?, ROM = ?, Color = ? WHERE ProductID = ?";
        try (Connection conn = DBContext.getConnection();
                PreparedStatement ps = conn.prepareStatement(sql)) {

            ps.setString(1, name);
            ps.setDouble(2, price);
            ps.setInt(3, quantity);
            ps.setString(4, desc);
            ps.setString(5, img);
            ps.setInt(6, cateID);
            ps.setInt(7, suppID);
            ps.setString(8, ram);
            ps.setString(9, rom);
            ps.setString(10, color);
            ps.setString(11, id); // Đẩy ID vào vị trí dấu ? cuối cùng
            ps.executeUpdate();
        } catch (Exception e) {
            e.printStackTrace();
        }
    }
}
