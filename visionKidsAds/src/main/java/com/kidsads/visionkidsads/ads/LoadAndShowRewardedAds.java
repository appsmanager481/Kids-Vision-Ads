package com.kidsads.visionkidsads.ads;

import static com.kidsads.visionkidsads.retrofit.ManagerAdsData.ADMOB;
import static com.kidsads.visionkidsads.retrofit.ManagerAdsData.ADMOB1;
import static com.kidsads.visionkidsads.retrofit.ManagerAdsData.ADMOB2;
import static com.kidsads.visionkidsads.retrofit.ManagerAdsData.APPLOVIN;
import static com.kidsads.visionkidsads.retrofit.ManagerAdsData.FACEBOOK;
import static com.kidsads.visionkidsads.retrofit.ManagerAdsData.appDialogBeforeAdShow;
import static com.kidsads.visionkidsads.retrofit.ManagerAdsData.getRandomID;

import android.app.Activity;
import android.content.Intent;
import android.util.Log;

import androidx.annotation.NonNull;

import com.kidsads.visionkidsads.activity.CustomRewardedActivity;
import com.kidsads.visionkidsads.loader.InterAdsLoader;
import com.kidsads.visionkidsads.model.CustomAds;
import com.kidsads.visionkidsads.utils.PrefLibAds;
import com.google.android.gms.ads.AdError;
import com.google.android.gms.ads.AdRequest;
import com.google.android.gms.ads.FullScreenContentCallback;
import com.google.android.gms.ads.LoadAdError;
import com.google.android.gms.ads.OnUserEarnedRewardListener;
import com.google.android.gms.ads.rewarded.RewardItem;
import com.google.android.gms.ads.rewarded.RewardedAd;
import com.google.android.gms.ads.rewarded.RewardedAdLoadCallback;

import java.util.ArrayList;
import java.util.Collections;
import java.util.List;

public class LoadAndShowRewardedAds {

    private static final String TAG = "InterstitialAds";
    private static final InterAdsLoader progressBar = new InterAdsLoader();
    public static int count_click = -1;
    public static int count_click_for_alt = -1;
    public static boolean click_count_flag = true;
    static ArrayList<String> rewarded_sequence = new ArrayList<>();
    private static RewardedAd mRewardedAd;
    private static RewardedAd mRewardedAd1;
    private static RewardedAd mRewardedAd2;

    public static void loadShowAds(Activity activity, Intent intent, boolean isFinished, int clicks) {

        String google_i = "";
        if (PrefLibAds.getInstance().getString("loadAdIds").equals("Fix")) {
            google_i = PrefLibAds.getInstance().getString("ADMOB_REWARD");
        } else {
            google_i = getRandomID(ADMOB, "R");
        }

        String facebook_i = "";
        if (PrefLibAds.getInstance().getString("loadAdIds").equals("Fix")) {
            facebook_i = PrefLibAds.getInstance().getString("FACEBOOK_REWARD");
        } else {
            facebook_i = getRandomID(FACEBOOK, "R");
        }

        String applovin_i = "";
        if (PrefLibAds.getInstance().getString("loadAdIds").equals("Fix")) {
            applovin_i = PrefLibAds.getInstance().getString("APPLOVIN_REWARD");
        } else {
            applovin_i = getRandomID(APPLOVIN, "R");
        }

        String google_i1 = "";
        if (PrefLibAds.getInstance().getString("loadAdIds").equals("Fix")) {
            google_i1 = PrefLibAds.getInstance().getString("ADMOB_REWARD1");
        } else {
            google_i1 = getRandomID(ADMOB1, "R");
        }

        String google_i2 = "";
        if (PrefLibAds.getInstance().getString("loadAdIds").equals("Fix")) {
            google_i2 = PrefLibAds.getInstance().getString("ADMOB_REWARD2");
        } else {
            google_i2 = getRandomID(ADMOB2, "R");
        }

        loadAds(activity, google_i, facebook_i, applovin_i, google_i1, google_i2, intent, isFinished, clicks);
    }

