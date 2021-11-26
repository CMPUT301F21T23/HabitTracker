package com.example.habittracker.activities;

import androidx.annotation.Nullable;
import androidx.appcompat.app.AppCompatActivity;

import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.Button;
import android.widget.ListView;

import com.example.habittracker.DatabaseManager;
import com.example.habittracker.Habit;

import com.example.habittracker.NavBarManager;
import com.example.habittracker.R;
import com.example.habittracker.activities.eventlist.EventListActivity;
import com.example.habittracker.activities.tracking.ProgressUpdater;
import com.example.habittracker.utils.CustomHabitList;
import com.example.habittracker.utils.DateConverter;
import com.example.habittracker.utils.HabitListCallback;
import com.example.habittracker.utils.SharedInfo;
import com.google.firebase.firestore.EventListener;
import com.google.firebase.firestore.FirebaseFirestoreException;
import com.google.firebase.firestore.QueryDocumentSnapshot;
import com.google.firebase.firestore.QuerySnapshot;


import java.io.Serializable;
import java.util.ArrayList;
import java.util.Date;

/**
 * This activity, HomeActivity displays the "home" aka, the daily view of activities
 */
public class HomeActivity extends AppCompatActivity {

    private ArrayList<Habit> habitList = new ArrayList<>();
    private ListView list = null;
    private CustomHabitList habitAdapter;

    /**
     * Populates the screen's interactables (nav bar click, button clicks, etc..)
     * @param savedInstanceState    {@code Bundle}  the bundle info used to create
     */
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_home);
        NavBarManager nav = new NavBarManager(this,findViewById(R.id.bottom_navigation));

        list = findViewById(R.id.today_habits_list_view);

        this.habitAdapter = new CustomHabitList(this, habitList);
        this.list.setAdapter(habitAdapter);

        this.list.setOnItemClickListener(new AdapterView.OnItemClickListener() {
            /**
             * Performs an action (open habit view details) when click on habit
             * @param adapter   {@code AdapterView<?> } the adapter view related to this list
             * @param v         {@code View}            the view clicked
             * @param position  {@code int}             the index of the item clicked
             * @param id        {@code long}            the id
             */
            public void onItemClick(AdapterView<?> adapter, View v, int position, long id) {
                Intent intent = new Intent(getApplicationContext(),HabitViewActivity.class);
                Bundle bundle = new Bundle();
                bundle.putSerializable("habit", (Serializable) list.getItemAtPosition(position));
                intent.putExtras(bundle);
                startActivity(intent);
            }
        });

        Button event_list_button = findViewById(R.id.event_list_button);
        event_list_button.setOnClickListener(new View.OnClickListener() {

            /**
             * Performs an action (takes user to event list ivew) when milestone button is pressed.
             * @param view  {@code View} the view that was pressed
             */
            @Override
            public void onClick(View view) {
                Intent intent = new Intent(getApplicationContext(), EventListActivity.class);
                startActivity(intent);
            }
        });

        DatabaseManager
                .get()
                .getAllHabits(SharedInfo.getInstance().getCurrentUser().getUsername(), new HabitListCallback() {
                    @Override
                    public void onCallbackSuccess(ArrayList<Habit> habitList) {
                        repopulate(habitList);
                    }

                    @Override
                    public void onCallbackFailed() {

                    }
                });
    }

    /**
     * sets the arraylist and notifies data changed
     * @param habitList
     */
    private void repopulate (ArrayList<Habit> habitList) {
        for (Habit h : habitList) {
            if ((h.getWeekDays() != null) && (h.getWeekDays().contains(DateConverter.getCurrentWeekDay()))) {
                this.habitList.add(h);
            }
            this.habitAdapter.notifyDataSetChanged();
        }
    }
}
