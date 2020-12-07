package com.example.manoshop.model;

public class Cart {
    String productName,
            imgUrl, productID, productSize, productBrand, productDes;
    int productAmount, productPrice, totalPrice;

    public Cart() {

    }

    public Cart(String productName, int productAmount,int totalPrice, int productPrice, String imgUrl, String productID, String productSize, String productBrand, String productDes) {
        this.productName = productName;
        this.productAmount = productAmount;
        this.productPrice = productPrice;
        this.imgUrl = imgUrl;
        this.productID = productID;
        this.productSize = productSize;
        this.productBrand = productBrand;
        this.productDes = productDes;
        this.totalPrice = totalPrice;
    }

    public int getTotalPrice() {
        return totalPrice;
    }

    public int setTotalPrice(int totalPrice) {
        this.totalPrice = totalPrice;
        return totalPrice;
    }

    public String getProductName() {
        return productName;
    }

    public void setProductName(String productName) {
        this.productName = productName;
    }

    public int getProductAmount() {
        return productAmount;
    }

    public int setProductAmount(int productAmount) {
        this.productAmount = productAmount;
        return productAmount;
    }

    public int getProductPrice() {
        return productPrice;
    }

    public int setProductPrice(int productPrice) {
        this.productPrice = productPrice;
        return productPrice;
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
}
