package com.hido.worldreader.detail;

import android.content.ActivityNotFoundException;
import android.content.ClipData;
import android.content.ClipboardManager;
import android.content.Intent;
import android.net.Uri;
import android.os.Bundle;
import android.preference.PreferenceManager;
import android.support.annotation.NonNull;
import android.support.annotation.Nullable;
import android.support.design.widget.BottomSheetDialog;
import android.support.design.widget.CollapsingToolbarLayout;
import android.support.v4.app.Fragment;
import android.support.v4.widget.NestedScrollView;
import android.support.v7.widget.AppCompatTextView;
import android.text.Html;
import android.view.LayoutInflater;
import android.view.Menu;
import android.view.MenuInflater;
import android.view.MenuItem;
import android.view.View;
import android.view.ViewGroup;
import android.webkit.WebSettings;
import android.webkit.WebView;
import android.webkit.WebViewClient;
import android.widget.ImageView;
import android.widget.Toast;

import com.bumptech.glide.Glide;
import com.hido.worldreader.util.ConstantStrings;
import com.hido.worldreader.R;
import com.hido.worldreader.data.bean.ZhihuDailyContent;

import java.util.Iterator;
import java.util.LinkedHashMap;

import static android.content.Context.CLIPBOARD_SERVICE;

/**
 * Created by hidohuang on 2017/9/27.
 */

public class DetailsFragment extends Fragment implements DetailsContract.View {
    
    private ImageView mImageView;
    
    private WebView mWebView;
    
    private CollapsingToolbarLayout mCollapsingToolbarLayout;
    
    private android.support.v7.widget.Toolbar mToolbar;
    
    private NestedScrollView mNestedScrollView;
    
    private DetailsContract.Presenter mPresenter;
    
    private int mArticleId;
    
    private String mTitle;
    
    private boolean mIsNightMode;
    
    private boolean mIsFavorite;
    
    public static int REQUEST_SHARE = 0;
    
    public static int REQUEST_COPY_LINK = 1;
    
    public static int REQUEST_OPEN_WITH_BROWSER = 2;
    
