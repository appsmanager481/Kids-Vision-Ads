package com.kidsads.visionkidsads.ads;

import static com.kidsads.visionkidsads.retrofit.ManagerAdsData.ADMOB;
import static com.kidsads.visionkidsads.retrofit.ManagerAdsData.ADMOB1;
import static com.kidsads.visionkidsads.retrofit.ManagerAdsData.ADMOB2;

import android.annotation.SuppressLint;
import android.app.Activity;
import android.content.Context;
import android.content.Intent;
import android.net.Uri;
import android.util.AttributeSet;
import android.util.DisplayMetrics;
import android.util.Log;
import android.view.Display;
import android.view.Gravity;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.FrameLayout;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.RelativeLayout;
import android.widget.Toast;

import androidx.annotation.NonNull;

import com.kidsads.visionkidsads.R;
import com.kidsads.visionkidsads.model.CustomAds;
import com.kidsads.visionkidsads.utils.Connectivity;
import com.kidsads.visionkidsads.utils.PrefLibAds;
import com.bumptech.glide.Glide;
import com.facebook.shimmer.ShimmerFrameLayout;
import com.google.android.gms.ads.AdListener;
import com.google.android.gms.ads.AdRequest;
import com.google.android.gms.ads.AdSize;
import com.google.android.gms.ads.AdView;
import com.google.android.gms.ads.LoadAdError;

import java.util.ArrayList;
import java.util.Collections;
import java.util.List;

public class BannerAdsView extends LinearLayout {

    private static final String TAG = BannerAdsView.class.getSimpleName();
    public static int count_banner = -1;
    public String state_admobBanner = "Start";
    public String state_fbBanner = "Start";
    public String state_admobBanner1 = "Start";
    public String state_admobBanner2 = "Start";
    public String state_applovinBanner = "Start";
    ArrayList<String> banner_sequence = new ArrayList<>();
    AdView admobBannerAd_preLoad = null;
    AdView admobBannerAd_preLoad1 = null;
    AdView admobBannerAd_preLoad2 = null;
    private ShimmerFrameLayout shimmer_view_container;
    private RelativeLayout bannerlout;
    private FrameLayout banner_ad_container;

    public BannerAdsView(Context context) {
        this(context, null);
    }

    public BannerAdsView(Context context, AttributeSet attrs) {
        this(context, attrs, 0);
    }

    @SuppressLint("CustomViewStyleable")
    public BannerAdsView(Context context, AttributeSet attrs, int defStyle) {
        super(context, attrs, defStyle);

        boolean isFastConnection = Connectivity.isConnectedFast(context);

        if (!isFastConnection) {
            return;
        }

        if (PrefLibAds.getInstance().getString("app_adShowStatus").equals("false")) {
            return;
        }

        count_banner++;

        View.inflate(context, R.layout.main_banner_layout, this);

        try {
            banner_ad_container = findViewById(R.id.banner_ad_container);
            bannerlout = findViewById(R.id.bannerlout);
            shimmer_view_container = findViewById(R.id.shimmer_view_container);
            banner_ad_container.setVisibility(View.VISIBLE);
            bannerlout.setVisibility(View.VISIBLE);
        } catch (Exception e) {
            Log.e("----error", e.getMessage() + "");
            Toast.makeText(context, e.getMessage() + "", Toast.LENGTH_SHORT).show();
        }


        String adApply = PrefLibAds.getInstance().getString("app_adApply");

        String all_howShowAd = PrefLibAds.getInstance().getString("app_howShowAd");
        String all_adPlatformSequence = PrefLibAds.getInstance().getString("app_adPlatformSequence");

        String format_howShowAdBanner = PrefLibAds.getInstance().getString("app_howShowAdBanner");
        String format_adPlatformSequenceBanner = PrefLibAds.getInstance().getString("app_adPlatformSequenceBanner").toLowerCase();

        banner_sequence = new ArrayList<String>();
        if (adApply.equals("allAdFormat")) {
            if (all_howShowAd.equals("fixSequence") && !all_adPlatformSequence.isEmpty()) {
                String[] adSequence = all_adPlatformSequence.split(", ");
                Collections.addAll(banner_sequence, adSequence);
            } else if (all_howShowAd.equals("alternateAdShow") && !all_adPlatformSequence.isEmpty()) {
                String[] alernateAd = all_adPlatformSequence.split(", ");

                int index = 0;
                for (int j = 0; j <= 10; j++) {
                    if (count_banner % alernateAd.length == j) {
                        index = j;
                        banner_sequence.add(alernateAd[index]);
                    }
                }

                String[] adSequence = all_adPlatformSequence.split(", ");
                for (String s : adSequence) {
                    if (banner_sequence.size() != 0) {
                        if (!banner_sequence.get(0).equals(s)) {
                            banner_sequence.add(s);
                        }
                    }
                }
            } else {
                showCustomBannerAds((Activity) context, banner_ad_container, shimmer_view_container);
            }
        } else {
            if (format_howShowAdBanner.equals("fixSequence") && !format_adPlatformSequenceBanner.isEmpty()) {
                String[] adSequence = format_adPlatformSequenceBanner.split(", ");
                Collections.addAll(banner_sequence, adSequence);

            } else if (format_howShowAdBanner.equals("alternateAdShow") && !format_adPlatformSequenceBanner.isEmpty()) {
                String[] alernateAd = format_adPlatformSequenceBanner.split(", ");

                int index = 0;
                for (int j = 0; j <= 10; j++) {
                    if (count_banner % alernateAd.length == j) {
                        index = j;
                        banner_sequence.add(alernateAd[index]);
                    }
                }

                String[] adSequence = format_adPlatformSequenceBanner.split(", ");
                for (String s : adSequence) {
                    if (banner_sequence.size() != 0) {
                        if (!banner_sequence.get(0).equals(s)) {
                            banner_sequence.add(s);
                        }
                    }
                }
            } else {
                showCustomBannerAds((Activity) context, banner_ad_container, shimmer_view_container);
            }
        }

        if (banner_sequence.size() != 0) {
            displayBanner(banner_sequence.get(0), (Activity) context, banner_ad_container, shimmer_view_container);
        } else {
            showCustomBannerAds((Activity) context, banner_ad_container, shimmer_view_container);
        }
    }

