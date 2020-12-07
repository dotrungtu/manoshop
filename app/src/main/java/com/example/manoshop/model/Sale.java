package com.example.manoshop.model;

public class Sale {
    int productAmount, productNewPrice, productPrice;
    String productName, productBrand, productDes, imgUrl, productID;

    public Sale() {

    }

    public Sale(String productID, int productAmount, int productNewPrice, int productPrice, String productName, String productBrand, String productDes, String imgUrl) {
        this.productID = productID;
        this.productAmount = productAmount;
        this.productNewPrice = productNewPrice;
        this.productPrice = productPrice;
        this.productName = productName;
        this.productBrand = productBrand;

        this.productDes = productDes;
        this.imgUrl = imgUrl;
    }

    public String getProductID() {
        return productID;
    }

    public void setProductID(String productID) {
        this.productID = productID;
    }

    public int getProductAmount() {
        return productAmount;
    }

    public void setProductAmount(int productAmount) {
        this.productAmount = productAmount;
    }

    public int getProductNewPrice() {
        return productNewPrice;
    }

    public void setProductNewPrice(int productNewPrice) {
        this.productNewPrice = productNewPrice;
    }

    public int getProductPrice() {
        return productPrice;
    }

    public void setProductPrice(int productPrice) {
        this.productPrice = productPrice;
    }

    public String getProductName() {
        return productName;
    }

    public void setProductName(String productName) {
        this.productName = productName;
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

    public String getImgUrl() {
        return imgUrl;
    }

    public void setImgUrl(String imgUrl) {
        this.imgUrl = imgUrl;
    }
}
