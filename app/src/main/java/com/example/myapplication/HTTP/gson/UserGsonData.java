package com.example.myapplication.HTTP.gson;

import com.example.myapplication.HTTP.model.UserInfoResponse;
import com.google.gson.annotations.Expose;
import com.google.gson.annotations.SerializedName;

public class UserGsonData extends GsonData{
    @SerializedName("data")
    @Expose
    UserInfoResponse data;
    public UserInfoResponse getData(){
        return this.data;
    }
}
