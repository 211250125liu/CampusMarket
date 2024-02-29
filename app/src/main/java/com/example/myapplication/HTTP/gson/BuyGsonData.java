package com.example.myapplication.HTTP.gson;

import com.google.gson.annotations.Expose;
import com.google.gson.annotations.SerializedName;

public class BuyGsonData extends GsonData{
    @SerializedName("data")
    @Expose
    long data;
    public long getData(){
        return this.data;
    }
}
