package com.example.habittracker.utils;

import com.example.habittracker.Habit;

    /**
     * HabitDeleteCallback interface is used to pass a successful deletion of a Habit from the database.
    */
    public interface HabitDeleteCallback {
    /**
     * Defines what to do upon callback success.
     */
    void onCallbackSuccess();

    /**
     * Defines what to do upon callback failure.
     * @param reason    {@code String} Reason for the failure
     */
    void onCallbackFailure(String reason);

}
