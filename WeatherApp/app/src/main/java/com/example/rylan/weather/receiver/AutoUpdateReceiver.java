package com.example.rylan.weather.receiver;

import android.content.BroadcastReceiver;
import android.content.Context;
import android.content.Intent;

import com.example.rylan.weather.service.AutoUpdateService;
import com.example.rylan.weather.util.LogUtil;

/**
 * Created by Rylan on 2016/10/20.
 */
public class AutoUpdateReceiver extends BroadcastReceiver {
    @Override
    public void onReceive(Context context, Intent intent) {
        LogUtil.log("onReceive", "onReceive", LogUtil.DEBUG);

        Intent i = new Intent(context, AutoUpdateService.class);
        context.startActivity(i);
    }
}
