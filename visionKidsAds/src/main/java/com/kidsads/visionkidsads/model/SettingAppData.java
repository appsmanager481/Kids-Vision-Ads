package com.kidsads.visionkidsads.model;

import androidx.annotation.Keep;

import com.google.gson.annotations.SerializedName;

import java.util.List;
@Keep
public class SettingAppData {

    @SerializedName("adPlatformSequenceForNative")
    private String adPlatformSequenceForNative;

    @SerializedName("redirectToOtherApp")
    private boolean redirectToOtherApp;

    @SerializedName("appMoreFieldBannerPreLoad")
    private boolean appMoreFieldBannerPreLoad;

    @SerializedName("getMoreAppGroups")
    private List<GetMoreAppGroups> getMoreAppGroups;

    @SerializedName("appMoreFieldNativeAdSize")
    private String appMoreFieldNativeAdSize;

    @SerializedName("howToShowAd")
    private String howToShowAd;

    @SerializedName("howToShowAdInterstitialAndOthers")
    private String howToShowAdInterstitialAndOthers;

    @SerializedName("howToShowAdNative")
    private String howToShowAdNative;

    @SerializedName("howToShowAdBannerNativeBanner")
    private String howToShowAdBannerNativeBanner;

    @SerializedName("splashAdType")
    private String splashAdType;

    @SerializedName("adPlatformSequenceForBannerNativeBanner")
    private String adPlatformSequenceForBannerNativeBanner;

    @SerializedName("appMode")
    private String appMode;

    @SerializedName("appMoreFieldNativePreLoad")
    private boolean appMoreFieldNativePreLoad;

    @SerializedName("adsOnEveryclick")
    private int adsOnEveryclick;

    @SerializedName("adPlatformSequenceForInterstitialAndOther")
    private String adPlatformSequenceForInterstitialAndOther;

    @SerializedName("splashAd")
    private boolean splashAd;

    @SerializedName("versionUpdateDialog")
    private boolean versionUpdateDialog;

    @SerializedName("adPlatformSequence")
    private String adPlatformSequence;

    @SerializedName("packageName")
    private String packageName;


    @SerializedName("adApply")
    private String adApply;

    @SerializedName("appMoreFieldAdsButtonTextColor")
    private String appMoreFieldAdsButtonTextColor;

    @SerializedName("oneSignalAppId")
    private String oneSignalAppId;

    @SerializedName("appMoreFieldBannerAdPlaceholder")
    private boolean appMoreFieldBannerAdPlaceholder;

    @SerializedName("appMoreFieldBackgroundAppOpenAd")
    private boolean appMoreFieldBackgroundAppOpenAd;

    @SerializedName("appMoreFieldAdsButtonColor")
    private String appMoreFieldAdsButtonColor;

    @SerializedName("appMoreFieldAdsTextColor")
    private String appMoreFieldAdsTextColor;

    @SerializedName("appMoreFieldNativeAdPlaceholder")
    private boolean appMoreFieldNativeAdPlaceholder;

    @SerializedName("appName")
    private String appName;

    @SerializedName("showAdInApp")
    private boolean showAdInApp;

    @SerializedName("appMoreFieldAdPlaceholderText")
    private String appMoreFieldAdPlaceholderText;


    @SerializedName("backPressAdType")
    private String backPressAdType;

    @SerializedName("backPressAd")
    private boolean backPressAd;

    @SerializedName("mainPageAdClickCount")
    private int mainPageAdClickCount;

    @SerializedName("loadAdIds")
    private String loadAdIds;

    @SerializedName("needInternetCompulsory")
    private boolean needInternetCompulsory;

    @SerializedName("appStatus")
    private String appStatus;

    @SerializedName("version")
    private String version;

    @SerializedName("locationWiseReport")
    private boolean locationWiseReport;

    @SerializedName("appMoreFieldInnerMoreAppDataAd")
    private boolean appMoreFieldInnerMoreAppDataAd;

    @SerializedName("innerPageAdClickCount")
    private int innerPageAdClickCount;

    @SerializedName("getCustomAds")
    private List<CustomAds> getCustomAds;

    @SerializedName("appMoreFieldAdsBackgroundColor")
    private String appMoreFieldAdsBackgroundColor;

    @SerializedName("showDialogBeforeAdShow")
    private boolean showDialogBeforeAdShow;

    @SerializedName("placement")
    private Placement placement;

    @SerializedName("showTestAdInDebugApp")
    private boolean showTestAdInDebugApp;

    @SerializedName("appPrivacyPolicyLink")
    private String appPrivacyPolicyLink;

    @SerializedName("redirectToOtherAppText")
    private String redirectToOtherAppText;

    @SerializedName("versionUpdateDialogText")
    private String versionUpdateDialogText;
    @SerializedName("appExtraData")
    private Extra appExtraData;

    @SerializedName("moreApp")
    private boolean moreApp;

    public boolean getMoreApp() {
        return moreApp;
    }

    public String getHowToShowAdInterstitialAndOthers() {
        return howToShowAdInterstitialAndOthers;
    }

