package com.hido.worldreader.favorite;

import com.hido.worldreader.util.BasePresenter;
import com.hido.worldreader.util.BaseView;
import com.hido.worldreader.data.bean.ZhihuDailyNewsQuestion;

import java.util.List;

/**
 * Created by hidohuang on 2017/9/21.
 */

public interface FavoritesContract {
    
    interface View extends BaseView<Presenter>{
        void setLoadingIndicator(boolean active);
        boolean isActive();
        void showFavorites(List<ZhihuDailyNewsQuestion> zhihuList);
        
    }
    
    interface Presenter extends BasePresenter{
        void loadFavorites();
        
    }
}
