<<<<<<< HEAD
package vn.edu.phoneshop.dao;

import vn.edu.phoneshop.model.Supplier;
import vn.edu.phoneshop.utils.DBContext;

=======
/*
 * Click nbfs://nbhost/SystemFileSystem/Templates/Licenses/license-default.txt to change this license
 * Click nbfs://nbhost/SystemFileSystem/Templates/Classes/Class.java to edit this template
 */
package vn.edu.phoneshop.dao;

>>>>>>> e7b45a906d28bff2867a45721aa3c05ab0f4cc4d
import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.util.ArrayList;
import java.util.List;
<<<<<<< HEAD

public class SupplierDAO {

    // ===============================
    // GET ALL SUPPLIERS
    // ===============================
    public List<Supplier> getAll() {
        List<Supplier> list = new ArrayList<>();
        String sql = "SELECT * FROM Suppliers";

        try (Connection conn = DBContext.getConnection();
             PreparedStatement ps = conn.prepareStatement(sql);
             ResultSet rs = ps.executeQuery()) {

            while (rs.next()) {
                Supplier s = new Supplier(
                        rs.getInt("SupplierID"),
                        rs.getString("SupplierName"),
                        rs.getString("ContactName"),
                        rs.getString("Phone"),
                        rs.getString("Email"),
                        rs.getString("Address"),
                        rs.getString("Logo"),
                        rs.getBoolean("status")
                );
                list.add(s);
            }

        } catch (Exception e) {
            e.printStackTrace();
        }

        return list;
    }

    // ===============================
    // INSERT SUPPLIER
    // ===============================
    public void insert(Supplier s) throws Exception {

        String sql = "INSERT INTO Suppliers "
                + "(SupplierName, ContactName, Phone, Email, Address, Logo, status) "
                + "VALUES (?, ?, ?, ?, ?, ?, ?)";

        try (Connection conn = DBContext.getConnection();
             PreparedStatement ps = conn.prepareStatement(sql)) {

            ps.setString(1, s.getSupplierName());
            ps.setString(2, s.getContactName());
            ps.setString(3, s.getPhone());
            ps.setString(4, s.getEmail());
            ps.setString(5, s.getAddress());
            ps.setString(6, s.getLogo());
            ps.setBoolean(7, s.isStatus());

            ps.executeUpdate();
        }
    }
    // UPDATE SUPPLIER
public void update(Supplier s) throws Exception {

    String sql = "UPDATE Suppliers SET "
            + "SupplierName = ?, "
            + "ContactName = ?, "
            + "Phone = ?, "
            + "Email = ?, "
            + "Address = ?, "
            + "Logo = ? "
            + "WHERE SupplierID = ?";

    try (Connection conn = DBContext.getConnection();
         PreparedStatement ps = conn.prepareStatement(sql)) {

        ps.setString(1, s.getSupplierName());
        ps.setString(2, s.getContactName());
        ps.setString(3, s.getPhone());
        ps.setString(4, s.getEmail());
        ps.setString(5, s.getAddress());
        ps.setString(6, s.getLogo());
        ps.setInt(7, s.getSupplierID());

        ps.executeUpdate();
    }
}
// SOFT DELETE (Disable)
public void updateStatus(int id, boolean status) throws Exception {

    String sql = "UPDATE Suppliers SET status = ? WHERE SupplierID = ?";

    try (Connection conn = DBContext.getConnection();
         PreparedStatement ps = conn.prepareStatement(sql)) {

        ps.setBoolean(1, status);
        ps.setInt(2, id);
        ps.executeUpdate();
    }
}
// SEARCH BY NAME
public List<Supplier> searchByName(String keyword) {

    List<Supplier> list = new ArrayList<>();
    String sql = "SELECT * FROM Suppliers WHERE SupplierName LIKE ?";

    try (Connection conn = DBContext.getConnection();
         PreparedStatement ps = conn.prepareStatement(sql)) {

        ps.setString(1, "%" + keyword + "%");
        ResultSet rs = ps.executeQuery();

        while (rs.next()) {
            Supplier s = new Supplier(
                    rs.getInt("SupplierID"),
                    rs.getString("SupplierName"),
                    rs.getString("ContactName"),
                    rs.getString("Phone"),
                    rs.getString("Email"),
                    rs.getString("Address"),
                    rs.getString("Logo"),
                    rs.getBoolean("status")
            );
            list.add(s);
        }

    } catch (Exception e) {
        e.printStackTrace();
    }

    return list;
}

    public Supplier getById(int id) {

    String sql = "SELECT * FROM Suppliers WHERE SupplierID = ?";

    try (Connection conn = DBContext.getConnection();
         PreparedStatement ps = conn.prepareStatement(sql)) {

        ps.setInt(1, id);
        ResultSet rs = ps.executeQuery();

        if (rs.next()) {
            return new Supplier(
                    rs.getInt("SupplierID"),
                    rs.getString("SupplierName"),
                    rs.getString("ContactName"),
                    rs.getString("Phone"),
                    rs.getString("Email"),
                    rs.getString("Address"),
                    rs.getString("Logo"),
                    rs.getBoolean("status")
            );
        }

    } catch (Exception e) {
        e.printStackTrace();
    }

    return null;
}

