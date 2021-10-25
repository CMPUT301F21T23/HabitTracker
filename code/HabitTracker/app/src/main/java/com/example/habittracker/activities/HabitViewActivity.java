package com.example.habittracker.activities;

import androidx.appcompat.app.AppCompatActivity;

import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.TextView;

import com.example.habittracker.NavBarManager;
import com.example.habittracker.R;
import com.example.habittracker.activities.fragments.HabitInputFragment;

public class HabitViewActivity extends AppCompatActivity implements HabitInputFragment.HabitInputDialogListener {

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_habit_view);

        //This is getting the passed HabitID argument from the intent bundle
        Bundle b = this.getIntent().getExtras();
        String value = "Habit"; // or other values
        if(b != null)
            value = b.getString("HabitID");
        TextView habit_title = findViewById(R.id.habitTitle);
        habit_title.setText(value);

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
    public void onOkPressed() {
        //Add activity
    }
}