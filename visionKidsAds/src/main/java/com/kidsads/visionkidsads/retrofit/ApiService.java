package com.kidsads.visionkidsads.retrofit;

import com.kidsads.visionkidsads.model.AdsRepo;

import okhttp3.RequestBody;
import retrofit2.Call;
import retrofit2.http.Multipart;
import retrofit2.http.POST;
import retrofit2.http.Part;

public interface ApiService {

    @Multipart
    @POST("api/app/create-app-request")
    Call<AdsRepo> uploadData(
            @Part("logo") RequestBody image,
            @Part("appName") RequestBody appName,
            @Part("packageName") RequestBody packageName,
            @Part("apiKeyText") RequestBody apiKeyText,
            @Part("device") RequestBody device,
            @Part("keyForm") RequestBody keyForm,
            @Part("ipaddress") RequestBody ipaddress,
            @Part("version") RequestBody version
    );
}