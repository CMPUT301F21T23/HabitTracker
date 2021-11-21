package com.example.habittracker;

import static org.junit.jupiter.api.Assertions.*;

import android.util.Log;

import com.example.habittracker.utils.DateConverter;

import org.junit.jupiter.api.Test;

import java.lang.reflect.Array;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.Calendar;
import java.util.Date;
import java.util.GregorianCalendar;

public class DateConverterTest {
    @Test
    void testDateToArrayList() {
        Date date = new GregorianCalendar(2014, Calendar.FEBRUARY, 11).getTime();
        ArrayList<Integer> ans = DateConverter.dateToArrayList(date);
        ArrayList<Integer> expected = new ArrayList<>();
        expected.add(2014);
        expected.add(2);
        expected.add(11);
        assertEquals(expected, ans);
    }

    @Test
    void testArrayListToDate() {
        Date date = new GregorianCalendar(2014, Calendar.FEBRUARY, 11).getTime();
        ArrayList<Long> expected = new ArrayList<>();
        expected.add(new Long(2014));
        expected.add(new Long(2));
        expected.add(new Long(11));
        Date ans = DateConverter.arrayListToDate(expected);
        assertEquals(date, ans);
    }

    @Test
    void testGetCurrentWeekDay() {
        String weekday = DateConverter.getCurrentWeekDay();
        String weekdays[] = {"Sun", "Mon", "Tue", "Wed", "Thu", "Fri", "Sat"};
        assertTrue(Arrays.asList(weekdays).contains(weekday));
    }

    @Test
    void testArrayListToString() {
        String date = "2014-2-11";
        ArrayList<Integer> expected = new ArrayList<>();
        expected.add(2014);
        expected.add(2);
        expected.add(11);
        String ans = DateConverter.arrayListToString(expected);
        assertEquals(date,ans);
    }

    @Test
    void testStringToArrayList() {
        String date = "2014-2-11";
        ArrayList<Integer> expected = new ArrayList<>();
        expected.add(2014);
        expected.add(2);
        expected.add(11);
        ArrayList<Integer> ans = DateConverter.stringToArraylist(date);
        assertEquals(expected,ans);
    }

    @Test
    void testIntArrayListToDate() {
        Date date = new GregorianCalendar(2014, Calendar.FEBRUARY, 11).getTime();
        ArrayList<Integer> expected = new ArrayList<>();
        expected.add(2014);
        expected.add(2);
        expected.add(11);
        Date ans = DateConverter.intArrayListToDate(expected);
        assertEquals(date.getYear(), ans.getYear());
        assertEquals(date.getMonth(), ans.getMonth());
        assertEquals(date.getDay(), ans.getDay());
    }
}
