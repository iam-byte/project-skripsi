package com.ilham.inventoridiecut.preference;

import android.content.Context;
import android.content.SharedPreferences;
import android.preference.PreferenceManager;

public class preferences {
    static final String USER  ="user", status = "Login", KEY = "key",ID = "id";

    private static SharedPreferences getSharedPreferences(Context context){
        return PreferenceManager.getDefaultSharedPreferences(context);
    }

    public static void setUser(Context context, String nama){
        SharedPreferences.Editor editor = getSharedPreferences(context).edit();
        editor.putString(USER,nama);
        editor.apply();
    }

    public static String getUser(Context context){
        return getSharedPreferences(context).getString(USER,"");
    }

    public static void setKey(Context context, String nama){
        SharedPreferences.Editor editor = getSharedPreferences(context).edit();
        editor.putString(KEY,nama);
        editor.apply();
    }

    public static String getKey(Context context){
        return getSharedPreferences(context).getString(KEY,"");
    }

    public static void setId(Context context, String nama){
        SharedPreferences.Editor editor = getSharedPreferences(context).edit();
        editor.putString(ID,nama);
        editor.apply();
    }

    public static String getId(Context context){
        return getSharedPreferences(context).getString(ID,"");
    }

    public static void setLoginStatus(Context context, boolean active){
        SharedPreferences.Editor editor = getSharedPreferences(context).edit();
        editor.putBoolean(status,active);
        editor.apply();
    }
    public static boolean getLoginStatus(Context context){
        return getSharedPreferences(context).getBoolean(status,false);
    }

    public static void clearUser(Context context){
        SharedPreferences.Editor editor = getSharedPreferences(context).edit();
        editor.remove(USER);
        editor.remove(status);
        editor.apply();
    }
}
