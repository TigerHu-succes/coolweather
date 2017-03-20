package com.example.coolweather.gson;

/**
 * Created by Administrator on 2017/3/20.
 *"suggestion": {
 "comf": {
 "brf": "较舒适",
 "txt": "白天天气阴沉，会感到有点儿凉，但大部分人完全可以接受。"
 },
 "cw": {
 "brf": "较适宜",
 "txt": "较适宜洗车，未来一天无雨，风力较小，擦洗一新的汽车至少能保持一天。"
 },
 "sport": {
 "brf": "较不宜",
 "txt": "阴天，且天气寒冷，推荐您在室内进行低强度运动；若坚持户外运动，请选择合适的运动并注意保暖。"
 }
 }
 *
 */

public class Suggestion {

    public Comfort comf;

    public CardWash cw;

    public Sport sport;

    public class Comfort{

        public String txt;

    }

    public class CardWash{

        public String txt;


    }

    public class Sport{

        public String txt;

    }



}
