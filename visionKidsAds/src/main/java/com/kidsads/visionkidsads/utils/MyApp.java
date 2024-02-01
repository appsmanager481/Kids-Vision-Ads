package com.kidsads.visionkidsads.utils;

import android.app.Application;
import android.os.Handler;

import androidx.appcompat.app.AppCompatDelegate;

import com.kidsads.visionkidsads.activity.NativeBaseActivity;

public class MyApp extends Application {

    public static MyApp mInstance;
    private Handler handler;

    public static MyApp getApplication() {
        return mInstance;
    }

    @Override
    public void onCreate() {
        super.onCreate();
        AppCompatDelegate.setDefaultNightMode(AppCompatDelegate.MODE_NIGHT_NO);
        mInstance = this;
        NativeBaseActivity.isSubscribed = false;
        PrefLibAds.getInstance().init(this);
    }
}
