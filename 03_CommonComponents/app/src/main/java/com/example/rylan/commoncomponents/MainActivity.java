package com.example.rylan.commoncomponents;

import android.os.Bundle;
import android.support.design.widget.FloatingActionButton;
import android.support.design.widget.Snackbar;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.Toolbar;
import android.view.View;
import android.view.Menu;
import android.view.MenuItem;
import android.widget.Button;
import android.widget.CheckBox;
import android.widget.EditText;
import android.widget.RadioButton;
import android.widget.TextView;

public class MainActivity extends AppCompatActivity {

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
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
        });

        Button button=(Button)findViewById(R.id.button);
         final EditText editTextResult=(EditText)findViewById(R.id.editText);

        button.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                editTextResult.setText("点击了BUTTON按钮！");
            }
        });

    }

    public void onCheckboxClicked(View view) {
        CheckBox checkBox1=(CheckBox)findViewById(R.id.checkBox1);
        CheckBox checkBox2=(CheckBox)findViewById(R.id.checkBox2);
        TextView textView=(TextView)findViewById(R.id.textView);

        boolean CBoxFlag1 = checkBox1.isChecked();
        boolean CBoxFlag2 = checkBox2.isChecked();

        if(CBoxFlag1==true&&CBoxFlag2==true)
            textView.setText("选择一和选择二");
        else if(CBoxFlag1)
            textView.setText("选择一");
        else if(CBoxFlag2)
            textView.setText("选择二");
        else if(CBoxFlag1==false&&CBoxFlag2==false)
            textView.setText("初始 TextView");

      /*  boolean CBoxFlag1=false;
        boolean CBoxFlag2=false;
        // Is the view now checked?
        boolean checked = ((CheckBox) view).isChecked();
        TextView textView=(TextView)findViewById(R.id.textView);
        String str=null;
        // Check which checkbox was clicked
        switch(view.getId()) {
            case R.id.checkBox1:
                if(checked){
                    CBoxFlag1=true;
                }else{
                    CBoxFlag1=false;
                }

                break;
            case R.id.checkBox2:
                if(checked){
                    CBoxFlag2=true;
                }else{
                    CBoxFlag2=false;
                }
                break;

        }
        if(CBoxFlag1==true&&CBoxFlag2==true)
            textView.setText("选择一和选择二");
        else if(CBoxFlag1)
            textView.setText("选择一");
        else if(CBoxFlag2)
            textView.setText("选择二");
        else if(CBoxFlag1==false&&CBoxFlag2==false)
            textView.setText("初始 TextView");

            */
    }


    public void onRadioButtonClicked(View view) {
        // Is the button now checked?
        boolean checked = ((RadioButton) view).isChecked();
        TextView textView=(TextView)findViewById(R.id.textView);

        // Check which radio button was clicked
        switch(view.getId()) {
            case R.id.radioButton1:
                if (checked)
                    textView.setText("用户选择了：单选一");
                break;
            case R.id.radioButton2:
                if (checked)
                    textView.setText("用户选择了：单选二");
                break;
        }
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
