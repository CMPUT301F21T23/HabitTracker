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

public class ProgressUpdater {
    private Habit habit;
    private int order;

    public ProgressUpdater(Habit habit,int order){
        this.habit = habit;
        this.order = order;
    }

    public void update(){
        DatabaseManager.get().getAllHabitEvents(SharedInfo.getInstance().getCurrentUser().getUsername(),habit.getTitle(),new HabitEventListCallback() {
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
                doc.put("order",order);
                DatabaseManager.get().updateHabitDocument(SharedInfo.getInstance().getCurrentUser().getUsername(),habit.getTitle(),habit.getTitle(),doc);
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
