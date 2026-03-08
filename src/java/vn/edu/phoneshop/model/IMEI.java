package vn.edu.phoneshop.model;

import java.sql.Date;

public class IMEI {
    private int imeiId;
    private int productId;
    private String serialNumber;
    private String status;
    private Date warrantyStart;
    private Date warrantyEnd;
    private Integer orderId;

    public IMEI() {}

    public IMEI(int imeiId, int productId, String serialNumber,
                String status, Date warrantyStart,
                Date warrantyEnd, Integer orderId) {
        this.imeiId = imeiId;
        this.productId = productId;
        this.serialNumber = serialNumber;
        this.status = status;
        this.warrantyStart = warrantyStart;
        this.warrantyEnd = warrantyEnd;
        this.orderId = orderId;
    }

    // Getter & Setter

    public int getImeiId() {
        return imeiId;
    }

    public void setImeiId(int imeiId) {
        this.imeiId = imeiId;
    }

    public int getProductId() {
        return productId;
    }

    public void setProductId(int productId) {
        this.productId = productId;
    }

    public String getSerialNumber() {
        return serialNumber;
    }

    public void setSerialNumber(String serialNumber) {
        this.serialNumber = serialNumber;
    }

    public String getStatus() {
        return status;
    }

    public void setStatus(String status) {
        this.status = status;
    }

    public Date getWarrantyStart() {
        return warrantyStart;
    }

    public void setWarrantyStart(Date warrantyStart) {
        this.warrantyStart = warrantyStart;
    }

    public Date getWarrantyEnd() {
        return warrantyEnd;
    }

    public void setWarrantyEnd(Date warrantyEnd) {
        this.warrantyEnd = warrantyEnd;
    }

    public Integer getOrderId() {
        return orderId;
    }

    public void setOrderId(Integer orderId) {
        this.orderId = orderId;
    }

    public void setImeiNumber(String string) {
        this.serialNumber = string;
    }
    
}