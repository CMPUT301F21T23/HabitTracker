package com.example.habittracker.activities;

import androidx.appcompat.app.AppCompatActivity;

import android.content.Intent;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.Button;
import android.widget.ListView;

import com.example.habittracker.DatabaseManager;
import com.example.habittracker.Habit;
import com.example.habittracker.HabitEvent;
import com.example.habittracker.NavBarManager;
import com.example.habittracker.R;
import com.example.habittracker.activities.eventlist.EventListActivity;
import com.example.habittracker.utils.HabitEventListCallback;
import com.example.habittracker.utils.HabitListCallback;
import com.example.habittracker.utils.SharedInfo;

import java.util.ArrayList;

public class HomeActivity extends AppCompatActivity {

    private ArrayList<String> habitList = new ArrayList<String>();
    private ListView list = null;
    private ArrayAdapter<String> adapter = null;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_home);
        NavBarManager nav = new NavBarManager(this,findViewById(R.id.bottom_navigation));

        list = findViewById(R.id.today_habits_list_view);
        adapter = new ArrayAdapter<String>(this, R.layout.home_habit_list,habitList);
        list.setAdapter(adapter);
        adapter.add("Habit "+habitList.size()+" -- Progress: 0-100");

        this.list.setOnItemClickListener(new AdapterView.OnItemClickListener() {
            public void onItemClick(AdapterView<?> adapter, View v, int position, long id) {
                DatabaseManager db = DatabaseManager.get();
                db.getAllHabits(SharedInfo.getInstance().getCurrentUser().getUsername(),new HabitListCallback() {
                    @Override
                    public void onCallbackSuccess(ArrayList<Habit> habitList) {
                        //Do what you need to do with your list
                        Intent intent = new Intent(getApplicationContext(),HabitViewActivity.class);
                        Bundle bundle = new Bundle();
//                        Log.d("testing",""+habitList.get(1).getTitle());//remove once real habit is implemented
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

        Button event_list_button = findViewById(R.id.event_list_button);
        event_list_button.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Intent intent = new Intent(getApplicationContext(), EventListActivity.class);
                startActivity(intent);
            }
        });
    }
    }