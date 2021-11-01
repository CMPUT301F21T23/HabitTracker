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

import at.favre.lib.crypto.bcrypt.BCrypt;

public class User {

    // all these lists the user should have references to.
    // FriendsList
    // RequestList
    // HabitList
    // HabitEventsList

    private String username;
    private String password;

    /**
     * Creates a user. Constructor.
     * Add the following to your module build gradle:
     * implementation group: 'at.favre.lib', name: 'bcrypt', version:'0.9.0'
     * @param username {String} The unique id of the user. Uniqueness checking performed beforehand
     * @param password {String} The password to be associated with the user's account
     */
    public User (String username, String password) {
        // this is the value that should be saved. DO NOT SAVE THE ACTUAL USER'S PASSWORD
        String hashedPassword = BCrypt.withDefaults().hashToString(12, password.toCharArray());

        // not sure if this is even necessary.
        this.username = username;
        this.password = hashedPassword;

        // TODO: add user to database here
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
