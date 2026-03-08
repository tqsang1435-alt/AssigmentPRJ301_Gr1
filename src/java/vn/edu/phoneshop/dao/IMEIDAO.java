package vn.edu.phoneshop.dao;

import vn.edu.phoneshop.model.IMEI;

import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import vn.edu.phoneshop.utils.DBContext;

public class IMEIDAO {

    // 🔹 Tìm IMEI theo Serial
    public IMEI findBySerial(String serial) {
        String sql = "SELECT * FROM ProductIMEI WHERE IMEI_Number = ?";

        try (Connection conn = DBContext.getConnection();
             PreparedStatement ps = conn.prepareStatement(sql)) {

            ps.setString(1, serial);
            ResultSet rs = ps.executeQuery();

            if (rs.next()) {

                IMEI imei = new IMEI();
                // map columns according to SQL script
                imei.setImeiId(rs.getInt("IMEI_ID"));
                imei.setProductId(rs.getInt("ProductID"));
                imei.setSerialNumber(rs.getString("IMEI_Number"));
                // status stored as INT in DB: 0=stock,1=sold,2=return
                int st = rs.getInt("Status");
                imei.setStatus(String.valueOf(st));
                try {
                    int oid = rs.getInt("OrderID");
                    if (!rs.wasNull()) imei.setOrderId(oid);
                } catch (Exception ignore) {
                    imei.setOrderId(null);
                }
                try {
                    java.sql.Date ws = rs.getDate("Warranty_Start");
                    java.sql.Date we = rs.getDate("Warranty_End");
                    imei.setWarrantyStart(ws);
                    imei.setWarrantyEnd(we);
                } catch (Exception ignore) {
                    // ignore if columns absent
                }

                return imei;
            }

        } catch (Exception e) {
            e.printStackTrace();
        }

        return null;
    }

    // 🔹 Lấy IMEI available để assign khi checkout
    public int getAvailableIMEI(int productId) {

        String sql = "SELECT TOP 1 IMEI_ID FROM ProductIMEI "
               + "WHERE ProductID = ? AND Status = 0"; // 0 = in stock

        try (Connection conn = DBContext.getConnection();
             PreparedStatement ps = conn.prepareStatement(sql)) {

            ps.setInt(1, productId);
            ResultSet rs = ps.executeQuery();

            if (rs.next()) {
                return rs.getInt("IMEI_ID");
            }

        } catch (Exception e) {
            e.printStackTrace();
        }

        return -1;
    }

    // 🔹 Assign IMEI khi order hoàn thành
    public void assignIMEI(int imeiId, int orderId) {
        // set status to sold, link order and store warranty period (now -> now+12 months)
        String sql = "UPDATE ProductIMEI SET Status = 1, OrderID = ?, Warranty_Start = GETDATE(), Warranty_End = DATEADD(MONTH,12,GETDATE()) WHERE IMEI_ID = ?";

        try (Connection conn = DBContext.getConnection();
             PreparedStatement ps = conn.prepareStatement(sql)) {

            ps.setInt(1, orderId);
            ps.setInt(2, imeiId);
            ps.executeUpdate();

        } catch (Exception e) {
            e.printStackTrace();
        }
    }
}