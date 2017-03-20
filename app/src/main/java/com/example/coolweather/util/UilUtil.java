package com.example.coolweather.util;

/**
 * Created by Administrator on 2017/3/15.
 * http://guolin.tech/api/weather?cityid=CN101220401&key=97abd375ef1b4c27a91b26e7db3113bd
 */

public class UilUtil {

    public static final String PROTOCOL="http://";

    public static final String DOMAIN_NAME="guolin.tech";

    public static final String RESOURCES="/api/china";

    public static final String WEATHER_STATU_RESOURCES="/api/weather";

    public static final String PARAMETERS_CITYID="cityid";

    public static final String PARAMETERS_KEY="key";

    public static final String URL=PROTOCOL+DOMAIN_NAME+RESOURCES;

    public static final String WEATHER_STATU_URL=PROTOCOL
            +DOMAIN_NAME
            +WEATHER_STATU_RESOURCES;

}
