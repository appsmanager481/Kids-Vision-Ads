package com.kidsads.visionfikids;

import androidx.appcompat.app.AppCompatDelegate;

import com.kidsads.visionkidsads.utils.MyApp;

public class MyMainApp extends MyApp {

    private static MyMainApp MyApplications;

    public static MyMainApp getApplication() {
        return MyApplications;
    }

    @Override
    public void onCreate() {
        super.onCreate();
        AppCompatDelegate.setDefaultNightMode(AppCompatDelegate.MODE_NIGHT_NO);
        MyApplications = this;
    }
}
