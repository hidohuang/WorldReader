package com.hido.worldreader.favorite;

import android.support.annotation.NonNull;

import com.hido.worldreader.data.bean.ZhihuDailyNewsQuestion;
import com.hido.worldreader.data.datasource.ZhihuDailyDataSource;
import com.hido.worldreader.data.repository.ZhihuDailyRepository;

import java.util.List;

/**
 * Created by hidohuang on 2017/9/21.
 */

public class FavoritesPresenter implements FavoritesContract.Presenter {
    
    private FavoritesContract.View mView;
    
    private ZhihuDailyRepository mZhihuDailyRepository;
    
    public FavoritesPresenter(@NonNull FavoritesContract.View view,
                              @NonNull ZhihuDailyRepository zhihuDailyRepository) {
        this.mView=view;
        mView.setPresenter(this);
        this.mZhihuDailyRepository = zhihuDailyRepository;
        
    }
    
    @Override
    
    public void start() {
    
    }
    
    @Override
    public void loadFavorites() {
        mZhihuDailyRepository.getFavorites(new ZhihuDailyDataSource.LoadZhihuDailyNewsCallback() {
            @Override
            public void onNewsLoaded(@NonNull List<ZhihuDailyNewsQuestion> list) {
                if (mView.isActive()) {
                    mView.showFavorites(list);
                    mView.setLoadingIndicator(false);
                }
                // TODO: 2017/9/30
            }
    
            @Override
            public void onDataNotAvailable() {
                if (mView.isActive()) {
                    mView.setLoadingIndicator(false);
    
                }
        
            }
        });
    }
}
