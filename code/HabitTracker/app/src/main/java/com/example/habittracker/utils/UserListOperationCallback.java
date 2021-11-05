package com.example.habittracker.utils;

public interface UserListOperationCallback {
    /**
     * Defines what to do upon callback success.
     * @param userid        {@code String} User ID of the requester
     */
    void onCallbackSuccess(String userid);

    /**
     * Defines what to do upon callback failure.
     * @param reason        {@code String} Reason of the failure
     */
    void onCallbackFailure(String reason);
}
