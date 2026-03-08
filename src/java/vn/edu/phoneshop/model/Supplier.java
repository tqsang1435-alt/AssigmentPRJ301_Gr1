<<<<<<< HEAD
package vn.edu.phoneshop.model;

public class Supplier {
    private int supplierID;
    private String supplierName;
    private String contactName;
    private String phone;
    private String email;
    private String address;
    private String logo;
    private boolean status;

    // constructor + getter setter

    public Supplier(int supplierID, String supplierName, String contactName, String phone, String email, String address, String logo, boolean status) {
        this.supplierID = supplierID;
        this.supplierName = supplierName;
        this.contactName = contactName;
        this.phone = phone;
        this.email = email;
        this.address = address;
        this.logo = logo;
        this.status = status;
    }

    public Supplier(String name, String contact, String phone, String email, String address, String logo, boolean b) {
        this.supplierName = name;
        this.contactName = contact;
        this.phone = phone;
        this.email = email;
        this.address = address;
        this.logo = logo;
        this.status = b;
    }

    public int getSupplierID() {
        return supplierID;
    }

    public void setSupplierID(int supplierID) {
        this.supplierID = supplierID;
    }

    public String getSupplierName() {
        return supplierName;
    }

    public void setSupplierName(String supplierName) {
        this.supplierName = supplierName;
    }

    public String getContactName() {
        return contactName;
    }

    public void setContactName(String contactName) {
        this.contactName = contactName;
    }

    public String getPhone() {
        return phone;
    }

    public void setPhone(String phone) {
        this.phone = phone;
    }

    public String getEmail() {
        return email;
    }

    public void setEmail(String email) {
        this.email = email;
    }

    public String getAddress() {
        return address;
    }

    public void setAddress(String address) {
        this.address = address;
    }

    public String getLogo() {
        return logo;
    }

    public void setLogo(String logo) {
        this.logo = logo;
    }

    public boolean isStatus() {
        return status;
    }

    public void setStatus(boolean status) {
        this.status = status;
    }
    
=======
/*
 * Click nbfs://nbhost/SystemFileSystem/Templates/Licenses/license-default.txt to change this license
 * Click nbfs://nbhost/SystemFileSystem/Templates/Classes/Class.java to edit this template
 */
package vn.edu.phoneshop.model;

/**
 *
 * @author tqsan
 */

public class Supplier {
    private int id;
    private String name;
    private String phone;
    private String email;
    private String address;

    public Supplier() {
    }

    public Supplier(int id, String name, String phone, String email, String address) {
        this.id = id;
        this.name = name;
        this.phone = phone;
        this.email = email;
        this.address = address;
    }

    // Getters và Setters (Bắt buộc để JSP hiểu được)
    public int getId() { return id; }
    public void setId(int id) { this.id = id; }

    public String getName() { return name; }
    public void setName(String name) { this.name = name; }

    public String getPhone() { return phone; }
    public void setPhone(String phone) { this.phone = phone; }

    public String getEmail() { return email; }
    public void setEmail(String email) { this.email = email; }

    public String getAddress() { return address; }
    public void setAddress(String address) { this.address = address; }
>>>>>>> e7b45a906d28bff2867a45721aa3c05ab0f4cc4d
}