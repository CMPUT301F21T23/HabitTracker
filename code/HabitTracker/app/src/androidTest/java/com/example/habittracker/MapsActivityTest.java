package com.example.habittracker;


import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertFalse;
import static org.junit.Assert.assertTrue;

import android.app.Activity;

import androidx.test.platform.app.InstrumentationRegistry;
import androidx.test.rule.ActivityTestRule;

import com.example.habittracker.activities.eventlist.EventDetailActivity;
import com.example.habittracker.activities.eventlist.MapsActivity;
import com.robotium.solo.Solo;

import org.junit.After;
import org.junit.Before;
import org.junit.Rule;
import org.junit.Test;

public class MapsActivityTest {
    private Solo solo;

    @Rule
    public ActivityTestRule<MapsActivity> rule = new ActivityTestRule<>(MapsActivity.class, true, true);

    @Rule
    public ActivityTestRule<EventDetailActivity> rule2 = new ActivityTestRule<>(EventDetailActivity.class, true, true);

    /**
     *
     * @throws Exception
     */
    @Before
    public void setUp() throws Exception{
        solo = new Solo(InstrumentationRegistry.getInstrumentation(),rule.getActivity());
    }

    /**
     * start test
     * @throws Exception
     */
    @Test
    public void start() throws Exception{
        Activity activity = rule.getActivity();
    }

    /**
     * test transition to Event Detail Activity
     */
    @Test
    public void checkTransition(){
        solo.assertCurrentActivity("Wrong Activity", MapsActivity.class);
        solo.goBack();
        solo.assertCurrentActivity("Wrong Activity", EventDetailActivity.class);
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
