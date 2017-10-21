package com.hido.worldreader.detail;

import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.v7.app.AppCompatActivity;
import android.transition.Transition;
import android.transition.TransitionInflater;
import android.view.Window;

import com.hido.worldreader.R;
import com.hido.worldreader.data.local.ZhihuDailyContentLocalDataSource;
import com.hido.worldreader.data.local.ZhihuDailyLocalDataSource;
import com.hido.worldreader.data.remote.ZhihuDailyContentRemoteDataSource;
import com.hido.worldreader.data.remote.ZhihuDailyRemoteDataSource;
import com.hido.worldreader.data.repository.ZhihuDailyContentRepository;
import com.hido.worldreader.data.repository.ZhihuDailyRepository;

/**
 * Created by hidohuang on 2017/9/27.
 */

public class DetailsActivity extends AppCompatActivity {
    
    public static final String KEY_ARTICLE_ID = "KEY_ARTICLE_ID";
    public static final String KEY_ARTICLE_TITLE = "KEY_ARTICLE_TITLE";
    public static final String KEY_ARTICLE_IS_FAVORITE = "KEY_ARTICLE_IS_FAVORITE";
    
    private DetailsFragment mDetailsFragment;
    
    @Override
    protected void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
      
        setContentView(R.layout.frame);
     
    
        if (savedInstanceState != null) {
            mDetailsFragment = (DetailsFragment) getSupportFragmentManager().getFragment(savedInstanceState, DetailsFragment.class.getSimpleName());
        } else {
            mDetailsFragment = DetailsFragment.newInstance();
            getSupportFragmentManager().beginTransaction()
                    .add(R.id.container, mDetailsFragment, DetailsFragment.class.getSimpleName())
                    .commit();
        }
    
        new DetailsPresenter(mDetailsFragment,
                             ZhihuDailyRepository.getInstance(ZhihuDailyRemoteDataSource.getInstance(),
                                                              ZhihuDailyLocalDataSource.getInstance(DetailsActivity.this)),
                             ZhihuDailyContentRepository.getInstance(ZhihuDailyContentRemoteDataSource.getInstance(),
                                                                     ZhihuDailyContentLocalDataSource.getInstance(DetailsActivity.this)));

    }
    
    @Override
    protected void onDestroy() {
        super.onDestroy();
        ZhihuDailyContentRepository.destroyInstance();
        
    }
    
    @Override
    protected void onSaveInstanceState(Bundle outState) {
        super.onSaveInstanceState(outState);
        if (mDetailsFragment.isAdded()) {
            getSupportFragmentManager().putFragment(outState, DetailsFragment.class.getSimpleName(), mDetailsFragment);
        }
    }
}
