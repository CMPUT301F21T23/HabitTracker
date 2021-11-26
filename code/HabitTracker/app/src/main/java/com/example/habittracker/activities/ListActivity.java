package com.example.habittracker.activities;

import androidx.appcompat.app.AppCompatActivity;

import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.view.ViewGroup;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.Button;
import android.widget.LinearLayout;
import android.widget.ListView;

import com.example.habittracker.DatabaseManager;
import com.example.habittracker.Habit;
import com.example.habittracker.HabitEvent;
import com.example.habittracker.NavBarManager;
import com.example.habittracker.R;
import com.example.habittracker.activities.fragments.HabitInputFragment;
import com.example.habittracker.activities.tracking.ProgressUpdater;
import com.example.habittracker.activities.tracking.ProgressUtil;
import com.example.habittracker.utils.CustomHabitList;

import com.example.habittracker.utils.HabitListCallback;
import com.example.habittracker.utils.SharedInfo;
import com.google.android.material.floatingactionbutton.FloatingActionButton;

import java.io.Serializable;
import java.util.ArrayList;
import java.util.HashMap;

public class ListActivity extends AppCompatActivity implements HabitInputFragment.HabitInputDialogListener {

    private ArrayList<Habit> habitList = new ArrayList<>();
    private ListView list = null;
    private CustomHabitList habitAdapter;


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
                startActivity(intent);
            }
        });

        Button reorder_button = findViewById(R.id.reorder_button);
        reorder_button.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Button self = (Button) view;
                if(self.getText().equals("REORDER")){
                    self.setText("DONE");
                    ((View) view.getParent()).findViewById(R.id.add_habit_button).setEnabled(false);
                    habitAdapter.toggleButtons(true);
                }
                else{
                    self.setText("REORDER");
                    ((View) view.getParent()).findViewById(R.id.add_habit_button).setEnabled(true);
                    habitAdapter.toggleButtons(false);
                }
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
     * Action to be triggered when the user is OK with adding a new Habit.
     */
    @Override
    public void onOkPressed(Habit habit, String prevTitle) {
        // add habit to database
        habit.addToDB();
        habitAdapter.add(habit);
    }

    /**
     * sets the arraylist and notifies data changed
     * @param habitList
     */
    private void repopulate (ArrayList<Habit> habitList) {
        int orderCount = 0;
        this.habitAdapter.clear();
        for(Habit h:habitList){
            this.habitList.add(h);
            HashMap<String,Object> doc = h.toDocument();
            doc.put("order",orderCount);
            DatabaseManager.get().updateHabitDocument(SharedInfo.getInstance().getCurrentUser().getUsername(),h.getTitle(),h.getTitle(), doc);
            orderCount++;
        }
        this.habitAdapter.notifyDataSetChanged();
    }
}
