package com.example.coolweather.gson;

import com.google.gson.annotations.SerializedName;

/**
 * Created by Administrator on 2017/3/20.
 * "city": "淮南",
 "cnty": "中国",
 "id": "CN101220401",
 "lat": "32.662000",
 "lon": "117.020000",
 "update": {
 "loc": "2017-03-20 12:51",
 "utc": "2017-03-20 04:51"
 }
 */

public class Basic {

    @SerializedName("city")
    public String cityName;

    @SerializedName("id")
    public String weatherId;

    public Update update;

    public class Update{

        @SerializedName("loc")
        public String upDateTime;

    }
}
