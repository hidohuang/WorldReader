package com.hido.worldreader.ui;

import android.content.ActivityNotFoundException;
import android.content.Intent;
import android.content.res.Configuration;
import android.net.Uri;
import android.os.Build;
import android.os.Bundle;
import android.os.Handler;
import android.support.v7.app.AppCompatDelegate;
import android.support.v7.preference.PreferenceFragmentCompat;
import android.widget.Toast;

import com.bumptech.glide.Glide;
import com.hido.worldreader.R;

/**
 * Created by hidohuang on 2017/9/24.
 */

public class InfoPreferenceFragment extends PreferenceFragmentCompat {
    private static final int MSG_GLIDE_CLEAR_CACHE_DONE=1;
    private final Handler handler = new Handler(message -> {
        switch (message.what) {
            case MSG_GLIDE_CLEAR_CACHE_DONE:
                Toast.makeText(getContext(), "缓存已清除^ ^", Toast.LENGTH_SHORT).show();
                break;
            default:
                break;
        }
        return true;
    });

    @Override
    public void onCreatePreferences(Bundle savedInstanceState, String rootKey) {
     
        
        addPreferencesFromResource(R.xml.perferences);
    
        // Setting of night mode
        findPreference("night_mode").setOnPreferenceChangeListener((p, o) -> {
            if ((getResources().getConfiguration().uiMode & Configuration.UI_MODE_NIGHT_MASK)
                    == Configuration.UI_MODE_NIGHT_YES) {
                AppCompatDelegate.setDefaultNightMode(AppCompatDelegate.MODE_NIGHT_NO);
            } else {
            
                AppCompatDelegate.setDefaultNightMode(AppCompatDelegate.MODE_NIGHT_YES);
            }
            getActivity().getWindow().setWindowAnimations(R.style.WindowAnimationFadeInOut);
            getActivity().recreate();
            return true;
        });
    
        
        // Clear the cache of glide
        findPreference("clear_glide_cache").setOnPreferenceClickListener(v -> {
            new Thread(() -> {
                Glide.get(getContext()).clearDiskCache();
                handler.sendEmptyMessage(MSG_GLIDE_CLEAR_CACHE_DONE);
            }).start();
            return true;
        });
        
    
        //Author
        findPreference("author").setOnPreferenceClickListener(p ->{
            try {
                startActivity(new Intent(Intent.ACTION_VIEW).setData(Uri.parse("https://github.com/hidohuang")));
            } catch (ActivityNotFoundException e) {
                Toast.makeText(getContext(), R.string.no_browser_found, Toast.LENGTH_SHORT).show();
            }
            return true;
        });
    
        // Feedback through sending an email
        findPreference("feedback").setOnPreferenceClickListener(p -> {
            try{
                Uri uri = Uri.parse(getString(R.string.sendto));
                Intent intent = new Intent(Intent.ACTION_SENDTO,uri);
                intent.putExtra(Intent.EXTRA_SUBJECT, getString(R.string.mail_topic));
                intent.putExtra(Intent.EXTRA_TEXT,
                                getString(R.string.device_model) + Build.MODEL + "\n"
                                        + getString(R.string.sdk_version) + Build.VERSION.RELEASE + "\n"
                                        + getString(R.string.version));
                startActivity(intent);
            } catch (android.content.ActivityNotFoundException ex){
                Toast.makeText(getContext(), R.string.no_mail_app, Toast.LENGTH_SHORT).show();
                
            }
            return true;
        });
    }
}
