package com.example.myapplication.HTTP.gson;

import com.google.gson.annotations.Expose;
import com.google.gson.annotations.SerializedName;

public class GsonData {
    @SerializedName("errorMsg")
    @Expose
    String errorMsg;

    public String getErrorMsg() {
        return errorMsg;
    }

    public boolean isSuccess() {
        return success;
    }

    @SerializedName("total")
    @Expose
    Long total;
    @SerializedName("success")
    @Expose
    boolean success;


}
