package com.example.myapplication.ui.home;

import static android.nfc.tech.MifareUltralight.PAGE_SIZE;

import android.content.Intent;
import android.os.Bundle;
import android.util.Log;
import android.view.GestureDetector;
import android.view.LayoutInflater;
import android.view.MotionEvent;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.TextView;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.fragment.app.Fragment;
import androidx.recyclerview.widget.GridLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import com.example.myapplication.R;
import com.example.myapplication.HTTP.gson.GoodsListGsonData;
import com.example.myapplication.HTTP.service.GoodService;
import com.example.myapplication.HTTP.Retrofit;
import com.example.myapplication.databinding.FragmentHomeBinding;
import com.example.myapplication.HTTP.model.Commodity;
import com.example.myapplication.HTTP.model.GoodsType;
import com.example.myapplication.ui.data.Token;
import com.example.myapplication.ui.productDetails.ProductDetailActivity;
import com.example.myapplication.ui.search.SearchActivity;
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

public class HomeFragment extends Fragment {

    private FragmentHomeBinding binding;
    private ImageView imageViewSearch;
    private RecyclerView recyclerViewCommodities;
    private HomeCommodityAdapter homeCommodityAdapter;
    private TextView searchTextView;

    private List<GoodsType> goods = new ArrayList<>();

    private static int pageNum = 1;

    public View onCreateView(@NonNull LayoutInflater inflater,
                             ViewGroup container, Bundle savedInstanceState) {

        binding = FragmentHomeBinding.inflate(inflater, container, false);
        pageNum = 1;
        View root = binding.getRoot();
        imageViewSearch = root.findViewById(R.id.imageViewSearchIcon);
        searchTextView = root.findViewById(R.id.editTextSearch);
        searchTextView.setOnClickListener(v->{
            Intent intent = new Intent(getActivity(), SearchActivity.class);
            startActivity(intent);
        });
        imageViewSearch.setOnClickListener(v -> {
            // 点击imageView后跳转到SearchActivity
            Intent intent = new Intent(getActivity(), SearchActivity.class);
            startActivity(intent);
        });

        recyclerViewCommodities = root.findViewById(R.id.recyclerViewProducts);

        recyclerViewCommodities.addOnItemTouchListener(new RecyclerView.OnItemTouchListener() {
            GestureDetector gestureDetector = new GestureDetector(getActivity(), new GestureDetector.SimpleOnGestureListener() {
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
                    Intent intent = new Intent(getActivity(), ProductDetailActivity.class);
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

        initRecyclerView();
        return root;
    }

    private List<GoodsType> getGoodsTypeList(){
        return goods;
    }

    private void initRecyclerView() {
        // 创建适配器并设置给RecyclerView
        homeCommodityAdapter = new HomeCommodityAdapter();
        GridLayoutManager layoutManager = new GridLayoutManager(getContext(), 2);
        recyclerViewCommodities.setLayoutManager(layoutManager);
        recyclerViewCommodities.setAdapter(homeCommodityAdapter);

        // 添加滚动监听器
        recyclerViewCommodities.addOnScrollListener(new RecyclerView.OnScrollListener() {
            @Override
            public void onScrolled(@NonNull RecyclerView recyclerView, int dx, int dy) {
                super.onScrolled(recyclerView, dx, dy);
                // 在这里检测是否已经滚动到底部
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

        // 异步加载商品数据
        generateSampleCommoditiesAsync();
    }

    private void loadMoreData(){
        generateSampleCommoditiesAsync();
    }

    private void generateSampleCommoditiesAsync() {
        GoodService goodService = Retrofit.getRetrofitInstance().create(GoodService.class);

        Call<ResponseBody> call = goodService.defaultOrder(pageNum, Token.getInstance().getToken());
        call.enqueue(new Callback<ResponseBody>() {
            @Override
            public void onResponse(Call<ResponseBody> call, Response<ResponseBody> response) {
                if (response.isSuccessful()) {
                    try {
                        pageNum++;
                        String responseData = response.body().string();
                        // 处理 responseData
                        Gson gson = new Gson();
                        Type type1 = new TypeToken<GoodsListGsonData>(){}.getType();
                        GoodsListGsonData data1 = gson.fromJson(responseData,type1);

                        for(GoodsType goodsType : data1.getData()){
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

                        // 更新 RecyclerView 数据
                        updateRecyclerView(productList);
                    } catch (IOException e) {
                        e.printStackTrace();
                    }
                } else {
                    Toast.makeText(getActivity(),"后端异常",Toast.LENGTH_SHORT).show();
                }
            }

            @Override
            public void onFailure(Call<ResponseBody> call, Throwable t) {
                Toast.makeText(getActivity(),"请求失败",Toast.LENGTH_SHORT).show();
            }
        });
    }

    private void updateRecyclerView(List<Commodity> productList) {
        // 在主线程更新 RecyclerView 数据
        getActivity().runOnUiThread(() -> {
            homeCommodityAdapter.setCommodityList(productList);
            homeCommodityAdapter.notifyDataSetChanged();
        });
    }


    @Override
    public void onDestroyView() {
        super.onDestroyView();
        binding = null;
    }
}