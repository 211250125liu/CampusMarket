package com.example.myapplication.ui.user.orderHistory;

import android.app.Activity;
import android.content.Intent;
import android.view.View;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

import com.bumptech.glide.Glide;
import com.example.myapplication.R;
import com.example.myapplication.ui.data.Address;
import com.example.myapplication.ui.user.changeUserInfo.UserInfoActivity;

public class OrderHistoryViewHolder extends RecyclerView.ViewHolder {
    ImageView orderPic;
    TextView describe;
    TextView title;
    TextView date;
    TextView price;
    Button setComment;

    public OrderHistoryViewHolder(@NonNull View itemView) {
        super(itemView);
        orderPic = itemView.findViewById(R.id.OrderImage);
        describe = itemView.findViewById(R.id.Describe);
        title = itemView.findViewById(R.id.historyTitle);
        price = itemView.findViewById(R.id.Price);
        date = itemView.findViewById(R.id.date);
        setComment = itemView.findViewById(R.id.setComment);
    }

    public void bindPic(String url){
        String computerIpAddress = Address.getPicAddress();
        String imageUrl = computerIpAddress + Address.cut(url);
        // 使用 Glide 加载商品图片，你可以根据你的选择使用其他库
        Glide.with(itemView.getContext())
                .load(imageUrl)
                .into(orderPic);
    }
}
