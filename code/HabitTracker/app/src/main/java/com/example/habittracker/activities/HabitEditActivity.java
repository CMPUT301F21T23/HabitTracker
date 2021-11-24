package com.example.habittracker.activities;

import androidx.appcompat.app.AppCompatActivity;
import androidx.appcompat.widget.SwitchCompat;
import androidx.core.content.ContextCompat;

import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.CompoundButton;
import android.widget.EditText;
import android.widget.TextView;
import android.widget.ToggleButton;

import com.example.habittracker.Habit;
import com.example.habittracker.NavBarManager;
import com.example.habittracker.R;
import com.example.habittracker.utils.CustomDatePicker;

import java.io.Serializable;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.Date;
import java.util.Locale;

public class HabitEditActivity extends AppCompatActivity {

    static final String EXTRA_UPDATE_STAT = "update_status";
    private ArrayList<String> daysChecked = new ArrayList<>();
    private Habit habit;

    @Override
    protected void onCreate(Bundle savedInstanceState) {

        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_habit_edit);
        //This is getting the passed Habit argument from the intent
        Intent intent = getIntent();
        habit = (Habit) intent.getSerializableExtra(ListActivity.EXTRA_HABIT);

        ToggleButton[] dayButtons = {
                findViewById(R.id.mon),
                findViewById(R.id.tue),
                findViewById(R.id.wed),
                findViewById(R.id.thu),
                findViewById(R.id.fri),
                findViewById(R.id.sat),
                findViewById(R.id.sun)
        };
        EditText habit_title = findViewById(R.id.habitTitle);
        EditText dft_text_reason = findViewById(R.id.habitReason);
        SwitchCompat dft_share_status = findViewById(R.id.switch1);

        if (habit != null) {
            // set habit title
            habit_title.setText(habit.getTitle());

            // set habit reason
            String reason = habit.getReason();
            if (reason != null) {
                // populate with information
                dft_text_reason.setText(reason);
            }
            else {
                dft_text_reason.setText(" ");
            }

            // Set date to start
            TextView dft_text_date = findViewById(R.id.date);
            Date startDate = habit.getStartDate();
            SimpleDateFormat sdf;
            String myFormat = "yyyy-MM-dd";
            if (startDate != null) {
                sdf = new SimpleDateFormat(myFormat, Locale.US);
                String dateStr = sdf.format(startDate);
                dft_text_date.setText(dateStr);
            }
            else {
                // should never happen as date is automatically set to the current day
                // when creating an event the date should default to today
                sdf = new SimpleDateFormat(myFormat, Locale.US);
                String dateStr = sdf.format(new Date());
                dft_text_date.setText(dateStr);
            }

            // color days of the week selected
            String[] daysExisting = {"Mon", "Tue", "Wed", "Thu", "Fri", "Sat", "Sun"};
            for (String day: habit.getWeekDays()) {
                int index = Arrays.asList(daysExisting).indexOf(day);
                if (index != -1) {
                    daysChecked.add(daysExisting[index]);
                    dayButtons[index].setChecked(true);
                    dayButtons[index].setBackgroundTintList(ContextCompat.getColorStateList(getApplicationContext(), R.color.spotify));
                }
            }

            // Set share status
            boolean isPublic = habit.isPublic();
            dft_share_status.setChecked(isPublic);
        }

        NavBarManager nav = new NavBarManager(this,findViewById(R.id.bottom_navigation));

        // Create listener for all of the togglebuttons
        CompoundButton.OnCheckedChangeListener listener = (buttonView, isChecked) -> {
            if (isChecked) {
                daysChecked.add((String) buttonView.getText());
                buttonView.setBackgroundTintList(ContextCompat.getColorStateList(getApplicationContext(), R.color.spotify));
            }
            else {
                daysChecked.remove((String) buttonView.getText());
                buttonView.setBackgroundTintList(ContextCompat.getColorStateList(getApplicationContext(), R.color.grey_1));
            }
        };
        // Add listener to all the buttons
        for (ToggleButton button : dayButtons) {
            button.setOnCheckedChangeListener(listener);
        }

        View view =  findViewById(R.id.date_holder);
        CustomDatePicker datePicker = new CustomDatePicker(this, view, R.id.date);

        Button discardButton = findViewById(R.id.discardBtn);
        discardButton.setOnClickListener(new View.OnClickListener() {
            /**
             * Performs an action (return to previous screen, all changes made discarded)
             * @param view {view} the view clicked, the discard button in this case.
             */
            @Override
            public void onClick(View view) {
                Intent discardIntent = new Intent(getApplicationContext(),HabitViewActivity.class);
                discardIntent.putExtra(ListActivity.EXTRA_HABIT, (Serializable) habit);
                startActivity(discardIntent);
            }
        });

        Button saveButton = findViewById(R.id.saveBtn);
        saveButton.setOnClickListener(new View.OnClickListener() {

            /**
             * Performs an action (saves information) when the button is pressed
             * @param view {view} the view clicked, the save button in this case.
             */
            @Override
            public void onClick(View view) {
                // send the newly edited habit to the habit view activity
                Intent saveIntent = new Intent(getApplicationContext(),HabitViewActivity.class);
                saveIntent.putExtra(EXTRA_UPDATE_STAT, true);
                saveIntent.putExtra("old_habit_title", habit.getTitle());

                // store the edited data in the habit
                habit.setTitle(habit_title.getText().toString());
                habit.setReason(dft_text_reason.getText().toString());
                habit.setWeekDays(daysChecked);
                habit.setShareStatus(dft_share_status.isChecked());
                habit.setStartDate(datePicker.getSetDate());

                // ready to send the habit
                saveIntent.putExtra(ListActivity.EXTRA_HABIT, (Serializable) habit);
                startActivity(saveIntent);

            }
        });

    }
}