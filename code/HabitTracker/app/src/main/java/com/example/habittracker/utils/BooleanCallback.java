package com.example.habittracker.utils;

public interface BooleanCallback {
    /**
     * called when task is successful
     * @param flag {boolean}
     */
    void onCallbackSuccess(boolean flag);

    /**
     * called when task is unsuccessful
     */
    void onCallbackFailed();
}