    public String getHowToShowAdNative() {
        return howToShowAdNative;
    }

    public String getHowToShowAdBannerNativeBanner() {
        return howToShowAdBannerNativeBanner;
    }

    public String getAppMoreFieldAdsTextColor() {
        return appMoreFieldAdsTextColor;
    }


    public String getAdApply() {
        return adApply;
    }

    public Extra getAppExtraData() {
        return appExtraData;
    }

    public String getSplashAdType() {
        return splashAdType;
    }

    public String getAppPrivacyPolicyLink() {
        return appPrivacyPolicyLink;
    }

    public String getBackPressAdType() {
        return backPressAdType;
    }

    public String getRedirectToOtherAppText() {
        return redirectToOtherAppText;
    }

    public String getHowToShowAd() {
        return howToShowAd;
    }

    public String getVersionUpdateDialogText() {
        return versionUpdateDialogText;
    }

    public String getAdPlatformSequenceForNative() {
        return adPlatformSequenceForNative;
    }

    public void setAdPlatformSequenceForNative(String adPlatformSequenceForNative) {
        this.adPlatformSequenceForNative = adPlatformSequenceForNative;
    }

    public boolean isRedirectToOtherApp() {
        return redirectToOtherApp;
    }

    public void setRedirectToOtherApp(boolean redirectToOtherApp) {
        this.redirectToOtherApp = redirectToOtherApp;
    }

    public boolean isAppMoreFieldBannerPreLoad() {
        return appMoreFieldBannerPreLoad;
    }

    public void setAppMoreFieldBannerPreLoad(boolean appMoreFieldBannerPreLoad) {
        this.appMoreFieldBannerPreLoad = appMoreFieldBannerPreLoad;
    }

    public List<GetMoreAppGroups> getGetMoreAppGroups() {
        return getMoreAppGroups;
    }

    public void setGetMoreAppGroups(List<GetMoreAppGroups> getMoreAppGroups) {
        this.getMoreAppGroups = getMoreAppGroups;
    }

    public String getAppMoreFieldNativeAdSize() {
        return appMoreFieldNativeAdSize;
    }

    public void setAppMoreFieldNativeAdSize(String appMoreFieldNativeAdSize) {
        this.appMoreFieldNativeAdSize = appMoreFieldNativeAdSize;
    }

    public String getAdPlatformSequenceForBannerNativeBanner() {
        return adPlatformSequenceForBannerNativeBanner;
    }

    public void setAdPlatformSequenceForBannerNativeBanner(String adPlatformSequenceForBannerNativeBanner) {
        this.adPlatformSequenceForBannerNativeBanner = adPlatformSequenceForBannerNativeBanner;
    }

    public String getAppMode() {
        return appMode;
    }

    public void setAppMode(String appMode) {
        this.appMode = appMode;
    }

    public boolean isAppMoreFieldNativePreLoad() {
        return appMoreFieldNativePreLoad;
    }

    public void setAppMoreFieldNativePreLoad(boolean appMoreFieldNativePreLoad) {
        this.appMoreFieldNativePreLoad = appMoreFieldNativePreLoad;
    }

    public int getAdsOnEveryclick() {
        return adsOnEveryclick;
    }

    public void setAdsOnEveryclick(int adsOnEveryclick) {
        this.adsOnEveryclick = adsOnEveryclick;
    }

    public String getAdPlatformSequenceForInterstitialAndOther() {
        return adPlatformSequenceForInterstitialAndOther;
    }

    public void setAdPlatformSequenceForInterstitialAndOther(String adPlatformSequenceForInterstitialAndOther) {
        this.adPlatformSequenceForInterstitialAndOther = adPlatformSequenceForInterstitialAndOther;
    }

    public boolean isSplashAd() {
        return splashAd;
    }

    public void setSplashAd(boolean splashAd) {
        this.splashAd = splashAd;
    }

    public boolean isVersionUpdateDialog() {
        return versionUpdateDialog;
    }

    public void setVersionUpdateDialog(boolean versionUpdateDialog) {
        this.versionUpdateDialog = versionUpdateDialog;
    }

    public String getAdPlatformSequence() {
        return adPlatformSequence;
    }

    public void setAdPlatformSequence(String adPlatformSequence) {
        this.adPlatformSequence = adPlatformSequence;
    }

    public String getPackageName() {
        return packageName;
    }

    public void setPackageName(String packageName) {
        this.packageName = packageName;
    }

    public String getAppMoreFieldAdsButtonTextColor() {
        return appMoreFieldAdsButtonTextColor;
    }

    public void setAppMoreFieldAdsButtonTextColor(String appMoreFieldAdsButtonTextColor) {
        this.appMoreFieldAdsButtonTextColor = appMoreFieldAdsButtonTextColor;
    }

    public String getOneSignalAppId() {
        return oneSignalAppId;
    }

    public void setOneSignalAppId(String oneSignalAppId) {
        this.oneSignalAppId = oneSignalAppId;
    }

    public boolean isAppMoreFieldBannerAdPlaceholder() {
        return appMoreFieldBannerAdPlaceholder;
    }

