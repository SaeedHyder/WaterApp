package com.ingic.waterapp.entities;


public class MyOrdersChildListEntity {
    private String imgUrl;
    private String name;
    private String price;
    private String quantity;

    public MyOrdersChildListEntity(String imgUrl, String title, String price, String quantity) {
        this.imgUrl = imgUrl;
        this.name = title;
        this.price = price;
        this.quantity = quantity;
    }

    public String getImgUrl() {
        return imgUrl;
    }

    public void setImgUrl(String imgUrl) {
        this.imgUrl = imgUrl;
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public String getPrice() {
        return price;
    }

    public void setPrice(String price) {
        this.price = price;
    }

    public String getQuantity() {
        return quantity;
    }

    public void setQuantity(String quantity) {
        this.quantity = quantity;
    }
}
