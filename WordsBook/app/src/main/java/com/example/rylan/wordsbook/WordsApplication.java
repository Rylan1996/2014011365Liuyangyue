package com.example.rylan.wordsbook;

import android.app.Application;
import android.content.Context;

/**
 * Created by rylan on 2016-10-12.
 */
public class WordsApplication extends Application {
    private static Context context;
    public static Context getContext(){
        return WordsApplication.context;
    }


    @Override
    public void onCreate() {
        super.onCreate();
        WordsApplication.context=getApplicationContext();
    }
}
