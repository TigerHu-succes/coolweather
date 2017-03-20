package com.example.coolweather.gson;

/**
 * Created by Administrator on 2017/3/20.
 * cond": {
 * "code_d": "104",
 * "code_n": "101",
 * "txt_d": "阴",
 * "txt_n": "多云"
 * },
 */

public class Forecast {

    public String date;

    public Temperature tmp;

    public More cond;

    public class Temperature {

        public String max;

        public String min;

    }

    public class More {

        public String txt_d;

    }


}
