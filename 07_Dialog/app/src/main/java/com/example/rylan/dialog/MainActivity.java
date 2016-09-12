package com.example.rylan.dialog;

import android.content.DialogInterface;
import android.os.Bundle;
import android.support.design.widget.FloatingActionButton;
import android.support.design.widget.Snackbar;
import android.support.v7.app.AlertDialog;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.Toolbar;
import android.view.LayoutInflater;
import android.view.View;
import android.view.Menu;
import android.view.MenuItem;
import android.widget.Button;
import android.widget.EditText;
import android.widget.Toast;
import java.util.Objects;

public class MainActivity extends AppCompatActivity {
    private final  String ID ="abc";
    private final  String PASSWORD ="123";

    @Override
    protected void onCreate(Bundle savedInstanceState) {


        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
        Toolbar toolbar = (Toolbar) findViewById(R.id.toolbar);
        setSupportActionBar(toolbar);

        FloatingActionButton fab = (FloatingActionButton) findViewById(R.id.fab);
        fab.setOnClickListener(new View.OnClickListener() {
            public void onClick(View view) {
                Snackbar.make(view, "Replace with your own action", Snackbar.LENGTH_LONG)
                        .setAction("Action", null).show();
            }
        });

        Button button = (Button) findViewById(R.id.buttonLogin);
        button.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                AlertDialog.Builder builder = new AlertDialog.Builder(MainActivity.this);
                // Get the layout inflater
                //LayoutInflater inflater = getLayoutInflater();

                LayoutInflater inflater = LayoutInflater.from(MainActivity.this);

                final View DialogView = inflater.inflate(R.layout.login, null);

                builder.setView(inflater.inflate(R.layout.login, null))
                        .setTitle("登陆")
                        // Add action buttons
                        .setView(DialogView)
                        .setPositiveButton("登录", new DialogInterface.OnClickListener() {
                            @Override

                            public void onClick(DialogInterface dialog, int id) {
                                // login

                                EditText userid =(EditText) DialogView.findViewById(R.id.editTextId);
                                EditText password =(EditText) DialogView.findViewById(R.id.editTextPassWord);
                                String strId = userid.getText().toString().trim();
                                String strPassword = password.getText().toString().trim();
                                boolean criId=strId.equals(ID);
                                boolean criPassword=strPassword.equals(PASSWORD);

                                if(criId && criPassword) {
                                    Toast.makeText(MainActivity.this, "登录成功", Toast.LENGTH_LONG).show();
                                }
                                else {
                                    Toast.makeText(MainActivity.this, "账号或密码错误", Toast.LENGTH_LONG).show();
                                }
                            }
                        });

                builder.setNegativeButton("取消", new DialogInterface.OnClickListener() {
                    public void onClick(DialogInterface dialog, int id) {
                        Toast.makeText(MainActivity.this,"取消登录", Toast.LENGTH_LONG).show();
                    }
                });
                builder.show();
            }



        });


        Button inf = (Button) findViewById(R.id.buttonApply);
        inf.setOnClickListener(new View.OnClickListener() {

            @Override
            public void onClick(View v) {
                AlertDialog.Builder builder = new AlertDialog.Builder(MainActivity.this);
                builder.setMessage("显示内容XXX")
                        .setTitle("提示对话框");

                builder.setPositiveButton("确定", new DialogInterface.OnClickListener() {

                    @Override
                    public void onClick(DialogInterface dialog, int which) {
                        Toast.makeText(MainActivity.this, "确定", Toast.LENGTH_LONG).show();
                    }
                });

                builder.setNeutralButton("忽略", new DialogInterface.OnClickListener() {

                    @Override
                    public void onClick(DialogInterface dialog, int which) {
                        Toast.makeText(MainActivity.this, "忽略", Toast.LENGTH_LONG).show();
                    }

                });
                builder.show();
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
