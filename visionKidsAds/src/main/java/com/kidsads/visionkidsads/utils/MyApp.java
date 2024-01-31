package com.kidsads.visionkidsads.utils;

import android.app.Application;
import android.os.Handler;

import androidx.appcompat.app.AppCompatDelegate;

import com.kidsads.visionkidsads.activity.NativeBaseActivity;
import com.google.android.gms.ads.MobileAds;
import com.google.android.gms.ads.RequestConfiguration;

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

        RequestConfiguration requestConfiguration = MobileAds.getRequestConfiguration()
                .toBuilder()
                .setTagForChildDirectedTreatment(RequestConfiguration.TAG_FOR_CHILD_DIRECTED_TREATMENT_TRUE)
                .setTagForUnderAgeOfConsent(RequestConfiguration.TAG_FOR_UNDER_AGE_OF_CONSENT_TRUE)
                .setMaxAdContentRating(RequestConfiguration.MAX_AD_CONTENT_RATING_G)
                .build();
        MobileAds.setRequestConfiguration(requestConfiguration);
    }
}
