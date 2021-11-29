package com.example.habittracker.activities.tracking;

import android.util.Log;

import com.example.habittracker.DatabaseManager;
import com.example.habittracker.Habit;
import com.example.habittracker.HabitEvent;
import com.example.habittracker.utils.HabitEventListCallback;
import com.example.habittracker.utils.SharedInfo;

import java.util.ArrayList;
import java.util.Calendar;
import java.util.HashMap;

/**
 * Progress Updater - Updates the progress using the database manager class
 */
public class ProgressUpdater {
    private Habit habit;

    /**
     * public constructor for the class
     * @param habit
     */
    public ProgressUpdater(Habit habit){
        this.habit = habit;
    }

    /**
     * runs the updating of the habit using the current username
     */
    public void update(){
        DatabaseManager.get().getAllHabitEvents(habit.getUser().getUsername(),habit.getTitle(),new HabitEventListCallback() {
            /**
             * Called when success to get habit events
             * @param eventList {ArrayList<HabitEvent>}
             */
            @Override
            public void onCallbackSuccess(ArrayList<HabitEvent> eventList) {
                //Do what you need to do with your list
                HashMap<String,Integer> hash = ProgressUtil.getOverallProgress(habit,eventList, 1, 100);
                HashMap<String,Object> doc = habit.toDocument();
                doc.put("progress",hash.get("overall"));
                DatabaseManager.get().updateHabitDocument(habit.getUser().getUsername(),habit.getTitle(),habit.getTitle(),doc);
            }

            /**
             * called when failed to get habit events
             */
            @Override
            public void onCallbackFailed() {
                Log.d("Error","Failed to get habit events");
            }
        });
    }
}
