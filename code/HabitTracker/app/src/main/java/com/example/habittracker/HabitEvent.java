package com.example.habittracker;

import java.util.Date;

public class HabitEvent {
    private Habit habit;

    private String comment;
    private Date startDate;

    /**
     * Creates a habit event.
     * A habit event is an entry that the user might create when they've done a habit as planned.
     * @param habit     {Habit}     The habit associated with this habit event
     * @param comment   {String}    Optional, comment on the habit event
     * @param startDate {Date}      The date during which the habit event was "done"
     * @param location  {String}    The location where the habit was accomplished
     * @param image     {String}    Path to the image of the accomplishment.
     */
    public HabitEvent (Habit habit, String comment, Date startDate, String location, String image) {
        this.habit = habit;
        this.comment = comment;
        this.startDate = startDate;

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
    public Date getStartDate() {
        return startDate;
    }
}
