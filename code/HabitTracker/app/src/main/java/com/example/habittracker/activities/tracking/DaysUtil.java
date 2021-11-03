package com.example.habittracker.activities.tracking;


import com.example.habittracker.Habit;

import java.util.ArrayList;

public class DaysUtil {
    static public ArrayList<Integer> getHabitDays(Habit habit){
        String[] days = habit.getWeekDays();
        ArrayList<Integer> dayInt = new ArrayList<>();
        for(int i = 0 ; i < days.length; i++ ){
            switch(days[i]){
                case "Mon":
                    dayInt.add(1);
                    break;
                case "Tues":
                    dayInt.add(2);
                    break;
                case "Wed":
                    dayInt.add(3);
                    break;
                case "Thu":
                    dayInt.add(4);
                    break;
                case "Fri":
                    dayInt.add(5);
                    break;
                case "Sat":
                    dayInt.add(6);
                    break;
                case "Sun":
                    dayInt.add(7);
                    break;
            }
        }
        return dayInt;
    }
}
