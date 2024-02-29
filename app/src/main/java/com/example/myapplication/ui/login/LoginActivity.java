package com.example.myapplication.ui.login;

import android.content.Intent;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.TextView;
import android.widget.Toast;

import androidx.appcompat.app.AppCompatActivity;

import com.example.myapplication.HTTP.gson.LoginGsonData;
import com.example.myapplication.HTTP.gson.UserInfoGsonData;
import com.example.myapplication.HTTP.model.LoginRequest;
import com.example.myapplication.HTTP.model.UserInfoResponse;
import com.example.myapplication.HTTP.service.AuthService;
import com.example.myapplication.HTTP.service.UserInfoService;
import com.example.myapplication.MainActivity;
import com.example.myapplication.R;
import com.example.myapplication.HTTP.Retrofit;
import com.example.myapplication.ui.data.LocalUser;
import com.example.myapplication.ui.data.Login;
import com.example.myapplication.ui.data.Token;
import com.google.gson.Gson;
import com.google.gson.reflect.TypeToken;

import java.io.IOException;
import java.lang.reflect.Type;

import okhttp3.ResponseBody;
import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;


public class LoginActivity extends AppCompatActivity {
    private EditText EditStudentId;
    private EditText EditPassword;
    Button buttonLogin;
    TextView textRegister;
    TextView textForgetPassword;

    @Override
    protected void onCreate(Bundle savedInstancesState) {
        super.onCreate(savedInstancesState);


        setContentView(R.layout.activity_login);
        EditStudentId = findViewById(R.id.editTextStudentId);
        EditPassword = findViewById(R.id.editTextPassword);
        buttonLogin = findViewById(R.id.buttonLogin);
        textRegister = findViewById(R.id.textViewRegister);
        textForgetPassword = findViewById(R.id.textViewForgotPassword);

        textForgetPassword.setOnClickListener(v -> {
            Toast.makeText(LoginActivity.this, "请联系客服18897635905", Toast.LENGTH_LONG).show();
        });
        buttonLogin.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                login();
            }
        });
        textRegister.setOnClickListener(v -> {
            Intent intent = new Intent(LoginActivity.this, RegisterActivity.class);
            startActivity(intent);
        });
    }

    public void login() {
        String StudentId = EditStudentId.getText().toString();
        String password = EditPassword.getText().toString();

        // 获取 Retrofit 实例
        AuthService authService = Retrofit.getRetrofitInstance().create(AuthService.class);

        // 构建登录请求数据
        LoginRequest loginRequest = new LoginRequest(StudentId, password);

        Call<ResponseBody> call = authService.login(loginRequest);
        call.enqueue(new Callback<ResponseBody>() {
            @Override
            public void onResponse(Call<ResponseBody> call, Response<ResponseBody> response) {
                if (response.isSuccessful()) {
                    // 处理登录成功的情况
                    Toast.makeText(LoginActivity.this, "登陆成功", Toast.LENGTH_SHORT).show();
                    try {
                        String result = response.body().string();
                        Gson gson = new Gson();
                        Type type1 = new TypeToken<LoginGsonData>() {
                        }.getType();
                        LoginGsonData data1 = gson.fromJson(result, type1);
                        String token = data1.getData();
//                        Log.e("login", token);
//                        Toast.makeText(LoginActivity.this,token,Toast.LENGTH_LONG).show();
                        // 处理服务端返回的数据
                        Token.getInstance().setToken(token);

                        Call<ResponseBody> userCall = Retrofit.getRetrofitInstance().create(UserInfoService.class).getUserInfo(token);

                        userCall.enqueue(new Callback<ResponseBody>() {
                            @Override
                            public void onResponse(Call<ResponseBody> call, Response<ResponseBody> response) {
                                if (response.isSuccessful()) {
                                    try {
                                        String result = response.body().string();
                                        Gson gson = new Gson();
                                        Type type1 = new TypeToken<UserInfoGsonData>() {
                                        }.getType();
                                        UserInfoGsonData data1 = gson.fromJson(result, type1);
                                        UserInfoResponse userInfoResponse = data1.getData();
                                        // store info globally.
                                        LocalUser localUser = LocalUser.getLocalUser();
                                        localUser.nickname = userInfoResponse.nickname;
                                        localUser.id = userInfoResponse.id;
                                        localUser.email = userInfoResponse.email;
                                        localUser.photo = userInfoResponse.photo;
                                        localUser.studentId = userInfoResponse.studentId;
                                    } catch (Exception e) {
                                        e.printStackTrace();
                                    }
                                } else {
                                    Log.e("USER", "MISS");
                                }
                            }

                            @Override
                            public void onFailure(Call<ResponseBody> call, Throwable t) {
                                Log.e("USER", "MISS_BRANCH_2");
                            }
                        });
                    } catch (IOException e) {
                        e.printStackTrace();
                    }
                    Login.getInstance().setLoggedIn(true);
                    Intent intent = new Intent(LoginActivity.this, MainActivity.class);
                    startActivity(intent);
                } else {
                    // 处理登录失败的情况
                    Toast.makeText(LoginActivity.this, "账号或密码错误", Toast.LENGTH_SHORT).show();
                }
            }

            @Override
            public void onFailure(Call<ResponseBody> call, Throwable t) {
                // 处理请求失败的情况
                Toast.makeText(LoginActivity.this, "请求失败", Toast.LENGTH_SHORT).show();
            }
        });
    }
}