    public void delete(int deleteId) {
        String sql = "DELETE FROM Suppliers WHERE SupplierID = ?";
        try (Connection conn = DBContext.getConnection();
             PreparedStatement ps = conn.prepareStatement(sql)) {

            ps.setInt(1, deleteId);
            ps.executeUpdate();

=======
import vn.edu.phoneshop.model.Supplier;
import vn.edu.phoneshop.utils.DBContext;

public class SupplierDAO {

    Connection conn = null;
    PreparedStatement ps = null;
    ResultSet rs = null;

    // Hàm lấy tất cả nhà cung cấp (CHỈ LẤY CÁI CÓ STATUS = 1)
    public List<Supplier> getAllSuppliers() {
        List<Supplier> list = new ArrayList<>();
        // SỬA: Thêm điều kiện WHERE status = 1
        String query = "SELECT * FROM Suppliers WHERE status = 1";
        try {
            conn = new DBContext().getConnection();
            ps = conn.prepareStatement(query);
            rs = ps.executeQuery();
            while (rs.next()) {
                list.add(new Supplier(
                        rs.getInt("SupplierID"),
                        rs.getString("SupplierName"),
                        rs.getString("Phone"),
                        rs.getString("Email"),
                        rs.getString("Address")
                ));
            }
        } catch (Exception e) {
            e.printStackTrace();
        }
        return list;
    }

    public void insertSupplier(String name, String phone, String email, String address) {
        // Mặc định khi thêm mới thì status tự động là 1 do cấu hình database
        String query = "INSERT INTO Suppliers (SupplierName, Phone, Email, Address) VALUES (?, ?, ?, ?)";
        try {
            conn = new DBContext().getConnection();
            ps = conn.prepareStatement(query);
            ps.setString(1, name);
            ps.setString(2, phone);
            ps.setString(3, email);
            ps.setString(4, address);
            ps.executeUpdate();
>>>>>>> e7b45a906d28bff2867a45721aa3c05ab0f4cc4d
        } catch (Exception e) {
            e.printStackTrace();
        }
    }

<<<<<<< HEAD
    public void toggleStatus(int toggleId) {
        String sql = "UPDATE Suppliers SET status = CASE WHEN status = 1 THEN 0 ELSE 1 END WHERE SupplierID = ?";
        try (Connection conn = DBContext.getConnection();
             PreparedStatement ps = conn.prepareStatement(sql)) {

            ps.setInt(1, toggleId);
            ps.executeUpdate();

=======
    // Hàm xóa mềm nhà cung cấp (SỬA LẠI TÊN VÀ SQL)
    public void deleteSupplier(String id) {
        // Thay vì xóa, ta update status về 0
        String query = "UPDATE Suppliers SET status = 0 WHERE SupplierID = ?";
        try {
            conn = new DBContext().getConnection();
            ps = conn.prepareStatement(query);
            ps.setString(1, id);
            ps.executeUpdate();
>>>>>>> e7b45a906d28bff2867a45721aa3c05ab0f4cc4d
        } catch (Exception e) {
            e.printStackTrace();
        }
    }
<<<<<<< HEAD
}
=======

    // Hàm lấy thông tin 1 nhà cung cấp theo ID (Dùng cho chức năng Sửa)
    public Supplier getSupplierByID(String id) {
        String query = "SELECT * FROM Suppliers WHERE SupplierID = ?";
        try {
            conn = new DBContext().getConnection();
            ps = conn.prepareStatement(query);
            ps.setString(1, id);
            rs = ps.executeQuery();
            while (rs.next()) {
                return new Supplier(
                        rs.getInt("SupplierID"),
                        rs.getString("SupplierName"),
                        rs.getString("Phone"),
                        rs.getString("Email"),
                        rs.getString("Address")
                );
            }
        } catch (Exception e) {
            e.printStackTrace();
        }
        return null;
    }

    // Hàm cập nhật thông tin nhà cung cấp
    public void updateSupplier(String id, String name, String phone, String email, String address) {
        String query = "UPDATE Suppliers SET SupplierName = ?, Phone = ?, Email = ?, Address = ? WHERE SupplierID = ?";
        try {
            conn = new DBContext().getConnection();
            ps = conn.prepareStatement(query);
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

    public static void main(String[] args) {
        SupplierDAO dao = new SupplierDAO();
        List<Supplier> list = dao.getAllSuppliers();
        for (Supplier o : list) {
            System.out.println(o); // Đảm bảo class Supplier có hàm toString() để in ra đẹp hơn
        }
    }
}
>>>>>>> e7b45a906d28bff2867a45721aa3c05ab0f4cc4d
