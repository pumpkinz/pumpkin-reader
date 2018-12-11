package io.pumpkinz.pumpkinreader;

import android.app.Application;
import android.content.SharedPreferences;
import android.preference.PreferenceManager;

import io.pumpkinz.pumpkinreader.etc.Constants;
import io.pumpkinz.pumpkinreader.util.DayNightSensor;

public class PumpkinReaderApplication extends Application implements SharedPreferences.OnSharedPreferenceChangeListener, DayNightSensor.Settings {
  private SharedPreferences sharedPreferences;

  @Override
  public void onCreate() {
    sharedPreferences = PreferenceManager.getDefaultSharedPreferences(this);
    sharedPreferences.registerOnSharedPreferenceChangeListener(this);
    super.onCreate();
    configureDayNightSensor();
  }

  @Override
  public void onSharedPreferenceChanged(final SharedPreferences sharedPreferences, final String key) {
    if (key.equals(Constants.CONFIG_AUTO_DARK_THEME)) {
      configureDayNightSensor();
    }
  }

  @Override
  public void onSensorChanged(boolean isDark) {
    SharedPreferences.Editor prefEditor = sharedPreferences.edit();
    prefEditor.putBoolean(Constants.CONFIG_DARK_THEME, isDark);
    prefEditor.apply();
  }

  @Override
  public void notSupported() {
    SharedPreferences.Editor prefEditor = sharedPreferences.edit();
    prefEditor.putBoolean(Constants.CONFIG_DARK_THEME, true);
    prefEditor.putBoolean(Constants.CONFIG_AUTO_DARK_THEME, false);
    prefEditor.apply();
  }

  private void configureDayNightSensor() {
    boolean isAutoDark = sharedPreferences.getBoolean(Constants.CONFIG_AUTO_DARK_THEME, false);

    if (isAutoDark) {
      DayNightSensor.start(this, this);
    } else {
      DayNightSensor.stop(this);
    }
  }
}
