package com.example.habittracker.activities;

import androidx.appcompat.app.AppCompatActivity;
import androidx.appcompat.widget.SwitchCompat;
import androidx.core.content.ContextCompat;

import android.content.Intent;
import android.graphics.drawable.RotateDrawable;
import android.os.Build;
import android.os.Bundle;

import android.util.Log;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.ProgressBar;
import android.widget.TextView;


import com.example.habittracker.DatabaseManager;

import com.example.habittracker.Habit;
import com.example.habittracker.HabitEvent;
import com.example.habittracker.NavBarManager;
import com.example.habittracker.R;
import com.example.habittracker.activities.eventlist.EventListActivity;
import com.example.habittracker.activities.fragments.HabitInputFragment;
import com.example.habittracker.activities.tracking.ProgressUpdater;
import com.example.habittracker.activities.tracking.ProgressUtil;
import com.example.habittracker.utils.HabitDeleteCallback;
import com.example.habittracker.utils.HabitEventListCallback;
import com.example.habittracker.utils.HabitListCallback;
import com.example.habittracker.utils.SharedInfo;

import java.io.Serializable;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.Calendar;
import java.util.Date;
import java.util.HashMap;
import java.util.Locale;

/**
 * HabitViewActivity class: the activity that displays details concerning a Habit
 */
public class HabitViewActivity extends AppCompatActivity {

    private Habit habit;

    /**
     * Populates the screen with data regarding the specified habit. Some fields are optional.
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
            ProgressUpdater updater = new ProgressUpdater(habit);
            updater.update();
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

            // Set share status
            SwitchCompat dft_share_status = findViewById(R.id.switch1);
            boolean isPublic = habit.isPublic();
            dft_share_status.setChecked(isPublic);
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
                Intent intent = new Intent(getApplicationContext(),HabitEditActivity.class);
                intent.putExtra(ListActivity.EXTRA_HABIT, (Serializable) habit);
                startActivity(intent);
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
                DatabaseManager
                        .get()
                        .deleteHabitDocument(
                                SharedInfo.getInstance().getCurrentUser().getUsername(),
                                habit.getTitle(),
                                new HabitDeleteCallback() {
                                    @Override
                                    public void onCallbackSuccess() {
                                        Intent intent = new Intent(getApplicationContext(),ListActivity.class);
                                        startActivity(intent);
                                    }

                                    @Override
                                    public void onCallbackFailure(String reason) {

                                    }
                                });
            }
        });

        Button seeProgressButton = findViewById(R.id.see_progress_button);
        seeProgressButton.setOnClickListener(new View.OnClickListener() {

            /**
             * Performs an action (moves user to progress graph view) when the progress button is clicked
             * @para view {@code view}  the view that was clicked, the progress button view.
             */
            @Override
            public void onClick(View view) {
                Intent intent = new Intent(getApplicationContext(),ProgressTrackingActivity.class);
                Bundle bundle = new Bundle();
                bundle.putSerializable("habit", habit);
                intent.putExtras(bundle);
                startActivity(intent);
            }
        });

        // get the update status from the bundle.
        boolean updated = intent.getBooleanExtra(HabitEditActivity.EXTRA_UPDATE_STAT, false);
        if (updated) {
            String oldTitle = intent.getStringExtra("old_habit_title");
            onOkPressed(habit, oldTitle);
        }

        Button seeEventsButton = findViewById(R.id.see_event_button);
        seeEventsButton.setOnClickListener(new View.OnClickListener() {

            /**
             * Performs an action (moves user to events list) when the event button is clicked
             * @para view {@code view}  the view that was clicked, the progress button view.
             */
            @Override
            public void onClick(View view) {
                Intent intent = new Intent(getApplicationContext(), EventListActivity.class);
                Bundle bundle = new Bundle();
                bundle.putSerializable("habit", habit);
                intent.putExtras(bundle);
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
    public void onOkPressed(Habit habit, String prevTitle) {
        String newTitle = habit.getTitle();

        HashMap<String, Object> habitHm= habit.toDocument();
        DatabaseManager
                .get()
                .updateHabitDocument(
                        SharedInfo.getInstance().getCurrentUser().getUsername(),
                        prevTitle,
                        newTitle,
                        habitHm);
    }

}