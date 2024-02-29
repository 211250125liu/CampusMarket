package com.example.myapplication.ui.user;

import android.content.Intent;
import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.fragment.app.Fragment;

import com.bumptech.glide.Glide;
import com.example.myapplication.R;
import com.example.myapplication.ui.data.Address;
import com.example.myapplication.ui.data.LocalUser;
import com.example.myapplication.ui.data.Login;
import com.example.myapplication.ui.login.LoginActivity;
import com.example.myapplication.ui.review.ReviewActivity;
import com.example.myapplication.ui.user.changeUserInfo.UserInfoActivity;
import com.example.myapplication.ui.user.orderHistory.OrderHistoryActivity;

public class UserFragment extends Fragment {
    View v;

    @Nullable
    @Override
    public View onCreateView(@NonNull LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        v = inflater.inflate(R.layout.fragment_user, container, false);

        return v;
    }

    @Override
    public void onStart() {
        super.onStart();
        if (!Login.getInstance().isLoggedIn()) {
            // 用户未登录，跳转到登录页面
//            startActivity(new Intent(getActivity(), LoginActivity.class));
            getActivity().finishAffinity();
        }

        ImageView userBackground = v.findViewById(R.id.UserBackground);

        userBackground.setOnClickListener(view -> {
            Intent intent = new Intent(getActivity(), UserInfoActivity.class);
            startActivity(intent);
        });

        ImageView dealHistory = v.findViewById(R.id.dealHistory);

        dealHistory.setOnClickListener(view -> {
            startActivity(new Intent(getActivity(), OrderHistoryActivity.class));
        });

        ImageView commentHistory = v.findViewById(R.id.commentHistory);

        commentHistory.setOnClickListener(view -> {
            Intent intent = new Intent(getActivity(), ReviewActivity.class);

            LocalUser localUser = LocalUser.getLocalUser();

            intent.putExtra("nickname", localUser.nickname);
            intent.putExtra("email", localUser.email);
            intent.putExtra("photo", localUser.photo);
            intent.putExtra("id", localUser.id);

            startActivity(intent);
        });

        TextView userName = v.findViewById(R.id.UserName2);
        TextView userId = v.findViewById(R.id.Signature2);
        TextView email = v.findViewById(R.id.Email);
        ImageView avatar = v.findViewById(R.id.UserPhoto);
        TextView backLogin = v.findViewById(R.id.textView13);
        backLogin.setOnClickListener(w -> {
            Login.getInstance().setLoggedIn(false);
            startActivity(new Intent(getActivity(), LoginActivity.class));
        });

        LocalUser localUser = LocalUser.getLocalUser();

        userName.setText(localUser.nickname);
        userId.setText(localUser.studentId);
        email.setText(localUser.email);
        String computerIpAddress = Address.getPicAddress();
        String imageUrl = computerIpAddress + Address.cut(localUser.photo);

        Glide.with(v.getContext())
                .load(imageUrl)
                .into(avatar);

//        Call<ResponseBody> call = RetrofitClient.getRetrofitInstance().create(UserInfoService.class).getUserInfo(Token.getInstance().getToken());
//
//        call.enqueue(new Callback<ResponseBody>() {
//            @Override
//            public void onResponse(Call<ResponseBody> call, Response<ResponseBody> response) {
//                if (response.isSuccessful()) {
//                    try {
//                        String result = response.body().string();
//                        Gson gson = new Gson();
//                        Type type1 = new TypeToken<UserInfoGsonData>() {
//                        }.getType();
//                        UserInfoGsonData data1 = gson.fromJson(result, type1);
//                        UserInfoResponse userInfoResponse = data1.getData();
//                        userName.setText(userInfoResponse.nickname);
//                        userId.setText(userInfoResponse.studentId);
//                        email.setText(userInfoResponse.email);
//                        String computerIpAddress = Address.getPicAddress();
//                        String imageUrl = computerIpAddress + Address.cut(userInfoResponse.photo);
//
//                        Glide.with(v.getContext())
//                                .load(imageUrl)
//                                .into(avatar);
//                    } catch (Exception e) {
//                        e.printStackTrace();
//                    }
//                } else {
//                    Log.e("USER", "MISS");
//                }
//            }
//
//            @Override
//            public void onFailure(Call<ResponseBody> call, Throwable t) {
//                Log.e("USER", "MISS_BRANCH_2");
//            }
//        });
    }
}
