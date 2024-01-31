package com.kidsads.visionkidsads.retrofit;

import android.app.Activity;
import android.content.Context;
import android.content.Intent;
import android.content.SharedPreferences;
import android.util.Log;

import androidx.annotation.NonNull;

import com.kidsads.visionkidsads.activity.CustomAppOpenActivity;
import com.kidsads.visionkidsads.ads.AppOpenManager;
import com.kidsads.visionkidsads.loader.AppOpenAdsLoader;
import com.kidsads.visionkidsads.model.AdsRepo;
import com.kidsads.visionkidsads.model.CustomAds;
import com.kidsads.visionkidsads.model.GetMoreAppGroups;
import com.kidsads.visionkidsads.utils.PrefLibAds;
import com.google.android.gms.ads.FullScreenContentCallback;
import com.google.android.gms.ads.MobileAds;
import com.google.android.gms.ads.appopen.AppOpenAd;
import com.google.android.gms.ads.initialization.InitializationStatus;
import com.google.android.gms.ads.initialization.OnInitializationCompleteListener;
import com.google.gson.Gson;

import java.util.ArrayList;
import java.util.List;

public class ManagerAdsData {
    private static final String TAG = ManagerAdsData.class.getSimpleName();
    public static String ADMOB = "admob";
    public static String FACEBOOK = "facebook";
    public static String APPLOVIN = "applovin";
    public static String ADMOB1 = "admanager1";
    public static String ADMOB2 = "admanager2";
    public static SharedPreferences mySharedPref;
    public static String appAdShowStatus = "";
    public static String appPrivacyPolicyLink = "";
    public static boolean appUpdateAppDialogStatus = false;
    public static boolean appDialogBeforeAdShow = false;
    public static int appMainClickCntSwAd = 0;
    public static int appInnerClickCntSwAd = 0;
    public static String ADMOB_APP_ID = "";
    public static String[] ADMOB_BANNER = {"", "", "", "", ""};
    public static String[] ADMOB_INTER = {"", "", "", "", ""};
    public static String[] ADMOB_NATIVE = {"", "", "", "", ""};
    public static String[] ADMOB_REWARD = {"", "", "", "", ""};
    public static String[] ADMOB_APP_OPEN = {"", "", "", "", ""};
    public static String[] FACEBOOK_INTER = {"", "", "", "", ""};
    public static String[] FACEBOOK_BANNER = {"", "", "", "", ""};
    public static String[] FACEBOOK_NATIVE = {"", "", "", "", ""};
    public static String[] FACEBOOK_REWARD = {"", "", "", "", ""};
    public static String[] APPLOVIN_BANNER = {"", "", "", "", ""};
    public static String[] APPLOVIN_INTER = {"", "", "", "", ""};
    public static String[] APPLOVIN_NATIVE = {"", "", "", "", ""};
    public static String[] APPLOVIN_REWARD = {"", "", "", "", ""};
    public static String ADMOB_APP_ID1 = "";
    public static String[] ADMOB_BANNER1 = {"", "", "", "", ""};
    public static String[] ADMOB_INTER1 = {"", "", "", "", ""};
    public static String[] ADMOB_NATIVE1 = {"", "", "", "", ""};
    public static String[] ADMOB_REWARD1 = {"", "", "", "", ""};
    public static String[] ADMOB_APP_OPEN1 = {"", "", "", "", ""};
    public static String ADMOB_APP_ID2 = "";
    public static String[] ADMOB_BANNER2 = {"", "", "", "", ""};
    public static String[] ADMOB_INTER2 = {"", "", "", "", ""};
    public static String[] ADMOB_NATIVE2 = {"", "", "", "", ""};
    public static String[] ADMOB_APP_OPEN2 = {"", "", "", "", ""};
    public static String[] ADMOB_REWARD2 = {"", "", "", "", ""};
    public static String multiScreen = "";
    public static String urlStatus = "";
    public static String isOpen = "1";
    public static int count_click = -1;
    static Context activity;
    static AppOpenAdsLoader appOpenAdsLoader = new AppOpenAdsLoader();
    private static String appopen_id_pre = "";
    private static ManagerAdsData mInstance;
    private static AppOpenManager appopenManager;
    private static AppOpenManager appopenManager1;
    private static AppOpenManager appopenManager2;
    private static AppOpenAd appOpenAd;
    private AdsRepo apiSettings;

    public ManagerAdsData(Context activity) {
        ManagerAdsData.activity = activity;
        mySharedPref = activity.getSharedPreferences(activity.getPackageName(), Context.MODE_PRIVATE);
        getDataFromDirectPref();
    }

    public static ManagerAdsData getInstance(Context activity) {
        ManagerAdsData.activity = activity;
        if (mInstance == null) {
            mInstance = new ManagerAdsData(activity);
        }
        return mInstance;
    }

