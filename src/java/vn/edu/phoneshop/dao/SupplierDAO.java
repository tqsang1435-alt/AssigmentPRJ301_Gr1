/*
 * Click nbfs://nbhost/SystemFileSystem/Templates/Licenses/license-default.txt to change this license
 * Click nbfs://nbhost/SystemFileSystem/Templates/Classes/Class.java to edit this template
 */
package vn.edu.phoneshop.dao;

import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.util.ArrayList;
import java.util.List;
import vn.edu.phoneshop.model.Supplier;
import vn.edu.phoneshop.utils.DBContext;

/**
 *
 * @author tqsan
 */
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
        } catch (Exception e) {
            e.printStackTrace();
        }
    }

    // Hàm xóa mềm nhà cung cấp (SỬA LẠI TÊN VÀ SQL)
    public void deleteSupplier(String id) {
        // Thay vì xóa, ta update status về 0
        String query = "UPDATE Suppliers SET status = 0 WHERE SupplierID = ?";
        try {
            conn = new DBContext().getConnection();
            ps = conn.prepareStatement(query);
            ps.setString(1, id);
            ps.executeUpdate();
        } catch (Exception e) {
            e.printStackTrace();
        }
    }

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