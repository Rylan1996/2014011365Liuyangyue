package com.example.rylan.sharedpreferences;

import android.content.SharedPreferences;
import android.os.Bundle;
import android.support.design.widget.FloatingActionButton;
import android.support.design.widget.Snackbar;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.Toolbar;
import android.view.View;
import android.view.Menu;
import android.view.MenuItem;
import android.widget.Button;
import android.widget.Toast;

import java.text.SimpleDateFormat;
import java.util.Date;

public class MainActivity extends AppCompatActivity {
    private final static String SharedPreferencesFileName="config";

    private final static String Key_UserName="UserName";//用户名
    private final static String Key_ID="ID";//登录时间
    //private final static String Key_LoginDate="LoginDate";//登录时间
    private final static String Key_UserType="UserType";

    SharedPreferences preferences;
    SharedPreferences.Editor editor;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        preferences=getSharedPreferences(SharedPreferencesFileName,MODE_PRIVATE);
        editor=preferences.edit();
        Button btnWrite=(Button)findViewById(R.id.ButtonWrite);
        Button btnRead=(Button)findViewById(R.id.ButtonRead);

        btnWrite.setOnClickListener(new View.OnClickListener() {
            public void onClick(View view) {
                //SimpleDateFormat simpleDateFormat = new SimpleDateFormat("yyyy-MM-dd hh:mm:ss");
                //String strLoginDate = simpleDateFormat.format(new Date());
                editor.putString(Key_UserName, "LiuYangyue");
                editor.putString(Key_ID, "2014011365");
                editor.putInt(Key_UserType, 1);
                editor.apply();
            }
        });

        btnRead.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                String strUserName = preferences.getString(Key_UserName, null);
                String strID = preferences.getString(Key_ID, null);
                int nUserType=preferences.getInt(Key_UserType,0);
                if (strUserName != null && strID != null)
                    Toast.makeText(MainActivity.this, "姓名:" + strUserName + "学号:" + strID+"用户类型:"+nUserType, Toast.LENGTH_LONG).show();
                else
                    Toast.makeText(MainActivity.this, "无数据",Toast.LENGTH_LONG).show();
            }
        });
                Toolbar toolbar = (Toolbar) findViewById(R.id.toolbar);
        setSupportActionBar(toolbar);

        FloatingActionButton fab = (FloatingActionButton) findViewById(R.id.fab);
        fab.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Snackbar.make(view, "Replace with your own action", Snackbar.LENGTH_LONG)
                        .setAction("Action", null).show();
            }
        });
    }

    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        // Inflate the menu; this adds items to the action bar if it is present.
        getMenuInflater().inflate(R.menu.menu_main, menu);
        return true;
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        // Handle action bar item clicks here. The action bar will
        // automatically handle clicks on the Home/Up button, so long
        // as you specify a parent activity in AndroidManifest.xml.
        int id = item.getItemId();

        //noinspection SimplifiableIfStatement
        if (id == R.id.action_settings) {
            return true;
        }

        return super.onOptionsItemSelected(item);
    }
}
