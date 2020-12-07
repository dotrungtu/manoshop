package com.example.manoshop.model;

public class Featured {
    String productID;
    String productName;
    int productPrice;
    String productDes;
    String imgUrl;
    String productBrand;
    int productAmount;

    public Featured()
    {

    }

    public Featured(String productID, String productName, int productPrice, String productDes, String imgUrl, String productBrand, int productAmount) {
        this.productID = productID;
        this.productName = productName;
        this.productPrice = productPrice;
        this.productDes = productDes;
        this.imgUrl = imgUrl;
        this.productBrand = productBrand;
        this.productAmount = productAmount;
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

    public int getProductPrice() {
        return productPrice;
    }

    public void setProductPrice(int productPrice) {
        this.productPrice = productPrice;
    }

    public String getProductDes() {
        return productDes;
    }

    public void setProductDes(String productDes) {
        this.productDes = productDes;
    }

    public String getImgUrl() {
        return imgUrl;
    }

    public void setImgUrl(String imgUrl) {
        this.imgUrl = imgUrl;
    }

    public String getProductBrand() {
        return productBrand;
    }

    public void setProductBrand(String productBrand) {
        this.productBrand = productBrand;
    }

    public int getProductAmount() {
        return productAmount;
    }

    public void setProductAmount(int productAmount) {
        this.productAmount = productAmount;
    }
}
