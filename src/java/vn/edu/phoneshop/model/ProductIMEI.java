package vn.edu.phoneshop.model;

/*
 * Click nbfs://nbhost/SystemFileSystem/Templates/Licenses/license-default.txt to change this license
 * Click nbfs://nbhost/SystemFileSystem/Templates/Classes/Class.java to edit this template
 */

/**
 *
 * @author Lenovo
 */

public class ProductIMEI {
    private int imeiID;
    private int productID;
    private String imeiNumber;
    private int status;
    private Integer orderID;

    public ProductIMEI() {}

    public ProductIMEI(int imeiID, int productID,
                       String imeiNumber, int status, Integer orderID) {
        this.imeiID = imeiID;
        this.productID = productID;
        this.imeiNumber = imeiNumber;
        this.status = status;
        this.orderID = orderID;
    }

    public int getImeiID() { return imeiID; }
    public void setImeiID(int imeiID) { this.imeiID = imeiID; }

    public int getProductID() { return productID; }
    public void setProductID(int productID) { this.productID = productID; }

    public String getImeiNumber() { return imeiNumber; }
    public void setImeiNumber(String imeiNumber) { this.imeiNumber = imeiNumber; }

    public int getStatus() { return status; }
    public void setStatus(int status) { this.status = status; }

    public Integer getOrderID() { return orderID; }
    public void setOrderID(Integer orderID) { this.orderID = orderID; }
}
