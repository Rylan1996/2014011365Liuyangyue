package com.example.rylan.weather.module;
/**
 * Created by Rylan on 2016/10/20.
 */
public class City {
    private int mId;//ID
    private String mCityName;//城市名
    private int mProvinceId;//省ID

    public int getId() {
        return mId;
    }

    public void setId(int id) {
        mId = id;
    }

    public String getCityName() {
        return mCityName;
    }

    public void setCityName(String cityName) {
        mCityName = cityName;
    }

    public int getProvinceId() {
        return mProvinceId;
    }

    public void setProvinceId(int provinceId) {
        mProvinceId = provinceId;
    }
}
