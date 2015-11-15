package io.pumpkinz.pumpkinreader;

import android.content.SharedPreferences;
import android.os.Bundle;
import android.preference.PreferenceManager;
import android.support.design.widget.AppBarLayout;
import android.support.v7.app.AppCompatActivity;

import io.pumpkinz.pumpkinreader.etc.Constants;


public class PumpkinReaderActivity extends AppCompatActivity implements SharedPreferences.OnSharedPreferenceChangeListener {

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        SharedPreferences pref = PreferenceManager.getDefaultSharedPreferences(this);
        pref.registerOnSharedPreferenceChangeListener(this);

        changeTheme(pref);

        super.onCreate(savedInstanceState);
    }

    @Override
    public void onSharedPreferenceChanged(SharedPreferences sharedPreferences, String s) {
        if (s.equals(Constants.CONFIG_DARK_THEME)) {
            changeTheme(PreferenceManager.getDefaultSharedPreferences(this));
            recreate();
        }
    }

    protected void changeTheme(SharedPreferences preferences) {
        boolean isDarkTheme = preferences.getBoolean(Constants.CONFIG_DARK_THEME, false);

        if (isDarkTheme) {
            setTheme(R.style.DarkPumpkinTheme);
        } else {
            setTheme(R.style.PumpkinTheme);
        }
    }

    protected void setScrollFlag(AppBarLayout.LayoutParams layoutParams) {
        final SharedPreferences pref = PreferenceManager.getDefaultSharedPreferences(this);
        boolean shouldHideToolbar = pref.getBoolean(Constants.CONFIG_HIDE_TOOLBAR, false);

        if (shouldHideToolbar) {
            layoutParams.setScrollFlags(AppBarLayout.LayoutParams.SCROLL_FLAG_SCROLL
                    | AppBarLayout.LayoutParams.SCROLL_FLAG_ENTER_ALWAYS);
        } else {
            layoutParams.setScrollFlags(0);
        }
    }

}
