package com.example.coolweather.util;

import android.app.Application;
import android.content.Context;

import org.litepal.LitePalApplication;

/**
 * Created by Administrator on 2017/3/13.
 */

public class ApplicationUtil extends Application {

    private static Context context;

    @Override
    public void onCreate() {
        super.onCreate();
        context=getApplicationContext();
        LitePalApplication.initialize(context);
    }

    public Context getContext(){

        return context;
    }

}
