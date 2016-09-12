package com.example.rylan.showlistview;

import android.app.Activity;
import android.os.Bundle;
import android.support.design.widget.FloatingActionButton;
import android.support.design.widget.Snackbar;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.Toolbar;
import android.view.View;
import android.view.Menu;
import android.view.MenuItem;
import android.widget.ListView;
import android.widget.SimpleAdapter;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

public class MainActivity extends Activity {
    private final static String NAME="name";
    private final static String SEX="sex";
    private final static String SCHOOL="school";
    private final static String PHONE="phone";


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        String[] name={"张三","李四","王五","赵六"};
        String[] sex={"男","女","男","男"};
        String[] school={"计科1402 2014011235","软工1402 2014011444","网工1402 2014011765","信息1402 2014011512"};
        String[] phone={"13211044332","156343566645","010-82324455","95588"};

        List<Map<String,Object>> items=new ArrayList<Map<String,Object>>();

        for(int i=0;i<name.length;i++) {
            Map<String,Object> item=new HashMap<String,Object>();
            item.put(NAME, name[i]);
            item.put(SEX, sex[i]);
            item.put(SCHOOL, school[i]);
            item.put(PHONE, phone[i]);
            items.add(item);
        }

        SimpleAdapter adapter=new SimpleAdapter(this,items,R.layout.item,new String[]{NAME,SEX,SCHOOL,PHONE},new int[]{R.id.txt_Name,R.id.txt_Sex,R.id.txt_School,R.id.txt_Phone});

        ListView list=(ListView)findViewById(R.id.list);

        list.setAdapter(adapter);
    }
}
