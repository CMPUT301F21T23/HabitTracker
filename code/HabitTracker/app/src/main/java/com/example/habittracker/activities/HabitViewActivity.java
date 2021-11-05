package com.example.habittracker.activities;

import androidx.appcompat.app.AppCompatActivity;
import androidx.core.content.ContextCompat;

import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.TextView;

import com.example.habittracker.DatabaseManager;
import com.example.habittracker.Habit;
import com.example.habittracker.NavBarManager;
import com.example.habittracker.R;
import com.example.habittracker.activities.fragments.HabitInputFragment;
import com.google.android.gms.common.util.ArrayUtils;

import java.io.Serializable;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.Date;
import java.util.HashMap;
import java.util.Locale;

/**
 * HabitViewActivity class: the activity that displays details concerning a Habit
 */
public class HabitViewActivity extends AppCompatActivity implements HabitInputFragment.HabitInputDialogListener {

    private Habit habit;
    /**
     * Populates the screen with data reagrding the specified habit. Some fields are optional.
     * It may (or may not) include date, reason, link to progress, days of the week to perform habit
     * @param savedInstanceState {Bundle} needed to initiate activity
     */
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_habit_view);

        //This is getting the passed HabitID argument from the intent bundle
        Intent intent = getIntent();
        habit = (Habit) intent.getSerializableExtra(ListActivity.EXTRA_HABIT);

        if (habit != null) {
            // set habit title
            TextView habit_title = findViewById(R.id.habitTitle);
            habit_title.setText(habit.getTitleDisplay());

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

            // Set date to start
            TextView dft_text_date = findViewById(R.id.date);
            Date startDate = habit.getStartDate();

            if (startDate != null) {
                String myFormat = "yyyy-MM-dd";
                SimpleDateFormat sdf = new SimpleDateFormat(myFormat, Locale.US);
                String dateStr = sdf.format(startDate);
                dft_text_date.setText(dateStr);
            }
            else {
                TextView preset_text_date = findViewById(R.id.dateToStart);
                ((ViewGroup) preset_text_date.getParent()).removeView(preset_text_date);
            }

            // color days of the week selected
            Button[] dayButtons = {
                    findViewById(R.id.mon),
                    findViewById(R.id.tue),
                    findViewById(R.id.wed),
                    findViewById(R.id.thu),
                    findViewById(R.id.fri),
                    findViewById(R.id.sat),
                    findViewById(R.id.sun)
            };
            String[] daysExisting = {"Mon", "Tue", "Wed", "Thu", "Fri", "Sat", "Sun"};
            for (String day: habit.getWeekDays()) {
                int index = Arrays.asList(daysExisting).indexOf(day);
                if (index != -1) {
                    dayButtons[index].setBackgroundTintList(ContextCompat.getColorStateList(getApplicationContext(), R.color.spotify));
                }
            }
        }

        NavBarManager nav = new NavBarManager(this,findViewById(R.id.bottom_navigation));
        Button editButton = findViewById(R.id.editBtn);
        editButton.setOnClickListener(new View.OnClickListener() {
            /**
             * Performs an action (pop up edit fragment, send info) when the edit button is pressed
             * @param view {view} the view clicked, the edit button in this case.
             */
            @Override
            public void onClick(View view) {
                Bundle bundle = new Bundle();
                bundle.putString("old_habit_title", habit.getTitle());

                HabitInputFragment hif = new HabitInputFragment();
                hif.setArguments(bundle);
                hif.show(getSupportFragmentManager(), "EDIT EVENT");
            }
        });

        Button deleteButton = findViewById(R.id.deleteBtn);
        deleteButton.setOnClickListener(new View.OnClickListener() {
            /**
             * Performs an action (pop up edit fragment, send info) when the edit button is pressed
             * @param view {view} the view clicked, the edit button in this case.
             */
            @Override
            public void onClick(View view) {
                DatabaseManager.get().deleteHabitDocument("Pao_Dummy", habit.getTitle());
                Intent intent = new Intent(getApplicationContext(),ListActivity.class);
                startActivity(intent);
            }
        });
    }

    /**
     * Performs an action when the user is done editing a habit.
     * The habit will be updated
     * @param habit     {Habit}
     * @param prevTitle {String}    The title of the previously modified habit
     */
    @Override
    public void onOkPressed(Habit habit, String prevTitle) {
        String newTitle = habit.getTitle();

        HashMap<String, Object> habitHm= habit.toDocument();
        DatabaseManager.get().updateHabitDocument("Pao_Dummy", prevTitle, newTitle, habitHm);

        Intent intent = new Intent(getApplicationContext(),ListActivity.class);
        startActivity(intent);
    }

}