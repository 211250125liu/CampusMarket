package com.example.myapplication.HTTP.gson;

import com.example.myapplication.HTTP.model.OrderResponse;
import com.google.gson.annotations.Expose;
import com.google.gson.annotations.SerializedName;

import java.util.List;

public class OrderGsonData extends GsonData{
    @SerializedName("data")
    @Expose
    List<OrderResponse> data;
    public List<OrderResponse> getData(){
        return this.data;
    }
}
