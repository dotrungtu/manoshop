package com.example.manoshop.model;

public class Bill {
    String billID, productID;
    String productName,
            imgUrl, productSize, productBrand, productDes, dateOfPayment;
    String productAmount, totalPrice;

    public Bill() {

    }

    public Bill(String billID, String productID, String productName, String imgUrl, String productSize, String productBrand, String productDes, String productAmount, String totalPrice, String dateOfPayment) {
        this.billID = billID;
        this.productID = productID;
        this.productName = productName;
        this.imgUrl = imgUrl;
        this.productSize = productSize;
        this.productBrand = productBrand;
        this.productDes = productDes;
        this.productAmount = productAmount;
        this.totalPrice = totalPrice;
        this.dateOfPayment = dateOfPayment;
    }

    public String getDateOfPayment() {
        return dateOfPayment;
    }

    public void setDateOfPayment(String dateOfPayment) {
        this.dateOfPayment = dateOfPayment;
    }

    public String getBillID() {
        return billID;
    }

    public void setBillID(String billID) {
        this.billID = billID;
    }

    public String getProductID() {
        return productID;
    }

    public void setProductID(String productID) {
        this.productID = productID;
    }

    public String getProductName() {
        return productName;
    }

    public void setProductName(String productName) {
        this.productName = productName;
    }

    public String getImgUrl() {
        return imgUrl;
    }

    public void setImgUrl(String imgUrl) {
        this.imgUrl = imgUrl;
    }

    public String getProductSize() {
        return productSize;
    }

    public void setProductSize(String productSize) {
        this.productSize = productSize;
    }

    public String getProductBrand() {
        return productBrand;
    }

    public void setProductBrand(String productBrand) {
        this.productBrand = productBrand;
    }

    public String getProductDes() {
        return productDes;
    }

    public void setProductDes(String productDes) {
        this.productDes = productDes;
    }

    public String getProductAmount() {
        return productAmount;
    }

    public void setProductAmount(String productAmount) {
        this.productAmount = productAmount;
    }

    public String getTotalPrice() {
        return totalPrice;
    }

    public void setTotalPrice(String totalPrice) {
        this.totalPrice = totalPrice;
    }
}
