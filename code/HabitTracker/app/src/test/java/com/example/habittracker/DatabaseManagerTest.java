package com.example.habittracker;

import static org.junit.Assert.assertTrue;
import static org.junit.jupiter.api.Assertions.assertEquals;

import com.example.habittracker.utils.SharedInfo;
import com.google.firebase.firestore.CollectionReference;
import com.google.firebase.firestore.DocumentReference;
import com.google.firebase.firestore.FirebaseFirestore;

import org.junit.jupiter.api.Test;
import com.example.habittracker.DatabaseManager;

import java.util.ArrayList;
import java.util.HashMap;

public class DatabaseManagerTest {

    private DatabaseManager mockDM() {
        return DatabaseManager.get();
    }
    private User mockUser() {
        return new User("HabitualHuman");
    }

    @Test
    void testGetInstance() {
        DatabaseManager dm = mockDM();
        assertTrue(dm.getInstance() instanceof FirebaseFirestore);
    }

    @Test
    void testGetUsersColRef() {
        DatabaseManager dm = mockDM();
        assertTrue(dm.getUsersColRef() instanceof CollectionReference);
    }

    @Test
    void testGetHabitColRef() {
        DatabaseManager dm = mockDM();
        SharedInfo.getInstance().setCurrentUser(mockUser());
        assertTrue(dm.getHabitsColRef(SharedInfo.getInstance().getCurrentUser().getUsername()) instanceof CollectionReference);
    }

    @Test
    void testGetUserColName() {
        DatabaseManager dm = mockDM();
        assertEquals(dm.getUsersColName(), "Users");
    }

    @Test
    void testGetHabitColName() {
        DatabaseManager dm = mockDM();
        assertEquals(dm.getHabitsColName(), "Habits");
    }

    @Test
    void testGetHabitEventColName() {
        DatabaseManager dm = mockDM();
        assertEquals(dm.getHabitEventsColName(), "HabitEvents");
    }

    @Test
    void testAddUsersDocument() {
        DatabaseManager dm = mockDM();
        SharedInfo.getInstance().setCurrentUser(mockUser());
        HashMap<String, Object> map = new HashMap<>();
        assertTrue(dm.addUsersDocument(SharedInfo.getInstance().getCurrentUser().getUsername(), map) instanceof DocumentReference);
    }

    @Test
    void testAddHabitDocument() {
        DatabaseManager dm = mockDM();
        SharedInfo.getInstance().setCurrentUser(mockUser());
        HashMap<String, Object> map = new HashMap<>();
        assertTrue(dm.addHabitDocument(SharedInfo.getInstance().getCurrentUser().getUsername(), "abc", map) instanceof DocumentReference);
    }

    /** Rest void functions should be tested using intent testing.*/




}
