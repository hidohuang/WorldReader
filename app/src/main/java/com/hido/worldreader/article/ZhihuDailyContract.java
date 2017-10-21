package com.hido.worldreader.article;

import android.support.annotation.NonNull;

import com.hido.worldreader.util.BasePresenter;
import com.hido.worldreader.util.BaseView;
import com.hido.worldreader.data.bean.ZhihuDailyNewsQuestion;

import java.util.List;

/**
 * Created by hidohuang on 2017/9/26.
 */

public interface ZhihuDailyContract {
    
    interface View extends BaseView<Presenter> {
        
        boolean isActive();
        
        void setLoadingIndicator(boolean active);
        
        void showResult(@NonNull List<ZhihuDailyNewsQuestion> list);
        
    }
    
    interface Presenter extends BasePresenter {
        
        void loadNews(boolean forceUpdate, boolean clearCache, long date);
        
    }
    
}
