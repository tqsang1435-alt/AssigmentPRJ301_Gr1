package vn.edu.phoneshop.model;

/**
 * CartItem - Đại diện cho một sản phẩm trong giỏ hàng
 */
public class CartItem {
    private Product product;
    private int quantity;
    private boolean selected; // true nếu được chọn để mua

    public CartItem() {
        this.selected = true; // mặc định chọn
    }

    public CartItem(Product product, int quantity) {
        this.product = product;
        this.quantity = quantity;
        this.selected = true; // mặc định chọn
    }

    public Product getProduct() {
        return product;
    }

    public void setProduct(Product product) {
        this.product = product;
    }

    public int getQuantity() {
        return quantity;
    }

    public void setQuantity(int quantity) {
        if (quantity < 1) {
            this.quantity = 1;
        } else {
            this.quantity = quantity;
        }
    }

    public double getSubtotal() {
        if (product != null) {
            return product.getPrice() * quantity;
        }
        return 0;
    }

    public boolean isSelected() {
        return selected;
    }

    public void setSelected(boolean selected) {
        this.selected = selected;
    }
}
