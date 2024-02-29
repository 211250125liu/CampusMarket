package com.example.myapplication.HTTP.model;

import com.google.gson.annotations.SerializedName;

public class OrderRequest {
    @SerializedName("goodsId")
    public Long goodsId;
    public OrderRequest(long goodsId){
        this.goodsId = goodsId;
    }
}
