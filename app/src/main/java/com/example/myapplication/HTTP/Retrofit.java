package com.example.myapplication.HTTP;

import android.util.Log;

import com.example.myapplication.ui.data.Address;

import java.io.IOException;

import okhttp3.Interceptor;
import okhttp3.OkHttpClient;
import okhttp3.Request;
import okhttp3.Response;
import okhttp3.logging.HttpLoggingInterceptor;
import okio.Buffer;
import retrofit2.converter.gson.GsonConverterFactory;

public class Retrofit {

    private static retrofit2.Retrofit retrofit;
    private static retrofit2.Retrofit retrofit2;
    private static retrofit2.Retrofit retrofit3;
    private static final String BASE_URL = Address.getAddress();
    private static final String BASE_URL2 = Address.getPicAddress();
    private static final String BASE_URL3 = "http://10.58.0.2:8000/";

    public static retrofit2.Retrofit getRetrofitInstance3(){
        if (retrofit3 == null) {
            retrofit3 = new retrofit2.Retrofit.Builder()
                    .baseUrl(BASE_URL3)
                    .addConverterFactory(GsonConverterFactory.create())
                    .build();
        }
        return retrofit3;
    }

    //8082
    public static retrofit2.Retrofit getRetrofitInstance2(){
        if (retrofit2 == null) {
            retrofit2 = new retrofit2.Retrofit.Builder()
                    .baseUrl(BASE_URL2)
                    .addConverterFactory(GsonConverterFactory.create())
                    .build();
        }
        return retrofit2;
    }
    public static retrofit2.Retrofit getRetrofitInstance() {

        HttpLoggingInterceptor loggingInterceptor = new HttpLoggingInterceptor(message -> {
            // 使用 Log 输出拦截器捕获的消息
            Log.d("OkHttp", message); // 这里的 "OkHttp" 是日志标签，你可以自定义标签名称
        });
        loggingInterceptor.setLevel(HttpLoggingInterceptor.Level.BODY); // 设置日志级别为BODY，记录请求和响应的详细信息

//         创建 OkHttp 客户端并添加日志拦截器
        OkHttpClient.Builder httpClient = new OkHttpClient.Builder();
        httpClient.addInterceptor(chain -> {
            Request request = chain.request();

            // 打印请求头部信息
            Log.d("RequestHeaders", request.headers().toString());

            // 打印请求消息体（仅限 POST、PUT 等带有请求体的请求）
            if (request.body() != null) {
                Buffer buffer = new Buffer();
                request.body().writeTo(buffer);
                Log.d("RequestBody", buffer.readUtf8());
            }

            Response response = chain.proceed(request);

            // 打印响应头部信息
            Log.d("ResponseHeaders", response.headers().toString());

            // 打印响应消息体
            if (response.body() != null) {
                String responseBody = response.body().string();
                Log.d("ResponseBody", responseBody);

                // 注意：由于 OkHttp 的 ResponseBody 只能读取一次，所以打印后如果需要使用，需要重新构建 Response
                response = response.newBuilder()
                        .body(okhttp3.ResponseBody.create(response.body().contentType(), responseBody))
                        .build();
            }
            return response;
        });
        httpClient.addInterceptor(loggingInterceptor); // 添加日志拦截器

        if (retrofit == null) {
            retrofit = new retrofit2.Retrofit.Builder()
                    .baseUrl(BASE_URL)
                    .addConverterFactory(GsonConverterFactory.create())
                    .build();
        }
        return retrofit;
    }
}
