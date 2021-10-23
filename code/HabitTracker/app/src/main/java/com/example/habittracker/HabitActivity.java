package com.example.habittracker;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;

import android.os.Bundle;
import android.util.Log;

import com.google.android.gms.tasks.OnFailureListener;
import com.google.android.gms.tasks.OnSuccessListener;
import com.google.firebase.firestore.CollectionReference;
import com.google.firebase.firestore.DocumentReference;
import com.google.firebase.firestore.FirebaseFirestore;

import java.util.Arrays;
import java.util.HashMap;

public class HabitActivity extends AppCompatActivity {

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_habit);

        // TEST DATABASE
        testDatabase();
    }

    void testDatabase() {
        testCreateUser1();
    }

    void testCreateUser1() {
        HashMap<String, Object> userDocument = new HashMap<>();
        HashMap<String, Object> habitDocument = new HashMap<>();
        HashMap<String, Object> habitEventDocument = new HashMap<>();

        // add data for the user document
        String userid = "user1";
        userDocument.put("following", Arrays.asList("user2"));
        userDocument.put("followers", Arrays.asList("user2"));
        userDocument.put("pendingFollowReqs", Arrays.asList("user3"));
        userDocument.put("pendingFollowerReqs", Arrays.asList("user3"));

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
}