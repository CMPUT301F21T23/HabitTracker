package com.example.habittracker.utils;

import com.example.habittracker.Habit;
import com.example.habittracker.HabitEvent;

import java.util.ArrayList;

public interface HabitListCallback {
    void onCallbackSuccess(ArrayList<Habit> habitList);
    void onCallbackFailed();
}
