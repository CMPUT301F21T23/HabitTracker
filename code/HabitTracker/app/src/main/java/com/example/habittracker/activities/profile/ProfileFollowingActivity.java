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
import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.Task;
import com.google.firebase.firestore.DocumentReference;
import com.google.firebase.firestore.DocumentSnapshot;
import com.google.firebase.firestore.FieldValue;

import java.util.ArrayList;
import java.util.HashMap;

public class ProfileFollowingActivity extends AppCompatActivity {
    private ListView followingListView;
    private ArrayAdapter<String> followingArrayAdapter;
    private ArrayList<String> followingList;
    private HashMap<String, Boolean> pendingStatus;     // maps a username to their follow status
    private String TAG = "ProfileFollowingActivity";
    private LinearLayout usernameEntry;
    Button unfollowButton;
    private EditText usernameEditText;
    private Button confirmButton;
    private Button cancelButton;

    // TODO: the user will be retrieved using the SharedInfo class later
    private User currentUser = new User("user1");
    private String currentUsername = currentUser.getUsername();

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_profile_following);
        Button back_button = findViewById(R.id.back);
        back_button.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                finish();
            }
        });

        // get the View objects
        followingListView = findViewById(R.id.followingList);
        usernameEntry = findViewById(R.id.usernameEntry);
        confirmButton = findViewById(R.id.buttonConfirm);
        cancelButton = findViewById(R.id.buttonCancel);
        usernameEditText = findViewById(R.id.username);
        unfollowButton = findViewById(R.id.unfollowButton);

        followingList = new ArrayList<>();
        pendingStatus = new HashMap<>();
        followingArrayAdapter = new FollowingArrayAdapter(this, followingList, pendingStatus);
        followingListView.setAdapter(followingArrayAdapter);
        populateList();

        unfollowOnRequest();
    }

    /**
     * Populates the followingList. If the follow request is pending on user, pendingStatus for
     * that user is set to true.
     */
    private void populateList() {
        // get the document for the current user
        DocumentReference docRef = DatabaseManager.get().getUsersColRef().document(currentUsername);

        // retrieve the list of following and pending follow requests for the current user
        docRef.get().addOnCompleteListener(new OnCompleteListener<DocumentSnapshot>() {
            @Override
            public void onComplete(@NonNull Task<DocumentSnapshot> task) {
                if (task.isSuccessful()) {
                    DocumentSnapshot document = task.getResult();
                    if (document.exists()) {
                        // perform actions for the users that the current user is already following
                        ArrayList<String> following = (ArrayList<String>) document.get("following");
                        for (String user : following) {
                            followingList.add(user);
                            pendingStatus.put(user, false);
                            followingArrayAdapter.notifyDataSetChanged();
                        }

                        // perform actions for pending follow requests
                        ArrayList<String> pending = (ArrayList<String>) document.get("pendingFollowReqs");
                        for (String user : pending) {
                            followingList.add(user);
                            pendingStatus.put(user, true);
                            followingArrayAdapter.notifyDataSetChanged();
                        }
                    } else {
                        Log.d(TAG, "No user document for username " + currentUsername);
                    }
                } else {
                    Log.d(TAG, "Followers retrieval failed with: ", task.getException());
                }
            }
        });
    }

    /**
     * Adds listeners to the buttons of ProfileFollowingActivity to unfollow a user when requested.
     */
    public void unfollowOnRequest() {
        // makes the user entry options visible when the Remove button is clicked
        unfollowButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                usernameEntry.setVisibility(View.VISIBLE);
            }
        });

        // take actions when the Confirm button is pressed
        confirmButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                String username = usernameEditText.getText().toString();
                // update the followersList of ProfileFollowersActivity
                boolean isValid = followingList.remove(username);
                if (isValid) {
                    DocumentReference docRef = DatabaseManager.get().getUsersColRef().document(currentUsername);
                    if (pendingStatus.get(username) == true) {
                        // cancel follow request
                        docRef.update("pendingFollowReqs", FieldValue.arrayRemove(username));
                    } else {
                        // unfollow user
                        docRef.update("following", FieldValue.arrayRemove(username));
                    }
                    pendingStatus.get(username);
                    followingArrayAdapter.notifyDataSetChanged();
                } else {
                    // TODO: Let the user know that they entered an invalid username
                    Log.d(TAG, "Invalid username entered");
                }
                // clear the EditText view and make entry options invisible again
                usernameEditText.getText().clear();
                usernameEntry.setVisibility(View.INVISIBLE);
            }
        });

        // take actions when the Cancel button is pressed
        cancelButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                usernameEditText.getText().clear();
                usernameEntry.setVisibility(View.INVISIBLE);
            }
        });
    }
}