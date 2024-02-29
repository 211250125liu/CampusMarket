package com.example.myapplication.ui.user.orderHistory;

import android.app.Activity;
import android.content.Intent;
import android.view.LayoutInflater;
import android.view.ViewGroup;

import androidx.recyclerview.widget.RecyclerView;

import com.example.myapplication.R;

import java.util.List;

public class OrderHistoryAdapter extends RecyclerView.Adapter<OrderHistoryViewHolder> {
    private final List<OrderHistoryItem> historyItemList;
    Activity activity;

    public OrderHistoryAdapter(List<OrderHistoryItem> historyItemList, Activity activity){
        this.historyItemList = historyItemList;
        this.activity = activity;
    }


    @androidx.annotation.NonNull
    @Override
    public OrderHistoryViewHolder onCreateViewHolder(@androidx.annotation.NonNull ViewGroup parent, int viewType){
        return new OrderHistoryViewHolder(LayoutInflater.from(parent.getContext()).inflate(R.layout.item_history, parent, false));
    }

    @Override
    public void onBindViewHolder(@androidx.annotation.NonNull OrderHistoryViewHolder holder, int position) {
        OrderHistoryItem i = historyItemList.get(position);

        holder.bindPic(i.orderPic);
        holder.title.setText(i.title);
        holder.describe.setText(i.describe);
        holder.price.setText(String.valueOf(i.price));
        holder.date.setText(i.date);

        // 检查是否有评论内容，如果有，则更新按钮文本或其他显示方式
        if (!i.commentContent.isEmpty()) {
            // 如果有评论内容，将按钮文本设置为最新的评论内容
            holder.setComment.setText("已评价");
            holder.setComment.setEnabled(false);
            holder.setComment.setClickable(false);
        }

        holder.setComment.setOnClickListener(view -> {
            Intent intent = new Intent(activity, AddCommentActivity.class);
            intent.putExtra("buyerId", i.orderResponse.buyerId);
            intent.putExtra("orderId", i.orderResponse.id);
            intent.putExtra("sellerId", i.orderResponse.sellerId);
            activity.startActivityForResult(intent, 48);
        });
    }

    @Override
    public int getItemCount() {
        return historyItemList.size();
    }
}
