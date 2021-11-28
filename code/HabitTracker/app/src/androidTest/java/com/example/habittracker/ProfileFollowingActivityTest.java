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

import java.util.Arrays;
import java.util.HashMap;

public class ProfileFollowingActivityTest {
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
        FirebaseFirestore db = FirebaseFirestore.getInstance();
        HashMap<String, Object> mockDoc = new HashMap<>();
        mockDoc.put("followers", Arrays.asList("milkyman"));
        mockDoc.put("following", Arrays.asList("strangeman"));
        mockDoc.put("pendingFollowReqs", Arrays.asList("happyman"));
        mockDoc.put("pendingFollowerReqs", Arrays.asList("sadman"));
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
        solo.assertCurrentActivity("Wrong Activity", ProfileFollowingActivity.class);
        solo.clickOnButton("Back");
        solo.assertCurrentActivity("Activity not switched properly", ProfileActivity.class);
    }

    /**
     * Tests whether the following list is displayed correctly
     */
    @Test
    public void checkListItem() {
        solo.assertCurrentActivity("Wrong Activity", ProfileFollowingActivity.class);
        // following strange
        solo.waitForText("strangeman", 1, 3000);
        // requested to follow happyman
        solo.waitForText("happyman", 1, 3000);

        // get the ProfileFollowingActivity to get its variables and methods
        ProfileFollowingActivity activity = (ProfileFollowingActivity) solo.getCurrentActivity();
        final ListView followingList = activity.followingListView;
        // following is shown before pending following
        User user1 = (User) followingList.getItemAtPosition(0);
        assertEquals("strangeman", user1.getUsername());
        // pending request
        User user2 = (User) followingList.getItemAtPosition(1);
        assertEquals("happyman", user2.getUsername());
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
        assertTrue(solo.waitForText("Cancel Request", 1, 5000));

        // get the ProfileFollowingActivity to get its variables and methods
        ProfileFollowingActivity activity = (ProfileFollowingActivity) solo.getCurrentActivity();
        final ListView followingList = activity.followingListView;

        // clicking the unfollow button should remove the user from the ProfileFollowingActivity
        solo.clickOnButton("Unfollow");
        // wait one second for the database operation to complete
        solo.waitForText("no text", 1, 1000);
        assertFalse(solo.searchText("strangeman"));

        // clicking the cancel request button should remove the pending user from the ProfileFollowingActivity
        solo.clickOnButton("Cancel Request");
        // wait one second for the database operation to complete
        solo.waitForText("no text", 1, 1000);
        assertFalse(solo.searchText("happyman"));
    }
}
