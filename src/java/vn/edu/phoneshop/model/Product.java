/*
 * Click nbfs://nbhost/SystemFileSystem/Templates/Licenses/license-default.txt to change this license
 * Click nbfs://nbhost/SystemFileSystem/Templates/Classes/Class.java to edit this template
 */
package vn.edu.phoneshop.model;

/**
 *
 * @author tqsan
 */
public class Product {
    private int productID;
    private String productName;
    private double price;
    private int stockQuantity;
    private String description;
    private String imageURL;
    private int categoryID;
    private int supplierID;
    private boolean status;
    
    private String ram;
    private String rom;
    private String color;

    public Product() {
    }

    public Product(int productID, String productName, double price, int stockQuantity, String description, String imageURL, int categoryID, int supplierID, boolean status, String ram, String rom, String color) {
        this.productID = productID;
        this.productName = productName;
        this.price = price;
        this.stockQuantity = stockQuantity;
        this.description = description;
        this.imageURL = imageURL;
        this.categoryID = categoryID;
        this.supplierID = supplierID;
        this.status = status;
        this.ram = ram;
        this.rom = rom;
        this.color = color;
    }

    public int getProductID() {
        return productID;
    }

    public void setProductID(int productID) {
        this.productID = productID;
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

    public int getStockQuantity() {
        return stockQuantity;
    }

    public void setStockQuantity(int stockQuantity) {
        this.stockQuantity = stockQuantity;
    }

    public String getDescription() {
        return description;
    }

    public void setDescription(String description) {
        this.description = description;
    }

    public String getImageURL() {
        return imageURL;
    }

    public void setImageURL(String imageURL) {
        this.imageURL = imageURL;
    }

    public int getCategoryID() {
        return categoryID;
    }

    public void setCategoryID(int categoryID) {
        this.categoryID = categoryID;
    }

    public int getSupplierID() {
        return supplierID;
    }

    public void setSupplierID(int supplierID) {
        this.supplierID = supplierID;
    }

    public boolean isStatus() {
        return status;
    }

    public void setStatus(boolean status) {
        this.status = status;
    }

    
    public String getRam() { return ram; }
    public void setRam(String ram) { this.ram = ram; }
    public String getRom() { return rom; }
    public void setRom(String rom) { this.rom = rom; }
    public String getColor() { return color; }
    public void setColor(String color) { this.color = color; }
}
