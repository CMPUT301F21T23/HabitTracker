package com.example.habittracker;

import com.example.habittracker.activities.tracking.ProgressUtil;
import com.example.habittracker.utils.DateConverter;
import com.example.habittracker.utils.SharedInfo;

import static org.junit.jupiter.api.Assertions.*;

import android.util.Log;
import org.junit.jupiter.api.Test;
import java.lang.reflect.Array;
import java.util.ArrayList;
import java.util.Calendar;
import java.util.HashMap;


public class HabitEventTest {

    /**
     * create a test habit event
     * @return
     */
    private HabitEvent mockHabitEvent(){
//        HabitEvent HE = new HabitEvent();
        ArrayList<Integer> date = new ArrayList<>();
        date.add(2021);
        date.add(1);
        date.add(1);
        String test_habit = "Habit 1";
        String test_comments = "This is comment";
        String test_location = "University of Alberta";
        String test_eventID = "123";
        SharedInfo.getInstance().setCurrentUser(new User("mock"));
//        HE.setHabit(test_habit);
//        HE.setEventId(test_eventID);
//        HE.setComment(test_comments);
//        HE.setStartDate(date);
//        HE.setLocation(test_location);
        HabitEvent HE = new HabitEvent(test_habit,test_eventID,test_comments,date,test_location,"");
        return HE;
    }

    @Test
    void testGetComment(){
        HabitEvent temp = mockHabitEvent();
        String ans = temp.getComment();
        assertEquals(ans,"This is comment");
    }

    @Test
    void testGetStartDate(){
        HabitEvent temp = mockHabitEvent();
        ArrayList<Integer> ans = temp.getStartDate();
        int a1 = ans.get(0);
        int a2 = ans.get(1);
        int a3 = ans.get(2);
        assertEquals(a1,2021);
        assertEquals(a2,1);
        assertEquals(a3,1);
    }

    @Test
    void testSetComment(){
        HabitEvent temp = mockHabitEvent();
        temp.setComment("not a comment");
        String ans = temp.getComment();
        assertEquals(ans,"not a comment");
    }

    @Test
    void testSetStartDate(){
        HabitEvent temp = mockHabitEvent();
        ArrayList<Integer> input = new ArrayList<>();
        input.add(2020);
        input.add(9);
        input.add(9);
        temp.setStartDate(input);
        ArrayList<Integer> ans = temp.getStartDate();
        String ans1 = DateConverter.arrayListToString(ans);
        assertEquals(ans1,"2020-9-9");
    }

    @Test
    void testGetHabit(){
        HabitEvent temp = mockHabitEvent();
        String ans = temp.getHabit();
        assertEquals(ans,"Habit 1");
    }

    @Test
    void testGetLocation(){
        HabitEvent temp = mockHabitEvent();
        String ans = temp.getLocation();
        assertEquals(ans,"University of Alberta");
    }

    @Test
    void testSetLocation(){
        HabitEvent temp = mockHabitEvent();
        temp.setLocation("ualberta");
        String ans = temp.getLocation();
        assertEquals(ans,"ualberta");
    }

//    @Test
//    void testHabit(){
//        HabitEvent temp = mockHabitEvent();
//        temp.setHabit("Habit 2");
//        String ans = temp.getHabit();
//        assertEquals(ans,"Habit 2");
//    }

//    @Test
//    void setHabit(){
//        HabitEvent temp = mockHabitEvent();
//        temp.setHabit("Habit 2");
//        String ans = temp.getHabit();
//        assertEquals(ans,"Habit 2");
//    }

    @Test
    void setEventID(){
        HabitEvent temp = mockHabitEvent();
        temp.setEventId("abc");
        String ans = temp.getEventId();
        assertEquals(ans,"abc");
    }

    @Test
    void getEventID(){
        HabitEvent temp = mockHabitEvent();
        String ans = temp.getEventId();
        assertEquals(ans,"123");
    }
}