    public static void getDataFromDirectPref() {
        mySharedPref = activity.getSharedPreferences(ManagerAdsData.activity.getPackageName(), Context.MODE_PRIVATE);
        String response1 = mySharedPref.getString("response", "");

        SharedPreferences.Editor editor_init = mySharedPref.edit();
        editor_init.putInt("count_admob_B", 0);
        editor_init.putInt("count_admob_I", 0);
        editor_init.putInt("count_admob_N", 0);
        editor_init.putInt("count_admob_AO", 0);
        editor_init.putInt("count_facebook_B", 0);
        editor_init.putInt("count_facebook_NB", 0);
        editor_init.putInt("count_facebook_I", 0);
        editor_init.putInt("count_facebook_N", 0);
        editor_init.putInt("count_admob_B1", 0);
        editor_init.putInt("count_admob_I1", 0);
        editor_init.putInt("count_admob_N1", 0);
        editor_init.putInt("count_admob_AO1", 0);
        editor_init.putInt("count_admob_B2", 0);
        editor_init.putInt("count_admob_I2", 0);
        editor_init.putInt("count_admob_N2", 0);
        editor_init.putInt("count_admob_AO2", 0);
        editor_init.putInt("count_applovin_B", 0);
        editor_init.putInt("count_applovin_I", 0);
        editor_init.putInt("count_applovin_N", 0);
        editor_init.putString("activityNames", "");
        editor_init.commit();

        if (!response1.isEmpty()) {
            try {
                Gson gson = new Gson();
                AdsRepo apiSettings = gson.fromJson(response1, AdsRepo.class);
                Log.e(TAG, "getResponseFromPref: " + response1);

                if (apiSettings.getData().getAppPrivacyPolicyLink().equals("")) {
                    appPrivacyPolicyLink = "https://ghanshyamprivacypolicy.blogspot.com/2021/10/privacy-policy-effective-year-2020.html";
                } else {
                    appPrivacyPolicyLink = apiSettings.getData().getAppPrivacyPolicyLink();
                }
                appUpdateAppDialogStatus = apiSettings.getData().isVersionUpdateDialog();
                appDialogBeforeAdShow = apiSettings.getData().isShowDialogBeforeAdShow();
                PrefLibAds.getInstance().setBool("adLoader", appDialogBeforeAdShow);

                appAdShowStatus = String.valueOf(apiSettings.getData().isShowAdInApp());

                appMainClickCntSwAd = apiSettings.getData().getMainPageAdClickCount();
                appInnerClickCntSwAd = apiSettings.getData().getInnerPageAdClickCount();

                PrefLibAds.getInstance().setString("app_name", apiSettings.getData().getAppName());
                PrefLibAds.getInstance().setString("app_versionCode", apiSettings.getData().getVersionUpdateDialogText());
                PrefLibAds.getInstance().setString("redirectToOtherAppText", apiSettings.getData().getRedirectToOtherAppText());

                PrefLibAds.getInstance().setString("app_privacyPolicyLink", appPrivacyPolicyLink);
                PrefLibAds.getInstance().setBool("app_updateAppDialogStatus", appUpdateAppDialogStatus);
                PrefLibAds.getInstance().setString("app_adShowStatus", appAdShowStatus);

                if (appMainClickCntSwAd == 0) {
                    PrefLibAds.getInstance().setInt("app_mainClickCntSwAd", 1);
                } else {
                    PrefLibAds.getInstance().setInt("app_mainClickCntSwAd", appMainClickCntSwAd);
                }

                if (appInnerClickCntSwAd == 0) {
                    PrefLibAds.getInstance().setInt("app_innerClickCntSwAd", 1);
                } else {
                    PrefLibAds.getInstance().setInt("app_innerClickCntSwAd", appInnerClickCntSwAd);
                }

                if (apiSettings.getData().getAdsOnEveryclick() == 0) {
                    PrefLibAds.getInstance().setInt("app_backPressAdLimit", 1);
                } else {
                    PrefLibAds.getInstance().setInt("app_backPressAdLimit", apiSettings.getData().getAdsOnEveryclick());
                }

                PrefLibAds.getInstance().setString("app_howShowAd", apiSettings.getData().getHowToShowAd());
                PrefLibAds.getInstance().setBool("splashAd", apiSettings.getData().isSplashAd());
                PrefLibAds.getInstance().setString("app_splashAdType", apiSettings.getData().getSplashAdType());
                PrefLibAds.getInstance().setString("oneSignalAppId", apiSettings.getData().getOneSignalAppId());

                PrefLibAds.getInstance().setBool("app_backPressAdStatus", apiSettings.getData().isBackPressAd());
                PrefLibAds.getInstance().setString("app_backPressAdType", apiSettings.getData().getBackPressAdType());

                PrefLibAds.getInstance().setBool("appNativePreLoad", apiSettings.getData().isAppMoreFieldNativePreLoad());
                PrefLibAds.getInstance().setBool("appBannerPreLoad", apiSettings.getData().isAppMoreFieldBannerPreLoad());
                PrefLibAds.getInstance().setBool("appBannerAdPlaceHolder", apiSettings.getData().isAppMoreFieldBannerAdPlaceholder());
                PrefLibAds.getInstance().setString("appAdPlaceHolderText", apiSettings.getData().getAppMoreFieldAdPlaceholderText());
                PrefLibAds.getInstance().setString("appNativeAdSize", apiSettings.getData().getAppMoreFieldNativeAdSize());
                PrefLibAds.getInstance().setBool("appBackgroundAppOpenAdStatus", apiSettings.getData().isAppMoreFieldBackgroundAppOpenAd());

                PrefLibAds.getInstance().setString("appAdsButtonColor", apiSettings.getData().getAppMoreFieldAdsButtonColor());
                PrefLibAds.getInstance().setString("appAdsTextColor", apiSettings.getData().getAppMoreFieldAdsTextColor());
                PrefLibAds.getInstance().setString("appAdsButtonTextColor", apiSettings.getData().getAppMoreFieldAdsButtonTextColor());
                PrefLibAds.getInstance().setString("appAdsBackgroundColor", apiSettings.getData().getAppMoreFieldAdsBackgroundColor());

                PrefLibAds.getInstance().setString("app_adPlatformSequence", apiSettings.getData().getAdPlatformSequence().toLowerCase());

                PrefLibAds.getInstance().setString("app_adApply", apiSettings.getData().getAdApply());
                PrefLibAds.getInstance().setString("app_howShowAdBanner", apiSettings.getData().getHowToShowAdBannerNativeBanner());
                PrefLibAds.getInstance().setString("app_howShowAdInterstitial", apiSettings.getData().getHowToShowAdInterstitialAndOthers());
                PrefLibAds.getInstance().setString("app_howShowAdNative", apiSettings.getData().getHowToShowAdNative());

                PrefLibAds.getInstance().setString("app_adPlatformSequenceInterstitial", apiSettings.getData().getAdPlatformSequenceForInterstitialAndOther().toLowerCase());
                PrefLibAds.getInstance().setString("app_adPlatformSequenceNative", apiSettings.getData().getAdPlatformSequenceForNative().toLowerCase());
                PrefLibAds.getInstance().setString("app_adPlatformSequenceBanner", apiSettings.getData().getAdPlatformSequenceForBannerNativeBanner().toLowerCase());

                PrefLibAds.getInstance().setString("loadAdIds", apiSettings.getData().getLoadAdIds());
                PrefLibAds.getInstance().setBool("moreApp", apiSettings.getData().getMoreApp());

                Log.e(TAG, "placementItem: " + apiSettings.getData());
                ADMOB_BANNER[0] = apiSettings.getData().getPlacement().getAdmob().getBanner();
                ADMOB_INTER[0] = apiSettings.getData().getPlacement().getAdmob().getInterstitial();
                ADMOB_NATIVE[0] = apiSettings.getData().getPlacement().getAdmob().getNativeAdvanced();
                ADMOB_APP_OPEN[0] = apiSettings.getData().getPlacement().getAdmob().getAppOpen();
                ADMOB_REWARD[0] = apiSettings.getData().getPlacement().getAdmob().getRewarded();
                PrefLibAds.getInstance().setString("ADMOB_BANNER", apiSettings.getData().getPlacement().getAdmob().getBanner());
                PrefLibAds.getInstance().setString("ADMOB_INTER", apiSettings.getData().getPlacement().getAdmob().getInterstitial());
                PrefLibAds.getInstance().setString("ADMOB_NATIVE", apiSettings.getData().getPlacement().getAdmob().getNativeAdvanced());
                PrefLibAds.getInstance().setString("ADMOB_REWARD", apiSettings.getData().getPlacement().getAdmob().getRewarded());
                PrefLibAds.getInstance().setString("ADMOB_APP_OPEN", apiSettings.getData().getPlacement().getAdmob().getAppOpen());

                FACEBOOK_BANNER[0] = apiSettings.getData().getPlacement().getFacebook().getBanner();
                FACEBOOK_INTER[0] = apiSettings.getData().getPlacement().getFacebook().getInterstitial();
                FACEBOOK_NATIVE[0] = apiSettings.getData().getPlacement().getFacebook().getNativeAdvanced();
                FACEBOOK_REWARD[0] = apiSettings.getData().getPlacement().getFacebook().getRewarded();

                PrefLibAds.getInstance().setString("FACEBOOK_BANNER", apiSettings.getData().getPlacement().getFacebook().getBanner());
                PrefLibAds.getInstance().setString("FACEBOOK_INTER", apiSettings.getData().getPlacement().getFacebook().getInterstitial());
                PrefLibAds.getInstance().setString("FACEBOOK_NATIVE", apiSettings.getData().getPlacement().getFacebook().getNativeAdvanced());
                PrefLibAds.getInstance().setString("FACEBOOK_REWARD", apiSettings.getData().getPlacement().getFacebook().getRewarded());

                APPLOVIN_BANNER[0] = apiSettings.getData().getPlacement().getApplovin().getBanner();
                APPLOVIN_INTER[0] = apiSettings.getData().getPlacement().getApplovin().getInterstitial();
                APPLOVIN_NATIVE[0] = apiSettings.getData().getPlacement().getApplovin().getNativeAdvanced();
                APPLOVIN_REWARD[0] = apiSettings.getData().getPlacement().getApplovin().getRewarded();

                PrefLibAds.getInstance().setString("APPLOVIN_BANNER", apiSettings.getData().getPlacement().getApplovin().getBanner());
                PrefLibAds.getInstance().setString("APPLOVIN_INTER", apiSettings.getData().getPlacement().getApplovin().getInterstitial());
                PrefLibAds.getInstance().setString("APPLOVIN_NATIVE", apiSettings.getData().getPlacement().getApplovin().getNativeAdvanced());
                PrefLibAds.getInstance().setString("APPLOVIN_REWARD", apiSettings.getData().getPlacement().getApplovin().getRewarded());

                ADMOB_BANNER2[0] = apiSettings.getData().getPlacement().getAdmanager2().getBanner();
                ADMOB_INTER2[0] = apiSettings.getData().getPlacement().getAdmanager2().getInterstitial();
                ADMOB_NATIVE2[0] = apiSettings.getData().getPlacement().getAdmanager2().getNativeAdvanced();
                ADMOB_APP_OPEN2[0] = apiSettings.getData().getPlacement().getAdmanager2().getAppOpen();
                ADMOB_REWARD1[0] = apiSettings.getData().getPlacement().getAdmanager2().getRewarded();
                PrefLibAds.getInstance().setString("AppOpenID2", apiSettings.getData().getPlacement().getAdmanager2().getAppOpen());

                PrefLibAds.getInstance().setString("ADMOB_BANNER2", apiSettings.getData().getPlacement().getAdmanager2().getBanner());
                PrefLibAds.getInstance().setString("ADMOB_INTER2", apiSettings.getData().getPlacement().getAdmanager2().getInterstitial());
                PrefLibAds.getInstance().setString("ADMOB_NATIVE2", apiSettings.getData().getPlacement().getAdmanager2().getNativeAdvanced());
                PrefLibAds.getInstance().setString("ADMOB_REWARD2", apiSettings.getData().getPlacement().getAdmanager2().getRewarded());
                PrefLibAds.getInstance().setString("ADMOB_APP_OPEN2", apiSettings.getData().getPlacement().getAdmanager2().getAppOpen());

                ADMOB_BANNER1[0] = apiSettings.getData().getPlacement().getAdmanager1().getBanner();
                ADMOB_INTER1[0] = apiSettings.getData().getPlacement().getAdmanager1().getInterstitial();
                ADMOB_NATIVE1[0] = apiSettings.getData().getPlacement().getAdmanager1().getNativeAdvanced();
                ADMOB_APP_OPEN1[0] = apiSettings.getData().getPlacement().getAdmanager1().getAppOpen();
                ADMOB_REWARD2[0] = apiSettings.getData().getPlacement().getAdmanager1().getRewarded();
                PrefLibAds.getInstance().setString("AppOpenID1", apiSettings.getData().getPlacement().getAdmanager1().getAppOpen());

                PrefLibAds.getInstance().setString("ADMOB_BANNER1", apiSettings.getData().getPlacement().getAdmanager1().getBanner());
                PrefLibAds.getInstance().setString("ADMOB_INTER1", apiSettings.getData().getPlacement().getAdmanager1().getInterstitial());
                PrefLibAds.getInstance().setString("ADMOB_NATIVE1", apiSettings.getData().getPlacement().getAdmanager1().getNativeAdvanced());
                PrefLibAds.getInstance().setString("ADMOB_REWARD1", apiSettings.getData().getPlacement().getAdmanager1().getRewarded());
                PrefLibAds.getInstance().setString("ADMOB_APP_OPEN1", apiSettings.getData().getPlacement().getAdmanager1().getAppOpen());

                List<GetMoreAppGroups> appInfoList = apiSettings.getData().getGetMoreAppGroups();
                if (!appInfoList.isEmpty()) {
                    PrefLibAds.setMoreAppData(activity, appInfoList);
                    for (int i = 0; i < appInfoList.size(); i++) {
                        PrefLibAds.setAssignAppData(activity, appInfoList.get(i).getAssignAppIds());
                    }
                }

                List<CustomAds> customAds = apiSettings.getData().getGetCustomAds();
                if (!customAds.isEmpty()) {
                    PrefLibAds.setCustomAdsData(activity, customAds);
                }

                Log.e(TAG, "extradata: " + apiSettings.getData().getAppExtraData());

                if (apiSettings.getData().getAppExtraData() != null) {
                    multiScreen = apiSettings.getData().getAppExtraData().getMultiscreen();
                    urlStatus = apiSettings.getData().getAppExtraData().getUrl();
                }
            } catch (Exception e) {
                e.printStackTrace();
            }
        }
    }

