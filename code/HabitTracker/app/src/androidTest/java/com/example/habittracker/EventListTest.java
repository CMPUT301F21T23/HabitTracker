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
import com.example.habittracker.activities.SharingActivity;
import com.example.habittracker.activities.eventlist.EventListActivity;
import com.example.habittracker.activities.fragments.AddEventFragment;
import com.example.habittracker.activities.profile.ProfileActivity;
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
 *
 * Please NOTE: This test only works when user is 'John_test_user'
 * In order to do that, comment out line 131 in EventListActivity.java
 * and uncomment line 129;
 * comment out line 111 in EventListActivity.java
 * and uncomment line 109;
 * comment out line 200 in AddEventFragment.java
 * and uncomment line 199.
 *
 * After testing, please revise the above changes.
 */
public class EventListTest {
    private Solo solo;
    @Rule
    public ActivityTestRule<EventListActivity> rule =
            new ActivityTestRule<>(EventListActivity.class, true, true);


//    /**
//     * Adds a mock user document to Firestore.
//     */
//    @Before
//    public void addMockTarget() throws Exception{
//        User mockUser = new User("yongquan");
//        SharedInfo.getInstance().setCurrentUser(mockUser);
//        FirebaseFirestore db = FirebaseFirestore.getInstance();
//        HashMap<String, Object> mockDoc = new HashMap<>();
//        mockDoc.put("followers", Arrays.asList("milkyman"));
//        mockDoc.put("following", Arrays.asList("strangeman"));
//        mockDoc.put("pendingFollowReqs", Arrays.asList("happyman"));
//        mockDoc.put("pendingFollowerReqs", Arrays.asList("sadman"));
//        db.collection(DatabaseManager.get().getUsersColName()).document(mockUser.getUsername())
//                .set(mockDoc);
//        String [] weekdays = {"MON","WED"};
//        Habit h1 = new Habit("Habit 1", "no reason", Calendar.getInstance().getTime(), weekdays);
//        HashMap<String, Object> mockDoc2 = new HashMap<>();
//        mockDoc2.put("dateStarted", DateConverter.dateToArrayList(Calendar.getInstance().getTime()));
//        mockDoc2.put("whatDays", weekdays);
//        db.collection(DatabaseManager.get().getUsersColName()).document(mockUser.getUsername())
//                .collection(DatabaseManager.get().getHabitsColName()).document(h1.getTitle()).set(mockDoc2);
//
//        HashMap<String, Object> mockDoc3 = new HashMap<>();
//        mockDoc3.put("startDate", DateConverter.dateToArrayList(Calendar.getInstance().getTime()));
//        mockDoc3.put("Habit", "Habit 1");
//        db.collection(DatabaseManager.get().getUsersColName()).document(mockUser.getUsername())
//                .collection(DatabaseManager.get().getHabitsColName()).document(h1.getTitle())
//                .collection(DatabaseManager.get().getHabitEventsColName()).add(mockDoc3);
//    }

    /**
     * Runs before all tests and creates solo instance.
     * @throws Exception
     */
    @Before
    public void setUp() throws Exception{
        solo = new Solo(InstrumentationRegistry.getInstrumentation(), rule.getActivity());
    }

    /**
     * test EventListActivity
     */
    @Test
    public void testEventListActivity() {
        solo.assertCurrentActivity("Wrong Activity", EventListActivity.class);
        solo.clickOnView(solo.getView(R.id.add_event_button));
        assertTrue(solo.waitForText("Add Event", 1, 2000));
        solo.clickOnView(solo.getView(android.R.id.button2));
        assertTrue(solo.waitForText("Habit 1", 1, 2000));
        EventListActivity activity = (EventListActivity) solo.getCurrentActivity();
        final ListView eventList = activity.eventList; // Get the listview
        HabitEvent event = (HabitEvent) eventList.getItemAtPosition(0); // Get item from first position
        assertEquals("Habit 1", event.getHabit());
        solo.clickInList(1);
        assertTrue(solo.waitForText("Edit event/Delete event", 1, 2000));
    }

    /**
     * test add new habit event
     */
    @Test
    public void testAddNewHabitEvent() {
        solo.assertCurrentActivity("Wrong Activity", EventListActivity.class);
        solo.clickOnView(solo.getView(R.id.add_event_button));
        solo.waitForText("Add Event",1,20000);
        solo.pressSpinnerItem(0,0);
        assertTrue(solo.isSpinnerTextSelected(0,"Habit 1"));
        solo.clickOnView(solo.getView(R.id.date_editText));
        solo.setDatePicker(0, 2021, 10, 1);
        solo.clickOnView(solo.getView(android.R.id.button1));
        assertTrue(solo.waitForText("2021-11-01",1,2000));
        solo.enterText((EditText) solo.getView(R.id.comment_body), "a comment");
        assertTrue(solo.waitForText("a comment",1,2000));
        // add the event
        solo.clickOnView(solo.getView(android.R.id.button1));

        //The listview can only display 2 items at a time, select 1 means the last item of listview
        solo.clickInList(0);
        solo.clickOnView(solo.getView(android.R.id.button3));
    }

    /**
     * test edit/delete habit event
     */
    @Test
    public void testEditDeleteHabitEvent() {
        solo.assertCurrentActivity("Wrong Activity", EventListActivity.class);
        solo.clickInList(0);
        solo.waitForText("Edit event/Delete event",1,2000);
        solo.pressSpinnerItem(0,0);
        assertTrue(solo.isSpinnerTextSelected(0,"Habit 1"));
        solo.clickOnView(solo.getView(R.id.date_editText));
        solo.setDatePicker(0, 2021, 10, 1);
        solo.clickOnView(solo.getView(android.R.id.button1));
        assertTrue(solo.waitForText("2021-11-01",1,2000));
        solo.clearEditText((EditText) solo.getView(R.id.comment_body));
        solo.enterText((EditText) solo.getView(R.id.comment_body), "another comment");
        assertTrue(solo.waitForText("another comment",1,2000));
        // add the event
        solo.clickOnView(solo.getView(android.R.id.button1));
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
