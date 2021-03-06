package com.example.habittracker.activities.profile;

import androidx.appcompat.app.AppCompatActivity;

import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.ArrayAdapter;
import android.widget.Button;
import android.widget.ListView;

import com.example.habittracker.DatabaseManager;
import com.example.habittracker.R;
import com.example.habittracker.User;
import com.example.habittracker.utils.SharedInfo;
import com.example.habittracker.utils.SharingListCallback;

import java.util.ArrayList;

/**
 * Shows a list of the user's followers
 */
public class ProfileFollowersActivity extends AppCompatActivity {
    public ListView followersListView;
    private ArrayAdapter<User> followersArrayAdapter;
    private ArrayList<User> followersList;
    private String TAG = "ProfileFollowersActivity";
    private User currentUser = SharedInfo.getInstance().getCurrentUser();

    /**
     * Defines what to do when the ProfileFollowersActivity is created.
     * @param savedInstanceState
     */
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_profile_followers);


        // get the View objects
        followersListView = findViewById(R.id.followersList);

        // initialize array adapter
        followersList = new ArrayList<>();
        followersArrayAdapter = new FollowersArrayAdapter(this, followersList);
        followersListView.setAdapter(followersArrayAdapter);
        populateList();
    }

    /**
     * Retrives the list of followers from the database and appends them to the followersList of
     * this class.
     */
    private void populateList() {
        DatabaseManager.get().getUserListItems(SharedInfo.getInstance().getCurrentUser().getUsername(),
                "followers", new SharingListCallback() {
                    @Override
                    public void onCallbackSuccess(ArrayList<String> dataList) {
                        for (String userid : dataList) {
                            followersList.add(new User(userid));
                        }
                        followersArrayAdapter.notifyDataSetChanged();
                    }

                    @Override
                    public void onCallbackFailure(String reason) {
                        Log.d(TAG, reason);
                    }
                });
    }

}