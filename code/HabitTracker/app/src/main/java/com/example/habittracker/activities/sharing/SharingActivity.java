package com.example.habittracker.activities.sharing;

import androidx.appcompat.app.AppCompatActivity;

import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.ArrayAdapter;
import android.widget.Button;
import android.widget.ListView;

import com.example.habittracker.DatabaseManager;
import com.example.habittracker.Habit;
import com.example.habittracker.NavBarManager;
import com.example.habittracker.R;
import com.example.habittracker.activities.fragments.HabitInputFragment;
import com.example.habittracker.activities.fragments.SharingInputFragment;
import com.example.habittracker.utils.HabitCallback;
import com.example.habittracker.utils.SharedInfo;
import com.google.android.material.floatingactionbutton.FloatingActionButton;

import java.util.ArrayList;

public class SharingActivity extends AppCompatActivity {

    public ListView sharingListView;
    private ArrayAdapter<Habit> sharingArrayAdapter;
    private ArrayList<Habit> sharingList;
    private String TAG = "SharingActivity";

    /**
     * Defines what to do when SharingActivity is created.
     * @param savedInstanceState
     */
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_sharing);
        NavBarManager nav = new NavBarManager(this,findViewById(R.id.bottom_navigation));
        Button followButton = findViewById(R.id.follow_button);
        followButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                new SharingInputFragment().show(getSupportFragmentManager(), "FOLLOW");
            }
        });
        // get the View objects
        sharingListView = findViewById(R.id.sharing_list_view);

        // initialize the array adapter
        sharingList = new ArrayList<>();
        sharingArrayAdapter = new SharingArrayAdapter(this, sharingList);
        sharingListView.setAdapter(sharingArrayAdapter);

        // update the sharing list
        updateSharingList();
    }

    /**
     * Updates the sharingList property of the SharingActivity.
     */
    private void updateSharingList() {
        DatabaseManager.get().getFollowingHabits(
                SharedInfo.getInstance().getCurrentUser().getUsername(),
                new HabitCallback() {
                    @Override
                    public void onCallbackSuccess(Habit habit) {
                        sharingList.add(habit);
                        sharingArrayAdapter.notifyDataSetChanged();
                    }

                    @Override
                    public void onCallbackFailure(String reason) {
                        Log.d(TAG, reason);
                    }
                }
        );
    }
}