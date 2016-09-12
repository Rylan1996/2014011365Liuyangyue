package com.example.rylan.wordsqloption;

import android.app.AlertDialog;
import android.content.ContentValues;
import android.content.DialogInterface;
import android.content.Intent;
import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;
import android.os.Bundle;
import android.support.design.widget.FloatingActionButton;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.Toolbar;
import android.util.Log;
import android.view.ContextMenu;
import android.view.View;
import android.view.Menu;
import android.view.MenuItem;
import android.widget.AdapterView;
import android.widget.EditText;
import android.widget.ListView;
import android.widget.SimpleAdapter;
import android.widget.TableLayout;
import android.widget.TextView;
import android.widget.Toast;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.Map;

public class MainActivity extends AppCompatActivity {
    WordsDBHelper Helper;

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
                InsertDialog();
            }
        });

        ListView list = (ListView) findViewById(R.id.lstWords);
        registerForContextMenu(list);

        Helper = new WordsDBHelper(this);
        ArrayList<Map<String, String>> items=getAll();
        setWordsListView(items);

    }

    @Override
    protected void onDestroy() {
        super.onDestroy();
        Helper.close();
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
        switch (id) {
            case R.id.action_search:
                SearchDialog();
                return true;
            case R.id.action_insert:
                InsertDialog();
                return true;
        }


        return super.onOptionsItemSelected(item);
    }

    @Override
    public void onCreateContextMenu(ContextMenu menu, View v, ContextMenu.ContextMenuInfo menuInfo) {
        getMenuInflater().inflate(R.menu.textmenu, menu);
    }

    @Override
    public boolean onContextItemSelected(MenuItem item) {
        TextView textId=null;
        TextView textWord=null;
        TextView textMeaning=null;
        TextView textSample=null;

        AdapterView.AdapterContextMenuInfo info=null;
        View itemView=null;

        switch (item.getItemId()){
            case R.id.action_delete:
                info=(AdapterView.AdapterContextMenuInfo)item.getMenuInfo();
                itemView=info.targetView;
                textId =(TextView)itemView.findViewById(R.id.textId);
                if(textId!=null){
                    String strId=textId.getText().toString();
                    DeleteDialog(strId);
                }
                break;
            case R.id.action_update:
                info=(AdapterView.AdapterContextMenuInfo)item.getMenuInfo();
                itemView=info.targetView;
                textId =(TextView)itemView.findViewById(R.id.textId);
                textWord =(TextView)itemView.findViewById(R.id.textViewWord);
                textMeaning =(TextView)itemView.findViewById(R.id.textViewMeaning);
                textSample =(TextView)itemView.findViewById(R.id.textViewSample);
                if(textId!=null && textWord!=null && textMeaning!=null && textSample!=null){
                    String strId=textId.getText().toString();
                    String strWord=textWord.getText().toString();
                    String strMeaning=textMeaning.getText().toString();
                    String strSample=textSample.getText().toString();
                    UpdateDialog(strId, strWord, strMeaning, strSample);
                }
                break;
        }
        return true;
    }

    private void setWordsListView(ArrayList<Map<String, String>> items){
        SimpleAdapter adapter = new SimpleAdapter(this, items, R.layout.item,
                new String[]{Words.Word._ID,Words.Word.COLUMN_NAME_WORD, Words.Word.COLUMN_NAME_MEANING, Words.Word.COLUMN_NAME_SAMPLE},
                new int[]{R.id.textId,R.id.textViewWord, R.id.textViewMeaning, R.id.textViewSample});

        ListView list = (ListView) findViewById(R.id.lstWords);

        list.setAdapter(adapter);
    }

    private ArrayList<Map<String, String>> getAll() {
        SQLiteDatabase db = Helper.getReadableDatabase();

        String[] projection = {
                Words.Word._ID,
                Words.Word.COLUMN_NAME_WORD,
                Words.Word.COLUMN_NAME_MEANING,
                Words.Word.COLUMN_NAME_SAMPLE
        };

        String sortOrder =
                Words.Word.COLUMN_NAME_WORD + " DESC";

        Cursor c = db.query(
                Words.Word.TABLE_NAME,
                projection,
                null,
                null,
                null,
                null,
                sortOrder
        );
        return ConvertCursor2List(c);
    }



    private ArrayList<Map<String, String>> ConvertCursor2List(Cursor cursor) {
        ArrayList<Map<String, String>> result = new ArrayList<>();
        while (cursor.moveToNext()) {
            Map<String, String> map = new HashMap<>();
            map.put(Words.Word._ID, String.valueOf(cursor.getInt(0)));
            map.put(Words.Word.COLUMN_NAME_WORD, cursor.getString(1));
            map.put(Words.Word.COLUMN_NAME_MEANING, cursor.getString(2));
            map.put(Words.Word.COLUMN_NAME_SAMPLE, cursor.getString(3));
            result.add(map);
        }
        return result;
    }

    private void InsertUserSql(String strWord, String strMeaning, String strSample){
        String sql="insert into  words(word,meaning,sample) values(?,?,?)";

        //Gets the data repository in write mode*/
        SQLiteDatabase db = Helper.getWritableDatabase();
        db.execSQL(sql,new String[]{strWord,strMeaning,strSample});
    }

    private void Insert(String strWord, String strMeaning, String strSample) {

        SQLiteDatabase db = Helper.getWritableDatabase();

        // Create a new map of values, where column names are the keys
        ContentValues values = new ContentValues();
        values.put(Words.Word.COLUMN_NAME_WORD, strWord);
        values.put(Words.Word.COLUMN_NAME_MEANING, strMeaning);
        values.put(Words.Word.COLUMN_NAME_SAMPLE, strSample);

        // Insert the new row, returning the primary key value of the new row
        long newRowId;
        newRowId = db.insert(
                Words.Word.TABLE_NAME,
                null,
                values);
    }

    private void InsertDialog() {
        final TableLayout tableLayout = (TableLayout) getLayoutInflater().inflate(R.layout.insert, null);
        new AlertDialog.Builder(this)
                .setTitle("新增单词")
                .setView(tableLayout)
                .setPositiveButton("确定", new DialogInterface.OnClickListener() {
                    @Override
                    public void onClick(DialogInterface dialogInterface, int i) {
                        String strWord=((EditText)tableLayout.findViewById(R.id.txtWord)).getText().toString();
                        String strMeaning=((EditText)tableLayout.findViewById(R.id.txtMeaning)).getText().toString();
                        String strSample=((EditText)tableLayout.findViewById(R.id.txtSample)).getText().toString();

                        Insert(strWord, strMeaning, strSample);

                        ArrayList<Map<String, String>> items=getAll();
                        setWordsListView(items);

                    }
                })
                .setNegativeButton("取消", new DialogInterface.OnClickListener() {
                    @Override
                    public void onClick(DialogInterface dialogInterface, int i) {

                    }
                })
                .create()
                .show();

    }

    private void DeleteUseSql(String strId) {
        String sql="delete from words where _id='"+strId+"'";

        //Gets the data repository in write mode*/
        SQLiteDatabase db = Helper.getReadableDatabase();
        db.execSQL(sql);
    }


    private void Delete(String strId) {
        SQLiteDatabase db = Helper.getReadableDatabase();
        String selection = Words.Word._ID + " = ?";
        String[] selectionArgs = {strId};
        db.delete(Words.Word.TABLE_NAME, selection, selectionArgs);
    }



    private void DeleteDialog(final String strId){
        new AlertDialog.Builder(this).setTitle("删除单词").setMessage("是否真的删除单词?").setPositiveButton("确定", new DialogInterface.OnClickListener() {
            @Override
            public void onClick(DialogInterface dialogInterface, int i) {
                DeleteUseSql(strId);
                //Delete(strId);
                setWordsListView(getAll());
            }
        }).setNegativeButton("取消", new DialogInterface.OnClickListener() {
            @Override
            public void onClick(DialogInterface dialogInterface, int i) {

            }
        }).create().show();
    }

    private void UpdateUseSql(String strId,String strWord, String strMeaning, String strSample) {
        SQLiteDatabase db = Helper.getReadableDatabase();
        String sql="update words set word=?,meaning=?,sample=? where _id=?";
        db.execSQL(sql, new String[]{strWord, strMeaning, strSample,strId});
    }

    private void Update(String strId,String strWord, String strMeaning, String strSample) {
        SQLiteDatabase db = Helper.getReadableDatabase();

        //Log.v("My",strId);
        //Log.v("My",strWord);
       // Log.v("My",strMeaning);
        //Log.v("My",strSample);

        // New value for one column
        ContentValues values = new ContentValues();
        values.put(Words.Word.COLUMN_NAME_WORD, strWord);
        values.put(Words.Word.COLUMN_NAME_MEANING, strMeaning);
        values.put(Words.Word.COLUMN_NAME_SAMPLE, strSample);

        String selection = Words.Word._ID + " = ?";
        String[] selectionArgs = {strId};

        int count = db.update(
                Words.Word.TABLE_NAME,
                values,
                selection,
                selectionArgs);
    }

    private void UpdateDialog(final String strId, final String strWord, final String strMeaning, final String strSample) {
        final TableLayout tableLayout = (TableLayout) getLayoutInflater().inflate(R.layout.insert, null);
        ((EditText)tableLayout.findViewById(R.id.txtWord)).setText(strWord);
        ((EditText)tableLayout.findViewById(R.id.txtMeaning)).setText(strMeaning);
        ((EditText)tableLayout.findViewById(R.id.txtSample)).setText(strSample);
        new AlertDialog.Builder(this)
                .setTitle("修改单词")
                .setView(tableLayout)
                .setPositiveButton("确定", new DialogInterface.OnClickListener() {
                    @Override
                    public void onClick(DialogInterface dialogInterface, int i) {
                        String strNewWord = ((EditText) tableLayout.findViewById(R.id.txtWord)).getText().toString();
                        String strNewMeaning = ((EditText) tableLayout.findViewById(R.id.txtMeaning)).getText().toString();
                        String strNewSample = ((EditText) tableLayout.findViewById(R.id.txtSample)).getText().toString();

                        UpdateUseSql(strId, strNewWord, strNewMeaning, strNewSample);
                        setWordsListView(getAll());
                    }
                })

                .setNegativeButton("取消", new DialogInterface.OnClickListener() {
                    @Override
                    public void onClick(DialogInterface dialogInterface, int i) {

                    }
                })
                .create()
                .show();


    }

    private ArrayList<Map<String, String>> SearchUseSql(String strWordSearch) {
        SQLiteDatabase db = Helper.getReadableDatabase();

        String sql="select * from words where word like ? order by word desc";
        Cursor c=db.rawQuery(sql,new String[]{"%"+strWordSearch+"%"});

        return ConvertCursor2List(c);
    }

    private ArrayList<Map<String, String>> Search(String strWordSearch) {
        SQLiteDatabase db = Helper.getReadableDatabase();

        String[] projection = {
                Words.Word._ID,
                Words.Word.COLUMN_NAME_WORD,
                Words.Word.COLUMN_NAME_MEANING,
                Words.Word.COLUMN_NAME_SAMPLE
        };

        String sortOrder =
                Words.Word.COLUMN_NAME_WORD + " DESC";

        String selection = Words.Word.COLUMN_NAME_WORD + " LIKE ?";
        String[] selectionArgs = {"%"+strWordSearch+"%"};

        Cursor c = db.query(
                Words.Word.TABLE_NAME,
                projection,
                selection,
                selectionArgs,
                null,
                null,
                sortOrder
        );

        return ConvertCursor2List(c);
    }

    private void SearchDialog() {
        final TableLayout tableLayout = (TableLayout) getLayoutInflater().inflate(R.layout.search_item, null);
        new AlertDialog.Builder(this)
                .setTitle("查找单词")
                .setView(tableLayout)
                .setPositiveButton("确定", new DialogInterface.OnClickListener() {
                    @Override
                    public void onClick(DialogInterface dialogInterface, int i) {
                        String txtSearchWord=((EditText)tableLayout.findViewById(R.id.txtSearchWord)).getText().toString();
                        ArrayList<Map<String, String>> items=null;
                        items=SearchUseSql(txtSearchWord);
                        // items=Search(txtSearchWord);

                        if(items.size()>0) {
                            Bundle bundle=new Bundle();
                            bundle.putSerializable("result",items);
                            Intent intent=new Intent(MainActivity.this,SearchActivity.class);
                            intent.putExtras(bundle);
                            startActivity(intent);
                        }else
                            Toast.makeText(MainActivity.this,"没有找到",Toast.LENGTH_LONG).show();


                    }
                })
                .setNegativeButton("取消", new DialogInterface.OnClickListener() {
                    @Override
                    public void onClick(DialogInterface dialogInterface, int i) {

                    }
                })
                .create()
                .show();
    }
}
