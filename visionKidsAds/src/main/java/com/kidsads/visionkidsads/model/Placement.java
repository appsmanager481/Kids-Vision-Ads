package com.kidsads.visionkidsads.model;

import androidx.annotation.Keep;

import com.google.gson.annotations.SerializedName;
@Keep
public class Placement {

    @SerializedName("Admob")
    private Admob admob;
    @SerializedName("Facebook")
    private Facebook facebook;

    @SerializedName("Admanager1")
    private Admob1 admanager1;

    @SerializedName("Admanager2")
    private Admob2 admanager2;

    @SerializedName("AppLovin")
    private AppLovin applovin;

    public Admob getAdmob() {
        return admob;
    }

    public Facebook getFacebook() {
        return facebook;
    }

    public Admob1 getAdmanager1() {
        return admanager1;
    }

    public Admob2 getAdmanager2() {
        return admanager2;
    }

    public AppLovin getApplovin() {
        return applovin;
    }
}