package com.example.myapplication.HTTP.gson;

import com.example.myapplication.HTTP.model.GoodsType;
import com.google.gson.annotations.Expose;
import com.google.gson.annotations.SerializedName;


public class GoodsGsonData extends GsonData{
    @SerializedName("data")
    @Expose
    GoodsType data;
    public GoodsType getData(){
        return this.data;
    }
}
