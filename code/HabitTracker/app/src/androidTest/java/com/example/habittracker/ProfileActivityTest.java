package com.example.habittracker;
import android.app.Activity;

import androidx.test.platform.app.InstrumentationRegistry;
import androidx.test.rule.ActivityTestRule;

import com.example.habittracker.activities.LoginActivity;
import com.example.habittracker.activities.profile.ProfileActivity;
import com.example.habittracker.activities.profile.ProfileFollowersActivity;
import com.example.habittracker.activities.profile.ProfileFollowingActivity;
import com.example.habittracker.activities.profile.ProfilePendingFollowersActivity;
import com.example.habittracker.utils.SharedInfo;
import com.robotium.solo.Solo;

import org.junit.After;
import org.junit.Before;
import org.junit.Rule;
import org.junit.Test;

/**
 * Test class for ProfileActivity. All the UI tests are written here using Robotium test
 * framework.
 */
public class ProfileActivityTest {
    private Solo solo;
    @Rule
    public ActivityTestRule<ProfileActivity> rule =
            new ActivityTestRule<>(ProfileActivity.class, true, true);


    /**
     * Runs before all tests and creates solo instance.
     * @throws Exception
     */
    @Before
    public void setUp() throws Exception{
        solo = new Solo(InstrumentationRegistry.getInstrumentation(),rule.getActivity());
        SharedInfo.getInstance().setCurrentUser(new User("mockUser"));
    }

    /**
     * Closes the activity after each test
     * @throws Exception
     */
    @After
    public void tearDown() throws Exception{
        solo.finishOpenedActivities();
    }

    /**
     * Gets the ProfileActivity
     * @throws Exception
     */
    @Test
    public void start() throws Exception{
        Activity activity = rule.getActivity();
    }

    /**
     * Tests whether ProfileActivity switches to ProfileFollowingActivity when the
     * FOLLOWING button is pressed.
     */
    @Test
    public void followingButton() {
        solo.assertCurrentActivity("Wrong Activity", ProfileActivity.class);
        solo.clickOnButton("Following");
        solo.assertCurrentActivity("Activity was not switched properly", ProfileFollowingActivity.class);
    }

    /**
     * Tests whether the ProfileActivity switches to ProfileFollowersActivity when the
     * Followers button is pressed
     */
    @Test
    public void followersButton() {
        solo.assertCurrentActivity("Wrong Activity", ProfileActivity.class);
        solo.clickOnButton("Followers");
        solo.assertCurrentActivity("Activity was not switched properly", ProfileFollowersActivity.class);
    }

    /**
     * Tests whether the ProfileActivity switches to ProfilePendingFollowersActivity when the
     * Requests button is pressed
     */
    @Test
    public void requestsButton() {
        solo.assertCurrentActivity("Wrong Activity", ProfileActivity.class);
        solo.clickOnButton("Requests");
        solo.assertCurrentActivity("Activity was not switched properly", ProfilePendingFollowersActivity.class);
    }

    /**
     * Tests whether the ProfileActivity switches to LoginActivity when the Log out button is
     * pressed
     */
    @Test
    public void logoutButton() {
        solo.assertCurrentActivity("Wrong Activity", ProfileActivity.class);
        solo.clickOnButton("Log out");
        solo.assertCurrentActivity("Activity was not switched properly", LoginActivity.class);
    }

}
