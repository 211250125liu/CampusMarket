package com.example.myapplication.ui.data;

//登录状态保存
public class Login {
    private static Login instance;
    private boolean isLoggedIn = false;

    private Login() {
        // 私有构造函数，防止外部实例化
    }

    public static synchronized Login getInstance() {
        if (instance == null) {
            instance = new Login();
        }
        return instance;
    }

    public boolean isLoggedIn() {
        return isLoggedIn;
    }

    public void setLoggedIn(boolean loggedIn) {
        isLoggedIn = loggedIn;
    }
}
