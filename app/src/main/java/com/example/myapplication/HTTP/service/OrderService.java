package com.example.myapplication.HTTP.service;

import com.example.myapplication.HTTP.model.OrderRequest;

import okhttp3.ResponseBody;
import retrofit2.Call;
import retrofit2.http.Body;
import retrofit2.http.DELETE;
import retrofit2.http.GET;
import retrofit2.http.Header;
import retrofit2.http.POST;
import retrofit2.http.Path;

public interface OrderService {
    @POST("order/buy")
    Call<ResponseBody> createOrder(@Header("authorization") String token, @Body OrderRequest orderRequest);

    @POST("order/pay/{good_id}")
    Call<ResponseBody> payOrder(@Header("authorization") String token, @Path("good_id") long good_id);

    @DELETE("order/cancel/{good_id}")
    Call<ResponseBody> cancelOrder(@Header("authorization") String token, @Path("good_id") long good_id);

    @GET("order/get")
    Call<ResponseBody> getAllOrders(@Header("authorization") String token);
}
