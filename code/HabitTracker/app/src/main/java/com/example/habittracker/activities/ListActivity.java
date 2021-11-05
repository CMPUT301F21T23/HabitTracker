package com.example.habittracker.activities;

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
import com.example.habittracker.utils.HabitListCallback;
import com.example.habittracker.utils.SharedInfo;
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
        this.adapter = new ArrayAdapter<String>(this, R.layout.home_habit_list,habitList);
        this.list.setAdapter(adapter);

        add_button.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                new HabitInputFragment().show(getSupportFragmentManager(), "ADD MEDICINE");
            }
        });

        this.list.setOnItemClickListener(new AdapterView.OnItemClickListener() {
            public void onItemClick(AdapterView<?> adapter, View v, int position, long id) {
                DatabaseManager db = DatabaseManager.get();
                db.getAllHabits(SharedInfo.getInstance().getCurrentUser().getUsername(),new HabitListCallback() {
                    @Override
                    public void onCallbackSuccess(ArrayList<Habit> habitList) {
                        //Do what you need to do with your list
                        Intent intent = new Intent(getApplicationContext(),HabitViewActivity.class);
                        Bundle bundle = new Bundle();
//                        Log.d("testing",""+habitList.get(3).getTitle());//remove once real habit is implemented
                        Habit habit = habitList.get(0);//remove once real habit is implemented
                        bundle.putSerializable("habit", habit);
                        intent.putExtras(bundle);
                        startActivity(intent);
                    }

                    @Override
                    public void onCallbackFailed() {

                    }
                });
            }});
    }

    @Override
    public void onOkPressed() {
        adapter.add("Habit "+habitList.size()+" -- Progress: 0-100");
    }
}