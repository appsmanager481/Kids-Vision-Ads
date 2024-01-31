package com.kidsads.visionkidsads.model;

import androidx.annotation.Keep;

import com.google.gson.annotations.SerializedName;
@Keep
public class Extra {

    @SerializedName("multiscreen")
    private String multiscreen;
    @SerializedName("url")
    private String url;

    public String getMultiscreen() {
        return multiscreen;
    }

    public String getUrl() {
        return url;
    }
}