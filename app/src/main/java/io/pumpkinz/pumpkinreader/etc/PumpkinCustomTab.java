package io.pumpkinz.pumpkinreader.etc;

import android.app.Activity;
import android.app.PendingIntent;
import android.content.ComponentName;
import android.content.Intent;
import android.content.SharedPreferences;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.net.Uri;
import android.preference.PreferenceManager;
import android.support.customtabs.CustomTabsClient;
import android.support.customtabs.CustomTabsIntent;
import android.support.customtabs.CustomTabsServiceConnection;
import android.support.v4.content.ContextCompat;

import org.parceler.Parcels;

import io.pumpkinz.pumpkinreader.R;
import io.pumpkinz.pumpkinreader.WebViewActivity;
import io.pumpkinz.pumpkinreader.model.News;

public class PumpkinCustomTab extends CustomTabsServiceConnection {

    private Activity activity;
    private News news;
    private CustomTabsClient client;
    private CustomTabsIntent customTabsIntent;
    private boolean isCustomTabAvailable;

    public PumpkinCustomTab(Activity activity, News news) {
        this.activity = activity;
        this.news = news;
        isCustomTabAvailable =
            CustomTabsClient.bindCustomTabsService(activity, "com.android.chrome", this);
    }

    public void openPage() {
        if (news == null) return;
        if (!news.isUrlValid()) return;

        if (isCustomTabAvailable) {
            customTabsIntent = buildCustomTabsIntent();
            customTabsIntent.launchUrl(activity, Uri.parse(news.getUrl()));
        } else {
            Intent intent = new Intent(activity, WebViewActivity.class);
            intent.putExtra(Constants.NEWS, Parcels.wrap(news));
            activity.startActivity(intent);
        }
    }

    public void unbindCustomTabService() {
        if (client == null) return;
        client = null;
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
        Bitmap icon =
            BitmapFactory.decodeResource(activity.getResources(), R.drawable.ic_share_white_24dp);
        builder.setActionButton(icon, label, createShareIntent());

        return builder.build();
    }

    private PendingIntent createShareIntent() {
        Intent actionIntent =
            new Intent(activity.getApplicationContext(), ShareBroadcastReceiver.class);
        actionIntent.putExtra(Constants.NEWS, Parcels.wrap(news));
        return PendingIntent.getBroadcast(activity.getApplicationContext(), 0, actionIntent,
            PendingIntent.FLAG_CANCEL_CURRENT);
    }

    @Override public void onCustomTabsServiceConnected(ComponentName componentName,
        CustomTabsClient customTabsClient) {
        client = customTabsClient;
        client.warmup(0);
    }

    @Override public void onServiceDisconnected(ComponentName componentName) {
        client = null;
    }
}
