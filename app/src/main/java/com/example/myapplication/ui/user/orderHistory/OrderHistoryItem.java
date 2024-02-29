package com.example.myapplication.ui.user.orderHistory;

import android.os.Parcel;
import android.os.Parcelable;

import com.example.myapplication.HTTP.model.OrderResponse;

public class OrderHistoryItem implements Parcelable {
    String orderPic;
    String describe;
    String title;
    double price;
    public OrderResponse orderResponse;
    public String commentContent;
    String date;


    public OrderHistoryItem(String orderPic, String describe, String title, double price, OrderResponse orderResponse, String date){
        this.orderPic = orderPic;
        this.describe = describe;
        this.title = title;
        this.price = price;
        this.orderResponse = orderResponse;
        this.commentContent = "";
        this.date = date;
    }

    protected OrderHistoryItem(Parcel in) {
        orderPic = in.readString();
        describe = in.readString();
        title = in.readString();
        price = in.readDouble();
        orderResponse = in.readParcelable(OrderResponse.class.getClassLoader());
        commentContent = in.readString();
    }

    @Override
    public void writeToParcel(Parcel dest, int flags) {
        dest.writeString(orderPic);
        dest.writeString(describe);
        dest.writeString(title);
        dest.writeDouble(price);
        dest.writeParcelable(orderResponse, flags);
        dest.writeString(commentContent);
    }

    @Override
    public int describeContents() {
        return 0;
    }

    public static final Creator<OrderHistoryItem> CREATOR = new Creator<OrderHistoryItem>() {
        @Override
        public OrderHistoryItem createFromParcel(Parcel in) {
            return new OrderHistoryItem(in);
        }

        @Override
        public OrderHistoryItem[] newArray(int size) {
            return new OrderHistoryItem[size];
        }
    };
}
