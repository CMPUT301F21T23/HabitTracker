package com.example.habittracker.utils;

/**
 * StringCallback interface is used when only messages are required through the callback functions.
 */
public interface StringCallback {

    /**
     * Defines what to do upon callback success.
     * @param msg       {@code String} Success message
     */
    void onCallbackSuccess(String msg);

    /**
     * Defines what to do upon callback failure.
     * @param reason    {@code String} Reason for the failure
     */
    void onCallbackFailure(String reason);
}
