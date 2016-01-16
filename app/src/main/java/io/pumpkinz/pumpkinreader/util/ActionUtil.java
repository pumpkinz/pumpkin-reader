package io.pumpkinz.pumpkinreader.util;

import android.app.Activity;
import android.content.ComponentName;
import android.content.Context;
import android.content.Intent;
import android.net.Uri;
import android.support.customtabs.CustomTabsClient;
import android.support.customtabs.CustomTabsServiceConnection;
import android.support.design.widget.CoordinatorLayout;
import android.support.design.widget.Snackbar;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;

import org.parceler.Parcels;

import io.pumpkinz.pumpkinreader.R;
import io.pumpkinz.pumpkinreader.WebViewActivity;
import io.pumpkinz.pumpkinreader.etc.Constants;
import io.pumpkinz.pumpkinreader.etc.PumpkinCustomTab;
import io.pumpkinz.pumpkinreader.model.News;


public class ActionUtil {

    public static void open(Context ctx, News news) {
        if (news.getUrl() != null && !news.getUrl().isEmpty()) {
            Uri uri = Uri.parse(news.getUrl());

            if (isCustomTabsAvailable(ctx)) {
                PumpkinCustomTab customTab = new PumpkinCustomTab((Activity) ctx, news);
                customTab.openPage(uri);
            } else {
                Intent intent = new Intent(ctx, WebViewActivity.class);
                intent.putExtra(Constants.NEWS, Parcels.wrap(news));
                ctx.startActivity(intent);
            }
        }
    }

    public static void save(final Context ctx, final Menu menu, final News news) {
        final boolean isNewsSaved = PreferencesUtil.isNewsSaved(ctx, news);

        if (isNewsSaved) {
            PreferencesUtil.removeNews(ctx, news);
        } else {
            PreferencesUtil.saveNews(ctx, news);
        }

        CoordinatorLayout layout = (CoordinatorLayout) ((Activity) ctx).findViewById(R.id.news_detail_layout);

        Snackbar sb = Snackbar.make(layout, isNewsSaved ? R.string.unsaved_news : R.string.saved_news, Snackbar.LENGTH_LONG)
                .setAction(R.string.undo, new View.OnClickListener() {
                    @Override
                    public void onClick(View view) {
                        if (isNewsSaved) {
                            PreferencesUtil.saveNews(ctx, news);
                        } else {
                            PreferencesUtil.removeNews(ctx, news);
                        }

                        toggleSaveAction(ctx, menu, news);
                    }
                })
                .setActionTextColor(ctx.getResources().getColor(R.color.yellow_500));

        View sbView = sb.getView();
        sbView.setBackgroundColor(ctx.getResources().getColor(R.color.grey_800));

        sb.show();

        toggleSaveAction(ctx, menu, news);
    }

    public static void share(Context ctx, News news) {
        ctx.startActivity(Intent.createChooser(getPumpkinShareIntent(news), ctx.getResources().getString(R.string.share)));
    }

    public static void toggleSaveAction(Context ctx, Menu menu, News news) {
        MenuItem item = menu.findItem(R.id.action_save);

        if (PreferencesUtil.isNewsSaved(ctx, news)) {
            item.setIcon(R.drawable.ic_bookmark_white_24dp);
            item.setTitle(R.string.unsave);
        } else {
            item.setIcon(R.drawable.ic_bookmark_border_white_24dp);
            item.setTitle(R.string.save);
        }
    }

    public static Intent getPumpkinShareIntent(News news) {
        Intent shareIntent = new Intent(Intent.ACTION_SEND);
        shareIntent.putExtra(Intent.EXTRA_SUBJECT, news.getTitle());

        String text;

        if (news.getUrl() != null && !news.getUrl().isEmpty()) {
            text = news.getUrl();
        } else {
            text = Constants.HN_BASE_URL + news.getId();
        }

        shareIntent.putExtra(Intent.EXTRA_TEXT, text);
        shareIntent.setType(Constants.MIME_TEXT_PLAIN);

        return shareIntent;
    }

    private static boolean isCustomTabsAvailable(Context ctx) {
        return CustomTabsClient.bindCustomTabsService(ctx, "com.android.chrome", new CustomTabsServiceConnection() {
            @Override
            public void onCustomTabsServiceConnected(ComponentName componentName, CustomTabsClient customTabsClient) {

            }

            @Override
            public void onServiceDisconnected(ComponentName name) {

            }
        });
    }

}
