package io.pumpkinz.pumpkinreader;

import android.content.SharedPreferences;
import android.os.Bundle;
import android.preference.Preference;
import android.preference.PreferenceFragment;
import android.preference.PreferenceManager;

import io.pumpkinz.pumpkinreader.etc.Constants;


public class SettingsActivityFragment extends PreferenceFragment implements SharedPreferences.OnSharedPreferenceChangeListener {
    private SharedPreferences sharedPreferences;

    public SettingsActivityFragment() {
    }

    @Override
    public void onCreate(Bundle savedInstanceState) {
        sharedPreferences = PreferenceManager.getDefaultSharedPreferences(getActivity().getBaseContext());
        sharedPreferences.registerOnSharedPreferenceChangeListener(this);

        super.onCreate(savedInstanceState);
        addPreferencesFromResource(R.xml.preferences);
        configureDarkModePref();
    }

    private void configureDarkModePref() {
        boolean enabled = sharedPreferences.getBoolean(Constants.CONFIG_AUTO_DARK_THEME, false);
        Preference pref = getPreferenceManager().findPreference(Constants.CONFIG_DARK_THEME);

        if (pref != null) {
            pref.setEnabled(!enabled);
        }
    }

    @Override
    public void onSharedPreferenceChanged(SharedPreferences sharedPreferences, String key) {
        if (key.equals(Constants.CONFIG_AUTO_DARK_THEME)) {
            configureDarkModePref();
        }
    }
}
