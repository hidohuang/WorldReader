package com.hido.worldreader.ui;

import android.app.Fragment;
import android.os.Bundle;
import android.support.annotation.Nullable;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import com.hido.worldreader.R;

/**
 * Created by hidohuang on 2017/9/21.
 */

public class InfoFragment extends android.support.v4.app.Fragment {
    
    public InfoFragment() {
    }
    
    public static InfoFragment newInstance() {
        
        Bundle args = new Bundle();
        
        InfoFragment fragment = new InfoFragment();
        fragment.setArguments(args);
        return fragment;
    }
    
    @Nullable
    @Override
    public View onCreateView(LayoutInflater inflater, @Nullable ViewGroup container, Bundle savedInstanceState) {
        View view = inflater.inflate(R.layout.fragment_info, container, false);
        
        getChildFragmentManager().beginTransaction()
                .replace(R.id.info_container, new InfoPreferenceFragment())
                .commit();
        
        return view;
    }
}
