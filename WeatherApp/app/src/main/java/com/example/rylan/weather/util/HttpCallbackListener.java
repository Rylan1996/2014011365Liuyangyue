package com.example.rylan.weather.util;


import java.io.InputStream;


/**
 * Created by Rylan on 2016/10/20.
 */
public interface HttpCallbackListener {


    void onFinish(InputStream in);

    void onError(Exception e);
}
