package com.kidsads.visionkidsads.loader;

import android.app.Activity;
import android.app.Dialog;
import android.content.Context;
import android.content.DialogInterface;
import android.view.LayoutInflater;
import android.view.View;

import com.kidsads.visionkidsads.R;
import com.wang.avi.AVLoadingIndicatorView;

public final class AppOpenAdsLoader {

    private Dialog dialog;

    public Dialog show(Context context) {
        return show(context, null);
    }

    public Dialog show(Context context, CharSequence title) {
        return show(context, title, false);
    }

    public Dialog show(Context context, CharSequence title, boolean cancelable) {
        return show(context, title, cancelable, null);
    }

    public Dialog show(Context context, CharSequence title, boolean cancelable,
                       DialogInterface.OnCancelListener cancelListener) {
        LayoutInflater inflator = (LayoutInflater) context.getSystemService(Context.LAYOUT_INFLATER_SERVICE);
        final View view = inflator.inflate(R.layout.dialog_appopen_ads, null);

        dialog = new Dialog(context, android.R.style.Theme_Black_NoTitleBar_Fullscreen);
        dialog.setContentView(view);
        dialog.setCancelable(cancelable);
        dialog.setOnCancelListener(cancelListener);
        AVLoadingIndicatorView avLoadingIndicatorView = dialog.findViewById(R.id.avLoader);
        avLoadingIndicatorView.setIndicatorColor(R.color.gnt_gray);
        avLoadingIndicatorView.setIndicator("BallPulseIndicator");
        if (!((Activity) context).isFinishing()) {
            dialog.show();

            //show dialog
        }

        return dialog;
    }

    public Dialog getDialog() {
        return dialog;
    }

    public boolean isShowing() {
        return dialog != null && dialog.isShowing();
    }
}