package com.example.myapplication.HTTP.service;

import okhttp3.MultipartBody;
import okhttp3.ResponseBody;
import retrofit2.Call;
import retrofit2.http.Body;
import retrofit2.http.GET;
import retrofit2.http.Multipart;
import retrofit2.http.PUT;
import retrofit2.http.Part;

public interface AvatarService {
    @PUT("users/userAvatar")
    @Multipart
    Call<ResponseBody> avatar(@Part MultipartBody.Part avatarFile);

    @GET("users/userAvatar")
    Call<ResponseBody> getAvatar(@Body String url);
}
