package com.example.rylan.weather.service;

import android.app.AlarmManager;
import android.app.PendingIntent;
import android.app.Service;
import android.content.Intent;
import android.content.SharedPreferences;
import android.os.IBinder;
import android.os.SystemClock;
import android.preference.PreferenceManager;
import android.support.annotation.Nullable;

import com.example.rylan.weather.activity.ChooseAreaActivity;
import com.example.rylan.weather.activity.WeatherActivity;
import com.example.rylan.weather.util.HttpCallbackListener;
import com.example.rylan.weather.util.HttpUtil;
import com.example.rylan.weather.util.LogUtil;
import com.example.rylan.weather.util.Utility;

import java.io.InputStream;
import java.io.UnsupportedEncodingException;
import java.net.URLEncoder;

/**
 * Created by Rylan on 2016/10/20.
 */
public class AutoUpdateService extends Service {
    @Nullable
    @Override
    public IBinder onBind(Intent intent) {
        return null;
    }

    @Override
    public int onStartCommand(Intent intent, int flags, int startId) {
        LogUtil.log("onStartCommand", "onStartCommand", LogUtil.DEBUG);

        new Thread(new Runnable() {
            @Override
            public void run() {
                updateWeather();
            }
        }).start();
        AlarmManager alarmManager = (AlarmManager) getSystemService(ALARM_SERVICE);
        int eightHour = 8 * 60 * 60 * 1000;
        long triggerAtTime = SystemClock.elapsedRealtime() + eightHour;
        Intent i = new Intent(this, AutoUpdateService.class);
        PendingIntent pendingIntent = PendingIntent.getBroadcast(this, 0, i, 0);
        alarmManager.set(AlarmManager.ELAPSED_REALTIME_WAKEUP, triggerAtTime, pendingIntent);

        return super.onStartCommand(intent, flags, startId);
    }

    /**
     * 更新天气信息。
     */
    private void updateWeather() {
        SharedPreferences sharedPreferences = PreferenceManager.getDefaultSharedPreferences(this);
        String cityName = sharedPreferences.getString("city_name", "");
        try {
            String address = WeatherActivity.QUERY_WEATHER + URLEncoder.encode(cityName, "UTF-8")
                    + "&key=" + ChooseAreaActivity.API_KEY;
            HttpUtil.sendHttpRequest(address, new HttpCallbackListener() {
                @Override
                public void onFinish(InputStream in) {
                    // 把最新的天气又保存在了 SharedPreferences 对象中
                    // 下一次打开 WeatherActivity 时会调用 showWeather() 从 SharedPreferences 对象中
                    // 读取数据并显示
                    Utility.handleWeatherResponse(AutoUpdateService.this, in);
                }

                @Override
                public void onError(Exception e) {

                }
            });
        } catch (UnsupportedEncodingException e) {
            e.printStackTrace();
        }
    }
}
