package com.example.habittracker;

import java.util.Date;

public class Habit {
    private String title;
    private String reason;
    private Date startDate;
    private String [] weekDays;

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
}
