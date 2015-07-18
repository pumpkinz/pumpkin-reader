package io.pumpkinz.pumpkinreader.util;

import android.content.Context;
import android.content.SharedPreferences;
import android.preference.PreferenceManager;

import io.pumpkinz.pumpkinreader.model.News;


public class PreferencesUtil {

    public static void markNewsAsRead(Context ctx, News news) {
        SharedPreferences pref = PreferenceManager.getDefaultSharedPreferences(ctx);
        pref.edit().putBoolean(String.valueOf(news.getId()), true).commit();
    }

    public static boolean isNewsRead(Context ctx, News news) {
        SharedPreferences pref = PreferenceManager.getDefaultSharedPreferences(ctx);
        return pref.getBoolean(String.valueOf(news.getId()), false);
    }

}
