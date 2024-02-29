package com.example.myapplication.ui.user.orderHistory;

import android.app.Activity;
import android.content.Intent;
import android.content.SharedPreferences;
import android.os.Bundle;

import androidx.annotation.Nullable;
import androidx.appcompat.app.AppCompatActivity;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import com.example.myapplication.R;
import com.example.myapplication.HTTP.gson.GoodsGsonData;
import com.example.myapplication.HTTP.gson.OrderGsonData;
import com.example.myapplication.HTTP.model.OrderResponse;
import com.example.myapplication.HTTP.service.GoodService;
import com.example.myapplication.HTTP.service.OrderService;
import com.example.myapplication.HTTP.Retrofit;
import com.example.myapplication.HTTP.model.GoodsType;
import com.example.myapplication.ui.data.Token;
import com.google.gson.Gson;
import com.google.gson.reflect.TypeToken;

import java.io.IOException;
import java.lang.reflect.Type;
import java.util.ArrayList;
import java.util.Collections;
import java.util.HashSet;
import java.util.List;
import java.util.Set;

import okhttp3.ResponseBody;
import retrofit2.Call;
import retrofit2.Response;

public class OrderHistoryActivity extends AppCompatActivity {
    OrderGsonData orderResponseList;

    ArrayList<OrderHistoryItem> adapterList = new ArrayList<>();

    RecyclerView recyclerView;
    RecyclerView.Adapter<OrderHistoryViewHolder> adapter;

    private static final String SHARED_PREF_NAME = "OrderHistorySharedPref";

    private void saveDataToSharedPreferences() {
        SharedPreferences sharedPreferences = getSharedPreferences(SHARED_PREF_NAME, MODE_PRIVATE);
        SharedPreferences.Editor editor = sharedPreferences.edit();
        Gson gson = new Gson();
        String json = sharedPreferences.getString("adapterList", "");

        if (!json.isEmpty()) {
            Type type = new TypeToken<ArrayList<OrderHistoryItem>>() {
            }.getType();
            ArrayList<OrderHistoryItem> list = gson.fromJson(json, type);

            for(int i = 0; i < list.size(); i++){
                    adapterList.add(list.get(i));
            }
        }

        Set<Long> idSet = new HashSet<>();
        ArrayList<OrderHistoryItem> uniqueList = new ArrayList<>();
        for (OrderHistoryItem item : adapterList) {
            if (idSet.add(item.orderResponse.id)) {
                uniqueList.add(item);
            }
        }

        // 将 adapterList 转换成 JSON 字符串并保存到 SharedPreferences
        String jsonR = gson.toJson(uniqueList);
        editor.putString("adapterList", jsonR);
        editor.apply();
    }

    private void loadDataFromSharedPreferences() {
        SharedPreferences sharedPreferences = getSharedPreferences(SHARED_PREF_NAME, MODE_PRIVATE);
        Gson gson = new Gson();
        String json = sharedPreferences.getString("adapterList", "");

        if (!json.isEmpty()) {
            Type type = new TypeToken<ArrayList<OrderHistoryItem>>() {
            }.getType();
            ArrayList<OrderHistoryItem> list = gson.fromJson(json, type);


            for (int i = 0; i < adapterList.size(); i++) {
                for (int j = 0; j < list.size(); j++) {
                    if (adapterList.get(i).orderResponse.id == list.get(j).orderResponse.id) {
                        adapterList.get(i).commentContent = list.get(j).commentContent;
                        break;
                    }
                }
            }

            // 更新 RecyclerView 的 adapterList
            adapter.notifyDataSetChanged();
        }
    }

    @Override
    protected void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_order_history);

        getAdapterList();

        findViewById(R.id.Return).setOnClickListener(view -> {
            this.finish();
        });

        recyclerView = findViewById(R.id.HistoryRecycle);
        adapter = new OrderHistoryAdapter(adapterList, this);
        recyclerView.setAdapter(adapter);
        recyclerView.setLayoutManager(new LinearLayoutManager(this, LinearLayoutManager.VERTICAL, false));

        loadDataFromSharedPreferences();
    }

    private void getAdapterList() {

        // 创建一个新的线程执行网络请求
        Thread thread = new Thread(new Runnable() {
            @Override
            public void run() {
                try {
                    // 执行网络请求获取订单列表（同步方式）
                    Call<ResponseBody> orderCall = Retrofit.getRetrofitInstance().create(OrderService.class).getAllOrders(Token.getInstance().getToken());
                    Response<ResponseBody> orderResponse = orderCall.execute();

                    if (orderResponse.isSuccessful()) {
                        String responseData = orderResponse.body().string();
                        Gson gson = new Gson();
                        Type type = new TypeToken<OrderGsonData>() {
                        }.getType();
                        orderResponseList = gson.fromJson(responseData, type);

                        // 循环遍历订单列表，同步获取商品信息
                        for (OrderResponse orderResponseItem : orderResponseList.getData()) {
                            GoodService goodService = Retrofit.getRetrofitInstance().create(GoodService.class);
                            Call<ResponseBody> goodsCall = goodService.getGoods(orderResponseItem.goodsId, Token.getInstance().getToken());
                            Response<ResponseBody> goodsResponse = goodsCall.execute();

                            if (goodsResponse.isSuccessful()) {
                                String goodsData = goodsResponse.body().string();
                                Type type1 = new TypeToken<GoodsGsonData>() {
                                }.getType();
                                GoodsGsonData data1 = gson.fromJson(goodsData, type1);
                                GoodsType goodsType = data1.getData();

                                List<String> imageList = new ArrayList<>();
                                Collections.addAll(imageList, goodsType.getPhotosArray());

                                adapterList.add(new OrderHistoryItem(imageList.get(0), goodsType.getDescription(), goodsType.getName(), goodsType.getPrice(), orderResponseItem, goodsType.getUpdateTime()));
                            } else {
                                // 处理请求商品信息失败的情况
                            }
                        }
                    } else {
                        // 处理请求订单列表失败的情况
                    }
                } catch (IOException e) {
                    e.printStackTrace();
                }
            }
        });

        thread.start(); // 启动新线程执行网络请求

        try {
            // 在主线程中等待新线程执行完毕
            thread.join();
        } catch (InterruptedException e) {
            e.printStackTrace();
        }
    }

    // 在 OrderHistoryActivity 中重写 onActivityResult 方法
    @Override
    protected void onActivityResult(int requestCode, int resultCode, @Nullable Intent data) {
        super.onActivityResult(requestCode, resultCode, data);
        if (requestCode == 48 && resultCode == Activity.RESULT_OK && data != null) {
            String commentText = data.getStringExtra("commentText");
            long orderId = data.getLongExtra("orderId", 0);
            if (commentText != null) {
                // 找到需要更新评论内容的项，并更新评论内容
                for (int i = 0; i < adapterList.size(); i++) {
                    if (adapterList.get(i).orderResponse.id == orderId) {
                        adapterList.get(i).commentContent = commentText;
                        break; // 找到对应项后立即退出循环
                    }
                }
                // 通知 Adapter 数据集发生更改
                adapter.notifyDataSetChanged();
            }
        }
    }

    @Override
    protected void onDestroy() {
        super.onDestroy();
        saveDataToSharedPreferences();
    }
}
