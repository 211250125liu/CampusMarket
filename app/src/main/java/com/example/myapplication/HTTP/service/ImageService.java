package com.example.myapplication.HTTP.service;

import java.util.List;

import okhttp3.MultipartBody;
import okhttp3.ResponseBody;
import retrofit2.Call;

import retrofit2.http.Header;
import retrofit2.http.Multipart;
import retrofit2.http.PUT;
import retrofit2.http.Part;


public interface ImageService {
    @Multipart
    @PUT("image/upload")
    Call<ResponseBody> uploadImage(@Part List<MultipartBody.Part> files,@Header("authorization") String headerValue);

    @Multipart
    @PUT("image/upload")
    Call<ResponseBody> uploadImage2(@Part List<MultipartBody.Part> files);

}