    public void displayBanner(String platform, Activity activity, FrameLayout bannerad, ShimmerFrameLayout shimmer) {
        if (platform.equals(ADMOB)) {
            showAdmobBanner(activity, bannerad, shimmer);
        } else if (platform.equals(ADMOB1)) {
            showAdmobBanner1(activity, bannerad, shimmer);
        } else if (platform.equals(ADMOB2)) {
            showAdmobBanner2(activity, bannerad, shimmer);
        } else {
            showCustomBannerAds(activity, bannerad, shimmer);
        }
    }

    public void showCustomBannerAds(Activity activity, FrameLayout frameLayout, ShimmerFrameLayout shimmer) {
        frameLayout.setVisibility(View.VISIBLE);
        shimmer.setVisibility(View.GONE);
        shimmer.stopShimmer();
        LayoutInflater inflater = LayoutInflater.from(activity);
        View customView = inflater.inflate(R.layout.custom_banner_ads, frameLayout, false);
        frameLayout.addView(customView);

        ImageView bannerImg = customView.findViewById(R.id.bannerImg);

        List<CustomAds> customAds = PrefLibAds.getCustomAdsData(activity);
        if (customAds != null && !customAds.isEmpty()) {
            for (int i = 0; i < customAds.size(); i++) {
                if (customAds.get(i).getFormatName().equals("banner")) {
                    Glide.with(activity).load(customAds.get(i).getAssetsBanner()).into(bannerImg);

                    int finalI = i;
                    bannerImg.setOnClickListener(new OnClickListener() {
                        @Override
                        public void onClick(View v) {
                            Intent httpIntent = new Intent(Intent.ACTION_VIEW);
                            httpIntent.setData(Uri.parse(customAds.get(finalI).getAssetsUrl()));
                            activity.startActivity(httpIntent);
                        }
                    });
                }
            }
        } else {
            nextBannerPlatform(activity, frameLayout, shimmer);
        }
    }

