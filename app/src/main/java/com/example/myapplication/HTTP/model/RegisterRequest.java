package com.example.myapplication.HTTP.model;

import com.google.gson.annotations.SerializedName;

import okhttp3.MultipartBody;
import retrofit2.http.Part;

public class RegisterRequest {
    @SerializedName("nickname")
    private final String nickname;
    @SerializedName("studentId")
    private final String studentId;
    @SerializedName("password")
    private final String password;
    @SerializedName("email")
    private final String email;
    @SerializedName("photo")
    private final String photo;

    public RegisterRequest(String nickname, String studentId, String password, String email,String photo) {
        this.nickname = nickname;
        this.studentId = studentId;
        this.password = password;
        this.email = email;
        this.photo = photo;
    }

}
