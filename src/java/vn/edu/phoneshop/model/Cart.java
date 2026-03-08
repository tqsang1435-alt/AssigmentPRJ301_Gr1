package vn.edu.phoneshop.model;

import java.util.ArrayList;
import java.util.List;

/**
 * Cart - Quản lý các sản phẩm trong giỏ hàng
 */
public class Cart {
    private List<CartItem> cartItems;

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
}
