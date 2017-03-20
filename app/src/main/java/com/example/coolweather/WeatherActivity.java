package com.example.coolweather;

import android.app.ProgressDialog;
import android.content.Intent;
import android.os.Build;
import android.os.Bundle;
import android.support.v4.view.GravityCompat;
import android.support.v4.widget.DrawerLayout;
import android.support.v4.widget.SwipeRefreshLayout;
import android.support.v7.app.AppCompatActivity;
import android.text.TextUtils;
import android.view.LayoutInflater;
import android.view.View;
import android.view.Window;
import android.view.WindowManager;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.ScrollView;
import android.widget.TextView;
import android.widget.Toast;

import com.bumptech.glide.Glide;
import com.example.coolweather.gson.Forecast;
import com.example.coolweather.gson.Weather;
import com.example.coolweather.util.ApplicationUtil;
import com.example.coolweather.util.GsonUtil;
import com.example.coolweather.util.HttpUtil;
import com.example.coolweather.util.LogUtil;
import com.example.coolweather.util.SaveUtil;
import com.example.coolweather.util.UilUtil;

import java.io.IOException;

import butterknife.ButterKnife;
import butterknife.InjectView;
import butterknife.OnClick;
import okhttp3.Call;
import okhttp3.Callback;
import okhttp3.Response;

public class WeatherActivity extends AppCompatActivity {

