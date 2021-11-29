package com.example.habittracker.utils;
import java.text.ParseException;
import java.text.SimpleDateFormat;
import android.os.Build;
import android.util.Log;

import androidx.annotation.RequiresApi;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.Calendar;
import java.util.Collection;
import java.util.Date;
import java.util.Locale;
import java.util.stream.Collectors;

/**
 * This class is responsible for several date-related utilities. It allows for conversion between
 * different date formats (used on convenience)
 */
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

     /**
     * converts date array list to string
     * @param date_list
     * @return
     */
    @RequiresApi(api = Build.VERSION_CODES.N)
    public static String arrayListToString (ArrayList<Integer> date_list) {

        String date = "";
        for (int i = 0;i<date_list.size();i++) {
            if(i<date_list.size()-1) {
                date = date + date_list.get(i) + "-";
            }
            else {
                date = date + date_list.get(i);
            }
        }
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

    /**
     * converts arraylist to Date object
     * @param array
     * @return
     */
    @RequiresApi(api = Build.VERSION_CODES.N)
    public static Date intArrayListToDate(ArrayList<Integer> array){ //

        Calendar cal = Calendar.getInstance();
        Object[] objArray = array.toArray();
        Integer year = Long.valueOf(objArray[0].toString()).intValue();
        Integer month = Long.valueOf(objArray[1].toString()).intValue();
        Integer day = Long.valueOf(objArray[2].toString()).intValue();
        cal.set( year,month-1,day);
        return cal.getTime();
    }
}
