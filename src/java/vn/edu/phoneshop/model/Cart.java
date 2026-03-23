package vn.edu.phoneshop.model;

import java.util.ArrayList;
import java.util.List;

/**
 * Cart - Quản lý các sản phẩm trong giỏ hàng
 */
public class Cart {
    private List<CartItem> cartItems;
    private double discountPercent; // Rank discount
    private double voucherDiscount; // Voucher discount amount

    public Cart() {
        this.cartItems = new ArrayList<>();
    }

    // Thêm sản phẩm vào giỏ hàng
    public void addProduct(Product product, int quantity) {
        if (product == null || quantity < 1) {
            return;
        }

        // Kiểm tra sản phẩm đã có trong giỏ hay chưa
        for (CartItem item : cartItems) {
            if (item.getProduct().getProductID() == product.getProductID()) {
                item.setQuantity(item.getQuantity() + quantity);
                return;
            }
        }

        // Nếu chưa có, thêm sản phẩm mới
        cartItems.add(new CartItem(product, quantity));
    }

    // Xóa sản phẩm khỏi giỏ hàng
    public void removeProduct(int productID) {
        cartItems.removeIf(item -> item.getProduct().getProductID() == productID);
    }

    // Cập nhật số lượng sản phẩm
    public void updateQuantity(int productID, int quantity) {
        for (CartItem item : cartItems) {
            if (item.getProduct().getProductID() == productID) {
                item.setQuantity(quantity);
                return;
            }
        }
    }

    // Lấy item theo productID
    public CartItem getItemByProductID(int productID) {
        for (CartItem item : cartItems) {
            if (item.getProduct().getProductID() == productID) {
                return item;
            }
        }
        return null;
    }

    // Lấy danh sách các items
    public List<CartItem> getCartItems() {
        return cartItems;
    }

    // Đặt danh sách items
    public void setCartItems(List<CartItem> cartItems) {
        this.cartItems = cartItems;
    }

    // Tính tổng số lượng sản phẩm
    public int getTotalQuantity() {
        return cartItems.stream().mapToInt(CartItem::getQuantity).sum();
    }

    // Tính tổng tiền
    public double getTotalPrice() {
        return cartItems.stream().mapToDouble(CartItem::getSubtotal).sum();
    }

    // Kiểm tra giỏ hàng có trống không
    public boolean isEmpty() {
        return cartItems.isEmpty();
    }

    // Xóa toàn bộ giỏ hàng
    public void clear() {
        cartItems.clear();
    }

    // Lấy số lượng sản phẩm trong giỏ
    public int getCartSize() {
        return cartItems.size();
    }

    public double getDiscountPercent() {
        return discountPercent;
    }

    public void setDiscountPercent(double discountPercent) {
        this.discountPercent = discountPercent;
    }

    public double getDiscountAmount() {
        return (getTotalPrice() * (discountPercent / 100.0)) + voucherDiscount;
    }

    public double getRankDiscountAmount() {
        return getTotalPrice() * (discountPercent / 100.0);
    }

    public double getFinalTotalPrice() {
        return getTotalPrice() - getDiscountAmount();
    }

    // Lấy danh sách các items được chọn
    public List<CartItem> getSelectedItems() {
        List<CartItem> selected = new ArrayList<>();
        for (CartItem item : cartItems) {
            if (item.isSelected()) {
                selected.add(item);
            }
        }
        return selected;
    }

    // Tính tổng tiền chỉ của items được chọn
    public double getSelectedTotalPrice() {
        return getSelectedItems().stream().mapToDouble(CartItem::getSubtotal).sum();
    }

    // Tính số tiền giảm giá cho selected items
    public double getSelectedDiscountAmount() {
        return (getSelectedTotalPrice() * (discountPercent / 100.0)) + voucherDiscount;
    }

    // Tính tổng tiền sau giảm giá cho selected items
    public double getSelectedFinalTotalPrice() {
        return getSelectedTotalPrice() - getSelectedDiscountAmount();
    }

    // Tính tổng số lượng items được chọn
    public int getSelectedTotalQuantity() {
        return getSelectedItems().stream().mapToInt(CartItem::getQuantity).sum();
    }
    public double getVoucherDiscount() {
        return voucherDiscount;
    }

    public void setVoucherDiscount(double voucherDiscount) {
        this.voucherDiscount = voucherDiscount;
    }}
}
