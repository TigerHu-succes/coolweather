package com.example.coolweather.gson;

import com.google.gson.annotations.SerializedName;

/**
 * Created by Administrator on 2017/3/20.
 * "now": {
 * "cond": {
 * "code": "104",
 * "txt": "阴"
 * },
 * "fl": "7",
 * "hum": "96",
 * "pcpn": "0",
 * "pres": "1020",
 * "tmp": "7",
 * "vis": "7",
 * "wind": {
 * "deg": "142",
 * "dir": "东北风",
 * "sc": "4-5",
 * "spd": "22"
 * }
 * },
 */

public class Now {

    @SerializedName("tmp")
    public String temperature;

    public Cond cond;

    public Wind wind;

    public class Cond {

        @SerializedName("txt")
        public String weatherStatu;

    }

    public class Wind {

        @SerializedName("dir")
        public String wind;

        @SerializedName("sc")
        public String windSize;
    }

}
