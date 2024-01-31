package com.kidsads.visionkidsads.activity;

import android.content.DialogInterface;
import android.content.IntentSender;
import android.content.pm.PackageInfo;
import android.content.pm.PackageManager;
import android.os.Bundle;
import android.provider.Settings;
import android.util.Log;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.appcompat.app.AlertDialog;
import androidx.appcompat.app.AppCompatActivity;

import com.kidsads.visionkidsads.utils.PrefLibAds;
import com.google.android.gms.ads.AdRequest;
import com.google.android.play.core.appupdate.AppUpdateInfo;
import com.google.android.play.core.appupdate.AppUpdateManager;
import com.google.android.play.core.appupdate.AppUpdateManagerFactory;
import com.google.android.play.core.install.InstallState;
import com.google.android.play.core.install.InstallStateUpdatedListener;
import com.google.android.play.core.install.model.AppUpdateType;
import com.google.android.play.core.install.model.InstallStatus;
import com.google.android.play.core.install.model.UpdateAvailability;
import com.google.android.play.core.review.ReviewInfo;
import com.google.android.play.core.review.ReviewManager;
import com.google.android.play.core.review.ReviewManagerFactory;
import com.google.android.play.core.tasks.OnCompleteListener;
import com.google.android.play.core.tasks.OnSuccessListener;
import com.google.android.play.core.tasks.Task;
import com.google.android.ump.ConsentDebugSettings;
import com.google.android.ump.ConsentForm;
import com.google.android.ump.ConsentInformation;
import com.google.android.ump.ConsentRequestParameters;
import com.google.android.ump.FormError;
import com.google.android.ump.UserMessagingPlatform;
import com.onesignal.OneSignal;
import com.onesignal.debug.LogLevel;

import java.security.MessageDigest;
import java.security.NoSuchAlgorithmException;

import eu.dkaratzas.android.inapp.update.Constants;
import eu.dkaratzas.android.inapp.update.InAppUpdateManager;
import eu.dkaratzas.android.inapp.update.InAppUpdateStatus;

public class NativeBaseActivity extends AppCompatActivity {

    private static final int REQ_CODE_VERSION_UPDATE = 530;
    private static final int RC_APP_UPDATE = 11;
    public static boolean isSubscribed = false;
    private InAppUpdateManager inAppUpdateManager;
    private AppUpdateManager mAppUpdateManager;
    private ConsentInformation consentInformation;
    private int versionCode = 0;

    @Override
    protected void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

        OneSignal.getDebug().setLogLevel(LogLevel.VERBOSE);
        // OneSignal Initialization
        if (!PrefLibAds.getInstance().getString("oneSignalAppId").equals("")) {
            OneSignal.initWithContext(this, PrefLibAds.getInstance().getString("oneSignalAppId"));
        }

        String android_id = Settings.Secure.getString(getContentResolver(), Settings.Secure.ANDROID_ID);
        String deviceId = md5(android_id).toUpperCase();
        Log.i("device id=", deviceId);

        Log.e("TAG", AdRequest.DEVICE_ID_EMULATOR);
        ConsentDebugSettings debugSettings = new ConsentDebugSettings.Builder(this)
                .setDebugGeography(ConsentDebugSettings.DebugGeography.DEBUG_GEOGRAPHY_EEA)
                .addTestDeviceHashedId(deviceId)
                .build();

        ConsentRequestParameters params = new ConsentRequestParameters
                .Builder()
                .setTagForUnderAgeOfConsent(false)
                .build();

        consentInformation = UserMessagingPlatform.getConsentInformation(this);
        consentInformation.requestConsentInfoUpdate(
                this,
                params,
                new ConsentInformation.OnConsentInfoUpdateSuccessListener() {
                    @Override
                    public void onConsentInfoUpdateSuccess() {
                        loadForm();
                    }
                },
                new ConsentInformation.OnConsentInfoUpdateFailureListener() {
                    @Override
                    public void onConsentInfoUpdateFailure(FormError formError) {
                        // Handle the error.
                    }
                });

        try {
            PackageInfo packageInfo = getPackageManager().getPackageInfo(getPackageName(), 0);
            versionCode = packageInfo.versionCode;

            // Now, 'versionCode' contains the version code of your app module
            // You can use 'versionCode' as needed
        } catch (PackageManager.NameNotFoundException e) {
            e.printStackTrace();
        }

        inAppRating();

