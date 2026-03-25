package vn.edu.phoneshop.controller;

import java.io.IOException;
import java.util.List;
import jakarta.servlet.ServletException;
import jakarta.servlet.annotation.WebServlet;
import jakarta.servlet.http.HttpServlet;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;
import jakarta.servlet.http.HttpSession;
import vn.edu.phoneshop.dao.OrderDAO;
import vn.edu.phoneshop.dao.UserDAO;
import vn.edu.phoneshop.dao.VoucherDAO;
import vn.edu.phoneshop.model.Cart;
import vn.edu.phoneshop.model.CartItem;
import vn.edu.phoneshop.model.User;
import vn.edu.phoneshop.model.Voucher;

@WebServlet(name = "CheckoutControl", urlPatterns = { "/checkout" })
public class CheckoutControl extends HttpServlet {

    @Override
    protected void doGet(HttpServletRequest request, HttpServletResponse response)
            throws ServletException, IOException {
        HttpSession session = request.getSession();

        User account = (User) session.getAttribute("ACC");
        if (account == null) {
            response.sendRedirect("user-login"); // Redirect to login page
            return;
        }

        // Check if cart is empty
        Cart cart = (Cart) session.getAttribute("cart");
        if (cart == null || cart.getSelectedTotalQuantity() == 0) {
            // Redirect to cart page with an error message
            request.setAttribute("mess", "Vui lòng chọn ít nhất một sản phẩm để thanh toán!");
            request.getRequestDispatcher("/cart.jsp").forward(request, response);
            return;
        }

        // Tính toán giảm giá theo hạng thành viên
        double discountPercent = getDiscountPercent(account.getCustomerType());
        cart.setDiscountPercent(discountPercent);
        cart.setVoucherDiscount(0); // Reset voucher discount

        // Lấy danh sách voucher hợp lệ
        VoucherDAO voucherDAO = new VoucherDAO();
        List<Voucher> validVouchers = voucherDAO.getAllValidVouchers();
        if (validVouchers.isEmpty()) {
            voucherDAO.seedVouchers();
            validVouchers = voucherDAO.getAllValidVouchers();
        }
        request.setAttribute("validVouchers", validVouchers);
        request.setAttribute("appliedVoucher", null); // Reset applied voucher

        // Forward to the checkout page to display it
        request.getRequestDispatcher("checkout.jsp").forward(request, response);
    }

