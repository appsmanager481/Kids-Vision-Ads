package com.kidsads.visionkidsads.utils;

import android.app.Activity;
import android.content.Context;
import android.content.SharedPreferences;

import com.kidsads.visionkidsads.model.CustomAds;
import com.kidsads.visionkidsads.model.GetMoreAppGroups;
import com.kidsads.visionkidsads.model.MoreAppIds;
import com.google.gson.Gson;
import com.google.gson.reflect.TypeToken;

import java.lang.reflect.Type;
import java.util.List;

public class PrefLibAds {
    private static final String PREF_NAME = "GetMoreAppGroupsData";
    private static final String KEY_APP_DATA = "MoreDataApp";
    private volatile static PrefLibAds mInstance;
    private SharedPreferences appSharedPrefs;
    private SharedPreferences.Editor prefsEditor;

    public PrefLibAds(Context context) {
        appSharedPrefs = context.getSharedPreferences("sdfa_ASGrrg_gasvacefa", Activity.MODE_PRIVATE);
        prefsEditor = appSharedPrefs.edit();
    }

    public PrefLibAds() {
    }

    public static PrefLibAds getInstance() {
        if (null == mInstance) {
            synchronized (PrefLibAds.class) {
                if (null == mInstance) {
                    mInstance = new PrefLibAds();
                }
            }
        }
        return mInstance;
    }

    public static void setMoreAppData(Context context, List<GetMoreAppGroups> appInfoList) {
        SharedPreferences preferences = context.getSharedPreferences(PREF_NAME, Context.MODE_PRIVATE);
        SharedPreferences.Editor editor = preferences.edit();

        // Convert the list of objects to a JSON string
        Gson gson = new Gson();
        String jsonData = gson.toJson(appInfoList);

        editor.putString(KEY_APP_DATA, jsonData);
        editor.apply();
    }

    public static List<GetMoreAppGroups> getMoreAppData(Context context) {
        SharedPreferences preferences = context.getSharedPreferences(PREF_NAME, Context.MODE_PRIVATE);

        // Retrieve the JSON string and convert it back to a list of objects
        String jsonData = preferences.getString(KEY_APP_DATA, null);
        if (jsonData != null) {
            Gson gson = new Gson();
            Type type = new TypeToken<List<GetMoreAppGroups>>() {
            }.getType();
            return gson.fromJson(jsonData, type);
        }
        return null;
    }

    public static void setAssignAppData(Context context, List<MoreAppIds> appInfoList) {
        SharedPreferences preferences = context.getSharedPreferences("assign_ap_darta", Context.MODE_PRIVATE);
        SharedPreferences.Editor editor = preferences.edit();

        // Convert the list of objects to a JSON string
        Gson gson = new Gson();
        String jsonData = gson.toJson(appInfoList);

        editor.putString("assign_item", jsonData);
        editor.apply();
    }

    public static List<MoreAppIds> getAssignAppData(Context context) {
        SharedPreferences preferences = context.getSharedPreferences("assign_ap_darta", Context.MODE_PRIVATE);

        // Retrieve the JSON string and convert it back to a list of objects
        String jsonData = preferences.getString("assign_item", null);
        if (jsonData != null) {
            Gson gson = new Gson();
            Type type = new TypeToken<List<MoreAppIds>>() {
            }.getType();
            return gson.fromJson(jsonData, type);
        }
        return null;
    }

    public static void setCustomAdsData(Context context, List<CustomAds> appInfoList) {
        SharedPreferences preferences = context.getSharedPreferences("custom_app_data", Context.MODE_PRIVATE);
        SharedPreferences.Editor editor = preferences.edit();

        // Convert the list of objects to a JSON string
        if (appInfoList != null) {
            Gson gson = new Gson();
            String jsonData = gson.toJson(appInfoList);
            editor.putString("custom_key", jsonData);
            editor.apply();
        }
    }

    public static List<CustomAds> getCustomAdsData(Context context) {
        SharedPreferences preferences = context.getSharedPreferences("custom_app_data", Context.MODE_PRIVATE);

        // Retrieve the JSON string and convert it back to a list of objects
        String jsonData = preferences.getString("custom_key", null);
        if (jsonData != null) {
            Gson gson = new Gson();
            Type type = new TypeToken<List<CustomAds>>() {
            }.getType();
            return gson.fromJson(jsonData, type);
        }

        return null;
    }

    public void init(Context context) {
        if (context == null) {
            appSharedPrefs = context.getSharedPreferences("sdfa_ASGrrg_gasvacefa", Activity.MODE_PRIVATE);
            prefsEditor = appSharedPrefs.edit();
        }

        if (appSharedPrefs == null) {
            appSharedPrefs = context.getSharedPreferences("sdfa_ASGrrg_gasvacefa", Activity.MODE_PRIVATE);
            prefsEditor = appSharedPrefs.edit();
        }
    }

    public boolean checkPreferenceSet(String key_value) {
        return appSharedPrefs.contains(key_value);
    }

    public boolean getBool(String key_value, boolean default_value) {
        return appSharedPrefs.getBoolean(key_value, default_value);
    }

    public void setBool(String key_value, boolean default_value) {
        prefsEditor.putBoolean(key_value, default_value).commit();
    }

    public int getInt(String key_value) {
        return appSharedPrefs.getInt(key_value, 0);
    }

    public void setInt(String key_value, int default_value) {
        prefsEditor.putInt(key_value, default_value).commit();
    }

    public String getString(String key_value, String default_value) {
        return appSharedPrefs.getString(key_value, default_value);
    }

    public String getString(String key_value) {
        return appSharedPrefs.getString(key_value, "");
    }

    public void setString(String key_value, String default_value) {
        prefsEditor.putString(key_value, default_value).commit();
    }

    public long getLong(String key_value) {
        return appSharedPrefs.getLong(key_value, -1);
    }

    public void setLong(String key_value, Long default_value) {
        prefsEditor.putLong(key_value, default_value).commit();
    }
}
