package com.example.habittracker;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;

import android.content.Intent;
import android.os.Bundle;
import android.util.Log;

import com.example.habittracker.activities.LoginActivity;
import com.example.habittracker.utils.BooleanCallback;
import com.example.habittracker.utils.CheckPasswordCallback;
import com.example.habittracker.utils.HabitEventListCallback;
import com.example.habittracker.utils.HabitListCallback;
import com.example.habittracker.utils.SharedInfo;
import com.example.habittracker.utils.UserDetailsCallback;
import com.google.android.gms.tasks.OnFailureListener;
import com.google.android.gms.tasks.OnSuccessListener;
import com.google.firebase.firestore.CollectionReference;
import com.google.firebase.firestore.DocumentReference;
import com.google.firebase.firestore.FieldValue;
import com.google.firebase.firestore.FirebaseFirestore;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.Calendar;
import java.util.HashMap;

import com.example.habittracker.activities.HomeActivity;

public class HabitActivity extends AppCompatActivity {

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_habit);

        // TEST DATABASE
        testDatabase();

        DatabaseManager db = DatabaseManager.get();
        /*
        db.getUserDetails("user1",new UserDetailsCallback() {
            @Override
            public void onCallbackSuccess(HashMap<String,Object> userDetails) {
                //Do what you need to do with your list
                Log.d("User",""+userDetails);
            }

            @Override
            public void onCallbackFailed() {
                Log.d("Error","Failed to get user");
            }
        });
        */
        NavBarManager nav = new NavBarManager(this,findViewById(R.id.bottom_navigation));
        Intent intent = new Intent(this, LoginActivity.class);
        startActivity(intent);
    }

    void testDatabase() {
        testCreateUser1();
        testUpdateUser1();
//        testDeleteUser1();
    }

    // demonstrates how to create a document with the DatabaseManager class
    void testCreateUser1() {
        HashMap<String, Object> userDocument = new HashMap<>();
        HashMap<String, Object> habitDocument = new HashMap<>();
        HashMap<String, Object> habitEventDocument = new HashMap<>();

        // add data for the user document
        String userid = "user1";
        userDocument.put("following", Arrays.asList("user2"));
        userDocument.put("followers", Arrays.asList("user2"));
        userDocument.put("pendingFollowReqs", Arrays.asList("user3"));
        userDocument.put("pendingFollowerReqs", Arrays.asList("user3", "user9", "user10"));

        // use database manager
        DatabaseManager.get().addUsersDocument(userid, userDocument);

        // add data for the habit document
        String title = "Habit 1";
        habitDocument.put("reason", "Reason 1");
        habitDocument.put("dateStarted", Arrays.asList(2021, 10, 22));  // YYYY/MM/DD
        habitDocument.put("whatDays", Arrays.asList("Monday", "Wednesday", "Saturday"));
        habitDocument.put("progress", 0);

        // use database manager
        DatabaseManager.get().addHabitDocument(userid, title, habitDocument);

        // add data for the habit event document
        habitEventDocument.put("comment", "Comment 1");
        habitEventDocument.put("image", "Image 1");
        habitEventDocument.put("location", "Location 1");

        // use database manager
        DatabaseManager.get().addHabitEventDocument(userid, title, habitEventDocument);
    }

    // demonstrates how to update particular documents in Firestore
    void testUpdateUser1() {
        // add a new user to the following list
        DatabaseManager.get().getUsersColRef().document("user1")
                .update("following", FieldValue.arrayUnion("user7"))
                .addOnSuccessListener(new OnSuccessListener<Void>() {
                    @Override
                    public void onSuccess(Void unused) {
                        Log.d("HabitActivity", "Following list updated");
                    }
                });
    }

    // demonstrates how to delete a user with the DatabaseManager class
    void testDeleteUser1() {
        String userid = "user1";
        DatabaseManager.get().deleteUserDocument(userid);
    }
}