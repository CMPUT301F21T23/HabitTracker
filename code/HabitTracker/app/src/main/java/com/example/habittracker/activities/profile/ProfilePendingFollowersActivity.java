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

public class ProfilePendingFollowersActivity extends AppCompatActivity {
    private ListView pendingFollowersListView;
    private ArrayAdapter<User> pendingFollowersArrayAdapter;
    private ArrayList<User> pendingFollowersList;
    private String TAG = "ProfilePendingFollowersActivity";

    // TODO: the user will be retrieved using the SharedInfo class later
    private User currentUser = new User("user1");
    private String currentUsername = currentUser.getUsername();

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_profile_follow_requests);
        Button back_button = findViewById(R.id.back);
        back_button.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                finish();
            }
        });

        // get the View objects
        pendingFollowersListView = findViewById(R.id.pendingFollowersList);

        // TODO: to be removed
        SharedInfo.getInstance().setCurrentUser(currentUser);

        pendingFollowersList = new ArrayList<>();
        pendingFollowersArrayAdapter = new PendingFollowersArrayAdapter(this, pendingFollowersList);
        pendingFollowersListView.setAdapter(pendingFollowersArrayAdapter);

        populateList();
    }

    /**
     * Populates the data list shown by PendingFollowersArrayAdapter
     */
    void populateList() {
        // get the list of the current user's pending follow requests
        DatabaseManager.get().getPendingFollowers(currentUsername, new SharingListCallback() {
            @Override
            public void onCallbackSuccess(ArrayList<String> dataList) {
                for (String userid : dataList) {
                    pendingFollowersList.add(new User(userid));
                }
                pendingFollowersArrayAdapter.notifyDataSetChanged();
            }
            @Override
            public void onCallbackFailure(String reason) {
                Log.d(TAG, reason);
            }
        });
    }
}