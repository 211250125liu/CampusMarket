package com.example.myapplication.ui.productDetails;


import android.content.Intent;
import android.os.Bundle;
import android.text.method.ScrollingMovementMethod;
import android.util.Log;
import android.view.View;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.TextView;
import android.widget.Toast;

import androidx.appcompat.app.AppCompatActivity;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import com.example.myapplication.HTTP.gson.AiGsonData;
import com.example.myapplication.HTTP.gson.BuyGsonData;
import com.example.myapplication.HTTP.gson.GoodsGsonData;
import com.example.myapplication.HTTP.gson.UserGsonData;
import com.example.myapplication.HTTP.model.AiRequest;
import com.example.myapplication.HTTP.model.GoodsType;
import com.example.myapplication.HTTP.model.Message;
import com.example.myapplication.HTTP.model.OrderRequest;
import com.example.myapplication.HTTP.model.UserInfoResponse;
import com.example.myapplication.HTTP.service.AiService;
import com.example.myapplication.HTTP.service.AuthService;
import com.example.myapplication.HTTP.service.GoodService;
import com.example.myapplication.HTTP.service.OrderService;
import com.example.myapplication.R;
import com.example.myapplication.HTTP.Retrofit;
import com.example.myapplication.ui.data.Address;
import com.example.myapplication.ui.data.Token;
import com.example.myapplication.ui.pay.PaymentActivity;
import com.example.myapplication.ui.review.ReviewActivity;
import com.google.gson.Gson;
import com.google.gson.reflect.TypeToken;
import com.squareup.picasso.Picasso;

import java.io.IOException;
import java.lang.reflect.Type;
import java.util.ArrayList;
import java.util.Collections;
import java.util.List;

import okhttp3.ResponseBody;
import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;

public class ProductDetailActivity extends AppCompatActivity {

    GoodsType receivedGoodsType;
    UserInfoResponse user;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        Intent thisIntent = getIntent();
        if (thisIntent != null) {
            receivedGoodsType = thisIntent.getParcelableExtra("goodsType");
            // 使用 receivedGoodsType 进行操作
        }

        setContentView(R.layout.activity_product_detail);

        // 获取布局中的视图
        ImageView imageAvatar = findViewById(R.id.imageAvatar);
        TextView textUsername = findViewById(R.id.textUsername);
        TextView textPrice = findViewById(R.id.textPrice);
        TextView textProductName = findViewById(R.id.textProductName);
        TextView textDescription = findViewById(R.id.textDescription);
        RecyclerView recyclerViewImages = findViewById(R.id.recyclerViewImages);
        Button btnBuy = findViewById(R.id.btnBuy);
        TextView Ai = findViewById(R.id.AIRES);

        Ai.setMovementMethod(ScrollingMovementMethod.getInstance());

        List<Message> messages = new ArrayList<>();
        messages.add(new Message("user", "我现在在网上购物，这是商品描述：" + receivedGoodsType.getDescription() + "\n" +
                "这是商品价格" + receivedGoodsType.getPrice() +"\n"+
                "购买该商品划算吗，请给出你的分析。" +
                "尽可能给出分析，"+
                "如果条件不足够你分析就输出’挺划算的，推荐购买‘"));

        Call<ResponseBody> AiCall = Retrofit.getRetrofitInstance3().create(AiService.class).ask(new AiRequest("Qwen-14B", 0.7, messages, 512, null, 1, 1.0));

        AiCall.enqueue(new Callback<ResponseBody>() {
            @Override
            public void onResponse(Call<ResponseBody> call, Response<ResponseBody> response) {
                try {
                    String responseData = response.body().string();

                    Gson gson = new Gson();
                    Type type1 = new TypeToken<AiGsonData>() {
                    }.getType();
                    AiGsonData aiGsonData = gson.fromJson(responseData, type1);

                    Ai.setText(aiGsonData.getMessage());
                } catch (Exception e) {

                }
            }

            @Override
            public void onFailure(Call<ResponseBody> call, Throwable t) {
                Toast.makeText(ProductDetailActivity.this, "Ai请求错误", Toast.LENGTH_SHORT).show();
            }
        });


