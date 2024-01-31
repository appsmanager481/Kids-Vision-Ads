package com.kidsads.visionkidsads.ads;

import static com.kidsads.visionkidsads.retrofit.ManagerAdsData.ADMOB;
import static com.kidsads.visionkidsads.retrofit.ManagerAdsData.ADMOB1;
import static com.kidsads.visionkidsads.retrofit.ManagerAdsData.ADMOB2;

import android.annotation.SuppressLint;
import android.app.Activity;
import android.content.Context;
import android.content.Intent;
import android.content.res.ColorStateList;
import android.graphics.Color;
import android.net.ConnectivityManager;
import android.net.NetworkInfo;
import android.net.Uri;
import android.os.Bundle;
import android.util.AttributeSet;
import android.util.DisplayMetrics;
import android.util.Log;
import android.view.Display;
import android.view.Gravity;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.FrameLayout;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.RatingBar;
import android.widget.RelativeLayout;
import android.widget.TextView;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.cardview.widget.CardView;
import androidx.core.content.ContextCompat;

import com.kidsads.visionkidsads.R;
import com.kidsads.visionkidsads.model.CustomAds;
import com.kidsads.visionkidsads.utils.Connectivity;
import com.kidsads.visionkidsads.utils.PrefLibAds;
import com.bumptech.glide.Glide;
import com.facebook.shimmer.ShimmerFrameLayout;
import com.google.ads.mediation.admob.AdMobAdapter;
import com.google.android.gms.ads.AdListener;
import com.google.android.gms.ads.AdLoader;
import com.google.android.gms.ads.AdRequest;
import com.google.android.gms.ads.AdSize;
import com.google.android.gms.ads.AdView;
import com.google.android.gms.ads.LoadAdError;
import com.google.android.gms.ads.VideoOptions;
import com.google.android.gms.ads.formats.NativeAdOptions;
import com.google.android.gms.ads.nativead.NativeAd;
import com.google.android.gms.ads.nativead.NativeAdView;

import java.util.ArrayList;
import java.util.Collections;
import java.util.List;

public class CollapsibleBannerAdsView extends LinearLayout {

    private static final String TAG = CollapsibleBannerAdsView.class.getSimpleName();
    public static int count_banner = -1;
    public static NativeAd nativeAds;
    public static NativeAd nativeAds1;
    public static NativeAd nativeAds2;
    public static String state_admobNative = "Start";
    public static String state_admobNative1 = "Start";
    public static String state_admobNative2 = "Start";
    static RelativeLayout relativeLayout;
    static NativeAd admobNativeAd_preLoad = null;
    static NativeAd admobNativeAd_preLoad1 = null;
    static NativeAd admobNativeAd_preLoad2 = null;
    public String state_admobBanner = "Start";
    public String state_fbBanner = "Start";
    public String state_admobBanner1 = "Start";
    public String state_admobBanner2 = "Start";
    public String state_applovinBanner = "Start";
    public String state_fbNative = "Start";
    public String state_applovinNative = "Start";
    ArrayList<String> banner_sequence = new ArrayList<>();
    AdView admobBannerAd_preLoad = null;
    AdView admobBannerAd_preLoad1 = null;
    AdView admobBannerAd_preLoad2 = null;
    ArrayList<String> native_sequence = new ArrayList<>();
    private CardView cardMain;

    public CollapsibleBannerAdsView(Context context) {
        this(context, null);
    }

    public CollapsibleBannerAdsView(Context context, AttributeSet attrs) {
        this(context, attrs, 0);
    }

