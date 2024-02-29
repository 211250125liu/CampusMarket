package com.example.myapplication.HTTP.service;

import com.example.myapplication.HTTP.model.LoginRequest;
import com.example.myapplication.HTTP.model.RegisterRequest;

import okhttp3.ResponseBody;
import retrofit2.Call;
import retrofit2.http.Body;
import retrofit2.http.GET;
import retrofit2.http.Header;
import retrofit2.http.POST;
import retrofit2.http.Query;

public interface AuthService {
    @POST("users/register")
    Call<ResponseBody> register(@Body RegisterRequest RegisterRequest, @Query("code") String code);

    @POST("users/login")
    Call<ResponseBody> login(@Body LoginRequest LoginRequest);

    @GET("users/simpleUser")
    Call<ResponseBody> getUser(@Query("userId") Long userId,@Header("authorization") String headerValue);

    @GET("users/getCode")
    Call<ResponseBody> getCode(@Query("email") String email);

}
