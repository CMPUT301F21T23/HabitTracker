package com.example.habittracker;

import androidx.test.platform.app.InstrumentationRegistry;
import androidx.test.rule.ActivityTestRule;

import com.example.habittracker.activities.HabitViewActivity;
import com.example.habittracker.activities.HomeActivity;
import com.example.habittracker.activities.ListActivity;
import com.example.habittracker.activities.ProgressTrackingActivity;
import com.example.habittracker.activities.SharingActivity;
import com.example.habittracker.activities.profile.ProfileActivity;
import com.example.habittracker.utils.SharedInfo;
import com.google.firebase.firestore.FirebaseFirestore;
import com.robotium.solo.Solo;

import org.junit.After;
import org.junit.Before;
import org.junit.Rule;
import org.junit.Test;

import java.util.Arrays;
import java.util.HashMap;

public class ProgressActivityTest {


    private Solo solo;
    @Rule
    public ActivityTestRule<ProfileActivity> rule =
            new ActivityTestRule<>(ProfileActivity.class, true, true);
    User mockUser;

    /**
     * Runs before all tests and creates solo instance.
     * @throws Exception
     */
    @Before
    public void setUp() throws Exception{
        mockUser = new User("mockUser");
        SharedInfo.getInstance().setCurrentUser(mockUser);
        solo = new Solo(InstrumentationRegistry.getInstrumentation(),rule.getActivity());
        addMockUser();
    }

    /**
     * Closes the activity after each test
     * @throws Exception
     */
    @After
    public void tearDown() throws Exception{
        solo.finishOpenedActivities();
        deleteMockUser();
    }


    /**
     * Adds a mock user document to Firestore.
     */
    public void addMockUser() {
        FirebaseFirestore db = FirebaseFirestore.getInstance();
        HashMap<String, Object> mockDoc = new HashMap<>();
        mockDoc.put("followers", Arrays.asList("milkyman"));
        mockDoc.put("following", Arrays.asList("strangeman"));
        mockDoc.put("pendingFollowReqs", Arrays.asList("happyman"));
        mockDoc.put("pendingFollowerReqs", Arrays.asList("sadman", "stalkerman"));
        db.collection(DatabaseManager.get().getUsersColName()).document(mockUser.getUsername())
                .set(mockDoc);
        HashMap<String, Object> habitDoc = new HashMap<>();
        habitDoc.put("dateStarted", Arrays.asList(2021,11,1));
        habitDoc.put("display", "habit");
        habitDoc.put("reason", "");
        habitDoc.put("progress", 0);
        habitDoc.put("whatDays", Arrays.asList("Mon", "Wed"));
        db.collection(DatabaseManager.get().getUsersColName()).document(mockUser.getUsername()).collection("Habits").document("habit")
                .set(habitDoc);
    }

    /**
     * Deletes the mock user added to the database.
     */
    public void deleteMockUser() {
        FirebaseFirestore db = FirebaseFirestore.getInstance();
        db.collection(DatabaseManager.get().getUsersColName()).document(mockUser.getUsername()).collection("Habits").document("habit")
                .delete();
        db.collection(DatabaseManager.get().getUsersColName()).document(mockUser.getUsername())
                .delete();

    }

    /**
     * test each button on the navigation bar
     */
    @Test
    public void testProgress(){
        solo.assertCurrentActivity("Wrong Activity", ProfileActivity.class);
        solo.clickOnView(solo.getView(R.id.list));
        solo.assertCurrentActivity("Wrong Activity", ListActivity.class);
        solo.waitForText("habit");
        solo.clickOnText("habit");
        solo.assertCurrentActivity("Wrong Activity", HabitViewActivity.class);
        solo.clickOnView(solo.getView(R.id.see_progress_button));
        solo.assertCurrentActivity("Wrong Activity", ProgressTrackingActivity.class);
    }
}
