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

import java.util.ArrayList;
import java.util.Arrays;
import java.util.HashMap;

public class ProfilePendingFollowersActivityTest {
    private Solo solo;
    User mockUser = new User("mockUser");
    @Rule
    public CustomActivityTestRule<ProfileActivity> rule =
            new CustomActivityTestRule<>(ProfileActivity.class, true, true, mockUser);
    String mockUser2 = "anthony johnson";
    String mockUser3 = "daniel cormier";


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
        ArrayList<String> emptyList = new ArrayList<>();
        FirebaseFirestore db = FirebaseFirestore.getInstance();
        HashMap<String, Object> mockDoc = new HashMap<>();
        mockDoc.put("followers", emptyList);
        mockDoc.put("following", emptyList);
        mockDoc.put("pendingFollowReqs", emptyList);
        mockDoc.put("pendingFollowerReqs", Arrays.asList(mockUser2, mockUser3));
        db.collection(DatabaseManager.get().getUsersColName()).document(mockUser.getUsername())
                .set(mockDoc);

        mockDoc.put("followers", emptyList);
        mockDoc.put("following", emptyList);
        mockDoc.put("pendingFollowReqs", emptyList);
        mockDoc.put("pendingFollowerReqs", emptyList);
        db.collection(DatabaseManager.get().getUsersColName()).document(mockUser2)
                .set(mockDoc);
        db.collection(DatabaseManager.get().getUsersColName()).document(mockUser3)
                .set(mockDoc);
    }

    /**
     * Deletes the mock user added to the database.
     */
    public void deleteMockUser() {
        FirebaseFirestore db = FirebaseFirestore.getInstance();
        db.collection(DatabaseManager.get().getUsersColName()).document(mockUser.getUsername())
                .delete();
        db.collection(DatabaseManager.get().getUsersColName()).document(mockUser2)
                .delete();
        db.collection(DatabaseManager.get().getUsersColName()).document(mockUser3)
                .delete();
    }

    /**
     * Tests whether follow requests are shown accurately
     */
    @Test
    public void checkListItem() {
        solo.assertCurrentActivity("Wrong Activity", ProfilePendingFollowersActivity.class);
        // requesters - sadman, stalkerman
        solo.waitForText(mockUser2, 1, 3000);
        solo.waitForText(mockUser3, 1, 3000);

        // get the ProfileFollowingActivity to get its variables and methods
        ProfilePendingFollowersActivity activity = (ProfilePendingFollowersActivity) solo.getCurrentActivity();
        final ListView requestList = activity.pendingFollowersListView;

        User user1 = (User) requestList.getItemAtPosition(0);
        assertEquals(mockUser2, user1.getUsername());
        User user2 = (User) requestList.getItemAtPosition(1);
        assertEquals(mockUser3, user2.getUsername());
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
        assertFalse(solo.searchText(mockUser2));

        solo.clickOnButton("Decline");
        // wait one second for the database operation to complete
        solo.waitForText("no text", 1, 1000);
        assertFalse(solo.searchText(mockUser3));
    }
}
