package com.example.myapplication.HTTP.model;

import com.google.gson.annotations.SerializedName;

public class LoginRequest {
    @SerializedName("studentId")
    private final String studentId;
    @SerializedName("password")
    private final String password;

    public LoginRequest(String studentId, String password) {
        this.studentId = studentId;
        this.password = password;
    }
}
