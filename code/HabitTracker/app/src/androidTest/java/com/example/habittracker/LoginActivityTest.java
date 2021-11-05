package com.example.habittracker;

import android.app.Activity;
import android.widget.EditText;

import androidx.test.platform.app.InstrumentationRegistry;
import androidx.test.rule.ActivityTestRule;

import com.example.habittracker.activities.HomeActivity;
import com.example.habittracker.activities.LoginActivity;
import com.example.habittracker.activities.profile.ProfileActivity;
import com.robotium.solo.Solo;

import org.junit.Before;
import org.junit.Rule;
import org.junit.Test;

import java.util.Random;

public class LoginActivityTest {
    private Solo solo;

    @Rule
    public ActivityTestRule<LoginActivity> rule =
            new ActivityTestRule<>(LoginActivity.class, true, true);

    /**
     * Runs before all tests and creates solo instance.
     * @throws Exception
     */
    @Before
    public void setUp() throws Exception{
        solo = new Solo(InstrumentationRegistry.getInstrumentation(), rule.getActivity());
    }

    /**
     * Gets the Activity
     * @throws Exception
     */
    @Test
    public void start() throws Exception{
        Activity activity = rule.getActivity();
    }

    @Test
    public void checkRegister() {
        // Asserts that the current activity is the LoginActivity. Otherwise, show “Wrong Activity”
        solo.assertCurrentActivity("Wrong Activity", LoginActivity.class);

        // Generate a random username and password
        final int random1 = new Random().nextInt(1000000000);
        final int random2 = new Random().nextInt(1000000000);

        // Get view for EditText and enter a username and password
        solo.enterText((EditText) solo.getView(R.id.editTextUsername), "Intent_Test_User" + String.valueOf(random1));
        solo.enterText((EditText) solo.getView(R.id.editTextPassword), "5up3rStr0nkP@sSw0rd" + String.valueOf(random2));

        // This user shouldn't exist so logging in won't work
        solo.clickOnView(solo.getView(R.id.login));
        solo.assertCurrentActivity("Wrong Activity", LoginActivity.class);

        // Register the new user and make sure the app is in the HomeActivity
        solo.clickOnView(solo.getView(R.id.register));
        solo.assertCurrentActivity("Wrong Activity", HomeActivity.class);

        // Use the navbar to go to the Profile screen
        //solo.clickOnButton("Profile");
        solo.clickOnView(solo.getView(R.id.profile));
        solo.assertCurrentActivity("Wrong Activity", ProfileActivity.class);

        // Finally, log out!
        solo.clickOnView(solo.getView(R.id.logoutButton));
        solo.assertCurrentActivity("Wrong Activity", LoginActivity.class);
    }
}