    public static String getRandomID(String platform, String adFormat) {
        String return_adId = "";
        mySharedPref = activity.getSharedPreferences(activity.getPackageName(), Context.MODE_PRIVATE);
        SharedPreferences.Editor editor_count = mySharedPref.edit();
        int count = 0;
        String[] platform_Format_Ids = {"", "", "", "", ""};
        if (platform.equals(ADMOB)) {
            switch (adFormat) {
                case "B":
                    platform_Format_Ids = ADMOB_BANNER;
                    count = mySharedPref.getInt("count_admob_B", 0) + 1;
                    editor_count.putInt("count_admob_B", count);
                    break;
                case "I":
                    platform_Format_Ids = ADMOB_INTER;
                    count = mySharedPref.getInt("count_admob_I", 0) + 1;
                    editor_count.putInt("count_admob_I", count);
                    break;
                case "N":
                    platform_Format_Ids = ADMOB_NATIVE;
                    count = mySharedPref.getInt("count_admob_N", 0) + 1;
                    editor_count.putInt("count_admob_N", count);
                    break;
                case "R":
                    platform_Format_Ids = ADMOB_REWARD;
                    count = mySharedPref.getInt("count_admob_R", 0) + 1;
                    editor_count.putInt("count_admob_R", count);
                    break;
                case "AO":
                    platform_Format_Ids = ADMOB_APP_OPEN;
                    count = mySharedPref.getInt("count_admob_AO", 0) + 1;
                    editor_count.putInt("count_admob_AO", count);
                    break;
            }
        } else if (platform.equals(FACEBOOK)) {
            switch (adFormat) {
                case "B":
                    platform_Format_Ids = FACEBOOK_BANNER;
                    count = mySharedPref.getInt("count_facebook_B", 0) + 1;
                    editor_count.putInt("count_facebook_B", count);
                    break;
                case "I":
                    platform_Format_Ids = FACEBOOK_INTER;
                    count = mySharedPref.getInt("count_facebook_I", 0) + 1;
                    editor_count.putInt("count_facebook_I", count);
                    break;
                case "R":
                    platform_Format_Ids = FACEBOOK_REWARD;
                    count = mySharedPref.getInt("count_facebook_R", 0) + 1;
                    editor_count.putInt("count_facebook_R", count);
                    break;

                case "N":
                    platform_Format_Ids = FACEBOOK_NATIVE;
                    count = mySharedPref.getInt("count_facebook_N", 0) + 1;
                    editor_count.putInt("count_facebook_N", count);
                    break;
            }
        } else if (platform.equals(ADMOB1)) {
            switch (adFormat) {
                case "B":
                    platform_Format_Ids = ADMOB_BANNER1;
                    count = mySharedPref.getInt("count_admob_B1", 0) + 1;
                    editor_count.putInt("count_admob_B1", count);
                    break;
                case "I":
                    platform_Format_Ids = ADMOB_INTER1;
                    count = mySharedPref.getInt("count_admob_I1", 0) + 1;
                    editor_count.putInt("count_admob_I1", count);
                    break;
                case "N":
                    platform_Format_Ids = ADMOB_NATIVE1;
                    count = mySharedPref.getInt("count_admob_N1", 0) + 1;
                    editor_count.putInt("count_admob_N1", count);
                    break;
                case "R":
                    platform_Format_Ids = ADMOB_REWARD1;
                    count = mySharedPref.getInt("count_admob_R1", 0) + 1;
                    editor_count.putInt("count_admob_R1", count);
                    break;
                case "AO":
                    platform_Format_Ids = ADMOB_APP_OPEN1;
                    count = mySharedPref.getInt("count_admob_AO1", 0) + 1;
                    editor_count.putInt("count_admob_AO1", count);
                    break;
            }
        } else if (platform.equals(ADMOB2)) {
            switch (adFormat) {
                case "B":
                    platform_Format_Ids = ADMOB_BANNER2;
                    count = mySharedPref.getInt("count_admob_B2", 0) + 1;
                    editor_count.putInt("count_admob_B2", count);
                    break;
                case "I":
                    platform_Format_Ids = ADMOB_INTER2;
                    count = mySharedPref.getInt("count_admob_I2", 0) + 1;
                    editor_count.putInt("count_admob_I2", count);
                    break;
                case "N":
                    platform_Format_Ids = ADMOB_NATIVE2;
                    count = mySharedPref.getInt("count_admob_N2", 0) + 1;
                    editor_count.putInt("count_admob_N2", count);
                    break;
                case "R":
                    platform_Format_Ids = ADMOB_REWARD2;
                    count = mySharedPref.getInt("count_admob_R2", 0) + 1;
                    editor_count.putInt("count_admob_R2", count);
                    break;
                case "AO":
                    platform_Format_Ids = ADMOB_APP_OPEN2;
                    count = mySharedPref.getInt("count_admob_AO2", 0) + 1;
                    editor_count.putInt("count_admob_AO2", count);
                    break;
            }
        } else if (platform.equals(APPLOVIN)) {
            switch (adFormat) {
                case "B":
                    platform_Format_Ids = APPLOVIN_BANNER;
                    count = mySharedPref.getInt("count_applovin_B", 0) + 1;
                    editor_count.putInt("count_applovin_B", count);
                    break;
                case "I":
                    platform_Format_Ids = APPLOVIN_INTER;
                    count = mySharedPref.getInt("count_applovin_I", 0) + 1;
                    editor_count.putInt("count_applovin_I", count);
                    break;
                case "N":
                    platform_Format_Ids = APPLOVIN_NATIVE;
                    count = mySharedPref.getInt("count_applovin_N", 0) + 1;
                    editor_count.putInt("count_applovin_N", count);
                    break;
                case "R":
                    platform_Format_Ids = APPLOVIN_REWARD;
                    count = mySharedPref.getInt("count_applovin_R2", 0) + 1;
                    editor_count.putInt("count_applovin_R2", count);
                    break;
            }
        }

        ArrayList<String> placemnt_Ids = new ArrayList<String>();
        for (String platformFormatId : platform_Format_Ids) {
            if (!platformFormatId.isEmpty()) {
                placemnt_Ids.add(platformFormatId);
            }
        }

        if (placemnt_Ids.size() != 0) {
            if (count % placemnt_Ids.size() == 0) {
                return_adId = placemnt_Ids.get(0);
            } else if (count % placemnt_Ids.size() == 1) {
                return_adId = placemnt_Ids.get(1);
            } else if (count % placemnt_Ids.size() == 2) {
                return_adId = placemnt_Ids.get(2);
            } else if (count % placemnt_Ids.size() == 3) {
                return_adId = placemnt_Ids.get(3);
            } else if (count % placemnt_Ids.size() == 4) {
                return_adId = placemnt_Ids.get(4);
            }
        }
        return return_adId;
    }

