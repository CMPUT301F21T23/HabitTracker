package com.example.habittracker;

import static org.junit.Assert.assertFalse;
import static org.junit.Assert.assertTrue;

import android.widget.ListView;

import androidx.test.platform.app.InstrumentationRegistry;
import androidx.test.rule.ActivityTestRule;

import com.example.habittracker.activities.profile.ProfileActivity;
import com.example.habittracker.activities.profile.ProfileFollowersActivity;
import com.example.habittracker.activities.profile.ProfileFollowingActivity;
import com.example.habittracker.utils.SharedInfo;
import com.google.firebase.firestore.FirebaseFirestore;
import com.robotium.solo.Solo;

import org.junit.After;
import org.junit.Before;
import org.junit.Rule;
import org.junit.Test;

import java.util.Arrays;
import java.util.HashMap;

public class ProfileFollowersActivityTest {
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
        solo.clickOnButton("Followers");
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
        solo.assertCurrentActivity("Wrong Activity", ProfileFollowersActivity.class);
        solo.clickOnButton("Back");
        solo.assertCurrentActivity("Activity not switched properly", ProfileActivity.class);
    }

    /**
     * Tests whether followers are shown correctly in ProfileFollowersActivity
     */
    @Test
    public void checkListItem() {
        solo.assertCurrentActivity("Wrong Activity", ProfileFollowersActivity.class);
        // follower - milkyman
        assertTrue(solo.waitForText("milkyman", 1, 3000));
    }

    /**
     * Tests the Remove follower button on ProfileFollowersActivity
     */
    @Test
    public void removeFollowerButton() {
        solo.assertCurrentActivity("Wrong Activity", ProfileFollowersActivity.class);
        // Remove button should be attached to the follower
        assertTrue(solo.waitForText("Remove", 1, 5000));

        // get the ProfileFollowersActivity to get its variables and methods
        ProfileFollowersActivity activity = (ProfileFollowersActivity) solo.getCurrentActivity();
        final ListView followersList = activity.followersListView;

        // clicking on the Remove button should remove the follower from the list
        solo.clickOnButton("Remove");
        // wait one second for the database operation to complete
        solo.waitForText("no text", 1, 1000);
        assertFalse(solo.searchText("milkyman"));
    }
}
