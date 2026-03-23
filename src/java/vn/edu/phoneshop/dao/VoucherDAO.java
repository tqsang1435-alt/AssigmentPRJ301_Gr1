package vn.edu.phoneshop.dao;

import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.util.ArrayList;
import java.util.List;
import vn.edu.phoneshop.model.Voucher;
import vn.edu.phoneshop.utils.DBContext;

public class VoucherDAO extends DBContext {

    // Lấy voucher theo code
    public Voucher getVoucherByCode(String code) {
        String sql = "SELECT * FROM Vouchers WHERE Code = ? AND Status = 1";
        try (Connection conn = getConnection();
             PreparedStatement ps = conn.prepareStatement(sql)) {
            ps.setString(1, code);
            try (ResultSet rs = ps.executeQuery()) {
                if (rs.next()) {
                    Voucher v = new Voucher();
                    v.setVoucherID(rs.getInt("VoucherID"));
                    v.setCode(rs.getString("Code"));
                    v.setDiscountType(rs.getString("DiscountType"));
                    v.setDiscountValue(rs.getDouble("DiscountValue"));
                    v.setMinOrderValue(rs.getDouble("MinOrderValue"));
                    v.setMaxDiscount(rs.getDouble("MaxDiscount"));
                    v.setExpiryDate(rs.getTimestamp("ExpiryDate"));
                    v.setUsageLimit(rs.getInt("UsageLimit"));
                    v.setUsedCount(rs.getInt("UsedCount"));
                    v.setStatus(rs.getBoolean("Status"));
                    return v;
                }
            }
        } catch (Exception e) {
            e.printStackTrace();
        }
        return null;
    }

    // Kiểm tra voucher có hợp lệ không (chưa hết hạn, chưa vượt usage limit)
    public boolean isVoucherValid(Voucher voucher) {
        if (voucher == null || !voucher.isStatus()) return false;
        if (voucher.getExpiryDate() != null && voucher.getExpiryDate().before(new java.util.Date())) return false;
        if (voucher.getUsageLimit() > 0 && voucher.getUsedCount() >= voucher.getUsageLimit()) return false;
        return true;
    }

    // Tăng số lần sử dụng voucher
    public void incrementUsedCount(int voucherID) {
        String sql = "UPDATE Vouchers SET UsedCount = UsedCount + 1 WHERE VoucherID = ?";
        try (Connection conn = getConnection();
             PreparedStatement ps = conn.prepareStatement(sql)) {
            ps.setInt(1, voucherID);
            ps.executeUpdate();
        } catch (Exception e) {
            e.printStackTrace();
        }
    }

