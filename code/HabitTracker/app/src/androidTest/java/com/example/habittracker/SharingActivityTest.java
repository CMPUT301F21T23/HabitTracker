package com.example.habittracker;


import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertFalse;
import static org.junit.Assert.assertTrue;

import android.app.Activity;
import android.widget.EditText;
import android.widget.ListView;

import androidx.test.platform.app.InstrumentationRegistry;
import androidx.test.rule.ActivityTestRule;

import com.example.habittracker.activities.fragments.SharingInputFragment;
import com.example.habittracker.activities.profile.ProfileActivity;
import com.example.habittracker.activities.profile.ProfileFollowingActivity;
import com.example.habittracker.activities.sharing.SharingActivity;

import com.example.habittracker.testUtils.CustomActivityTestRule;
import com.example.habittracker.utils.SharedInfo;
import com.google.firebase.firestore.CollectionReference;
import com.google.firebase.firestore.DocumentReference;
import com.google.firebase.firestore.FirebaseFirestore;
import com.robotium.solo.Solo;

import org.junit.After;
import org.junit.AfterClass;
import org.junit.Before;
import org.junit.BeforeClass;
import org.junit.Rule;
import org.junit.Test;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.HashMap;
import java.util.concurrent.TimeUnit;

public class SharingActivityTest {
    private Solo solo;

    public static String testUserid1 = "shane";        // represents the current user of the app
    public static String testUserid2 = "gary";         // represents a person testUserid1 is following
    public static String testUserid3 = "michelle";     // represents a person testUserid1 will request to follow
    public static String testHabit1;                   // represents a public habit for testUserid2
    public static String testHabit2;                   // represents a private habit for testUserid2
    public User currentUser = new User(testUserid1);

    @Rule
    public CustomActivityTestRule<SharingActivity> rule = new CustomActivityTestRule<>(
            SharingActivity.class, true, true, currentUser
    );

    @BeforeClass
    public static void setUpClass() {
        createTestDocs();
        // wait for db ops to complete
        try {
            TimeUnit.SECONDS.sleep(3);
        } catch (InterruptedException e) {
            e.printStackTrace();
        }
    }

    @AfterClass
    public static void tearDownClass() {
        deleteTestDocs();
    }

    /**
     * Runs before every test.
     * @throws Exception
     */
    @Before
    public void setUp() throws Exception{
        solo = new Solo(InstrumentationRegistry.getInstrumentation(), rule.getActivity());
    }

    @Test
    public void start() throws Exception{
        Activity activity = rule.getActivity();
    }

    public static void createTestDocs() {
        FirebaseFirestore db = FirebaseFirestore.getInstance();
        CollectionReference usersColRef = DatabaseManager.get().getUsersColRef();

        // create testUserDoc1
        HashMap<String, Object> testUserDoc1 = new HashMap<>();
        ArrayList<String> emptyFollowing = new ArrayList<>();
        ArrayList<String> emptyFollowers = new ArrayList<>();
        ArrayList<String> emptyPendingFollowReqs = new ArrayList<>();
        ArrayList<String> emptyPendingFollowerReqs = new ArrayList<>();
        testUserDoc1.put("following", Arrays.asList(testUserid2));
        testUserDoc1.put("followers", emptyFollowers);
        testUserDoc1.put("pendingFollowReqs", emptyPendingFollowReqs);
        testUserDoc1.put("pendingFollowerReqs", emptyPendingFollowerReqs);
        testUserDoc1.put("hashedPassword", "blah");
        DocumentReference docRef = usersColRef.document(testUserid1);
        docRef.set(testUserDoc1);

        // create testUserDoc2
        HashMap<String, Object> testUserDoc2 = new HashMap<>();
        testUserDoc2.put("following", emptyFollowing);
        testUserDoc2.put("followers", Arrays.asList(testUserid1));
        testUserDoc2.put("pendingFollowReqs", emptyPendingFollowReqs);
        testUserDoc2.put("pendingFollowerReqs", emptyPendingFollowerReqs);
        testUserDoc2.put("hashedPassword", "bleh");
        docRef = usersColRef.document(testUserid2);
        docRef.set(testUserDoc2);

        // create testUserDoc3
        HashMap<String, Object> testUserDoc3 = new HashMap<>();
        testUserDoc3.put("following", emptyFollowing);
        testUserDoc3.put("followers", emptyFollowers);
        testUserDoc3.put("pendingFollowReqs", emptyPendingFollowReqs);
        testUserDoc3.put("pendingFollowerReqs", emptyPendingFollowerReqs);
        testUserDoc3.put("hashedPassword", "meh");
        docRef = usersColRef.document(testUserid3);
        docRef.set(testUserDoc3);

        // create a public Habit for testUserid2
        CollectionReference habitsColRef = DatabaseManager.get().getHabitsColRef(testUserid2);
        testHabit1 = "Play basketball";
        HashMap<String, Object> testHabit1Doc = new HashMap<>();
        testHabit1Doc.put("reason", "I want to get more atheletic.");
        testHabit1Doc.put("dateStarted", Arrays.asList(2021, 10, 23));
        testHabit1Doc.put("isPublic", true);
        testHabit1Doc.put("whatDays", Arrays.asList("Mon", "Wed", "Sat"));
        testHabit1Doc.put("progress", 90);
        testHabit1Doc.put("display", testHabit1);
        docRef = usersColRef.document(testUserid2).collection(DatabaseManager.get().getHabitsColName())
                .document(testHabit1);
        docRef.set(testHabit1Doc);

        // create a private Habit for testUserid2
        testHabit2 = "Practice swimming";
        HashMap<String, Object> testHabit2Doc = new HashMap<>();
        testHabit2Doc.put("reason", "I want to reduce my body fat.");
        testHabit2Doc.put("dateStarted", Arrays.asList(2021, 10, 11));
        testHabit2Doc.put("isPublic", false);
        testHabit2Doc.put("whatDays", Arrays.asList("Sun", "Wed", "Sat"));
        testHabit2Doc.put("progress", 33);
        testHabit2Doc.put("display", testHabit2);
        docRef = usersColRef.document(testUserid2).collection(DatabaseManager.get().getHabitsColName())
                .document(testHabit2);
        docRef.set(testHabit2Doc);
    }

