package com.example.habittracker.utils;

import android.os.Build;

import androidx.annotation.RequiresApi;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.Calendar;
import java.util.Collection;
import java.util.Date;
import java.util.stream.Collectors;

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

    @RequiresApi(api = Build.VERSION_CODES.N)
    public static String arrayListToString (ArrayList<Integer> date_list) {
        String date = date_list.stream().map(String::valueOf)
                .collect(Collectors.joining("-"));
        return date;
    }

    @RequiresApi(api = Build.VERSION_CODES.N)
    public static ArrayList<Integer> stringToArraylist (String date_string) {
        String [] temp = date_string.split("-");
        ArrayList<Integer> date = new ArrayList<>();
        for (int i = 0; i<temp.length;i++) {
            date.add(Integer.parseInt(temp[i]));
        }
        return date;
    }
}