package com.example.habittracker.activities.profile;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;

import android.os.Bundle;
import android.util.Log;
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
import com.google.android.gms.tasks.OnSuccessListener;
import com.google.android.gms.tasks.Task;
import com.google.firebase.firestore.DocumentReference;
import com.google.firebase.firestore.DocumentSnapshot;
import com.google.firebase.firestore.FieldValue;

import java.util.ArrayList;
import java.util.Arrays;

public class ProfileFollowersActivity extends AppCompatActivity {
    private ListView followersListView;
    private ArrayAdapter<String> followersArrayAdapter;
    private ArrayList<String> followersList;
    private String TAG = "ProfileFollowersActivity";
    private LinearLayout usernameEntry;
    Button removeFollowerButton;
    private EditText usernameEditText;
    private Button confirmButton;
    private Button cancelButton;

    // TODO: the user will be retrieved using the SharedInfo class later
    private User currentUser = new User("user1", "password");
    private String currentUsername = currentUser.getUsername();

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_profile_followers);

        // finish the activity upon pressing the back button
        Button back_button = findViewById(R.id.back);
        back_button.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                finish();
            }
        });

        // get the View objects
        followersListView = findViewById(R.id.followersList);
        usernameEntry = findViewById(R.id.usernameEntry);
        removeFollowerButton = findViewById(R.id.removeFollower);
        usernameEditText = findViewById(R.id.username);
        confirmButton = findViewById(R.id.buttonConfirm);
        cancelButton = findViewById(R.id.buttonCancel);


        followersList = new ArrayList<>();
        followersArrayAdapter = new FollowersArrayAdapter(this, followersList);
        followersListView.setAdapter(followersArrayAdapter);
        populateList();

        removeFollowerOnRequest();
    }

    /**
     * Retrives the list of followers from the database and appends them to the followersList of
     * this class.
     */
    private void populateList() {
        // get the document for the current user
        DocumentReference docRef = DatabaseManager.get().getUsersColRef().document(currentUsername);

        // retrive the list of followers from the database and append each follower to followersList
        docRef.get().addOnCompleteListener(new OnCompleteListener<DocumentSnapshot>() {
            @Override
            public void onComplete(@NonNull Task<DocumentSnapshot> task) {
                if (task.isSuccessful()) {
                    DocumentSnapshot document = task.getResult();
                    if (document.exists()) {
                        ArrayList<String> followers = (ArrayList<String>) document.get("followers");
                        for (String follower : followers) {
                            followersList.add(follower);
                            followersArrayAdapter.notifyDataSetChanged();
                        }
//                        followersArrayAdapter.notifyDataSetChanged();
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
     * Removes a follower from the followers list
     */
    private void removeFollowerOnRequest() {
        // makes the user entry options visible when the Remove button is clicked
        removeFollowerButton.setOnClickListener(new View.OnClickListener() {
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
                boolean isValid = followersList.remove(username);
                if (isValid) {
                    followersArrayAdapter.notifyDataSetChanged();
                    // remove the follower from the database
                    DocumentReference docRef = DatabaseManager.get().getUsersColRef().document(currentUsername);
                    docRef.update("followers", FieldValue.arrayRemove(username));
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