    public static void loadAds(Activity activity, String google_i, String facebook_i, String applovin_i, String google_i1, String google_i2, Intent intent, boolean isFinished, int click) {

        if (PrefLibAds.getInstance().getString("app_adShowStatus").equals("false")) {
            if (intent != null) {
                activity.startActivity(intent);
            }
            if (isFinished) {
                activity.finish();
            }
            return;
        }

        if (click_count_flag) {
            count_click++;
            if (click != 0) {
                if (count_click % click != 0) {
                    if (intent != null) {
                        activity.startActivity(intent);
                    }
                    if (isFinished) {
                        activity.finish();
                    }
                    return;
                }
            }
        }


        count_click_for_alt++;

        String adApply = PrefLibAds.getInstance().getString("app_adApply");

        String all_howShowAd = PrefLibAds.getInstance().getString("app_howShowAd");
        String all_adPlatformSequence = PrefLibAds.getInstance().getString("app_adPlatformSequence");

        String format_howShowAdNative = PrefLibAds.getInstance().getString("app_howShowAdInterstitial");
        String format_adPlatformSequenceNative = PrefLibAds.getInstance().getString("app_adPlatformSequenceInterstitial").toLowerCase();

        rewarded_sequence = new ArrayList<String>();

        if (adApply.equals("allAdFormat")) {
            if (all_howShowAd.equals("fixSequence") && !all_adPlatformSequence.isEmpty()) {
                String[] adSequence = all_adPlatformSequence.split(", ");
                Collections.addAll(rewarded_sequence, adSequence);
            } else if (all_howShowAd.equals("alternateAdShow") && !all_adPlatformSequence.isEmpty()) {
                String[] alernateAd = all_adPlatformSequence.split(", ");

                int index = 0;
                for (int j = 0; j <= 10; j++) {
                    if (count_click_for_alt % alernateAd.length == j) {
                        index = j;
                        rewarded_sequence.add(alernateAd[index]);
                    }
                }

                String[] adSequence = all_adPlatformSequence.split(", ");
                for (String s : adSequence) {
                    if (rewarded_sequence.size() != 0) {
                        if (!rewarded_sequence.get(0).equals(s)) {
                            rewarded_sequence.add(s);
                        }
                    }

                }
            } else {
                if (appDialogBeforeAdShow) {
                    if (progressBar.isShowing()) {
                        progressBar.getDialog().dismiss();
                    }
                }

                List<CustomAds> customAds = PrefLibAds.getCustomAdsData(activity);
                if (customAds != null && !customAds.isEmpty()) {
                    Intent intent1 = new Intent(activity, CustomRewardedActivity.class);
                    if (intent != null) {
                        intent1.putExtra("passedIntent", intent);
                    }
                    activity.startActivity(intent1);
                } else {
                    if (intent != null) {
                        activity.startActivity(intent);
                    }
                    if (isFinished) {
                        activity.finish();
                    }
                }
            }
        } else {
            if (format_howShowAdNative.equals("fixSequence") && !format_adPlatformSequenceNative.isEmpty()) {
                String[] adSequence = format_adPlatformSequenceNative.split(", ");
                Collections.addAll(rewarded_sequence, adSequence);
            } else if (format_howShowAdNative.equals("alternateAdShow") && !format_adPlatformSequenceNative.isEmpty()) {
                String[] alernateAd = format_adPlatformSequenceNative.split(", ");

                int index = 0;
                for (int j = 0; j <= 10; j++) {
                    if (count_click_for_alt % alernateAd.length == j) {
                        index = j;
                        rewarded_sequence.add(alernateAd[index]);
                    }
                }

                String[] adSequence = format_adPlatformSequenceNative.split(", ");
                for (String s : adSequence) {
                    if (rewarded_sequence.size() != 0) {
                        if (!rewarded_sequence.get(0).equals(s)) {
                            rewarded_sequence.add(s);
                        }
                    }
                }
            } else {
                if (appDialogBeforeAdShow) {
                    if (progressBar.isShowing()) {
                        progressBar.getDialog().dismiss();
                    }
                }

                List<CustomAds> customAds = PrefLibAds.getCustomAdsData(activity);
                if (customAds != null && !customAds.isEmpty()) {
                    Intent intent1 = new Intent(activity, CustomRewardedActivity.class);
                    if (intent != null) {
                        intent1.putExtra("passedIntent", intent);
                    }
                    activity.startActivity(intent1);
                } else {
                    if (intent != null) {
                        activity.startActivity(intent);
                    }
                    if (isFinished) {
                        activity.finish();
                    }
                }
            }
        }

        if (rewarded_sequence.size() != 0) {
            showRewardedAd(rewarded_sequence.get(0), activity, intent, isFinished, google_i, facebook_i, applovin_i, google_i1, google_i2);
        } else {
            if (appDialogBeforeAdShow) {
                if (progressBar.isShowing()) {
                    progressBar.getDialog().dismiss();
                }
            }

            List<CustomAds> customAds = PrefLibAds.getCustomAdsData(activity);
            if (customAds != null && !customAds.isEmpty()) {
                Intent intent1 = new Intent(activity, CustomRewardedActivity.class);
                if (intent != null) {
                    intent1.putExtra("passedIntent", intent);
                }
                activity.startActivity(intent1);
            } else {
                if (intent != null) {
                    activity.startActivity(intent);
                }
                if (isFinished) {
                    activity.finish();
                }
            }
        }
    }

