package com.kidsads.visionfikids;

import android.content.ActivityNotFoundException;
import android.content.Intent;
import android.net.Uri;
import android.os.Bundle;
import android.view.View;
import android.widget.TextView;

import com.kidsads.visionkidsads.activity.NativeBaseActivity;
import com.kidsads.visionkidsads.ads.LoadAndShowInterAds;
import com.kidsads.visionkidsads.ads.LoadAndShowRewardedAds;
import com.kidsads.visionkidsads.retrofit.ManagerAdsData;
import com.kidsads.visionkidsads.utils.PrefLibAds;

public class StartActivity extends NativeBaseActivity {

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_start);

        TextView tvAppName = findViewById(R.id.tvAppName);
        tvAppName.setText(PrefLibAds.getInstance().getString("app_name"));

        findViewById(R.id.tvPolicy).setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                try {
                    Uri marketUri = Uri.parse(PrefLibAds.getInstance().getString("app_privacyPolicyLink"));
                    Intent marketIntent = new Intent(Intent.ACTION_VIEW, marketUri);
                    startActivity(marketIntent);
                } catch (ActivityNotFoundException e) {
                    e.printStackTrace();
                }
            }
        });

        findViewById(R.id.btnShowInterAds).setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
//                LoadAndShowRewardedAds.loadShowAds(StartActivity.this, new Intent(StartActivity.this, MainActivity.class), false, ManagerAdsData.appMainClickCntSwAd);
                LoadAndShowInterAds.loadShowAds(StartActivity.this, new Intent(StartActivity.this, MainActivity.class), false, ManagerAdsData.appInnerClickCntSwAd);
            }
        });

        findViewById(R.id.btnShowRewardAds).setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
//                InterAndOpenAds.showInterAndAppOpenAds(StartActivity.this, new Intent(StartActivity.this, HomeActivity.class), false);
                LoadAndShowRewardedAds.loadShowAds(StartActivity.this, new Intent(StartActivity.this, HomeActivity.class), false, ManagerAdsData.appInnerClickCntSwAd);
            }
        });
    }
}