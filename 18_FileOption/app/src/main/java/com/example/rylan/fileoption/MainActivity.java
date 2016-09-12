package com.example.rylan.fileoption;

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

import java.io.BufferedInputStream;
import java.io.BufferedOutputStream;
import java.io.FileInputStream;
import java.io.FileOutputStream;
import java.io.InputStream;
import java.io.OutputStream;
import java.nio.charset.StandardCharsets;

public class MainActivity extends AppCompatActivity {
    private final static String MyFileName="myfile";
    @Override
   protected void onCreate(Bundle savedInstanceState) {
       /* super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
        Toolbar toolbar = (Toolbar) findViewById(R.id.toolbar);
        setSupportActionBar(toolbar);

        FloatingActionButton fab = (FloatingActionButton) findViewById(R.id.fab);
        fab.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Snackbar.make(view, "Replace with your own action", Snackbar.LENGTH_LONG)
                        .setAction("Action", null).show();
            }
        });*/


        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        Button buttonWrite=(Button)findViewById(R.id.buttonWrite);
        Button buttonRead=(Button)findViewById(R.id.buttonRead);
        buttonWrite.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                OutputStream out=null;
                try {
                    FileOutputStream fileOutputStream=openFileOutput(MyFileName,MODE_PRIVATE);
                    out=new BufferedOutputStream(fileOutputStream);
                    String content="LiuYangyue 2014011365";
                try {
                    out.write(content.getBytes(StandardCharsets.UTF_8));
                }
                finally {
                    if(out!=null)
                        out.close();
                }
            }
            catch (Exception e){
                e.printStackTrace();
            }
        }
    });

    buttonRead.setOnClickListener(new View.OnClickListener() {
        @Override
        public void onClick(View view) {
            InputStream in=null;
            try {
                FileInputStream fileInputStream = openFileInput(MyFileName);
                in=new BufferedInputStream(fileInputStream);

                int c;
                StringBuilder stringBuilder=new StringBuilder("");
                try{
                    while ((c=in.read())!=-1) {
                        stringBuilder.append((char)c);
                    }
                    Toast.makeText(MainActivity.this,stringBuilder.toString(),Toast.LENGTH_LONG).show();
                }
                finally {
                    if(in!=null)
                        in.close();
                }
            }
            catch (Exception e){
                e.printStackTrace();
            }


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
