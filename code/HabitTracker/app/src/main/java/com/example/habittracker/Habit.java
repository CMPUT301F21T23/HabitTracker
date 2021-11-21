package com.example.habittracker;


import android.util.Log;

import com.example.habittracker.utils.DateConverter;
import com.example.habittracker.utils.SharedInfo;

import java.io.Serializable;
import java.util.ArrayList;

import java.util.Date;
import java.util.HashMap;

/**
 * This class represents a habit object,
 * it is a data class holding information but also performs some operations on the habit
 */
public class Habit implements Serializable {
    private String title;
    private String reason;
    private Date startDate;
    private User owner;
    private int progress; // provisional until we determine how to implement progress
    private ArrayList<String> weekDays;

    private boolean isPublic = false;

    /**
     * An empty constructor for Habit
     */
    public Habit () {

    }
    /**
     * Creates a habit belonging to a user.
     * @param titlePermanent    {string}    the title of the habit (ie. Sleep Early)
     * @param reason            {String}    a description of the habit
     * @param startDate         {Date}      the date in which the habit was started
     * @param weekDays          {ArrayList<String>}  the days of the week during which the habit should be practiced
     */
    public Habit (String titlePermanent, String reason, Date startDate, ArrayList<String> weekDays, boolean isPublic, User owner) {
        this.title = titlePermanent;
        this.reason = reason;
        this.startDate = startDate;
        this.progress = 0;
        this.isPublic = isPublic;
        this.owner = SharedInfo.getInstance().getCurrentUser();

        this.weekDays = weekDays;
    }

    /**
     * Gets the title for the habit
     * @return title
     */
    public String getTitle() {
        return title;
    }

    /**
     * Returns the owner user
     * @return title
     */
    public User getUser() {
        return (owner);
    }

    /**
     * Sets the owner to user
     * @param user {User}   the user to set this to.
     */
    public void setUser(User user) {
        owner = user;
    }

    /**
     * Gets the description for the habit
     * @return reason
     */
    public String getReason() {
        return reason;
    }
    /**
     * Gets the starting date for the habit
     * @return startDate
     */
    public Date getStartDate() {
        return startDate;
    }

    /**
     * Gets the week days where the habit is meant to be practiced
     * @return weekDays
     */
    public ArrayList<String> getWeekDays() {
        return (weekDays);
    }

    /**
     * Changes the title of the habit
     * @param title {String} the title to change
     */
    public void setTitle(String title) {
        this.title = title;
    }
    /**
     * Changes the reason of the habit
     * @param reason {String} the reason to change
     */
    public void setReason(String reason) {
        this.reason = reason;
    }

    /**
     * Changes the startDate of the habit
     * @param startDate {Date} the date to change
     */
    public void setStartDate(Date startDate) {
        this.startDate = startDate;
    }

    /**
     * Add habit to database.
     */
    public void addToDB() {
        HashMap <String, Object> doc = this.toDocument();
        DatabaseManager
                .get()
                .addHabitDocument(SharedInfo.getInstance().getCurrentUser().getUsername(), doc);
    }

    /**
     * Converts the habit to a document that can directly interact with the database
     * @return habitMap, the document as a hashmap.
     */
    public HashMap <String, Object> toDocument () {
        HashMap<String,Object> habitMap = new HashMap<>();

        // make necessary conversion to convert to document
        ArrayList<Integer> dateArrayList = DateConverter.dateToArrayList(startDate);

        // TODO: the list of attributes for habit should be somewhere commonly accessible.

        // the attribute names as specified in the schema and the values that correspond
        String [] attributes = {"title", "reason", "dateStarted", "whatDays", "progress"};
        Object [] values = {title, reason, dateArrayList, weekDays, progress};

        // populate the hash map
        for (int i = 0; i < attributes.length; i++) {
            habitMap.put(attributes[i], values[i]);
        }
        // done populating
        return (habitMap);
    }
}
