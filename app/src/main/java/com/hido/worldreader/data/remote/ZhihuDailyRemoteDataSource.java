package com.hido.worldreader.data.remote;

import android.support.annotation.NonNull;

import com.hido.worldreader.util.DateFormatUtil;
import com.hido.worldreader.data.bean.ZhihuDailyNews;
import com.hido.worldreader.data.bean.ZhihuDailyNewsQuestion;
import com.hido.worldreader.data.datasource.ZhihuDailyDataSource;
import com.hido.worldreader.data.retrofit.RetrofitService;

import java.util.List;

import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;
import retrofit2.Retrofit;
import retrofit2.converter.gson.GsonConverterFactory;

/**
 * Created by hidohuang on 2017/9/28.
 */

public class ZhihuDailyRemoteDataSource implements ZhihuDailyDataSource {
    
    
    @NonNull
    private static ZhihuDailyRemoteDataSource INSTANCE =null;
    
    public static ZhihuDailyRemoteDataSource getInstance() {
        if (INSTANCE == null) {
            INSTANCE=new ZhihuDailyRemoteDataSource();
        }
        return INSTANCE;
    }
    
    public ZhihuDailyRemoteDataSource() {
    }
    
    @Override
    
    public void getZhihuDailyNews(boolean forceUpdate, boolean clearCache, long date, @NonNull LoadZhihuDailyNewsCallback callback) {
    
        Retrofit retrofit = new Retrofit.Builder()
                .baseUrl(RetrofitService.ZHIHU_DAILY)
                .addConverterFactory(GsonConverterFactory.create())
                .build();
    
        RetrofitService.ZhihuDailyService service = retrofit.create(RetrofitService.ZhihuDailyService.class);
    
        service.getZhihuList(DateFormatUtil.formatZhihuDailyDateLongToString(date))
                .enqueue(new Callback<ZhihuDailyNews>() {
                    @Override
                    public void onResponse(Call<ZhihuDailyNews> call, Response<ZhihuDailyNews> response) {
                    
                        // Note: Only the timestamp of zhihu daily was set in remote source.
                        // The other two was set in repository due to structure of returning json.
                        try {
        
                            long timestamp = DateFormatUtil.formatZhihuDailyDateStringToLong(response.body().getDate());
        
        
                            for (ZhihuDailyNewsQuestion item : response.body().getStories()) {
                                item.setTimestamp(timestamp);
                            }
                            callback.onNewsLoaded(response.body().getStories());
                        } catch (NullPointerException e) {
                            callback.onDataNotAvailable();
                            
                        }
                    }
                
                    @Override
                    public void onFailure(Call<ZhihuDailyNews> call, Throwable t) {
                        callback.onDataNotAvailable();
                    }
                });
    
    }
    
    @Override
    public void getFavorites(@NonNull LoadZhihuDailyNewsCallback callback) {
    
    }
    
    @Override
    public void getItem(int itemId, @NonNull GetNewsItemCallback callback) {
    
    }
    
    @Override
    public void favoriteItem(int itemId, boolean favorite) {
    
    }
    
    @Override
    public void saveAll(@NonNull List<ZhihuDailyNewsQuestion> list) {
    
    }
}
