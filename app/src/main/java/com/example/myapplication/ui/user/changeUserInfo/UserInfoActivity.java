package com.example.myapplication.ui.user.changeUserInfo;

import android.os.Bundle;
import android.util.Log;
import android.widget.EditText;

import androidx.annotation.Nullable;
import androidx.appcompat.app.AppCompatActivity;

import com.example.myapplication.R;
import com.example.myapplication.HTTP.gson.UserInfoGsonData;
import com.example.myapplication.HTTP.model.UserInfoResponse;
import com.example.myapplication.HTTP.Retrofit;
import com.example.myapplication.HTTP.service.UserInfoService;
import com.example.myapplication.ui.data.LocalUser;
import com.example.myapplication.ui.data.Token;
import com.google.gson.Gson;
import com.google.gson.reflect.TypeToken;

import java.io.IOException;
import java.lang.reflect.Type;

import okhttp3.ResponseBody;
import retrofit2.Response;

public class UserInfoActivity extends AppCompatActivity {

    EditText email;
    EditText nickname;

    String e;
    String n;

    @Override
    protected void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_change_info);

        email = findViewById(R.id.StudentEmail);
        nickname = findViewById(R.id.StudentName);

        findViewById(R.id.setComment).setOnClickListener(x -> {
            Thread thread = new Thread(new Runnable() {
                @Override
                public void run() {
                    changeInfo();
                }
            });

// 启动线程
            thread.start();
        });

        findViewById(R.id.cancel).setOnClickListener(x -> {
            this.finish();
        });

        e = LocalUser.getLocalUser().email;
        n = LocalUser.getLocalUser().nickname;
    }

    private void changeInfo() {
        n = (nickname.getText().toString().equals("")) ? n : nickname.getText().toString();
        e = (email.getText().toString().equals("")) ? e : email.getText().toString();

        UserInfoService userInfoService = Retrofit.getRetrofitInstance().create(UserInfoService.class);

        try {
            // 同步执行请求
            Response<ResponseBody> response = userInfoService.putUserInfo(Token.getInstance().getToken(), n, e).execute();

            if (response.isSuccessful()) {
                // 如果请求成功，则继续执行下一个同步请求
                Response<ResponseBody> userResponse = Retrofit.getRetrofitInstance().create(UserInfoService.class)
                        .getUserInfo(Token.getInstance().getToken()).execute();

                if (userResponse.isSuccessful()) {
                    String result = userResponse.body().string();
                    Gson gson = new Gson();
                    Type type1 = new TypeToken<UserInfoGsonData>() {
                    }.getType();
                    UserInfoGsonData data1 = gson.fromJson(result, type1);
                    UserInfoResponse userInfoResponse = data1.getData();
                    // 存储全局信息
                    LocalUser localUser = LocalUser.getLocalUser();
                    localUser.nickname = userInfoResponse.nickname;
                    localUser.id = userInfoResponse.id;
                    localUser.email = userInfoResponse.email;
                    localUser.photo = userInfoResponse.photo;
                    localUser.studentId = userInfoResponse.studentId;
                } else {
                    Log.e("USER", "MISS");
                }
            }
        } catch (IOException e) {
            Log.e("USER", "MISS_BRANCH_2");
        }

        this.finish();
    }
}
