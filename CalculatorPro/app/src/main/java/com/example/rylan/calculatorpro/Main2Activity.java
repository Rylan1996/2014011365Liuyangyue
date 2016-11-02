package com.example.rylan.calculatorpro;

import android.os.Bundle;
import android.support.design.widget.FloatingActionButton;
import android.support.design.widget.Snackbar;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.Toolbar;
import android.view.View;
import android.widget.EditText;
import android.widget.TextView;
import android.widget.Toast;
import android.view.View.OnClickListener;

import java.text.DecimalFormat;


public class Main2Activity extends AppCompatActivity {
    EditText edit=null;
    TextView length = null;
    TextView mass = null;
    TextView text = null;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main2);
        Toolbar toolbar = (Toolbar) findViewById(R.id.toolbar);
        setSupportActionBar(toolbar);

        edit = (EditText) findViewById(R.id.editText);
        text = (TextView) findViewById(R.id.textView);

        length = (TextView) findViewById(R.id.length);
        length.setOnClickListener(new lengthListener());
        mass = (TextView) findViewById(R.id.mass);
        mass.setOnClickListener(new massListener());

    }

    private class lengthListener implements OnClickListener {
        String result;
        double km = 0.0;
        double cm = 0.0;
        double mm = 0.0;
        double ft = 0.0;//英尺
        double yd = 0.0;//码
        DecimalFormat decimalFormat=new DecimalFormat(".000");
        @Override
        public void onClick(View v) {
            text.setText("");
            String content = edit.getText().toString();
            Double temp = Double.parseDouble(content);

            km=temp*0.001;
            cm=temp*100;
            mm=temp*1000;
            ft=temp*3.281;
            yd=temp*1.094;
            result =  content + "米的单位转换算为：\n";
            if(km<1)
                result+=0;
            result = result + decimalFormat.format(km) + "千米。\n"
                    + decimalFormat.format(cm) + "厘米。\n"
                    + decimalFormat.format(mm) + "毫米。\n"
                    + decimalFormat.format(ft) + "英尺。\n"
                    + decimalFormat.format(yd) + "码。\n";
            text.setText(result);
        }
    }

    private class massListener implements OnClickListener {
        String result;
        double t = 0.0;
        double lb = 0.0;
        double g = 0.0;
        double oz = 0.0;

        DecimalFormat decimalFormat=new DecimalFormat(".000");
        @Override
        public void onClick(View v) {
            text.setText("");
            String content = edit.getText().toString();
            Double temp = Double.parseDouble(content);
            t=temp*0.001;
            lb=temp*2.205;//磅
            g=temp*1000;
            oz=temp*35.2739;//盎司

            result =  content + "KG的单位转换算为：\n";
            if(t<1)
                result+=0;
            result = result + decimalFormat.format(t) + "吨。\n"
                    + decimalFormat.format(lb) + "磅。\n"
                    + decimalFormat.format(g) + "克。\n"
                    + decimalFormat.format(oz) + "盎司。\n";
            text.setText(result);
        }
    }



}
