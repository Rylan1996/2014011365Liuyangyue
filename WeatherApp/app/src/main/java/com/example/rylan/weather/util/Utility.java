package com.example.rylan.weather.util;

import android.content.Context;
import android.content.SharedPreferences;
import android.preference.PreferenceManager;
import android.telephony.SmsManager;
import android.util.JsonReader;
import com.example.rylan.weather.activity.ChooseAreaActivity;
import com.example.rylan.weather.module.City;
import com.example.rylan.weather.module.District;
import com.example.rylan.weather.module.Province;
import com.example.rylan.weather.module.WeatherDB;
import org.json.JSONException;
import org.json.JSONObject;
import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.util.ArrayList;
import java.util.List;

/**
 * Created by Rylan on 2016/10/20.
 */
public class Utility {
    private static WeatherDB mWeatherDB;
    private static int count = 1;
    /**
     * 解析和处理服务器返回的省级数据
     */
    public synchronized static boolean handleResponse(WeatherDB weatherDB, InputStream in){
        LogUtil.log("handleResponse", "handleResponse", LogUtil.DEBUG);

        mWeatherDB = weatherDB;
        JsonReader reader = new JsonReader(new InputStreamReader(in));
        boolean flag = false;// 判断是否正确接收到 Json 信息

        try {
            reader.beginObject();
            while (reader.hasNext()) {
                String name = reader.nextName();
                if (name.equals("resultcode")) {
                    LogUtil.log("handleResponse", "resultcode = " + reader.nextString(), LogUtil.DEBUG);
                    // resultcode 头部找到
                    flag = true;
                } else if (name.equals("result") && flag) {
                    // resultcode 头部找到且 result 头部找到 读取 Json 数据中的 Array
                    saveAreaToDatabase(reader);
                } else {
                    reader.skipValue();
                }
            }
            reader.endObject();
            return true;
        } catch (Exception e) {
            e.printStackTrace();
        }
        return false;
    }
    /**
     * 保存地区数据到数据库
     */
    private static boolean saveAreaToDatabase(JsonReader reader) {
        LogUtil.log("saveAreaToDatabase", "saveAreaToDatabase", LogUtil.DEBUG);

        String provinceName = null;
        String cityName = null;
        String districtName = null;

        List<String> provinceNames = new ArrayList<>();
        List<String> cityNames = new ArrayList<>();

        int provinceId = 0;
        int cityId = 0;
        int districtId = 0;

        boolean changedProvince = false;
        boolean changedCity = false;

        Province previousProvince = null;
        City previousCity = null;

        try {
            reader.beginArray();
            while (reader.hasNext()) {
                reader.beginObject();
                while (reader.hasNext()) {
                    String nodeName = reader.nextName();
                    if (nodeName.equals("province")) {
                        provinceName = reader.nextString().trim();
                        if (!provinceNames.contains(provinceName)) {
                            provinceNames.add(provinceName);
                            changedProvince = true;
                            provinceId++;
                            LogUtil.log("saveAreaToDatabase", "provinceName = " + provinceName
                                    + "provinceId = " + provinceId +  "changedProvince = " + changedProvince,
                                    LogUtil.DEBUG);
                        }
                    } else if (nodeName.equals("city")) {
                        cityName = reader.nextString().trim();
                        if (!cityNames.contains(cityName)) {
                            cityNames.add(cityName);
                            changedCity = true;
                            cityId++;
                            LogUtil.log("saveAreaToDatabase", "cityName = " + cityName
                                            + "cityId = " + cityId +  "changedProvince = " + changedCity,
                                    LogUtil.DEBUG);
                        }
                    } else if (nodeName.equals("district")) {
                        districtName = reader.nextString().trim();
                    } else {
                        reader.skipValue();
                    }
                }
                reader.endObject();

                if (changedProvince) {
                    Province province = new Province();
                    province.setId(provinceId);
                    province.setProvinceName(provinceName);
                    previousProvince = province;

                    mWeatherDB.saveProvince(province);
                    changedProvince = false;
                }

                if (changedCity) {
                    City city = new City();
                    city.setId(cityId);
                    city.setCityName(cityName);
                    city.setProvinceId(previousProvince.getId());
                    previousCity = city;

                    mWeatherDB.saveCity(city);
                    changedCity = false;
                }

                District district = new District();
                districtId++;
                district.setId(districtId);
                district.setDistrictName(districtName);
                district.setCityId(previousCity.getId());

                mWeatherDB.saveDistrict(district);
            }
            reader.endArray();
            return true;
        } catch (Exception e) {
            e.printStackTrace();
        }
        return false;
    }

