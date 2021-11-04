package com.example.habittracker.activities.profile;

import androidx.appcompat.app.AppCompatActivity;

import android.os.Bundle;
import android.view.View;
import android.widget.ArrayAdapter;
import android.widget.Button;
import android.widget.ListView;

import com.example.habittracker.R;
import com.example.habittracker.User;

import java.util.ArrayList;

public class ProfileFollowRequestsActivity extends AppCompatActivity {
    private ListView pendingFollowersListView;
    private ArrayAdapter<User> pendingFollowersArrayAdapter;
    private ArrayList<User> pendingFollowersList;
    private String TAG = "ProfileFollowRequestsActivity";

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

        pendingFollowersList = new ArrayList<>();
        pendingFollowersArrayAdapter = new PendingFollowersArrayAdapter(this, pendingFollowersList);
        pendingFollowersListView.setAdapter(pendingFollowersArrayAdapter);
    }
}