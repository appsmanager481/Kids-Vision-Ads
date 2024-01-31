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
import android.net.Uri;
import android.os.Handler;
import android.util.AttributeSet;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.widget.Button;
import android.widget.FrameLayout;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.RatingBar;
import android.widget.RelativeLayout;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.cardview.widget.CardView;
import androidx.core.content.ContextCompat;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import com.kidsads.visionkidsads.R;
import com.kidsads.visionkidsads.adapter.MoreAppAdapter;
import com.kidsads.visionkidsads.model.CustomAds;
import com.kidsads.visionkidsads.model.MoreAppIds;
import com.kidsads.visionkidsads.utils.PrefLibAds;
import com.bumptech.glide.Glide;
import com.facebook.shimmer.ShimmerFrameLayout;
import com.google.android.gms.ads.AdListener;
import com.google.android.gms.ads.AdLoader;
import com.google.android.gms.ads.AdRequest;
import com.google.android.gms.ads.LoadAdError;
import com.google.android.gms.ads.VideoOptions;
import com.google.android.gms.ads.formats.NativeAdOptions;
import com.google.android.gms.ads.nativead.NativeAd;
import com.google.android.gms.ads.nativead.NativeAdView;

import java.util.ArrayList;
import java.util.Collections;
import java.util.List;

@SuppressWarnings("UnusedDeclaration")
public class MediumNativeAdsView extends LinearLayout {
    private static final String TAG = MediumNativeAdsView.class.getSimpleName();
    public static NativeAd nativeAds;
    public static NativeAd nativeAds1;
    public static NativeAd nativeAds2;
    public static int count_native = -1;
    public static String state_admobNative = "Start";
    public static String state_admobNative1 = "Start";
    public static String state_admobNative2 = "Start";
    static NativeAd nativeAda;
    static FrameLayout aplovinnative_ad_container;
    static RelativeLayout relativeLayout;
    static NativeAd admobNativeAd_preLoad = null;
    static NativeAd admobNativeAd_preLoad1 = null;
    static NativeAd admobNativeAd_preLoad2 = null;
    static View moreView;
    static int scrollCount = 0;
    private static ArrayList<MoreAppIds> moreItemArrayList;
    public String state_fbNative = "Start";
    public String state_applovinNative = "Start";
    ArrayList<String> native_sequence = new ArrayList<>();
    String facebook_n;
    String applovin_n;
    private ShimmerFrameLayout shimmer_view_container;
    private RelativeLayout nativelout;
    private FrameLayout greedyxbannerad;
    private CardView cardMain;
    private ShimmerFrameLayout shimmerMoreApp;

    public MediumNativeAdsView(Context context) {
        this(context, null);
    }

    public MediumNativeAdsView(Context context, AttributeSet attrs) {
        this(context, attrs, 0);
    }

