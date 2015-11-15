package io.pumpkinz.pumpkinreader;

import android.content.SharedPreferences;
import android.os.Bundle;
import android.preference.Preference;
import android.preference.PreferenceFragment;
import android.preference.PreferenceManager;

import io.pumpkinz.pumpkinreader.etc.Constants;


public class SettingsActivityFragment extends PreferenceFragment implements SharedPreferences.OnSharedPreferenceChangeListener {

    public SettingsActivityFragment() {
    }

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        addPreferencesFromResource(R.xml.preferences);

        SharedPreferences sp = PreferenceManager.getDefaultSharedPreferences(getActivity());
        sp.registerOnSharedPreferenceChangeListener(this);

        setCustomTabsPref(sp);
    }

    @Override
    public void onSharedPreferenceChanged(SharedPreferences sharedPreferences, String s) {
        if (s.equals(Constants.CONFIG_EXTERNAL_BROWSER)) {
            setCustomTabsPref(sharedPreferences);
        }
    }

    private void setCustomTabsPref(SharedPreferences sp) {
        Preference pref = getPreferenceScreen().findPreference(Constants.CONFIG_CUSTOM_TABS);
        pref.setEnabled(sp.getBoolean(Constants.CONFIG_EXTERNAL_BROWSER, false));
    }

}
