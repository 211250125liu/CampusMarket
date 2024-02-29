package com.example.myapplication.ui.data;

import android.util.Log;

public class Address {
    // 后端地址
    public static String getAddress() {
        return "http://" + "124.70.45.20:10010/";
    }

    public static String getPicAddress() {
        return "http://" + "60.204.235.187:8082/";
    }

    private static String getIp() {
        return "172.31.59.134";
    }

    public static String cut(String path) {
        path = path.replace("[", "").replace("]", "");
        StringBuilder answer = new StringBuilder();
        String[] tmp = path.split("/");
        if(tmp.length >= 4) {
            answer.append(tmp[2]).append("/").append(tmp[3]);
        }
        return answer.toString();
    }
}
