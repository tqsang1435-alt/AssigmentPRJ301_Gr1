package vn.edu.phoneshop.dao;

import vn.edu.phoneshop.model.Supplier;
import vn.edu.phoneshop.utils.DBContext;

import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.util.ArrayList;
import java.util.List;

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

        } catch (Exception e) {
            e.printStackTrace();
        }
    }

    public void toggleStatus(int toggleId) {
        String sql = "UPDATE Suppliers SET status = CASE WHEN status = 1 THEN 0 ELSE 1 END WHERE SupplierID = ?";
        try (Connection conn = DBContext.getConnection();
             PreparedStatement ps = conn.prepareStatement(sql)) {

            ps.setInt(1, toggleId);
            ps.executeUpdate();

        } catch (Exception e) {
            e.printStackTrace();
        }
    }
}