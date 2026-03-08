package vn.edu.phoneshop.model;

public class Product {

    private int productId;
    private String productName;
    private double price;
    private int quantity;
    private String description;
    private String image;

    public Product() {
    }

    public Product(String productName, double price, int quantity, String description, String image) {
        this.productName = productName;
        this.price = price;
        this.quantity = quantity;
        this.description = description;
        this.image = image;
    }

    public Product(int productId, String productName, double price, int quantity, String description, String image) {
        this.productId = productId;
        this.productName = productName;
        this.price = price;
        this.quantity = quantity;
        this.description = description;
        this.image = image;
    }

    // ===== Getter & Setter =====

    public int getProductId() {
        return productId;
    }

    public void setProductId(int productId) {
        this.productId = productId;
    }

    public String getProductName() {
        return productName;
    }

    public void setProductName(String productName) {
        this.productName = productName;
    }

    public double getPrice() {
        return price;
    }

    public void setPrice(double price) {
        this.price = price;
    }

    public int getQuantity() {
        return quantity;
    }

    public void setQuantity(int quantity) {
        this.quantity = quantity;
    }

    public String getDescription() {
        return description;
    }

    public void setDescription(String description) {
        this.description = description;
    }

    public String getImage() {
        return image;
    }

    public void setImage(String image) {
        this.image = image;
    }
}