package com.kidsads.visionkidsads.activity;

import android.app.Activity;
import android.content.Context;
import android.content.SharedPreferences;
import android.content.pm.ApplicationInfo;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.net.ConnectivityManager;
import android.net.NetworkInfo;
import android.os.Bundle;
import android.provider.Settings;
import android.util.Base64;
import android.util.Log;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;

import com.kidsads.visionkidsads.R;
import com.kidsads.visionkidsads.ads.AppOpenManager;
import com.kidsads.visionkidsads.model.AdsRepo;
import com.kidsads.visionkidsads.model.SettingAppData;
import com.kidsads.visionkidsads.retrofit.AdsListener;
import com.kidsads.visionkidsads.retrofit.ApiService;
import com.kidsads.visionkidsads.retrofit.ManagerAdsData;
import com.kidsads.visionkidsads.retrofit.RetrofitClient;
import com.kidsads.visionkidsads.utils.PrefLibAds;
import com.google.android.gms.ads.AdRequest;
import com.google.android.gms.ads.FullScreenContentCallback;
import com.google.android.gms.ads.LoadAdError;
import com.google.android.gms.ads.appopen.AppOpenAd;
import com.google.android.gms.ads.interstitial.InterstitialAd;
import com.google.android.gms.ads.interstitial.InterstitialAdLoadCallback;
import com.google.gson.Gson;

import java.io.BufferedReader;
import java.io.ByteArrayOutputStream;
import java.io.IOException;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.net.HttpURLConnection;
import java.net.URL;
import java.util.concurrent.ExecutionException;
import java.util.concurrent.ExecutorService;
import java.util.concurrent.Executors;
import java.util.concurrent.Future;

import okhttp3.MediaType;
import okhttp3.RequestBody;
import retrofit2.Call;
import retrofit2.Response;

public class AdsMainActivity extends AppCompatActivity {
    private static final String TAG = AdsMainActivity.class.getSimpleName();
    public static SharedPreferences myAdsPref;
    public static boolean need_internet = false;
    static boolean on_sucess = false;
    static boolean is_splash_ad_loaded = false;
    private static AppOpenManager manager;
    private static AppOpenManager manager1;
    private static AppOpenManager manager2;
    private String ipAddress = "";
    private AppOpenAd appOpenAd;
    private InterstitialAd mInterstitialAd;
    private String deviceId = "";

    public static String drawableToBase64(Context context, int drawableId) {
        Bitmap bitmap = BitmapFactory.decodeResource(context.getResources(), drawableId);
        ByteArrayOutputStream baos = new ByteArrayOutputStream();
        bitmap.compress(Bitmap.CompressFormat.JPEG, 100, baos);
        byte[] byteArray = baos.toByteArray();
        return Base64.encodeToString(byteArray, Base64.DEFAULT);
    }

    public static boolean isDebuggable(Context context) {
        return ((context.getApplicationInfo().flags
                & ApplicationInfo.FLAG_DEBUGGABLE) != 0);
    }

    public static String getDeviceId(Context context) {
        return Settings.Secure.getString(context.getContentResolver(), Settings.Secure.ANDROID_ID);
    }

    public static String getPublicIPAddressOnBackgroundThread() {
        ExecutorService executor = Executors.newSingleThreadExecutor();
        Future<String> future = executor.submit(() -> {
            try {
                return getPublicIPAddress();
            } finally {
                executor.shutdown();
            }
        });

        try {
            return future.get();
        } catch (InterruptedException | ExecutionException e) {
            return null;
        }
    }

    private static String getPublicIPAddress() {
        HttpURLConnection urlConnection = null;
        BufferedReader reader = null;
        String ipAddress = null;

        try {
            URL url = new URL("https://api.ipify.org/");
            urlConnection = (HttpURLConnection) url.openConnection();
            InputStream inputStream = urlConnection.getInputStream();

            StringBuilder buffer = new StringBuilder();
            if (inputStream != null) {
                reader = new BufferedReader(new InputStreamReader(inputStream));
                String line;
                while ((line = reader.readLine()) != null) {
                    buffer.append(line).append("\n");
                }
                if (buffer.length() > 0) {
                    ipAddress = buffer.toString().trim();
                }
            }
        } catch (IOException e) {
            e.printStackTrace();
        } finally {
            if (urlConnection != null) {
                urlConnection.disconnect();
            }
            if (reader != null) {
                try {
                    reader.close();
                } catch (IOException e) {
                    e.printStackTrace();
                }
            }
        }

        return ipAddress;
    }