    public static void showAppOpenAd(final Activity activity, Intent intent, boolean isFinished, int how_many_clicks) {

        if (PrefLibAds.getInstance().getString("app_adShowStatus").equals("false")) {
            if (intent != null) {
                activity.startActivity(intent);
            }
            if (isFinished) {
                activity.finish();
            }
            return;
        }

        count_click++;
        if (how_many_clicks != 0) {
            if (count_click % how_many_clicks != 0) {
                if (intent != null) {
                    activity.startActivity(intent);
                }
                if (isFinished) {
                    activity.finish();
                }
                return;
            }
        }

        if (appopenManager == null) {
            List<CustomAds> customAds = PrefLibAds.getCustomAdsData(activity);
            if (customAds != null && !customAds.isEmpty()) {
                Intent intent1 = new Intent(activity, CustomAppOpenActivity.class);
                if (intent != null) {
                    intent1.putExtra("passedIntent", intent);
                }
                activity.startActivity(intent1);
                if (isFinished) {
                    activity.finish();
                }
            } else {
                if (intent != null) {
                    activity.startActivity(intent);
                }
                if (isFinished) {
                    activity.finish();
                }
            }
            return;
        }

        if (appopen_id_pre.isEmpty()) {
            appopen_id_pre = getRandomID(ADMOB, "AO");
        }

        if (appDialogBeforeAdShow) {
            if (!appOpenAdsLoader.isShowing()) {
                appOpenAdsLoader.show(activity);
            }
        }

        /*String app_open_id = PrefLibAds.getInstance().getString("ADMOB_APP_OPEN");

        if (app_open_id != null && !app_open_id.equals("")) {



            AppOpenAd.AppOpenAdLoadCallback loadCallback;
            loadCallback = new AppOpenAd.AppOpenAdLoadCallback() {
                @Override
                public void onAdLoaded(@NonNull AppOpenAd appAd) {
                    super.onAdLoaded(appAd);
                    appOpenAd = appAd;

                    showAdIfAvailable(activity, intent, isFinished);
                }

                @Override
                public void onAdFailedToLoad(@NonNull LoadAdError loadAdError) {
                    super.onAdFailedToLoad(loadAdError);
                    if (PrefLibAds.getInstance().getBool("adLoader", false)) {
                        if (appOpenAdsLoader.isShowing()) {
                            appOpenAdsLoader.getDialog().dismiss();
                        }
                    }

                    if (intent != null) {
                        activity.startActivity(intent);
                    }
                    if (isFinished) {
                        activity.finish();
                    }
                }
            };
            AdRequest request = new AdRequest.Builder().build();
            AppOpenAd.load(activity, app_open_id, request, AppOpenAd.APP_OPEN_AD_ORIENTATION_PORTRAIT, loadCallback);
        } else {
            if (intent != null) {
                activity.startActivity(intent);
            }
            if (isFinished) {
                activity.finish();
            }
        }*/


        appopenManager.showAdIfAvailable(new AppOpenManager.splshADlistner() {
            @Override
            public void onSuccess() {
                appopenManager = null;
                if (appDialogBeforeAdShow) {
                    if (appOpenAdsLoader.isShowing()) {
                        appOpenAdsLoader.getDialog().dismiss();
                    }
                }
                loadAdmobAppOpenAd(activity, appopen_id_pre);
                Log.e(TAG, "onError: " + "AD_SHOW");
                if (intent != null) {
                    activity.startActivity(intent);
                }
                if (isFinished) {
                    activity.finish();
                }
            }

            @Override
            public void onError(String error) {
                appopenManager = null;
                if (appDialogBeforeAdShow) {
                    if (appOpenAdsLoader.isShowing()) {
                        appOpenAdsLoader.getDialog().dismiss();
                    }
                }
                loadAdmobAppOpenAd(activity, appopen_id_pre);
                Log.e(TAG, "onError: " + "ERROR_FAIL");
                List<CustomAds> customAds = PrefLibAds.getCustomAdsData(activity);
                if (customAds != null && !customAds.isEmpty()) {
                    Intent intent1 = new Intent(activity, CustomAppOpenActivity.class);
                    if (intent != null) {
                        intent1.putExtra("passedIntent", intent);
                    }
                    activity.startActivity(intent1);
                    if (isFinished) {
                        activity.finish();
                    }
                } else {
                    if (intent != null) {
                        activity.startActivity(intent);
                    }
                    if (isFinished) {
                        activity.finish();
                    }
                }
            }
        });
    }