    @InjectView(R.id.title_city)
    public TextView titleCity;
    @InjectView(R.id.update_time)
    public TextView updateTime;
    @InjectView(R.id.degree_text)
    public TextView degreeText;
    @InjectView(R.id.weather_info)
    public TextView weatherInfo;
    @InjectView(R.id.forecast_layout)
    public LinearLayout forecastLayout;
    @InjectView(R.id.api_text)
    public TextView apiText;
    @InjectView(R.id.pm25_text)
    public TextView pm25Text;
    @InjectView(R.id.comfort_text)
    public TextView comfortText;
    @InjectView(R.id.car_wash_text)
    public TextView carWashText;
    @InjectView(R.id.sport_text)
    public TextView sportText;
    @InjectView(R.id.weather_layout)
    public ScrollView weatherLayout;
    @InjectView(R.id.back_group_image)
    public ImageView backGroupImage;
    @InjectView(R.id.swipe_refresh)
    public SwipeRefreshLayout swipeRefresh;
    @InjectView(R.id.drawer_Layout)
    public DrawerLayout drawerLayout;
    @InjectView(R.id.bu)
    public Button bu;
    private ProgressDialog progressDialog;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        requestWindowFeature(Window.FEATURE_NO_TITLE);
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_weather);
        ButterKnife.inject(this);
        init();
    }

    @OnClick(R.id.bu)
    public void onClick() {

        drawerLayout.openDrawer(GravityCompat.START);
    }


    private void init() {

        setStatusBar();

        swipeRefresh.setColorSchemeResources(R.color.colorPrimary);


        String bing_pic = SaveUtil.getString("bing_pic", null);

        if (!TextUtils.isEmpty(bing_pic)) {

            Glide.with(this).load(bing_pic).into(backGroupImage).onLoadStarted(getResources().getDrawable(R.mipmap.bg));

        } else {

            loadImage();
        }

        String weather_js = SaveUtil.getString("weather_js", null);

        String weatherId = null;

        if (!TextUtils.isEmpty(weather_js)) {

            Weather weather = GsonUtil.handleWeatherResponse(weather_js);
            weatherId = weather.basic.weatherId;
            showDialog();
            showConcreteInfo(weather);

        } else {

            weatherLayout.setVisibility(View.INVISIBLE);
            String weather_id = getIntent().getStringExtra("weather_id");
            weatherId = weather_id;
            showDialog();
            requestInfo(weather_id);

        }

        final String finalWeatherId = weatherId;

        swipeRefresh.setOnRefreshListener(new SwipeRefreshLayout.OnRefreshListener() {
            @Override
            public void onRefresh() {

                requestInfo(finalWeatherId);
            }
        });


    }

    private void setStatusBar() {

        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.KITKAT) {
            // 状态栏 顶部
            getWindow().addFlags(
                    WindowManager.LayoutParams.FLAG_TRANSLUCENT_STATUS);
            // 导航栏 底部
            getWindow().addFlags(
                    WindowManager.LayoutParams.FLAG_TRANSLUCENT_NAVIGATION);
        }
    }

    private void loadImage() {

        String requestBingPic = "http://guolin.tech/api/bing_pic";

        HttpUtil.sendOkHttpRequest(requestBingPic, new Callback() {
            @Override
            public void onFailure(Call call, IOException e) {

            }

            @Override
            public void onResponse(Call call, Response response) throws IOException {

                final String string = response.body().string();
                SaveUtil.saveString("bing_pic", string);

                runOnUiThread(new Runnable() {
                    @Override
                    public void run() {

                        Glide.with(WeatherActivity.this).load(string).into(backGroupImage).onLoadStarted(getResources().getDrawable(R.mipmap.bg));
                    }
                });

            }
        });

    }

    public void requestInfo(final String weather_id) {



        String url = UilUtil.WEATHER_STATU_URL
                + "?" + UilUtil.PARAMETERS_CITYID
                + "=" + weather_id + "&" + UilUtil.PARAMETERS_KEY
                + "=" + "97abd375ef1b4c27a91b26e7db3113bd";

        HttpUtil.sendOkHttpRequest(url, new Callback() {
            @Override
            public void onFailure(Call call, IOException e) {

                runOnUiThread(new Runnable() {
                    @Override
                    public void run() {
                        Toast.makeText(ApplicationUtil.getContext(), "网络不佳", Toast.LENGTH_LONG).show();

                        swipeRefresh.setRefreshing(false);
                    }
                });

            }

            @Override
            public void onResponse(Call call, Response response) throws IOException {

                final String s = response.body().string();
                final Weather weather = GsonUtil.handleWeatherResponse(s);

                runOnUiThread(new Runnable() {
                    @Override
                    public void run() {

                        if (weather != null && "ok".equals(weather.status)) {


                            Intent intent = new Intent(WeatherActivity.this, MyService.class);

                            startService(intent);

                            SaveUtil.saveString("weather_js", s);
                            showConcreteInfo(weather);

                        } else {
                            closeDialog();
                            Toast.makeText(ApplicationUtil.getContext(), "网络不佳", Toast.LENGTH_LONG).show();
                        }

                        swipeRefresh.setRefreshing(false);

                    }
                });

            }
        });

    }

    private void showConcreteInfo(Weather weather) {

        String cityName = weather.basic.cityName;
        String upDateTime = weather.basic.update.upDateTime;
        String degree = weather.now.temperature + "C";
        String weatherStatu = weather.now.cond.weatherStatu;

        titleCity.setText(cityName);
        updateTime.setText(upDateTime);
        degreeText.setText(degree);
        weatherInfo.setText(weatherStatu);

        forecastLayout.removeAllViews();


        View view = null;
        TextView date_text = null;
        TextView info_text = null;
        TextView max_text = null;
        TextView min_text = null;

        for (Forecast temp : weather.daily_forecast) {

            view = LayoutInflater.from(this).inflate(R.layout.forecast_item, forecastLayout, false);
            date_text = (TextView) view.findViewById(R.id.date_text);
            info_text = (TextView) view.findViewById(R.id.info_text);
            max_text = (TextView) view.findViewById(R.id.max_text);
            min_text = (TextView) view.findViewById(R.id.min_text);
            date_text.setText(temp.date);
            info_text.setText(temp.cond.txt_d);
            max_text.setText(temp.tmp.max);
            max_text.setText(temp.tmp.min);
            forecastLayout.addView(view);
        }

        if (weather.aqi != null) {
            apiText.setText(weather.aqi.city.aqi);
            pm25Text.setText(weather.aqi.city.pm25);
        }

        String comf = "舒适度" + weather.suggestion.comf.txt;
        String cw = "洗车建议" + weather.suggestion.cw.txt;
        String sport = "运动建议" + weather.suggestion.sport.txt;

        comfortText.setText(comf);
        carWashText.setText(cw);
        sportText.setText(sport);
        closeDialog();
        weatherLayout.setVisibility(View.VISIBLE);
    }


    public void showDialog() {

        if (progressDialog == null) {
            progressDialog = new ProgressDialog(this);
            progressDialog.setCanceledOnTouchOutside(false);
            progressDialog.setTitle("加载中....");
        }
        progressDialog.show();
    }

    public void closeDialog() {

        if (progressDialog != null) {

            progressDialog.dismiss();
        }
    }


}
