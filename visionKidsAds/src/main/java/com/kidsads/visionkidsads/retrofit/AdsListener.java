package com.kidsads.visionkidsads.retrofit;

public interface AdsListener {

    void onSuccess();

    void onRedirect(String url);

    void onReload();
}
