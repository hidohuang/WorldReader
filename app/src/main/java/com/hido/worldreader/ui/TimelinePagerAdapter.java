package com.hido.worldreader.ui;

import android.support.v4.app.Fragment;
import android.support.v4.app.FragmentManager;
import android.support.v4.app.FragmentPagerAdapter;

import com.hido.worldreader.favorite.FavoritesFragment;
import com.hido.worldreader.article.ZhihuDailyFragment;

/**
 * Created by hidohuang on 2017/9/20.
 */

public class TimelinePagerAdapter extends FragmentPagerAdapter {
    
    private String[] titles;
    
    private ZhihuDailyFragment mZhihuDailyFragment;
    
    private FavoritesFragment mFavoritesFragment;
    
    private InfoFragment mInfoFragment;
    
    
    public TimelinePagerAdapter(FragmentManager fm, ZhihuDailyFragment zhihuFragment, FavoritesFragment favoriteFragment, InfoFragment infoFragment) {
        super(fm);
        this.mZhihuDailyFragment = zhihuFragment;
        this.mFavoritesFragment = favoriteFragment;
        this.mInfoFragment = infoFragment;
        
        titles = new String[]{"article", "favorite", "Info"};
    }
    
    
    @Override
    public Fragment getItem(int position) {
        if (position == 0) {
            return mZhihuDailyFragment;
        } else if (position == 1) {
            return mFavoritesFragment;
        } else {
            return mInfoFragment;
        }
    }
    
    @Override
    public int getCount() {
        return 3;
    }
    
    @Override
    public CharSequence getPageTitle(int position) {
        return titles[position];
    }
}
