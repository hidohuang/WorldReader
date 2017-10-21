package com.hido.worldreader.detail;

import android.support.annotation.NonNull;
import android.support.annotation.Nullable;
import android.support.annotation.StringRes;

import com.hido.worldreader.util.BasePresenter;
import com.hido.worldreader.util.BaseView;
import com.hido.worldreader.data.bean.ZhihuDailyContent;

/**
 * Created by hidohuang on 2017/9/27.
 */

public interface DetailsContract {
    
    interface View extends BaseView<Presenter> {
    
        void showMessage(@StringRes int stringRes);
    
        boolean isActive();
    
        void showZhihuDailyContent(@NonNull ZhihuDailyContent content);
    
        void share(@Nullable String link);
    
        void copyLink(@Nullable String link);
    
        void openWithBrowser(@Nullable String link);
    
    }
    
    interface Presenter extends BasePresenter {
    
    
       // void favorite(ContentType type, int id, boolean favorite);
        void favorite(int id, boolean favorite);
    
        void loadZhihuDailyContent(int id);
    
        //void getLink(ContentType type, int requestCode, int id);
        void getLink( int requestCode, int id);
    }
    
}
