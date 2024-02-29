package com.example.myapplication.HTTP.service;

import com.example.myapplication.HTTP.model.CommentRequest;

import okhttp3.ResponseBody;
import retrofit2.http.Body;
import retrofit2.http.GET;
import retrofit2.http.Header;
import retrofit2.http.POST;
import retrofit2.Call;
import retrofit2.http.Path;

public interface CommentService {
    @POST("/comment")
    Call<ResponseBody> addComment(@Header("authorization") String token, @Body CommentRequest commentRequest);

    @GET("/comment/{seller_id}")
    Call<ResponseBody> getComment(@Header("authorization") String token, @Path("seller_id") Long seller_id);
}
