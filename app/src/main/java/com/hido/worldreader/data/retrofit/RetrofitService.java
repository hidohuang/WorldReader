package com.hido.worldreader.data.retrofit;

import com.hido.worldreader.data.bean.ZhihuDailyContent;
import com.hido.worldreader.data.bean.ZhihuDailyNews;

import retrofit2.Call;
import retrofit2.http.GET;
import retrofit2.http.Path;

/**
 * Created by hidohuang on 2017/9/24.
 */

public interface RetrofitService {
    
    String ZHIHU_DAILY = "https://news-at.zhihu.com/api/4/news/";
    
    interface ZhihuDailyService {
        
        @GET("before/{date}")
        Call<ZhihuDailyNews> getZhihuList(@Path("date") String date);
        
        @GET("{id}")
        Call<ZhihuDailyContent> getZhihuContent(@Path("id") int id);
        
    }
}
