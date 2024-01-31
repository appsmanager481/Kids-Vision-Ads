package com.kidsads.visionkidsads.ads;

import android.app.Activity;
import android.content.Intent;

import com.kidsads.visionkidsads.retrofit.ManagerAdsData;
import com.kidsads.visionkidsads.utils.PrefLibAds;

public class InterAndOpenAds {

    private static final String TAG = "BackInterAds";
    public static int count_back_click = 0;

    public static void showInterAndAppOpenAds(Activity activity, Intent intent, boolean isFinished) {

        if (PrefLibAds.getInstance().getString("app_adShowStatus").equals("false")) {
            if (intent != null) {
                activity.startActivity(intent);
            }
            if (isFinished) {
                activity.finish();
            }
            return;
        }

        String appBackPressAdFormat = PrefLibAds.getInstance().getString("app_backPressAdType");
        int how_many_clicks = PrefLibAds.getInstance().getInt("app_backPressAdLimit");

        if (!PrefLibAds.getInstance().getBool("app_backPressAdStatus", false)) {
            if (intent != null) {
                activity.startActivity(intent);
            }
            if (isFinished) {
                activity.finish();
            }
            return;
        }

        switch (appBackPressAdFormat) {
            case "Interstitial":
                LoadAndShowInterAds.loadShowAds(activity, intent, isFinished, how_many_clicks);
                break;
            case "AppOpen":
                ManagerAdsData.showAppOpenAd(activity, intent, isFinished, how_many_clicks);
                break;
            case "Alternate":
                if (count_back_click % 2 == 0) {
                    ManagerAdsData.showAppOpenAd(activity, intent, isFinished, how_many_clicks);
                } else {
                    LoadAndShowInterAds.loadShowAds(activity, intent, isFinished, how_many_clicks);
                }
                count_back_click++;
                break;
            default:
                if (intent != null) {
                    activity.startActivity(intent);
                }
                if (isFinished) {
                    activity.finish();
                }
                break;
        }
    }
}