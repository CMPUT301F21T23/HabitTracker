package com.example.habittracker;
import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertFalse;

import com.example.habittracker.utils.SharedInfo;

import org.junit.Before;
import org.junit.jupiter.api.Test;

public class SharedInfoTests {
    /**
     * Unit test the SharedInfo class
     */

    String testUsername1 = "HabitualHuman";

    @Test
    public void testSameInstance() {
        assertEquals(SharedInfo.getInstance(), SharedInfo.getInstance());
    }

    @Test
    public void testGetUser() {
        User testUser = new User(testUsername1);
        SharedInfo.getInstance().setCurrentUser(testUser);
        assertEquals(SharedInfo.getInstance().getCurrentUser().getUsername(), testUsername1);
    }

    @Test
    public void testClearUser() {
        User testUser = new User(testUsername1);
        SharedInfo.getInstance().setCurrentUser(testUser);
        assertFalse(SharedInfo.getInstance().getCurrentUser().getUsername().equals(""));
        SharedInfo.getInstance().clearCurrentUser();
        assertEquals(SharedInfo.getInstance().getCurrentUser().getUsername(), "");
    }
}