    private static void admobLoadShow(final Activity activity, Intent intent, boolean isFinished, String google_i, String facebook_i, String applovin_i, String google_i1, String google_i2) {

        if (!google_i.isEmpty()) {
            AdRequest adRequest = new AdRequest.Builder().build();
            RewardedAd.load(activity, google_i, adRequest, new RewardedAdLoadCallback() {
                @Override
                public void onAdFailedToLoad(@NonNull LoadAdError loadAdError) {
                    //loadAds(context);
                    mRewardedAd = null;
                    nextRewardedPlatform(activity, intent, isFinished, google_i, facebook_i, applovin_i, google_i1, google_i2);
                }

                @Override
                public void onAdLoaded(@NonNull RewardedAd rewardedAd) {
                    mRewardedAd = rewardedAd;
                    Log.e(TAG, "onAdLoaded" + "REQ_LOAD");
                    if (appDialogBeforeAdShow) {
                        if (progressBar.isShowing()) {
                            progressBar.getDialog().dismiss();
                        }
                    }

                    mRewardedAd.show(activity, new OnUserEarnedRewardListener() {
                        @Override
                        public void onUserEarnedReward(@NonNull RewardItem rewardItem) {

                        }
                    });

                    mRewardedAd.setFullScreenContentCallback(new FullScreenContentCallback() {
                        @Override
                        public void onAdFailedToShowFullScreenContent(@NonNull AdError adError) {
                            super.onAdFailedToShowFullScreenContent(adError);
                            Log.d("TAG", "The ad failed to show.");
                            mRewardedAd = null;
                        }

                        @Override
                        public void onAdShowedFullScreenContent() {
                            super.onAdShowedFullScreenContent();
                        }

                        @Override
                        public void onAdDismissedFullScreenContent() {
                            super.onAdDismissedFullScreenContent();
                            mRewardedAd = null;
                            if (appDialogBeforeAdShow) {
                                if (progressBar.isShowing()) {
                                    progressBar.getDialog().dismiss();
                                }
                            }

                            Log.d("TAG", "The ad was dismissed.");

                            if (intent != null) {
                                activity.startActivity(intent);
                            }
                            if (isFinished) {
                                activity.finish();
                            }
                        }

                        @Override
                        public void onAdImpression() {
                            super.onAdImpression();
                        }

                        @Override
                        public void onAdClicked() {
                            super.onAdClicked();
                        }
                    });
                }
            });
        } else {
            nextRewardedPlatform(activity, intent, isFinished, google_i, facebook_i, applovin_i, google_i1, google_i2);
        }
    }

