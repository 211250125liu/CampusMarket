package com.example.myapplication.ui.search;

import static android.nfc.tech.MifareUltralight.PAGE_SIZE;

import android.util.Log;
import android.view.GestureDetector;

import android.content.Intent;
import android.os.Bundle;
import android.view.MotionEvent;
import android.view.View;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import com.example.myapplication.R;
import com.example.myapplication.HTTP.gson.GoodsListGsonData;
import com.example.myapplication.HTTP.service.GoodService;
import com.example.myapplication.HTTP.Retrofit;
import com.example.myapplication.HTTP.model.Commodity;
import com.example.myapplication.HTTP.model.GoodsType;
import com.example.myapplication.ui.data.Token;
import com.example.myapplication.ui.productDetails.ProductDetailActivity;
import com.google.gson.Gson;
import com.google.gson.reflect.TypeToken;

import java.io.IOException;
import java.lang.reflect.Type;
import java.util.ArrayList;
import java.util.List;

import okhttp3.ResponseBody;
import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;

public class SearchActivity extends AppCompatActivity {
    private EditText editTextSearch;
    private RecyclerView recyclerViewProducts;

    private SearchCommodityAdapter searchCommodityAdapter;

    private static int pageNum = 1;

    private List<GoodsType> goods = new ArrayList<>();

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        pageNum = 1;
        setContentView(R.layout.activity_search);

        editTextSearch = findViewById(R.id.editTextSearch);
        ImageView imageViewSearch = findViewById(R.id.imageViewSearch);
        recyclerViewProducts = findViewById(R.id.recyclerViewProducts);

