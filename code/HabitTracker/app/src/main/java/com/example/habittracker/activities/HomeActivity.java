package com.example.habittracker.activities;

import androidx.appcompat.app.AppCompatActivity;

import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.Button;
import android.widget.ListView;

import com.example.habittracker.NavBarManager;
import com.example.habittracker.R;
import com.example.habittracker.activities.eventlist.EventListActivity;

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
                Intent intent = new Intent(getApplicationContext(),HabitViewActivity.class);
//                Bundle bundle = new Bundle();
//                bundle.putSerializable("habit",(Habit) list.getItemAtPosition(position));
//                intent.putExtras(bundle);
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
    }




}