    @SuppressLint("CustomViewStyleable")
    public CollapsibleBannerAdsView(Context context, AttributeSet attrs, int defStyle) {
        super(context, attrs, defStyle);

        boolean isFastConnection = Connectivity.isConnectedFast(context);

        if (!isFastConnection) {
            return;
        }

        if (PrefLibAds.getInstance().getString("app_adShowStatus").equals("false")) {
            return;
        }

        count_banner++;

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
            }
        }


        if (banner_sequence.size() != 0) {
            if (PrefLibAds.getInstance().getString("appNativeAdSize").equals("Big")) {
                View.inflate(context, R.layout.main_banner_layout, this);
                try {
                    FrameLayout banner_ad_container = findViewById(R.id.banner_ad_container);
                    RelativeLayout bannerlout = findViewById(R.id.bannerlout);
                    ShimmerFrameLayout shimmer_view_container = findViewById(R.id.shimmer_view_container);
                    banner_ad_container.setVisibility(View.VISIBLE);
                    bannerlout.setVisibility(View.VISIBLE);

                    displayBanner(banner_sequence.get(0), (Activity) context, banner_ad_container, shimmer_view_container);
                } catch (Exception e) {
                    Log.e("----error", e.getMessage() + "");
                    Toast.makeText(context, e.getMessage() + "", Toast.LENGTH_SHORT).show();
                }
            } else {
                View.inflate(context, R.layout.main_small_native_layout, this);
                try {
                    FrameLayout greedyxbannerad = findViewById(R.id.native_ad_container);
                    RelativeLayout nativelout = findViewById(R.id.nativelout);
                    ShimmerFrameLayout shimmer_view_container = findViewById(R.id.shimmer_view_container);
                    cardMain = findViewById(R.id.cardMain);
                    if (!PrefLibAds.getInstance().getString("appAdsBackgroundColor").equals("")) {
                        cardMain.setCardBackgroundColor(ColorStateList.valueOf(Color.parseColor(PrefLibAds.getInstance().getString("appAdsBackgroundColor"))));
                    }

                    greedyxbannerad.setVisibility(View.VISIBLE);
                    nativelout.setVisibility(View.VISIBLE);

                    displayNative(banner_sequence.get(0), greedyxbannerad, shimmer_view_container, (Activity) context);

                } catch (Exception e) {
                    Log.e("----error", e.getMessage() + "");
                }
            }
        }
    }

    public static NativeAd getNativeAds() {
        return nativeAds;
    }

    private static void populateUnifiedNativeAdView(Activity activity, NativeAd nativeAd, FrameLayout frameLayout) {

        NativeAdView adView = (NativeAdView) activity.getLayoutInflater().inflate(R.layout.admob_small_native_ads, null);

        if (frameLayout != null) {
            frameLayout.removeAllViews();
            frameLayout.addView(adView);
        }

        com.google.android.gms.ads.nativead.MediaView mediaView = adView.findViewById(R.id.ad_media);
        adView.setMediaView(mediaView);

        adView.setHeadlineView(adView.findViewById(R.id.ad_headline));
        adView.setBodyView(adView.findViewById(R.id.ad_body));
        adView.setCallToActionView(adView.findViewById(R.id.ad_call_to_action));
        adView.setIconView(adView.findViewById(R.id.ad_app_icon));
        adView.setPriceView(adView.findViewById(R.id.ad_price));
        adView.setStarRatingView(adView.findViewById(R.id.ad_stars));
        adView.setStoreView(adView.findViewById(R.id.ad_store));
        adView.setAdvertiserView(adView.findViewById(R.id.ad_advertiser));

        ((TextView) adView.getHeadlineView()).setText(nativeAd.getHeadline());

        if (nativeAd.getHeadline() == null) {
            adView.getHeadlineView().setVisibility(View.INVISIBLE);
        } else {
            adView.getHeadlineView().setVisibility(View.VISIBLE);
            ((TextView) adView.getHeadlineView()).setText(nativeAd.getHeadline());
            if (!PrefLibAds.getInstance().getString("appAdsTextColor").equals("")) {
                ((TextView) adView.getHeadlineView()).setTextColor(ColorStateList.valueOf(Color.parseColor(PrefLibAds.getInstance().getString("appAdsTextColor"))));
            } else {
                ((TextView) adView.getHeadlineView()).setTextColor(ColorStateList.valueOf(Color.parseColor("#000000")));
            }
        }


        if (nativeAd.getBody() == null) {
            adView.getBodyView().setVisibility(View.GONE);
        } else {
            adView.getBodyView().setVisibility(View.GONE);
            ((TextView) adView.getBodyView()).setText(nativeAd.getBody());
        }

        if (nativeAd.getCallToAction() == null) {
            adView.getCallToActionView().setVisibility(View.INVISIBLE);
        } else {
            adView.getCallToActionView().setVisibility(View.VISIBLE);
            ((TextView) adView.getCallToActionView()).setText(nativeAd.getCallToAction());
            if (!PrefLibAds.getInstance().getString("appAdsButtonTextColor").equals("")) {
                ((TextView) adView.getCallToActionView()).setTextColor(ColorStateList.valueOf(Color.parseColor(PrefLibAds.getInstance().getString("appAdsButtonTextColor"))));
            } else {
                ((TextView) adView.getCallToActionView()).setTextColor(ColorStateList.valueOf(Color.parseColor("#FFFFFF")));
            }
            if (!PrefLibAds.getInstance().getString("appAdsButtonColor").equals("")) {
                adView.findViewById(R.id.ad_call_to_action).setBackgroundTintList(ColorStateList.valueOf(Color.parseColor(PrefLibAds.getInstance().getString("appAdsButtonColor"))));
            } else {
                adView.findViewById(R.id.ad_call_to_action).setBackgroundTintList(ColorStateList.valueOf(Color.parseColor("#0C6EFC")));
            }
        }

        if (nativeAd.getIcon() == null) {
            adView.getIconView().setVisibility(View.GONE);
        } else {
            ((ImageView) adView.getIconView()).setImageDrawable(nativeAd.getIcon().getDrawable());
            adView.getIconView().setVisibility(View.VISIBLE);
        }

        if (nativeAd.getPrice() == null) {
            adView.getPriceView().setVisibility(View.GONE);
        } else {
            adView.getPriceView().setVisibility(View.GONE);
            ((TextView) adView.getPriceView()).setText(nativeAd.getPrice());
        }

        if (nativeAd.getStore() == null) {
            adView.getStoreView().setVisibility(View.GONE);
        } else {
            adView.getStoreView().setVisibility(View.GONE);
            ((TextView) adView.getStoreView()).setText(nativeAd.getStore());
        }

        if (nativeAd.getStarRating() == null) {
            adView.getStarRatingView().setVisibility(View.GONE);
        } else {
            ((RatingBar) adView.getStarRatingView()).setRating(nativeAd.getStarRating().floatValue());
            adView.getStarRatingView().setVisibility(View.GONE);
        }

        if (nativeAd.getAdvertiser() == null) {
            adView.getAdvertiserView().setVisibility(View.INVISIBLE);
        } else {
            adView.getAdvertiserView().setVisibility(View.VISIBLE);
            ((TextView) adView.getAdvertiserView()).setText(nativeAd.getAdvertiser());
            if (!PrefLibAds.getInstance().getString("appAdsTextColor").equals("")) {
                ((TextView) adView.getAdvertiserView()).setTextColor(ColorStateList.valueOf(Color.parseColor(PrefLibAds.getInstance().getString("appAdsTextColor"))));
            } else {
                ((TextView) adView.getAdvertiserView()).setTextColor(ColorStateList.valueOf(Color.parseColor("#000000")));
            }
        }

        adView.setNativeAd(nativeAd);

        if (admobNativeAd_preLoad == null && state_admobNative.equals("Loaded")) {
            state_admobNative = "Start";
        }
        admobNativeAd_preLoad = null;
        if (PrefLibAds.getInstance().getBool("appNativePreLoad", false) && (state_admobNative.equals("Start")) || state_admobNative.equals("Fail")) {

            if (PrefLibAds.getInstance().getString("ADMOB_NATIVE").isEmpty()) {
                return;
            }
            state_admobNative = "Loading";
            final AdLoader adLoader = new AdLoader.Builder(activity,PrefLibAds.getInstance().getString("ADMOB_NATIVE"))
                    .forNativeAd(new NativeAd.OnNativeAdLoadedListener() {
                        @Override
                        public void onNativeAdLoaded(NativeAd nativeAd) {
                            // Show the ad.
                            admobNativeAd_preLoad = nativeAd;
                            state_admobNative = "Loaded";
                            Log.e(TAG, "onNativeAdLoaded: " + "REQ_LOAD");
                            Log.e(TAG, "onNativeAdLoaded: " + "AD_SHOW");
                        }
                    })
                    .withAdListener(new AdListener() {
                        @Override
                        public void onAdFailedToLoad(LoadAdError adError) {
                            // Handle the failure by logging, altering the UI, and so on.
                            state_admobNative = "Fail";
                            Log.e(TAG, "onNativeAdLoaded: " + "REQ_FAIL");
                        }
                    })
                    .withNativeAdOptions(new NativeAdOptions.Builder()
                            // Methods in the NativeAdOptions.Builder class can be
                            // used here to specify individual options settings.
                            .build())
                    .build();
            adLoader.loadAd(new AdRequest.Builder().build());


        } else {
            Log.e("admob_state", "proccess");
        }
    }

    private static void populateUnifiedNativeAdView2(Activity activity, NativeAd nativeAd, FrameLayout frameLayout) {

        NativeAdView adView = (NativeAdView) activity.getLayoutInflater().inflate(R.layout.admob_small_native_ads, null);

        if (frameLayout != null) {
            frameLayout.removeAllViews();
        }
        frameLayout.addView(adView);

        com.google.android.gms.ads.nativead.MediaView mediaView = adView.findViewById(R.id.ad_media);
        adView.setMediaView(mediaView);

        adView.setHeadlineView(adView.findViewById(R.id.ad_headline));
        adView.setBodyView(adView.findViewById(R.id.ad_body));
        adView.setCallToActionView(adView.findViewById(R.id.ad_call_to_action));
        adView.setIconView(adView.findViewById(R.id.ad_app_icon));
        adView.setPriceView(adView.findViewById(R.id.ad_price));
        adView.setStarRatingView(adView.findViewById(R.id.ad_stars));
        adView.setStoreView(adView.findViewById(R.id.ad_store));
        adView.setAdvertiserView(adView.findViewById(R.id.ad_advertiser));

        if (nativeAd.getHeadline() == null) {
            adView.getHeadlineView().setVisibility(View.INVISIBLE);
        } else {
            adView.getHeadlineView().setVisibility(View.VISIBLE);
            ((TextView) adView.getHeadlineView()).setText(nativeAd.getHeadline());
            if (!PrefLibAds.getInstance().getString("appAdsTextColor").equals("")) {
                ((TextView) adView.getHeadlineView()).setTextColor(ColorStateList.valueOf(Color.parseColor(PrefLibAds.getInstance().getString("appAdsTextColor"))));
            } else {
                ((TextView) adView.getHeadlineView()).setTextColor(ColorStateList.valueOf(Color.parseColor("#000000")));
            }
        }


        if (nativeAd.getBody() == null) {
            adView.getBodyView().setVisibility(View.GONE);
        } else {
            adView.getBodyView().setVisibility(View.GONE);
            ((TextView) adView.getBodyView()).setText(nativeAd.getBody());
        }

        if (nativeAd.getCallToAction() == null) {
            adView.getCallToActionView().setVisibility(View.INVISIBLE);
        } else {
            adView.getCallToActionView().setVisibility(View.VISIBLE);
            ((TextView) adView.getCallToActionView()).setText(nativeAd.getCallToAction());
            if (!PrefLibAds.getInstance().getString("appAdsButtonTextColor").equals("")) {
                ((TextView) adView.getCallToActionView()).setTextColor(ColorStateList.valueOf(Color.parseColor(PrefLibAds.getInstance().getString("appAdsButtonTextColor"))));
            } else {
                ((TextView) adView.getCallToActionView()).setTextColor(ColorStateList.valueOf(Color.parseColor("#FFFFFF")));
            }
            if (!PrefLibAds.getInstance().getString("appAdsButtonColor").equals("")) {
                adView.findViewById(R.id.ad_call_to_action).setBackgroundTintList(ColorStateList.valueOf(Color.parseColor(PrefLibAds.getInstance().getString("appAdsButtonColor"))));
            } else {
                adView.findViewById(R.id.ad_call_to_action).setBackgroundTintList(ColorStateList.valueOf(Color.parseColor("#0C6EFC")));
            }
        }

        if (nativeAd.getIcon() == null) {
            adView.getIconView().setVisibility(View.GONE);
        } else {
            ((ImageView) adView.getIconView()).setImageDrawable(nativeAd.getIcon().getDrawable());
            adView.getIconView().setVisibility(View.VISIBLE);
        }

        if (nativeAd.getPrice() == null) {
            adView.getPriceView().setVisibility(View.GONE);
        } else {
            adView.getPriceView().setVisibility(View.GONE);
            ((TextView) adView.getPriceView()).setText(nativeAd.getPrice());
        }

        if (nativeAd.getStore() == null) {
            adView.getStoreView().setVisibility(View.GONE);
        } else {
            adView.getStoreView().setVisibility(View.GONE);
            ((TextView) adView.getStoreView()).setText(nativeAd.getStore());
        }

        if (nativeAd.getStarRating() == null) {
            adView.getStarRatingView().setVisibility(View.GONE);
        } else {
            ((RatingBar) adView.getStarRatingView()).setRating(nativeAd.getStarRating().floatValue());
            adView.getStarRatingView().setVisibility(View.GONE);
        }

        if (nativeAd.getAdvertiser() == null) {
            adView.getAdvertiserView().setVisibility(View.INVISIBLE);
        } else {
            adView.getAdvertiserView().setVisibility(View.VISIBLE);
            ((TextView) adView.getAdvertiserView()).setText(nativeAd.getAdvertiser());
            if (!PrefLibAds.getInstance().getString("appAdsTextColor").equals("")) {
                ((TextView) adView.getAdvertiserView()).setTextColor(ColorStateList.valueOf(Color.parseColor(PrefLibAds.getInstance().getString("appAdsTextColor"))));
            } else {
                ((TextView) adView.getAdvertiserView()).setTextColor(ColorStateList.valueOf(Color.parseColor("#000000")));
            }
        }

        adView.setNativeAd(nativeAd);

        if (admobNativeAd_preLoad2 == null && state_admobNative2.equals("Loaded")) {
            state_admobNative2 = "Start";
        }
        admobNativeAd_preLoad2 = null;
        if (PrefLibAds.getInstance().getBool("appNativePreLoad", false) && (state_admobNative2.equals("Start")) || state_admobNative2.equals("Fail")) {

            if (PrefLibAds.getInstance().getString("ADMOB_NATIVE2").isEmpty()) {
                return;
            }
            state_admobNative2 = "Loading";
            final AdLoader adLoader = new AdLoader.Builder(activity, PrefLibAds.getInstance().getString("ADMOB_NATIVE2"))
                    .forNativeAd(new NativeAd.OnNativeAdLoadedListener() {
                        @Override
                        public void onNativeAdLoaded(NativeAd nativeAd) {
                            // Show the ad.
                            admobNativeAd_preLoad2 = nativeAd;
                            state_admobNative2 = "Loaded";
                            Log.e(TAG, "onNativeAdLoaded: " + "REQ_LOAD");
                            Log.e(TAG, "onNativeAdLoaded: " + "AD_SHOW");
                        }
                    })
                    .withAdListener(new AdListener() {
                        @Override
                        public void onAdFailedToLoad(LoadAdError adError) {
                            // Handle the failure by logging, altering the UI, and so on.
                            state_admobNative2 = "Fail";
                            Log.e(TAG, "onNativeAdLoaded: " + "REQ_FAIL");
                        }
                    })
                    .withNativeAdOptions(new NativeAdOptions.Builder()
                            // Methods in the NativeAdOptions.Builder class can be
                            // used here to specify individual options settings.
                            .build())
                    .build();
            adLoader.loadAd(new AdRequest.Builder().build());


        } else {
            Log.e("admob_state", "proccess");
        }
    }

    private static void populateUnifiedNativeAdView1(Activity activity, NativeAd nativeAd, FrameLayout frameLayout) {

        NativeAdView adView = (NativeAdView) activity.getLayoutInflater().inflate(R.layout.admob_small_native_ads, null);

        if (frameLayout != null) {
            frameLayout.removeAllViews();
        }
        frameLayout.addView(adView);

        com.google.android.gms.ads.nativead.MediaView mediaView = adView.findViewById(R.id.ad_media);
        adView.setMediaView(mediaView);

        adView.setHeadlineView(adView.findViewById(R.id.ad_headline));
        adView.setBodyView(adView.findViewById(R.id.ad_body));
        adView.setCallToActionView(adView.findViewById(R.id.ad_call_to_action));
        adView.setIconView(adView.findViewById(R.id.ad_app_icon));
        adView.setPriceView(adView.findViewById(R.id.ad_price));
        adView.setStarRatingView(adView.findViewById(R.id.ad_stars));
        adView.setStoreView(adView.findViewById(R.id.ad_store));
        adView.setAdvertiserView(adView.findViewById(R.id.ad_advertiser));

        if (nativeAd.getHeadline() == null) {
            adView.getHeadlineView().setVisibility(View.INVISIBLE);
        } else {
            adView.getHeadlineView().setVisibility(View.VISIBLE);
            ((TextView) adView.getHeadlineView()).setText(nativeAd.getHeadline());
            if (!PrefLibAds.getInstance().getString("appAdsTextColor").equals("")) {
                ((TextView) adView.getHeadlineView()).setTextColor(ColorStateList.valueOf(Color.parseColor(PrefLibAds.getInstance().getString("appAdsTextColor"))));
            } else {
                ((TextView) adView.getHeadlineView()).setTextColor(ColorStateList.valueOf(Color.parseColor("#000000")));
            }
        }


        if (nativeAd.getBody() == null) {
            adView.getBodyView().setVisibility(View.GONE);
        } else {
            adView.getBodyView().setVisibility(View.GONE);
            ((TextView) adView.getBodyView()).setText(nativeAd.getBody());
        }

        if (nativeAd.getCallToAction() == null) {
            adView.getCallToActionView().setVisibility(View.INVISIBLE);
        } else {
            adView.getCallToActionView().setVisibility(View.VISIBLE);
            ((TextView) adView.getCallToActionView()).setText(nativeAd.getCallToAction());
            if (!PrefLibAds.getInstance().getString("appAdsButtonTextColor").equals("")) {
                ((TextView) adView.getCallToActionView()).setTextColor(ColorStateList.valueOf(Color.parseColor(PrefLibAds.getInstance().getString("appAdsButtonTextColor"))));
            } else {
                ((TextView) adView.getCallToActionView()).setTextColor(ColorStateList.valueOf(Color.parseColor("#FFFFFF")));
            }
            if (!PrefLibAds.getInstance().getString("appAdsButtonColor").equals("")) {
                adView.findViewById(R.id.ad_call_to_action).setBackgroundTintList(ColorStateList.valueOf(Color.parseColor(PrefLibAds.getInstance().getString("appAdsButtonColor"))));
            } else {
                adView.findViewById(R.id.ad_call_to_action).setBackgroundTintList(ColorStateList.valueOf(Color.parseColor("#0C6EFC")));
            }
        }

        if (nativeAd.getIcon() == null) {
            adView.getIconView().setVisibility(View.GONE);
        } else {
            ((ImageView) adView.getIconView()).setImageDrawable(nativeAd.getIcon().getDrawable());
            adView.getIconView().setVisibility(View.VISIBLE);
        }

        if (nativeAd.getPrice() == null) {
            adView.getPriceView().setVisibility(View.GONE);
        } else {
            adView.getPriceView().setVisibility(View.GONE);
            ((TextView) adView.getPriceView()).setText(nativeAd.getPrice());
        }

        if (nativeAd.getStore() == null) {
            adView.getStoreView().setVisibility(View.GONE);
        } else {
            adView.getStoreView().setVisibility(View.GONE);
            ((TextView) adView.getStoreView()).setText(nativeAd.getStore());
        }

        if (nativeAd.getStarRating() == null) {
            adView.getStarRatingView().setVisibility(View.GONE);
        } else {
            ((RatingBar) adView.getStarRatingView()).setRating(nativeAd.getStarRating().floatValue());
            adView.getStarRatingView().setVisibility(View.GONE);
        }

        if (nativeAd.getAdvertiser() == null) {
            adView.getAdvertiserView().setVisibility(View.INVISIBLE);
        } else {
            adView.getAdvertiserView().setVisibility(View.VISIBLE);
            ((TextView) adView.getAdvertiserView()).setText(nativeAd.getAdvertiser());
            if (!PrefLibAds.getInstance().getString("appAdsTextColor").equals("")) {
                ((TextView) adView.getAdvertiserView()).setTextColor(ColorStateList.valueOf(Color.parseColor(PrefLibAds.getInstance().getString("appAdsTextColor"))));
            } else {
                ((TextView) adView.getAdvertiserView()).setTextColor(ColorStateList.valueOf(Color.parseColor("#000000")));
            }
        }

        adView.setNativeAd(nativeAd);

        if (admobNativeAd_preLoad1 == null && state_admobNative1.equals("Loaded")) {
            state_admobNative1 = "Start";
        }
        admobNativeAd_preLoad1 = null;
        if (PrefLibAds.getInstance().getBool("appNativePreLoad", false) && (state_admobNative1.equals("Start")) || state_admobNative1.equals("Fail")) {

            if (PrefLibAds.getInstance().getString("ADMOB_NATIVE1").isEmpty()) {
                return;
            }
            state_admobNative1 = "Loading";
            final AdLoader adLoader = new AdLoader.Builder(activity, PrefLibAds.getInstance().getString("ADMOB_NATIVE1"))
                    .forNativeAd(new NativeAd.OnNativeAdLoadedListener() {
                        @Override
                        public void onNativeAdLoaded(@NonNull NativeAd nativeAd) {
                            // Show the ad.
                            admobNativeAd_preLoad1 = nativeAd;
                            state_admobNative1 = "Loaded";
                            Log.e(TAG, "onNativeAdLoaded1: " + "REQ_LOAD");
                            Log.e(TAG, "onNativeAdLoaded1: " + "AD_SHOW");
                        }
                    })
                    .withAdListener(new AdListener() {
                        @Override
                        public void onAdFailedToLoad(LoadAdError adError) {
                            // Handle the failure by logging, altering the UI, and so on.
                            state_admobNative1 = "Fail";
                            Log.e(TAG, "onNativeAdLoaded1: " + "REQ_FAIL");
                        }
                    })
                    .withNativeAdOptions(new NativeAdOptions.Builder()
                            // Methods in the NativeAdOptions.Builder class can be
                            // used here to specify individual options settings.
                            .build())
                    .build();
            adLoader.loadAd(new AdRequest.Builder().build());


        } else {
            Log.e("admob_state", "proccess");
        }
    }

    public static boolean hasActiveInternetConnection(Context context) {
        @SuppressLint("WrongConstant") NetworkInfo activeNetworkInfo = ((ConnectivityManager) context.getSystemService("connectivity")).getActiveNetworkInfo();
        return activeNetworkInfo != null && activeNetworkInfo.isConnected();
    }

    public void displayNative(String platform, FrameLayout nativeAdContainer, ShimmerFrameLayout shimmer_view_container, Activity context) {
        if (platform.equals(ADMOB)) {
            showAdmobNative(nativeAdContainer, shimmer_view_container, context);
        } else if (platform.equals(ADMOB1)) {
            showAdmobNative1(nativeAdContainer, shimmer_view_container, context);
        } else if (platform.equals(ADMOB2)) {
            showAdmobNative2(nativeAdContainer, shimmer_view_container, context);
        } else {
            showCustomNativeAd(context, nativeAdContainer, shimmer_view_container);
        }
    }

    public void showCustomNativeAd(Activity activity, FrameLayout frameLayout, ShimmerFrameLayout shimmer) {
        frameLayout.setVisibility(View.VISIBLE);
        shimmer.setVisibility(View.GONE);
        shimmer.stopShimmer();
        LayoutInflater inflater = LayoutInflater.from(activity);
        View customView = inflater.inflate(R.layout.custom_native_small_ads, frameLayout, false);
        frameLayout.addView(customView);

        ImageView ad_app_icon = customView.findViewById(R.id.ad_app_icon);
        TextView ad_headline = customView.findViewById(R.id.ad_headline);
        Button ad_call_to_action = customView.findViewById(R.id.ad_call_to_action);
        com.whinc.widget.ratingbar.RatingBar ratingBar = customView.findViewById(R.id.ratingBar);

        List<CustomAds> customAds = PrefLibAds.getCustomAdsData(activity);
        if (customAds != null && !customAds.isEmpty()) {
            for (int i = 0; i < customAds.size(); i++) {
                if (customAds.get(i).getFormatName() != null) {
                    if (customAds.get(i).getFormatName().equals("nativeAdvanced")) {
                        Glide.with(activity).load(customAds.get(i).getAssetsBanner()).into(ad_app_icon);
                        ad_headline.setText(customAds.get(i).getAssetsDownload());
                        if (!PrefLibAds.getInstance().getString("appAdsTextColor").equals("")) {
                            ad_headline.setTextColor(ColorStateList.valueOf(Color.parseColor(PrefLibAds.getInstance().getString("appAdsTextColor"))));
                        } else {
                            ad_headline.setTextColor(ColorStateList.valueOf(Color.parseColor("#000000")));
                        }
                        if (!PrefLibAds.getInstance().getString("appAdsButtonColor").equals("")) {
                            ad_call_to_action.setBackgroundTintList(ColorStateList.valueOf(Color.parseColor(PrefLibAds.getInstance().getString("appAdsButtonColor"))));
                        } else {
                            ad_call_to_action.setTextColor(ColorStateList.valueOf(Color.parseColor("#0C6EFC")));
                        }
                        if (!PrefLibAds.getInstance().getString("appAdsButtonTextColor").equals("")) {
                            ad_call_to_action.setTextColor(ColorStateList.valueOf(Color.parseColor(PrefLibAds.getInstance().getString("appAdsButtonTextColor"))));
                        } else {
                            ad_call_to_action.setTextColor(ColorStateList.valueOf(Color.parseColor("#FFFFFF")));
                        }
                        ad_call_to_action.setText(customAds.get(i).getActionButtonName());

                        ratingBar.setMaxCount(5);
                        ratingBar.setCount(customAds.get(i).getAssetsRating());
                        ratingBar.setEmptyDrawable(ContextCompat.getDrawable(activity, com.whinc.widget.ratingbar.R.drawable.empty));
                        ratingBar.setFillDrawable(ContextCompat.getDrawable(activity, com.whinc.widget.ratingbar.R.drawable.fill));
                        ratingBar.setClickRating(false);
                        ratingBar.setTouchRating(false);

                        int finalI = i;
                        ad_call_to_action.setOnClickListener(new OnClickListener() {
                            @Override
                            public void onClick(View v) {
                                Intent httpIntent = new Intent(Intent.ACTION_VIEW);
                                httpIntent.setData(Uri.parse(customAds.get(finalI).getAssetsUrl()));
                                activity.startActivity(httpIntent);
                            }
                        });
                    }
                } else {
                    cardMain.setVisibility(View.GONE);
                }
            }
        } else {
            cardMain.setVisibility(View.GONE);
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
            cardMain.setVisibility(View.GONE);
        }
    }


    private void nextNativePlatform(FrameLayout nativeAdContainer, ShimmerFrameLayout shimmer_view_container, Activity context) {
        if (native_sequence.size() != 0) {
            native_sequence.remove(0);
            if (native_sequence.size() != 0) {
                displayNative(native_sequence.get(0), nativeAdContainer, shimmer_view_container, context);
            } else {
                showCustomNativeAd(context, nativeAdContainer, shimmer_view_container);
            }
        } else {
            showCustomNativeAd(context, nativeAdContainer, shimmer_view_container);
        }
    }

    private void showAdmobNative(final FrameLayout nativeAdContainer, ShimmerFrameLayout shimmer, Activity activity) {
        if (PrefLibAds.getInstance().getString("ADMOB_NATIVE").isEmpty() || PrefLibAds.getInstance().getString("ad_showAdStatus").equals("false")) {
            nextNativePlatform(nativeAdContainer, shimmer, activity);
            return;
        }

        if (admobNativeAd_preLoad == null) {
            AdLoader.Builder builder = new AdLoader.Builder(activity, PrefLibAds.getInstance().getString("ADMOB_NATIVE"));
            builder.forNativeAd(new NativeAd.OnNativeAdLoadedListener() {

                @Override
                public void onNativeAdLoaded(@NonNull NativeAd nativeAd) {
                    if (nativeAds != null) {
                        nativeAds.destroy();
                    }
                    nativeAds = nativeAd;

                    shimmer.setVisibility(View.GONE);
                    shimmer.stopShimmer();
                    populateUnifiedNativeAdView(activity, nativeAd, nativeAdContainer);

                    Log.e(TAG, "onNativeAdLoaded: " + "REQ_LOAD");
                    Log.e(TAG, "onNativeAdLoaded: " + "AD_SHOW");
                }
            });

            VideoOptions videoOptions = new VideoOptions.Builder().setStartMuted(false).build();

            NativeAdOptions adOptions = new NativeAdOptions.Builder().setVideoOptions(videoOptions).build();

            builder.withNativeAdOptions(adOptions);

            AdLoader adLoader = builder.withAdListener(new AdListener() {
                @Override
                @Deprecated
                public void onAdFailedToLoad(@NonNull LoadAdError loadAdError) {
                    super.onAdFailedToLoad(loadAdError);
                    nativeAds = null;
                    Log.e(TAG, "onNativeAdLoaded: " + "REQ_FAIL");
                    nextNativePlatform(nativeAdContainer, shimmer, activity);
                }
            }).build();
            AdRequest adRequest = new AdRequest.Builder().build();

            adLoader.loadAd(adRequest);
        } else {
            state_admobNative = "Start";
            shimmer.setVisibility(View.GONE);
            shimmer.stopShimmer();
            populateUnifiedNativeAdView(activity, admobNativeAd_preLoad, nativeAdContainer);
        }
    }

    private void showAdmobNative2(final FrameLayout nativeAdContainer, ShimmerFrameLayout shimmer, Activity activity) {
        if (PrefLibAds.getInstance().getString("ADMOB_NATIVE2").isEmpty() || PrefLibAds.getInstance().getString("ad_showAdStatus").equals("false")) {
            nextNativePlatform(nativeAdContainer, shimmer, activity);
            return;
        }

        if (admobNativeAd_preLoad2 == null) {
            AdLoader.Builder builder = new AdLoader.Builder(activity, PrefLibAds.getInstance().getString("ADMOB_NATIVE2"));
            builder.forNativeAd(new NativeAd.OnNativeAdLoadedListener() {

                @Override
                public void onNativeAdLoaded(@NonNull NativeAd nativeAd) {
                    if (nativeAds2 != null) {
                        nativeAds2.destroy();
                    }
                    nativeAds2 = nativeAd;
                    shimmer.setVisibility(View.GONE);
                    shimmer.stopShimmer();
                    populateUnifiedNativeAdView(activity, nativeAd, nativeAdContainer);

                    Log.e(TAG, "onNativeAdLoaded: " + "REQ_LOAD");
                    Log.e(TAG, "onNativeAdLoaded: " + "AD_SHOW");
                }
            });

            VideoOptions videoOptions = new VideoOptions.Builder().setStartMuted(false).build();

            NativeAdOptions adOptions = new NativeAdOptions.Builder().setVideoOptions(videoOptions).build();

            builder.withNativeAdOptions(adOptions);

            AdLoader adLoader = builder.withAdListener(new AdListener() {
                @Override
                @Deprecated
                public void onAdFailedToLoad(@NonNull LoadAdError loadAdError) {
                    super.onAdFailedToLoad(loadAdError);
                    nativeAds2 = null;
                    Log.e(TAG, "onNativeAdLoaded: " + "REQ_FAIL");
                    nextNativePlatform(nativeAdContainer, shimmer, activity);
                }
            }).build();
            AdRequest adRequest = new AdRequest.Builder().build();

            adLoader.loadAd(adRequest);
        } else {
            state_admobNative2 = "Start";
            shimmer.setVisibility(View.GONE);
            shimmer.stopShimmer();
            populateUnifiedNativeAdView2(activity, admobNativeAd_preLoad2, nativeAdContainer);
        }
    }

    private void showAdmobNative1(final FrameLayout nativeAdContainer, ShimmerFrameLayout shimmer, Activity activity) {
        if (PrefLibAds.getInstance().getString("ADMOB_NATIVE1").isEmpty() || PrefLibAds.getInstance().getString("ad_showAdStatus").equals("false")) {
            nextNativePlatform(nativeAdContainer, shimmer, activity);
            return;
        }

        if (admobNativeAd_preLoad1 == null) {
            AdLoader.Builder builder = new AdLoader.Builder(activity, PrefLibAds.getInstance().getString("ADMOB_NATIVE1"));
            builder.forNativeAd(new NativeAd.OnNativeAdLoadedListener() {

                @Override
                public void onNativeAdLoaded(@NonNull NativeAd nativeAd) {
                    if (nativeAds1 != null) {
                        nativeAds1.destroy();
                    }
                    nativeAds1 = nativeAd;
                    shimmer.setVisibility(View.GONE);
                    shimmer.stopShimmer();
                    populateUnifiedNativeAdView(activity, nativeAd, nativeAdContainer);

                    Log.e(TAG, "onNativeAdLoaded1: " + "REQ_LOAD");
                    Log.e(TAG, "onNativeAdLoaded1: " + "AD_SHOW");
                }
            });

            VideoOptions videoOptions = new VideoOptions.Builder().setStartMuted(false).build();

            NativeAdOptions adOptions = new NativeAdOptions.Builder().setVideoOptions(videoOptions).build();

            builder.withNativeAdOptions(adOptions);

            AdLoader adLoader = builder.withAdListener(new AdListener() {
                @Override
                @Deprecated
                public void onAdFailedToLoad(@NonNull LoadAdError loadAdError) {
                    super.onAdFailedToLoad(loadAdError);
                    nativeAds1 = null;
                    Log.e(TAG, "onNativeAdLoaded1: " + "REQ_FAIL");
                    nextNativePlatform(nativeAdContainer, shimmer, activity);
                }
            }).build();
            AdRequest adRequest = new AdRequest.Builder().build();

            adLoader.loadAd(adRequest);
        } else {
            state_admobNative1 = "Start";
            shimmer.setVisibility(View.GONE);
            shimmer.stopShimmer();
            populateUnifiedNativeAdView1(activity, admobNativeAd_preLoad1, nativeAdContainer);
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

    private void showAdmobBanner(Activity activity, FrameLayout greedyxbannerad, ShimmerFrameLayout shimmer) {
        if (PrefLibAds.getInstance().getString("ADMOB_BANNER").isEmpty() || PrefLibAds.getInstance().getString("app_adShowStatus").equals("false")) {
            nextBannerPlatform(activity, greedyxbannerad, shimmer);
            return;
        }

        if (admobBannerAd_preLoad == null) {
            AdView adView = new AdView(activity);
            adView.setAdUnitId(PrefLibAds.getInstance().getString("ADMOB_BANNER"));
            Bundle bundle = new Bundle();
            bundle.putString("collapsible", "bottom");
            AdRequest build = new AdRequest.Builder().addNetworkExtrasBundle(AdMobAdapter.class, bundle).build();
            adView.setAdListener(new AdListener() {
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

                @Override
                public void onAdFailedToLoad(@NonNull LoadAdError adError) {
                    greedyxbannerad.removeAllViews();
                    nextBannerPlatform(activity, greedyxbannerad, shimmer);
                    Log.e(TAG, ADMOB2 + "REQ_FAIL");
                }

                @Override
                public void onAdOpened() {
                    Log.d("4444", "onAdOpened");
                }

                @Override
                public void onAdClicked() {
                    Log.d("4444", "onAdClicked");
                }

                @Override
                public void onAdClosed() {
                    adView.setAdSize(AdSize.BANNER);
                }
            });
            FrameLayout.LayoutParams params = new FrameLayout.LayoutParams(ViewGroup.LayoutParams.WRAP_CONTENT, ViewGroup.LayoutParams.WRAP_CONTENT);
            params.gravity = Gravity.CENTER;
            greedyxbannerad.addView(adView, params);
            adView.setAdSize(getAdSize(activity, greedyxbannerad));
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
            Bundle bundle = new Bundle();
            bundle.putString("collapsible", "bottom");
            AdRequest build = new AdRequest.Builder().addNetworkExtrasBundle(AdMobAdapter.class, bundle).build();
            adView.setAdListener(new AdListener() {
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

                @Override
                public void onAdFailedToLoad(@NonNull LoadAdError adError) {
                    greedyxbannerad.removeAllViews();
                    nextBannerPlatform(activity, greedyxbannerad, shimmer);
                    Log.e(TAG, ADMOB1 + "REQ_FAIL");
                }

                @Override
                public void onAdOpened() {
                    Log.d("4444", "onAdOpened");
                }

                @Override
                public void onAdClicked() {
                    Log.d("4444", "onAdClicked");
                }

                @Override
                public void onAdClosed() {
                    adView.setAdSize(AdSize.BANNER);
                }
            });
            FrameLayout.LayoutParams params = new FrameLayout.LayoutParams(ViewGroup.LayoutParams.WRAP_CONTENT, ViewGroup.LayoutParams.WRAP_CONTENT);
            params.gravity = Gravity.CENTER;
            greedyxbannerad.addView(adView, params);
            adView.setAdSize(getAdSize(activity, greedyxbannerad));
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
            Bundle bundle = new Bundle();
            bundle.putString("collapsible", "bottom");
            AdRequest build = new AdRequest.Builder().addNetworkExtrasBundle(AdMobAdapter.class, bundle).build();
            adView.setAdListener(new AdListener() {
                @Override
                public void onAdLoaded() {
                    greedyxbannerad.removeAllViews();
                    greedyxbannerad.addView(adView);
                    shimmer.setVisibility(View.GONE);
                    shimmer.stopShimmer();
                    preLoadBanner(ADMOB2, activity, greedyxbannerad, shimmer);
                    Log.e(TAG, ADMOB2 + "REQ_LOAD");
                    Log.e(TAG, ADMOB2 + "AD_SHOW");
                }

                @Override
                public void onAdFailedToLoad(@NonNull LoadAdError adError) {
                    greedyxbannerad.removeAllViews();
                    nextBannerPlatform(activity, greedyxbannerad, shimmer);
                    Log.e(TAG, ADMOB2 + "REQ_FAIL");
                }

                @Override
                public void onAdOpened() {
                    Log.d("4444", "onAdOpened");
                }

                @Override
                public void onAdClicked() {
                    Log.d("4444", "onAdClicked");
                }

                @Override
                public void onAdClosed() {
                    adView.setAdSize(AdSize.BANNER);
                }
            });
            FrameLayout.LayoutParams params = new FrameLayout.LayoutParams(ViewGroup.LayoutParams.WRAP_CONTENT, ViewGroup.LayoutParams.WRAP_CONTENT);
            params.gravity = Gravity.CENTER;
            greedyxbannerad.addView(adView, params);
            adView.setAdSize(getAdSize(activity, greedyxbannerad));
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
                Bundle bundle = new Bundle();
                bundle.putString("collapsible", "bottom");
                AdRequest build = new AdRequest.Builder().addNetworkExtrasBundle(AdMobAdapter.class, bundle).build();
                adView.setAdListener(new AdListener() {
                    @Override
                    public void onAdLoaded() {
                        state_admobBanner = "Loaded";
                        admobBannerAd_preLoad = adView;
                        Log.e(TAG, ADMOB + "REQ_LOAD");
                        Log.e(TAG, ADMOB + "AD_SHOW");
                        shimmer.setVisibility(View.GONE);
                        shimmer.stopShimmer();
                    }

                    @Override
                    public void onAdFailedToLoad(@NonNull LoadAdError adError) {
                        state_admobBanner = "Fail";
                        Log.e(TAG, ADMOB2 + "REQ_FAIL");
                    }

                    @Override
                    public void onAdOpened() {
                        Log.d("4444", "onAdOpened");
                    }

                    @Override
                    public void onAdClicked() {
                        Log.d("4444", "onAdClicked");
                    }

                    @Override
                    public void onAdClosed() {
                        adView.setAdSize(AdSize.BANNER);
                    }
                });
                FrameLayout.LayoutParams params = new FrameLayout.LayoutParams(ViewGroup.LayoutParams.WRAP_CONTENT, ViewGroup.LayoutParams.WRAP_CONTENT);
                params.gravity = Gravity.CENTER;
                greedyxbannerad.addView(adView, params);
                adView.setAdSize(AdSize.BANNER);
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
                Bundle bundle = new Bundle();
                bundle.putString("collapsible", "bottom");
                AdRequest build = new AdRequest.Builder().addNetworkExtrasBundle(AdMobAdapter.class, bundle).build();
                adView.setAdListener(new AdListener() {
                    @Override
                    public void onAdLoaded() {
                        state_admobBanner1 = "Loaded";
                        admobBannerAd_preLoad1 = adView;
                        Log.e(TAG, ADMOB1 + "REQ_LOAD");
                        Log.e(TAG, ADMOB1 + "AD_SHOW");
                        shimmer.setVisibility(View.GONE);
                        shimmer.stopShimmer();
                    }

                    @Override
                    public void onAdFailedToLoad(@NonNull LoadAdError adError) {
                        state_admobBanner1 = "Fail";
                        Log.e(TAG, ADMOB1 + "REQ_FAIL");
                    }

                    @Override
                    public void onAdOpened() {
                        Log.d("4444", "onAdOpened");
                    }

                    @Override
                    public void onAdClicked() {
                        Log.d("4444", "onAdClicked");
                    }

                    @Override
                    public void onAdClosed() {
                        adView.setAdSize(AdSize.BANNER);
                    }
                });
                FrameLayout.LayoutParams params = new FrameLayout.LayoutParams(ViewGroup.LayoutParams.WRAP_CONTENT, ViewGroup.LayoutParams.WRAP_CONTENT);
                params.gravity = Gravity.CENTER;
                greedyxbannerad.addView(adView, params);
                adView.setAdSize(AdSize.BANNER);
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
                Bundle bundle = new Bundle();
                bundle.putString("collapsible", "bottom");
                AdRequest build = new AdRequest.Builder().addNetworkExtrasBundle(AdMobAdapter.class, bundle).build();
                adView.setAdListener(new AdListener() {
                    @Override
                    public void onAdLoaded() {
                        state_admobBanner2 = "Loaded";
                        admobBannerAd_preLoad2 = adView;
                        Log.e(TAG, ADMOB2 + "REQ_LOAD");
                        Log.e(TAG, ADMOB2 + "AD_SHOW");
                        shimmer.setVisibility(View.GONE);
                        shimmer.stopShimmer();
                    }

                    @Override
                    public void onAdFailedToLoad(@NonNull LoadAdError adError) {
                        state_admobBanner2 = "Fail";
                        Log.e(TAG, ADMOB2 + "REQ_FAIL");
                    }

                    @Override
                    public void onAdOpened() {
                        Log.d("4444", "onAdOpened");
                    }

                    @Override
                    public void onAdClicked() {
                        Log.d("4444", "onAdClicked");
                    }

                    @Override
                    public void onAdClosed() {
                        adView.setAdSize(AdSize.BANNER);
                    }
                });
                FrameLayout.LayoutParams params = new FrameLayout.LayoutParams(ViewGroup.LayoutParams.WRAP_CONTENT, ViewGroup.LayoutParams.WRAP_CONTENT);
                params.gravity = Gravity.CENTER;
                greedyxbannerad.addView(adView, params);
                adView.setAdSize(AdSize.BANNER);
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
            }
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

