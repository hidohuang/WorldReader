package com.hido.worldreader.ui;

import android.content.Intent;
import android.support.design.widget.TabLayout;
import android.support.v4.app.FragmentManager;
import android.support.v4.view.ViewPager;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;

import com.hido.worldreader.R;
import com.hido.worldreader.data.local.ZhihuDailyLocalDataSource;
import com.hido.worldreader.data.remote.ZhihuDailyRemoteDataSource;
import com.hido.worldreader.data.repository.ZhihuDailyRepository;
import com.hido.worldreader.favorite.FavoritesFragment;
import com.hido.worldreader.favorite.FavoritesPresenter;
import com.hido.worldreader.util.service.CacheService;
import com.hido.worldreader.article.ZhihuDailyFragment;
import com.hido.worldreader.article.ZhihuDailyPresenter;

public class MainActivity extends AppCompatActivity {

    private ZhihuDailyFragment mZhihuDailyFragment;
    private FavoritesFragment mFavoritesFragment;
    private InfoFragment mInfoFragment;
    private TabLayout mTabLayout;
    private ViewPager mViewPager;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

       initViews();
       if (savedInstanceState==null)
       {
           mZhihuDailyFragment=ZhihuDailyFragment.newInstance();
           mFavoritesFragment=FavoritesFragment.newInstance();
           mInfoFragment=InfoFragment.newInstance();
       }else {
           mZhihuDailyFragment = (ZhihuDailyFragment) getSupportFragmentManager().getFragment(savedInstanceState, ZhihuDailyFragment.class.getSimpleName());
           mFavoritesFragment = (FavoritesFragment) getSupportFragmentManager().getFragment(savedInstanceState, FavoritesFragment.class.getSimpleName());
           mInfoFragment = (InfoFragment) getSupportFragmentManager().getFragment(savedInstanceState, InfoFragment.class.getSimpleName());
           
       }
        new ZhihuDailyPresenter(mZhihuDailyFragment,
                                ZhihuDailyRepository.getInstance(ZhihuDailyRemoteDataSource.getInstance(),
                                                                 ZhihuDailyLocalDataSource.getInstance(MainActivity.this)));
        new FavoritesPresenter(mFavoritesFragment,
                               ZhihuDailyRepository.getInstance(ZhihuDailyRemoteDataSource.getInstance(),
                                                                 ZhihuDailyLocalDataSource.getInstance(MainActivity.this)));
    
    
        mViewPager.setAdapter(new TimelinePagerAdapter(getSupportFragmentManager(),
                                                       mZhihuDailyFragment,
                                                       mFavoritesFragment,
                                                       mInfoFragment));

        mViewPager.setOffscreenPageLimit(3);
        mTabLayout.setupWithViewPager(mViewPager);
    
        startService(new Intent(MainActivity.this, CacheService.class));
    
    
    }
    
    private void initViews() {
         mTabLayout = findViewById(R.id.tab_layout);
         mViewPager = findViewById(R.id.view_pager);
    
    }
    
    @Override
    protected void onSaveInstanceState(Bundle outState) {
        super.onSaveInstanceState(outState);
        FragmentManager fm = getSupportFragmentManager();
        if (mZhihuDailyFragment.isAdded()) {
            fm.putFragment(outState, ZhihuDailyFragment.class.getSimpleName(), mZhihuDailyFragment);
        }
        if (mFavoritesFragment.isAdded()) {
            fm.putFragment(outState, FavoritesFragment.class.getSimpleName(), mFavoritesFragment);
        }
        if (mInfoFragment.isAdded()) {
            fm.putFragment(outState, InfoFragment.class.getSimpleName(), mInfoFragment);
        }
    }
}
