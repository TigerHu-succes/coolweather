package com.example.coolweather;

import android.content.Intent;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.text.TextUtils;

import com.example.coolweather.util.SaveUtil;

public class MainActivity extends AppCompatActivity {

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
        init();
    }

    private void init() {

        String weather_id = SaveUtil.getString("weather_js", null);

        if(!TextUtils.isEmpty(weather_id)){
            Intent intent = new Intent(this, WeatherActivity.class);;
            startActivity(intent);
            finish();
        }

    }
}
