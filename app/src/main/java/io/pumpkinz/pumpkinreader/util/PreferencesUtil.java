package io.pumpkinz.pumpkinreader.util;

import android.content.Context;
import android.content.SharedPreferences;
import android.preference.PreferenceManager;

import io.pumpkinz.pumpkinreader.model.News;


public class PreferencesUtil {

    private static final String KEY_PREFIX_READ_STATUS = "read_";

    public static void markNewsAsRead(Context ctx, News news) {
        SharedPreferences pref = PreferenceManager.getDefaultSharedPreferences(ctx);
        pref.edit().putBoolean(getNewsId(news), true).commit();
    }

    public static boolean isNewsRead(Context ctx, News news) {
        SharedPreferences pref = PreferenceManager.getDefaultSharedPreferences(ctx);
        return pref.getBoolean(getNewsId(news), false);
    }

    private static String getNewsId(News news) {
        return KEY_PREFIX_READ_STATUS + news.getId();
    }

}
