package com.kidsads.visionkidsads.ads;

import android.app.Activity;
import android.app.Application;
import android.content.Context;
import android.content.SharedPreferences;
import android.net.ConnectivityManager;
import android.net.NetworkInfo;
import android.os.Build;
import android.os.Bundle;
import android.os.CountDownTimer;
import android.util.Log;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.lifecycle.Lifecycle;
import androidx.lifecycle.LifecycleObserver;
import androidx.lifecycle.OnLifecycleEvent;
import androidx.lifecycle.ProcessLifecycleOwner;

import com.kidsads.visionkidsads.utils.PrefLibAds;
import com.google.android.gms.ads.AdError;
import com.google.android.gms.ads.AdRequest;
import com.google.android.gms.ads.FullScreenContentCallback;
import com.google.android.gms.ads.LoadAdError;
import com.google.android.gms.ads.appopen.AppOpenAd;

import java.util.Date;

public class AppOpenManager implements Application.ActivityLifecycleCallbacks, LifecycleObserver {
    private static final String LOG_TAG = "AppOpenManager";
    public static boolean isScreenSplash = true;
    private static String AD_UNIT_ID;
    private static boolean isShowingAd = false;
    private final Activity myApplication;
    private AppOpenAd appOpenAd = null;
    private AppOpenAd.AppOpenAdLoadCallback loadCallback;
    private long loadTime = 2;
    private Activity currentActivity;
    private SharedPreferences mysharedpreferences;