    @SuppressLint("CustomViewStyleable")
    public MediumNativeAdsView(Context context, AttributeSet attrs, int defStyle) {
        super(context, attrs, defStyle);

        if (PrefLibAds.getInstance().getString("app_adShowStatus").equals("false")) {
            return;
        }

        count_native++;

        View.inflate(context, R.layout.main_medium_native_layout, this);
        try {
            aplovinnative_ad_container = findViewById(R.id.aplovinnative_ad_container);
            greedyxbannerad = findViewById(R.id.native_ad_container);
            nativelout = findViewById(R.id.nativelout);
            shimmer_view_container = findViewById(R.id.shimmer_view_container);
            cardMain = findViewById(R.id.cardMain);
            shimmerMoreApp = findViewById(R.id.shimmerMoreApp);
            if (!PrefLibAds.getInstance().getString("appAdsBackgroundColor").equals("")) {
                cardMain.setCardBackgroundColor(ColorStateList.valueOf(Color.parseColor(PrefLibAds.getInstance().getString("appAdsBackgroundColor"))));
            } else {
                cardMain.setCardBackgroundColor(ColorStateList.valueOf(Color.parseColor("#FFFFFF")));
            }

            greedyxbannerad.setVisibility(View.VISIBLE);
            nativelout.setVisibility(View.VISIBLE);
            shimmerMoreApp.setVisibility(View.GONE);
        } catch (Exception e) {
            Log.e("----error", e.getMessage() + "");
        }

        String adApply = PrefLibAds.getInstance().getString("app_adApply");

        String all_howShowAd = PrefLibAds.getInstance().getString("app_howShowAd");
        String all_adPlatformSequence = PrefLibAds.getInstance().getString("app_adPlatformSequence");

        String format_howShowAdNative = PrefLibAds.getInstance().getString("app_howShowAdNative");
        String format_adPlatformSequenceNative = PrefLibAds.getInstance().getString("app_adPlatformSequenceNative").toLowerCase();

        native_sequence = new ArrayList<String>();
        if (adApply.equals("allAdFormat")) {
            if (all_howShowAd.equals("fixSequence") && !all_adPlatformSequence.isEmpty()) {
                String[] adSequence = all_adPlatformSequence.split(", ");
                Collections.addAll(native_sequence, adSequence);
            } else if (all_howShowAd.equals("alternateAdShow") && !all_adPlatformSequence.isEmpty()) {
                String[] alernateAd = all_adPlatformSequence.split(", ");

                int index = 0;
                for (int j = 0; j <= 10; j++) {
                    if (count_native % alernateAd.length == j) {
                        index = j;
                        native_sequence.add(alernateAd[index]);
                    }
                }

                String[] adSequence = all_adPlatformSequence.split(", ");
                for (String s : adSequence) {
                    if (native_sequence.size() != 0) {
                        if (!native_sequence.get(0).equals(s)) {
                            native_sequence.add(s);
                        }
                    }
                }
            } else {
                if (PrefLibAds.getInstance().getBool("moreApp", false)) {
                    shimmer_view_container.setVisibility(View.GONE);
                    shimmer_view_container.stopShimmer();
                    shimmerMoreApp.setVisibility(View.VISIBLE);
                    showMoreAppAds((Activity) context, greedyxbannerad);
                } else {
                    showCustomNativeAd((Activity) context, greedyxbannerad, shimmer_view_container);
                }
            }
        } else {
            if (format_howShowAdNative.equals("fixSequence") && !format_adPlatformSequenceNative.isEmpty()) {
                String[] adSequence = format_adPlatformSequenceNative.split(", ");
                Collections.addAll(native_sequence, adSequence);

            } else if (format_howShowAdNative.equals("alternateAdShow") && !format_adPlatformSequenceNative.isEmpty()) {
                String[] alernateAd = format_adPlatformSequenceNative.split(", ");

                int index = 0;
                for (int j = 0; j <= 10; j++) {
                    if (count_native % alernateAd.length == j) {
                        index = j;
                        native_sequence.add(alernateAd[index]);
                    }
                }

                String[] adSequence = format_adPlatformSequenceNative.split(", ");
                for (String s : adSequence) {
                    if (native_sequence.size() != 0) {
                        if (!native_sequence.get(0).equals(s)) {
                            native_sequence.add(s);
                        }
                    }
                }
            } else {
                if (PrefLibAds.getInstance().getBool("moreApp", false)) {
                    shimmer_view_container.setVisibility(View.GONE);
                    shimmer_view_container.stopShimmer();
                    shimmerMoreApp.setVisibility(View.VISIBLE);
                    showMoreAppAds((Activity) context, greedyxbannerad);
                } else {
                    showCustomNativeAd((Activity) context, greedyxbannerad, shimmer_view_container);
                }
            }
        }

        if (native_sequence.size() != 0) {
            displayNative(native_sequence.get(0), greedyxbannerad, shimmer_view_container, (Activity) context);
        } else {
            if (PrefLibAds.getInstance().getBool("moreApp", false)) {
                shimmer_view_container.setVisibility(View.GONE);
                shimmer_view_container.stopShimmer();
                shimmerMoreApp.setVisibility(View.VISIBLE);
                showMoreAppAds((Activity) context, greedyxbannerad);
            } else {
                showCustomNativeAd((Activity) context, greedyxbannerad, shimmer_view_container);
            }
        }
    }

    public static void autoScrollAnother(MoreAppAdapter categoryAdapter, RecyclerView rvAdsList) {
        scrollCount = 0;
        final Handler handler = new Handler();
        final Runnable runnable = new Runnable() {
            @Override
            public void run() {
                rvAdsList.smoothScrollToPosition((scrollCount++));
                if (moreItemArrayList != null) {
                    if (scrollCount == categoryAdapter.getItemCount() - 1) {
                        moreItemArrayList.addAll(moreItemArrayList);
                        categoryAdapter.notifyDataSetChanged();
                    }
                }
                handler.postDelayed(this, 1000);
            }
        };
        handler.postDelayed(runnable, 1000);
    }

    public static NativeAd getNativeAds() {
        return nativeAds;
    }