    private void showAdmobBanner(Activity activity, FrameLayout greedyxbannerad, ShimmerFrameLayout shimmer) {
        if (PrefLibAds.getInstance().getString("ADMOB_BANNER").isEmpty() || PrefLibAds.getInstance().getString("app_adShowStatus").equals("false")) {
            nextBannerPlatform(activity, greedyxbannerad, shimmer);
            return;
        }

        if (admobBannerAd_preLoad == null) {
            AdView adView = new AdView(activity);
            adView.setAdUnitId(PrefLibAds.getInstance().getString("ADMOB_BANNER"));
            adView.setAdListener(new AdListener() {
                @Override
                public void onAdFailedToLoad(@NonNull LoadAdError loadAdError) {
                    super.onAdFailedToLoad(loadAdError);
                    greedyxbannerad.removeAllViews();
                    nextBannerPlatform(activity, greedyxbannerad, shimmer);
                    Log.e(TAG, ADMOB2 + "REQ_FAIL");
                }

                @Override
                public void onAdLoaded() {
                    greedyxbannerad.removeAllViews();
                    greedyxbannerad.addView(adView);
                    shimmer.setVisibility(View.GONE);
                    shimmer.stopShimmer();
                    preLoadBanner(ADMOB, activity, greedyxbannerad, shimmer);
                    Log.e(TAG, ADMOB + "REQ_LOAD");
                    Log.e(TAG, ADMOB + "AD_SHOW");

                }
            });
            FrameLayout.LayoutParams params = new FrameLayout.LayoutParams(ViewGroup.LayoutParams.WRAP_CONTENT, ViewGroup.LayoutParams.WRAP_CONTENT);
            params.gravity = Gravity.CENTER;
            greedyxbannerad.addView(adView, params);
            AdSize adSize = getAdSize(activity, greedyxbannerad);
            adView.setAdSize(AdSize.BANNER);
            AdRequest build = new AdRequest.Builder().build();
            adView.loadAd(build);
        } else {
            greedyxbannerad.removeAllViews();
            greedyxbannerad.addView(admobBannerAd_preLoad);
            state_admobBanner = "Start";
            preLoadBanner(ADMOB, activity, greedyxbannerad, shimmer);
        }
    }

    private void showAdmobBanner1(Activity activity, FrameLayout greedyxbannerad, ShimmerFrameLayout shimmer) {
        if (PrefLibAds.getInstance().getString("ADMOB_BANNER1").isEmpty() || PrefLibAds.getInstance().getString("app_adShowStatus").equals("false")) {
            nextBannerPlatform(activity, greedyxbannerad, shimmer);
            return;
        }

        if (admobBannerAd_preLoad1 == null) {
            AdView adView = new AdView(activity);
            adView.setAdUnitId(PrefLibAds.getInstance().getString("ADMOB_BANNER1"));
            adView.setAdListener(new AdListener() {
                @Override
                public void onAdFailedToLoad(@NonNull LoadAdError loadAdError) {
                    super.onAdFailedToLoad(loadAdError);
                    greedyxbannerad.removeAllViews();
                    nextBannerPlatform(activity, greedyxbannerad, shimmer);
                    Log.e(TAG, ADMOB1 + "REQ_FAIL");
                }

                @Override
                public void onAdLoaded() {
                    greedyxbannerad.removeAllViews();
                    greedyxbannerad.addView(adView);
                    shimmer.setVisibility(View.GONE);
                    shimmer.stopShimmer();
                    preLoadBanner(ADMOB1, activity, greedyxbannerad, shimmer);
                    Log.e(TAG, ADMOB1 + "REQ_LOAD");
                    Log.e(TAG, ADMOB1 + "AD_SHOW");

                }
            });
            FrameLayout.LayoutParams params = new FrameLayout.LayoutParams(ViewGroup.LayoutParams.WRAP_CONTENT, ViewGroup.LayoutParams.WRAP_CONTENT);
            params.gravity = Gravity.CENTER;
            greedyxbannerad.addView(adView, params);
            AdSize adSize = getAdSize(activity, greedyxbannerad);
            adView.setAdSize(AdSize.BANNER);
            AdRequest build = new AdRequest.Builder().build();
            adView.loadAd(build);
        } else {
            greedyxbannerad.removeAllViews();
            greedyxbannerad.addView(admobBannerAd_preLoad1);
            state_admobBanner1 = "Start";
            preLoadBanner(ADMOB1, activity, greedyxbannerad, shimmer);
        }
    }

