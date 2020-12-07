package com.example.manoshop.model;

public class WishList {
    String productName,
            imgUrl, productID, productBrand, productDes;
    int productAmount, productPrice;

    public WishList() {

    }

    public WishList(String productName, int productAmount, int productPrice, String imgUrl, String productID, String productBrand, String productDes) {
        this.productName = productName;
        this.productAmount = productAmount;
        this.productPrice = productPrice;
        this.imgUrl = imgUrl;
        this.productID = productID;
        this.productBrand = productBrand;
        this.productDes = productDes;
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

    public void setProductAmount(int productAmount) {
        this.productAmount = productAmount;
    }

    public int getProductPrice() {
        return productPrice;
    }

    public void setProductPrice(int productPrice) {
        this.productPrice = productPrice;
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
