package com.hido.worldreader.favorite;

import android.app.Fragment;
import android.content.Context;
import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.v4.widget.SwipeRefreshLayout;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import com.hido.worldreader.R;
import com.hido.worldreader.data.bean.ZhihuDailyNewsQuestion;

import java.util.List;

/**
 * Created by hidohuang on 2017/9/21.
 */

public class FavoritesFragment extends android.support.v4.app.Fragment implements FavoritesContract.View {
    
    private FavoritesAdapter mFavoritesAdapter;
    private FavoritesContract.Presenter mPresenter;
    private RecyclerView mRecyclerView;
    private SwipeRefreshLayout mSwipeRefreshLayout;
    private View mView;
    
    @Override
    public void onAttach(Context context) {
        super.onAttach(context);
    }
    
    @Override
    public void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
    }
    
    @Nullable
    @Override
    public View onCreateView(LayoutInflater inflater, @Nullable ViewGroup container, Bundle savedInstanceState) {
        View view = inflater.inflate(R.layout.framgent_favorites, container, false);
        initViews(view);
        mSwipeRefreshLayout.setOnRefreshListener(()->mPresenter.loadFavorites());
        return view;
    }
    
    @Override
    public void onActivityCreated(@Nullable Bundle savedInstanceState) {
        super.onActivityCreated(savedInstanceState);
    }
    
    @Override
    public void onStart() {
        super.onStart();
    }
    
    @Override
    public void onResume() {
        super.onResume();
        mPresenter.loadFavorites();
        
    }
    
    @Override
    public void onPause() {
        super.onPause();
    }
    
    @Override
    public void onStop() {
        super.onStop();
    }
    
    @Override
    public void onDestroyView() {
        super.onDestroyView();
    }
    
    @Override
    public void onDestroy() {
        super.onDestroy();
    }
    
    @Override
    public void onDetach() {
        super.onDetach();
    }
    
    @Override
    public void setLoadingIndicator(boolean active) {
        mSwipeRefreshLayout.post(() -> mSwipeRefreshLayout.setRefreshing(active));
    
    }
    
    @Override
    public boolean isActive() {
        return isAdded()&&isResumed();
    }
    

    
    public FavoritesFragment() {
    }
    
    public static FavoritesFragment newInstance() {
        
        Bundle args = new Bundle();
        
        FavoritesFragment fragment = new FavoritesFragment();
        fragment.setArguments(args);
        return fragment;
    }
    
    @Override
    public void setPresenter(FavoritesContract.Presenter presenter) {
        if (presenter != null)
        {
            mPresenter = presenter;
        }
    }
    
    @Override
    public void initViews(View view) {
        
        mRecyclerView = view.findViewById(R.id.recycler_view);
        mRecyclerView.setLayoutManager(new LinearLayoutManager(getContext()));
        mSwipeRefreshLayout = view.findViewById(R.id.refresh_layout);
        mView = view.findViewById(R.id.empty_view);
    }
    
    
    @Override
    public void showFavorites(List<ZhihuDailyNewsQuestion> zhihuList) {
        
        if (zhihuList == null)
        {
            mView.setVisibility(View.VISIBLE);
            return;
        }
        
        if (mFavoritesAdapter == null)
        {
            mFavoritesAdapter = new FavoritesAdapter(getContext(), zhihuList);
            mRecyclerView.setAdapter(mFavoritesAdapter);
            
        } else
        {
            mFavoritesAdapter.updateData(zhihuList);
        }
        mRecyclerView.setVisibility(zhihuList.isEmpty() ?View.GONE:View.VISIBLE);
        mView.setVisibility(zhihuList.isEmpty() ? View.VISIBLE : View.GONE);
        
    }
}
