package com.hido.worldreader.detail;

import android.support.annotation.NonNull;

import com.hido.worldreader.R;
import com.hido.worldreader.data.bean.ZhihuDailyContent;
import com.hido.worldreader.data.datasource.ZhihuDailyContentDataSource;
import com.hido.worldreader.data.repository.ZhihuDailyContentRepository;
import com.hido.worldreader.data.repository.ZhihuDailyRepository;

/**
 * Created by hidohuang on 2017/9/27.
 */

public class DetailsPresenter implements DetailsContract.Presenter {
    
    @NonNull
    private final DetailsContract.View mView;
    
    @NonNull
    private ZhihuDailyRepository mZhihuDailyRepository;
    
    @NonNull
    private ZhihuDailyContentRepository mZhihuDailyContentRepository;
    
    public DetailsPresenter(@NonNull DetailsContract.View view, @NonNull ZhihuDailyRepository zhihuDailyRepository, @NonNull ZhihuDailyContentRepository zhihuDailyContentRepository) {
        mView = view;
        mView.setPresenter(this);
        mZhihuDailyRepository = zhihuDailyRepository;
        mZhihuDailyContentRepository = zhihuDailyContentRepository;
    }
    
    @Override
    public void favorite(int id, boolean favorite) {
    
        mZhihuDailyRepository.favoriteItem(id,favorite);
    }
    
    @Override
    public void loadZhihuDailyContent(int id) {
    
        mZhihuDailyContentRepository.getZhihuDailyContent(id, new ZhihuDailyContentDataSource.LoadZhihuDailyContentCallback() {
            @Override
            public void onContentLoaded(@NonNull ZhihuDailyContent content) {
                if (mView.isActive()) {
                    mView.showZhihuDailyContent(content);
                }
            }
        
            @Override
            public void onDataNotAvailable() {
                if (mView.isActive()) {
                    mView.showMessage(R.string.something_wrong);
                }
            }
        });
    }
    
    @Override
    public void getLink(int requestCode, int id) {
    
        mZhihuDailyContentRepository.getZhihuDailyContent(id, new ZhihuDailyContentDataSource.LoadZhihuDailyContentCallback() {
            @Override
            public void onContentLoaded(@NonNull ZhihuDailyContent content) {
                if (mView.isActive()) {
                    String url = content.getShareUrl();
                    if (requestCode == DetailsFragment.REQUEST_SHARE) {
                        mView.share(url);
                    } else if (requestCode == DetailsFragment.REQUEST_COPY_LINK) {
                        mView.copyLink(url);
                    } else if (requestCode == DetailsFragment.REQUEST_OPEN_WITH_BROWSER){
                        mView.openWithBrowser(url);
                    }
                }
            }
        
            @Override
            public void onDataNotAvailable() {
                if (mView.isActive()) {
                    mView.showMessage(R.string.share_error);
                }
            }
        });
    
    }
    
    @Override
    public void start() {
    
    }
}
