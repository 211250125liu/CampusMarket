package com.example.myapplication.HTTP.model;

import android.os.Parcel;
import android.os.Parcelable;

public class GoodsType implements Parcelable {
    private String description;
    private String updateTime;
    private long userId;
    private String photos;
    private String createTime;
    private double price;
    private String name;
    private long id;

    // 添加构造函数、getter 和 setter 方法

    public String getDescription() {
        return description;
    }

    public long getUserId() {
        return userId;
    }

    public String getPhotos() {
        return photos;
    }

    public double getPrice() {
        return price;
    }

    public String getName() {
        return name;
    }

    public long getId() {
        return id;
    }

    public String getUpdateTime() {
        return updateTime;
    }

    // 例如：
    public GoodsType(String description, String updateTime, long userId, String photos, String createTime, double price, String name, long id) {
        this.description = description;
        this.updateTime = updateTime;
        this.userId = userId;
        this.photos = photos;
        this.createTime = createTime;
        this.price = price;
        this.name = name;
        this.id = id;
    }

    public String toString() {
        StringBuilder answer = new StringBuilder("description: " + this.description + "\n"
                + "price: " + this.price
                + "\n" + "name: " + this.name + "\n" + "photos: " + photos);
        return answer.toString();
    }

    public String[] getPhotosArray() {
        // 在这里进行手动解析，将字符串分割成数组
        return photos.split(","); // 假设图片 URL 之间使用逗号分隔s
    }

    public static final Parcelable.Creator<GoodsType> CREATOR = new Parcelable.Creator<GoodsType>() {
        @Override
        public GoodsType createFromParcel(Parcel in) {
            return new GoodsType(in);
        }

        @Override
        public GoodsType[] newArray(int size) {
            return new GoodsType[size];
        }
    };

    protected GoodsType(Parcel in) {
        description = in.readString();
        updateTime = in.readString();
        userId = in.readLong();
        photos = in.readString();
        createTime = in.readString();
        price = in.readDouble();
        name = in.readString();
        id = in.readLong();
    }

    @Override
    public void writeToParcel(Parcel dest, int flags) {
        dest.writeString(description);
        dest.writeString(updateTime);
        dest.writeLong(userId);
        dest.writeString(photos);
        dest.writeString(createTime);
        dest.writeDouble(price);
        dest.writeString(name);
        dest.writeLong(id);
    }

    @Override
    public int describeContents() {
        return 0;
    }

}

