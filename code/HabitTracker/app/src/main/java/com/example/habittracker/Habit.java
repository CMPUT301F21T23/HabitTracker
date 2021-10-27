package com.example.habittracker;

import com.example.habittracker.utils.DateConverter;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.Date;
import java.util.HashMap;
import java.util.List;

public class Habit {
    private String title;
    private String reason;
    private Date startDate;
    private int progress; // provisional until we determine how to implement progress
    private String [] weekDays;

    /**
     * An empty constructor for Habit
     */
    public Habit () {

    }
    /**
     * Creates a habit belonging to a user.
     * @param title     {string}    the title of the habit (ie. Sleep Early)
     * @param reason    {String}    a description of the habit
     * @param startDate {Date}      the date in which the habit was started
     * @param weekDays  {String[]}  the days of the week during which the habit should be practiced
     */
    public Habit (String title, String reason, Date startDate, String [] weekDays) {
        this.title = title;
        this.reason = reason;
        this.startDate = startDate;
        this.progress = 0;

        this.weekDays = weekDays.clone();
    }

    /**
     * Gets the title for the habit
     * @return title
     */
    public String getTitle() {
        return title;
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
     * Changes the set of days of the habit
     * @param weekDays {String[]} the new set of days
     */
    public void setWeekDays(String[] weekDays) {
        this.weekDays = weekDays;
    }

    /**
     * Add habit to database.
     */
    public void addToDB() {
        HashMap <String, Object> doc = this.toDocument();
        DatabaseManager.get().addHabitDocument("Pao_Dummy", title, doc);
        //TODO: Remember to get user when you are done testing
    }

    /**
     * Converts the habit to a document that can directly interact with the database
     * @return habitMap, the document as a hashmap.
     */
    private HashMap <String, Object> toDocument () {
        HashMap<String,Object> habitMap = new HashMap<>();

        // make necessary conversion to convert to document
        ArrayList<Integer> dateArrayList = DateConverter.dateToArrayList(startDate);
        List<String> whatDays =  Arrays.asList(weekDays);

        // the attribute names as specified in the schema and the values that correspond
        String [] attributes = {"reason", "dateStarted", "whatDays", "progress"};
        Object [] values = { reason, dateArrayList, whatDays, progress};

        // populate the hash map
        for (int i = 0; i < attributes.length; i++) {
            habitMap.put(attributes[i], values[i]);
        }
        // done populating
        return (habitMap);
    }
}
