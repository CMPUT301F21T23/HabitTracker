package com.example.habittracker;

import androidx.appcompat.app.AppCompatActivity;

import android.content.Intent;
import android.os.Bundle;

import com.example.habittracker.activities.HomeActivity;

public class HabitActivity extends AppCompatActivity {

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_habit);
        NavBarManager nav = new NavBarManager(this,findViewById(R.id.bottom_navigation));
        Intent intent = new Intent(this, HomeActivity.class);
        startActivity(intent);
    }
}