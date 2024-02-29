package com.example.myapplication.HTTP.service;

import okhttp3.ResponseBody;
import retrofit2.Call;
import retrofit2.http.GET;
import retrofit2.http.Header;
import retrofit2.http.PUT;
import retrofit2.http.Query;

public interface UserInfoService {
    @GET("users/myInformation")
    Call<ResponseBody> getUserInfo(@Header("authorization") String token);

    @PUT("users/userInformation")
    Call<ResponseBody> putUserInfo(@Header("authorization") String token, @Query("nickname") String nickname, @Query("email") String email);
}
