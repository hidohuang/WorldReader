package com.hido.worldreader.article;

import android.support.annotation.NonNull;

import com.hido.worldreader.data.bean.ZhihuDailyNewsQuestion;
import com.hido.worldreader.data.datasource.ZhihuDailyDataSource;
import com.hido.worldreader.data.repository.ZhihuDailyRepository;

import java.util.List;

/**
 * Created by hidohuang on 2017/9/26.
 */

public class ZhihuDailyPresenter implements ZhihuDailyContract.Presenter {
    
    private final ZhihuDailyContract.View mView;
    
    private final ZhihuDailyRepository mZhihuDailyRepository;
    
    public ZhihuDailyPresenter(@NonNull ZhihuDailyContract.View view, @NonNull ZhihuDailyRepository zhihuDailyRepository) {
        mView = view;
        mView.setPresenter(this);
        mZhihuDailyRepository = zhihuDailyRepository;
    }
    
    @Override
    public void start() {
    
    }
    
    @Override
    public void loadNews(boolean forceUpdate, boolean clearCache, long date) {
        mZhihuDailyRepository.getZhihuDailyNews(forceUpdate,clearCache,date,new ZhihuDailyDataSource.LoadZhihuDailyNewsCallback() {
            @Override
            public void onNewsLoaded(@NonNull List<ZhihuDailyNewsQuestion> list) {
                if (mView.isActive()) {
                    mView.showResult(list);
                    mView.setLoadingIndicator(false);
                }
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
