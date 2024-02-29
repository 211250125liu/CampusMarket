package com.example.myapplication.HTTP.model;

public class Commodity {
    String PictureUrl;
    String Name;

    public Commodity(String pictureUrl, String name, double price, String introduce) {
        PictureUrl = pictureUrl;
        Name = name;
        this.price = price;
        Introduce = introduce;
    }

    double price;
    String Introduce;


    public String getPictureUrl() {
        return PictureUrl;
    }
    public String getIntroduce() {
        return Introduce;
    }

    public String getName() {
        return Name;
    }

    public double getPrice() {
        return price;
    }
}
