package com.example.habittracker.activities.profile;

import androidx.appcompat.app.AppCompatActivity;

import android.os.Bundle;

import com.example.habittracker.NavBarManager;
import com.example.habittracker.R;

public class ProfileLoginActivity extends AppCompatActivity {

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_profile_login);
        NavBarManager nav = new NavBarManager(this,findViewById(R.id.bottom_navigation));
    }
}