package com.example.habittracker;

import java.io.Serializable;
import java.util.Calendar;
import java.util.Date;
import java.util.HashMap;

public class HabitEvent implements Serializable {
    private String habit;
    private String title;
    private String location;
    private String comment;
    private String startDate;
    private String userId;
    private Date date_calendar;
    private String habitId;
    private String eventId;
    private HashMap<String, Object> habitEventDocument;


    /**
     * An empty constructor for Habit Events
     */
    public HabitEvent () {
//        this.habit = habit;
        this.userId = "user1";
        this.habitId = "Habit 1";
//        this.comment = comment;
//        this.startDate = startDate;
        habitEventDocument = new HashMap<>();
    }
    
    /**
     * Creates a habit event.
     * A habit event is an entry that the user might create when they've done a habit as planned.
     * @param habit     {Habit}     The habit associated with this habit event
     * @param comment   {String}    Optional, comment on the habit event
     * @param startDate {Date}      The date during which the habit event was "done"
     * @param location  {String}    The location where the habit was accomplished
     * @param image     {String}    Path to the image of the accomplishment.
     */
    public HabitEvent (String habit, String eventId ,String comment, String startDate, String location, String image) {

        this.userId = "user1";
        this.eventId = eventId;
        this.habitId = habit;
        this.comment = comment;
        this.startDate = startDate;
        habitEventDocument = new HashMap<>();

        // location and image storage are still TBD, I've declared them as strings for now.
    }
    /**
     * Gets the comment on the habit event
     * @return comment
     */
    public String getComment() {
        return comment;
    }
    /**
     * Gets the starting date for the habit event
     * @return startDate
     */
    public String getStartDate() {
        return startDate;
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
     * @param startDate {Date} the new, edited start date
     */
    public void setStartDate(String startDate) {
        this.habitEventDocument.put("startDate", startDate);
        this.startDate = startDate;
    }

    public String getTitle() { return this.title;
    }

    public String getHabit() { return this.habitId;
    }

    public String getLocation() { return this.location;
    }

    public void setTitle(String title) { this.title = title;
    }

    public void setLocation(String location) {
        this.habitEventDocument.put("location", location);
        this.location = location;
    }
    // This is not correct!!!!!!!
    public void setHabit(String habit) {
        this.habitEventDocument.put("Habit", habit);
        this.habitId = habit;
    }

    public void updateDB() {
        DatabaseManager.get().addHabitEventDocument(this.userId, this.habitId, this.habitEventDocument);
    }

    public void editDB() {
        DatabaseManager.get().updateHabitEventDocument(this.userId, this.habitId, this.eventId, this.habitEventDocument);
    }

    public void deleteDB() {
        DatabaseManager.get().deleteHabitEventDocument(this.userId, this.habitId, this.eventId);
    }
    public void setEventId(String id) {
        this.eventId = id;
    }
    public String getEventId() {
        return this.eventId;
    }
    public void setCalendar(Date date) {
        this.date_calendar = date;
        this.habitEventDocument.put("date", date);
    }

    public Date getCalendar() {
        return this.date_calendar;
    }
}
