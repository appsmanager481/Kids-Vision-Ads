package com.kidsads.visionkidsads.activity;

import android.annotation.SuppressLint;
import android.content.Intent;
import android.content.res.ColorStateList;
import android.graphics.Color;
import android.net.Uri;
import android.os.Bundle;
import android.os.CountDownTimer;
import android.view.View;
import android.view.animation.Animation;
import android.view.animation.AnimationUtils;
import android.view.animation.TranslateAnimation;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.TextView;

import androidx.appcompat.app.AppCompatActivity;

import com.kidsads.visionkidsads.R;
import com.kidsads.visionkidsads.model.CustomAds;
import com.kidsads.visionkidsads.utils.PrefLibAds;
import com.bumptech.glide.Glide;
import com.daimajia.androidanimations.library.Techniques;
import com.daimajia.androidanimations.library.YoYo;

import java.util.List;

public class CustomRewardedActivity extends AppCompatActivity {

    Intent passedIntent;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_custom_rewarded);

        if (getIntent().hasExtra("passedIntent")) {
            passedIntent = getIntent().getParcelableExtra("passedIntent");
        }

        ImageView ivMedia = findViewById(R.id.ivMedia);
        Button btnRedirect = findViewById(R.id.btnRedirect);
        ImageView ivClose = findViewById(R.id.ivClose);
        LinearLayout llAds = findViewById(R.id.llAds);
        TextView tvTimer = findViewById(R.id.tvTimer);
        ivClose.setEnabled(false);
        new CountDownTimer(15000, 1000) {
            @Override
            public void onTick(long millisUntilFinished) {
                int sec = (int) (millisUntilFinished / 1000);
                if (sec < 10) {
                    ivClose.setEnabled(true);
                    ivClose.setColorFilter(getResources().getColor(R.color.black));
                }
                if (sec == 0) {
                    tvTimer.setText("Rewarded Done");
                } else {
                    tvTimer.setText(sec + " Seconds Remaining");
                }
            }

            @Override
            public void onFinish() {
                tvTimer.setText("Rewarded Done");
            }

        }.start();

        SlideToTop(ivClose, 1000);
        SlideToTop(llAds, 1000);
        FadeIn(btnRedirect);

        List<CustomAds> customAds = PrefLibAds.getCustomAdsData(this);
        if (customAds != null && !customAds.isEmpty()) {
            for (int i = 0; i < customAds.size(); i++) {
                if (customAds.get(i).getFormatName().equals("interstitial")) {
                    Glide.with(CustomRewardedActivity.this).load(customAds.get(i).getAssetsBanner()).into(ivMedia);
                    btnRedirect.setText(customAds.get(i).getActionButtonName());

                    int finalI = i;
                    btnRedirect.setOnClickListener(new View.OnClickListener() {
                        @Override
                        public void onClick(View v) {
                            Intent httpIntent = new Intent(Intent.ACTION_VIEW);
                            httpIntent.setData(Uri.parse(customAds.get(finalI).getAssetsUrl()));
                            startActivity(httpIntent);
                        }
                    });
                }
            }
        } else {
            closeAd();
        }

        YoYo.with(Techniques.Flash).duration(2000).repeat(1000).playOn(btnRedirect);

        if (!PrefLibAds.getInstance().getString("appAdsButtonColor").equals("")) {
            btnRedirect.setBackgroundTintList(ColorStateList.valueOf(Color.parseColor(PrefLibAds.getInstance().getString("appAdsButtonColor"))));
        } else {
            btnRedirect.setBackgroundTintList(ColorStateList.valueOf(Color.parseColor("#0C6EFC")));
        }
        if (!PrefLibAds.getInstance().getString("appAdsButtonTextColor").equals("")) {
            btnRedirect.setTextColor(ColorStateList.valueOf(Color.parseColor(PrefLibAds.getInstance().getString("appAdsButtonTextColor"))));
        } else {
            btnRedirect.setTextColor(ColorStateList.valueOf(Color.parseColor("#FFFFFF")));
        }
        ivClose.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                closeAd();
            }
        });
    }

    public void closeAd() {
        if (passedIntent != null) {
            startActivity(passedIntent);
        }
        finish();
    }

    public void SlideToTop(final View view, int i) {
        TranslateAnimation translateAnimation = new TranslateAnimation(1, 0.0f, 1, 0.0f, 1, -2.0f, 1, 0.0f);
        translateAnimation.setDuration(i);
        translateAnimation.setFillAfter(true);
        translateAnimation.setAnimationListener(new Animation.AnimationListener() {
            public void onAnimationEnd(Animation animation) {
            }

            public void onAnimationRepeat(Animation animation) {
            }

            @SuppressLint("WrongConstant")
            public void onAnimationStart(Animation animation) {
                view.setVisibility(0);
            }
        });
        translateAnimation.setFillEnabled(true);
        view.startAnimation(translateAnimation);
    }

    public void FadeIn(final View view) {
        Animation loadAnimation = AnimationUtils.loadAnimation(this, R.anim.fade_in);
        view.startAnimation(loadAnimation);
        loadAnimation.setAnimationListener(new Animation.AnimationListener() {
            public void onAnimationRepeat(Animation animation) {
            }

            public void onAnimationStart(Animation animation) {
            }

            @SuppressLint("WrongConstant")
            public void onAnimationEnd(Animation animation) {
                view.setVisibility(0);
            }
        });
    }
}