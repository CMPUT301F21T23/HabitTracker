package com.example.habittracker;

import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertFalse;
import static org.junit.Assert.assertTrue;

import android.app.Activity;
import android.widget.EditText;
import android.widget.ListView;

import androidx.fragment.app.FragmentActivity;
import androidx.test.platform.app.InstrumentationRegistry;
import androidx.test.rule.ActivityTestRule;

import com.example.habittracker.activities.HomeActivity;
import com.example.habittracker.activities.SharingActivity;
import com.example.habittracker.activities.eventlist.MapsActivity;
import com.example.habittracker.activities.fragments.SharingInputFragment;
import com.google.android.material.floatingactionbutton.FloatingActionButton;
import com.example.habittracker.activities.eventlist.LocationActivity;

import com.example.habittracker.activities.tracking.*;
import com.example.habittracker.activities.profile.ProfileFollowersActivity;
import com.example.habittracker.activities.profile.ProfileFollowingActivity;
import com.example.habittracker.utils.SharedInfo;

import com.robotium.solo.Solo;

import org.junit.After;
import org.junit.Before;
import org.junit.Rule;
import org.junit.Test;

public class SharingInputFragmentTest {
    private Solo solo;

    @Rule
    public ActivityTestRule<SharingActivity> rule = new ActivityTestRule<>(SharingActivity.class, true, true);

    /**
     *
     * @throws Exception
     */
    @Before
    public void setUp() throws Exception{
        solo = new Solo(InstrumentationRegistry.getInstrumentation(),rule.getActivity());
    }

    /**
     *
     * @throws Exception
     */
    @Test
    public void start() throws Exception{
        Activity activity = rule.getActivity();
    }

    /**
     * check fragment launched after clicking floating action button
     */
    @Test
    public void checkFragment(){
        solo.assertCurrentActivity("Wrong Activity", SharingActivity.class);
        solo.clickOnView(solo.getView(R.id.add_person_button));
        solo.clickOnView(solo.getView(R.id.sharing_input_send_button));
        solo.assertCurrentActivity("Wrong Activity", SharingActivity.class);
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
