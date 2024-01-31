package com.kidsads.visionfikids;

import android.app.Dialog;
import android.content.ActivityNotFoundException;
import android.content.Intent;
import android.content.pm.PackageInfo;
import android.content.pm.PackageManager;
import android.graphics.Color;
import android.graphics.drawable.ColorDrawable;
import android.net.Uri;
import android.os.Build;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.view.Window;
import android.widget.Button;
import android.widget.LinearLayout;
import android.widget.TextView;

import com.kidsads.visionkidsads.activity.AdsMainActivity;
import com.kidsads.visionkidsads.retrofit.AdsListener;
import com.kidsads.visionkidsads.utils.Connectivity;
import com.kidsads.visionkidsads.utils.PrefLibAds;

public class SplashActivity extends AdsMainActivity {

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_splash);

        boolean isFastConnection = Connectivity.isConnectedFast(this);

        if (isFastConnection) {
            ADSInit(this, getString(R.string.app_name), getCurrentVersionCode(), R.drawable.logo, new AdsListener() {
                @Override
                public void onSuccess() {
                    startActivity(new Intent(SplashActivity.this, StartActivity.class));
                    finish();
                }

                @Override
                public void onRedirect(String url) {
                    Log.e("my_log", "onRedirect: " + url);
                    showRedirectDialog(url);
                }

                @Override
                public void onReload() {
                    startActivity(new Intent(SplashActivity.this, SplashActivity.class));
                    finish();
                }
            });
        } else {
            PrefLibAds.getInstance().setString("app_adShowStatus", "false");
            startActivity(new Intent(SplashActivity.this, StartActivity.class));
            finish();
        }
    }

    public void noInternet() {
        final Dialog dialog = new Dialog(SplashActivity.this);
        dialog.setCancelable(false);
        View view = getLayoutInflater().inflate(com.kidsads.visionkidsads.R.layout.dialog_no_internet, null);
        dialog.setContentView(view);
        Window window = dialog.getWindow();
        if (window != null) {
            window.setLayout(
                    LinearLayout.LayoutParams.MATCH_PARENT,
                    LinearLayout.LayoutParams.WRAP_CONTENT
            );
            window.setBackgroundDrawable(new ColorDrawable(Color.TRANSPARENT));
        }
        Button retryButton = view.findViewById(com.kidsads.visionkidsads.R.id.nointrnet);
        dialog.show();
        retryButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                startActivity(new Intent(SplashActivity.this, SplashActivity.class));
                finish();
            }
        });
    }

    public void showRedirectDialog(final String url) {
        final Dialog dialog = new Dialog(SplashActivity.this);
        dialog.setCancelable(false);
        View view = getLayoutInflater().inflate(com.kidsads.visionkidsads.R.layout.dialog_new_install_app, null);
        dialog.setContentView(view);
        TextView update = view.findViewById(com.kidsads.visionkidsads.R.id.update);
        TextView lable = view.findViewById(com.kidsads.visionkidsads.R.id.lable);
        TextView txt_title = view.findViewById(com.kidsads.visionkidsads.R.id.txt_title);
        TextView txt_decription = view.findViewById(com.kidsads.visionkidsads.R.id.txt_decription);

        lable.setText("Install App");
        update.setText("Install Now");
        txt_title.setText("Install our new app now and enjoy");
        txt_decription.setText("We have transferred our server, so install our new app by clicking the button below to enjoy the new features of app.");

        update.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                try {
                    Uri marketUri = Uri.parse(url);
                    Intent marketIntent = new Intent(Intent.ACTION_VIEW, marketUri);
                    startActivity(marketIntent);
                } catch (ActivityNotFoundException e) {
                    e.printStackTrace();
                }
            }
        });

        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.LOLLIPOP) {
            dialog.create();
        }

        dialog.show();
        Window window = dialog.getWindow();
        window.setLayout(LinearLayout.LayoutParams.MATCH_PARENT, LinearLayout.LayoutParams.WRAP_CONTENT);
        window.setBackgroundDrawable(new ColorDrawable(Color.TRANSPARENT));
    }

    public int getCurrentVersionCode() {
        PackageManager manager = getPackageManager();
        PackageInfo info = null;
        try {
            info = manager.getPackageInfo(
                    getPackageName(), 0);
            return info.versionCode;

        } catch (PackageManager.NameNotFoundException e) {
            e.printStackTrace();
        }

        return 0;
    }
}