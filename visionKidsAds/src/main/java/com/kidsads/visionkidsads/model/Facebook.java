package com.kidsads.visionkidsads.model;

import androidx.annotation.Keep;

import com.google.gson.annotations.SerializedName;
@Keep
public class Facebook {

    @SerializedName("banner")
    private String banner;

    @SerializedName("nativeAdvanced")
    private String nativeAdvanced;

    @SerializedName("interstitial")
    private String interstitial;

    @SerializedName("rewarded")
    private String rewarded;

    public String getRewarded() {
        return rewarded;
    }


    public String getBanner() {
        return banner;
    }

    public String getNativeAdvanced() {
        return nativeAdvanced;
    }

    public String getInterstitial() {
        return interstitial;
    }


}