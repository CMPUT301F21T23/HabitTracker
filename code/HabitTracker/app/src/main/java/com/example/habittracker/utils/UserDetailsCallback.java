package com.example.habittracker.utils;

import java.util.HashMap;


public interface UserDetailsCallback {
    /**
     * success callback
     * @param userDetails
     */
    void onCallbackSuccess(HashMap<String,Object> userDetails);

    /**
     * called when task is unsuccessful
     */
    void onCallbackFailed();
}
