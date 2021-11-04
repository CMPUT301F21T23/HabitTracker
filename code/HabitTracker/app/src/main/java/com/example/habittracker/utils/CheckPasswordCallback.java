package com.example.habittracker.utils;

public interface CheckPasswordCallback {
    /**
     * success callback
     * @param username
     * @param hashedPassword
     */
    void onCallbackSuccess(String username,String hashedPassword);

    /**
     * called when task is unsuccessful
     */
    void onCallbackFailed();
}
