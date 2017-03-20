package com.example.coolweather.util;

import android.content.SharedPreferences;
import android.preference.PreferenceManager;


/**
 * Created by Administrator on 2017/3/20.
 */

public class SaveUtil {

    public static String getString(String key, String defal) {

        SharedPreferences defaultSharedPreferences = PreferenceManager.getDefaultSharedPreferences(ApplicationUtil.getContext());

        return defaultSharedPreferences.getString(key, defal);
    }

    public static boolean saveString(String key, String value) {

        SharedPreferences defaultSharedPreferences = PreferenceManager.getDefaultSharedPreferences(ApplicationUtil.getContext());

        SharedPreferences.Editor edit = defaultSharedPreferences.edit();
        edit.putString(key,value);
        return edit.commit();
    };



}