    private void showAdmobBanner2(Activity activity, FrameLayout greedyxbannerad, ShimmerFrameLayout shimmer) {
        if (PrefLibAds.getInstance().getString("ADMOB_BANNER2").isEmpty() || PrefLibAds.getInstance().getString("app_adShowStatus").equals("false")) {
            nextBannerPlatform(activity, greedyxbannerad, shimmer);
            return;
        }

        if (admobBannerAd_preLoad2 == null) {
            AdView adView = new AdView(activity);
            adView.setAdUnitId(PrefLibAds.getInstance().getString("ADMOB_BANNER2"));
            adView.setAdListener(new AdListener() {
                @Override
                public void onAdFailedToLoad(@NonNull LoadAdError loadAdError) {
                    super.onAdFailedToLoad(loadAdError);
                    greedyxbannerad.removeAllViews();
                    nextBannerPlatform(activity, greedyxbannerad, shimmer);
                    Log.e(TAG, ADMOB2 + "REQ_FAIL");
                }

                @Override
                public void onAdLoaded() {
                    greedyxbannerad.removeAllViews();
                    greedyxbannerad.addView(adView);
                    preLoadBanner(ADMOB2, activity, greedyxbannerad, shimmer);
                    Log.e(TAG, ADMOB2 + "REQ_LOAD");
                    Log.e(TAG, ADMOB2 + "AD_SHOW");
                    shimmer.setVisibility(View.GONE);
                    shimmer.stopShimmer();
                }
            });
            FrameLayout.LayoutParams params = new FrameLayout.LayoutParams(ViewGroup.LayoutParams.WRAP_CONTENT, ViewGroup.LayoutParams.WRAP_CONTENT);
            params.gravity = Gravity.CENTER;
            greedyxbannerad.addView(adView, params);
            AdSize adSize = getAdSize(activity, greedyxbannerad);
            adView.setAdSize(adSize);
            AdRequest build = new AdRequest.Builder().build();
            adView.loadAd(build);
        } else {
            greedyxbannerad.removeAllViews();
            greedyxbannerad.addView(admobBannerAd_preLoad2);

            state_admobBanner2 = "Start";
            preLoadBanner(ADMOB2, activity, greedyxbannerad, shimmer);
        }
    }

