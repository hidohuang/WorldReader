package com.hido.worldreader.data.datasource;

import android.support.annotation.NonNull;

import com.hido.worldreader.data.bean.ZhihuDailyContent;

/**
 * Created by hidohuang on 2017/9/28.
 */

public interface ZhihuDailyContentDataSource {
    
    interface LoadZhihuDailyContentCallback {
        
        void onContentLoaded(@NonNull ZhihuDailyContent content);
        
        void onDataNotAvailable();
        
    }
    
    void getZhihuDailyContent(int id, @NonNull LoadZhihuDailyContentCallback callback);
    
    void saveContent(@NonNull ZhihuDailyContent content);
    
}
