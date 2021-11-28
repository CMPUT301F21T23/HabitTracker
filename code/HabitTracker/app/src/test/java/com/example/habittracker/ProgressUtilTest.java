package com.example.habittracker;

import static org.junit.jupiter.api.Assertions.assertEquals;

import com.example.habittracker.activities.tracking.ProgressUtil;

import org.junit.jupiter.api.Test;

import java.util.ArrayList;
import java.util.Calendar;
import java.util.GregorianCalendar;

public class ProgressUtilTest {

    @Test
    void testGetHabitDays() {
        ArrayList<String> weekDays = new ArrayList<>();
        weekDays.add("Mon");
        Habit h = new Habit ("Title", "no reason",new GregorianCalendar(2014, Calendar.FEBRUARY, 11).getTime(), weekDays,0,0, true, new User("test"));

        ArrayList<Integer> ans = ProgressUtil.getHabitDays(h);

        assertEquals(ans.get(0), 2);
    }
}
