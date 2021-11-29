package com.example.habittracker.utils;

import com.example.habittracker.Habit;

/**
 * HabitCallback interface is used to pass a successful retrieval of a Habit from the database.
 */
public interface HabitCallback {

    /**
     * Defines what to do upon callback success.
     * @param habit     {@code Habit} Habit object
     */
    void onCallbackSuccess(Habit habit);

    /**
     * Defines what to do upon callback failure.
     * @param reason    {@code String} Reason for the failure
     */
    void onCallbackFailure(String reason);
}
