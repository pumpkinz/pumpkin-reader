package io.pumpkinz.pumpkinreader.util;


import android.app.Activity;
import android.content.Context;
import android.content.Intent;
import android.net.Uri;
import android.support.design.widget.CoordinatorLayout;
import android.support.design.widget.Snackbar;
import android.view.View;

import io.pumpkinz.pumpkinreader.R;
import io.pumpkinz.pumpkinreader.etc.Constants;
import io.pumpkinz.pumpkinreader.model.News;

public class ActionUtil {

    public static void open(Context ctx, News news) {
        if (news.getUrl() != null && !news.getUrl().isEmpty()) {
            Uri uri = Uri.parse(news.getUrl());
            Intent intent = new Intent(Intent.ACTION_VIEW, uri);
            ctx.startActivity(intent);
        }
    }

    public static void save(final Context ctx, final News news) {
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
                    }
                })
                .setActionTextColor(ctx.getResources().getColor(R.color.yellow_500));

        View sbView = sb.getView();
        sbView.setBackgroundColor(ctx.getResources().getColor(R.color.grey_800));

        sb.show();
    }

    public static void share(Context ctx, News news) {
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

        ctx.startActivity(Intent.createChooser(shareIntent, ctx.getResources().getString(R.string.share)));
    }

}
