package com.example.rylan.weather.module;

import android.content.ContentValues;
import android.content.Context;
import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;

import com.example.rylan.weather.db.WeatherOpenHelper;

import java.util.ArrayList;
import java.util.List;

/**
 * Created by Rylan on 2016/10/20.
 */
public class WeatherDB {// 单例
    /**
     * 数据库名
     */
    private static final String DB_NAME = "weather";
    /**
     * 数据库版本
     */
    private static final int VERSION = 1;
    private static WeatherDB sWeatherDB;
    private SQLiteDatabase mDB;

    /**
     * 将构造方法私有化，传入 Context
     */
    private WeatherDB(Context context) {
        WeatherOpenHelper helper = new WeatherOpenHelper(context, DB_NAME, null, VERSION);
        mDB = helper.getWritableDatabase();
        
    }

    /**
     * 获取WeatherDB的实例。
     */
    public synchronized static WeatherDB getInstance(Context context) {
        if (sWeatherDB == null) {
            sWeatherDB = new WeatherDB(context);
        }
        return sWeatherDB;
    }

    /**
     * 将Province实例存储到数据库。
     */
    public void saveProvince(Province province) {
        if (province != null) {
            ContentValues values = new ContentValues();

            //Province 表中 id 的值设为了自增长，数据入库时会自动生成，不用设置 看来这话不一定对
            values.put("province_name", province.getProvinceName());

            mDB.insert("Province", null, values);
        }
    }

    /**
     * 从数据库读取省份信息。
     */
    public List<Province> loadProvince() {
        List<Province> list = new ArrayList<>();

        Cursor cursor = mDB.query("Province", null, null, null, null, null, null);
        if (cursor.moveToFirst()) {
            do {
                Province province = new Province();

                province.setId(cursor.getInt(cursor.getColumnIndex("id")));
                province.setProvinceName(cursor.getString(cursor.getColumnIndex("province_name")));

                list.add(province);
            } while (cursor.moveToNext());
        }
        cursor.close();

        return list;
    }

    /**
     * 将City实例存储到数据库。
     */
    public void saveCity(City city) {
        if (city != null) {
            ContentValues values = new ContentValues();

            values.put("city_name", city.getCityName());
            values.put("province_id", city.getProvinceId());

            mDB.insert("City", null, values);
        }
    }

    /**
     * 从数据库读取某省下所有的城市信息。
     */
    public List<City> loadCity(int provinceId) {
        List<City> list = new ArrayList<>();
        Cursor cursor = mDB.query("City", null,
                "province_id = ?", new String[]{String.valueOf(provinceId)},
                null, null, null);
        if (cursor.moveToFirst()) {
            do {
                City city = new City();

                city.setId(cursor.getInt(cursor.getColumnIndex("id")));
                city.setCityName(cursor.getString(cursor.getColumnIndex("city_name")));
                city.setProvinceId(provinceId);

                list.add(city);
            } while (cursor.moveToNext());
        }
        cursor.close();
        return list;
    }

    /**
     * 将County实例存储到数据库。
     */
    public void saveDistrict(District district) {
        if (district != null) {
            ContentValues values = new ContentValues();

            values.put("district_name", district.getDistrictName());
            values.put("city_id", district.getCityId());

            mDB.insert("District", null, values);
        }
    }

    /**
     * 从数据库读取某城市下所有的县信息。
     */
    public List<District> loadDistrict(int cityId) {
        List<District> list = new ArrayList<>();
        Cursor cursor = mDB.query("District", null,
                "city_id = ?", new String[]{String.valueOf(cityId)},
                null, null, null);
        if (cursor.moveToFirst()) {
            do {
                District district = new District();

                district.setId(cursor.getInt(cursor.getColumnIndex("id")));
                district.setDistrictName(cursor.getString(cursor.getColumnIndex("district_name")));
                district.setCityId(cityId);

                list.add(district);
            } while (cursor.moveToNext());
        }
        cursor.close();
        return list;
    }
}