    public static void deleteTestDocs() {
        FirebaseFirestore db = FirebaseFirestore.getInstance();
        // delete testUserid1
        db.collection(DatabaseManager.get().getUsersColName()).document(testUserid1).delete();
        // delete the test habits of testUserid2
        db.collection(DatabaseManager.get().getUsersColName()).document(testUserid2)
                .collection(DatabaseManager.get().getHabitsColName())
                .document(testHabit1)
                .delete();
        db.collection(DatabaseManager.get().getUsersColName()).document(testUserid2)
                .collection(DatabaseManager.get().getHabitsColName())
                .document(testHabit2)
                .delete();
        // delete testUserid2
        db.collection(DatabaseManager.get().getUsersColName()).document(testUserid2).delete();
        // delete testUserid3
        db.collection(DatabaseManager.get().getUsersColName()).document(testUserid3).delete();
    }

    @Test
    public void checkListItem() {
        solo.assertCurrentActivity("Wrong activity", SharingActivity.class);
        // public habit of testUserid2 should appear on the screen
//        assertTrue(solo.waitForText(testHabit1, 1, 5000));

        // private habit of testUserid2 should not appear on the screen
        assertFalse(solo.searchText(testHabit2));

        // get the SharingActivity to get access to its methods and variables
        SharingActivity activity = (SharingActivity) solo.getCurrentActivity();
        final ListView sharingList = activity.sharingListView;

        Habit testHabit1Object = (Habit) sharingList.getItemAtPosition(0);
        assertEquals(testHabit1, testHabit1Object.getTitleDisplay());

        // progress for testHabit1 should be 90
        assertEquals((Integer) 90, testHabit1Object.getProgress());

        // owner of testHabit1Object should be testUserid2
        assertEquals(testUserid2, testHabit1Object.getUser().getUsername());
    }

    @Test
    public void followButton() {
        solo.assertCurrentActivity("Wrong activity", SharingActivity.class);
        solo.clickOnButton("Follow");

        // clicking on the follow button should load the SharingInputFragment
        assertTrue(solo.waitForFragmentByTag(SharingInputFragment.TAG));
    }

    @Test
    public void followUser() {
        solo.assertCurrentActivity("Wrong activity", SharingActivity.class);
        solo.clickOnButton("Follow");

        // clicking on the follow button should load the SharingInputFragment
        assertTrue(solo.waitForFragmentByTag(SharingInputFragment.TAG));

        // request to follow testUserid3
        solo.enterText((EditText) solo.getView(R.id.sharing_input_edit_text), testUserid3);
        solo.clickOnButton("SEND");
        // wait 3 seconds for database operations to complete
        solo.waitForText("no text", 1, 3000);

        // go to the ProfileFollowingActivity
        solo.clickOnView(solo.getView(R.id.profile));
        solo.assertCurrentActivity("Wrong Activity", ProfileActivity.class);
        solo.clickOnButton("Following");
        solo.assertCurrentActivity("Wrong Activity", ProfileFollowingActivity.class);

        // testUserid3 should be in the pending follow request list
        assertTrue(solo.waitForText(testUserid3, 1, 3000));
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
