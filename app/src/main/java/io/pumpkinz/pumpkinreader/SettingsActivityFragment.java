package io.pumpkinz.pumpkinreader;

import android.os.Bundle;
import android.preference.PreferenceFragment;


public class SettingsActivityFragment extends PreferenceFragment {

    public SettingsActivityFragment() {
    }

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        addPreferencesFromResource(R.xml.preferences);
    }

}
