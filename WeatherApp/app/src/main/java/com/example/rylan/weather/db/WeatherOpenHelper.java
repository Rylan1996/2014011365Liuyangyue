package com.example.rylan.weather.db;

import android.content.Context;
import android.database.sqlite.SQLiteDatabase;
import android.database.sqlite.SQLiteOpenHelper;

/**
 * Created by Rylan on 2016/10/20.
 */
public class WeatherOpenHelper extends SQLiteOpenHelper {
    /**
     * Province表建表语句
     */
    private static final String CREATE_PROVINCE = "create table Province ("
            + "id integer primary key autoincrement, "
            + "province_name text)";

    /**
     * City表建表语句
     */
    private static final String CREATE_CITY = "create table City ("
            + "id integer primary key autoincrement, "
            + "city_name text, "
            + "province_id integer)";

    /**
     * District表建表语句
     */
    private static final String CREATE_DISTRICT = "create table District("
            + "id integer primary key autoincrement, "
            + "district_name text, "
            + "city_id integer)";

    public WeatherOpenHelper(Context context, String name, SQLiteDatabase.CursorFactory factory, int version) {
        super(context, name, factory, version);
    }

    @Override
    public void onCreate(SQLiteDatabase db) {
        db.execSQL(CREATE_PROVINCE); // 创建Province表
        db.execSQL(CREATE_CITY); // 创建City表
        db.execSQL(CREATE_DISTRICT); // 创建District表
    }

    @Override
    public void onUpgrade(SQLiteDatabase db, int oldVersion, int newVersion) {

    }
}
