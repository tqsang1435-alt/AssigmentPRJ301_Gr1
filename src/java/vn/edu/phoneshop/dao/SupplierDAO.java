package vn.edu.phoneshop.dao;

import vn.edu.phoneshop.model.Supplier;
import vn.edu.phoneshop.utils.DBContext;

import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.ArrayList;
import java.util.List;

public class SupplierDAO {

    // Hàm lấy tất cả nhà cung cấp (CHỈ LẤY CÁI CÓ STATUS = 1)
    public List<Supplier> getAllSuppliers() {
        List<Supplier> list = new ArrayList<>();
        String sql = "SELECT * FROM Suppliers WHERE status = 1";

        try (Connection conn = DBContext.getConnection();
                PreparedStatement ps = conn.prepareStatement(sql);
                ResultSet rs = ps.executeQuery()) {

            while (rs.next()) {
                Supplier s = new Supplier(
                        rs.getInt("SupplierID"),
                        rs.getString("SupplierName"),
                        rs.getString("Phone"),
                        rs.getString("Email"),
                        rs.getString("Address"));
                list.add(s);
            }
        } catch (Exception e) {
            e.printStackTrace();
        }
        return list;
    }

    public void insertSupplier(String name, String phone, String email, String address) {
        // Mặc định khi thêm mới thì status tự động là 1 do cấu hình database hoặc logic
        String sql = "INSERT INTO Suppliers (SupplierName, Phone, Email, Address, status) VALUES (?, ?, ?, ?, 1)";

        try (Connection conn = DBContext.getConnection();
                PreparedStatement ps = conn.prepareStatement(sql)) {

            ps.setString(1, name);
            ps.setString(2, phone);
            ps.setString(3, email);
            ps.setString(4, address);
            ps.executeUpdate();
        } catch (Exception e) {
            e.printStackTrace();
        }
    }

    // Hàm lấy thông tin 1 nhà cung cấp theo ID (Dùng cho chức năng Sửa)
    public Supplier getSupplierByID(String id) {
        String sql = "SELECT * FROM Suppliers WHERE SupplierID = ?";

        try (Connection conn = DBContext.getConnection();
                PreparedStatement ps = conn.prepareStatement(sql)) {

            ps.setString(1, id);
            ResultSet rs = ps.executeQuery();
            if (rs.next()) {
                return new Supplier(
                        rs.getInt("SupplierID"),
                        rs.getString("SupplierName"),
                        rs.getString("Phone"),
                        rs.getString("Email"),
                        rs.getString("Address"));
            }
        } catch (Exception e) {
            e.printStackTrace();
        }
        return null;
    }

    private Supplier mapResultSetToSupplier(ResultSet rs) throws SQLException {
        return new Supplier(
                rs.getInt("SupplierID"),
                rs.getString("SupplierName"),
                rs.getString("Phone"),
                rs.getString("Email"),
                rs.getString("Address"));
    }

    public Supplier findById(int id) {
        String sql = "SELECT * FROM Suppliers WHERE SupplierID = ?";
        try (Connection conn = DBContext.getConnection();
                PreparedStatement ps = conn.prepareStatement(sql)) {
            ps.setInt(1, id);
            try (ResultSet rs = ps.executeQuery()) {
                if (rs.next()) {
                    return mapResultSetToSupplier(rs);
                }
            }
        } catch (Exception e) {
            e.printStackTrace();
        }
        return null;
    }

    // Hàm cập nhật thông tin nhà cung cấp
    public void updateSupplier(String id, String name, String phone, String email, String address) {
        String sql = "UPDATE Suppliers SET SupplierName = ?, Phone = ?, Email = ?, Address = ? WHERE SupplierID = ?";
        try (Connection conn = DBContext.getConnection();
                PreparedStatement ps = conn.prepareStatement(sql)) {

            ps.setString(1, name);
            ps.setString(2, phone);
            ps.setString(3, email);
            ps.setString(4, address);
            ps.setString(5, id);
            ps.executeUpdate();
        } catch (Exception e) {
            e.printStackTrace();
        }
    }

    public List<Supplier> getAll() {
        List<Supplier> list = new ArrayList<>();
        String sql = "SELECT * FROM Suppliers ORDER BY SupplierID DESC";
        try (Connection conn = DBContext.getConnection();
                PreparedStatement ps = conn.prepareStatement(sql);
                ResultSet rs = ps.executeQuery()) {
            while (rs.next()) {
                list.add(mapResultSetToSupplier(rs));
            }
        } catch (Exception e) {
            e.printStackTrace();
        }
        return list;
    }

    // Hàm xóa mềm nhà cung cấp
    public void deleteSupplier(String id) {
        // Thay vì xóa, ta update status về 0
        String sql = "UPDATE Suppliers SET status = 0 WHERE SupplierID = ?";
        try (Connection conn = DBContext.getConnection();
                PreparedStatement ps = conn.prepareStatement(sql)) {

            ps.setString(1, id);
            ps.executeUpdate();
        } catch (Exception e) {
            e.printStackTrace();
        }
    }

    public void updateStatus(int id, boolean status) {
        String sql = "UPDATE Suppliers SET status = ? WHERE SupplierID = ?";
        try (Connection conn = DBContext.getConnection();
                PreparedStatement ps = conn.prepareStatement(sql)) {
            ps.setBoolean(1, status);
            ps.setInt(2, id);
            ps.executeUpdate();
        } catch (Exception e) {
            e.printStackTrace();
        }
    }

    public void toggleStatus(int id) {
        String sql = "UPDATE Suppliers SET status = CASE WHEN status = 1 THEN 0 ELSE 1 END WHERE SupplierID = ?";
        try (Connection conn = DBContext.getConnection();
                PreparedStatement ps = conn.prepareStatement(sql)) {
            ps.setInt(1, id);
            ps.executeUpdate();
        } catch (Exception e) {
            e.printStackTrace();
        }
    }

    public void delete(int id) {
        String sql = "DELETE FROM Suppliers WHERE SupplierID = ?";
        try (Connection conn = DBContext.getConnection();
                PreparedStatement ps = conn.prepareStatement(sql)) {
            ps.setInt(1, id);
            ps.executeUpdate();
        } catch (Exception e) {
            e.printStackTrace();
        }
    }

    public List<Supplier> searchByName(String keyword) {
        List<Supplier> list = new ArrayList<>();
        String sql = "SELECT * FROM Suppliers WHERE SupplierName LIKE ?";
        try (Connection conn = DBContext.getConnection();
                PreparedStatement ps = conn.prepareStatement(sql)) {
            ps.setString(1, "%" + keyword + "%");
            try (ResultSet rs = ps.executeQuery()) {
                while (rs.next()) {
                    list.add(mapResultSetToSupplier(rs));
                }
            }
        } catch (Exception e) {
            e.printStackTrace();
        }
        return list;
    }
}
