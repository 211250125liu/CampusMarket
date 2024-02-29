package com.example.myapplication.ui.review;

import androidx.recyclerview.widget.RecyclerView;

import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.TextView;

import androidx.annotation.NonNull;

import com.bumptech.glide.Glide;
import com.example.myapplication.ui.data.Address;
import com.example.myapplication.HTTP.model.Review;

import com.example.myapplication.R;

import java.util.List;

public class ReviewAdapter extends RecyclerView.Adapter<ReviewAdapter.ReviewViewHolder> {
    private List<Review> reviewList; // 假设 Review 是你的数据模型类
    private Context context;

    public ReviewAdapter(List<Review> reviewList, Context context) {
        this.reviewList = reviewList;
        this.context = context;
    }

    @NonNull
    @Override
    public ReviewViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        View view = LayoutInflater.from(context).inflate(R.layout.item_review, parent, false);
        return new ReviewViewHolder(view);
    }

    @Override
    public void onBindViewHolder(@NonNull ReviewViewHolder holder, int position) {
        Review review = reviewList.get(position);

        // 设置评价项的数据
        holder.textReviewerName.setText(review.getReviewerName());
        holder.textReview.setText(review.getReviewText());
        holder.textReviewDate.setText(review.getReviewDate());

        // 设置好评差评的图片（假设有两种评价类型：positive 和 negative）
        if (review.getReviewType().equals("positive")) {
            holder.imageReviewType.setImageResource(R.drawable.good_foreground);
        } else {
            holder.imageReviewType.setImageResource(R.drawable.bad_foreground);
        }

        holder.bindPic(review.getPhoto());

        // 在这里你可能需要加载用户头像，可以使用类似 Glide 或 Picasso 的库

        // 如果需要更多的评价项，请继续添加到这里
    }

    @Override
    public int getItemCount() {
        return reviewList.size();
    }

    public static class ReviewViewHolder extends RecyclerView.ViewHolder {
        TextView textReviewerName;
        ImageView avatar;
        TextView textReview;
        TextView textReviewDate;
        ImageView imageReviewType;

        public ReviewViewHolder(@NonNull View itemView) {
            super(itemView);
            avatar = itemView.findViewById(R.id.avatarImageView);
            textReviewerName = itemView.findViewById(R.id.usernameTextView);
            textReview = itemView.findViewById(R.id.reviewContentTextView);
            textReviewDate = itemView.findViewById(R.id.dateTextView);
            imageReviewType = itemView.findViewById(R.id.ratingIconImageView);
        }

        public void bindPic(String url) {
            String computerIpAddress = Address.getPicAddress();
            String imageUrl = computerIpAddress + Address.cut(url);
            // 使用 Glide 加载商品图片，你可以根据你的选择使用其他库
            Glide.with(itemView.getContext())
                    .load(imageUrl)
                    .into(avatar);
        }
    }
}