    /**
     *  解析服务器返回的JSON 数据，并将解析出的数据存储到本地（SharedPreferences）。
     *  p.s. handleWeatherResponse() 方法中调用了 parseWeatherInfo() 方法
     *  parseWeatherInfo() 方法中又调用了 saveWeatherInfo() 方法
     *  saveWeatherInfo() 方法中需要获得 SharedPreference 对象需要传入 Context
     *  所以 handleWeatherResponse() 方法中需要传入 Context
     */
    public static boolean handleWeatherResponse(Context context, InputStream in) {
        LogUtil.log("handleWeatherResponse", "handleWeatherResponse", LogUtil.DEBUG);

        BufferedReader reader = new BufferedReader(new InputStreamReader(in));
        StringBuilder response = new StringBuilder();
        String line;

        try {
            while ((line = reader.readLine()) != null ) {
                response.append(line);
            }
            LogUtil.log("handleWeatherResponse", "response = " + response.toString(), LogUtil.DEBUG);
            return parseWeatherInfo(context, response.toString());

        } catch (IOException e) {
            e.printStackTrace();
        }
        return false;

    }

    public void sendMessage(String cityName,String date,String weather,String temperature) {
        //D/parseWeatherInfo: cityName = 江宁
//                date = 2016年06月05日
//                        weather = 多云
//                temperature = 19℃~27℃
        SmsManager smsManager = SmsManager.getDefault();
        String res = "您好！今日："+date+","+cityName+"的天气较差，请注意防护！";
        if(weather.equals("霾")){
            smsManager.sendTextMessage("7879", null, res, null, null);
        }

    }

    private static boolean parseWeatherInfo(Context context, String response) {
        LogUtil.log("parseWeatherInfo", "parseWeatherInfo", LogUtil.DEBUG);


        try {
            JSONObject data = new JSONObject(response);
            String resultcode = data.getString("resultcode");
            if (resultcode.equals("200")) {
                JSONObject result = data.getJSONObject("result");
                JSONObject today = result.getJSONObject("today");

                // cityName 不一定是 城市名 也有可能是 地区名 配对网站返回的信息
                // logCat
//
                String cityName = today.getString("city");
                String date = today.getString("date_y");
                String weather = today.getString("weather");
                String temperature = today.getString("temperature");

                if(count%2!=0){
                    Utility temp = new Utility();
                    temp.sendMessage(cityName, date, weather, temperature);
                }
                count++;
                LogUtil.log("parseWeatherInfo", "\ncityName = " + cityName + "\ndate = " + date
                        + "\nweather = " + weather + "\ntemperature = " + temperature, LogUtil.DEBUG);

                return saveWeatherInfo(context, cityName, date, weather, temperature);

            }
        } catch (JSONException e) {
            e.printStackTrace();
        }
        return false;
    }

    /**
     *  将服务器返回的某个地区的所有天气信息存储到SharedPreferences 文件中。
     *  p.s. Utility 是一个工具类 既不是 Context 也不是 Activity
     *  要想得到 SharedPreference 的对象
     *  可以调用PreferenceManager 类中的 getDefaultSharedPreferences()静态方法
     *  它接收一个 Context 参数，并自动使用当前应用程序的包名作为前缀来命名 SharedPreferences 文件
     */
    private static boolean saveWeatherInfo(Context context, String cityName,
                                           String date, String weather, String temperature) {
        LogUtil.log("saveWeatherInfo", "saveWeatherInfo", LogUtil.DEBUG);

        SharedPreferences.Editor editor = PreferenceManager.getDefaultSharedPreferences(context).edit();

        // ChooseAreaActivity 是 LAUNCHER activity
        // 天气查询是在依次选定了 province,city,district后跳转到 WeatherActivity 中执行，这时城市信息已经选定
        // 往 SharedPreferences 对象中多装入一条 city_selected boolean标志数据
        // 第二次打开时 ChooseAreaActivity 启动就会再次要求选择地区 如果 city_selected 为 true
        // 应该跳转到 WeatherActivity 中
        editor.putBoolean(ChooseAreaActivity.CITY_SELECTED, true);
        editor.putString("city_name", cityName);
        editor.putString("date", date);
        editor.putString("weather", weather);
        editor.putString("temperature_range", temperature);

        editor.apply();

        return true;
    }

}
