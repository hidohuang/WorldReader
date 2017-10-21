package com.hido.worldreader.article;


import android.content.Context;
import android.content.Intent;
import android.os.Bundle;
import android.support.annotation.NonNull;
import android.support.annotation.Nullable;
import android.support.design.widget.FloatingActionButton;
import android.support.v4.content.LocalBroadcastManager;
import android.support.v4.widget.SwipeRefreshLayout;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import com.hido.worldreader.R;
import com.hido.worldreader.data.bean.ZhihuDailyNewsQuestion;
import com.hido.worldreader.util.service.CacheService;
import com.wdullaer.materialdatetimepicker.date.DatePickerDialog;

import java.util.Calendar;
import java.util.List;
import java.util.TimeZone;

/**
 * Created by hidohuang on 2017/9/26.
 */

public class ZhihuDailyFragment extends android.support.v4.app.Fragment implements ZhihuDailyContract.View {
    
    private ZhihuDailyContract.Presenter mPresenter;
    
    @Override
    public void onAttach(Context context) {
        super.onAttach(context);
    }
    
    @Override
    public void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        Calendar c = Calendar.getInstance();
        c.setTimeZone(TimeZone.getTimeZone("GMT+08"));
        mYear = c.get(Calendar.YEAR);
        mMonth = c.get(Calendar.MONTH);
        mDay = c.get(Calendar.DAY_OF_MONTH);
    }
    
    @Nullable
    @Override
    public View onCreateView(LayoutInflater inflater, @Nullable ViewGroup container, Bundle savedInstanceState) {
        View view = inflater.inflate(R.layout.fragment_zhihu, container, false);
        initViews(view);
        mSwipeRefreshLayout.setOnRefreshListener(() -> {
            Calendar c = Calendar.getInstance();
            c.setTimeZone(TimeZone.getTimeZone("GMT+08"));
            mPresenter.loadNews(true, true, c.getTimeInMillis());
        });
        
        mRecyclerView.setOnScrollListener(new RecyclerView.OnScrollListener() {
            @Override
            public void onScrolled(RecyclerView recyclerView, int dx, int dy) {
                super.onScrolled(recyclerView, dx, dy);
                if (dy > 0) {
                    mFloatingActionButton.hide();
                    if (mLayoutManager.findLastCompletelyVisibleItemPosition() == mListSize - 1) {
                        loadMore();
                    }
                } else {
                    mFloatingActionButton.show();
                }
            }
        });
        mFloatingActionButton.setOnClickListener(v -> {
            Calendar c = Calendar.getInstance();
            c.set(mYear, mMonth, mDay);
            DatePickerDialog dialog = DatePickerDialog.newInstance((datePickerDialog, year, monthOfYear, dayOfMonth) -> {
                mYear = year;
                mMonth = monthOfYear;
                mDay = dayOfMonth;
                c.set(mYear, monthOfYear, mDay);
                // TODO: 2017/10/1
                mPresenter.loadNews(true, true, c.getTimeInMillis());
                
            }, c.get(Calendar.YEAR), c.get(Calendar.MONTH), c.get(Calendar.DAY_OF_MONTH));
            
            dialog.setMaxDate(Calendar.getInstance());
            
            Calendar minDate = Calendar.getInstance();
            minDate.set(2013, 5, 20);
            dialog.setMinDate(minDate);
            dialog.vibrate(false);
            
            dialog.show(getActivity().getFragmentManager(), ZhihuDailyFragment.class.getSimpleName());
            
        });
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
        mPresenter.start();
        Calendar c = Calendar.getInstance();
        c.setTimeZone(TimeZone.getTimeZone("GMT+08"));
        c.set(mYear, mMonth, mDay);
        setLoadingIndicator(mIsFirstLoad);
        if (mIsFirstLoad) {
            mPresenter.loadNews(true, false, c.getTimeInMillis());
            mIsFirstLoad = false;
        } else {
            mPresenter.loadNews(false, false, c.getTimeInMillis());
        }
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
    
    private ZhihuDailyAdapter mZhihuDailyAdapter;
    
    private SwipeRefreshLayout mSwipeRefreshLayout;
    
    private View mEmptyView;
    
    private FloatingActionButton mFloatingActionButton;
    
    private RecyclerView mRecyclerView;
    
    private LinearLayoutManager mLayoutManager;
    
    private int mYear, mMonth, mDay;
    
    private int mListSize;
    
    private boolean mIsFirstLoad = true;
    
    public ZhihuDailyFragment() {
    }
    
    public static ZhihuDailyFragment newInstance() {
        Bundle args = new Bundle();
        ZhihuDailyFragment fragment = new ZhihuDailyFragment();
        fragment.setArguments(args);
        return fragment;
    }
    
    @Override
    public void setPresenter(ZhihuDailyContract.Presenter presenter) {
        if (presenter != null) {
            mPresenter = presenter;
        }
        
    }
    
    @Override
    public boolean isActive() {
        return isAdded() && isResumed();
    }
    
    private void loadMore() {
        Calendar c = Calendar.getInstance();
        c.set(mYear, mMonth, --mDay);
        mPresenter.loadNews(true, false, c.getTimeInMillis());
    }
    
    @Override
    public void initViews(View view) {
        mEmptyView = view.findViewById(R.id.empty_view);
        mFloatingActionButton = view.findViewById(R.id.fab);
        mSwipeRefreshLayout = view.findViewById(R.id.refresh_layout);
        mRecyclerView = view.findViewById(R.id.recycler_view);
        mLayoutManager = new LinearLayoutManager(getContext());
        mRecyclerView.setLayoutManager(mLayoutManager);
        
    }
    
    @Override
    public void setLoadingIndicator(boolean active) {
        
        mSwipeRefreshLayout.post(() -> mSwipeRefreshLayout.setRefreshing(active));
    }
    
    @Override
    public void showResult(@NonNull List<ZhihuDailyNewsQuestion> list) {
    
        
        if (mZhihuDailyAdapter == null) {
            mZhihuDailyAdapter = new ZhihuDailyAdapter(getContext(),list);
            mRecyclerView.setAdapter(mZhihuDailyAdapter);
        } else {
            mZhihuDailyAdapter.updateData(list);
        }
    
        mListSize = list.size();
    
   
        mEmptyView.setVisibility(list.isEmpty() ? View.VISIBLE : View.INVISIBLE);
        //mRecyclerView.setVisibility(list.isEmpty() ? View.INVISIBLE : View.VISIBLE);
    
        // Cache data of the items
        for (ZhihuDailyNewsQuestion item : list) {
            Intent intent = new Intent(CacheService.BROADCAST_FILTER_ACTION);
            intent.putExtra(CacheService.ITEM_ID, item.getId());
            LocalBroadcastManager.getInstance(getContext()).sendBroadcast(intent);
        }
    }
    
}
