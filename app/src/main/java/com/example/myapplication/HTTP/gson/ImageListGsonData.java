package com.example.myapplication.HTTP.gson;

import com.google.gson.annotations.Expose;
import com.google.gson.annotations.SerializedName;

public class ImageListGsonData extends GsonData{
    @SerializedName("data")
    @Expose
    String[] data;
    public String[] getData(){
        return this.data;
    }
}
