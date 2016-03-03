package io.pumpkinz.pumpkinreader.etc;

import android.app.Activity;
import android.app.PendingIntent;
import android.content.Intent;
import android.content.SharedPreferences;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.net.Uri;
import android.preference.PreferenceManager;
import android.support.customtabs.CustomTabsIntent;
import android.support.v4.content.ContextCompat;

import org.parceler.Parcels;

import io.pumpkinz.pumpkinreader.R;
import io.pumpkinz.pumpkinreader.model.News;


public class PumpkinCustomTab {

    private Activity activity;
    private News news;
    private CustomTabsIntent customTabsIntent;

    public PumpkinCustomTab(Activity activity, News news) {
        this.activity = activity;
        this.news = news;
        this.customTabsIntent = buildCustomTabsIntent();
    }

    public void openPage(Uri uri) {
        customTabsIntent.launchUrl(activity, uri);
    }

    private CustomTabsIntent buildCustomTabsIntent() {
        CustomTabsIntent.Builder builder = new CustomTabsIntent.Builder();
        builder.setShowTitle(true);
        SharedPreferences pref = PreferenceManager.getDefaultSharedPreferences(activity);

        int color;

        if (pref.getBoolean(Constants.CONFIG_DARK_THEME, false)) {
            color = ContextCompat.getColor(activity, R.color.primary_material_dark);
        } else {
            color = ContextCompat.getColor(activity, R.color.pumpkin_primary);
        }

        builder.setToolbarColor(color);

        String label = activity.getResources().getString(R.string.share);
        Bitmap icon = BitmapFactory.decodeResource(activity.getResources(), R.drawable.ic_share_white_24dp);
        builder.setActionButton(icon, label, createShareIntent());

        return builder.build();
    }

    private PendingIntent createShareIntent() {
        Intent actionIntent = new Intent(activity.getApplicationContext(), ShareBroadcastReceiver.class);
        actionIntent.putExtra(Constants.NEWS, Parcels.wrap(news));
        return PendingIntent.getBroadcast(activity.getApplicationContext(), 0, actionIntent, PendingIntent.FLAG_CANCEL_CURRENT);
    }

}
