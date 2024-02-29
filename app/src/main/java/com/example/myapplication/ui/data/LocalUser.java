package com.example.myapplication.ui.data;

import android.os.LocaleList;

public class LocalUser {
    private static LocalUser localUser = null;
    public Long id;
    public String nickname;
    public String photo;
    public String email;
    public String studentId;

    private LocalUser(){}

    public static LocalUser getLocalUser(){
        if(localUser == null){
            localUser = new LocalUser();
        }
        return  localUser;
    }
}
