package com.kidsads.visionfikids;

import android.content.Intent;
import android.os.Bundle;
import android.view.View;

import androidx.appcompat.app.AppCompatActivity;

import com.kidsads.visionkidsads.ads.LoadAndShowRewardedAds;
import com.kidsads.visionkidsads.retrofit.ManagerAdsData;

public class HomeActivity extends AppCompatActivity {

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_home);

        findViewById(R.id.ivBack).setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                finish();
            }
        });


        findViewById(R.id.btnNext).setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                LoadAndShowRewardedAds.loadShowAds(HomeActivity.this, new Intent(HomeActivity.this, MediumActivity.class), false, ManagerAdsData.appMainClickCntSwAd);
            }
        });
    }
}