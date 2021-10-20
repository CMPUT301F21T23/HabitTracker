package com.example.habittracker.activities;

import androidx.appcompat.app.AppCompatActivity;

import android.os.Bundle;
import android.view.View;
import android.widget.ArrayAdapter;
import android.widget.ListView;

import com.example.habittracker.NavBarManager;
import com.example.habittracker.R;
import com.google.android.material.floatingactionbutton.FloatingActionButton;

import java.util.ArrayList;

public class ListActivity extends AppCompatActivity implements HabitInputFragment.HabitInputDialogListener {

    private ArrayList<String> habitList = new ArrayList<String>();
    private ListView list = null;
    private ArrayAdapter<String> adapter = null;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_list);

        NavBarManager nav = new NavBarManager(this,findViewById(R.id.bottom_navigation));
        list = findViewById(R.id.total_habit_list);
        FloatingActionButton add_button = findViewById(R.id.add_habit_button);
        adapter = new ArrayAdapter<String>(this, R.layout.home_habit_list,habitList);
        list.setAdapter(adapter);

        add_button.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                new HabitInputFragment().show(getSupportFragmentManager(), "ADD MEDICINE");
            }
        });
    }

    @Override
    public void onOkPressed() {
        adapter.add("Habit "+habitList.size()+" -- Progress: 0-100");
    }
}