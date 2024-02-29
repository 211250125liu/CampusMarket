package com.example.myapplication.HTTP.gson;

import com.example.myapplication.HTTP.model.GoodsType;
import com.google.gson.annotations.Expose;
import com.google.gson.annotations.SerializedName;

import java.util.List;

public class GoodsListGsonData extends GsonData{
    @SerializedName("data")
    @Expose
    List<GoodsType> data;
    public List<GoodsType> getData(){
        return this.data;
    }
}
