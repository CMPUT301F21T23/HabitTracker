package com.example.habittracker.activities;

import androidx.appcompat.app.AppCompatActivity;

import android.os.Bundle;
import android.view.View;

import com.example.habittracker.NavBarManager;
import com.example.habittracker.R;
import com.example.habittracker.activities.fragments.HabitInputFragment;
import com.example.habittracker.activities.fragments.SharingInputFragment;
import com.google.android.material.floatingactionbutton.FloatingActionButton;

public class SharingActivity extends AppCompatActivity implements SharingInputFragment.SharingInputDialogListener {

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_sharing);
        NavBarManager nav = new NavBarManager(this,findViewById(R.id.bottom_navigation));
        FloatingActionButton add_button = findViewById(R.id.add_person_button);
        add_button.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                new SharingInputFragment().show(getSupportFragmentManager(), "ADD MEDICINE");
            }
        });
    }

    @Override
    public void onOkPressed() {

    }
}