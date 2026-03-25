package vn.edu.phoneshop.dao;

import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import vn.edu.phoneshop.model.Order;
import vn.edu.phoneshop.model.OrderDetail;
import vn.edu.phoneshop.utils.DBContext;

public class OrderDAO extends DBContext {
    public List<Order> getAllOrdersWithCustomerName() {
        List<Order> list = new ArrayList<>();
        String sql = "SELECT o.*, u.FullName FROM Orders o JOIN Users u ON o.UserID = u.UserID ORDER BY o.OrderDate DESC";
        try (Connection conn = getConnection();
                PreparedStatement ps = conn.prepareStatement(sql);
                ResultSet rs = ps.executeQuery()) {
            while (rs.next()) {
                Order o = new Order();
                o.setOrderId(rs.getInt("OrderID"));
                o.setUserId(rs.getInt("UserID"));
                o.setOrderDate(rs.getTimestamp("OrderDate"));
                o.setTotalMoney(rs.getDouble("TotalMoney"));
                o.setShippingAddress(rs.getString("ShippingAddress"));
                o.setNote(rs.getString("Note"));
                o.setStatus(rs.getInt("Status"));
                o.setCustomerName(rs.getString("FullName"));
                o.setVoucherID(rs.getInt("VoucherID"));
                list.add(o);
            }
        } catch (Exception e) {
            e.printStackTrace();
        }
        return list;
    }

    // Cập nhật trạng thái đơn hàng (Dùng cho Admin)
    public void updateOrderStatus(int orderId, int status) {
        String sql = "UPDATE Orders SET Status = ? WHERE OrderID = ?";
        try (Connection conn = getConnection();
                PreparedStatement ps = conn.prepareStatement(sql)) {
            ps.setInt(1, status);
            ps.setInt(2, orderId);
            ps.executeUpdate();
        } catch (Exception e) {
            e.printStackTrace();
        }
    }

    // Tạo đơn hàng mới và trả về ID (Dùng cho Checkout)
    public int createOrder(int userId, double totalMoney, int status, int voucherID) {
        return createOrder(userId, totalMoney, status, voucherID, null);
    }

    // Tạo đơn hàng mới kèm địa chỉ giao hàng và trả về ID (Dùng cho Checkout)
    public int createOrder(int userId, double totalMoney, int status, int voucherID, String shippingAddress) {
        String sql = "INSERT INTO Orders (UserID, OrderDate, TotalMoney, Status, VoucherID, ShippingAddress) VALUES (?, GETDATE(), ?, ?, ?, ?)";
        try (Connection conn = getConnection();
                PreparedStatement ps = conn.prepareStatement(sql, PreparedStatement.RETURN_GENERATED_KEYS)) {
            ps.setInt(1, userId);
            ps.setDouble(2, totalMoney);
            ps.setInt(3, status);
            if (voucherID > 0) {
                ps.setInt(4, voucherID);
            } else {
                ps.setNull(4, java.sql.Types.INTEGER);
            }
            if (shippingAddress != null && !shippingAddress.trim().isEmpty()) {
                ps.setString(5, shippingAddress);
            } else {
                ps.setNull(5, java.sql.Types.NVARCHAR);
            }
            ps.executeUpdate();
            try (ResultSet rs = ps.getGeneratedKeys()) {
                if (rs.next()) {
                    return rs.getInt(1);
                }
            }
        } catch (Exception e) {
            e.printStackTrace();
        }
        return 0;
    }

    // Cập nhật địa chỉ giao hàng và trạng thái (Dùng cho Checkout)
    public void updateOrderStatusAndAddress(int orderId, int status, String address) {
        String sql = "UPDATE Orders SET Status = ?, ShippingAddress = ? WHERE OrderID = ?";
        try (Connection conn = getConnection();
                PreparedStatement ps = conn.prepareStatement(sql)) {
            ps.setInt(1, status);
            ps.setString(2, address);
            ps.setInt(3, orderId);
            ps.executeUpdate();
        } catch (Exception e) {
            e.printStackTrace();
        }
    }

    // Thêm chi tiết đơn hàng (Dùng cho Checkout)
    public void insertOrderDetail(int orderId, int productId, int quantity, double price) {
        String sql = "INSERT INTO OrderDetails (OrderID, ProductID, Quantity, Price) VALUES (?, ?, ?, ?)";
        try (Connection conn = getConnection();
                PreparedStatement ps = conn.prepareStatement(sql)) {
            ps.setInt(1, orderId);
            ps.setInt(2, productId);
            ps.setInt(3, quantity);
            ps.setDouble(4, price);
            ps.executeUpdate();
        } catch (Exception e) {
            e.printStackTrace();
        }
    }

    // Lấy danh sách đơn hàng theo ID người dùng (Dùng cho trang Lịch sử mua hàng)
    public List<Order> getOrdersByUserId(int userId) {
        List<Order> list = new ArrayList<>();
        String sql = "SELECT * FROM Orders WHERE UserID = ? ORDER BY OrderDate DESC";
        try (Connection conn = getConnection();
                PreparedStatement ps = conn.prepareStatement(sql)) {
            ps.setInt(1, userId);
            try (ResultSet rs = ps.executeQuery()) {
                while (rs.next()) {
                    Order o = new Order();
                    o.setOrderId(rs.getInt("OrderID"));
                    o.setUserId(rs.getInt("UserID"));
                    o.setOrderDate(rs.getTimestamp("OrderDate"));
                    o.setTotalMoney(rs.getDouble("TotalMoney"));
                    o.setShippingAddress(rs.getString("ShippingAddress"));
                    o.setNote(rs.getString("Note"));
                    o.setStatus(rs.getInt("Status"));
                    o.setVoucherID(rs.getInt("VoucherID"));
                    list.add(o);
                }
            }
        } catch (Exception e) {
            e.printStackTrace();
        }
        return list;
    }

