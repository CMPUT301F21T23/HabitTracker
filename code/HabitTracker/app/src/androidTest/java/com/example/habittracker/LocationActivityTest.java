package com.example.habittracker;

import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertFalse;
import static org.junit.Assert.assertTrue;

import android.app.Activity;
import android.widget.EditText;
import android.widget.ListView;

import androidx.test.platform.app.InstrumentationRegistry;
import androidx.test.rule.ActivityTestRule;

import com.example.habittracker.activities.eventlist.LocationActivity;
import com.example.habittracker.activities.eventlist.MapsActivity;
import com.example.habittracker.activities.profile.ProfileActivity;
import com.example.habittracker.activities.profile.ProfileFollowersActivity;
import com.example.habittracker.activities.profile.ProfileFollowingActivity;
import com.example.habittracker.utils.SharedInfo;
import com.google.firebase.firestore.FirebaseFirestore;
import com.robotium.solo.Solo;

import org.junit.After;
import org.junit.Before;
import org.junit.Rule;
import org.junit.Test;

public class LocationActivityTest {
    private Solo solo;

    @Rule
    public ActivityTestRule<LocationActivity> rule = new ActivityTestRule<>(LocationActivity.class, true, true);

    @Rule
    public ActivityTestRule<MapsActivity> rule2 = new ActivityTestRule<>(MapsActivity.class, true, true);

    /**
     *
     * @throws Exception
     */
    @Before
    public void setUp() throws Exception{
        solo = new Solo(InstrumentationRegistry.getInstrumentation(),rule.getActivity());
    }

    @Test
    public void start() throws Exception{
        Activity activity = rule.getActivity();
    }

    @Test
    public void checkTransition(){
        solo.assertCurrentActivity("Wrong Activity", LocationActivity.class);
        solo.clickOnButton("Choose Location");

        solo.assertCurrentActivity("Wrong Activity", MapsActivity.class);
    }


    /**
     * Close activity after each test
     * @throws Exception
     */
    @After
    public void tearDown() throws Exception{
        solo.finishOpenedActivities();
    }
}
