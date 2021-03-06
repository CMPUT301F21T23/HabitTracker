package com.example.habittracker;

import com.example.habittracker.utils.SharedInfo;

import java.io.Serializable;
import java.util.ArrayList;
import java.util.Calendar;
import java.util.Date;
import java.util.HashMap;
/**
 * The HabitEvent class is ued to as an intermediate storage between
 * HabitEvent list and firestore database
 * @author Yongquan Zhang
 */
public class HabitEvent implements Serializable {
    private String title;
    private String location;
    private String comment;
    private ArrayList<Integer> startDate;
    private String userId;
    private Date date_calendar;
    private String habitId;
    private String eventId;
    private String imageUrl;
    private HashMap<String, Object> habitEventDocument;


    /**
     * An empty constructor for Habit Events
     * userId and habitId are set to a default value because they are mandatory.
     */
    public HabitEvent () {
        // By default
        this.userId = SharedInfo.getInstance().getCurrentUser().getUsername();
        // For test
//        this.userId = "John_test_user";
        // By default
        this.habitId = "Habit 1";
        this.habitEventDocument = new HashMap<>();
    }

    /**
     * Creates a habit event.
     * A habit event is an entry that the user might create when they've done a habit as planned.
     * @param habit     {String}    The habit name associated with this habit event
     * @param comment   {String}    Optional, comment on the habit event
     * @param startDate {Date}      The date during which the habit event was "done"
     * @param location  {String}    The location where the habit was accomplished
     * @param image     {String}    Path to the image of the accomplishment.
     */
    public HabitEvent (String habit, String eventId ,String comment, ArrayList<Integer> startDate, String location, String image) {

        this.userId = SharedInfo.getInstance().getCurrentUser().getUsername();
        this.eventId = eventId;
        this.habitId = habit;
        this.comment = comment;
        this.startDate = startDate;
        this.location = location;
        this.imageUrl = image;
        this.habitEventDocument = new HashMap<>();
        this.setEventId(eventId);
        this.setHabit(habit);
        this.setComment(comment);
        this.setStartDate(startDate);
        this.setLocation(location);

        // location and image storage are still TBD, I've declared them as strings for now.
    }
    /**
     * Gets the comment on the habit event
     * @return comment
     */
    public String getComment() {
        return this.comment;
    }

    /**
     * Gets the starting date for the habit event
     * @return startDate
     */
    public ArrayList<Integer> getStartDate() {
        return this.startDate;
    }

    /**
     * Allows editing/setting of the comment field
     * @param comment {String} the new, edited  comment
     */
    public void setComment(String comment) {
        habitEventDocument.put("comment", comment);
        this.comment = comment;
    }

    /**
     * Allows editing/setting of the start date
     * @param startDate {String} the new, edited start date
     */
    public void setStartDate(ArrayList<Integer> startDate) {
        this.habitEventDocument.put("startDate", startDate);
        this.startDate = startDate;
    }
    /**
     * Gets the habit name that associated with this habit event
     * @return habitId
     */
    public String getHabit() { return this.habitId; }

    /**
     * Gets the location of the habit event
     * @return location
     */
    public String getLocation() { return this.location; }


    /**
     * Allows editing/setting of the location
     * @param location {String} the current or selected location
     */
    public void setLocation(String location) {
        this.habitEventDocument.put("location", location);
        this.location = location;
    }


    /**
     * Gets the habit id and performs an action accordingly to the desired outcome
     * @param habit     {String}    the title of the habit you want the id of
     * @param action    {int}       the type of action to be performed after finding the id
     */
    public void setHabitInDB(String habit, int action) {
        DatabaseManager.get().getAndSetHabitId(this.userId, habit, this, action);
    }

    /**
     * Allows editing/setting of the habit name
     * @param habit {String} the habit name
     */
    public void setHabit(String habit) {
        this.habitId = habit;
        this.habitEventDocument.put("Habit", habit);
    }

    /**
     * add new habit event to the database
     */
    public void updateDB() {
        DatabaseManager.get().addHabitEventDocument(this.userId, this.habitId, this.habitEventDocument);
    }
    /**
     * edit existed habit event in the database
     */
    public void editDB() {
        DatabaseManager.get().updateHabitEventDocument(this.userId, this.habitId, this.eventId, this.habitEventDocument);
    }
    /**
     * delete existed habit event in the database
     */
    public void deleteDB() {
        DatabaseManager.get().deleteHabitEventDocument(this.userId, this.habitId, this.eventId);
    }
    /**
     * Allows editing/setting of the habit event id
     * @param id {String} the habit event id (random generated by firestore)
     */
    public void setEventId(String id) {
        this.eventId = id;
    }

    /**
     * Gets the id of the habit event
     * @return eventId
     */
    public String getEventId() {
        return this.eventId;
    }

    /**
     * Allows editing/setting of the start date of the habit event (in Date format)
     * @param date {Date} the habit event start date
     */
    public void setCalendar(Date date) {
        this.date_calendar = date;
        this.habitEventDocument.put("date", date);
    }

    /**
     * Gets the the start date of the habit event (in Date format)
     * @return date_calendar
     */
    public Date getCalendar() {
        return this.date_calendar;
    }

    /**
     * Setter for imageUrl
     * @param imageUrl
     */
    public void setImageUrl(String imageUrl) {
        habitEventDocument.put("imageUrl", imageUrl);
        this.imageUrl = imageUrl;
    }

    /**
     * Getter for imageUrl
     * @return imageUrl: String
     */
    public String getImageUrl() {
        return this.imageUrl;
        //return "https://firebasestorage.googleapis.com/v0/b/habittracker-9232f.appspot.com/o/uploads%2F1636920826943.png?alt=media&token=9046a941-44df-437d-895d-73ffef423ef7";
    }
}
