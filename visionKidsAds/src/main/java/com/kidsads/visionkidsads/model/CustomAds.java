package com.kidsads.visionkidsads.model;

import androidx.annotation.Keep;

import com.google.gson.annotations.SerializedName;
@Keep
public class CustomAds {

    @SerializedName("actionButtonName")
    private String actionButtonName;

    @SerializedName("assetsBanner")
    private String assetsBanner;

    @SerializedName("assetsName")
    private String assetsName;

    @SerializedName("formatName")
    private String formatName;

    @SerializedName("assetsRating")
    private int assetsRating;

    @SerializedName("assetsDownload")
    private String assetsDownload;

    @SerializedName("assetsUrl")
    private String assetsUrl;

    public String getActionButtonName() {
        return actionButtonName;
    }

    public String getAssetsBanner() {
        return assetsBanner;
    }

    public String getAssetsName() {
        return assetsName;
    }

    public String getFormatName() {
        return formatName;
    }

    public int getAssetsRating() {
        return assetsRating;
    }

    public String getAssetsDownload() {
        return assetsDownload;
    }

    public String getAssetsUrl() {
        return assetsUrl;
    }
}