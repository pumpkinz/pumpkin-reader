package io.pumpkinz.pumpkinreader;

import android.support.v7.widget.Toolbar;

import org.junit.Before;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.robolectric.Robolectric;
import org.robolectric.RobolectricGradleTestRunner;
import org.robolectric.annotation.Config;

import static org.junit.Assert.assertNotNull;
import static org.junit.Assert.assertTrue;

@Config(constants = BuildConfig.class, sdk = 18)
@RunWith(RobolectricGradleTestRunner.class)
public class MainActivityTest {
    private MainActivity activity;

    @Before
    public void setup() throws Exception {
        activity = Robolectric.setupActivity(MainActivity.class);
        assertNotNull("MainActivity is not instantiated", activity);
    }

    @Test
    public void toolbarTitle() throws Exception {
        Toolbar appBar = (Toolbar) activity.findViewById(R.id.app_bar);
        assertNotNull("Toolbar could not be found", appBar);
        assertTrue("Toolbar contains incorrect title",
                "Top News".equals(appBar.getTitle().toString()));
    }

}
