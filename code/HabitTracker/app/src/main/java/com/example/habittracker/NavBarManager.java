package com.example.habittracker;

import android.content.Intent;
import android.view.MenuItem;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;

import com.example.habittracker.activities.profile.ProfileActivity;
import com.example.habittracker.activities.HomeActivity;
import com.example.habittracker.activities.ListActivity;
import com.google.android.material.bottomnavigation.BottomNavigationView;
import com.google.android.material.navigation.NavigationBarView;


public class NavBarManager{

    private AppCompatActivity context;
    private BottomNavigationView bottomNavigationView;

    /**
     * Constructor for the class
     * @param context
     * @param bottomNavigationView
     */
    public NavBarManager(AppCompatActivity context,BottomNavigationView bottomNavigationView) {
        this.context = context;
        this.bottomNavigationView = bottomNavigationView;
        this.bottomNavigationView.setOnItemSelectedListener(bottomNavMethod);
    }

    /**
     * Override of the nav bar on click event to display appropriate fragment.
     */
    private NavigationBarView.OnItemSelectedListener bottomNavMethod = new NavigationBarView.OnItemSelectedListener(){
        @Override
        public boolean onNavigationItemSelected(@NonNull MenuItem item) {
            AppCompatActivity activity = null;
            switch(item.getItemId()){
                //IDs of the nav buttons
                case R.id.home:
                    activity = new HomeActivity();
                    break;
                case R.id.list:
                    activity = new ListActivity();
                    break;
                case R.id.sharing:
                    break;
                case R.id.profile:
                    activity = new ProfileActivity();
                    break;
            }
            if(activity != null){
                startNewActivity(activity);
            }
            return false;
        }
    };

    /**
     * Starts a new activity.
     * @param activity
     * Activity to be started.
     */
    private void startNewActivity(AppCompatActivity activity){
        Intent intent = new Intent(context, activity.getClass());
        context.startActivity(intent);
    }

}