    public static void showAdIfAvailable(Activity activity, final Intent intent, boolean isFinished) {
        FullScreenContentCallback fullScreenContentCallback = new FullScreenContentCallback() {
            @Override
            public void onAdDismissedFullScreenContent() {
                appOpenAd = null;
                if (PrefLibAds.getInstance().getBool("adLoader", false)) {
                    if (appOpenAdsLoader.isShowing()) {
                        appOpenAdsLoader.getDialog().dismiss();
                    }
                }
                if (intent != null) {
                    activity.startActivity(intent);
                }
                if (isFinished) {
                    activity.finish();
                }
            }

            @Override
            public void onAdFailedToShowFullScreenContent(@NonNull com.google.android.gms.ads.AdError adError) {
                if (PrefLibAds.getInstance().getBool("adLoader", false)) {
                    if (appOpenAdsLoader.isShowing()) {
                        appOpenAdsLoader.getDialog().dismiss();
                    }
                }
                if (intent != null) {
                    activity.startActivity(intent);
                }
                if (isFinished) {
                    activity.finish();
                }
            }

            @Override
            public void onAdShowedFullScreenContent() {
            }
        };
        appOpenAd.setFullScreenContentCallback(fullScreenContentCallback);
        appOpenAd.show(activity);
    }

    public static void loadAdmobAppOpenAd(final Context context, String appopen_id) {

        if (appopen_id.isEmpty()) {
            appopen_id = getRandomID(ADMOB, "AO");
        }

        if (appAdShowStatus.equals("false")) {
            return;
        }

        if (PrefLibAds.getInstance().getString("ad_showAdStatus").equals("false") || appopen_id.isEmpty()) {
            return;
        }

        if (appopenManager != null) {
            if (appopen_id_pre.equals(appopen_id)) {
                return;
            }
        }

        appopen_id_pre = appopen_id;

        appopenManager = new AppOpenManager((Activity) context, appopen_id);
        appopenManager.fetchAd(new AppOpenManager.splshADlistner() {
            @Override
            public void onSuccess() {
                Log.e(TAG, "onSuccess: " + "REQ_LOAD");
            }

            @Override
            public void onError(String error) {
                appopenManager = null;
                Log.e(TAG, "onSuccess: " + "REQ_FAIL");
            }
        });
    }

