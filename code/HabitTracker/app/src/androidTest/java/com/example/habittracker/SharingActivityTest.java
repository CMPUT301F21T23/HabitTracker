package com.example.habittracker;


import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertFalse;
import static org.junit.Assert.assertTrue;

import android.app.Activity;

import androidx.test.platform.app.InstrumentationRegistry;
import androidx.test.rule.ActivityTestRule;

import com.example.habittracker.activities.sharing.SharingActivity;

import com.robotium.solo.Solo;

import org.junit.After;
import org.junit.Before;
import org.junit.Rule;
import org.junit.Test;

public class SharingActivityTest {
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

    @Test
    public void start() throws Exception{
        Activity activity = rule.getActivity();
    }

    @Test
    public void checkTransition(){
        solo.assertCurrentActivity("Wrong Activity", SharingActivity.class);
        solo.clickOnView(solo.getView(R.id.add_person_button));
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
