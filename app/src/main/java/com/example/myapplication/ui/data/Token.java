package com.example.myapplication.ui.data;

// 保存用户的token
public class Token {
    private static Token token;
    private String this_token;

    private Token() {
        // 私有构造函数，防止外部实例化
    }
    public static Token getInstance(){
        if(token == null){
            token = new Token();
        }
        return token;
    }

    public String getToken() {
        return this.this_token;
    }

    public void setToken(String newToken) {
        this.this_token = newToken;
    }
}
