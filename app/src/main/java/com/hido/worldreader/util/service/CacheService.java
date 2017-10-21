package com.hido.worldreader.util.service;

import android.app.Service;
import android.content.BroadcastReceiver;
import android.content.Context;
import android.content.Intent;
import android.content.IntentFilter;
import android.os.IBinder;
import android.support.annotation.Nullable;
import android.support.v4.content.LocalBroadcastManager;
import android.support.v7.preference.PreferenceManager;

import com.hido.worldreader.data.bean.ZhihuDailyContent;
import com.hido.worldreader.data.bean.ZhihuDailyNewsQuestion;
import com.hido.worldreader.data.retrofit.RetrofitService;
import com.hido.worldreader.data.room.AppDataBase;
import com.hido.worldreader.data.room.DatabaseCreator;

import java.io.IOException;
import java.util.Calendar;
import java.util.List;

import retrofit2.Retrofit;
import retrofit2.converter.gson.GsonConverterFactory;

/**
 * Created by hidohuang on 2017/9/24.
 */

public class CacheService extends Service {
    
    public static final String ITEM_ID = "item_id";
    
    public static final String BROADCAST_FILTER_ACTION = "com.hido.worldreader.LOCAL_BROADCAST";
    
    private AppDataBase mDb = null;
    
    private LocalReceiver mLocalReceiver;
    
    private RetrofitService.ZhihuDailyService mZhihuDailyService;
    
    public class LocalReceiver extends BroadcastReceiver {
        
        @Override
        public void onReceive(Context context, Intent intent) {
            
            int id = intent.getIntExtra(ITEM_ID, 0);
            
            if (mDb == null) {
                DatabaseCreator creator = DatabaseCreator.getInstance();
                if (!creator.isDatabaseCreated()) {
                    creator.createDb(CacheService.this);
                }
                mDb = creator.getDatabase();
            }
            
            new Thread(() -> {
                
                if (mDb != null && mDb.zhihuDailyContentDao().queryContentById(id) == null) {
                    mDb.beginTransaction();
                    try {
                        // Call execute() rather than enqueue()
                        // or you will go back to main thread in onResponse() function.
                        // TODO: 2017/10/16  
                        ZhihuDailyContent tmp = mZhihuDailyService.getZhihuContent(id).execute().body();
                        if (tmp != null) {
                            mDb.zhihuDailyContentDao().insert(tmp);
                            mDb.setTransactionSuccessful();
                        }
                    } catch (IOException e) {
                        e.printStackTrace();
                    } finally {
                        mDb.endTransaction();
                        clearTimeoutContent();
                        
                    }
                }
            }).start();
            
        }
    }
    
    @Override
    public void onCreate() {
        super.onCreate();
        
        mLocalReceiver = new LocalReceiver();
        IntentFilter intentFilter = new IntentFilter();
        intentFilter.addAction(BROADCAST_FILTER_ACTION);
        LocalBroadcastManager.getInstance(this).registerReceiver(mLocalReceiver, intentFilter);
        
        mZhihuDailyService = new Retrofit.Builder().baseUrl(RetrofitService.ZHIHU_DAILY).addConverterFactory(GsonConverterFactory.create()).build().create(RetrofitService.ZhihuDailyService.class);
        
    }
    
    @Nullable
    @Override
    public IBinder onBind(Intent intent) {
        return null;
    }
    
    @Override
    public void onDestroy() {
        super.onDestroy();
        if (mLocalReceiver != null) {
            LocalBroadcastManager.getInstance(this).unregisterReceiver(mLocalReceiver);
        }
    }
    
    
    public void clearTimeoutContent() {
        if (mDb == null) {
            DatabaseCreator creator = DatabaseCreator.getInstance();
            if (!creator.isDatabaseCreated()) {
                creator.createDb(this);
            }
            mDb = creator.getDatabase();
        }
        
        int cacheDays = switchDays(Integer.parseInt(PreferenceManager.getDefaultSharedPreferences(CacheService.this).getString("time_of_saving_articles", "2")));
        
        new Thread(() -> {
            if (mDb != null) {
                mDb.beginTransaction();
                try {
                    long timeInMillis = Calendar.getInstance().getTimeInMillis() - cacheDays * 24 * 60 * 60 * 1000;
                    List<ZhihuDailyNewsQuestion> zhihuDailyNewsQuestionList = mDb.zhihuDailyNewsDao().queryAllTimeoutItems(timeInMillis);
                    for (ZhihuDailyNewsQuestion item : zhihuDailyNewsQuestionList) {
                        mDb.zhihuDailyNewsDao().delete(item);
                        if (item.getId() != 0) {
                
                            mDb.zhihuDailyContentDao().delete(mDb.zhihuDailyContentDao().queryContentById(item.getId()));
                        }
                    }
        
        
                } catch (NullPointerException e) {
    
                } finally {
                    mDb.endTransaction();
                }
            }
            
        }).start();
        
    }
    
    private int switchDays(int day) {
        switch (day) {
            case 0:
                return 1;
            case 1:
                return 5;
            case 2:
                return 15;
            case 3:
                return 30;
            default:
                return 15;
        }
    }
}
