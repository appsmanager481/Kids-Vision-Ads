package com.kidsads.visionkidsads.adapter;

import android.app.Activity;
import android.content.Intent;
import android.content.res.ColorStateList;
import android.graphics.Color;
import android.net.Uri;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.cardview.widget.CardView;
import androidx.recyclerview.widget.RecyclerView;

import com.kidsads.visionkidsads.R;
import com.kidsads.visionkidsads.model.MoreAppIds;
import com.kidsads.visionkidsads.utils.PrefLibAds;
import com.bumptech.glide.Glide;

import java.util.List;

public class MoreAppAdapter extends RecyclerView.Adapter<MoreAppAdapter.ViewHolder> {

    private final Activity activity;
    private final List<MoreAppIds> moreApps;
    private final int layoutItem;

    public MoreAppAdapter(Activity activity, List<MoreAppIds> moreAppIds, int layoutItem) {
        this.activity = activity;
        this.moreApps = moreAppIds;
        this.layoutItem = layoutItem;
    }

    @NonNull
    @Override
    public ViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        View view = LayoutInflater.from(parent.getContext()).inflate(layoutItem, parent, false);
        return new ViewHolder(view);
    }

    @Override
    public void onBindViewHolder(@NonNull ViewHolder holder, int position) {
        MoreAppIds moreApp = moreApps.get(position);

        Glide.with(activity).load(moreApp.getAppLogo()).into(holder.appLogoImageView);
        holder.appNameTextView.setText(moreApp.getAppName());
        if (!PrefLibAds.getInstance().getString("appAdsTextColor").equals("")) {
            holder.appNameTextView.setTextColor(ColorStateList.valueOf(Color.parseColor(PrefLibAds.getInstance().getString("appAdsTextColor"))));
        }

        if (!PrefLibAds.getInstance().getString("appAdsButtonTextColor").equals("")) {
            holder.tvInstall.setTextColor(ColorStateList.valueOf(Color.parseColor(PrefLibAds.getInstance().getString("appAdsButtonTextColor"))));
        }

        if (!PrefLibAds.getInstance().getString("appAdsBackgroundColor").equals("")) {
            holder.cvInstall.setCardBackgroundColor(ColorStateList.valueOf(Color.parseColor(PrefLibAds.getInstance().getString("appAdsBackgroundColor"))));
        }

        holder.cvInstall.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                final String appPackageName = moreApp.getPackageName();
                try {
                    activity.startActivity(new Intent(Intent.ACTION_VIEW, Uri.parse("market://details?id=" + appPackageName)));
                } catch (android.content.ActivityNotFoundException anfe) {
                    activity.startActivity(new Intent(Intent.ACTION_VIEW, Uri.parse("https://play.google.com/store/apps/details?id=" + appPackageName)));
                }
            }
        });
    }

    @Override
    public int getItemCount() {
        return moreApps.size();
    }

    public static class ViewHolder extends RecyclerView.ViewHolder {
        private final ImageView appLogoImageView;
        private final TextView appNameTextView;
        private final TextView tvInstall;
        private final CardView cvInstall;

        public ViewHolder(@NonNull View itemView) {
            super(itemView);
            appLogoImageView = itemView.findViewById(R.id.ivLogo);
            cvInstall = itemView.findViewById(R.id.cvInstall);
            appNameTextView = itemView.findViewById(R.id.tvAppName);
            tvInstall = itemView.findViewById(R.id.tvInstall);
        }
    }
}