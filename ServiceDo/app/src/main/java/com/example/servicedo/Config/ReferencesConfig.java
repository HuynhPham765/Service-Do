package com.example.servicedo.Config;

import android.content.Context;
import android.content.SharedPreferences;

public class ReferencesConfig {

    private final String APP_NAME = "service_do";
    public static final String USER_NAME = "user_name";
    public static final String PASSWORD = "password";
    private SharedPreferences share;

    public ReferencesConfig(Context context) {
        this.share = context.getSharedPreferences(APP_NAME, Context.MODE_PRIVATE);
    }

    public void putString(String key, String value){
        SharedPreferences.Editor editor = share.edit();
        editor.putString(key, value);
        editor.apply();
    }

    public String getString(String key){
        return share.getString(key, "");
    }

    public void putBoolean(String key, boolean value){
        SharedPreferences.Editor editor = share.edit();
        editor.putBoolean(key, value);
        editor.apply();
    }

    public boolean getBoolean(String key){
        return share.getBoolean(key, true);
    }

    public void putInt(String key, int value){
        SharedPreferences.Editor editor = share.edit();
        editor.putInt(key, value);
        editor.apply();
    }

    public int getInt(String key){
        return share.getInt(key, 0);
    }
}
