package com.example.habittracker.activities.profile;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;

import android.os.Bundle;
import android.util.Log;
import android.util.Pair;
import android.view.View;
import android.widget.ArrayAdapter;
import android.widget.Button;
import android.widget.EditText;
import android.widget.LinearLayout;
import android.widget.ListView;

import com.example.habittracker.DatabaseManager;
import com.example.habittracker.R;
import com.example.habittracker.User;
import com.example.habittracker.utils.SharedInfo;
import com.example.habittracker.utils.SharingListCallback;
import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.Task;
import com.google.firebase.firestore.DocumentReference;
import com.google.firebase.firestore.DocumentSnapshot;
import com.google.firebase.firestore.FieldValue;

import java.util.ArrayList;
import java.util.HashMap;

public class ProfileFollowingActivity extends AppCompatActivity {
    public ListView followingListView;
    private ArrayAdapter<User> followingArrayAdapter;
    private ArrayList<User> followingList;
    private HashMap<User, Boolean> pendingStatus;     // maps a user to their follow status
                                                      // pendingStatus is true for a pending request
    private String TAG = "ProfileFollowingActivity";


    /**
     * Defines what to do when ProfileFollowingActivity is created
     * @param savedInstanceState
     */
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_profile_following);

        // get the View objects
        followingListView = findViewById(R.id.followingList);

        // initialize the array adapter
        followingList = new ArrayList<>();
        pendingStatus = new HashMap<>();
        followingArrayAdapter = new FollowingArrayAdapter(this, followingList, pendingStatus);
        followingListView.setAdapter(followingArrayAdapter);
        populateList();

    }

    /**
     * Populates the followingList. If the follow request is pending on user, pendingStatus for
     * that user is set to true.
     */
    private void populateList() {
        // get the list of people that the user is following
        DatabaseManager.get().getUserListItems(SharedInfo.getInstance().getCurrentUser().getUsername(),
                "following",
                new SharingListCallback() {
                    @Override
                    public void onCallbackSuccess(ArrayList<String> dataList) {
                        for (String userid: dataList) {
                            User user = new User(userid);
                            followingList.add(user);
                            pendingStatus.put(user, false);
                            followingArrayAdapter.notifyDataSetChanged();
                        }
                    }
                    @Override
                    public void onCallbackFailure(String reason) {
                        Log.d(TAG, reason);
                    }
                });
        // get the list of people that the user has requested to follow
        DatabaseManager.get().getUserListItems(SharedInfo.getInstance().getCurrentUser().getUsername(),
                "pendingFollowReqs",
                new SharingListCallback() {
                    @Override
                    public void onCallbackSuccess(ArrayList<String> dataList) {
                        for (String userid: dataList) {
                            User user = new User(userid);
                            followingList.add(user);
                            pendingStatus.put(user, true);
                            followingArrayAdapter.notifyDataSetChanged();
                        }
                    }
                    @Override
                    public void onCallbackFailure(String reason) {
                        Log.d(TAG, reason);
                    }
                });
    }
}