package com.example.coolweather.util;

import android.util.Log;

/**
 * Created by Administrator on 2017/3/15.
 */

public class LogUtil {

    public static final String TAG="com.example.coolweather";

    public static final int VERBOSE = 1;
    public static final int DEBUG = 2;
    public static final int INFO = 3;
    public static final int WARN = 4;
    public static final int ERROR = 5;

    public static final int NO_LOGINFO = 6;

    public static int courentLevel=VERBOSE;

    public static void v(String tag,String msg){

        if(courentLevel<=VERBOSE){

            Log.v(tag,msg);

        }

    }

    public static void d(String tag,String msg){

        if(courentLevel<=DEBUG){

            Log.d(tag,msg);

        }

    }

    public static void i(String tag,String msg){

        if(courentLevel<=INFO){

            Log.i(tag,msg);

        }

    }

    public static void w(String tag,String msg){

        if(courentLevel<=WARN){

            Log.w(tag,msg);

        }

    }

    public static void e(String tag,String msg){

        if(courentLevel<=ERROR){

            Log.e(tag,msg);
        }

    }



}
