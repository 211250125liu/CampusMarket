package com.example.myapplication.ui.home;

import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

import com.bumptech.glide.Glide;
import com.example.myapplication.R;
import com.example.myapplication.HTTP.model.Commodity;
import com.example.myapplication.ui.data.Address;

import java.util.List;

public class HomeCommodityAdapter extends RecyclerView.Adapter<HomeCommodityAdapter.CommudityViewHolder>{
    private List<Commodity> commodityList;
    public void setCommodityList(List<Commodity> commodityList) {
        this.commodityList = commodityList;
    }
    @NonNull
    @Override
    public HomeCommodityAdapter.CommudityViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        Context context = parent.getContext();
        LayoutInflater inflater = LayoutInflater.from(context);
        View view = inflater.inflate(R.layout.item_commodity_two, parent, false); // item_product 是商品列表项的布局文件
        return new HomeCommodityAdapter.CommudityViewHolder(view);
    }

    @Override
    public void onBindViewHolder(@NonNull HomeCommodityAdapter.CommudityViewHolder holder, int position) {
        if (commodityList != null) {
            Commodity commodity = commodityList.get(position);

            // 设置商品信息到 ViewHolder 中
            holder.bind(commodity);
        }
    }

    @Override
    public int getItemCount() {
        return (commodityList != null) ? commodityList.size() : 0;
    }

    public static class CommudityViewHolder extends RecyclerView.ViewHolder {
        ImageView imageViewProduct;
        TextView textViewName;
        TextView textViewPrice;
        TextView textViewDescription;


        public CommudityViewHolder(@NonNull View itemView) {
            super(itemView);
            imageViewProduct = itemView.findViewById(R.id.imageViewProduct);
            textViewName = itemView.findViewById(R.id.textViewProductName);
            textViewPrice = itemView.findViewById(R.id.textViewProductPrice);
            textViewDescription = itemView.findViewById(R.id.textViewProductDescription);
        }


        public void bind(Commodity commodity) {
            textViewName.setText(commodity.getName());
            textViewPrice.setText(String.valueOf(commodity.getPrice()));
            textViewDescription.setText(commodity.getIntroduce());

            String imageUrl = Address.getPicAddress() + Address.cut(commodity.getPictureUrl());
            // 使用 Glide 加载商品图片，你可以根据你的选择使用其他库
            Glide.with(itemView.getContext())
                    .load(imageUrl)
                    .into(imageViewProduct);
        }
    }
}