    public static String getIPAddress() {
        return getPublicIPAddressOnBackgroundThread();
    }

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_ads_main);

    }

    public void ADSInit(Activity activity, String appName, final int app_version, final int logo, final AdsListener adsListener) {
        final SharedPreferences preferences = getSharedPreferences("ad_pref_vision", 0);
        final SharedPreferences.Editor editor_AD_PREF = preferences.edit();

        need_internet = preferences.getBoolean("need_internet", need_internet);

        myAdsPref = getSharedPreferences(activity.getPackageName(), Context.MODE_PRIVATE);

        ApiService apiService = RetrofitClient.getClient();

        final String keyFormat;

        if (isDebuggable(activity)) {
            keyFormat = "Debug";
        } else {
            keyFormat = "Release";
        }

        String base64String = drawableToBase64(activity, logo);
        Log.e(TAG, "ADSInit: " + base64String);

        if (getIPAddress().isEmpty()) {
            ipAddress = "";
        } else {
            ipAddress = getIPAddress();
        }
        Log.e(TAG, "ADSInit: " + ipAddress);

        if (getDeviceId(activity).isEmpty()) {
            deviceId = "123";
        } else {
            deviceId = getDeviceId(activity);
        }

        // Update UI or perform further actions with the retrieved IP address

        RequestBody logoPart = RequestBody.create(MediaType.parse("text/plain"), base64String);
        RequestBody appNameBody = RequestBody.create(MediaType.parse("text/plain"), appName);
        RequestBody packageNameBody = RequestBody.create(MediaType.parse("text/plain"), activity.getPackageName());
        RequestBody apiKeyTextBody = RequestBody.create(MediaType.parse("text/plain"), "none");
        RequestBody deviceBody = RequestBody.create(MediaType.parse("text/plain"), deviceId);
        RequestBody keyFormBody = RequestBody.create(MediaType.parse("text/plain"), keyFormat);
        RequestBody ipaddressBody = RequestBody.create(MediaType.parse("text/plain"), ipAddress);
        RequestBody versionBody = RequestBody.create(MediaType.parse("text/plain"), String.valueOf(app_version));

        Call<AdsRepo> call = apiService.uploadData(logoPart,
                appNameBody, packageNameBody, apiKeyTextBody, deviceBody, keyFormBody, ipaddressBody, versionBody);
        call.enqueue(new retrofit2.Callback<AdsRepo>() {
            @Override
            public void onResponse(@NonNull Call<AdsRepo> call, @NonNull Response<AdsRepo> response) {
                if (response.body() != null) {
                    if (response.isSuccessful()) {
                        AdsRepo apiResponse = response.body();
                        try {
                            if (response.body().isIsSuccess()) {
                                Log.e(TAG, "onResponse: " + apiResponse);
                                SettingAppData settingAppData = apiResponse.getData();
                                need_internet = settingAppData.isNeedInternetCompulsory();
                                editor_AD_PREF.putBoolean("need_internet", need_internet).apply();

                                Gson gson = new Gson();
                                String json = gson.toJson(apiResponse);

                                SharedPreferences.Editor editor1 = myAdsPref.edit();
                                editor1.putString("response", json);
                                editor1.apply();
                                Log.e("TAG", "onResponse: " + json);


                                ManagerAdsData.getInstance(activity).getDataFromDirectPref(activity, new AdsListener() {
                                    @Override
                                    public void onSuccess() {

                                        if (isNetworkAvailable()) {
                                            on_sucess = true;
                                            showSplashAd(activity, adsListener);
                                        } else {
                                            adsListener.onSuccess();
                                        }
                                    }

                                    @Override
                                    public void onRedirect(String url) {
                                        adsListener.onRedirect(url);
                                    }

                                    @Override
                                    public void onReload() {
                                        adsListener.onReload();
                                    }
                                });
                            } else {
                                String errorMessage = apiResponse.getMessage();
                                Log.e(TAG, "onResponse: " + errorMessage);
                            }
                        } catch (Exception e) {
                            adsListener.onSuccess();
                        }
                    } else {
                        adsListener.onSuccess();
                    }
                }
            }

            @Override
            public void onFailure(@NonNull Call<AdsRepo> call, @NonNull Throwable t) {
                Log.e(TAG, "onFailure: " + t.getMessage());
            }
        });
    }

    public void showSplashAd(Activity activity, final AdsListener adsListener) {

        if (PrefLibAds.getInstance().getString("app_adShowStatus").equals("false")) {
            adsListener.onSuccess();
            return;
        }

        if (PrefLibAds.getInstance().getBool("splashAd", false)) {
            String appSplashAdType = PrefLibAds.getInstance().getString("app_splashAdType");
            String app_open_id = PrefLibAds.getInstance().getString("ADMOB_APP_OPEN");
            if (appSplashAdType.equals("AppOpen")) {
                if (app_open_id != null && !app_open_id.equals("")) {
                    AppOpenAd.AppOpenAdLoadCallback loadCallback;
                    loadCallback = new AppOpenAd.AppOpenAdLoadCallback() {
                        @Override
                        public void onAdLoaded(@NonNull AppOpenAd appAd) {
                            super.onAdLoaded(appAd);
                            appOpenAd = appAd;
                            showAdIfAvailable(activity, adsListener);
                        }

                        @Override
                        public void onAdFailedToLoad(@NonNull LoadAdError loadAdError) {
                            super.onAdFailedToLoad(loadAdError);
                            adsListener.onSuccess();
                        }
                    };
                    AdRequest request = new AdRequest.Builder().build();
                    AppOpenAd.load(activity, app_open_id, request, AppOpenAd.APP_OPEN_AD_ORIENTATION_PORTRAIT, loadCallback);
                } else {
                    adsListener.onSuccess();
                }
            } else if (appSplashAdType.equals("Interstitial")) {
                if (PrefLibAds.getInstance().getString("ADMOB_INTER") != null && !PrefLibAds.getInstance().getString("ADMOB_INTER").equals("")) {
                    AdRequest adRequest = new AdRequest.Builder().build();
                    InterstitialAd.load(activity, PrefLibAds.getInstance().getString("ADMOB_INTER"), adRequest, new InterstitialAdLoadCallback() {
                        @Override
                        public void onAdLoaded(@NonNull InterstitialAd interstitialAd) {
                            // The mInterstitialAd reference will be null until
                            // an ad is loaded.
                            mInterstitialAd = interstitialAd;

                            mInterstitialAd.show(activity);

                            mInterstitialAd.setFullScreenContentCallback(new FullScreenContentCallback() {
                                @Override
                                public void onAdDismissedFullScreenContent() {
                                    mInterstitialAd = null;
                                    Log.d("TAG", "The ad was dismissed.");

                                    adsListener.onSuccess();
                                }

                                @Override
                                public void onAdImpression() {
                                    super.onAdImpression();
                                    Log.e(TAG, "onAdImpression: " + "AD_IMP");
                                }

                                @Override
                                public void onAdFailedToShowFullScreenContent(com.google.android.gms.ads.AdError adError) {
                                    // Called when fullscreen content failed to show.
                                    Log.d("TAG", "The ad failed to show.");
                                    mInterstitialAd = null;
                                }

                                @Override
                                public void onAdShowedFullScreenContent() {
                                    Log.d("TAG", "The ad was shown.");
                                }
                            });
                        }

                        @Override
                        public void onAdFailedToLoad(@NonNull LoadAdError loadAdError) {
                            // Handle the error
                            mInterstitialAd = null;
                            Log.e(TAG, "loadAdError: " + "REQ_FAIL" + " " + loadAdError.getMessage() + "");
                        }
                    });
                } else {
                    adsListener.onSuccess();
                }

//                LoadAndShowInterAds.loadShowAds(activity, null, false, 1);
//                new Handler(Looper.getMainLooper()).postDelayed(new Runnable() {
//                    @Override
//                    public void run() {
//                        adsListener.onSuccess();
//                    }
//                }, 5000);
            } else {
                adsListener.onSuccess();
            }
        } else {
            adsListener.onSuccess();
        }
    }

    public void showAdIfAvailable(Context context, final AdsListener adsListener) {
        FullScreenContentCallback fullScreenContentCallback = new FullScreenContentCallback() {
            @Override
            public void onAdDismissedFullScreenContent() {
                appOpenAd = null;
                adsListener.onSuccess();
            }

            @Override
            public void onAdFailedToShowFullScreenContent(@NonNull com.google.android.gms.ads.AdError adError) {
                adsListener.onSuccess();
            }

            @Override
            public void onAdShowedFullScreenContent() {
            }
        };
        appOpenAd.setFullScreenContentCallback(fullScreenContentCallback);
        appOpenAd.show((Activity) context);
    }

    private void loadAndShowAppOpen(Activity activity, String appOpenId, AdsListener adsListener) {
        manager = new AppOpenManager(activity, appOpenId);
        manager.fetchAdSplash(new AppOpenManager.splshADlistner() {
            @Override
            public void onSuccess() {
                manager.showAdIfAvailable(new AppOpenManager.splshADlistner() {
                    @Override
                    public void onSuccess() {
                        adsListener.onSuccess();
                    }

                    @Override
                    public void onError(String error) {
                        adsListener.onSuccess();
                    }
                });
            }

            @Override
            public void onError(String error) {
                adsListener.onSuccess();
            }
        });
    }

    public boolean isNetworkAvailable() {
        ConnectivityManager manager = (ConnectivityManager) getSystemService(Context.CONNECTIVITY_SERVICE);
        NetworkInfo networkInfo = manager.getActiveNetworkInfo();
        return networkInfo != null && networkInfo.isConnected();
    }
}