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
    private Integer progress;
    private ArrayList<String> weekDays;
    private User user;

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
    public Habit (String titlePermanent, String reason, Date startDate, ArrayList<String> weekDays, int progress, boolean isPublic, User owner) {

        this.title = titlePermanent;
        this.reason = reason;
        this.startDate = startDate;
        this.progress = progress;
        this.isPublic = isPublic;
        this.user = owner;
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
     * Returns sharing status
     * @return isPublic
     */
    public boolean isPublic() {
        return isPublic;
    }

    /**
     * Sets sharing status
     * @param isPublic {boolean}   true if public, false otherwise
     */
    public void setShareStatus(boolean isPublic) {
        this.isPublic = isPublic;
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
     * Changes the progress of the habit
     * @param progress {int}
     */
    public void setProgress(int progress) { this.progress = progress; }

    /**
     * Changes the startDate of the habit
     * @param startDate {Date} the date to change
     */
    public void setStartDate(Date startDate) {
        this.startDate = startDate;
    }

    /**
     * Gets the overall progress for a Habit.
     * @return          {@code int} Overall progress
     */
    public Integer getProgress() {
        return progress;
    }

    /**
     * Sets the overall progress for a Habit.
     * @param progress   {@code int} Overall progress
     */
    public void setProgress(Integer progress) {
        this.progress = progress;
    }

    /**
     * Gets the User that owns this Habit.
     * @return      {@code User} User object
     */
    public User getUser() {
        return user;
    }

    /**
     * Sets the User that owns this Habit.
     * @param user  {@code User} User object
     */
    public void setUser(User user) {
        this.user = user;
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
        String [] attributes = {"title", "reason", "dateStarted", "whatDays", "progress","order", "isPublic"};
        Object [] values = {title, reason, dateArrayList, weekDays, progress, 0, isPublic};

        // populate the hash map
        for (int i = 0; i < attributes.length; i++) {
            habitMap.put(attributes[i], values[i]);
        }
        // done populating
        return (habitMap);
    }
}