    @Override
    public void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        mArticleId = getActivity().getIntent().getIntExtra(DetailsActivity.KEY_ARTICLE_ID, -1);
        mIsFavorite = getActivity().getIntent().getBooleanExtra(DetailsActivity.KEY_ARTICLE_IS_FAVORITE, false);
        mTitle = getActivity().getIntent().getStringExtra(DetailsActivity.KEY_ARTICLE_TITLE);
        mIsNightMode = PreferenceManager.getDefaultSharedPreferences(getContext()).getBoolean(ConstantStrings.KEY_NIGHT_MODE, false);
        
    }
    
    @Nullable
    @Override
    public View onCreateView(LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        View view = inflater.inflate(R.layout.fragment_details, container, false);
        initViews(view);
        setCollapsingToolbarLayoutTitle(mTitle);
        mToolbar.setOnClickListener(v -> mNestedScrollView.smoothScrollTo(0, 0));
        setHasOptionsMenu(true);
        
        return view;
    }
    
    @Override
    public void onResume() {
        super.onResume();
        mPresenter.loadZhihuDailyContent(mArticleId);
    }
    
    public static DetailsFragment newInstance() {
        Bundle args = new Bundle();
        DetailsFragment fragment = new DetailsFragment();
        fragment.setArguments(args);
        return fragment;
        
        
    }
    
    public DetailsFragment() {
    }
    
    @Override
    public void showMessage(int stringRes) {
        Toast.makeText(getContext(), stringRes, Toast.LENGTH_SHORT).show();
    }
    
    @Override
    public boolean isActive() {
        return isAdded() && isResumed();
    }
    
    @Override
    public void showZhihuDailyContent(@NonNull ZhihuDailyContent content) {
    
        if (content.getBody() != null) {
            String result = content.getBody();
            result = result.replace("<div class=\"img-place-holder\">", "");
            result = result.replace("<div class=\"headline\">", "");
        
            String css = "<link rel=\"stylesheet\" href=\"file:///android_asset/zhihu_daily.css\" type=\"text/css\">";
        
            String theme = "<body className=\"\" onload=\"onLoaded()\">";
            if (mIsNightMode) {
                theme = "<body className=\"\" onload=\"onLoaded()\" class=\"night\">";
            }
        
            result = "<!DOCTYPE html>\n"
                    + "<html lang=\"en\" xmlns=\"http://www.w3.org/1999/xhtml\">\n"
                    + "<head>\n"
                    + "\t<meta charset=\"utf-8\" />"
                    + css
                    + "\n</head>\n"
                    + theme
                    + result
                    + "</body></html>";
        
            mWebView.loadDataWithBaseURL("x-data://base", result,"text/html","utf-8",null);
    
        } else {
            mWebView.loadDataWithBaseURL("x-data://base", content.getShareUrl(),"text/html","utf-8",null);
    
        }
  
        String imageUrl =content.getImage();
        if (imageUrl != null) {
            Glide.with(getContext())
                    .load(imageUrl)
                    .asBitmap()
                    .placeholder(R.drawable.ic_loading)
                    .centerCrop()
                    .error(R.drawable.ic_loading)
                    .into(mImageView);
        } else {
            mImageView.setImageResource(R.drawable.photo);
        }
    
    }
    
    @Override
    public void share(@Nullable String link) {
        try {
            Intent shareIntent = new Intent().setAction(Intent.ACTION_SEND).setType("text/plain");
            String shareText = "" + mTitle + " " + link;
            shareIntent.putExtra(Intent.EXTRA_TEXT, shareText);
            startActivity(Intent.createChooser(shareIntent, getString(R.string.share_to)));
        } catch (android.content.ActivityNotFoundException ex) {
            showMessage(R.string.something_wrong);
        }
    }
    
    @Override
    public void copyLink(@Nullable String link) {
        if (link != null) {
            ClipboardManager manager = (ClipboardManager) getContext().getSystemService(CLIPBOARD_SERVICE);
            ClipData clipData = ClipData.newPlainText("text", Html.fromHtml(link).toString());
            manager.setPrimaryClip(clipData);
            showMessage(R.string.copied_to_clipboard);
        } else {
            showMessage(R.string.something_wrong);
        }
    }
    
    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        int id = item.getItemId();
        if (id == android.R.id.home) {
            getActivity().onBackPressed();
        } else if (id == R.id.action_more) {
        
            final BottomSheetDialog dialog = new BottomSheetDialog(getActivity());
        
            View view = getActivity().getLayoutInflater().inflate(R.layout.actions_details_sheet, null);
        
            AppCompatTextView favorite = view.findViewById(R.id.text_view_favorite);
            AppCompatTextView copyLink = view.findViewById(R.id.text_view_copy_link);
            AppCompatTextView openWithBrowser = view.findViewById(R.id.text_view_open_with_browser);
            AppCompatTextView share = view.findViewById(R.id.text_view_share);
        
            if (mIsFavorite) {
                favorite.setText(R.string.unfavorite);
            } else {
                favorite.setText(R.string.favorite);
            }
        
            favorite.setOnClickListener(v -> {
                dialog.dismiss();
                mIsFavorite = !mIsFavorite;
                mPresenter.favorite(mArticleId, mIsFavorite);
            });
        
            copyLink.setOnClickListener(v -> {
                mPresenter.getLink( REQUEST_COPY_LINK, mArticleId);
                dialog.dismiss();
            });
        
            openWithBrowser.setOnClickListener(v -> {
                mPresenter.getLink( REQUEST_OPEN_WITH_BROWSER, mArticleId);
                dialog.dismiss();
            });
        
            share.setOnClickListener(v -> {
                mPresenter.getLink( REQUEST_SHARE, mArticleId);
                dialog.dismiss();
            });
        
            dialog.setContentView(view);
            dialog.show();
        }
        return true;
    }

    
    @Override
    public void onCreateOptionsMenu(Menu menu, MenuInflater inflater) {
        inflater.inflate(R.menu.menu_more, menu);
        super.onCreateOptionsMenu(menu, inflater);
    
    }
    
    @Override
    public void openWithBrowser(@Nullable String link) {
        if (link != null) {
            try {
                getContext().startActivity(new Intent(Intent.ACTION_VIEW).setData(Uri.parse(link)));
            } catch (ActivityNotFoundException e) {
                Toast.makeText(getContext(), R.string.no_browser_found, Toast.LENGTH_SHORT).show();
            }
        } else {
            showMessage(R.string.something_wrong);
        }
    }
    
    @Override
    public void setPresenter(DetailsContract.Presenter presenter) {
        
        if (presenter != null) {
            mPresenter = presenter;
        }
    }
    
    @Override
    public void initViews(View view) {
        
        mToolbar = view.findViewById(R.id.toolbar);
        
        DetailsActivity activity = (DetailsActivity) getActivity();
        activity.setSupportActionBar(mToolbar);
        activity.getSupportActionBar().setDisplayHomeAsUpEnabled(true);
        activity.getSupportActionBar().setHomeAsUpIndicator(R.drawable.ic_arrow_back_24dp);
        
        mImageView = view.findViewById(R.id.image_view);
        
        mCollapsingToolbarLayout = view.findViewById(R.id.toolbar_layout);
        mNestedScrollView = view.findViewById(R.id.nested_scroll_view);
        
        mWebView = view.findViewById(R.id.web_view);
        
        mWebView.setScrollbarFadingEnabled(true);
        mWebView.getSettings().setJavaScriptEnabled(true);
        mWebView.getSettings().setBuiltInZoomControls(false);
        mWebView.getSettings().setCacheMode(WebSettings.LOAD_NO_CACHE);
        mWebView.getSettings().setDomStorageEnabled(true);
        mWebView.getSettings().setAppCacheEnabled(false);
        
        // Show the images or not.
        mWebView.getSettings().setBlockNetworkImage(PreferenceManager.getDefaultSharedPreferences(getContext()).getBoolean("no_picture_mode", false));
        // TODO: 2017/10/2
        mWebView.setWebViewClient(new WebViewClient() {
            @Override
            public boolean shouldOverrideUrlLoading(WebView view, String url) {
                
                try {
                    getContext().startActivity(new Intent(Intent.ACTION_VIEW).setData(Uri.parse(url)));
                } catch (ActivityNotFoundException e) {
                    Toast.makeText(getContext(), R.string.no_browser_found, Toast.LENGTH_SHORT).show();
                }
                
                return true;
            }
            
        });
    }
    
    private void setCollapsingToolbarLayoutTitle(String title) {
        mCollapsingToolbarLayout.setTitle(title);
//        mCollapsingToolbarLayout.setExpandedTitleTextAppearance(R.style.ExpandedAppBar);
//        mCollapsingToolbarLayout.setCollapsedTitleTextAppearance(R.style.CollapsedAppBar);
        mCollapsingToolbarLayout.setExpandedTitleTextAppearance(R.style.ExpandedAppBarPlus1);
        mCollapsingToolbarLayout.setCollapsedTitleTextAppearance(R.style.CollapsedAppBarPlus1);
    }
}

