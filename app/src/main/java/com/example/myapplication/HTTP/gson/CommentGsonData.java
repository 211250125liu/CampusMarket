package com.example.myapplication.HTTP.gson;

import com.example.myapplication.HTTP.model.CommentResponse;
import com.google.gson.annotations.Expose;
import com.google.gson.annotations.SerializedName;

import java.util.List;

public class CommentGsonData extends GsonData {
    @SerializedName("data")
    @Expose
    List<CommentResponse> data;

    public List<CommentResponse> getData(){
        return this.data;
    }
}