        if (PrefLibAds.getInstance().getBool("app_updateAppDialogStatus", false)) {
            inAppUpdating();
        }
    }

    public String md5(String s) {
        try {
            // Create MD5 Hash
            MessageDigest digest = MessageDigest.getInstance("MD5");
            digest.update(s.getBytes());
            byte[] messageDigest = digest.digest();

            // Create Hex String
            StringBuffer hexString = new StringBuffer();
            for (int i = 0; i < messageDigest.length; i++)
                hexString.append(Integer.toHexString(0xFF & messageDigest[i]));
            return hexString.toString();

        } catch (NoSuchAlgorithmException e) {
            e.printStackTrace();
        }
        return "";
    }

    public void loadForm() {
        // Loads a consent form. Must be called on the main thread.

        UserMessagingPlatform.loadConsentForm(
                this,
                new UserMessagingPlatform.OnConsentFormLoadSuccessListener() {
                    @Override
                    public void onConsentFormLoadSuccess(@NonNull ConsentForm consentForm) {

                        if (consentInformation.getConsentStatus() == ConsentInformation.ConsentStatus.REQUIRED) {
                            consentForm.show(
                                    NativeBaseActivity.this,
                                    new ConsentForm.OnConsentFormDismissedListener() {
                                        @Override
                                        public void onConsentFormDismissed(@Nullable FormError formError) {
                                            if (consentInformation.getConsentStatus() == ConsentInformation.ConsentStatus.OBTAINED) {
                                                // App can start requesting ads.
                                            }

                                            // Handle dismissal by reloading form.
                                            loadForm();
                                        }
                                    });
                        }
                    }
                },
                new UserMessagingPlatform.OnConsentFormLoadFailureListener() {
                    @Override
                    public void onConsentFormLoadFailure(@NonNull FormError formError) {
                        // Handle the error.
                    }
                }
        );
    }

    private void inAppRating() {
        final ReviewManager manager = ReviewManagerFactory.create(this);
        Task<ReviewInfo> request = manager.requestReviewFlow();
        request.addOnCompleteListener(new OnCompleteListener<ReviewInfo>() {
            @Override
            public void onComplete(@NonNull Task<ReviewInfo> task) {
                if (task.isSuccessful()) {
                    // We can get the ReviewInfo object
                    ReviewInfo reviewInfo = task.getResult();
                    Task<Void> flow = manager.launchReviewFlow(NativeBaseActivity.this, reviewInfo);
                    flow.addOnCompleteListener(new OnCompleteListener<Void>() {
                        @Override
                        public void onComplete(@NonNull Task<Void> task) {

                        }
                    });
                } else {
                    Log.d("inAppRating", "inAppRating_Else: ");
                }
            }
        });
    }

    private void inAppUpdating() {
        inAppUpdateManager = InAppUpdateManager.Builder(this, REQ_CODE_VERSION_UPDATE)
                .resumeUpdates(true) // Resume the update, if the update was stalled. Default is true
                .mode(Constants.UpdateMode.FLEXIBLE)
                .snackBarMessage("An update has just been downloaded.")
                .snackBarAction("RESTART")
                .handler(new InAppUpdateManager.InAppUpdateHandler() {
                    @Override
                    public void onInAppUpdateError(int code, Throwable error) {

                    }

                    @Override
                    public void onInAppUpdateStatus(InAppUpdateStatus status) {
                        if (status.isDownloaded()) {
                            new AlertDialog.Builder(NativeBaseActivity.this)
                                    .setTitle("Download Done")
                                    .setMessage("An update has just been downloaded.")
                                    .setPositiveButton("INSTALL", new DialogInterface.OnClickListener() {
                                        @Override
                                        public void onClick(DialogInterface dialog, int which) {
                                            if (mAppUpdateManager != null) {
                                                inAppUpdateManager.completeUpdate();
                                            }
                                        }
                                    })
                                    .setNegativeButton("Cancel", new DialogInterface.OnClickListener() {
                                        @Override
                                        public void onClick(DialogInterface dialog, int which) {

                                        }
                                    })
                                    .show();
                        }
                    }
                });

        inAppUpdateManager.checkForAppUpdate();

        mAppUpdateManager = AppUpdateManagerFactory.create(this);

        mAppUpdateManager.registerListener(installStateUpdatedListener);

        mAppUpdateManager.getAppUpdateInfo().addOnSuccessListener(new OnSuccessListener<AppUpdateInfo>() {
            @Override
            public void onSuccess(AppUpdateInfo appUpdateInfo) {
                if (appUpdateInfo.updateAvailability() == UpdateAvailability.UPDATE_AVAILABLE
                        && appUpdateInfo.isUpdateTypeAllowed(AppUpdateType.FLEXIBLE /*AppUpdateType.IMMEDIATE*/)) {
                    try {
                        mAppUpdateManager.startUpdateFlowForResult(appUpdateInfo, AppUpdateType.FLEXIBLE, NativeBaseActivity.this, RC_APP_UPDATE);
                    } catch (IntentSender.SendIntentException e) {
                        e.printStackTrace();
                    }
                } else if (appUpdateInfo.installStatus() == InstallStatus.DOWNLOADED) {
                    popupSnackbarForCompleteUpdate();
                } else {
                    Log.e(" Tag", "checkForAppUpdateAvailability: something else");
                }
            }
        });
    }

    private void popupSnackbarForCompleteUpdate() {
        new AlertDialog.Builder(this)
                .setTitle("Update Version")
                .setMessage("This app's new version available, Do you want to update?")
                .setPositiveButton("INSTALL", new DialogInterface.OnClickListener() {
                    @Override
                    public void onClick(DialogInterface dialog, int which) {
                        if (mAppUpdateManager != null) {
                            mAppUpdateManager.completeUpdate();
                        }
                    }
                })
                .setNegativeButton("Cancel", new DialogInterface.OnClickListener() {
                    @Override
                    public void onClick(DialogInterface dialog, int which) {

                    }
                })
                .show();
    }

    InstallStateUpdatedListener installStateUpdatedListener = new
            InstallStateUpdatedListener() {
                @Override
                public void onStateUpdate(InstallState state) {
                    if (state.installStatus() == InstallStatus.DOWNLOADED) {
                        //CHECK THIS if AppUpdateType.FLEXIBLE, otherwise you can skip
                        popupSnackbarForCompleteUpdate();
                    } else if (state.installStatus() == InstallStatus.INSTALLED) {
                        if (mAppUpdateManager != null) {
                            mAppUpdateManager.unregisterListener(installStateUpdatedListener);
                        }

                    } else {
                        Log.i("", "InstallStateUpdatedListener: state: " + state.installStatus());
                    }
                }
            };


}
