package com.example.habittracker.activities.tracking;

import android.util.Log;

import com.example.habittracker.Habit;
import com.example.habittracker.HabitEvent;
import com.example.habittracker.utils.DateConverter;

import java.util.ArrayList;
import java.util.Calendar;
import java.util.Date;
import java.util.HashMap;

/**
 * This class is to calculate scores for a habit progress
 */
public class ProgressUtil {

    /**
     * returns hashmap of all scores and stats of the habit progress
     * @param habit
     * @param habitEvents
     * @param idealPerDay
     * @param penaltyWeight
     * @return
     */
    static public HashMap<String,Integer> getOverallProgress(Habit habit, ArrayList<HabitEvent> habitEvents, int idealPerDay, double penaltyWeight){
        ArrayList<Double> dataOverall = new ArrayList<>();
        ArrayList<Double> dataRecent = new ArrayList<>();
        HashMap<String,Integer> scoreAndStats = new HashMap<>();
        Date habitStart = habit.getStartDate();

        //get list of days that are from habit
        ArrayList<Integer> daysOfHabit = getHabitDays(habit);

        double weight = penaltyWeight;
        int idealNumOfEvents;

        int missedDays = 0,tooManyDays = 0,onIdealDays = 0,totalIdeal = 0,totalRecentIdeal =0;

        //setup start time and end times
        Calendar habitCal = Calendar.getInstance();
        habitCal.setTime(habitStart);
        Calendar start = Calendar.getInstance();
        Calendar end = Calendar.getInstance();
        Calendar recentStart = Calendar.getInstance();
        recentStart.setTime(start.getTime());
        end.setTime(start.getTime());
        start.set(habitCal.get(Calendar.YEAR),habitCal.get(Calendar.MONTH),habitCal.get(Calendar.DAY_OF_MONTH));

        //go back only a month
        recentStart.add(Calendar.MONTH,-1);


        //loop through each day
        for (Date date = start.getTime(); !start.after(end); start.add(Calendar.DATE, 1), date = start.getTime()) {
            int numOnDay = 0;
            for (int i = 0 ; i < habitEvents.size(); i++){
                Calendar first = Calendar.getInstance();
                Calendar sec = Calendar.getInstance();
//                Log.d("Integer",""+habitEvents.getClass().getName());
                first.setTime(DateConverter.arrayListToDate(habitEvents.get(i).getStartDate()));
                sec.setTime(date);
                //check if two dates are on same date
                boolean sameDay = first.get(Calendar.DAY_OF_YEAR) == sec.get(Calendar.DAY_OF_YEAR) && first.get(Calendar.YEAR) == sec.get(Calendar.YEAR);
                if(sameDay){
                    //increase num of events that day
                    numOnDay++;
                }
            }
            //check if habit requires event on that day
            if(!daysOfHabit.contains(start.get(Calendar.DAY_OF_WEEK))){
                idealNumOfEvents = 0;
            }
            else{
                idealNumOfEvents = idealPerDay;
            }
            totalIdeal = totalIdeal + idealNumOfEvents;
            //score calculation
            double score = Math.max(Math.min(100,(100 - (weight * Math.abs((idealNumOfEvents-numOnDay))))),0);
            //other stats
            if(idealNumOfEvents-numOnDay == 0){
                onIdealDays++;
            }
            else if (idealNumOfEvents-numOnDay > 0){
                missedDays++;
            }
            else{
                tooManyDays++;
            }
            dataOverall.add(score);
            //recent only last 30 days
            if(start.after(recentStart) && !start.before(recentStart) ){
                totalRecentIdeal = totalRecentIdeal + idealNumOfEvents;
                dataRecent.add(score);
            }
        }
        //put all scores and stats into hashmap and return
        scoreAndStats.put("score", (int) calcAverage(dataOverall));
        scoreAndStats.put("recent",(int) calcAverage(dataRecent));
        scoreAndStats.put("under",missedDays);
        scoreAndStats.put("over",tooManyDays);
        scoreAndStats.put("ideal",onIdealDays);
        return scoreAndStats;
    }

    /**
     * calculates and returns average score from a list of numbers
     * @param array
     * @return
     */
    static private double calcAverage(ArrayList<Double> array){
        double ave = 0;
        for(int i = 0;i < array.size();i++){
            ave = ave + array.get(i);
        }
        return ave / array.size();
    }

    /**
     * returns Arraylist<int> of a given habit's weekdays
     * @param habit
     * @return
     */
    static public ArrayList<Integer> getHabitDays(Habit habit){
        String[] days = habit.getWeekDays();
        ArrayList<Integer> dayInt = new ArrayList<>();
        //loop through each string in list
        for(int i = 0 ; i < days.length; i++ ){
            //switch for converting string to proper int value
            switch(days[i]){
                case "Monday":
                    dayInt.add(1);
                    break;
                case "Tuesday":
                    dayInt.add(2);
                    break;
                case "Wednesday":
                    dayInt.add(3);
                    break;
                case "Thursday":
                    dayInt.add(4);
                    break;
                case "Friday":
                    dayInt.add(5);
                    break;
                case "Saturday":
                    dayInt.add(6);
                    break;
                case "Sunday":
                    dayInt.add(7);
                    break;
            }
        }
        return dayInt;
    }
}
