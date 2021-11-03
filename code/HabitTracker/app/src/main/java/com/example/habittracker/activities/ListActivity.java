package com.example.habittracker.activities;

import androidx.annotation.Nullable;
import androidx.appcompat.app.AppCompatActivity;

import android.content.Intent;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.ListView;

import com.example.habittracker.DatabaseManager;
import com.example.habittracker.Habit;
import com.example.habittracker.NavBarManager;
import com.example.habittracker.R;
import com.example.habittracker.activities.fragments.HabitInputFragment;
import com.example.habittracker.utils.CustomHabitList;
import com.example.habittracker.utils.DateConverter;
import com.google.android.material.floatingactionbutton.FloatingActionButton;
import com.google.firebase.firestore.EventListener;
import com.google.firebase.firestore.FirebaseFirestoreException;
import com.google.firebase.firestore.QueryDocumentSnapshot;
import com.google.firebase.firestore.QuerySnapshot;

import java.io.Serializable;
import java.util.ArrayList;
import java.util.Date;

public class ListActivity extends AppCompatActivity implements HabitInputFragment.HabitInputDialogListener {

    private ArrayList<Habit> habitList = new ArrayList<>();
    private ListView list = null;
    private ArrayAdapter<Habit> habitAdapter;

    // constant
    static final String EXTRA_HABIT = "habit";

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_list);

        NavBarManager nav = new NavBarManager(this,findViewById(R.id.bottom_navigation));
        list = findViewById(R.id.total_habit_list);
        FloatingActionButton add_button = findViewById(R.id.add_habit_button);

        this.habitAdapter = new CustomHabitList(this, habitList);
        this.list.setAdapter(habitAdapter);

        add_button.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                new HabitInputFragment().show(getSupportFragmentManager(), "ADD MEDICINE");
            }
        });

        this.list.setOnItemClickListener(new AdapterView.OnItemClickListener() {
            public void onItemClick(AdapterView<?> adapter, View v, int position, long id) {
                Intent intent = new Intent(getApplicationContext(),HabitViewActivity.class);
                intent.putExtra(EXTRA_HABIT, (Serializable) list.getItemAtPosition(position));

//                Bundle bundle = new Bundle();
//                bundle.putString("HabitID", list.getItemAtPosition(position).toString());
//                intent.putExtras(bundle);
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
     * Action to be triggered when the user is OK with adding a new Habit.
     */
    @Override
    public void onOkPressed(Habit habit) {
        // add habit to database
        habit.addToDB();
        habitAdapter.add(habit);
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
                String habitTitle = doc.getId();

                String habitReason = (String) doc.getData().get(attributes[0]);

                ArrayList<Long> dateTest = (ArrayList<Long>) doc.get(attributes[1]);
                Date startDate = DateConverter.arrayListToDate(dateTest);

                ArrayList<String> weekDays = (ArrayList<String>) doc.get(attributes[2]);

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