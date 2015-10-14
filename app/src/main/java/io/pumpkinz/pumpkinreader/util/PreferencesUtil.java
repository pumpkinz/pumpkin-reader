package io.pumpkinz.pumpkinreader.util;

import android.content.Context;
import android.content.SharedPreferences;
import android.preference.PreferenceManager;

import java.util.List;

import io.pumpkinz.pumpkinreader.etc.Constants;
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

    /**
     * Saved the News ID into SAVED_FILE_SP.
     * As the news ID is saved in string form separated by |, we need to split the saved
     * news first, append the new one, and saved it again.
     *
     * @param ctx
     * @param news
     */
    public static void saveNews(Context ctx, News news) {
        SharedPreferences savedPref = ctx.getSharedPreferences(
                Constants.SAVED_FILE_SP, Context.MODE_PRIVATE);

        String savedNews = savedPref.getString(Constants.SAVED_VAL_SP, "");
        List<Integer> newSavedNews = Util.splitNews(savedNews);

        if (newSavedNews.contains(news.getId())) {
            return;
        }

        newSavedNews.add(news.getId());
        SharedPreferences.Editor editor = savedPref.edit();
        editor.putString(Constants.SAVED_VAL_SP, Util.joinNews(newSavedNews)).commit();
    }

    private static String getNewsId(News news) {
        return KEY_PREFIX_READ_STATUS + news.getId();
    }

}
