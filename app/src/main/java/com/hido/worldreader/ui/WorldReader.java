package com.hido.worldreader.ui;

import android.app.Application;
import android.os.Looper;
import android.preference.PreferenceManager;
import android.support.v7.app.AppCompatDelegate;

/**
 * Created by hidohuang on 2017/9/29.
 */

public class WorldReader extends Application {
    
    
    @Override
    public void onCreate() {
        //Looper.loop();
        super.onCreate();
        if (PreferenceManager.getDefaultSharedPreferences(getApplicationContext()).getBoolean("night_mode", false)) {
            AppCompatDelegate.setDefaultNightMode(AppCompatDelegate.MODE_NIGHT_YES);
        } else {
            AppCompatDelegate.setDefaultNightMode(AppCompatDelegate.MODE_NIGHT_NO);
        }
    }
}
