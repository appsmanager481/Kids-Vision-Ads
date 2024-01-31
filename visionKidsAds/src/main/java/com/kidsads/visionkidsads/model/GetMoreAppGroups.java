package com.kidsads.visionkidsads.model;

import androidx.annotation.Keep;

import com.google.gson.annotations.SerializedName;

import java.util.List;
@Keep
public class GetMoreAppGroups {

    @SerializedName("innerAppIds")
    private List<MoreAppIds> innerAppIds;

    @SerializedName("splashMoreAppIds")
    private List<MoreAppIds> splashMoreAppIds;

    @SerializedName("exitMoreAppIds")
    private List<MoreAppIds> exitMoreAppIds;

    @SerializedName("assignAppIds")
    private List<MoreAppIds> assignAppIds;

    public List<MoreAppIds> getInnerAppIds() {
        return innerAppIds;
    }

    public List<MoreAppIds> getSplashMoreAppIds() {
        return splashMoreAppIds;
    }

    public List<MoreAppIds> getExitMoreAppIds() {
        return exitMoreAppIds;
    }

    public List<MoreAppIds> getAssignAppIds() {
        return assignAppIds;
    }
}