    public AppOpenManager(Activity myApplication) {
        this.myApplication = myApplication;
        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.Q) {
            this.myApplication.registerActivityLifecycleCallbacks(this);
        }
        AD_UNIT_ID = PrefLibAds.getInstance().getString("ADMOB_APP_OPEN");
        ProcessLifecycleOwner.get().getLifecycle().addObserver(this);
    }

    public AppOpenManager(Activity myApplication, String appopen_id) {
        this.myApplication = myApplication;
        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.Q) {
            this.myApplication.registerActivityLifecycleCallbacks(this);
        }
        AD_UNIT_ID = appopen_id;
        ProcessLifecycleOwner.get().getLifecycle().addObserver(this);
    }

    public void showAdIfAvailable(final splshADlistner listner) {
        if (!isShowingAd && isAdAvailable()) {
            Log.e(LOG_TAG, "Will show ad.");

            FullScreenContentCallback fullScreenContentCallback =
                    new FullScreenContentCallback() {
                        @Override
                        public void onAdDismissedFullScreenContent() {
                            AppOpenManager.this.appOpenAd = null;
                            isShowingAd = false;
                            listner.onSuccess();
                        }

                        @Override
                        public void onAdFailedToShowFullScreenContent(AdError adError) {
                            listner.onError(adError.getMessage());
                        }

                        @Override
                        public void onAdShowedFullScreenContent() {
                            isShowingAd = true;

                        }
                    };

            appOpenAd.setFullScreenContentCallback(fullScreenContentCallback);
            appOpenAd.show(currentActivity);


        } else {
            listner.onError("");
        }
    }

    /**
     * Request an ad
     */

    public void fetchAdSplash(final splshADlistner listner) {
        if (isAdAvailable()) {
            return;
        }

        loadCallback = new AppOpenAd.AppOpenAdLoadCallback() {
            @Override
            public void onAdLoaded(@NonNull AppOpenAd appOpenAd) {
                super.onAdLoaded(appOpenAd);
                AppOpenManager.this.appOpenAd = appOpenAd;
                AppOpenManager.this.loadTime = (new Date()).getTime();
                Log.e("AppOpenManager", "onAppOpenAdToLoad: Splash");
                isScreenSplash = false;
                listner.onSuccess();

            }

            @Override
            public void onAdFailedToLoad(@NonNull LoadAdError loadAdError) {
                super.onAdFailedToLoad(loadAdError);
                isScreenSplash = false;
                Log.e("AppOpenManager", "onAppOpenAdFailedToLoad: Splash" + loadAdError.getMessage());
                listner.onError(loadAdError.getMessage());
            }
        };

        AdRequest request = getAdRequest();
        AppOpenAd.load(
                myApplication, AD_UNIT_ID, request,
                AppOpenAd.APP_OPEN_AD_ORIENTATION_PORTRAIT, loadCallback);
    }

    public void fetchAd(final splshADlistner listner) {
        if (isAdAvailable()) {
            return;
        }

        loadCallback = new AppOpenAd.AppOpenAdLoadCallback() {
            @Override
            public void onAdLoaded(@NonNull AppOpenAd appOpenAd) {
                super.onAdLoaded(appOpenAd);
                AppOpenManager.this.appOpenAd = appOpenAd;
                AppOpenManager.this.loadTime = (new Date()).getTime();
                Log.e("AppOpenManager", "onAppOpenAdToLoad: ");
                listner.onSuccess();
                isScreenSplash = false;
            }

            @Override
            public void onAdFailedToLoad(@NonNull LoadAdError loadAdError) {
                super.onAdFailedToLoad(loadAdError);
                Log.e("AppOpenManager", "onAppOpenAdFailedToLoad: " + loadAdError.getMessage());
                listner.onError(loadAdError.getMessage());
                isScreenSplash = false;
            }
        };

        AdRequest request = getAdRequest();
        AppOpenAd.load(
                myApplication, AD_UNIT_ID, request,
                AppOpenAd.APP_OPEN_AD_ORIENTATION_PORTRAIT, loadCallback);
    }

    /**
     * Creates and returns ad request.
     */
    private AdRequest getAdRequest() {
        return new AdRequest.Builder().build();
    }

    private boolean wasLoadTimeLessThanNHoursAgo(long numHours) {
        long dateDifference = (new Date()).getTime() - this.loadTime;
        long numMilliSecondsPerHour = 3600000;
        return (dateDifference < (numMilliSecondsPerHour * numHours));
    }

    public boolean isAdAvailable() {
        return appOpenAd != null && wasLoadTimeLessThanNHoursAgo(4);
    }

    @Override
    public void onActivityPreCreated(@NonNull Activity activity, @Nullable Bundle savedInstanceState) {

    }

    @Override
    public void onActivityCreated(Activity activity, Bundle savedInstanceState) {
    }

    @Override
    public void onActivityPostCreated(@NonNull Activity activity, @Nullable Bundle savedInstanceState) {

    }

    @Override
    public void onActivityPreStarted(@NonNull Activity activity) {

    }

    @Override
    public void onActivityStarted(Activity activity) {
        currentActivity = activity;
    }

    @Override
    public void onActivityPostStarted(@NonNull Activity activity) {

    }

    @Override
    public void onActivityPreResumed(@NonNull Activity activity) {

    }

    @Override
    public void onActivityResumed(Activity activity) {
        currentActivity = activity;
    }

    @Override
    public void onActivityPostResumed(@NonNull Activity activity) {

    }

    @Override
    public void onActivityPrePaused(@NonNull Activity activity) {

    }

    @Override
    public void onActivityStopped(Activity activity) {
    }

    @Override
    public void onActivityPostStopped(@NonNull Activity activity) {

    }

    @Override
    public void onActivityPreSaveInstanceState(@NonNull Activity activity, @NonNull Bundle outState) {

    }

    @Override
    public void onActivityPaused(Activity activity) {
    }

    @Override
    public void onActivityPostPaused(@NonNull Activity activity) {

    }

    @Override
    public void onActivityPreStopped(@NonNull Activity activity) {

    }

    @Override
    public void onActivitySaveInstanceState(Activity activity, Bundle bundle) {
    }

    @Override
    public void onActivityPostSaveInstanceState(@NonNull Activity activity, @NonNull Bundle outState) {

    }

    @Override
    public void onActivityPreDestroyed(@NonNull Activity activity) {

    }

    @Override
    public void onActivityDestroyed(Activity activity) {
        currentActivity = null;
    }

    @Override
    public void onActivityPostDestroyed(@NonNull Activity activity) {

    }

    @OnLifecycleEvent(Lifecycle.Event.ON_START)
    public void onStart() {
        Log.e(LOG_TAG, "onStart");

        if (!isScreenSplash) {
            new CountDownTimer(1000, 1000) {

                public void onTick(long millisUntilFinished) {
//                mTextField.setText("seconds remaining: " + millisUntilFinished / 1000);
                    // logic to set the EditText could go here
                }

                public void onFinish() {
                    if (!AD_UNIT_ID.isEmpty() && isNetworkAvailable()) {
                        showAdIfAvailable();
                    }
                }

            }.start();
        }


    }

    private boolean isNetworkAvailable() {
        ConnectivityManager manager =
                (ConnectivityManager) myApplication.getSystemService(Context.CONNECTIVITY_SERVICE);
        NetworkInfo networkInfo = manager.getActiveNetworkInfo();
        boolean isAvailable = networkInfo != null && networkInfo.isConnected();
        // Network is present and connected
        return isAvailable;
    }

    public void showAdIfAvailable() {

        if (isAdAvailable()) {
            if (!isShowingAd) {
                Log.e(LOG_TAG, "Will show ad.");
                isShowingAd = true;
                FullScreenContentCallback fullScreenContentCallback =
                        new FullScreenContentCallback() {
                            @Override
                            public void onAdDismissedFullScreenContent() {

                                AppOpenManager.this.appOpenAd = null;
                                isShowingAd = false;
                                fetchAd();

                            }

                            @Override
                            public void onAdFailedToShowFullScreenContent(AdError adError) {
                            }

                            @Override
                            public void onAdShowedFullScreenContent() {
                                isShowingAd = true;

                            }
                        };

                appOpenAd.setFullScreenContentCallback(fullScreenContentCallback);
                appOpenAd.show(currentActivity);


            }
        } else {
            fetchAd();
        }

    }

    public void fetchAd() {
        if (isAdAvailable()) {
            return;
        }

        loadCallback = new AppOpenAd.AppOpenAdLoadCallback() {
            @Override
            public void onAdLoaded(@NonNull AppOpenAd appOpenAd) {
                super.onAdLoaded(appOpenAd);
                AppOpenManager.this.appOpenAd = appOpenAd;
                AppOpenManager.this.loadTime = (new Date()).getTime();
                Log.e("AppOpenManager", "onAppOpenAdToLoad: ");
            }

            @Override
            public void onAdFailedToLoad(@NonNull LoadAdError loadAdError) {
                super.onAdFailedToLoad(loadAdError);
                Log.e("AppOpenManager", "onAppOpenAdFailedToLoad: " + loadAdError.getMessage());
            }
        };

        AdRequest request = getAdRequest();
        AppOpenAd.load(
                myApplication, AD_UNIT_ID, request,
                AppOpenAd.APP_OPEN_AD_ORIENTATION_PORTRAIT, loadCallback);
    }

    public interface splshADlistner {

        void onSuccess();

        void onError(String error);
    }


}