package com.hido.worldreader.data.datasource;

import android.support.annotation.NonNull;

import com.hido.worldreader.data.bean.ZhihuDailyNewsQuestion;

import java.util.List;

/**
 * Created by hidohuang on 2017/9/28.
 */

public interface ZhihuDailyDataSource {
    
    interface LoadZhihuDailyNewsCallback {
        
        void onNewsLoaded(@NonNull List<ZhihuDailyNewsQuestion> list);
        
        void onDataNotAvailable();
        
    }
    
    interface GetNewsItemCallback {
        
        void onItemLoaded(@NonNull ZhihuDailyNewsQuestion item);
        
        void onDataNotAvailable();
        
    }
    
    void getZhihuDailyNews(boolean forceUpdate, boolean clearCache, long date, @NonNull LoadZhihuDailyNewsCallback callback);
    
    void getFavorites(@NonNull LoadZhihuDailyNewsCallback callback);
    
    void getItem(int itemId, @NonNull GetNewsItemCallback callback);
    
    void favoriteItem(int itemId, boolean favorite);
    
    void saveAll(@NonNull List<ZhihuDailyNewsQuestion> list);
    
}
