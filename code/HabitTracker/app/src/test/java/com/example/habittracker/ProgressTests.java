package com.example.habittracker;

import com.example.habittracker.Habit;
import com.example.habittracker.activities.tracking.ProgressUtil;
import com.example.habittracker.utils.DateConverter;
import com.example.habittracker.utils.SharedInfo;

import static org.junit.jupiter.api.Assertions.*;

import android.util.Log;

import org.junit.jupiter.api.Test;

import java.util.ArrayList;
import java.util.Calendar;
import java.util.HashMap;

public class ProgressTests {

    /**
     * create a test habit
     * @return
     */
    private Habit mockHabit(){
        SharedInfo.getInstance().setCurrentUser(new User(""));
        ArrayList<String> weekdays = new ArrayList<>();
        weekdays.add("Sun");
        weekdays.add("Mon");
        weekdays.add("Tue");
        weekdays.add("Wed");
        weekdays.add("Thu");
        weekdays.add("Fri");
        weekdays.add("Sat");


        Calendar cal = Calendar.getInstance();
        cal.add(Calendar.DATE,-2);
        Habit habit = new Habit("Habit","displayHabit","Reason", cal.getTime(),0, weekdays);
        return habit;
    }

    /**
     * create a test event list
     * @param habit
     * @return
     */
    private ArrayList<HabitEvent> mockEvents(Habit habit){
        ArrayList<HabitEvent> events = new ArrayList<>();
        events.add(new HabitEvent("habit","id","comment", DateConverter.dateToArrayList(habit.getStartDate()),"location","image"));
        return events;
    }

    /**
     * tests the proper calculation of score and stats for the habit and eventlist
     */
    @Test
    void testGetOverall(){
        Habit habit = mockHabit();
        ArrayList<HabitEvent> events = mockEvents(habit);
        HashMap<String,Integer> hash = ProgressUtil.getOverallProgress(habit,events,1,100);
        assertEquals(33,hash.get("overall"));
        assertEquals(33,hash.get("recent"));
        assertEquals(1,hash.get("ideal"));
        assertEquals(2,hash.get("under"));
        assertEquals(0,hash.get("over"));
    }


    @Test
    void testDays(){
        Habit habit = mockHabit();
        ArrayList<Integer> array = new ArrayList<Integer>();
        array.add(1);
        array.add(2);
        array.add(3);
        array.add(4);
        array.add(5);
        array.add(6);
        array.add(7);
        assertArrayEquals(array.toArray(),ProgressUtil.getHabitDays(habit).toArray());
    }
}

