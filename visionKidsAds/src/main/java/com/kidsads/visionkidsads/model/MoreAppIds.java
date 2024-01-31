package com.kidsads.visionkidsads.model;

import androidx.annotation.Keep;

import com.google.gson.annotations.SerializedName;
@Keep
public class MoreAppIds {

    @SerializedName("appLogo")
    private String appLogo;

    @SerializedName("appName")
    private String appName;

    @SerializedName("packageName")
    private String packageName;

    public String getAppLogo() {
        return appLogo;
    }

    public String getAppName() {
        return appName;
    }

    public String getPackageName() {
        return packageName;
    }
}