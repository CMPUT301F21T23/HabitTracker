package com.example.habittracker.utils;

import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.Calendar;
import java.util.Date;
import java.util.Locale;

public class DateConverter {

    /**
     * Returns the given date as a more parse-friendly array list.
     * @param startDate {Date} The date to convert
     * @return {year, month, day}
     */
    public static ArrayList<Integer> dateToArrayList(Date startDate) {
        Calendar cal = Calendar.getInstance();
        cal.setTime(startDate);

        int year = cal.get(Calendar.YEAR);
        int month = cal.get(Calendar.MONTH) + 1; // months start at 0; therefore, we add 1.
        int day = cal.get(Calendar.DAY_OF_MONTH);

        return (new ArrayList<Integer>(
                Arrays.asList(
                        year,
                        month,
                        day
                )));
    }

    /**
     * Converts a given arraylist to a Date
     * @param dateArrayList {ArrayList<Long>}   given in the following format: {year, month, day}
     * @return  date, the converted Date
     */
    public static Date arrayListToDate (ArrayList<Long> dateArrayList) {
        Date date = null;

        if (dateArrayList != null) {
            long year = dateArrayList.get(0);
            long month = dateArrayList.get(1);
            long day = dateArrayList.get(2);
            String dateAsString = String.format(Locale.US, "%d/%d/%d", month, day, year);

            try {
                date = new SimpleDateFormat("MM/dd/yyyy", Locale.US).parse(dateAsString);
            } catch (ParseException e) {
                e.printStackTrace();
            }
        }
        return (date);
    }

    /**
     * Gets the current day of the week in an easily comparable format
     * @return weekDay, the current day of the week
     */
    public static String getCurrentWeekDay() {
        Calendar calendar = Calendar.getInstance();
        int day = calendar.get(Calendar.DAY_OF_WEEK);
        String weekDay = "";

        switch (day) {
            case Calendar.SUNDAY:
                weekDay = "Sun";
                break;
            case Calendar.MONDAY:
                weekDay = "Mon";
                break;
            case Calendar.TUESDAY:
                weekDay = "Tue";
                break;
            case Calendar.WEDNESDAY:
                weekDay = "Wed";
                break;
            case Calendar.THURSDAY:
                weekDay = "Thu";
                break;
            case Calendar.FRIDAY:
                weekDay = "Fri";
                break;
            case Calendar.SATURDAY:
                weekDay = "Sat";
                break;
        }

        return (weekDay);
    }
}
