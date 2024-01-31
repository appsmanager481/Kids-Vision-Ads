package com.kidsads.visionkidsads.loader;

import android.app.Activity;
import android.app.Dialog;
import android.content.Context;
import android.content.DialogInterface;
import android.view.LayoutInflater;
import android.view.View;
import android.view.Window;

import com.kidsads.visionkidsads.R;

public final class InterAdsLoader {

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
        final View view = inflator.inflate(R.layout.dialog_inter_ads, null);

        dialog = new Dialog(context, R.style.WideDialog100);
        dialog.requestWindowFeature(Window.FEATURE_NO_TITLE);
        dialog.setContentView(view);
        dialog.setCancelable(cancelable);
        dialog.setOnCancelListener(cancelListener);
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