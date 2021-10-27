package com.example.habittracker.utils;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.Calendar;
import java.util.Date;

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

    
}
