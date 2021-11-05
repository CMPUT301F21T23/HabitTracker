package com.example.habittracker;

import androidx.test.platform.app.InstrumentationRegistry;
import androidx.test.rule.ActivityTestRule;

import com.example.habittracker.activities.ListActivity;
import com.example.habittracker.activities.SharingActivity;
import com.example.habittracker.activities.profile.ProfileActivity;
import com.robotium.solo.Solo;

import com.example.habittracker.activities.HomeActivity;
import org.junit.After;
import org.junit.Before;
import org.junit.Rule;
import org.junit.Test;

public class NavigationBarTests {
    private Solo solo;
    @Rule
    public ActivityTestRule<HomeActivity> rule =
            new ActivityTestRule<>(HomeActivity.class, true, true);
    /**
     * Runs before all tests and creates solo instance.
     * @throws Exception
     */
    @Before
    public void setUp() throws Exception{
        solo = new Solo(InstrumentationRegistry.getInstrumentation(), rule.getActivity());
    }

    /**
     * test each button on the navigation bar
     */
    @Test
    public void testNavBar(){
        solo.assertCurrentActivity("Wrong Activity", HomeActivity.class);
        solo.clickOnView(solo.getView(R.id.list));
        solo.assertCurrentActivity("Wrong Activity", ListActivity.class);
        solo.clickOnView(solo.getView(R.id.profile));
        solo.assertCurrentActivity("Wrong Activity", ProfileActivity.class);
        solo.clickOnView(solo.getView(R.id.sharing));
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
