package com.example.coolweather;

import android.app.Activity;
import android.app.AlarmManager;
import android.app.PendingIntent;
import android.app.Service;
import android.content.Intent;
import android.os.IBinder;
import android.os.SystemClock;
import android.support.annotation.IntDef;
import android.text.TextUtils;

import com.bumptech.glide.Glide;
import com.example.coolweather.gson.Weather;
import com.example.coolweather.util.GsonUtil;
import com.example.coolweather.util.HttpUtil;
import com.example.coolweather.util.SaveUtil;
import com.example.coolweather.util.UilUtil;

import java.io.IOException;

import okhttp3.Call;
import okhttp3.Callback;
import okhttp3.Response;

public class MyService extends Service {
    public MyService() {
    }

    @Override
    public IBinder onBind(Intent intent) {
        // TODO: Return the communication channel to the service.
        return null;
    }

    @Override
    public int onStartCommand(Intent intent, int flags, int startId) {

        updateWeather();

        updateBingPic();


        AlarmManager manager = (AlarmManager) getSystemService(ALARM_SERVICE);

        int anHour = 8 * 60 * 60 * 1000;

        long timer = SystemClock.elapsedRealtime() + anHour;

        Intent intents = new Intent(this, MyService.class);

        PendingIntent service = PendingIntent.getService(this, 0, intents, 0);

        manager.cancel(service);

        manager.set(AlarmManager.ELAPSED_REALTIME_WAKEUP, timer, service);

        return super.onStartCommand(intent, flags, startId);
    }

    private void updateBingPic() {

        String requestBingPic = "http://guolin.tech/api/bing_pic";

        HttpUtil.sendOkHttpRequest(requestBingPic, new Callback() {
            @Override
            public void onFailure(Call call, IOException e) {

            }

            @Override
            public void onResponse(Call call, Response response) throws IOException {

                final String string = response.body().string();
                SaveUtil.saveString("bing_pic", string);

            }
        });

    }

    private void updateWeather() {


        String weather_js = SaveUtil.getString("weather_js", null);

        if (!TextUtils.isEmpty(weather_js)) {

            Weather weather = GsonUtil.handleWeatherResponse(weather_js);

            String weatherId = weather.basic.weatherId;

            String url = UilUtil.WEATHER_STATU_URL
                    + "?" + UilUtil.PARAMETERS_CITYID
                    + "=" + weatherId + "&" + UilUtil.PARAMETERS_KEY
                    + "=" + "97abd375ef1b4c27a91b26e7db3113bd";

            HttpUtil.sendOkHttpRequest(url, new Callback() {
                @Override
                public void onFailure(Call call, IOException e) {

                }

                @Override
                public void onResponse(Call call, Response response) throws IOException {

                    String s = response.body().string();

                    Weather weather = GsonUtil.handleWeatherResponse(s);

                    if (weather != null && "ok".equals(weather.status)) {

                        SaveUtil.saveString("weather_js", s);
                    }
                }
            });


        }


    }
}
