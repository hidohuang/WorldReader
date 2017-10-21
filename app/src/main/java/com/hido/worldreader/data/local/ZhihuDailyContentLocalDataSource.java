package com.hido.worldreader.data.local;

import android.content.Context;
import android.os.AsyncTask;
import android.support.annotation.NonNull;
import android.support.annotation.Nullable;

import com.hido.worldreader.data.bean.ZhihuDailyContent;
import com.hido.worldreader.data.datasource.ZhihuDailyContentDataSource;
import com.hido.worldreader.data.room.AppDataBase;
import com.hido.worldreader.data.room.DatabaseCreator;

/**
 * Created by hidohuang on 2017/9/28.
 */

public class ZhihuDailyContentLocalDataSource implements ZhihuDailyContentDataSource {
    
    
    @Nullable
    private static ZhihuDailyContentLocalDataSource INSTANCE = null;
    
    @Nullable
    private AppDataBase mDb = null;
    
    private ZhihuDailyContentLocalDataSource(@NonNull Context context) {
        DatabaseCreator creator = DatabaseCreator.getInstance();
        if (!creator.isDatabaseCreated()) {
            creator.createDb(context);
        }
        mDb = creator.getDatabase();
    }
    
    public static ZhihuDailyContentLocalDataSource getInstance(@NonNull Context context) {
        if (INSTANCE == null) {
            INSTANCE = new ZhihuDailyContentLocalDataSource(context);
        }
        return INSTANCE;
    }
    
    @Override
    public void getZhihuDailyContent(int id, @NonNull LoadZhihuDailyContentCallback callback) {
        if (mDb == null) {
            callback.onDataNotAvailable();
            return;
        }
        
        new AsyncTask<Void, Void, ZhihuDailyContent>() {
            
            @Override
            protected ZhihuDailyContent doInBackground(Void... voids) {
                return mDb.zhihuDailyContentDao().queryContentById(id);
            }
            
            @Override
            protected void onPostExecute(ZhihuDailyContent content) {
                super.onPostExecute(content);
                if (content == null) {
                    callback.onDataNotAvailable();
                } else {
                    callback.onContentLoaded(content);
                }
            }
            
        }.execute();
    }
    
    @Override
    public void saveContent(@NonNull ZhihuDailyContent content) {
        if (mDb == null) {
            mDb = DatabaseCreator.getInstance().getDatabase();
        }
        
        if (mDb != null) {
            new Thread(() -> {
                mDb.beginTransaction();
                try {
                    mDb.zhihuDailyContentDao().insert(content);
                    mDb.setTransactionSuccessful();
                } finally {
                    mDb.endTransaction();
                }
            }).start();
        }
    }

}
