package com.hido.worldreader.util;

import android.view.View;

/**
 * Created by hidohuang on 2017/9/19.
 */

public interface BaseView<T> {

    void setPresenter(T presenter);

    void initViews(View view);
}
