package com.example.habittracker.utils;

public interface UserExistsCallback {
    /**
     * success calback when user exists
     * @param username
     */
    void onCallbackSuccess(String username);

    /**
     * called when task is unsuccessful
     */
    void onCallbackFailed();
}
