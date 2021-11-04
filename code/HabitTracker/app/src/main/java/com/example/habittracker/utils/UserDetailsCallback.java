package com.example.habittracker.utils;

import com.example.habittracker.User;


public interface UserDetailsCallback {
    /**
     * called when task is successful
     * @param user {User}
     */
    void onCallbackSuccess(User user);

    /**
     * called when task is unsuccessful
     */
    void onCallbackFailed();
}