    private static void admobLoadShow1(final Activity activity, Intent intent, boolean isFinished, String google_i, String facebook_i, String applovin_i, String google_i1, String google_i2) {

        if (!google_i1.isEmpty()) {
            AdRequest adRequest = new AdRequest.Builder().build();
            RewardedAd.load(activity, google_i1, adRequest, new RewardedAdLoadCallback() {
                @Override
                public void onAdFailedToLoad(@NonNull LoadAdError loadAdError) {
                    //loadAds(context);
                    mRewardedAd1 = null;
                    nextRewardedPlatform(activity, intent, isFinished, google_i, facebook_i, applovin_i, google_i1, google_i2);
                }

                @Override
                public void onAdLoaded(@NonNull RewardedAd rewardedAd) {
                    mRewardedAd1 = rewardedAd;
                    Log.e(TAG, "onAdLoaded" + "REQ_LOAD");
                    if (appDialogBeforeAdShow) {
                        if (progressBar.isShowing()) {
                            progressBar.getDialog().dismiss();
                        }
                    }

                    mRewardedAd1.show(activity, new OnUserEarnedRewardListener() {
                        @Override
                        public void onUserEarnedReward(@NonNull RewardItem rewardItem) {

                        }
                    });

                    mRewardedAd1.setFullScreenContentCallback(new FullScreenContentCallback() {
                        @Override
                        public void onAdFailedToShowFullScreenContent(@NonNull AdError adError) {
                            super.onAdFailedToShowFullScreenContent(adError);
                            Log.d("TAG", "The ad failed to show.");
                            mRewardedAd1 = null;
                        }

                        @Override
                        public void onAdShowedFullScreenContent() {
                            super.onAdShowedFullScreenContent();
                        }

                        @Override
                        public void onAdDismissedFullScreenContent() {
                            super.onAdDismissedFullScreenContent();
                            mRewardedAd1 = null;
                            if (appDialogBeforeAdShow) {
                                if (progressBar.isShowing()) {
                                    progressBar.getDialog().dismiss();
                                }
                            }

                            Log.d("TAG", "The ad was dismissed.");

                            if (intent != null) {
                                activity.startActivity(intent);
                            }
                            if (isFinished) {
                                activity.finish();
                            }
                        }

                        @Override
                        public void onAdImpression() {
                            super.onAdImpression();
                        }

                        @Override
                        public void onAdClicked() {
                            super.onAdClicked();
                        }
                    });
                }
            });
        } else {
            nextRewardedPlatform(activity, intent, isFinished, google_i, facebook_i, applovin_i, google_i1, google_i2);
        }
    }

    private static void admobLoadShow2(final Activity activity, Intent intent, boolean isFinished, String google_i, String facebook_i, String applovin_i, String google_i1, String google_i2) {

        if (!google_i2.isEmpty()) {
            AdRequest adRequest = new AdRequest.Builder().build();
            RewardedAd.load(activity, google_i2, adRequest, new RewardedAdLoadCallback() {
                @Override
                public void onAdFailedToLoad(@NonNull LoadAdError loadAdError) {
                    //loadAds(context);
                    mRewardedAd2 = null;
                    nextRewardedPlatform(activity, intent, isFinished, google_i, facebook_i, applovin_i, google_i1, google_i2);
                }

                @Override
                public void onAdLoaded(@NonNull RewardedAd rewardedAd) {
                    mRewardedAd2 = rewardedAd;
                    Log.e(TAG, "onAdLoaded" + "REQ_LOAD");
                    if (appDialogBeforeAdShow) {
                        if (progressBar.isShowing()) {
                            progressBar.getDialog().dismiss();
                        }
                    }

                    mRewardedAd2.show(activity, new OnUserEarnedRewardListener() {
                        @Override
                        public void onUserEarnedReward(@NonNull RewardItem rewardItem) {

                        }
                    });

                    mRewardedAd2.setFullScreenContentCallback(new FullScreenContentCallback() {
                        @Override
                        public void onAdFailedToShowFullScreenContent(@NonNull AdError adError) {
                            super.onAdFailedToShowFullScreenContent(adError);
                            Log.d("TAG", "The ad failed to show.");
                            mRewardedAd2 = null;
                        }

                        @Override
                        public void onAdShowedFullScreenContent() {
                            super.onAdShowedFullScreenContent();
                        }

                        @Override
                        public void onAdDismissedFullScreenContent() {
                            super.onAdDismissedFullScreenContent();
                            mRewardedAd2 = null;
                            if (appDialogBeforeAdShow) {
                                if (progressBar.isShowing()) {
                                    progressBar.getDialog().dismiss();
                                }
                            }

                            Log.d("TAG", "The ad was dismissed.");

                            if (intent != null) {
                                activity.startActivity(intent);
                            }
                            if (isFinished) {
                                activity.finish();
                            }
                        }

                        @Override
                        public void onAdImpression() {
                            super.onAdImpression();
                        }

                        @Override
                        public void onAdClicked() {
                            super.onAdClicked();
                        }
                    });
                }
            });
        } else {
            nextRewardedPlatform(activity, intent, isFinished, google_i, facebook_i, applovin_i, google_i1, google_i2);
        }
    }

