package com.example.coolweather.gson;

/**
 * Created by Administrator on 2017/3/20.
 *      "aqi": {
 "city": {
 "aqi": "74",
 "pm10": "60",
 "pm25": "54",
 "qlty": "良"
 }
 },
 *
 */

public class Aqi {

   public City city;

    public class City{

        public String aqi;

        public String pm25;

        public String qlty;
    }

}
