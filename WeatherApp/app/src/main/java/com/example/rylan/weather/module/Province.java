package com.example.rylan.weather.module;

import java.io.Serializable;

/**
 * Created by Rylan on 2016/10/20.
 */
public class Province implements Serializable{
    private int mId;
    private String mProvinceName;

    public int getId() {
        return mId;
    }

    public void setId(int id) {
        mId = id;
    }

    public String getProvinceName() {
        return mProvinceName;
    }

    public void setProvinceName(String provinceName) {
        mProvinceName = provinceName;
    }
}