    private static void showRewardedAd(String platform, final Activity activity, Intent intent, boolean isFinished, String google_i, String facebook_i, String applovin_i, String google_i1, String google_i2) {
        if (appDialogBeforeAdShow) {
            if (!progressBar.isShowing()) {
                progressBar.show(activity);
            }
        }

        if (platform.equals(ADMOB)) {
            admobLoadShow(activity, intent, isFinished, google_i, facebook_i, applovin_i, google_i1, google_i2);
        } else if (platform.equals(ADMOB1)) {
            admobLoadShow1(activity, intent, isFinished, google_i, facebook_i, applovin_i, google_i1, google_i2);
        } else if (platform.equals(ADMOB2)) {
            admobLoadShow2(activity, intent, isFinished, google_i, facebook_i, applovin_i, google_i1, google_i2);
        } else {
            if (appDialogBeforeAdShow) {
                if (progressBar.isShowing()) {
                    progressBar.getDialog().dismiss();
                }
            }

            List<CustomAds> customAds = PrefLibAds.getCustomAdsData(activity);
            if (customAds != null && !customAds.isEmpty()) {
                Intent intent1 = new Intent(activity, CustomRewardedActivity.class);
                if (intent != null) {
                    intent1.putExtra("passedIntent", intent);
                }
                activity.startActivity(intent1);
            } else {
                if (intent != null) {
                    activity.startActivity(intent);
                }
                if (isFinished) {
                    activity.finish();
                }
            }
        }
    }

    private static void nextRewardedPlatform(Activity activity, Intent intent, boolean isFinished, String google_i, String facebook_i, String applovin_i, String google_i1, String google_i2) {

        if (rewarded_sequence.size() != 0) {
            rewarded_sequence.remove(0);

            if (rewarded_sequence.size() != 0) {
                showRewardedAd(rewarded_sequence.get(0), activity, intent, isFinished, google_i, facebook_i, applovin_i, google_i1, google_i2);
            } else {
                if (appDialogBeforeAdShow) {
                    if (progressBar.isShowing()) {
                        progressBar.getDialog().dismiss();
                    }
                }

                List<CustomAds> customAds = PrefLibAds.getCustomAdsData(activity);
                if (customAds != null && !customAds.isEmpty()) {
                    Intent intent1 = new Intent(activity, CustomRewardedActivity.class);
                    if (intent != null) {
                        intent1.putExtra("passedIntent", intent);
                    }
                    activity.startActivity(intent1);
                } else {
                    if (intent != null) {
                        activity.startActivity(intent);
                    }
                    if (isFinished) {
                        activity.finish();
                    }
                }
            }
        } else {
            if (appDialogBeforeAdShow) {
                if (progressBar.isShowing()) {
                    progressBar.getDialog().dismiss();
                }
            }

            List<CustomAds> customAds = PrefLibAds.getCustomAdsData(activity);
            if (customAds != null && !customAds.isEmpty()) {
                Intent intent1 = new Intent(activity, CustomRewardedActivity.class);
                if (intent != null) {
                    intent1.putExtra("passedIntent", intent);
                }
                activity.startActivity(intent1);
            } else {
                if (intent != null) {
                    activity.startActivity(intent);
                }
                if (isFinished) {
                    activity.finish();
                }
            }
        }
    }
}