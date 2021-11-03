package com.example.habittracker.activities;

import androidx.appcompat.app.AppCompatActivity;

import android.content.Intent;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.Button;
import android.widget.TextView;

import com.example.habittracker.Habit;
import com.example.habittracker.NavBarManager;
import com.example.habittracker.R;
import com.example.habittracker.activities.fragments.HabitInputFragment;

public class HabitViewActivity extends AppCompatActivity implements HabitInputFragment.HabitInputDialogListener {

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_habit_view);

        // Bundle for intent's extra arguments
        Bundle b = this.getIntent().getExtras();
        Habit value = null; // or other values
        if(b != null)
            value =(Habit) b.getSerializable("habit");
        if(value == null)
            Log.d("Error","No habit given to progress activity.");
        //close activity
        finish();

        NavBarManager nav = new NavBarManager(this,findViewById(R.id.bottom_navigation));
        Button editButton = findViewById(R.id.editButton);
        editButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                new HabitInputFragment().show(getSupportFragmentManager(), "EDIT EVENT");
            }
        });

        Button seeProgressButton = findViewById(R.id.see_progress_button);
        seeProgressButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Intent intent = new Intent(getApplicationContext(),ProgressTrackingActivity.class);

                startActivity(intent);
            }
        });
    }

    @Override
    public void onOkPressed() {
        //Add activity
    }
}