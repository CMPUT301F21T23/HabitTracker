package com.example.habittracker.utils;

import java.util.ArrayList;

public interface SharingListCallback {
    /**
     * Defines what to do upon callback success.
     * @param dataList      {@code ArrayList<String> dataList}
     */
    void onCallbackSuccess(ArrayList<String> dataList);

    /**
     * Defines what to do upon callback failure.
     * @param reason        {@code String} Reason of the failure
     */
    void onCallbackFailure(String reason);
}
