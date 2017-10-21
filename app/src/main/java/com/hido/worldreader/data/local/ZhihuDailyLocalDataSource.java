package com.hido.worldreader.data.local;

import android.content.Context;
import android.os.AsyncTask;
import android.support.annotation.NonNull;
import android.support.annotation.Nullable;

import com.hido.worldreader.data.bean.ZhihuDailyNewsQuestion;
import com.hido.worldreader.data.datasource.ZhihuDailyDataSource;
import com.hido.worldreader.data.room.AppDataBase;
import com.hido.worldreader.data.room.DatabaseCreator;

import java.util.List;

/**
 * Created by hidohuang on 2017/9/28.
 */

public class ZhihuDailyLocalDataSource implements ZhihuDailyDataSource {
    @Nullable
    private static ZhihuDailyLocalDataSource INSTANCE = null;
    
    @Nullable
    private AppDataBase mDb = null;
    
    private ZhihuDailyLocalDataSource(@NonNull Context context) {
        DatabaseCreator creator = DatabaseCreator.getInstance();
        if (!creator.isDatabaseCreated()) {
            creator.createDb(context);
        }
    }
    
    public static ZhihuDailyLocalDataSource getInstance(Context context) {
        if (INSTANCE == null) {
            INSTANCE = new ZhihuDailyLocalDataSource(context);
        }
        return INSTANCE;
    }
    
    public static void destoryInstance() {
        INSTANCE=null;
    }
    @Override
    public void getZhihuDailyNews(boolean forceUpdate, boolean clearCache, long date, @NonNull LoadZhihuDailyNewsCallback callback) {
    
    }
    
    @Override
    public void getFavorites(@NonNull LoadZhihuDailyNewsCallback callback) {
        if (mDb == null) {
            mDb = DatabaseCreator.getInstance().getDatabase();
        }
        
        if(mDb != null) {
            new AsyncTask<Void, Void, List<ZhihuDailyNewsQuestion>>() {
                
                @Override
                protected List<ZhihuDailyNewsQuestion> doInBackground(Void... voids) {
                    return mDb.zhihuDailyNewsDao().queryAllFavorites();
                }
                
                @Override
                protected void onPostExecute(List<ZhihuDailyNewsQuestion> list) {
                    super.onPostExecute(list);
                    if (list == null) {
                        callback.onDataNotAvailable();
                    } else {
                        callback.onNewsLoaded(list);
                    }
                }
            }.execute();
        }
    }
    
    @Override
    public void getItem(int itemId, @NonNull GetNewsItemCallback callback) {
        if (mDb == null) {
            mDb = DatabaseCreator.getInstance().getDatabase();
        }
        
        if (mDb != null) {
            new AsyncTask<Void, Void, ZhihuDailyNewsQuestion>() {
                @Override
                protected ZhihuDailyNewsQuestion doInBackground(Void... voids) {
                    return mDb.zhihuDailyNewsDao().queryItemById(itemId);
                }
                
                @Override
                protected void onPostExecute(ZhihuDailyNewsQuestion item) {
                    super.onPostExecute(item);
                    if (item == null) {
                        callback.onDataNotAvailable();
                    } else {
                        callback.onItemLoaded(item);
                    }
                }
            }.execute();
        }
    }
    
    @Override
    public void favoriteItem(int itemId, boolean favorite) {
        if (mDb == null) {
            mDb = DatabaseCreator.getInstance().getDatabase();
        }
        
        if (mDb != null) {
            new Thread(() -> {
                ZhihuDailyNewsQuestion tmp = mDb.zhihuDailyNewsDao().queryItemById(itemId);
                tmp.setFavorite(favorite);
                mDb.zhihuDailyNewsDao().update(tmp);
            }).start();
        }
    }
    
    @Override
    public void saveAll(@NonNull List<ZhihuDailyNewsQuestion> list) {
        
        if (mDb == null) {
            mDb = DatabaseCreator.getInstance().getDatabase();
        }
        
        if (mDb != null){
            new Thread(() -> {
                mDb.beginTransaction();
                try {
                    mDb.zhihuDailyNewsDao().insertAll(list);
                    mDb.setTransactionSuccessful();
                } finally {
                    mDb.endTransaction();
                }
            }).start();
        }
    }
}


