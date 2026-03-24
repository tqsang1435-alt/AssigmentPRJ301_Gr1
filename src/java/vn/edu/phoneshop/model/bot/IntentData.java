package vn.edu.phoneshop.model.bot;

import java.util.ArrayList;
import java.util.List;

public class IntentData implements java.io.Serializable {
    private static final long serialVersionUID = 1L;
    public double minPrice = 0;
    public double maxPrice = 0;
    public String ram = "";
    public String rom = "";
    public boolean isComparison = false;
    public boolean isGaming = false;
    public boolean isCamera = false;
    public boolean isBattery = false;
    public boolean isBuying = false;
    public String exactPhrase = "";
    public List<String> keywords = new ArrayList<>();
    /**
     * Tên brand cụ thể user yêu cầu (vd: "iphone", "samsung", "oppo") - null nếu
     * không xác định, hoặc null nếu so sánh nhiều brand
     */
    public String requestedBrand = null;
    /**
     * Danh sách các brand được so sánh (dùng khi isComparison=true và có >= 2 brand)
     */
    public List<String> comparisonBrands = new ArrayList<>();

    public boolean hasFullIntent() {
        // Updated: Coi là có đủ Intent nếu có bất kỳ tiêu chí lọc cứng nào (Giá, RAM,
        // ROM, Gaming, Keywords)
        return minPrice > 0 || maxPrice > 0 || !ram.isEmpty() || !rom.isEmpty() || isGaming || !keywords.isEmpty();
    }
}
