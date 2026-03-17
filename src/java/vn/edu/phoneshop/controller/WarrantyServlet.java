package vn.edu.phoneshop.controller;

import jakarta.servlet.ServletException;
import jakarta.servlet.annotation.WebServlet;
import jakarta.servlet.http.HttpServlet;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;
import java.io.IOException;
import vn.edu.phoneshop.dao.IMEIDAO;
import vn.edu.phoneshop.model.IMEI;
import vn.edu.phoneshop.utils.DBContext;
import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.Timestamp;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.Calendar;
import java.util.Date;

@WebServlet("/warranty")
public class WarrantyServlet extends HttpServlet {

    IMEIDAO dao = new IMEIDAO();

    protected void doPost(HttpServletRequest request, HttpServletResponse response)
            throws ServletException, IOException {

        String serial = request.getParameter("serial");
        IMEI imei = dao.findBySerial(serial);

        request.setAttribute("imei", imei);

        // If IMEI is associated with an order, load order meta and items and compute
        // warranty
        if (imei != null && imei.getOrderId() != null && imei.getOrderId() > 0) {
            int orderId = imei.getOrderId();
            Map<String, Object> orderMeta = new HashMap<>();
            List<Map<String, Object>> items = new ArrayList<>();

            String orderSql = "SELECT OrderID, OrderDate, TotalMoney, ShippingAddress, Status, Note FROM Orders WHERE OrderID = ?";
            String itemsSql = "SELECT od.OrderDetailID, od.ProductID, od.Quantity, od.Price, p.ProductName "
                    + "FROM OrderDetails od LEFT JOIN Products p ON od.ProductID = p.ProductID WHERE od.OrderID = ?";

            try (Connection conn = DBContext.getConnection()) {
                try (PreparedStatement ps = conn.prepareStatement(orderSql)) {
                    ps.setInt(1, orderId);
                    ResultSet rs = ps.executeQuery();
                    if (rs.next()) {
                        orderMeta.put("OrderID", rs.getInt("OrderID"));
                        Timestamp odt = rs.getTimestamp("OrderDate");
                        orderMeta.put("OrderDate", odt);
                        orderMeta.put("TotalMoney", rs.getBigDecimal("TotalMoney"));
                        orderMeta.put("ShippingAddress", rs.getString("ShippingAddress"));
                        orderMeta.put("Status", rs.getInt("Status"));
                        orderMeta.put("Note", rs.getString("Note"));

                        // compute warranty period: prefer stored IMEI warranty if present
                        // (WarrantyServlet will later check IMEI fields first)
                        if (odt != null) {
                            Calendar cal = Calendar.getInstance();
                            cal.setTime(new Date(odt.getTime()));
                            Date warrantyStart = cal.getTime();
                            cal.add(Calendar.MONTH, 12);
                            Date warrantyEnd = cal.getTime();
                            orderMeta.put("WarrantyStart", warrantyStart);
                            orderMeta.put("WarrantyEnd", warrantyEnd);
                        }
                    }
                }

                try (PreparedStatement ps2 = conn.prepareStatement(itemsSql)) {
                    ps2.setInt(1, orderId);
                    ResultSet rs2 = ps2.executeQuery();
                    while (rs2.next()) {
                        Map<String, Object> it = new HashMap<>();
                        it.put("OrderDetailID", rs2.getInt("OrderDetailID"));
                        it.put("ProductID", rs2.getInt("ProductID"));
                        it.put("Quantity", rs2.getInt("Quantity"));
                        it.put("Price", rs2.getBigDecimal("Price"));
                        it.put("ProductName", rs2.getString("ProductName"));
                        items.add(it);
                    }
                }

            } catch (Exception e) {
                e.printStackTrace();
            }

            // If IMEI has stored warranty dates, use them instead of computed ones
            if (imei.getWarrantyStart() != null && imei.getWarrantyEnd() != null) {
                orderMeta.put("WarrantyStart", imei.getWarrantyStart());
                orderMeta.put("WarrantyEnd", imei.getWarrantyEnd());
            }

            request.setAttribute("orderMeta", orderMeta);
            request.setAttribute("orderItems", items);
        }

        request.getRequestDispatcher("warranty.jsp").forward(request, response);
    }
}