    private void preLoadBanner(String platform, Activity activity, FrameLayout greedyxbannerad, ShimmerFrameLayout shimmer) {

        if (platform.equals(ADMOB)) {
            if (admobBannerAd_preLoad == null && state_admobBanner.equals("Loaded")) {
                state_admobBanner = "Start";
            }
            admobBannerAd_preLoad = null;
            if (PrefLibAds.getInstance().getBool("appBannerPreLoad", false) && (state_admobBanner.equals("Start")) || state_admobBanner.equals("Fail")) {

                if (PrefLibAds.getInstance().getString("ADMOB_BANNER").isEmpty()) {
                    return;
                }
                state_admobBanner = "Loading";

                AdView adView = new AdView(activity);
                adView.setAdUnitId(PrefLibAds.getInstance().getString("ADMOB_BANNER"));
                adView.setAdListener(new AdListener() {
                    @Override
                    public void onAdFailedToLoad(@NonNull LoadAdError loadAdError) {
                        super.onAdFailedToLoad(loadAdError);
                        state_admobBanner = "Fail";
                        Log.e(TAG, ADMOB2 + "REQ_FAIL");
                    }

                    @Override
                    public void onAdLoaded() {
                        state_admobBanner = "Loaded";
                        admobBannerAd_preLoad = adView;
                        Log.e(TAG, ADMOB + "REQ_LOAD");
                        Log.e(TAG, ADMOB + "AD_SHOW");
                        shimmer.setVisibility(View.GONE);
                        shimmer.stopShimmer();
                    }
                });
                FrameLayout.LayoutParams params = new FrameLayout.LayoutParams(ViewGroup.LayoutParams.WRAP_CONTENT, ViewGroup.LayoutParams.WRAP_CONTENT);
                params.gravity = Gravity.CENTER;
                greedyxbannerad.addView(adView, params);
                AdSize adSize = getAdSize(activity, greedyxbannerad);
                adView.setAdSize(AdSize.BANNER);
                AdRequest build = new AdRequest.Builder().build();
                adView.loadAd(build);
            } else {
                Log.e("state_admobBanner", "proccess");
            }
        } else if (platform.equals(ADMOB1)) {
            if (admobBannerAd_preLoad1 == null && state_admobBanner1.equals("Loaded")) {
                state_admobBanner1 = "Start";
            }
            admobBannerAd_preLoad1 = null;
            if (PrefLibAds.getInstance().getBool("appBannerPreLoad", false) && (state_admobBanner1.equals("Start")) || state_admobBanner1.equals("Fail")) {

                if (PrefLibAds.getInstance().getString("ADMOB_BANNER1").isEmpty()) {
                    return;
                }
                state_admobBanner1 = "Loading";

                AdView adView = new AdView(activity);
                adView.setAdUnitId(PrefLibAds.getInstance().getString("ADMOB_BANNER1"));
                adView.setAdListener(new AdListener() {
                    @Override
                    public void onAdFailedToLoad(@NonNull LoadAdError loadAdError) {
                        super.onAdFailedToLoad(loadAdError);
                        state_admobBanner1 = "Fail";
                        Log.e(TAG, ADMOB1 + "REQ_FAIL");
                    }

                    @Override
                    public void onAdLoaded() {
                        state_admobBanner1 = "Loaded";
                        admobBannerAd_preLoad1 = adView;
                        Log.e(TAG, ADMOB1 + "REQ_LOAD");
                        Log.e(TAG, ADMOB1 + "AD_SHOW");
                        shimmer.setVisibility(View.GONE);
                        shimmer.stopShimmer();
                    }
                });
                FrameLayout.LayoutParams params = new FrameLayout.LayoutParams(ViewGroup.LayoutParams.WRAP_CONTENT, ViewGroup.LayoutParams.WRAP_CONTENT);
                params.gravity = Gravity.CENTER;
                greedyxbannerad.addView(adView, params);
                AdSize adSize = getAdSize(activity, greedyxbannerad);
                adView.setAdSize(AdSize.BANNER);
                AdRequest build = new AdRequest.Builder().build();
                adView.loadAd(build);
            } else {
                Log.e("state_admobBanner", "proccess");
            }
        } else if (platform.equals(ADMOB2)) {
            if (admobBannerAd_preLoad2 == null && state_admobBanner2.equals("Loaded")) {
                state_admobBanner2 = "Start";
            }
            admobBannerAd_preLoad2 = null;
            if (PrefLibAds.getInstance().getBool("appBannerPreLoad", false) && (state_admobBanner2.equals("Start")) || state_admobBanner2.equals("Fail")) {

                if (PrefLibAds.getInstance().getString("ADMOB_BANNER2").isEmpty()) {
                    return;
                }
                state_admobBanner2 = "Loading";

                AdView adView = new AdView(activity);
                adView.setAdUnitId(PrefLibAds.getInstance().getString("ADMOB_BANNER2"));
                adView.setAdListener(new AdListener() {
                    @Override
                    public void onAdFailedToLoad(@NonNull LoadAdError loadAdError) {
                        super.onAdFailedToLoad(loadAdError);
                        state_admobBanner2 = "Fail";
                        Log.e(TAG, ADMOB2 + "REQ_FAIL");
                    }

                    @Override
                    public void onAdLoaded() {
                        state_admobBanner2 = "Loaded";
                        admobBannerAd_preLoad2 = adView;
                        Log.e(TAG, ADMOB2 + "REQ_LOAD");
                        Log.e(TAG, ADMOB2 + "AD_SHOW");
                        shimmer.setVisibility(View.GONE);
                        shimmer.stopShimmer();
                    }
                });
                FrameLayout.LayoutParams params = new FrameLayout.LayoutParams(ViewGroup.LayoutParams.WRAP_CONTENT, ViewGroup.LayoutParams.WRAP_CONTENT);
                params.gravity = Gravity.CENTER;
                greedyxbannerad.addView(adView, params);
                AdSize adSize = getAdSize(activity, greedyxbannerad);
                adView.setAdSize(AdSize.BANNER);
                AdRequest build = new AdRequest.Builder().build();
                adView.loadAd(build);
            } else {
                Log.e("state_admobBanner", "proccess");
            }
        }
    }

    private void nextBannerPlatform(Activity activity, FrameLayout greedyxbannerad, ShimmerFrameLayout shimmer) {
        if (banner_sequence.size() != 0) {
            banner_sequence.remove(0);
            if (banner_sequence.size() != 0) {
                displayBanner(banner_sequence.get(0), activity, greedyxbannerad, shimmer);
            } else {
                showCustomBannerAds(activity, greedyxbannerad, shimmer);
            }
        } else {
            showCustomBannerAds(activity, greedyxbannerad, shimmer);
        }
    }

    private AdSize getAdSize(Activity context, FrameLayout rlBannerAd) {
        // Determine the screen width (less decorations) to use for the ad width.
        Display display = context.getWindowManager().getDefaultDisplay();
        DisplayMetrics outMetrics = new DisplayMetrics();
        display.getMetrics(outMetrics);

        float density = outMetrics.density;

        float adWidthPixels = rlBannerAd.getWidth();

        // If the ad hasn't been laid out, default to the full screen width.
        if (adWidthPixels == 0) {
            adWidthPixels = outMetrics.widthPixels;
        }

        int adWidth = (int) (adWidthPixels / density);
        return AdSize.getCurrentOrientationAnchoredAdaptiveBannerAdSize(context, adWidth);
    }
}

