package com.example.rylan.weather.activity;

import android.content.Intent;
import android.content.SharedPreferences;
import android.os.Bundle;
import android.preference.PreferenceManager;
import android.support.v4.content.ContextCompat;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.Toolbar;
import android.text.TextUtils;
import android.view.MenuItem;
import android.view.View;
import android.view.Window;
import android.widget.ImageView;
import android.widget.TextView;
import android.widget.Toast;

import com.example.rylan.weather.R;
import com.example.rylan.weather.service.AutoUpdateService;
import com.example.rylan.weather.util.HttpCallbackListener;
import com.example.rylan.weather.util.HttpUtil;
import com.example.rylan.weather.util.LogUtil;
import com.example.rylan.weather.util.Utility;
import com.orhanobut.logger.Logger;

import java.io.InputStream;
import java.io.UnsupportedEncodingException;
import java.net.URLEncoder;


public class WeatherActivity extends AppCompatActivity {
    public static final String QUERY_WEATHER = "http://v.juhe.cn/weather/index?format=2&cityname=";
    public static final String EXTRA_FROM_WEATHER_ACTIVITY = "from_weather_activity";

    private TextView mCurrentDateText;
    private TextView mWeatherDescriptionText;
    private TextView mTemperatureRangeText;
    private ImageView mWeatherImageView;

    private Toolbar mToolbar;
    private TextView mToolbarCityName;


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        supportRequestWindowFeature(Window.FEATURE_NO_TITLE);
        setContentView(R.layout.weather_layout);
        Logger.init("Weather");

        mCurrentDateText = (TextView) findViewById(R.id.current_date);
        mWeatherDescriptionText = (TextView) findViewById(R.id.weather_description);
        mTemperatureRangeText = (TextView) findViewById(R.id.temp_range);
        mWeatherImageView = (ImageView) findViewById(R.id.weather_imageView);
        mToolbar = (Toolbar) findViewById(R.id.toolbar);
        mToolbarCityName = (TextView) findViewById(R.id.toolbar_city_name);


        mToolbar.inflateMenu(R.menu.weather_toolbar_menu);
        //mToolbar.setLogo(R.mipmap.ic_weather);
        mToolbar.setNavigationIcon(R.drawable.ic_home);
        mToolbar.setTitleTextColor(ContextCompat.getColor(this, android.R.color.white));
        mToolbar.setNavigationOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent intent = new Intent(WeatherActivity.this, ChooseAreaActivity.class);
                intent.putExtra(EXTRA_FROM_WEATHER_ACTIVITY, true);
                startActivity(intent);
                finish();
            }
        });

        mToolbar.setOnMenuItemClickListener(new Toolbar.OnMenuItemClickListener() {
            @Override
            public boolean onMenuItemClick(MenuItem item) {
                switch (item.getItemId()) {
                    case R.id.action_refresh:
                        mWeatherDescriptionText.setText("同步中...");
                        SharedPreferences preferences =
                                PreferenceManager.getDefaultSharedPreferences(WeatherActivity.this);
                        String districtName = preferences.getString("district_name", "");
                        queryWeather(districtName);
                        break;
                    case R.id.action_about:
                        Toast.makeText(WeatherActivity.this, "本应用由 Rylan<刘杨钺> 开发", Toast.LENGTH_SHORT).show();
                    default:
                        break;
                }
                return true;
            }
        });

        // 检查 ChooseAreaActivity 中创建的 intent 是否携带地区名信息
        String districtName = getIntent().getStringExtra(ChooseAreaActivity.EXTRA_DISTRACT_NAME);
        if (!TextUtils.isEmpty(districtName)) {
            LogUtil.log("onCreate", "districtName = " + districtName, LogUtil.DEBUG);
            // 地区名字非空 开始查询地区天气 改变 mPublishText
            mWeatherDescriptionText.setText("同步中...");
            queryWeather(districtName);
        } else {
            showWeather();
        }
    }






    private void queryWeather(String name) {
        LogUtil.log("queryWeather", "queryWeather", LogUtil.DEBUG);
        try {
            String address = QUERY_WEATHER + URLEncoder.encode(name, "UTF-8")
                    + "&key=" + ChooseAreaActivity.API_KEY;
            HttpUtil.sendHttpRequest(address, new HttpCallbackListener() {
                // 建立连接后回调 onFinish() 方法
                @Override
                public void onFinish(InputStream in) {
                    // 调用 Utility.handleWeatherResponse() 方法可以处理数据并保存到 SharedPreference 对象中
                    Utility.handleWeatherResponse(WeatherActivity.this, in);

                    // 调用 showWeather() 读取 SharedPreference 对象中的数据 并改变控件显示天气信息
                    // 改变控件需要在UI线程中执行 调用 runOnUiThread() 方法
                    runOnUiThread(new Runnable() {
                        @Override
                        public void run() {
                            showWeather();
                        }
                    });
                }

                @Override
                public void onError(Exception e) {
                    runOnUiThread(new Runnable() {
                        @Override
                        public void run() {
                            mWeatherDescriptionText.setText("更新天气失败");
                        }
                    });
                }
            });
        } catch (UnsupportedEncodingException e) {
            e.printStackTrace();
        }

    }

    /**
     * 从SharedPreferences文件中读取存储的天气信息,并显示到界面上
     */
    private void showWeather() {
        SharedPreferences preferences = PreferenceManager.getDefaultSharedPreferences(this);

        mToolbarCityName.setText(preferences.getString("city_name", ""));
        mCurrentDateText.setText(preferences.getString("date", ""));
        String weatherDescription = preferences.getString("weather", "");
        switch (weatherDescription.hashCode()) {
            case 769209:
                mWeatherImageView.setImageResource(R.drawable.xiaoyu);
                break;
            case 1230675:
                mWeatherImageView.setImageResource(R.drawable.zhenyu);
                break;
            case -1955655440:
                mWeatherImageView.setImageResource(R.drawable.zhenyu);
                break;
            case 1183843099:
                mWeatherImageView.setImageResource(R.drawable.zhenyu);
                break;
            case 668590171:
                mWeatherImageView.setImageResource(R.drawable.leizhenyu);
                break;
            case -1779534254:
                mWeatherImageView.setImageResource(R.drawable.leizhenyu);
                break;
            case 669051637:
                mWeatherImageView.setImageResource(R.drawable.leizhenyu);
                break;
            case 26228:
                mWeatherImageView.setImageResource(R.drawable.qing);
                break;
            case 668479997:
                mWeatherImageView.setImageResource(R.drawable.leizhenyu);
                break;
            case 727223:
                mWeatherImageView.setImageResource(R.drawable.duoyun);
                break;
            case 700025727:
                mWeatherImageView.setImageResource(R.drawable.duoyun);
                break;
            case 1181534831:
                mWeatherImageView.setImageResource(R.drawable.yin);
                break;


            default:
                mWeatherImageView.setImageResource(R.drawable.weather_default);
                break;
        }
        mWeatherDescriptionText.setText(weatherDescription);
        Logger.d("weather %s %d", weatherDescription, weatherDescription.hashCode());
        mTemperatureRangeText.setText(preferences.getString("temperature_range", ""));

        // 在 showWeather()方法的最后加入启动 AutoUpdateService 这个服务的代码，
        // 这样只要一旦选中了某个城市并成功更新天气之后，AutoUpdateService就会一直在后台运行
        Intent intent = new Intent(this, AutoUpdateService.class);
        startService(intent);
    }
}
