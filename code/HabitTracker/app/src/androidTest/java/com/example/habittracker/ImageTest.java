package com.example.habittracker;

import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertTrue;

import android.app.AlertDialog;
import android.view.View;
import android.widget.EditText;
import android.widget.ListView;
import android.widget.Spinner;
import android.widget.TextView;

import androidx.test.platform.app.InstrumentationRegistry;
import androidx.test.rule.ActivityTestRule;

import com.example.habittracker.activities.ListActivity;
import com.example.habittracker.activities.eventlist.EventListActivity;
import com.example.habittracker.activities.profile.ProfileActivity;
import com.example.habittracker.testUtils.CustomActivityTestRule;
import com.example.habittracker.utils.DateConverter;
import com.example.habittracker.utils.SharedInfo;
import com.google.firebase.firestore.FirebaseFirestore;
import com.robotium.solo.Solo;

import com.example.habittracker.activities.HomeActivity;
import org.junit.After;
import org.junit.Before;
import org.junit.Rule;
import org.junit.Test;

import java.util.Arrays;
import java.util.Calendar;
import java.util.HashMap;


/**
 * Intent test for EventListActivity.
 */
public class ImageTest {
    private Solo solo;
    public User mockUser = new User("mockUser");

    @Rule
    public CustomActivityTestRule<ProfileActivity> rule = new CustomActivityTestRule<>(
            ProfileActivity.class, true, true, mockUser
    );

    /**
     * Runs before all tests and creates solo instance.
     * @throws Exception
     */
    @Before
    public void setUp() throws Exception{
        solo = new Solo(InstrumentationRegistry.getInstrumentation(),rule.getActivity());
        addMockUser();
    }

    /**
     * test EventListActivity
     */
    @Test
    public void testUploadImage() {
        solo.assertCurrentActivity("Wrong Activity", ProfileActivity.class);
        solo.clickOnView(solo.getView(R.id.list));
        solo.assertCurrentActivity("Wrong Activity", ListActivity.class);
        assertTrue(solo.waitForText("All Habits", 1, 2000));
        solo.waitForText("habit", 1, 2000);
        ListActivity activity = (ListActivity) solo.getCurrentActivity();
        final ListView HabitList = activity.list; // Get the listview
        Habit habit = (Habit) HabitList.getItemAtPosition(0); // Get item from first position
        assertEquals("habit", habit.getTitle());
        solo.clickInList(1);
        solo.waitForText("SEE EVENTS", 1, 2000);
        solo.clickOnButton("SEE EVENTS");
        solo.assertCurrentActivity("Wrong Activity", EventListActivity.class);

        solo.clickOnView(solo.getView(R.id.add_event_button));
        solo.clickOnView(solo.getView(R.id.addPhoto));
        solo.waitForText("Gallery", 1, 2000);
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
        habitDoc.put("title", "habit");
        habitDoc.put("reason", "");
        habitDoc.put("progress", "");
        habitDoc.put("whatDays", Arrays.asList("Mon", "Wed"));
        db.collection(DatabaseManager.get().getUsersColName()).document(mockUser.getUsername()).collection("Habits").document("habit")
                .set(habitDoc);

        HashMap<String, Object> mockDoc3 = new HashMap<>();
        mockDoc3.put("startDate", DateConverter.dateToArrayList(Calendar.getInstance().getTime()));
        mockDoc3.put("Habit", "habit");
        db.collection(DatabaseManager.get().getUsersColName()).document(mockUser.getUsername())
                .collection(DatabaseManager.get().getHabitsColName()).document("habit")
                .collection(DatabaseManager.get().getHabitEventsColName()).add(mockDoc3);
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
}