    private static void populateUnifiedNativeAdView(Activity activity, NativeAd nativeAd, FrameLayout frameLayout) {

        NativeAdView adView = (NativeAdView) activity.getLayoutInflater().inflate(R.layout.admob_medium_native_ads, null);

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
            adView.getBodyView().setVisibility(View.INVISIBLE);
        } else {
            adView.getBodyView().setVisibility(View.VISIBLE);
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
            final AdLoader adLoader = new AdLoader.Builder(activity, PrefLibAds.getInstance().getString("ADMOB_NATIVE"))
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

        NativeAdView adView = (NativeAdView) activity.getLayoutInflater().inflate(R.layout.admob_medium_native_ads, null);

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
            adView.getBodyView().setVisibility(View.INVISIBLE);
        } else {
            adView.getBodyView().setVisibility(View.VISIBLE);
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

        NativeAdView adView = (NativeAdView) activity.getLayoutInflater().inflate(R.layout.admob_medium_native_ads, null);

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
            adView.getBodyView().setVisibility(View.INVISIBLE);
        } else {
            adView.getBodyView().setVisibility(View.VISIBLE);
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

    public void showMoreAppAds(Activity activity, FrameLayout frameLayout) {
        frameLayout.setVisibility(View.VISIBLE);

        LayoutInflater inflater = LayoutInflater.from(activity);
        moreView = inflater.inflate(R.layout.main_more_small_app_ads, frameLayout, false);
        frameLayout.addView(moreView);

        RecyclerView rvCatAds = findViewById(R.id.rvCatAds);

        List<MoreAppIds> retrievedData = PrefLibAds.getAssignAppData(getContext());
        if (retrievedData != null && !retrievedData.isEmpty()) {
            for (int i = 0; i < retrievedData.size(); i++) {
                rvCatAds.setVisibility(View.VISIBLE);
                shimmerMoreApp.setVisibility(View.GONE);
                shimmerMoreApp.stopShimmer();

                MoreAppAdapter moreAppAdapter = new MoreAppAdapter(activity, retrievedData, R.layout.item_small_more_app_cat);
                autoScrollAnother(moreAppAdapter, rvCatAds);
                rvCatAds.setLayoutManager(new LinearLayoutManager(activity, RecyclerView.HORIZONTAL, false));
                rvCatAds.setHasFixedSize(true);
                rvCatAds.setAdapter(moreAppAdapter);
            }
        } else {
            cardMain.setVisibility(View.GONE);
            rvCatAds.setVisibility(View.GONE);
        }
    }

    private void displayNative(String platform, FrameLayout nativeAdContainer, ShimmerFrameLayout shimmer_view_container, Activity context) {
        if (platform.equals(ADMOB)) {
            showAdmobNative(nativeAdContainer, shimmer_view_container, context);
        } else if (platform.equals(ADMOB1)) {
            showAdmobNative1(nativeAdContainer, shimmer_view_container, context);
        } else if (platform.equals(ADMOB2)) {
            showAdmobNative2(nativeAdContainer, shimmer_view_container, context);
        } else {
            if (PrefLibAds.getInstance().getBool("moreApp", false)) {
                shimmer_view_container.setVisibility(View.GONE);
                shimmer_view_container.stopShimmer();
                shimmerMoreApp.setVisibility(View.VISIBLE);
                showMoreAppAds(context, nativeAdContainer);
            } else {
                showCustomNativeAd(context, nativeAdContainer, shimmer_view_container);
            }
        }
    }

    public void showCustomNativeAd(Activity activity, FrameLayout frameLayout, ShimmerFrameLayout shimmer) {
        frameLayout.setVisibility(View.VISIBLE);
        shimmer.setVisibility(View.GONE);
        shimmer.stopShimmer();
        LayoutInflater inflater = LayoutInflater.from(activity);
        View customView = inflater.inflate(R.layout.custom_native_medium_ads, frameLayout, false);
        frameLayout.addView(customView);

        ImageView bannerImg = customView.findViewById(R.id.bannerImg);
        ImageView ad_app_icon = customView.findViewById(R.id.ad_app_icon);
        TextView ad_headline = customView.findViewById(R.id.ad_headline);
        Button ad_call_to_action = customView.findViewById(R.id.ad_call_to_action);
        com.whinc.widget.ratingbar.RatingBar ratingBar = customView.findViewById(R.id.ratingBar);

        List<CustomAds> customAds = PrefLibAds.getCustomAdsData(activity);
        if (customAds != null && !customAds.isEmpty()) {
            for (int i = 0; i < customAds.size(); i++) {
                if (customAds.get(i).getFormatName() != null) {
                    if (customAds.get(i).getFormatName().equals("nativeAdvanced")) {
                        Glide.with(activity).load(customAds.get(i).getAssetsBanner()).into(bannerImg);
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

    private void nextNativePlatform(FrameLayout nativeAdContainer, ShimmerFrameLayout shimmer_view_container, Activity context) {
        if (native_sequence.size() != 0) {
            native_sequence.remove(0);
            if (native_sequence.size() != 0) {
                displayNative(native_sequence.get(0), nativeAdContainer, shimmer_view_container, context);
            } else {
                if (PrefLibAds.getInstance().getBool("moreApp", false)) {
                    shimmer_view_container.setVisibility(View.GONE);
                    shimmer_view_container.stopShimmer();
                    shimmerMoreApp.setVisibility(View.VISIBLE);
                    showMoreAppAds(context, nativeAdContainer);
                } else {
                    showCustomNativeAd(context, nativeAdContainer, shimmer_view_container);
                }
            }
        } else {
            if (PrefLibAds.getInstance().getBool("moreApp", false)) {
                shimmer_view_container.setVisibility(View.GONE);
                shimmer_view_container.stopShimmer();
                shimmerMoreApp.setVisibility(View.VISIBLE);
                showMoreAppAds(context, nativeAdContainer);
            } else {
                showCustomNativeAd(context, nativeAdContainer, shimmer_view_container);
            }
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
                    populateUnifiedNativeAdView2(activity, nativeAd, nativeAdContainer);

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
                    populateUnifiedNativeAdView1(activity, nativeAd, nativeAdContainer);

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
}

