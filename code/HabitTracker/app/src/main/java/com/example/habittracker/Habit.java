package com.example.habittracker;


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
    private String titleDisplay;
    private String reason;
    private Date startDate;
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
     * @param titleDisplay      {string}    the title of the habit (ie. Sleep Early)
     * @param titlePermanent    {string}    the title of the habit (ie. Sleep Early)
     * @param reason            {String}    a description of the habit
     * @param startDate         {Date}      the date in which the habit was started
     * @param weekDays          {ArrayList<String>}  the days of the week during which the habit should be practiced
     */
    public Habit (String titlePermanent, String titleDisplay, String reason, Date startDate,int progress, ArrayList<String> weekDays) {
        this.title = titlePermanent;
        this.titleDisplay = titleDisplay;
        this.reason = reason;
        this.startDate = startDate;
        this.progress = progress;
        this.isPublic = isPublic;

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
     * Gets the displayable title for the habit
     * @return title
     */
    public String getTitleDisplay() {
        return(titleDisplay);
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
     * Gets the progress of a habit
     * @return progress
     */
    public int getProgress() { return progress; }

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
     * Add habit to database.
     */
    public void addToDB() {
        HashMap <String, Object> doc = this.toDocument();
        DatabaseManager
                .get()
                .addHabitDocument(SharedInfo.getInstance().getCurrentUser().getUsername(), title, doc);
        //TODO: Remember to get user when you are done testing
    }

    /**
     * Converts the habit to a document that can directly interact with the database
     * @return habitMap, the document as a hashmap.
     */
    public HashMap <String, Object> toDocument () {
        HashMap<String,Object> habitMap = new HashMap<>();

        // make necessary conversion to convert to document
        ArrayList<Integer> dateArrayList = DateConverter.dateToArrayList(startDate);

        // TODO: have to add isPublic attribute to the schema. Check with Zarif for naming
        // TODO: the list of attributes for habit should be somewhere commonly accessible.
        //  prolly database manager. check w Zarif.

        // the attribute names as specified in the schema and the values that correspond
        String [] attributes = {"reason", "dateStarted", "whatDays", "progress", "order", "display"};
        Object [] values = { reason, dateArrayList, weekDays, progress, 0, titleDisplay};

        // populate the hash map
        for (int i = 0; i < attributes.length; i++) {
            habitMap.put(attributes[i], values[i]);
        }
        // done populating
        return (habitMap);
    }
}