    public void setAppMoreFieldBannerAdPlaceholder(boolean appMoreFieldBannerAdPlaceholder) {
        this.appMoreFieldBannerAdPlaceholder = appMoreFieldBannerAdPlaceholder;
    }

    public boolean isAppMoreFieldBackgroundAppOpenAd() {
        return appMoreFieldBackgroundAppOpenAd;
    }

    public void setAppMoreFieldBackgroundAppOpenAd(boolean appMoreFieldBackgroundAppOpenAd) {
        this.appMoreFieldBackgroundAppOpenAd = appMoreFieldBackgroundAppOpenAd;
    }

    public String getAppMoreFieldAdsButtonColor() {
        return appMoreFieldAdsButtonColor;
    }

    public void setAppMoreFieldAdsButtonColor(String appMoreFieldAdsButtonColor) {
        this.appMoreFieldAdsButtonColor = appMoreFieldAdsButtonColor;
    }

    public boolean isAppMoreFieldNativeAdPlaceholder() {
        return appMoreFieldNativeAdPlaceholder;
    }

    public void setAppMoreFieldNativeAdPlaceholder(boolean appMoreFieldNativeAdPlaceholder) {
        this.appMoreFieldNativeAdPlaceholder = appMoreFieldNativeAdPlaceholder;
    }

    public String getAppName() {
        return appName;
    }

    public void setAppName(String appName) {
        this.appName = appName;
    }

    public boolean isShowAdInApp() {
        return showAdInApp;
    }

    public void setShowAdInApp(boolean showAdInApp) {
        this.showAdInApp = showAdInApp;
    }

    public String getAppMoreFieldAdPlaceholderText() {
        return appMoreFieldAdPlaceholderText;
    }

    public void setAppMoreFieldAdPlaceholderText(String appMoreFieldAdPlaceholderText) {
        this.appMoreFieldAdPlaceholderText = appMoreFieldAdPlaceholderText;
    }

    public boolean isBackPressAd() {
        return backPressAd;
    }

    public void setBackPressAd(boolean backPressAd) {
        this.backPressAd = backPressAd;
    }

    public int getMainPageAdClickCount() {
        return mainPageAdClickCount;
    }

    public void setMainPageAdClickCount(int mainPageAdClickCount) {
        this.mainPageAdClickCount = mainPageAdClickCount;
    }

    public String getLoadAdIds() {
        return loadAdIds;
    }

    public void setLoadAdIds(String loadAdIds) {
        this.loadAdIds = loadAdIds;
    }

    public boolean isNeedInternetCompulsory() {
        return needInternetCompulsory;
    }

    public void setNeedInternetCompulsory(boolean needInternetCompulsory) {
        this.needInternetCompulsory = needInternetCompulsory;
    }

    public String getAppStatus() {
        return appStatus;
    }

    public void setAppStatus(String appStatus) {
        this.appStatus = appStatus;
    }

    public String getVersion() {
        return version;
    }

    public void setVersion(String version) {
        this.version = version;
    }

    public boolean isLocationWiseReport() {
        return locationWiseReport;
    }

    public void setLocationWiseReport(boolean locationWiseReport) {
        this.locationWiseReport = locationWiseReport;
    }

    public boolean isAppMoreFieldInnerMoreAppDataAd() {
        return appMoreFieldInnerMoreAppDataAd;
    }

    public void setAppMoreFieldInnerMoreAppDataAd(boolean appMoreFieldInnerMoreAppDataAd) {
        this.appMoreFieldInnerMoreAppDataAd = appMoreFieldInnerMoreAppDataAd;
    }

    public int getInnerPageAdClickCount() {
        return innerPageAdClickCount;
    }

    public void setInnerPageAdClickCount(int innerPageAdClickCount) {
        this.innerPageAdClickCount = innerPageAdClickCount;
    }

    public List<CustomAds> getGetCustomAds() {
        return getCustomAds;
    }

    public void setGetCustomAds(List<CustomAds> getCustomAds) {
        this.getCustomAds = getCustomAds;
    }

    public String getAppMoreFieldAdsBackgroundColor() {
        return appMoreFieldAdsBackgroundColor;
    }

    public void setAppMoreFieldAdsBackgroundColor(String appMoreFieldAdsBackgroundColor) {
        this.appMoreFieldAdsBackgroundColor = appMoreFieldAdsBackgroundColor;
    }

    public boolean isShowDialogBeforeAdShow() {
        return showDialogBeforeAdShow;
    }

    public void setShowDialogBeforeAdShow(boolean showDialogBeforeAdShow) {
        this.showDialogBeforeAdShow = showDialogBeforeAdShow;
    }

    public Placement getPlacement() {
        return placement;
    }

    public void setPlacement(Placement placement) {
        this.placement = placement;
    }

    public boolean isShowTestAdInDebugApp() {
        return showTestAdInDebugApp;
    }

    public void setShowTestAdInDebugApp(boolean showTestAdInDebugApp) {
        this.showTestAdInDebugApp = showTestAdInDebugApp;
    }
}