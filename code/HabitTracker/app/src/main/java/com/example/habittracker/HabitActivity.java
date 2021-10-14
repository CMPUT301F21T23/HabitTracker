package com.example.habittracker;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;
import androidx.fragment.app.Fragment;
import androidx.fragment.app.FragmentManager;

import android.content.Context;
import android.os.Bundle;
import android.view.MenuItem;

import com.example.habittracker.fragments.HomeFragment;
import com.example.habittracker.fragments.ListFragment;
import com.google.android.material.bottomnavigation.BottomNavigationView;
import com.google.android.material.navigation.NavigationBarView;

import java.util.List;
import java.util.ResourceBundle;

public class HabitActivity extends AppCompatActivity {

    private BottomNavigationView bottomNavigationView;
    private HomeFragment home = new HomeFragment();
    private ListFragment list = new ListFragment();

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_habit);

        bottomNavigationView = findViewById(R.id.bottom_navigation);

        bottomNavigationView.setOnItemSelectedListener(bottomNavMethod);
        getSupportFragmentManager().beginTransaction().replace(R.id.fragment,home).commit();



    }

    private NavigationBarView.OnItemSelectedListener bottomNavMethod = new NavigationBarView.OnItemSelectedListener(){
        @Override
        public boolean onNavigationItemSelected(@NonNull MenuItem item) {
            Fragment fragment = null;
            switch(item.getItemId()){
                case R.id.home:
                    fragment = home;
                    break;
                case R.id.list:
                    fragment = list;
                    break;
            }
            if(fragment != null){
                getSupportFragmentManager().beginTransaction().replace(R.id.fragment,fragment).commit();
            }
            return false;
        }
    };
}