    // Lấy tất cả voucher hợp lệ (chưa hết hạn, còn lượt sử dụng)
    public List<Voucher> getAllValidVouchers() {
        List<Voucher> list = new ArrayList<>();
        String sql = "SELECT * FROM Vouchers WHERE Status = 1 AND (ExpiryDate IS NULL OR ExpiryDate > GETDATE()) AND (UsageLimit = 0 OR UsedCount < UsageLimit)";
        try (Connection conn = getConnection();
             PreparedStatement ps = conn.prepareStatement(sql);
             ResultSet rs = ps.executeQuery()) {
            while (rs.next()) {
                Voucher v = new Voucher();
                v.setVoucherID(rs.getInt("VoucherID"));
                v.setCode(rs.getString("Code"));
                v.setDiscountType(rs.getString("DiscountType"));
                v.setDiscountValue(rs.getDouble("DiscountValue"));
                v.setMinOrderValue(rs.getDouble("MinOrderValue"));
                v.setMaxDiscount(rs.getDouble("MaxDiscount"));
                v.setExpiryDate(rs.getTimestamp("ExpiryDate"));
                v.setUsageLimit(rs.getInt("UsageLimit"));
                v.setUsedCount(rs.getInt("UsedCount"));
                v.setStatus(rs.getBoolean("Status"));
                list.add(v);
            }
        } catch (Exception e) {
            e.printStackTrace();
        }
        return list;
    }
    // Seed dữ liệu voucher mẫu (chỉ gọi một lần khi cần)
    public void seedVouchers() {
        // Thêm voucher mẫu nếu chưa tồn tại
        String[] codes = {"DISCOUNT10", "FIXED500K", "WELCOME5", "BIGSALE20", "FLASH300K", "LOYALTY15", "NEWUSER10", "SPECIAL25", "SUMMER50", "FREESHIP", "BLACKFRIDAY", "XMAS2025"};
        if (getVoucherByCode(codes[0]) == null) addVoucher(createVoucher("DISCOUNT10", "percent", 10.0, 1000000, 500000, 30, 100));
        if (getVoucherByCode(codes[1]) == null) addVoucher(createVoucher("FIXED500K", "fixed", 500000, 2000000, 0, 60, 50));
        if (getVoucherByCode(codes[2]) == null) addVoucher(createVoucher("WELCOME5", "percent", 5.0, 500000, 100000, 90, 200));
        if (getVoucherByCode(codes[3]) == null) addVoucher(createVoucher("BIGSALE20", "percent", 20.0, 3000000, 1000000, 30, 20));
        if (getVoucherByCode(codes[4]) == null) addVoucher(createVoucher("FLASH300K", "fixed", 300000, 1500000, 0, 7, 30));
        if (getVoucherByCode(codes[5]) == null) addVoucher(createVoucher("LOYALTY15", "percent", 15.0, 2000000, 750000, 180, 50));
        if (getVoucherByCode(codes[6]) == null) addVoucher(createVoucher("NEWUSER10", "percent", 10.0, 0, 200000, 60, 500));
        if (getVoucherByCode(codes[7]) == null) addVoucher(createVoucher("SPECIAL25", "percent", 25.0, 5000000, 1500000, 30, 10));
        if (getVoucherByCode(codes[8]) == null) addVoucher(createVoucher("SUMMER50", "percent", 50.0, 10000000, 2000000, 15, 5));
        if (getVoucherByCode(codes[9]) == null) addVoucher(createVoucher("FREESHIP", "fixed", 50000, 500000, 0, 365, 1000));
        if (getVoucherByCode(codes[10]) == null) addVoucher(createVoucher("BLACKFRIDAY", "percent", 30.0, 5000000, 1000000, 1, 50));
        if (getVoucherByCode(codes[11]) == null) addVoucher(createVoucher("XMAS2025", "fixed", 1000000, 15000000, 0, 30, 20));
    }

    // Thêm voucher mới (cho admin)
    public boolean addVoucher(Voucher voucher) {
        String sql = "INSERT INTO Vouchers (Code, DiscountType, DiscountValue, MinOrderValue, MaxDiscount, ExpiryDate, UsageLimit, Status) VALUES (?, ?, ?, ?, ?, ?, ?, ?)";
        try (Connection conn = getConnection();
             PreparedStatement ps = conn.prepareStatement(sql)) {
            ps.setString(1, voucher.getCode());
            ps.setString(2, voucher.getDiscountType());
            ps.setDouble(3, voucher.getDiscountValue());
            ps.setDouble(4, voucher.getMinOrderValue());
            ps.setDouble(5, voucher.getMaxDiscount());
            ps.setTimestamp(6, voucher.getExpiryDate() != null ? new java.sql.Timestamp(voucher.getExpiryDate().getTime()) : null);
            ps.setInt(7, voucher.getUsageLimit());
            ps.setBoolean(8, voucher.isStatus());
            return ps.executeUpdate() > 0;
        } catch (Exception e) {
            e.printStackTrace();
        }
        return false;
    }

    private Voucher createVoucher(String code, String type, double value, double minOrder, double maxDisc, int daysExpiry, int limit) {
        Voucher v = new Voucher();
        v.setCode(code);
        v.setDiscountType(type);
        v.setDiscountValue(value);
        v.setMinOrderValue(minOrder);
        v.setMaxDiscount(maxDisc);
        v.setExpiryDate(new java.util.Date(System.currentTimeMillis() + (long)daysExpiry * 24 * 60 * 60 * 1000));
        v.setUsageLimit(limit);
        v.setStatus(true);
        return v;
    }}