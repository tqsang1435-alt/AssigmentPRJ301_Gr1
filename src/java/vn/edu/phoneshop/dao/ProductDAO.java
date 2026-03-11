package vn.edu.phoneshop.dao;

import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.ArrayList;
import java.util.List;
import vn.edu.phoneshop.model.Product;
import vn.edu.phoneshop.utils.DBContext;
import vn.edu.phoneshop.model.Category;

public class ProductDAO extends DBContext {

    private Product mapRowToProduct(ResultSet rs) throws SQLException {
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
        p.setRam(rs.getString("RAM"));
        p.setRom(rs.getString("ROM"));
        p.setColor(rs.getString("Color"));

        Category c = new Category();
        c.setCategoryID(rs.getInt("CategoryID"));
        c.setCategoryName(rs.getString("CategoryName"));
        p.setCategory(c);

        return p;
    }

    public List<Product> getAllActiveProducts() {
        List<Product> list = new ArrayList<>();
        String sql = "SELECT p.*, c.CategoryName FROM Products p JOIN Categories c ON p.CategoryID = c.CategoryID WHERE p.Status = 1";
        try (Connection conn = getConnection();
                PreparedStatement ps = conn.prepareStatement(sql);
                ResultSet rs = ps.executeQuery()) {
            while (rs.next()) {
                list.add(mapRowToProduct(rs));
            }
        } catch (Exception e) {
            e.printStackTrace();
        }
        return list;
    }

    public List<Product> getAllProducts() {
        List<Product> list = new ArrayList<>();
        String sql = "SELECT p.*, c.CategoryName FROM Products p JOIN Categories c ON p.CategoryID = c.CategoryID ORDER BY p.ProductID DESC";
        try (Connection conn = getConnection();
                PreparedStatement ps = conn.prepareStatement(sql);
                ResultSet rs = ps.executeQuery()) {
            while (rs.next()) {
                list.add(mapRowToProduct(rs));
            }
        } catch (Exception e) {
            e.printStackTrace();
        }
        return list;
    }

    public List<Product> getProductsByCategoryId(int cid) {
        List<Product> list = new ArrayList<>();
        String sql = "SELECT p.*, c.CategoryName FROM Products p JOIN Categories c ON p.CategoryID = c.CategoryID WHERE p.CategoryID = ? AND p.Status = 1";
        try (Connection conn = getConnection(); PreparedStatement ps = conn.prepareStatement(sql)) {
            ps.setInt(1, cid);
            try (ResultSet rs = ps.executeQuery()) {
                while (rs.next()) {
                    list.add(mapRowToProduct(rs));
                }
            }
        } catch (Exception e) {
            e.printStackTrace();
        }
        return list;
    }

    public List<Product> filterProducts(String ram, String rom) {
        List<Product> list = new ArrayList<>();
        StringBuilder sql = new StringBuilder(
                "SELECT p.*, c.CategoryName FROM Products p JOIN Categories c ON p.CategoryID = c.CategoryID WHERE 1=1");

        if (ram != null && !ram.isEmpty()) {
            sql.append(" AND p.RAM = ?");
        }
        if (rom != null && !rom.isEmpty()) {
            sql.append(" AND p.ROM = ?");
        }

        try (Connection conn = getConnection(); PreparedStatement ps = conn.prepareStatement(sql.toString())) {
            int paramIndex = 1;
            if (ram != null && !ram.isEmpty()) {
                ps.setString(paramIndex++, ram);
            }
            if (rom != null && !rom.isEmpty()) {
                ps.setString(paramIndex++, rom);
            }

            try (ResultSet rs = ps.executeQuery()) {
                while (rs.next()) {
                    list.add(mapRowToProduct(rs));
                }
            }
        } catch (Exception e) {
            e.printStackTrace();
        }
        return list;
    }

    public Product getProductByID(int id) {
        String sql = "SELECT p.*, c.CategoryName FROM Products p JOIN Categories c ON p.CategoryID = c.CategoryID WHERE p.ProductID = ?";
        try (Connection conn = getConnection(); PreparedStatement ps = conn.prepareStatement(sql)) {
            ps.setInt(1, id);
            try (ResultSet rs = ps.executeQuery()) {
                if (rs.next()) {
                    return mapRowToProduct(rs);
                }
            }
        } catch (Exception e) {
            e.printStackTrace();
        }
        return null;
    }

    public void insertProduct(String name, double price, int quantity, String desc, String img, int cateId,
            int suppId, String ram, String rom, String color) {
        String sql = "INSERT INTO Products (ProductName, Price, StockQuantity, Description, ImageURL, CategoryID, SupplierID, RAM, ROM, Color) VALUES (?, ?, ?, ?, ?, ?, ?, ?, ?, ?)";
        try (Connection conn = getConnection(); PreparedStatement ps = conn.prepareStatement(sql)) {
            ps.setString(1, name);
            ps.setDouble(2, price);
            ps.setInt(3, quantity);
            ps.setString(4, desc);
            ps.setString(5, img);
            ps.setInt(6, cateId);
            ps.setInt(7, suppId);
            ps.setString(8, ram);
            ps.setString(9, rom);
            ps.setString(10, color);
            ps.executeUpdate();
        } catch (Exception e) {
            e.printStackTrace();
        }
    }

    public void deleteProduct(int pid) {
        String sql = "UPDATE Products SET Status = 0 WHERE ProductID = ?";
        try (Connection conn = getConnection(); PreparedStatement ps = conn.prepareStatement(sql)) {
            ps.setInt(1, pid);
            ps.executeUpdate();
        } catch (Exception e) {
            e.printStackTrace();
        }
    }

