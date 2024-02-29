package com.example.myapplication.HTTP.model;

import com.google.gson.annotations.Expose;
import com.google.gson.annotations.SerializedName;

public class UserInfoResponse {
    @SerializedName("id")
    @Expose
    public long id;

    @SerializedName("studentId")
    @Expose
    public String studentId;

    @SerializedName("nickname")
    @Expose
    public String nickname;

    @SerializedName("photo")
    @Expose
    public String photo;

    @SerializedName("email")
    @Expose
    public String email;
}
