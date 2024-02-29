package com.example.myapplication.ui.review;

import android.os.Bundle;
import android.widget.ImageView;
import android.widget.TextView;

import androidx.appcompat.app.AppCompatActivity;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import com.example.myapplication.R;
import com.example.myapplication.HTTP.service.CommentService;
import com.example.myapplication.HTTP.gson.CommentGsonData;
import com.example.myapplication.HTTP.gson.UserGsonData;
import com.example.myapplication.HTTP.model.CommentResponse;
import com.example.myapplication.HTTP.model.UserInfoResponse;
import com.example.myapplication.HTTP.service.AuthService;
import com.example.myapplication.HTTP.Retrofit;
import com.example.myapplication.ui.data.Address;
import com.example.myapplication.HTTP.model.Review;
import com.example.myapplication.ui.data.Token;
import com.google.gson.Gson;
import com.google.gson.reflect.TypeToken;
import com.squareup.picasso.Picasso;

import java.io.IOException;
import java.lang.reflect.Type;
import java.util.ArrayList;
import java.util.List;

import okhttp3.ResponseBody;
import retrofit2.Call;
import retrofit2.Response;

public class ReviewActivity extends AppCompatActivity {
    List<Review> reviewList = new ArrayList<>();

    Long id;
    String photo = "";
    String nickname = "";
    String email = "";

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_review);

        id = getIntent().getLongExtra("id", 0);
        photo = getIntent().getStringExtra("photo");
        nickname = getIntent().getStringExtra("nickname");
        email = getIntent().getStringExtra("email");

        // 获取布局中的视图
        ImageView imageAvatar = findViewById(R.id.imageAvatar);
        TextView textUsername = findViewById(R.id.textUsername);
        TextView textEmail = findViewById(R.id.emailAddress);
        RecyclerView recyclerViewReviews = findViewById(R.id.recyclerViewReviews);

        // 设置头像和用户信息（这里需要替换为实际数据）
        Picasso.get().load(Address.getPicAddress() + Address.cut(photo)).into(imageAvatar);
//        imageAvatar.setImageResource(R.drawable.user_avatar);
        textUsername.setText(nickname);
        textEmail.setText(email);

        // 设置 RecyclerView
        LinearLayoutManager layoutManager = new LinearLayoutManager(this);
        recyclerViewReviews.setLayoutManager(layoutManager);


        getReviewList();
        // 创建 Adapter 并设置给 RecyclerView
        // 注意：需要根据实际情况创建 ReviewAdapter，并传递合适的数据
        ReviewAdapter reviewAdapter = new ReviewAdapter(reviewList, this);
        recyclerViewReviews.setAdapter(reviewAdapter);
    }

    private void getReviewList() {
        Thread thread = new Thread(new Runnable() {
            @Override
            public void run() {
                try {
                    // 执行网络请求获取订单列表（同步方式）
                    Call<ResponseBody> reviewCall = Retrofit.getRetrofitInstance().create(CommentService.class).getComment(Token.getInstance().getToken(), id);
                    Response<ResponseBody> reviewResponse = reviewCall.execute();

                    if (reviewResponse.isSuccessful()) {
                        String responseData = reviewResponse.body().string();
                        Gson gson = new Gson();
                        Type type = new TypeToken<CommentGsonData>() {
                        }.getType();
                        CommentGsonData rawData = gson.fromJson(responseData, type);

                        for (CommentResponse commentResponse : rawData.getData()) {
//                            GoodService goodService = RetrofitGood.getRetrofitInstance().create(GoodService.class);
                            AuthService authService = Retrofit.getRetrofitInstance().create(AuthService.class);
                            Call<ResponseBody> call = authService.getUser(commentResponse.getReviewer(), Token.getInstance().getToken());
                            Response<ResponseBody> userResponse = call.execute();

                            if (userResponse.isSuccessful()) {
                                String userData = userResponse.body().string();
                                Type type1 = new TypeToken<UserGsonData>() {
                                }.getType();
                                UserGsonData data1 = gson.fromJson(userData, type1);
                                UserInfoResponse commenter = data1.getData();
                                reviewList.add(new Review(commenter.nickname, commentResponse.getContent(), commentResponse.getCreateTime(), commentResponse.getPositive() ? "positive" : "negative", commenter.photo));
                            }

//                            Call<ResponseBody> goodsCall = goodService.getGoods(orderResponseItem.goodsId, Token.getInstance().getToken());
//                            Response<ResponseBody> goodsResponse = goodsCall.execute();
//
//                            if (goodsResponse.isSuccessful()) {
//                                String goodsData = goodsResponse.body().string();
//                                Type type1 = new TypeToken<GoodsGsonData>(){}.getType();
//                                GoodsGsonData data1 = gson.fromJson(goodsData,type1);
//                                GoodsType goodsType = data1.getData();
//
//                                List<String> imageList = new ArrayList<>();
//                                Collections.addAll(imageList, goodsType.getPhotosArray());

                        }
                    }

                } catch (
                        IOException e) {
                    e.printStackTrace();
                }
            }
        });

        thread.start(); // 启动新线程执行网络请求

        try {
            // 在主线程中等待新线程执行完毕
            thread.join();
        } catch (
                InterruptedException e) {
            e.printStackTrace();
        }
    }
}
