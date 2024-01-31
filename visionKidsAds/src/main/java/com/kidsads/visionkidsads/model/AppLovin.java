package com.kidsads.visionkidsads.model;

import androidx.annotation.Keep;

import com.google.gson.annotations.SerializedName;
@Keep
public class AppLovin {

    @SerializedName("rewarded")
    private String rewarded;
    @SerializedName("banner")
    private String banner;
    @SerializedName("appOpen")
    private String appOpen;
    @SerializedName("nativeAdvanced")
    private String nativeAdvanced;
    @SerializedName("interstitial")
    private String interstitial;

    public String getRewarded() {
        return rewarded;
    }

    public String getBanner() {
        return banner;
    }

    public String getAppOpen() {
        return appOpen;
    }

    public String getNativeAdvanced() {
        return nativeAdvanced;
    }

    public String getInterstitial() {
        return interstitial;
    }
}