    public void updateProduct(int id, String name, double price, int quantity, String desc, String img, int cateId,
            int suppId, String ram, String rom, String color) {
        String sql = "UPDATE Products SET ProductName = ?, Price = ?, StockQuantity = ?, Description = ?, ImageURL = ?, CategoryID = ?, SupplierID = ?, RAM = ?, ROM = ?, Color = ? WHERE ProductID = ?";
        try (Connection conn = getConnection(); PreparedStatement ps = conn.prepareStatement(sql)) {
            ps.setString(1, name);
            ps.setDouble(2, price);
            ps.setInt(3, quantity);
            ps.setString(4, desc);
            ps.setString(5, img);
            ps.setInt(6, cateId);
            ps.setInt(7, suppId);
            ps.setString(8, ram);
            ps.setString(9, rom);
            ps.setString(10, color);
            ps.setInt(11, id);
            ps.executeUpdate();
        } catch (Exception e) {
            e.printStackTrace();
        }
    }

    public int getTotalProducts(String txtSearch) {
        String sql = "SELECT count(*) FROM Products WHERE ProductName LIKE ?";
        try (Connection conn = getConnection();
                PreparedStatement ps = conn.prepareStatement(sql)) {

            String search = (txtSearch == null) ? "" : txtSearch;
            ps.setString(1, "%" + search + "%");
            ResultSet rs = ps.executeQuery();
            if (rs.next()) {
                return rs.getInt(1);
            }
        } catch (Exception e) {
            e.printStackTrace();
        }
        return 0;
    }

    /**
     * Lấy danh sách sản phẩm có phân trang và tìm kiếm.
     * 
     * @param txtSearch Từ khóa tìm kiếm (tên sản phẩm).
     * @param page      Trang hiện tại (bắt đầu từ 1).
     * @param pageSize  Số lượng sản phẩm trên mỗi trang.
     * @return Danh sách sản phẩm.
     */
    public List<Product> getProductsWithPagination(String txtSearch, int page, int pageSize) {
        List<Product> list = new ArrayList<>();
        String sql = "SELECT p.*, c.CategoryName FROM Products p JOIN Categories c ON p.CategoryID = c.CategoryID WHERE p.ProductName LIKE ? ORDER BY p.ProductID OFFSET ? ROWS FETCH NEXT ? ROWS ONLY";
        try (Connection conn = getConnection();
                PreparedStatement ps = conn.prepareStatement(sql)) {

            String search = (txtSearch == null) ? "" : txtSearch;
            ps.setString(1, "%" + search + "%");
            ps.setInt(2, (page - 1) * pageSize); // Tính toán vị trí bắt đầu (OFFSET)
            ps.setInt(3, pageSize); // Số lượng dòng cần lấy (FETCH NEXT)

            ResultSet rs = ps.executeQuery();
            while (rs.next()) {
                list.add(mapRowToProduct(rs));
            }
        } catch (Exception e) {
            e.printStackTrace();
        }
        return list;
    }

    // Lấy danh sách các loại RAM (không trùng lặp) từ database
    public List<String> getAllRAMs() {
        List<String> list = new ArrayList<>();
        String sql = "SELECT DISTINCT RAM FROM Products WHERE RAM IS NOT NULL AND RAM <> '' ORDER BY RAM";
        try (Connection conn = getConnection();
                PreparedStatement ps = conn.prepareStatement(sql);
                ResultSet rs = ps.executeQuery()) {
            while (rs.next()) {
                list.add(rs.getString("RAM"));
            }
        } catch (Exception e) {
            e.printStackTrace();
        }
        return list;
    }

    // Lấy danh sách các loại ROM (không trùng lặp) từ database
    public List<String> getAllROMs() {
        List<String> list = new ArrayList<>();
        String sql = "SELECT DISTINCT ROM FROM Products WHERE ROM IS NOT NULL AND ROM <> '' ORDER BY ROM";
        try (Connection conn = getConnection();
                PreparedStatement ps = conn.prepareStatement(sql);
                ResultSet rs = ps.executeQuery()) {
            while (rs.next()) {
                list.add(rs.getString("ROM"));
            }
        } catch (Exception e) {
            e.printStackTrace();
        }
        return list;
    }

    // Tìm kiếm kết hợp lọc RAM, ROM
    public List<Product> searchAndFilterProducts(String search, String ram, String rom) {
        List<Product> list = new ArrayList<>();
        StringBuilder sql = new StringBuilder(
                "SELECT p.*, c.CategoryName FROM Products p JOIN Categories c ON p.CategoryID = c.CategoryID WHERE 1=1");

        if (search != null && !search.trim().isEmpty()) {
            sql.append(" AND p.ProductName LIKE ?");
        }
        if (ram != null && !ram.trim().isEmpty()) {
            sql.append(" AND p.RAM = ?");
        }
        if (rom != null && !rom.trim().isEmpty()) {
            sql.append(" AND p.ROM = ?");
        }
        sql.append(" ORDER BY p.ProductID DESC");

        try (Connection conn = getConnection();
                PreparedStatement ps = conn.prepareStatement(sql.toString())) {
            int index = 1;
            if (search != null && !search.trim().isEmpty()) {
                ps.setString(index++, "%" + search.trim() + "%");
            }
            if (ram != null && !ram.trim().isEmpty()) {
                ps.setString(index++, ram);
            }
            if (rom != null && !rom.trim().isEmpty()) {
                ps.setString(index++, rom);
            }
            ResultSet rs = ps.executeQuery();
            while (rs.next()) {
                list.add(mapRowToProduct(rs));
            }
        } catch (Exception e) {
            e.printStackTrace();
        }
        return list;
    }
}