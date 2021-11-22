package com.example.habittracker.testUtils;

import android.app.Activity;

import androidx.test.rule.ActivityTestRule;

import com.example.habittracker.User;
import com.example.habittracker.utils.SharedInfo;

/**
 * CustomActivityTestRule overrides some methods of ActivityTestRule to take some actions such as
 * setting the current user through SharedInfo BEFORE the activity is launched.
 * @param <T>   The Activity class under test
 */
public class CustomActivityTestRule<T extends Activity> extends ActivityTestRule<T> {
    public User currentUser;
    /**
     * Constructor for CustomActivityTestRule.
     * @param activityClass         The activity under test. This must be a class in the instrumentation
     *                              targetPackage specified in the AndroidManifest.xml
     * @param initialTouchMode      true if the Activity should be placed into "touch mode" when started
     * @param launchActivity        true if the Activity should be launched once per Test method.
     *                              It will be launched before the first Before method, and terminated
     *                              after the last After method.
     */
    public CustomActivityTestRule(Class<T> activityClass, boolean initialTouchMode, boolean launchActivity,
                           User currentUser) {
        super(activityClass, initialTouchMode, launchActivity);
        this.currentUser = currentUser;
    }

    /**
     * Sets the current user before launching the Activity under test.
     */
    @Override
    protected void beforeActivityLaunched() {
        SharedInfo.getInstance().setCurrentUser(currentUser);
    }
}
