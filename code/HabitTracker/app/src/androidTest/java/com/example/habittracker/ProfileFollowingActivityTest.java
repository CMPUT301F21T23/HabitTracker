package com.example.habittracker;

import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertFalse;
import static org.junit.Assert.assertTrue;

import android.app.Activity;
import android.widget.ListView;

import androidx.test.platform.app.InstrumentationRegistry;
import androidx.test.rule.ActivityTestRule;

import com.example.habittracker.activities.profile.ProfileActivity;
import com.example.habittracker.activities.profile.ProfileFollowersActivity;
import com.example.habittracker.activities.profile.ProfileFollowingActivity;
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

public class ProfileFollowingActivityTest {
    private Solo solo;
    User mockUser = new User("mockUser");
    @Rule
    public CustomActivityTestRule<ProfileActivity> rule =

            new CustomActivityTestRule<>(ProfileActivity.class, true, true, mockUser);
    String mockUser2 = "magic johnson";
    String mockUser3 = "michael jordan";


    /**
     * Runs before all tests and creates solo instance.
     * @throws Exception
     */
    @Before
    public void setUp() throws Exception{
        solo = new Solo(InstrumentationRegistry.getInstrumentation(),rule.getActivity());
        solo.clickOnButton("Following");
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
        ArrayList<String> emptyArrayList = new ArrayList<>();
        FirebaseFirestore db = FirebaseFirestore.getInstance();
        HashMap<String, Object> mockDoc = new HashMap<>();
        mockDoc.put("followers", emptyArrayList);
        mockDoc.put("following", Arrays.asList(mockUser2));
        mockDoc.put("pendingFollowReqs", Arrays.asList(mockUser3));
        mockDoc.put("pendingFollowerReqs", emptyArrayList);
        db.collection(DatabaseManager.get().getUsersColName()).document(mockUser.getUsername())
                .set(mockDoc);

        // second user
        HashMap<String, Object> mockDoc2 = new HashMap<>();
        mockDoc.put("followers", emptyArrayList);
        mockDoc.put("following", emptyArrayList);
        mockDoc.put("pendingFollowReqs", emptyArrayList);
        mockDoc.put("pendingFollowerReqs", emptyArrayList);
        db.collection(DatabaseManager.get().getUsersColName()).document(mockUser2)
                .set(mockDoc);

        // third user
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
     * Tests whether the following list is displayed correctly
     */
    @Test
    public void checkListItem() {
        solo.assertCurrentActivity("Wrong Activity", ProfileFollowingActivity.class);
        // following strange
        solo.waitForText(mockUser2, 1, 3000);
        // requested to follow happyman
        solo.waitForText(mockUser3, 1, 3000);

        // get the ProfileFollowingActivity to get its variables and methods
        ProfileFollowingActivity activity = (ProfileFollowingActivity) solo.getCurrentActivity();
        final ListView followingList = activity.followingListView;
        // following is shown before pending following
        User user1 = (User) followingList.getItemAtPosition(0);
        assertEquals(mockUser2, user1.getUsername());
        // pending request
        User user2 = (User) followingList.getItemAtPosition(1);
        assertEquals(mockUser3, user2.getUsername());
    }

    /**
     * Tests the Unfollow button on the ProfileFollowingActivity
     */
    @Test
    public void unfollowButton() {
        solo.assertCurrentActivity("Wrong Activity", ProfileFollowingActivity.class);
        // unfollow button should be attached to the following user
        assertTrue(solo.waitForText("Unfollow", 1, 5000));
        // cancel request button should be attached to the pending following user
        assertTrue(solo.waitForText(" Cancel ", 1, 5000));

        // get the ProfileFollowingActivity to get its variables and methods
        ProfileFollowingActivity activity = (ProfileFollowingActivity) solo.getCurrentActivity();
        final ListView followingList = activity.followingListView;

        // clicking the unfollow button should remove the user from the ProfileFollowingActivity
        solo.clickOnButton("Unfollow");
        // wait one second for the database operation to complete
        solo.waitForText("no text", 1, 3000);
        assertFalse(solo.searchText(mockUser2));

        // clicking the cancel request button should remove the pending user from the ProfileFollowingActivity
        solo.clickOnButton(" Cancel ");
        // wait one second for the database operation to complete
        solo.waitForText("no text", 1, 3000);
        assertFalse(solo.searchText(mockUser3));
    }
}
