package com.example.rylan.boardcast;

import android.content.BroadcastReceiver;
import android.content.Context;
import android.content.Intent;
import android.widget.Toast;

public class MyReceiver extends BroadcastReceiver {
    public MyReceiver() {
    }

    @Override
    public void onReceive(Context context, Intent intent) {
        String strMsg=intent.getStringExtra("broadcast");
        Toast.makeText(context,strMsg,Toast.LENGTH_LONG).show();
        //throw new UnsupportedOperationException("Not yet implemented");
    }
}
