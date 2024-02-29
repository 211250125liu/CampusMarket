package com.example.myapplication.HTTP.model;

import com.google.gson.annotations.SerializedName;

public class GoodsRequest {
    @SerializedName("photos")
    private final String[] photos;
    @SerializedName("name")
    private final String name;

    public GoodsRequest(String[] photos, String name, String description, Double price) {
        this.photos = photos;
        this.name = name;
        this.description = description;
        this.price = price;
    }

    @SerializedName("description")
    private final String description;

    @SerializedName("price")
    private final Double price;



}
