package io.pumpkinz.pumpkinreader.util;

import android.app.Activity;
import android.app.Application;
import android.arch.lifecycle.Lifecycle;
import android.arch.lifecycle.LifecycleObserver;
import android.arch.lifecycle.OnLifecycleEvent;
import android.arch.lifecycle.ProcessLifecycleOwner;
import android.content.Context;
import android.hardware.Sensor;
import android.hardware.SensorEvent;
import android.hardware.SensorEventListener;
import android.hardware.SensorManager;
import android.os.Bundle;
import android.support.v7.app.AppCompatDelegate;
import android.util.Log;

import java.util.concurrent.TimeUnit;

import rx.android.schedulers.AndroidSchedulers;
import rx.functions.Action1;
import rx.schedulers.Schedulers;
import rx.subjects.PublishSubject;

/**
 * Created by twig on 27/02/2016.
 */
public class DayNightSensor implements SensorEventListener {
  private static final String TAG = DayNightSensor.class.getName();

  // Threshold before lighting is considered "dark"
  public static final float LUX_THRESHOLD = 4.0f;
  // Interval between light samples taken in milliseconds
  public static final int DEFAULT_INTERVAL = 1000;


  // Easier for users to customise the library
  public interface Settings {
    // Threshold before lighting is considered "dark"
    float luxThreshold = LUX_THRESHOLD;
    // Interval between light samples taken in milliseconds
    int samplingDelay = DEFAULT_INTERVAL;

    void onSensorChanged(boolean isDark);

    // No light sensor
    void notSupported();
  }


  // Single instance
  private static DayNightSensor instance;


  // Class fields - state
  private LifecycleObserver lifecycleObserver;
  private SensorManager sensorManager;
  private Sensor lightSensor;
  private int currentNightMode;
  private PublishSubject<Float> throttler;

  // Class fields - configuration
  private Settings settings;

  /**
   * Begin monitoring the light levels for this application.
   *
   * @param application
   * @param settings Customised settings.
   */
  public static void start(Application application, Settings settings) {
    if (instance != null) {
      Log.i(TAG, "DayNightSensor already instantiated.");
      return;
    }

    instance = new DayNightSensor(application, settings);
  }


  /**
   * Stop monitor light levels for this application.
   *
   * @param application
   */
  public static void stop(Application application) {
    if (instance == null) {
      return;
    }

    instance.cleanup(application);
  }

  // ----- Nothing below here uses 'instance' -----

  // Private constructor
  private DayNightSensor(Application application, Settings settings) {
    this.lifecycleObserver = null;
    this.lightSensor = null;
    this.currentNightMode = AppCompatDelegate.MODE_NIGHT_AUTO;

    this.settings = settings;

    sensorManager = (SensorManager) application.getSystemService(Context.SENSOR_SERVICE);
    lightSensor = sensorManager.getDefaultSensor(Sensor.TYPE_LIGHT);

    if (lightSensor != null) {
      lifecycleObserver = getLifecycleObserver();
      ProcessLifecycleOwner.get().getLifecycle().addObserver(lifecycleObserver);
    } else {
      // No light sensor
      Log.e(TAG, "DayNightSensor: No light sensor");
      this.settings.notSupported();
    }
  }


  public void cleanup(Application application) {
    if (lifecycleObserver != null) {
      ProcessLifecycleOwner.get().getLifecycle().removeObserver(lifecycleObserver);
      lifecycleObserver = null;
    }

    if (lightSensor != null) {
      SensorManager sensorManager = (SensorManager) application.getSystemService(Context.SENSOR_SERVICE);
      sensorManager.unregisterListener(this, lightSensor);

      lightSensor = null;
    }
  }

  private LifecycleObserver getLifecycleObserver() {
    return new LifecycleObserver() {
      @OnLifecycleEvent(Lifecycle.Event.ON_START)
      void onForeGround() {
        beginMonitoringLightLevels();
      }

      @OnLifecycleEvent(Lifecycle.Event.ON_STOP)
      void onBackground() {
        stopMonitoringLightLevels();
      }
    };
  }

  /**
   * Check light levels every X milliseconds.
   */
  protected void beginMonitoringLightLevels() {
    // Sensor sampling period is in MICROseconds.
    // Most people are used to MILLIseconds so x1000
    sensorManager.registerListener(this, lightSensor, this.settings.samplingDelay * 1000);

    // Stops working after activity restarted
    throttler = PublishSubject.create();
    throttler
        .throttleFirst(this.settings.samplingDelay, TimeUnit.MILLISECONDS, Schedulers.newThread())
        .observeOn(AndroidSchedulers.mainThread())
        .subscribe(new Action1<Float>() {
          @Override
          public void call(Float lux) {
            Log.e("throttled", String.valueOf(lux));
            doSensorChange(lux);
          }
        })
    ;
  }

  /**
   * Stop checking light levels (Activity ended, suspended, etc)
   */
  protected void stopMonitoringLightLevels() {
    sensorManager.unregisterListener(this, lightSensor);
//        throttler.onCompleted();
  }


  /**
   * @see http://developer.android.com/reference/android/hardware/SensorEvent.html#values
   * (section regarding Sensor.TYPE_LIGHT)
   *
   * @see https://en.wikipedia.org/wiki/Lux
   */
  @Override
  public void onSensorChanged(SensorEvent event) {
    float lux = event.values[0];
        Log.e("onSensorChanged", String.valueOf(lux));

    throttler.onNext(lux);
  }

  private void doSensorChange(float lux) {
    int changeToMode;

    // Less than LUX_THRESHOLD is considered dark
    if (lux < this.settings.luxThreshold) {
      changeToMode = AppCompatDelegate.MODE_NIGHT_YES;
    }
    else {
      changeToMode = AppCompatDelegate.MODE_NIGHT_NO;
    }

    // Only update and recreate the Activity if needed!
    if (currentNightMode != changeToMode) {
      currentNightMode = changeToMode;

//      if (currentActivity != null ) {
      Log.e("callign settings.onsensor changed", String.valueOf(lux));
      this.settings.onSensorChanged(changeToMode == AppCompatDelegate.MODE_NIGHT_YES);
//      }
    }
  }

  @Override
  public void onAccuracyChanged(Sensor sensor, int accuracy) {
  }
}