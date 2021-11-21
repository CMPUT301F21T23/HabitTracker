package com.example.habittracker;

import static org.junit.jupiter.api.Assertions.assertEquals;

import org.junit.jupiter.api.Test;

import java.util.ArrayList;
import java.util.Calendar;
import java.util.Date;
import java.util.HashMap;

public class HabitTest {

    Date currentDate;
    ArrayList<String> weekDays = new ArrayList<>();

    private Habit mockHabit () {
        weekDays.add("Mon");

        currentDate = new Date();
        return new Habit(
                "Paint",
                "It makes me happy",
                currentDate,
                weekDays);
    }

    @Test
    void testGetTitle() {
        Habit habit = mockHabit();
        assertEquals(habit.getTitle(), "Paint");
    }

//    @Test
//    void testGetTitleDisplay() {
//        Habit habit = mockHabit();
//        assertEquals(habit.getTitleDisplay(), "Paint");
//    }

    @Test
    void testGetReason() {
        Habit habit = mockHabit();
        assertEquals(habit.getReason(), "It makes me happy");
    }

    @Test
    void testGetStartDate() {
        Habit habit = mockHabit();
        assertEquals(habit.getStartDate(), currentDate);
    }

    @Test
    void testGetWeekDays() {
        Habit habit = mockHabit();
        assertEquals(habit.getWeekDays(), weekDays);
    }

    @Test
    void testSetTitle() {
        Habit habit = mockHabit();
        habit.setTitle("Draw");
        assertEquals(habit.getTitle(), "Draw");
    }

    @Test
    void testSetReason() {
        Habit habit = mockHabit();
        habit.setReason("I am working on my masterpiece");
        assertEquals(habit.getReason(), "I am working on my masterpiece");
    }

    @Test
    void testSetStartDate() {
        Habit habit = mockHabit();
        Date newDate = new Date();
        habit.setStartDate(newDate);
        assertEquals(habit.getStartDate(), newDate);
    }

    @Test
    void testToDocument() {
        Habit habit = mockHabit();

        // mimic what the hash map is supposed to look like
        HashMap<String,Object> expected = new HashMap<>();
        expected.put("reason", "It makes me happy");

        ArrayList<Integer> dateAsList = new ArrayList<>();
        Calendar cal = Calendar.getInstance();
        cal.setTime(currentDate);

        dateAsList.add(cal.get(Calendar.YEAR));
        dateAsList.add(cal.get(Calendar.MONTH) +1);
        dateAsList.add(cal.get(Calendar.DAY_OF_MONTH));
        expected.put("dateStarted", dateAsList);

        expected.put("whatDays", weekDays);
        expected.put("progress", 0); // provisional
        expected.put("title", "Paint");

        // the function to test
        HashMap<String, Object> test =  habit.toDocument();

        assertEquals(test, expected);
    }
}