        // 设置RecyclerView的布局管理器和适配器（需要根据你的数据结构来实现）
        recyclerViewProducts.setLayoutManager(new LinearLayoutManager(this));
        searchCommodityAdapter = new SearchCommodityAdapter();
        // 设置搜索按钮点击事件
        imageViewSearch.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                performSearch();
            }
        });
        recyclerViewProducts.addOnItemTouchListener(new RecyclerView.OnItemTouchListener() {
            GestureDetector gestureDetector = new GestureDetector(SearchActivity.this, new GestureDetector.SimpleOnGestureListener() {
                @Override
                public boolean onSingleTapUp(MotionEvent e) {
                    return true;
                }
            });

            @Override
            public boolean onInterceptTouchEvent(@NonNull RecyclerView rv, @NonNull MotionEvent e) {
                View child = rv.findChildViewUnder(e.getX(), e.getY());
                if (child != null && gestureDetector.onTouchEvent(e)) {
                    int position = rv.getChildAdapterPosition(child);
                    List<GoodsType> goodsTypeList = getGoodsTypeList();
                    GoodsType clickedGoodsType = goodsTypeList.get(position);
                    // 处理点击事件，跳转到新的界面
                    Intent intent = new Intent(SearchActivity.this, ProductDetailActivity.class);
                    // 传递一些数据给新的界面，例如点击项的位置
                    intent.putExtra("goodsType", clickedGoodsType);
                    startActivity(intent);
                    return true;
                }
                return false;
            }

            @Override
            public void onTouchEvent(@NonNull RecyclerView rv, @NonNull MotionEvent e) {
                // 暂时不需要实现
            }

            @Override
            public void onRequestDisallowInterceptTouchEvent(boolean disallowIntercept) {
                // 暂时不需要实现
            }
        });
        recyclerViewProducts.addOnScrollListener(new RecyclerView.OnScrollListener() {
            @Override
            public void onScrolled(@NonNull RecyclerView recyclerView, int dx, int dy) {
                super.onScrolled(recyclerView, dx, dy);
                // 在这里检测是否已经滚动到底部
                LinearLayoutManager layoutManager = (LinearLayoutManager) recyclerView.getLayoutManager();
                int visibleItemCount = layoutManager.getChildCount();
                int totalItemCount = layoutManager.getItemCount();
                int firstVisibleItemPosition = layoutManager.findFirstVisibleItemPosition();

                if ((visibleItemCount + firstVisibleItemPosition) >= totalItemCount
                             && firstVisibleItemPosition >= 0
                        && totalItemCount >= PAGE_SIZE) {
                    // 滚动到底部，触发加载新数据的操作
                    loadMoreData();
                }
            }
        });
    }

    private List<GoodsType> getGoodsTypeList(){
        return goods;
    }

    private boolean isLastItemDisplaying() {
        LinearLayoutManager layoutManager = (LinearLayoutManager) recyclerViewProducts.getLayoutManager();
        int visibleItemCount = layoutManager.getChildCount();
        int totalItemCount = layoutManager.getItemCount();
        int firstVisibleItemPosition = layoutManager.findFirstVisibleItemPosition();

        return (visibleItemCount + firstVisibleItemPosition) >= totalItemCount
                && firstVisibleItemPosition >= 0
                && totalItemCount >= PAGE_SIZE;
    }

    private void loadMoreData() {
        if (isLastItemDisplaying()) {
            performSearch();
        }
    }


    private void performSearch() {
        //这里通过网络获取新数据后更新

        // 在进行新搜索之前清除先前的数据
        goods.clear();

        // 重置页码为第一页
        pageNum = 1;

        GoodService goodService = Retrofit.getRetrofitInstance().create(GoodService.class);

        Call<ResponseBody> call = goodService.search(editTextSearch.getText().toString(),pageNum, Token.getInstance().getToken());
        call.enqueue(new Callback<ResponseBody>() {
            @Override
            public void onResponse(Call<ResponseBody> call, Response<ResponseBody> response) {
                if (response.isSuccessful()) {
                    try {
                        pageNum++;
                        String responseData = response.body().string();
                        // 处理 responseData
                        Gson gson = new Gson();
                        Type type = new TypeToken<GoodsListGsonData>(){}.getType();
                        GoodsListGsonData data = gson.fromJson(responseData, type);
                        for(GoodsType goodsType : data.getData()){
                            boolean tag = false;
                            for(GoodsType everyGood : goods){
                                if(goodsType.getId() == everyGood.getId()){
                                    tag = true;
                                    break;
                                }
                            }
                            if(!tag) {
                                goods.add(goodsType);
                            }
                        }
                        for (GoodsType goodsType : goods) {
                            Log.d("goods", goodsType.toString());
                        }

                        // 处理数据
                        List<Commodity> productList = new ArrayList<>();
                        for (GoodsType goodsType : goods) {
                            productList.add(new Commodity(goodsType.getPhotosArray()[0],
                                    goodsType.getName(), goodsType.getPrice(), goodsType.getDescription()));
                        }
                        if(productList.size() == 0){
                            Toast.makeText(SearchActivity.this,"没有类似商品",Toast.LENGTH_SHORT).show();
                        }
                        // 更新 RecyclerView 数据
                        updateRecyclerView(productList);
                    } catch (IOException e) {
                        e.printStackTrace();
                        // 处理异常情况
                        Toast.makeText(SearchActivity.this,"请求异常",Toast.LENGTH_SHORT).show();
                    }
                } else {
                    // 处理请求失败情况
                    Toast.makeText(SearchActivity.this,"后端错误",Toast.LENGTH_SHORT).show();
                }
            }

            @Override
            public void onFailure(Call<ResponseBody> call, Throwable t) {
                // 处理请求失败情况
                Toast.makeText(SearchActivity.this,"请求失败",Toast.LENGTH_SHORT).show();
            }
        });
    }

    private void updateRecyclerView(List<Commodity> productList) {
        // 在主线程更新 RecyclerView 数据
        this.runOnUiThread(() -> {
            int currentSize = searchCommodityAdapter.getItemCount();
            searchCommodityAdapter.setCommodityList(productList);
            recyclerViewProducts.setAdapter(searchCommodityAdapter);
            searchCommodityAdapter.notifyItemRangeInserted(currentSize, productList.size());
        });
    }
}
