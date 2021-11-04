package com.example.habittracker.utils;

import com.example.habittracker.Habit;
import com.example.habittracker.HabitEvent;

import java.util.ArrayList;

/**
 * This interface is used for when you want to get a habit list from database manager,
 * it allows to access the data when the task is complete
 */
public interface HabitListCallback {
    /**
     * called when task is successful
     * @param habitList
     */
    void onCallbackSuccess(ArrayList<Habit> habitList);

    /**
     * called when task is unsuccessful
     */
    void onCallbackFailed();
}