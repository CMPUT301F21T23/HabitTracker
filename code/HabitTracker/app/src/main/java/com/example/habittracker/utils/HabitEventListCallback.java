package com.example.habittracker.utils;

import com.example.habittracker.HabitEvent;

import java.util.ArrayList;

public interface HabitEventListCallback {
    void onCallbackSuccess(ArrayList<HabitEvent> eventArrayList);
    void onCallbackFailed();
}
