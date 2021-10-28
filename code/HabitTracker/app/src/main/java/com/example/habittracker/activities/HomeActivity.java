package com.example.habittracker.activities;

import androidx.annotation.Nullable;
import androidx.appcompat.app.AppCompatActivity;

import android.content.Intent;
import android.graphics.Color;
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
import com.example.habittracker.utils.CustomHabitList;
import com.example.habittracker.utils.DateConverter;
import com.google.firebase.firestore.EventListener;
import com.google.firebase.firestore.FirebaseFirestoreException;
import com.google.firebase.firestore.QueryDocumentSnapshot;
import com.google.firebase.firestore.QuerySnapshot;

import java.util.ArrayList;
import java.util.Date;

public class HomeActivity extends AppCompatActivity {

    private ArrayList<Habit> habitList = new ArrayList<>();
    private ListView list = null;
    private ArrayAdapter<Habit> habitAdapter;
//    private ArrayAdapter<String> adapter = null;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_home);
        NavBarManager nav = new NavBarManager(this,findViewById(R.id.bottom_navigation));

        list = findViewById(R.id.today_habits_list_view);

        this.habitAdapter = new CustomHabitList(this, habitList);
        this.list.setAdapter(habitAdapter);

        this.list.setOnItemClickListener(new AdapterView.OnItemClickListener() {
            public void onItemClick(AdapterView<?> adapter, View v, int position, long id) {
                Intent intent = new Intent(getApplicationContext(),HabitViewActivity.class);
                Bundle bundle = new Bundle();
                bundle.putString("HabitID", list.getItemAtPosition(position).toString());
                intent.putExtras(bundle);
                startActivity(intent);
            }
        });

        Button event_list_button = findViewById(R.id.event_list_button);
        event_list_button.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Intent intent = new Intent(getApplicationContext(), EventListActivity.class);
                startActivity(intent);
            }
        });

        // TODO: Remember to change this with getuser when you are not testing anymore
        // snapshot
        DatabaseManager.get().getHabitsColRef("Pao_Dummy").addSnapshotListener(
                new EventListener<QuerySnapshot>() {
                    @Override
                    public void onEvent(@Nullable QuerySnapshot value, @Nullable FirebaseFirestoreException error) {
                        // Clear the old list
                        habitList.clear();
                        repopulate(value, error);
                        habitAdapter.notifyDataSetChanged();
                    }
                }
        );
    }

    /**
     * Repopulates the activity that lists all habits belonging to a user
     * @param value {QuerySnapshot}                 the snapshot value
     * @param error {FirebaseFirestoreException}    error, if any
     */
    private void repopulate (QuerySnapshot value, FirebaseFirestoreException error) {
        String [] attributes = {"reason", "dateStarted", "whatDays", "progress"};

        if (error == null) {
            for (QueryDocumentSnapshot doc : value) {
                ArrayList<String> weekDays = (ArrayList<String>) doc.get(attributes[2]);

                // check if the habit should be performed in today's day of the week
                if ( (weekDays != null) && (weekDays.contains(DateConverter.getCurrentWeekDay())) ) {

                    // check if the habit started today or before today.
                    ArrayList<Long> dateTest = (ArrayList<Long>) doc.get(attributes[1]);
                    Date startDate = DateConverter.arrayListToDate(dateTest);
                    Date today = new Date();

                    if (today.after(startDate)) {
                        String habitTitle = doc.getId();
                        String habitReason = (String) doc.getData().get(attributes[0]);

                        habitList.add(
                                new Habit(
                                        habitTitle,
                                        habitReason,
                                        startDate,
                                        weekDays
                                ));
                    }
                }
            }
        }
    }




}