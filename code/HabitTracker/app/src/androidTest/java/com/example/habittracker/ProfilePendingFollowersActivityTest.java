package com.example.habittracker;

import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertFalse;
import static org.junit.Assert.assertTrue;

import android.widget.ListView;

import androidx.test.platform.app.InstrumentationRegistry;
import androidx.test.rule.ActivityTestRule;

import com.example.habittracker.activities.profile.ProfileActivity;
import com.example.habittracker.activities.profile.ProfileFollowingActivity;
import com.example.habittracker.activities.profile.ProfilePendingFollowersActivity;
import com.example.habittracker.testUtils.CustomActivityTestRule;
import com.example.habittracker.utils.SharedInfo;
import com.google.firebase.firestore.FirebaseFirestore;
import com.robotium.solo.Solo;

import org.junit.After;
import org.junit.Before;
import org.junit.Rule;
import org.junit.Test;

import java.util.Arrays;
import java.util.HashMap;

public class ProfilePendingFollowersActivityTest {
    private Solo solo;
    User mockUser = new User("mockUser");
    @Rule
    public CustomActivityTestRule<ProfileActivity> rule =
            new CustomActivityTestRule<>(ProfileActivity.class, true, true,mockUser);


    /**
     * Runs before all tests and creates solo instance.
     * @throws Exception
     */
    @Before
    public void setUp() throws Exception{
        solo = new Solo(InstrumentationRegistry.getInstrumentation(),rule.getActivity());
        solo.clickOnButton("Requests");
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
    }

    /**
     * Deletes the mock user added to the database.
     */
    public void deleteMockUser() {
        FirebaseFirestore db = FirebaseFirestore.getInstance();
        db.collection(DatabaseManager.get().getUsersColName()).document(mockUser.getUsername())
                .delete();
    }

    /**
     * Tests whether the back button works correctly
     */
    @Test
    public void backButton() {
        solo.assertCurrentActivity("Wrong Activity", ProfilePendingFollowersActivity.class);
        solo.clickOnButton("Back");
        solo.assertCurrentActivity("Activity not switched properly", ProfileActivity.class);
    }

    /**
     * Tests whether follow requests are shown accurately
     */
    @Test
    public void checkListItem() {
        solo.assertCurrentActivity("Wrong Activity", ProfilePendingFollowersActivity.class);
        // requesters - sadman, stalkerman
        solo.waitForText("sadman", 1, 3000);
        solo.waitForText("stalkerman", 1, 3000);

        // get the ProfileFollowingActivity to get its variables and methods
        ProfilePendingFollowersActivity activity = (ProfilePendingFollowersActivity) solo.getCurrentActivity();
        final ListView requestList = activity.pendingFollowersListView;

        User user1 = (User) requestList.getItemAtPosition(0);
        assertEquals("sadman", user1.getUsername());
        User user2 = (User) requestList.getItemAtPosition(1);
        assertEquals("stalkerman", user2.getUsername());
    }

    /**
     * Tests the Accept/Decline buttons on ProfilePendingFollowersActivity.
     */
    @Test
    public void accpetDeclineButton() {
        solo.assertCurrentActivity("Wrong Activity", ProfilePendingFollowersActivity.class);
        // Accept/Decline buttons show up on screen
        assertTrue(solo.waitForText("Accept", 1, 3000));
        assertTrue(solo.waitForText("Decline", 1, 3000));

        // pressing the Accept/Decline button shown remove the request from the screen
        solo.clickOnButton("Accept");
        // wait one second for the database operation to complete
        solo.waitForText("no text", 1, 1000);
        assertFalse(solo.searchText("sadman"));

        solo.clickOnButton("Decline");
        // wait one second for the database operation to complete
        solo.waitForText("no text", 1, 1000);
        assertFalse(solo.searchText("stalkerman"));
    }
}
