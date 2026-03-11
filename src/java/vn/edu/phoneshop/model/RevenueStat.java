package vn.edu.phoneshop.model;

import java.util.Date;

public class RevenueStat {
    private int year;
    private int month;
    private Date date;
    private String productName;
    private int quantity;
    private int orderCount;
    private double totalRevenue;

    public RevenueStat() {
    }

    public RevenueStat(int year, int month, int orderCount, double totalRevenue) {
        this.year = year;
        this.month = month;
        this.orderCount = orderCount;
        this.totalRevenue = totalRevenue;
    }

    public int getYear() {
        return year;
    }

    public void setYear(int year) {
        this.year = year;
    }

    public int getMonth() {
        return month;
    }

    public void setMonth(int month) {
        this.month = month;
    }

    public int getOrderCount() {
        return orderCount;
    }

    public void setOrderCount(int orderCount) {
        this.orderCount = orderCount;
    }

    public double getTotalRevenue() {
        return totalRevenue;
    }

    public void setTotalRevenue(double totalRevenue) {
        this.totalRevenue = totalRevenue;
    }

    public Date getDate() {
        return date;
    }

    public void setDate(Date date) {
        this.date = date;
    }

    public String getProductName() {
        return productName;
    }

    public void setProductName(String productName) {
        this.productName = productName;
    }

    public int getQuantity() {
        return quantity;
    }

    public void setQuantity(int quantity) {
        this.quantity = quantity;
    }
}
