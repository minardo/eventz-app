package com.example.ardo.eventz.utils;

import android.content.Context;
import android.content.SharedPreferences;

import java.util.HashSet;

public class SessionManager {

    public static final String SP_ID = "spID";
    public static final String SP_USERNAME = "spUsername";
    public static final String SP_LOGIN = "loginSharedPreference";

    public static void saveSPString(Context context,String keySP, String value){
        SharedPreferences.Editor editor = context.getSharedPreferences(SP_LOGIN, Context.MODE_PRIVATE).edit();
        editor.putString(keySP, value);
        editor.commit();
    }

    public static String getCookiesPref(Context context,String paramater){
        SharedPreferences sp = context.getSharedPreferences(SP_LOGIN, Context.MODE_PRIVATE);
        return sp.getString(paramater,null);
    }

    public static void clearAll(Context context){
        SharedPreferences.Editor sp = context.getSharedPreferences(SP_LOGIN, Context.MODE_PRIVATE).edit();
        sp.clear();
        sp.commit();
    }

}
