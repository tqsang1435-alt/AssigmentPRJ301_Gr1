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
    
}