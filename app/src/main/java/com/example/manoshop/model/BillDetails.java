package com.example.manoshop.model;

public class BillDetails {
    String productName,
            imgUrl, productID, productSize, productBrand, productDes, productAmount, totalPrice;

    public BillDetails() {

    }

    public BillDetails(String productName, String imgUrl, String productID, String productSize, String productBrand, String productDes, String productAmount, String totalPrice) {
        this.productName = productName;
        this.imgUrl = imgUrl;
        this.productID = productID;
        this.productSize = productSize;
        this.productBrand = productBrand;
        this.productDes = productDes;
        this.productAmount = productAmount;
        this.totalPrice = totalPrice;
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

    public String getProductID() {
        return productID;
    }

    public void setProductID(String productID) {
        this.productID = productID;
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
