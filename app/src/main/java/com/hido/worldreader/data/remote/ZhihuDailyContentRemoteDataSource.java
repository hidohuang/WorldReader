package com.hido.worldreader.data.remote;

import android.support.annotation.NonNull;
import android.support.annotation.Nullable;

import com.hido.worldreader.data.bean.ZhihuDailyContent;
import com.hido.worldreader.data.datasource.ZhihuDailyContentDataSource;
import com.hido.worldreader.data.retrofit.RetrofitService;

import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;
import retrofit2.Retrofit;
import retrofit2.converter.gson.GsonConverterFactory;

/**
 * Created by hidohuang on 2017/9/28.
 */

public class ZhihuDailyContentRemoteDataSource implements ZhihuDailyContentDataSource {
    
    @Nullable
    private static ZhihuDailyContentRemoteDataSource INSTANCE = null;
    
    // Prevent direct instantiation.
    private ZhihuDailyContentRemoteDataSource() {}
    
    public static ZhihuDailyContentRemoteDataSource getInstance() {
        if (INSTANCE == null) {
            INSTANCE = new ZhihuDailyContentRemoteDataSource();
        }
        return INSTANCE;
    }
    
    @Override
    public void getZhihuDailyContent(int id, @NonNull LoadZhihuDailyContentCallback callback) {
    
        Retrofit retrofit = new Retrofit.Builder()
                .baseUrl(RetrofitService.ZHIHU_DAILY)
                .addConverterFactory(GsonConverterFactory.create())
                .build();
    
        RetrofitService.ZhihuDailyService service = retrofit.create(RetrofitService.ZhihuDailyService.class);
    
        service.getZhihuContent(id)
                .enqueue(new Callback<ZhihuDailyContent>() {
                    @Override
                    public void onResponse(Call<ZhihuDailyContent> call, Response<ZhihuDailyContent> response) {
                        callback.onContentLoaded(response.body());
                    }
                
                    @Override
                    public void onFailure(Call<ZhihuDailyContent> call, Throwable t) {
                        callback.onDataNotAvailable();
                    }
                });
    }
    
    @Override
    public void saveContent(@NonNull ZhihuDailyContent content) {
    
    }
}