    public static void showAppOpenAdTest(final Activity activity, Intent intent, boolean isFinished) {

        if (PrefLibAds.getInstance().getString("app_adShowStatus").equals("false")) {
            if (intent != null) {
                activity.startActivity(intent);
            }
            if (isFinished) {
                activity.finish();
            }
            return;
        }

        if (appDialogBeforeAdShow) {
            if (!appOpenAdsLoader.isShowing()) {
                appOpenAdsLoader.show(activity);
            }
        }

        appopenManager = new AppOpenManager(activity, ADMOB_APP_OPEN[0]);
        appopenManager.fetchAdSplash(new AppOpenManager.splshADlistner() {
            @Override
            public void onSuccess() {
                appopenManager.showAdIfAvailable(new AppOpenManager.splshADlistner() {
                    @Override
                    public void onSuccess() {
                        appopenManager = null;
                        if (appDialogBeforeAdShow) {
                            if (appOpenAdsLoader.isShowing()) {
                                appOpenAdsLoader.getDialog().dismiss();
                            }
                        }
                        if (intent != null) {
                            activity.startActivity(intent);
                        }
                        if (isFinished) {
                            activity.finish();
                        }
                    }

                    @Override
                    public void onError(String error) {
                        appopenManager1 = new AppOpenManager(activity, ADMOB_APP_OPEN1[0]);
                        appopenManager1.fetchAdSplash(new AppOpenManager.splshADlistner() {
                            @Override
                            public void onSuccess() {
                                appopenManager1.showAdIfAvailable(new AppOpenManager.splshADlistner() {
                                    @Override
                                    public void onSuccess() {
                                        appopenManager1 = null;
                                        if (appDialogBeforeAdShow) {
                                            if (appOpenAdsLoader.isShowing()) {
                                                appOpenAdsLoader.getDialog().dismiss();
                                            }
                                        }
                                        if (intent != null) {
                                            activity.startActivity(intent);
                                        }
                                        if (isFinished) {
                                            activity.finish();
                                        }
                                    }

                                    @Override
                                    public void onError(String error) {
                                        appopenManager2 = new AppOpenManager(activity, ADMOB_APP_OPEN2[0]);
                                        appopenManager2.fetchAdSplash(new AppOpenManager.splshADlistner() {
                                            @Override
                                            public void onSuccess() {
                                                appopenManager2.showAdIfAvailable(new AppOpenManager.splshADlistner() {
                                                    @Override
                                                    public void onSuccess() {
                                                        appopenManager2 = null;
                                                        if (appDialogBeforeAdShow) {
                                                            if (appOpenAdsLoader.isShowing()) {
                                                                appOpenAdsLoader.getDialog().dismiss();
                                                            }
                                                        }
                                                        if (intent != null) {
                                                            activity.startActivity(intent);
                                                        }
                                                        if (isFinished) {
                                                            activity.finish();
                                                        }
                                                    }

                                                    @Override
                                                    public void onError(String error) {
                                                        appopenManager2 = null;
                                                        if (appDialogBeforeAdShow) {
                                                            if (appOpenAdsLoader.isShowing()) {
                                                                appOpenAdsLoader.getDialog().dismiss();
                                                            }
                                                        }
                                                        List<CustomAds> customAds = PrefLibAds.getCustomAdsData(activity);
                                                        if (customAds != null && !customAds.isEmpty()) {
                                                            Intent intent1 = new Intent(activity, CustomAppOpenActivity.class);
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
                                                });
                                            }

                                            @Override
                                            public void onError(String error) {
                                                appopenManager2 = null;
                                                if (appDialogBeforeAdShow) {
                                                    if (appOpenAdsLoader.isShowing()) {
                                                        appOpenAdsLoader.getDialog().dismiss();
                                                    }
                                                }
                                                List<CustomAds> customAds = PrefLibAds.getCustomAdsData(activity);
                                                if (customAds != null && !customAds.isEmpty()) {
                                                    Intent intent1 = new Intent(activity, CustomAppOpenActivity.class);
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
                                        });
                                    }
                                });
                            }

                            @Override
                            public void onError(String error) {
                                appopenManager2 = new AppOpenManager(activity, ADMOB_APP_OPEN2[0]);
                                appopenManager2.fetchAdSplash(new AppOpenManager.splshADlistner() {
                                    @Override
                                    public void onSuccess() {
                                        appopenManager2.showAdIfAvailable(new AppOpenManager.splshADlistner() {
                                            @Override
                                            public void onSuccess() {
                                                appopenManager2 = null;
                                                if (appDialogBeforeAdShow) {
                                                    if (appOpenAdsLoader.isShowing()) {
                                                        appOpenAdsLoader.getDialog().dismiss();
                                                    }
                                                }
                                                if (intent != null) {
                                                    activity.startActivity(intent);
                                                }
                                                if (isFinished) {
                                                    activity.finish();
                                                }
                                            }

                                            @Override
                                            public void onError(String error) {
                                                appopenManager2 = null;
                                                if (appDialogBeforeAdShow) {
                                                    if (appOpenAdsLoader.isShowing()) {
                                                        appOpenAdsLoader.getDialog().dismiss();
                                                    }
                                                }
                                                List<CustomAds> customAds = PrefLibAds.getCustomAdsData(activity);
                                                if (customAds != null && !customAds.isEmpty()) {
                                                    Intent intent1 = new Intent(activity, CustomAppOpenActivity.class);
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
                                        });
                                    }

                                    @Override
                                    public void onError(String error) {
                                        appopenManager2 = null;
                                        if (appDialogBeforeAdShow) {
                                            if (appOpenAdsLoader.isShowing()) {
                                                appOpenAdsLoader.getDialog().dismiss();
                                            }
                                        }
                                        List<CustomAds> customAds = PrefLibAds.getCustomAdsData(activity);
                                        if (customAds != null && !customAds.isEmpty()) {
                                            Intent intent1 = new Intent(activity, CustomAppOpenActivity.class);
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
                                });
                            }
                        });
                    }
                });
            }

            @Override
            public void onError(String error) {
                appopenManager1 = new AppOpenManager(activity, ADMOB_APP_OPEN1[0]);
                appopenManager1.fetchAdSplash(new AppOpenManager.splshADlistner() {
                    @Override
                    public void onSuccess() {
                        appopenManager1.showAdIfAvailable(new AppOpenManager.splshADlistner() {
                            @Override
                            public void onSuccess() {
                                appopenManager1 = null;
                                if (appDialogBeforeAdShow) {
                                    if (appOpenAdsLoader.isShowing()) {
                                        appOpenAdsLoader.getDialog().dismiss();
                                    }
                                }
                                if (intent != null) {
                                    activity.startActivity(intent);
                                }
                                if (isFinished) {
                                    activity.finish();
                                }
                            }

                            @Override
                            public void onError(String error) {
                                appopenManager1 = null;
                                appopenManager2 = new AppOpenManager(activity, ADMOB_APP_OPEN2[0]);
                                appopenManager2.fetchAdSplash(new AppOpenManager.splshADlistner() {
                                    @Override
                                    public void onSuccess() {
                                        appopenManager2.showAdIfAvailable(new AppOpenManager.splshADlistner() {
                                            @Override
                                            public void onSuccess() {
                                                appopenManager2 = null;
                                                if (appDialogBeforeAdShow) {
                                                    if (appOpenAdsLoader.isShowing()) {
                                                        appOpenAdsLoader.getDialog().dismiss();
                                                    }
                                                }
                                                if (intent != null) {
                                                    activity.startActivity(intent);
                                                }
                                                if (isFinished) {
                                                    activity.finish();
                                                }
                                            }

                                            @Override
                                            public void onError(String error) {
                                                appopenManager2 = null;
                                                if (appDialogBeforeAdShow) {
                                                    if (appOpenAdsLoader.isShowing()) {
                                                        appOpenAdsLoader.getDialog().dismiss();
                                                    }
                                                }
                                                List<CustomAds> customAds = PrefLibAds.getCustomAdsData(activity);
                                                if (customAds != null && !customAds.isEmpty()) {
                                                    Intent intent1 = new Intent(activity, CustomAppOpenActivity.class);
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
                                        });
                                    }

                                    @Override
                                    public void onError(String error) {
                                        appopenManager2 = null;
                                        if (appDialogBeforeAdShow) {
                                            if (appOpenAdsLoader.isShowing()) {
                                                appOpenAdsLoader.getDialog().dismiss();
                                            }
                                        }
                                        List<CustomAds> customAds = PrefLibAds.getCustomAdsData(activity);
                                        if (customAds != null && !customAds.isEmpty()) {
                                            Intent intent1 = new Intent(activity, CustomAppOpenActivity.class);
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
                                });
                            }
                        });
                    }

                    @Override
                    public void onError(String error) {
                        appopenManager1 = null;
                        appopenManager2 = new AppOpenManager(activity, ADMOB_APP_OPEN2[0]);
                        appopenManager2.fetchAdSplash(new AppOpenManager.splshADlistner() {
                            @Override
                            public void onSuccess() {
                                appopenManager2.showAdIfAvailable(new AppOpenManager.splshADlistner() {
                                    @Override
                                    public void onSuccess() {
                                        appopenManager2 = null;
                                        if (appDialogBeforeAdShow) {
                                            if (appOpenAdsLoader.isShowing()) {
                                                appOpenAdsLoader.getDialog().dismiss();
                                            }
                                        }
                                        if (intent != null) {
                                            activity.startActivity(intent);
                                        }
                                        if (isFinished) {
                                            activity.finish();
                                        }
                                    }

                                    @Override
                                    public void onError(String error) {
                                        appopenManager2 = null;
                                        if (appDialogBeforeAdShow) {
                                            if (appOpenAdsLoader.isShowing()) {
                                                appOpenAdsLoader.getDialog().dismiss();
                                            }
                                        }
                                        List<CustomAds> customAds = PrefLibAds.getCustomAdsData(activity);
                                        if (customAds != null && !customAds.isEmpty()) {
                                            Intent intent1 = new Intent(activity, CustomAppOpenActivity.class);
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
                                });
                            }

                            @Override
                            public void onError(String error) {
                                appopenManager2 = null;
                                if (appDialogBeforeAdShow) {
                                    if (appOpenAdsLoader.isShowing()) {
                                        appOpenAdsLoader.getDialog().dismiss();
                                    }
                                }
                                List<CustomAds> customAds = PrefLibAds.getCustomAdsData(activity);
                                if (customAds != null && !customAds.isEmpty()) {
                                    Intent intent1 = new Intent(activity, CustomAppOpenActivity.class);
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
                        });
                    }
                });
            }
        });
    }

    public void getDataFromDirectPref(Activity activity, AdsListener listener) {
        mySharedPref = activity.getSharedPreferences(activity.getPackageName(), Context.MODE_PRIVATE);
        String adsResponse = mySharedPref.getString("response", "");

        SharedPreferences.Editor editor_init = mySharedPref.edit();
        editor_init.putInt("count_admob_B", 0);
        editor_init.putInt("count_admob_I", 0);
        editor_init.putInt("count_admob_N", 0);
        editor_init.putInt("count_admob_AO", 0);
        editor_init.putInt("count_facebook_B", 0);
        editor_init.putInt("count_facebook_NB", 0);
        editor_init.putInt("count_facebook_I", 0);
        editor_init.putInt("count_facebook_N", 0);
        editor_init.putInt("count_admob_B1", 0);
        editor_init.putInt("count_admob_I1", 0);
        editor_init.putInt("count_admob_N1", 0);
        editor_init.putInt("count_admob_AO1", 0);
        editor_init.putInt("count_admob_B2", 0);
        editor_init.putInt("count_admob_I2", 0);
        editor_init.putInt("count_admob_N2", 0);
        editor_init.putInt("count_admob_AO2", 0);
        editor_init.putInt("count_applovin_B", 0);
        editor_init.putInt("count_applovin_I", 0);
        editor_init.putInt("count_applovin_N", 0);
        editor_init.putString("activityNames", "");
        editor_init.commit();

        if (!adsResponse.isEmpty()) {
            try {
                Gson gson = new Gson();
                apiSettings = gson.fromJson(adsResponse, AdsRepo.class);
                Log.e(TAG, "apiSettings: " + apiSettings);

                if (apiSettings.getData().getAppPrivacyPolicyLink().equals("")) {
                    appPrivacyPolicyLink = "https://ghanshyamprivacypolicy.blogspot.com/2021/10/privacy-policy-effective-year-2020.html";
                } else {
                    appPrivacyPolicyLink = apiSettings.getData().getAppPrivacyPolicyLink();
                }
                appUpdateAppDialogStatus = apiSettings.getData().isVersionUpdateDialog();
                appDialogBeforeAdShow = apiSettings.getData().isShowDialogBeforeAdShow();
                PrefLibAds.getInstance().setBool("adLoader", appDialogBeforeAdShow);

                appAdShowStatus = String.valueOf(apiSettings.getData().isShowAdInApp());

                appMainClickCntSwAd = apiSettings.getData().getMainPageAdClickCount();
                appInnerClickCntSwAd = apiSettings.getData().getInnerPageAdClickCount();

                PrefLibAds.getInstance().setString("app_name", apiSettings.getData().getAppName());
                PrefLibAds.getInstance().setString("app_versionCode", apiSettings.getData().getVersionUpdateDialogText());
                PrefLibAds.getInstance().setString("redirectToOtherAppText", apiSettings.getData().getRedirectToOtherAppText());

                PrefLibAds.getInstance().setString("app_privacyPolicyLink", appPrivacyPolicyLink);
                PrefLibAds.getInstance().setBool("app_updateAppDialogStatus", appUpdateAppDialogStatus);
                PrefLibAds.getInstance().setString("app_adShowStatus", appAdShowStatus);

                if (appMainClickCntSwAd == 0) {
                    PrefLibAds.getInstance().setInt("app_mainClickCntSwAd", 1);
                } else {
                    PrefLibAds.getInstance().setInt("app_mainClickCntSwAd", appMainClickCntSwAd);
                }

                if (appInnerClickCntSwAd == 0) {
                    PrefLibAds.getInstance().setInt("app_innerClickCntSwAd", 1);
                } else {
                    PrefLibAds.getInstance().setInt("app_innerClickCntSwAd", appInnerClickCntSwAd);
                }

                if (apiSettings.getData().getAdsOnEveryclick() == 0) {
                    PrefLibAds.getInstance().setInt("app_backPressAdLimit", 1);
                } else {
                    PrefLibAds.getInstance().setInt("app_backPressAdLimit", apiSettings.getData().getAdsOnEveryclick());
                }

                PrefLibAds.getInstance().setString("app_howShowAd", apiSettings.getData().getHowToShowAd());
                PrefLibAds.getInstance().setBool("splashAd", apiSettings.getData().isSplashAd());
                PrefLibAds.getInstance().setString("app_splashAdType", apiSettings.getData().getSplashAdType());
                PrefLibAds.getInstance().setString("oneSignalAppId", apiSettings.getData().getOneSignalAppId());

                PrefLibAds.getInstance().setBool("app_backPressAdStatus", apiSettings.getData().isBackPressAd());
                PrefLibAds.getInstance().setString("app_backPressAdType", apiSettings.getData().getBackPressAdType());

                PrefLibAds.getInstance().setBool("appNativePreLoad", apiSettings.getData().isAppMoreFieldNativePreLoad());
                PrefLibAds.getInstance().setBool("appBannerPreLoad", apiSettings.getData().isAppMoreFieldBannerPreLoad());
                PrefLibAds.getInstance().setBool("appBannerAdPlaceHolder", apiSettings.getData().isAppMoreFieldBannerAdPlaceholder());
                PrefLibAds.getInstance().setString("appAdPlaceHolderText", apiSettings.getData().getAppMoreFieldAdPlaceholderText());
                PrefLibAds.getInstance().setString("appNativeAdSize", apiSettings.getData().getAppMoreFieldNativeAdSize());
                PrefLibAds.getInstance().setBool("appBackgroundAppOpenAdStatus", apiSettings.getData().isAppMoreFieldBackgroundAppOpenAd());

                PrefLibAds.getInstance().setString("appAdsButtonColor", apiSettings.getData().getAppMoreFieldAdsButtonColor());
                PrefLibAds.getInstance().setString("appAdsTextColor", apiSettings.getData().getAppMoreFieldAdsTextColor());
                PrefLibAds.getInstance().setString("appAdsButtonTextColor", apiSettings.getData().getAppMoreFieldAdsButtonTextColor());
                PrefLibAds.getInstance().setString("appAdsBackgroundColor", apiSettings.getData().getAppMoreFieldAdsBackgroundColor());

                PrefLibAds.getInstance().setString("app_adPlatformSequence", apiSettings.getData().getAdPlatformSequence().toLowerCase());

                PrefLibAds.getInstance().setString("app_adApply", apiSettings.getData().getAdApply());
                PrefLibAds.getInstance().setString("app_howShowAdBanner", apiSettings.getData().getHowToShowAdBannerNativeBanner());
                PrefLibAds.getInstance().setString("app_howShowAdInterstitial", apiSettings.getData().getHowToShowAdInterstitialAndOthers());
                PrefLibAds.getInstance().setString("app_howShowAdNative", apiSettings.getData().getHowToShowAdNative());

                PrefLibAds.getInstance().setString("app_adPlatformSequenceInterstitial", apiSettings.getData().getAdPlatformSequenceForInterstitialAndOther().toLowerCase());
                PrefLibAds.getInstance().setString("app_adPlatformSequenceNative", apiSettings.getData().getAdPlatformSequenceForNative().toLowerCase());
                PrefLibAds.getInstance().setString("app_adPlatformSequenceBanner", apiSettings.getData().getAdPlatformSequenceForBannerNativeBanner().toLowerCase());

                PrefLibAds.getInstance().setString("loadAdIds", apiSettings.getData().getLoadAdIds());
                PrefLibAds.getInstance().setBool("moreApp", apiSettings.getData().getMoreApp());

                Log.e(TAG, "placementItem: " + apiSettings.getData());
                ADMOB_BANNER[0] = apiSettings.getData().getPlacement().getAdmob().getBanner();
                ADMOB_INTER[0] = apiSettings.getData().getPlacement().getAdmob().getInterstitial();
                ADMOB_NATIVE[0] = apiSettings.getData().getPlacement().getAdmob().getNativeAdvanced();
                ADMOB_APP_OPEN[0] = apiSettings.getData().getPlacement().getAdmob().getAppOpen();
                ADMOB_REWARD[0] = apiSettings.getData().getPlacement().getAdmob().getRewarded();
                PrefLibAds.getInstance().setString("ADMOB_BANNER", apiSettings.getData().getPlacement().getAdmob().getBanner());
                PrefLibAds.getInstance().setString("ADMOB_INTER", apiSettings.getData().getPlacement().getAdmob().getInterstitial());
                PrefLibAds.getInstance().setString("ADMOB_NATIVE", apiSettings.getData().getPlacement().getAdmob().getNativeAdvanced());
                PrefLibAds.getInstance().setString("ADMOB_REWARD", apiSettings.getData().getPlacement().getAdmob().getRewarded());
                PrefLibAds.getInstance().setString("ADMOB_APP_OPEN", apiSettings.getData().getPlacement().getAdmob().getAppOpen());

                FACEBOOK_BANNER[0] = apiSettings.getData().getPlacement().getFacebook().getBanner();
                FACEBOOK_INTER[0] = apiSettings.getData().getPlacement().getFacebook().getInterstitial();
                FACEBOOK_NATIVE[0] = apiSettings.getData().getPlacement().getFacebook().getNativeAdvanced();
                FACEBOOK_REWARD[0] = apiSettings.getData().getPlacement().getFacebook().getRewarded();

                PrefLibAds.getInstance().setString("FACEBOOK_BANNER", apiSettings.getData().getPlacement().getFacebook().getBanner());
                PrefLibAds.getInstance().setString("FACEBOOK_INTER", apiSettings.getData().getPlacement().getFacebook().getInterstitial());
                PrefLibAds.getInstance().setString("FACEBOOK_NATIVE", apiSettings.getData().getPlacement().getFacebook().getNativeAdvanced());
                PrefLibAds.getInstance().setString("FACEBOOK_REWARD", apiSettings.getData().getPlacement().getFacebook().getRewarded());

                APPLOVIN_BANNER[0] = apiSettings.getData().getPlacement().getApplovin().getBanner();
                APPLOVIN_INTER[0] = apiSettings.getData().getPlacement().getApplovin().getInterstitial();
                APPLOVIN_NATIVE[0] = apiSettings.getData().getPlacement().getApplovin().getNativeAdvanced();
                APPLOVIN_REWARD[0] = apiSettings.getData().getPlacement().getApplovin().getRewarded();

                PrefLibAds.getInstance().setString("APPLOVIN_BANNER", apiSettings.getData().getPlacement().getApplovin().getBanner());
                PrefLibAds.getInstance().setString("APPLOVIN_INTER", apiSettings.getData().getPlacement().getApplovin().getInterstitial());
                PrefLibAds.getInstance().setString("APPLOVIN_NATIVE", apiSettings.getData().getPlacement().getApplovin().getNativeAdvanced());
                PrefLibAds.getInstance().setString("APPLOVIN_REWARD", apiSettings.getData().getPlacement().getApplovin().getRewarded());

                ADMOB_BANNER2[0] = apiSettings.getData().getPlacement().getAdmanager2().getBanner();
                ADMOB_INTER2[0] = apiSettings.getData().getPlacement().getAdmanager2().getInterstitial();
                ADMOB_NATIVE2[0] = apiSettings.getData().getPlacement().getAdmanager2().getNativeAdvanced();
                ADMOB_APP_OPEN2[0] = apiSettings.getData().getPlacement().getAdmanager2().getAppOpen();
                ADMOB_REWARD1[0] = apiSettings.getData().getPlacement().getAdmanager2().getRewarded();
                PrefLibAds.getInstance().setString("AppOpenID2", apiSettings.getData().getPlacement().getAdmanager2().getAppOpen());

                PrefLibAds.getInstance().setString("ADMOB_BANNER2", apiSettings.getData().getPlacement().getAdmanager2().getBanner());
                PrefLibAds.getInstance().setString("ADMOB_INTER2", apiSettings.getData().getPlacement().getAdmanager2().getInterstitial());
                PrefLibAds.getInstance().setString("ADMOB_NATIVE2", apiSettings.getData().getPlacement().getAdmanager2().getNativeAdvanced());
                PrefLibAds.getInstance().setString("ADMOB_REWARD2", apiSettings.getData().getPlacement().getAdmanager2().getRewarded());
                PrefLibAds.getInstance().setString("ADMOB_APP_OPEN2", apiSettings.getData().getPlacement().getAdmanager2().getAppOpen());

                ADMOB_BANNER1[0] = apiSettings.getData().getPlacement().getAdmanager1().getBanner();
                ADMOB_INTER1[0] = apiSettings.getData().getPlacement().getAdmanager1().getInterstitial();
                ADMOB_NATIVE1[0] = apiSettings.getData().getPlacement().getAdmanager1().getNativeAdvanced();
                ADMOB_APP_OPEN1[0] = apiSettings.getData().getPlacement().getAdmanager1().getAppOpen();
                ADMOB_REWARD2[0] = apiSettings.getData().getPlacement().getAdmanager1().getRewarded();
                PrefLibAds.getInstance().setString("AppOpenID1", apiSettings.getData().getPlacement().getAdmanager1().getAppOpen());

                PrefLibAds.getInstance().setString("ADMOB_BANNER1", apiSettings.getData().getPlacement().getAdmanager1().getBanner());
                PrefLibAds.getInstance().setString("ADMOB_INTER1", apiSettings.getData().getPlacement().getAdmanager1().getInterstitial());
                PrefLibAds.getInstance().setString("ADMOB_NATIVE1", apiSettings.getData().getPlacement().getAdmanager1().getNativeAdvanced());
                PrefLibAds.getInstance().setString("ADMOB_REWARD1", apiSettings.getData().getPlacement().getAdmanager1().getRewarded());
                PrefLibAds.getInstance().setString("ADMOB_APP_OPEN1", apiSettings.getData().getPlacement().getAdmanager1().getAppOpen());

                List<GetMoreAppGroups> appInfoList = apiSettings.getData().getGetMoreAppGroups();
                if (!appInfoList.isEmpty()) {
                    PrefLibAds.setMoreAppData(activity, appInfoList);
                }

                List<CustomAds> customAds = apiSettings.getData().getGetCustomAds();
                if (!customAds.isEmpty()) {
                    PrefLibAds.setCustomAdsData(activity, customAds);
                }
                if (apiSettings.getData().getAppExtraData() != null) {
                    multiScreen = apiSettings.getData().getAppExtraData().getMultiscreen();
                    urlStatus = apiSettings.getData().getAppExtraData().getUrl();
                }
            } catch (Exception e) {
                e.printStackTrace();
            }
        } else {
            initAd();
            listener.onSuccess();
        }

        if (apiSettings.getData().isRedirectToOtherApp()) {
            listener.onRedirect(PrefLibAds.getInstance().getString("redirectToOtherAppText"));
        } else {
            initAd();
            listener.onSuccess();
        }
    }

    private void initAd() {
        if (mySharedPref == null) {
            mySharedPref = activity.getSharedPreferences(activity.getPackageName(), Context.MODE_PRIVATE);
        }

        if (appAdShowStatus.equals("true")) {
            MobileAds.initialize(activity, new OnInitializationCompleteListener() {
                @Override
                public void onInitializationComplete(@NonNull InitializationStatus initializationStatus) {
                }
            });

            String appopen_id = getRandomID(ADMOB, "AO");
            loadAdmobAppOpenAd(activity, appopen_id);
        }
    }
}