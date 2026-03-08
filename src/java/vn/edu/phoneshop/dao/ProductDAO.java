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
    private Product mapResultSetToProduct(ResultSet rs) throws Exception {
        String ram = null, rom = null, color = null;
        try {
            ram = rs.getString("RAM");
        } catch (Exception e) {
        }
        try {
            rom = rs.getString("ROM");
        } catch (Exception e) {
        }
        try {
            color = rs.getString("Color");
        } catch (Exception e) {
        }

        return new Product(
                rs.getInt("ProductID"),
                rs.getString("ProductName"),
                rs.getDouble("Price"),
                rs.getInt("StockQuantity"),
                rs.getString("Description"),
                rs.getString("ImageURL"),
                rs.getInt("CategoryID"),
                rs.getInt("SupplierID"),
                rs.getBoolean("Status"),
                ram,
                rom,
                color);
    }

    public List<Product> getAllActiveProducts() {
        List<Product> list = new ArrayList<>();
        String sql = "SELECT * FROM Products WHERE Status = 1";

        try (Connection con = new DBContext().getConnection();
                PreparedStatement ps = con.prepareStatement(sql);
                ResultSet rs = ps.executeQuery()) {

            while (rs.next()) {
                Product p = new Product();
                p.setProductID(rs.getInt("ProductID"));
                p.setProductName(rs.getString("ProductName"));
                p.setPrice(rs.getDouble("Price"));
                p.setStockQuantity(rs.getInt("StockQuantity"));
                p.setDescription(rs.getString("Description"));
                p.setImageURL(rs.getString("ImageURL"));
                p.setCategoryID(rs.getInt("CategoryID"));
                p.setSupplierID(rs.getInt("SupplierID"));
                p.setStatus(rs.getBoolean("Status"));

                list.add(p);
            }

        } catch (Exception e) {
            e.printStackTrace();
        }
        return list;
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

    // 2. Hàm lấy sản phẩm theo Category
    public List<Product> getProductsByCategoryId(int cid) {
        List<Product> list = new ArrayList<>();
        String sql = "SELECT * FROM Products WHERE CategoryID = ? AND Status = 1";

        try (Connection conn = DBContext.getConnection();
                PreparedStatement ps = conn.prepareStatement(sql)) {

            ps.setInt(1, cid);
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

    // 3. Hàm lọc sản phẩm theo RAM và ROM
    public List<Product> filterProducts(String ram, String rom) {
        List<Product> list = new ArrayList<>();
        String sql = "SELECT * FROM Products WHERE Status = 1";

        if (ram != null && !ram.trim().isEmpty()) {
            sql += " AND RAM = ?";
        }
        if (rom != null && !rom.trim().isEmpty()) {
            sql += " AND ROM = ?";
        }

        try (Connection conn = DBContext.getConnection();
                PreparedStatement ps = conn.prepareStatement(sql)) {

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

    // 4. Hàm Lấy 1 Sản phẩm theo ID
    public Product getProductByID(int id) {
        String sql = "SELECT * FROM Products WHERE ProductID = ?";
        try (Connection conn = DBContext.getConnection();
                PreparedStatement ps = conn.prepareStatement(sql)) {

            ps.setInt(1, id);
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

    // 5. Hàm Thêm mới Sản phẩm
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

    // 6. Hàm Xóa mềm (Update Status = 0)
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

    // 7. Hàm Cập nhật Sản phẩm
    public void updateProduct(int id, String name, double price, int quantity, String desc, String img, int cateID,
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
            ps.setInt(11, id);
            ps.executeUpdate();
        } catch (Exception e) {
            e.printStackTrace();
        }
    }

    // Lấy TỔNG SỐ SẢN PHẨM (Có hỗ trợ lọc theo Category nếu khách đang chọn danh
    // mục)
    public int getTotalProducts(String cidRaw) {
        String sql = "SELECT COUNT(*) FROM Products WHERE Status = 1";
        if (cidRaw != null && !cidRaw.isEmpty()) {
            sql += " AND CategoryID = ?";
        }
        try (Connection conn = DBContext.getConnection();
                PreparedStatement ps = conn.prepareStatement(sql)) {

            if (cidRaw != null && !cidRaw.isEmpty()) {
                ps.setInt(1, Integer.parseInt(cidRaw));
            }
            try (ResultSet rs = ps.executeQuery()) {
                if (rs.next())
                    return rs.getInt(1);
            }
        } catch (Exception e) {
            e.printStackTrace();
        }
        return 0;
    }

    // Lấy SẢN PHẨM THEO TRANG
    public List<Product> getProductsWithPagination(String cidRaw, int page, int pageSize) {
        List<Product> list = new ArrayList<>();
        String sql = "SELECT * FROM Products WHERE Status = 1";

        if (cidRaw != null && !cidRaw.isEmpty()) {
            sql += " AND CategoryID = ?";
        }
        // Công thức bỏ qua dữ liệu cũ và lấy đúng pageSize của trang hiện tại
        sql += " ORDER BY ProductID OFFSET ? ROWS FETCH NEXT ? ROWS ONLY";

        try (Connection conn = DBContext.getConnection();
                PreparedStatement ps = conn.prepareStatement(sql)) {

            int paramIndex = 1;
            if (cidRaw != null && !cidRaw.isEmpty()) {
                ps.setInt(paramIndex++, Integer.parseInt(cidRaw));
            }
            ps.setInt(paramIndex++, (page - 1) * pageSize);
            ps.setInt(paramIndex, pageSize);

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
}