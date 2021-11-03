package com.example.habittracker.activities.tracking;

import android.util.Log;

import com.example.habittracker.Habit;
import com.example.habittracker.HabitEvent;

import java.util.ArrayList;
import java.util.Calendar;
import java.util.Date;
import java.util.HashMap;

public class ProgressUtil {

    static public HashMap<String,Integer> getOverallProgress(Habit habit, ArrayList<HabitEvent> habitEvents, int idealPerDay, double penaltyWeight){
        ArrayList<Double> dataOverall = new ArrayList<>();
        ArrayList<Double> dataRecent = new ArrayList<>();
        HashMap<String,Integer> scoreAndStats = new HashMap<>();
        Date habitStart = habit.getStartDate();

        //get list of days that are from habit
        ArrayList<Integer> daysOfHabit = DaysUtil.getHabitDays(habit);

        double weight = penaltyWeight;
        int idealNumOfEvents;

        int missedDays = 0,tooManyDays = 0,onIdealDays = 0,totalIdeal = 0,totalRecentIdeal =0;

        Calendar start = Calendar.getInstance();
        start.setTime(habitStart);
        Calendar end = Calendar.getInstance();
        Calendar recentStart = Calendar.getInstance();
        recentStart.add(Calendar.MONTH,-1);


        for (Date date = start.getTime(); start.before(end); start.add(Calendar.DATE, 1), date = start.getTime()) {
            int numOnDay = 0;
            for (int i = 0 ; i < habitEvents.size(); i++){
                Calendar first = Calendar.getInstance();
                Calendar sec = Calendar.getInstance();
                first.setTime(habitEvents.get(i).getStartDate());
                sec.setTime(date);
                boolean sameDay = first.get(Calendar.DAY_OF_YEAR) == sec.get(Calendar.DAY_OF_YEAR) && first.get(Calendar.YEAR) == sec.get(Calendar.YEAR);
                if(sameDay){
                    numOnDay++;
                }
            }
            if(!daysOfHabit.contains(start.get(Calendar.DAY_OF_WEEK))){
                idealNumOfEvents = 0;
            }
            else{
                idealNumOfEvents = idealPerDay;
            }
            totalIdeal = totalIdeal + idealNumOfEvents;
            double score = Math.max(Math.min(100,(100 - (weight * Math.abs((idealNumOfEvents-numOnDay))))),0);
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

    static private double calcAverage(ArrayList<Double> array){
        double ave = 0;
        for(int i = 0;i < array.size();i++){
            ave = ave + array.get(i);
        }
        return ave / array.size();
    }
}