    @Override
    protected void doPost(HttpServletRequest request, HttpServletResponse response)
            throws ServletException, IOException {
        request.setCharacterEncoding("UTF-8");
        HttpSession session = request.getSession();

        User user = (User) session.getAttribute("ACC");
        Cart cart = (Cart) session.getAttribute("cart");

        if (user == null) {
            response.sendRedirect("user-login");
            return;
        }

        if (cart == null || cart.isEmpty()) {
            response.sendRedirect("view-cart");
            return;
        }

        if (cart.getSelectedTotalQuantity() == 0) {
            request.setAttribute("mess", "Vui lòng chọn ít nhất một sản phẩm để thanh toán!");
            attachVouchersToRequest(request);
            request.getRequestDispatcher("checkout.jsp").forward(request, response);
            return;
        }

        String name = request.getParameter("name");
        String phone = request.getParameter("phone");
        String address = request.getParameter("address");
        String voucherCode = request.getParameter("voucher");

        // 1. Kiểm tra dữ liệu nhập vào có bị trống không
        if (name == null || name.trim().isEmpty() || phone == null || phone.trim().isEmpty() || address == null
                || address.trim().isEmpty()) {
            request.setAttribute("mess", "Vui lòng điền đầy đủ thông tin nhận hàng (Họ tên, SĐT, Địa chỉ)!");
            attachVouchersToRequest(request);
            request.getRequestDispatcher("checkout.jsp").forward(request, response);
            return;
        }

        // 2. Kiểm tra định dạng số điện thoại (10-11 chữ số)
        if (!phone.matches("\\d{10,11}")) {
            request.setAttribute("mess", "Số điện thoại không hợp lệ (phải là 10-11 số)!");
            attachVouchersToRequest(request);
            request.getRequestDispatcher("checkout.jsp").forward(request, response);
            return;
        }

        // 3. Xử lý voucher
        VoucherDAO voucherDAO = new VoucherDAO();
        int voucherID = 0;
        double voucherDiscount = 0;
        if (voucherCode != null && !voucherCode.trim().isEmpty()) {
            Voucher voucher = voucherDAO.getVoucherByCode(voucherCode.trim());
            if (voucher == null) {
                request.setAttribute("mess", "Mã voucher không tồn tại!");
                attachVouchersToRequest(request);
                request.getRequestDispatcher("checkout.jsp").forward(request, response);
                return;
            }
            if (!voucherDAO.isVoucherValid(voucher)) {
                request.setAttribute("mess", "Mã voucher không hợp lệ hoặc đã hết hạn!");
                attachVouchersToRequest(request);
                request.getRequestDispatcher("checkout.jsp").forward(request, response);
                return;
            }
            // Tính discount tạm thời để check min order (dùng selectedTotalPrice thay vì getTotalPrice)
            double tempTotal = cart.getSelectedTotalPrice();
            double rankDiscount = tempTotal * (getDiscountPercent(user.getCustomerType()) / 100.0);
            double subtotalAfterRank = tempTotal - rankDiscount;
            if (subtotalAfterRank < voucher.getMinOrderValue()) {
                request.setAttribute("mess", "Đơn hàng chưa đạt giá trị tối thiểu để sử dụng voucher này!");
                attachVouchersToRequest(request);
                request.getRequestDispatcher("checkout.jsp").forward(request, response);
                return;
            }
            // Tính voucher discount
            if ("percent".equals(voucher.getDiscountType())) {
                voucherDiscount = subtotalAfterRank * (voucher.getDiscountValue() / 100.0);
                if (voucher.getMaxDiscount() > 0 && voucherDiscount > voucher.getMaxDiscount()) {
                    voucherDiscount = voucher.getMaxDiscount();
                }
            } else if ("fixed".equals(voucher.getDiscountType())) {
                voucherDiscount = voucher.getDiscountValue();
            }
            voucherID = voucher.getVoucherID();
            request.setAttribute("appliedVoucher", voucher);
        }

        // Tính toán tổng tiền sau khi giảm giá
        double discountPercent = getDiscountPercent(user.getCustomerType());
        cart.setDiscountPercent(discountPercent);
        cart.setVoucherDiscount(voucherDiscount);
        double selectedTotal = cart.getSelectedTotalPrice();
        double discountAmount = selectedTotal * (discountPercent / 100.0) + voucherDiscount;
        double totalMoney = selectedTotal - discountAmount; // đã tính dựa trên selected items

        OrderDAO dao = new OrderDAO();
        // Tạo đơn hàng mới với trạng thái 1 (Chờ xác nhận)
        int orderId = dao.createOrder(user.getUserID(), totalMoney, 1, voucherID);

        if (orderId > 0) {
            dao.updateOrderStatusAndAddress(orderId, 1, address);
            for (CartItem item : cart.getSelectedItems()) {
                dao.insertOrderDetail(orderId, item.getProduct().getProductID(), item.getQuantity(),
                        item.getProduct().getPrice());
            }
            // Tăng used count cho voucher
            if (voucherID > 0) {
                voucherDAO.incrementUsedCount(voucherID);
            }
            session.removeAttribute("cart");
            session.setAttribute("cartCount", 0);

            // Cộng điểm thưởng và cập nhật hạng thành viên
            int pointsEarned = (int) (totalMoney / 100000);
            UserDAO userDAO = new UserDAO();
            userDAO.updateRewardPoints(user.getUserID(), totalMoney);
            user.setRewardPoints(user.getRewardPoints() + pointsEarned);
            int newPoints = user.getRewardPoints();
            String newType;
            if (newPoints >= 20000)
                newType = "Diamond";
            else if (newPoints >= 10000)
                newType = "Gold";
            else if (newPoints >= 5000)
                newType = "Silver";
            else if (newPoints >= 1000)
                newType = "Bronze";
            else
                newType = "New Member";

            user.setCustomerType(newType);
            // Lưu CustomerType mới vào database
            userDAO.updateCustomerType(user.getUserID(), newType);

            session.setAttribute("ACC", user);

            request.setAttribute("message", "Đơn hàng của bạn đã được gửi đến địa chỉ: " + address
                    + ". Bạn đã tích lũy được " + pointsEarned + " điểm.");
            request.getRequestDispatcher("thanks.jsp").forward(request, response);
        } else {
            request.setAttribute("mess", "Có lỗi xảy ra khi tạo đơn hàng. Vui lòng thử lại!");
            attachVouchersToRequest(request);
            request.getRequestDispatcher("checkout.jsp").forward(request, response);
        }
    }

    // Đính kèm danh sách voucher vào request khi forward về checkout.jsp
    private void attachVouchersToRequest(HttpServletRequest request) {
        try {
            VoucherDAO voucherDAO = new VoucherDAO();
            List<Voucher> validVouchers = voucherDAO.getAllValidVouchers();
            request.setAttribute("validVouchers", validVouchers);
        } catch (Exception e) {
            // ignore, page can still render without vouchers
        }
    }

    // Lấy phần trăm giảm giá dựa trên hạng thành viên.
    public static double getDiscountPercent(String customerType) {
        if (customerType == null) {
            return 0.0;
        }
        switch (customerType) {
            case "Diamond":
                return 15.0;
            case "Gold":
                return 10.0;
            case "Silver":
                return 5.0;
            case "Bronze":
                return 2.0;
            default:
                return 0.0;
        }
    }
}
