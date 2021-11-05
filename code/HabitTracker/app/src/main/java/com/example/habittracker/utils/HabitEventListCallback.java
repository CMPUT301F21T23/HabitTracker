package com.example.habittracker.utils;

import com.example.habittracker.HabitEvent;

import java.util.ArrayList;

/**
 * This interface is used for when you want to get a habit event list from database manager,
 *  * it allows to access the data when the task is complete
 */
public interface HabitEventListCallback {
    /**
     * Called when task is successful at retrieving data
     * @param eventArrayList
     */
    void onCallbackSuccess(ArrayList<HabitEvent> eventArrayList);

    /**
     * called when task is unsuccessful at retrieving data
     */
    void onCallbackFailed();
}
