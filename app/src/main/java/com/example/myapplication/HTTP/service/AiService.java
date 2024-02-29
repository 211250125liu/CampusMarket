package com.example.myapplication.HTTP.service;

import com.example.myapplication.HTTP.model.AiRequest;

import okhttp3.ResponseBody;
import retrofit2.Call;
import retrofit2.http.Body;
import retrofit2.http.Header;
import retrofit2.http.POST;

public interface AiService {

    @POST("v1/chat/completions")
    Call<ResponseBody> ask(@Body AiRequest aiRequest);
}
