package com.example.habittracker.activities.profile;

import androidx.appcompat.app.AppCompatActivity;

import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.widget.ArrayAdapter;
import android.widget.Button;
import android.widget.ListView;

import com.example.habittracker.DatabaseManager;
import com.example.habittracker.NavBarManager;
import com.example.habittracker.R;
import com.example.habittracker.activities.LoginActivity;
import com.example.habittracker.activities.eventlist.EventListActivity;
import com.example.habittracker.utils.SharedInfo;

import java.util.ArrayList;

public class ProfileActivity extends AppCompatActivity {

    // Variables
    ListView profileList;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        //setContentView(R.layout.profile.xml);
        setContentView(R.layout.activity_profile);
        NavBarManager nav = new NavBarManager(this,findViewById(R.id.bottom_navigation));
        
        Button followers_button = findViewById(R.id.followersButton);
        followers_button.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Intent intent = new Intent(getApplicationContext(), ProfileFollowersActivity.class);
                startActivity(intent);
            }
        });

        Button follower_requests_button = findViewById(R.id.followRequestsButton);
        follower_requests_button.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Intent intent = new Intent(getApplicationContext(), ProfilePendingFollowersActivity.class);
                startActivity(intent);
            }
        });

        Button following_button = findViewById(R.id.followingButton);
        following_button.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Intent intent = new Intent(getApplicationContext(), ProfileFollowingActivity.class);
                startActivity(intent);
            }
        });

        Button logout_button = findViewById(R.id.logoutButton);
        logout_button.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                // Log the user out

                // Set the global user instance
                SharedInfo.getInstance().clearCurrentUser();

                // Switch to login screen
                Intent intent = new Intent(getApplicationContext(), LoginActivity.class);
                startActivity(intent);
            }
        });

        /*
        Button delete_button = findViewById(R.id.delete_account);
        delete_button.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                // Delete the user from the database
                DatabaseManager db = DatabaseManager.get();
                String username = SharedInfo.getInstance().getCurrentUser().getUsername();
                if (username != null && !username.equals("")) {
                    db.deleteUserDocument(username);
                } else {
                    System.out.println("Trying to log out a null user :/");
                }

                // Log the user out

                // Set the global user instance
                SharedInfo.getInstance().clearCurrentUser();

                // Switch to login screen
                Intent intent = new Intent(getApplicationContext(), LoginActivity.class);
                startActivity(intent);
            }
        });
         */

    }
}