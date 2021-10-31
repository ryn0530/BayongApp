package com.example.bayongapp.models;

public class NavCategoryDetailedModel {
    String name;
    String img_url;
    String type;
    String price;

    public NavCategoryDetailedModel() {
    }

    public NavCategoryDetailedModel(String name, String img_url, String type, String price) {
        this.name = name;
        this.img_url = img_url;
        this.type = type;
        this.price = price;
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public String getImg_url() {
        return img_url;
    }

    public void setImg_url(String img_url) {
        this.img_url = img_url;
    }

    public String getType() {
        return type;
    }

    public void setType(String type) {
        this.type = type;
    }

    public String getPrice() {
        return price;
    }

    public void setPrice(String price) {
        this.price = price;
    }
}
