package com.example.myapplication.HTTP.service;

import com.example.myapplication.HTTP.model.GoodsRequest;

import okhttp3.ResponseBody;
import retrofit2.Call;
import retrofit2.http.Body;
import retrofit2.http.GET;
import retrofit2.http.Header;
import retrofit2.http.POST;
import retrofit2.http.Path;

public interface GoodService {
    @GET("goods/defaultOrder/{page}")
    Call<ResponseBody> defaultOrder(@Path("page") int page,@Header("authorization") String headerValue);

    @POST("goods")
    Call<ResponseBody> postGoods(@Body GoodsRequest goodsRequest,@Header("authorization") String headerValue);

    @GET("goods/search/{queryString}/{page}")
    Call<ResponseBody> search(@Path("queryString") String queryString, @Path("page") int page,@Header("authorization") String headerValue);

    @GET("goods/{id}")
    Call<ResponseBody> getGoods(@Path("id") Long id,@Header("authorization") String headerValue);
}
