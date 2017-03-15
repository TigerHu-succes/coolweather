package com.example.coolweather.util;

import android.text.TextUtils;

import com.example.coolweather.db.City;
import com.example.coolweather.db.County;
import com.example.coolweather.db.Province;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

/**
 * Created by Administrator on 2017/3/13.
 */

public class GsonUtil {


    public static boolean handleProvinceResponse(String response) {

        if (!TextUtils.isEmpty(response)) {

            try {

                JSONArray allProvince = new JSONArray(response);

                JSONObject jsonObject = null;

                Province province = null;

                for (int i = 0; i < allProvince.length(); i++) {

                    jsonObject = allProvince.getJSONObject(i);
                    province = new Province();
                    province.setProvinceCode(jsonObject.getInt("id"));
                    province.setProvinceName(jsonObject.getString("name"));
                    province.save();
                }

                return true;

            } catch (JSONException e) {
                e.printStackTrace();
            }

        }


        return false;
    }


    public static boolean handleCityResponse(String response, int provinceId) {

        if (!TextUtils.isEmpty(response)) {

            try {
                JSONArray allCity = new JSONArray(response);

                JSONObject jsonObject = null;

                City city = null;

                for (int i = 0; i < allCity.length(); i++) {

                    jsonObject = allCity.getJSONObject(i);
                    city = new City();
                    city.setCityCode(jsonObject.getInt("id"));
                    city.setCityName(jsonObject.getString("name"));
                    city.setProvinceId(provinceId);
                    city.save();
                }

                return true;

            } catch (JSONException e) {
                e.printStackTrace();
            }


        }


        return false;
    }


    public static boolean handleCountyResponse(String response, int cityId) {

        if (!TextUtils.isEmpty(response)) {

            try {
                JSONArray allCity = new JSONArray(response);

                JSONObject jsonObject = null;

                County county = null;

                for (int i = 0; i < allCity.length(); i++) {

                    jsonObject = allCity.getJSONObject(i);
                    county = new County();
                    county.setCountyCode(jsonObject.getInt("id"));
                    county.setCountyName(jsonObject.getString("name"));
                    county.setCityId(cityId);
                    county.setWeatherId(jsonObject.getString("weather_id"));
                    county.save();
                }

                return true;

            } catch (JSONException e) {
                e.printStackTrace();
            }


        }


        return false;
    }


}
