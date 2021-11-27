package com.example.habittracker;


import java.io.Serializable;

public class User implements Serializable {


    private String username;

    /**
     * Creates a user. Constructor.
     * @param username {String} The unique id of the user

     */
    public User (String username) {
        // not sure if this is even necessary.
        this.username = username;
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

    public void clearUsername() {
        this.username = "";
    }
}
