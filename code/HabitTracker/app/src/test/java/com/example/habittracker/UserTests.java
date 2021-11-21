package com.example.habittracker;

import static org.junit.Assert.assertEquals;


import org.junit.Before;
import org.junit.BeforeClass;
import org.junit.jupiter.api.Test;

public class UserTests {
    /**
     * Make sure the username can be get, set, and cleared correctly
     */

    String testUsername1 = "HabitualHuman";
    String testUsername2 = "ProfessionalProcrastinator";

    @Test
    public void testGetUsername() {
        User testUser = new User(testUsername1);
        assertEquals(testUser.getUsername(), testUsername1);
    }

    @Test
    public void testSetUsername() {
        User testUser = new User(testUsername1);
        testUser.setUsername(testUsername2);
        assertEquals(testUser.getUsername(), testUsername2);
    }

    @Test
    public void testClearUsername() {
        User testUser = new User(testUsername1);
        testUser.clearUsername();
        assertEquals(testUser.getUsername(), "");
    }
}
