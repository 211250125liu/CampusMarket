package com.example.myapplication.HTTP.model;

import android.os.Parcel;
import android.os.Parcelable;

import com.google.gson.annotations.SerializedName;

public class OrderResponse implements Parcelable {
    @SerializedName("goodsId")
    public long goodsId;

    @SerializedName("id")
    public long id;
    @SerializedName("buyerId")
    public long buyerId;
    @SerializedName("sellerId")
    public long sellerId;
    @SerializedName("status")
    public int status;

    public OrderResponse(long id, long goodsId, long buyerId, long sellerId, int status) {
        this.id = id;
        this.goodsId = goodsId;
        this.buyerId = buyerId;
        this.sellerId = sellerId;
        this.status = status;
    }

    // Parcelable 接口实现部分
    protected OrderResponse(Parcel in) {
        goodsId = in.readLong();
        id = in.readLong();
        buyerId = in.readLong();
        sellerId = in.readLong();
        status = in.readInt();
    }

    @Override
    public void writeToParcel(Parcel dest, int flags) {
        dest.writeLong(goodsId);
        dest.writeLong(id);
        dest.writeLong(buyerId);
        dest.writeLong(sellerId);
        dest.writeInt(status);
    }

    @Override
    public int describeContents() {
        return 0;
    }

    public static final Creator<OrderResponse> CREATOR = new Creator<OrderResponse>() {
        @Override
        public OrderResponse createFromParcel(Parcel in) {
            return new OrderResponse(in);
        }

        @Override
        public OrderResponse[] newArray(int size) {
            return new OrderResponse[size];
        }
    };
}