    public List<OrderDetail> getOrderDetailByOrderID(int orderId) {
        List<OrderDetail> list = new ArrayList<>();
        String sql = "SELECT od.*, p.ProductName, p.ImageURL " +
                "FROM OrderDetails od JOIN Products p ON od.ProductID = p.ProductID " +
                "WHERE od.OrderID = ?";
        try (Connection conn = getConnection();
                PreparedStatement ps = conn.prepareStatement(sql)) {
            ps.setInt(1, orderId);
            try (ResultSet rs = ps.executeQuery()) {
                while (rs.next()) {
                    OrderDetail od = new OrderDetail();
                    od.setDetailId(rs.getInt("OrderDetailID"));
                    od.setOrderId(rs.getInt("OrderID"));
                    od.setProductId(rs.getInt("ProductID"));
                    od.setQuantity(rs.getInt("Quantity"));
                    od.setPrice(rs.getDouble("Price"));
                    od.setProductName(rs.getString("ProductName"));
                    od.setImageURL(rs.getString("ImageURL"));
                    list.add(od);
                }
            }
        } catch (Exception e) {
            e.printStackTrace();
        }
        return list;
    }

    public Order getOrderById(int orderId) {
        String sql = "SELECT o.*, u.FullName, u.PhoneNumber " +
                "FROM Orders o JOIN Users u ON o.UserID = u.UserID WHERE o.OrderID = ?";
        try (Connection conn = getConnection();
                PreparedStatement ps = conn.prepareStatement(sql)) {
            ps.setInt(1, orderId);
            try (ResultSet rs = ps.executeQuery()) {
                if (rs.next()) {
                    Order o = new Order();
                    o.setOrderId(rs.getInt("OrderID"));
                    o.setUserId(rs.getInt("UserID"));
                    o.setOrderDate(rs.getTimestamp("OrderDate"));
                    o.setTotalMoney(rs.getDouble("TotalMoney"));
                    o.setShippingAddress(rs.getString("ShippingAddress"));
                    o.setNote(rs.getString("Note"));
                    o.setStatus(rs.getInt("Status"));
                    o.setCustomerName(rs.getString("FullName"));
                    // Giả sử bạn có thêm trường phone trong model Order để tiện hiển thị
                    return o;
                }
            }
        } catch (Exception e) {
            e.printStackTrace();
        }
        return null;
    }

    public Map<Integer, String> getProductNamesMap(int orderId) {
        Map<Integer, String> productNames = new HashMap<>();
        String sql = "SELECT p.ProductID, p.ProductName " +
                "FROM OrderDetails od JOIN Products p ON od.ProductID = p.ProductID " +
                "WHERE od.OrderID = ?";
        try (Connection conn = getConnection();
                PreparedStatement ps = conn.prepareStatement(sql)) {
            ps.setInt(1, orderId);
            try (ResultSet rs = ps.executeQuery()) {
                while (rs.next()) {
                    productNames.put(rs.getInt("ProductID"), rs.getString("ProductName"));
                }
            }
        } catch (Exception e) {
            e.printStackTrace();
        }
        return productNames;
    }

    public int getTotalOrders() {
        String sql = "SELECT COUNT(*) FROM Orders";
        try (Connection conn = getConnection();
                PreparedStatement ps = conn.prepareStatement(sql);
                ResultSet rs = ps.executeQuery()) {
            if (rs.next()) {
                return rs.getInt(1);
            }
        } catch (Exception e) {
            e.printStackTrace();
        }
        return 0;
    }

    public List<Order> getOrdersWithCustomerNamePaginated(int offset, int limit) {
        List<Order> list = new ArrayList<>();
        String sql = "SELECT o.*, u.FullName FROM Orders o JOIN Users u ON o.UserID = u.UserID " +
                     "ORDER BY o.OrderDate DESC " +
                     "OFFSET ? ROWS FETCH NEXT ? ROWS ONLY";
        try (Connection conn = getConnection();
                PreparedStatement ps = conn.prepareStatement(sql)) {
            ps.setInt(1, offset);
            ps.setInt(2, limit);
            try (ResultSet rs = ps.executeQuery()) {
                while (rs.next()) {
                    Order o = new Order();
                    o.setOrderId(rs.getInt("OrderID"));
                    o.setUserId(rs.getInt("UserID"));
                    o.setOrderDate(rs.getTimestamp("OrderDate"));
                    o.setTotalMoney(rs.getDouble("TotalMoney"));
                    o.setShippingAddress(rs.getString("ShippingAddress"));
                    o.setNote(rs.getString("Note"));
                    o.setStatus(rs.getInt("Status"));
                    o.setCustomerName(rs.getString("FullName"));
                    list.add(o);
                }
            }
        } catch (Exception e) {
            e.printStackTrace();
        }
        return list;
    }
}