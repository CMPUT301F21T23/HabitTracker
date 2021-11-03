package com.example.habittracker.activities;

import androidx.appcompat.app.AppCompatActivity;

import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.view.ViewGroup;
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

        //This is getting the passed HabitID argument from the intent bundle
        Intent intent = getIntent();
        Habit habit = (Habit) intent.getSerializableExtra(ListActivity.EXTRA_HABIT);

        if (habit != null) {
            // set habit title
            TextView habit_title = findViewById(R.id.habitTitle);
            habit_title.setText(habit.getTitle());

            // set habit reason
            TextView dft_text_reason = findViewById(R.id.habitReason);
            String reason = habit.getReason();
            if (reason != null) {
                // populate with information
                dft_text_reason.setText(reason);
            }
            else {
                View preset_text_reason = findViewById(R.id.reason);
                // if the reason field is empty, remove views that display reason
                ((ViewGroup) preset_text_reason.getParent()).removeView(preset_text_reason);
                ((ViewGroup) dft_text_reason.getParent()).removeView(dft_text_reason);
            }
        }

//        habitReason

        NavBarManager nav = new NavBarManager(this,findViewById(R.id.bottom_navigation));
        Button editButton = findViewById(R.id.editButton);
        editButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                new HabitInputFragment().show(getSupportFragmentManager(), "EDIT EVENT");
            }
        });
    }

    @Override
    public void onOkPressed(Habit habit) {
        //Add activity
    }
}