        // 设置头像和用户名
        AuthService authService = Retrofit.getRetrofitInstance().create(AuthService.class);
        Call<ResponseBody> call = authService.getUser(receivedGoodsType.getUserId(), Token.getInstance().getToken());
        call.enqueue(new Callback<ResponseBody>() {
            @Override
            public void onResponse(Call<ResponseBody> call, Response<ResponseBody> response) {
                if (response.isSuccessful()) {

                    String responseData;
                    try {
                        responseData = response.body().string();
                    } catch (IOException e) {
                        throw new RuntimeException(e);
                    }
                    Gson gson = new Gson();
//                    Type type2 = new TypeToken<User>(){}.getType();
//                    User user = gson.fromJson(responseData, type2);
                    Type type1 = new TypeToken<UserGsonData>() {
                    }.getType();
                    UserGsonData userGsonData = gson.fromJson(responseData, type1);
//                    Type type2 = new TypeToken<User>(){}.getType();
                    user = userGsonData.getData();

                    if (user != null) {
                        String imageUrl = user.photo;
                        Log.d("useruser", imageUrl);
                        String username = user.nickname;

                        // Update the ImageView and TextView here
                        Picasso.get().load(Address.getPicAddress() + Address.cut(imageUrl)).into(imageAvatar);
                        textUsername.setText(username);
                    } else {
                        Toast.makeText(ProductDetailActivity.this, "无法解析服务器响应", Toast.LENGTH_SHORT).show();
                    }
                } else {
                    Toast.makeText(ProductDetailActivity.this, "后端错误", Toast.LENGTH_SHORT).show();
                }
            }

            @Override
            public void onFailure(Call<ResponseBody> call, Throwable t) {
                Toast.makeText(ProductDetailActivity.this, "请求失败", Toast.LENGTH_SHORT).show();
            }
        });


        // 设置商品信息
        textPrice.setText("￥" + receivedGoodsType.getPrice());
        textProductName.setText(receivedGoodsType.getName());
        textDescription.setText(receivedGoodsType.getDescription());

        // 设置 RecyclerView
        LinearLayoutManager layoutManager = new LinearLayoutManager(this, LinearLayoutManager.HORIZONTAL, false);
        recyclerViewImages.setLayoutManager(layoutManager);

        // 创建 Adapter，并设置给 RecyclerView
        GoodService goodService = Retrofit.getRetrofitInstance().create(GoodService.class);
        Call<ResponseBody> call2 = goodService.getGoods(receivedGoodsType.getId(), Token.getInstance().getToken());
        call2.enqueue(new Callback<ResponseBody>() {
            @Override
            public void onResponse(Call<ResponseBody> call, Response<ResponseBody> response) {
                if (response.isSuccessful()) {
                    try {
                        String responseData = response.body().string();
                        Gson gson = new Gson();
                        Type type = new TypeToken<GoodsGsonData>() {
                        }.getType();
                        GoodsGsonData goodsGsonData = gson.fromJson(responseData, type);
//                        Log.d("thisString", goodsType.getPhotos());
                        List<String> imageList = new ArrayList<>();
                        Collections.addAll(imageList, goodsGsonData.getData().getPhotosArray());

                        ImageAdapter imageAdapter = new ImageAdapter(ProductDetailActivity.this, imageList);
                        imageAdapter.setHorizontalLayoutManager(recyclerViewImages);
                        recyclerViewImages.setAdapter(imageAdapter);
//                        Toast.makeText(ProductDetailActivity.this,"图片请求成功",Toast.LENGTH_SHORT).show();
                    } catch (IOException e) {
                        throw new RuntimeException(e);
                    }
                } else {
                    Toast.makeText(ProductDetailActivity.this, "后端失败", Toast.LENGTH_SHORT).show();
                }
            }

            @Override
            public void onFailure(Call<ResponseBody> call, Throwable t) {
                Toast.makeText(ProductDetailActivity.this, "请求失败", Toast.LENGTH_SHORT).show();
            }
        });


        // 设置购买按钮点击事件
        btnBuy.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Call<ResponseBody> orderCall = Retrofit.getRetrofitInstance().create(OrderService.class).createOrder(Token.getInstance().getToken(), new OrderRequest(receivedGoodsType.getId()));
                orderCall.enqueue(new Callback<ResponseBody>() {
                    @Override
                    public void onResponse(Call<ResponseBody> call, Response<ResponseBody> response) {
                        try {
                            String result = response.body().string();
                            Gson gson = new Gson();
                            Type type1 = new TypeToken<BuyGsonData>() {
                            }.getType();

                            BuyGsonData data = gson.fromJson(result, type1);

                            Log.d("sell", String.valueOf(data.getData()));

                            Intent intent = new Intent(ProductDetailActivity.this, PaymentActivity.class);
                            intent.putExtra("price", receivedGoodsType.getPrice());
                            intent.putExtra("GoodsId", receivedGoodsType.getId());
                            intent.putExtra("OrderId", data.getData());
                            startActivity(intent);
                        } catch (Exception e) {
                            Toast.makeText(ProductDetailActivity.this, "已售出", Toast.LENGTH_SHORT).show();
                        }
                    }

                    @Override
                    public void onFailure(Call<ResponseBody> call, Throwable t) {
                        Toast.makeText(ProductDetailActivity.this, "请求失败", Toast.LENGTH_SHORT).show();
                    }
                });
                // 处理点击事件，跳转到新的界面
                // 传递一些数据给新的界面
            }
        });

        imageAvatar.setOnClickListener(v -> {
            Intent intent = new Intent(ProductDetailActivity.this, ReviewActivity.class);
            intent.putExtra("nickname", user.nickname);
            intent.putExtra("email", user.email);
            intent.putExtra("photo", user.photo);
            intent.putExtra("id", user.id);
            startActivity(intent);
        });
    }

}

