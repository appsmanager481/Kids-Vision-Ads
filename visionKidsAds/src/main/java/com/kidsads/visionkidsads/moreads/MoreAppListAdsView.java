package com.kidsads.visionkidsads.moreads;

import android.annotation.SuppressLint;
import android.app.Activity;
import android.content.Context;
import android.content.res.ColorStateList;
import android.content.res.TypedArray;
import android.graphics.Color;
import android.net.ConnectivityManager;
import android.net.NetworkInfo;
import android.os.Handler;
import android.util.AttributeSet;
import android.view.View;
import android.widget.LinearLayout;

import androidx.cardview.widget.CardView;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import com.kidsads.visionkidsads.R;
import com.kidsads.visionkidsads.adapter.MoreAppAdapter;
import com.kidsads.visionkidsads.model.GetMoreAppGroups;
import com.kidsads.visionkidsads.model.MoreAppIds;
import com.kidsads.visionkidsads.utils.PrefLibAds;
import com.facebook.shimmer.ShimmerFrameLayout;

import java.util.ArrayList;
import java.util.List;

public class MoreAppListAdsView extends LinearLayout {

    private static final String TAG = MoreAppListAdsView.class.getSimpleName();
    static int scrollCount = 0;
    private static List<MoreAppIds> moreAppIds;
    private static ArrayList<MoreAppIds> moreItemArrayList;
    private int layoutItem;
    private int customAttribute;
    private int layoutSize;

    public MoreAppListAdsView(Context context) {
        this(context, null);
    }

    public MoreAppListAdsView(Context context, AttributeSet attrs) {
        this(context, attrs, 0);
    }

    @SuppressLint("CustomViewStyleable")
    public MoreAppListAdsView(Context context, AttributeSet attrs, int defStyle) {
        super(context, attrs, defStyle);

        TypedArray typedArray = context.obtainStyledAttributes(attrs, R.styleable.MoreAppAdsView);
        customAttribute = typedArray.getInt(R.styleable.MoreAppAdsView_customAttribute, 0);
        layoutSize = typedArray.getInt(R.styleable.MoreAppAdsView_layoutSize, 0);
        typedArray.recycle();

        if (!hasActiveInternetConnection(context)) {
            return;
        }

        if (PrefLibAds.getInstance().getString("app_adShowStatus").equals("false")) {
            return;
        }

        int layoutItem;
        if (layoutSize == 0) {
            View.inflate(context, R.layout.main_more_big_ads_layout, this);
            layoutItem = R.layout.item_big_more_app_cat;
        } else if (layoutSize == 1) {
            View.inflate(context, R.layout.main_more_small_ads_layout, this);
            layoutItem = R.layout.item_small_more_app_cat;
        } else {
            View.inflate(context, R.layout.main_more_small_ads_layout, this);
            layoutItem = R.layout.item_small_more_app_cat;
        }

        CardView cvCard = findViewById(R.id.cvCard);
        RecyclerView rvCatAds = findViewById(R.id.rvCatAds);
        ShimmerFrameLayout shimmerLoader = findViewById(R.id.shimmer_view_container);
        if (!PrefLibAds.getInstance().getString("appAdsBackgroundColor").equals("")) {
            cvCard.setCardBackgroundColor(ColorStateList.valueOf(Color.parseColor(PrefLibAds.getInstance().getString("appAdsBackgroundColor"))));
        }

        List<MoreAppIds> moreAppIds = getMoreAppIds(customAttribute, cvCard);
        if (moreAppIds != null && !moreAppIds.isEmpty()) {
            cvCard.setVisibility(View.VISIBLE);
            rvCatAds.setVisibility(View.VISIBLE);
            shimmerLoader.setVisibility(View.GONE);
            shimmerLoader.stopShimmer();

            MoreAppAdapter moreAppAdapter = new MoreAppAdapter((Activity) context, moreAppIds, layoutItem);
            autoScrollAnother(moreAppAdapter, rvCatAds);
            rvCatAds.setLayoutManager(new LinearLayoutManager(context, RecyclerView.HORIZONTAL, false));
            rvCatAds.setHasFixedSize(true);
            rvCatAds.setAdapter(moreAppAdapter);
        } else {
            cvCard.setVisibility(View.GONE);
        }
        /*
        if (!PrefLibAds.getMoreAppData(getContext()).isEmpty()) {

        } else {
            cvCard.setVisibility(View.GONE);
        }*/
    }

    public static void autoScrollAnother(MoreAppAdapter categoryAdapter, RecyclerView rvAdsList) {
        scrollCount = 0;
        final Handler handler = new Handler();
        final Runnable runnable = new Runnable() {
            @Override
            public void run() {
                rvAdsList.smoothScrollToPosition((scrollCount++));
                if (scrollCount == categoryAdapter.getItemCount() - 1) {
                    moreItemArrayList.addAll(moreItemArrayList);
                    categoryAdapter.notifyDataSetChanged();
                }
                handler.postDelayed(this, 1000);
            }
        };
        handler.postDelayed(runnable, 1000);
    }

    public static boolean hasActiveInternetConnection(Context context) {
        @SuppressLint("WrongConstant") NetworkInfo activeNetworkInfo = ((ConnectivityManager) context.getSystemService("connectivity")).getActiveNetworkInfo();
        return activeNetworkInfo != null && activeNetworkInfo.isConnected();
    }

    private List<MoreAppIds> getMoreAppIds(int customAttribute, CardView cvCard) {
        List<GetMoreAppGroups> retrievedData = PrefLibAds.getMoreAppData(getContext());
        if (retrievedData != null) {
            for (int i = 0; i < retrievedData.size(); i++) {
                switch (customAttribute) {
                    case 0:
                        return retrievedData.get(i).getInnerAppIds();
                    case 1:
                        return retrievedData.get(i).getSplashMoreAppIds();
                    case 2:
                        return retrievedData.get(i).getExitMoreAppIds();
                    case 3:
                        return retrievedData.get(i).getAssignAppIds();
                }
            }
        } else {
            cvCard.setVisibility(View.GONE);
        }
        return null;
    }

    public int getCustomAttribute() {
        return customAttribute;
    }

    public void setCustomAttribute(int customAttribute) {
        this.customAttribute = customAttribute;
    }

    public int getLayoutSize() {
        return layoutSize;
    }

    public void setLayoutSize(int layoutSize) {
        this.layoutSize = layoutSize;
    }
}


