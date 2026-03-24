package vn.edu.phoneshop.model.bot;

import java.util.ArrayList;
import java.util.LinkedHashSet;
import java.util.LinkedList;
import java.util.List;
import java.util.Set;

/**
 * UserMemory: lưu hành vi người dùng trong session để cá nhân hóa gợi ý.
 * - clickedProductIds: danh sách ID sản phẩm user đã click xem
 * - purchasedBrands: thương hiệu user đã từng mua (từ DB đơn hàng)
 */
public class UserMemory implements java.io.Serializable {
    private static final long serialVersionUID = 1L;
    // Tên sản phẩm đã click (tối đa 20 gần nhất)
    public LinkedList<String> clickedProductNames = new LinkedList<>();
    // Thương hiệu đã mua (Apple, Samsung, Xiaomi, ...)
    public Set<String> purchasedBrands = new LinkedHashSet<>();
    // Tên sản phẩm đã mua
    public List<String> purchasedProductNames = new ArrayList<>();
    // Đã tải lịch sử từ DB cho userId này chưa?
    public String loadedForUserId = null;

    public void trackClick(String productName) {
        if (productName == null || productName.isBlank())
            return;
        clickedProductNames.remove(productName); // tránh trùng
        clickedProductNames.addFirst(productName);
        if (clickedProductNames.size() > 20)
            clickedProductNames.removeLast();
    }

    /** Trích thương hiệu từ tên sản phẩm (token đầu tiên) */
    public static String extractBrand(String productName) {
        if (productName == null)
            return "";
        String[] parts = productName.trim().split("\\s+");
        return parts.length > 0 ? parts[0].toLowerCase() : "";
    }

    public boolean isEmpty() {
        return clickedProductNames.isEmpty() && purchasedBrands.isEmpty();
    }
}
