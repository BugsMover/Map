package com.map_demo;

import android.content.Context;
import android.content.SharedPreferences;

import java.util.HashMap;
import java.util.Map;

public class SPsaveCity {

    public static boolean saveCityInfo(Context context, String cityname) {
        SharedPreferences sp = context.getSharedPreferences("data",Context.MODE_PRIVATE);
        SharedPreferences.Editor editor = sp.edit();
        editor.putString("cityname",cityname);
        editor.commit();
        return true;
    }
    public static Map<String,String> getCityInfo(Context context){
        SharedPreferences sp = context.getSharedPreferences("data",context.MODE_PRIVATE);
        String cityname = sp.getString("cityname","北京市");
        Map<String,String> cityMap = new HashMap<String, String>();
        cityMap.put("cityname",cityname);
        return cityMap;
    }
}
