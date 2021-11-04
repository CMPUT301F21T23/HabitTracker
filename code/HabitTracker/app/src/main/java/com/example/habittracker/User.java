package com.example.habittracker;

import android.util.Log;

import androidx.annotation.NonNull;

import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.Task;
import com.google.android.gms.tasks.Tasks;
import com.google.firebase.firestore.DocumentReference;
import com.google.firebase.firestore.DocumentSnapshot;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.Map;

public class User {

    private String username;
    private String hashedPassword;

    /**
     * Creates a user. Constructor.
     * @param username {String} The unique id of the user
     * @param hashedPassword {String} The hashed password to be associated with the user's account
     */
    public User (String username, String hashedPassword) {
        // not sure if this is even necessary.
        this.username = username;
        this.hashedPassword = hashedPassword;
    }

    /**
     * Gets the username
     * @return      {@code String} the username
     */
    public String getUsername() {
        return username;
    }

    /**
     * Sets the username
     * @param username      {@code String} new username
     */
    public void setUsername(String username) {
        this.username = username;
    }

}