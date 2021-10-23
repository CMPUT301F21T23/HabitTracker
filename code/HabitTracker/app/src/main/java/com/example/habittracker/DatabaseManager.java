package com.example.habittracker;

import android.util.Log;

import androidx.annotation.NonNull;

import com.google.android.gms.tasks.OnFailureListener;
import com.google.android.gms.tasks.OnSuccessListener;
import com.google.firebase.firestore.CollectionReference;
import com.google.firebase.firestore.DocumentReference;
import com.google.firebase.firestore.FirebaseFirestore;

import java.util.HashMap;

/**
 * DatabaseManager is a singleton class that holds the instance of a Cloud Firestore database.
 * It also enforces data schema for the HabitTracker app.
 */
public class DatabaseManager {
    // holds database connection
    private FirebaseFirestore db;
    private final CollectionReference usersColRef;

    // initialize the names of the collections only once to ensure consistency
    private String usersColName = "Users";
    private String habitsColName = "Habits";
    private String habitEventsColName = "HabitEvents";
    private String DB_TAG = "DatabaseManager";

    // private constructor: no one from outside is allowed to invoke this
    private DatabaseManager() {
        // get the Cloud Firestore instance
        db = FirebaseFirestore.getInstance();
        usersColRef = db.collection(usersColName);
    }

    // enforce singleton pattern
    private static DatabaseManager databaseManager;
    public static DatabaseManager get() {
        if (databaseManager == null) {
            databaseManager = new DatabaseManager();
        }
        return databaseManager;
    }

    public FirebaseFirestore getInstance() {
        return db;
    }

    public String getUsersColName() {
        return usersColName;
    }

    public String getHabitsColName() {
        return habitsColName;
    }

    public String getHabitEventsColName() {
        return habitEventsColName;
    }

    /**
     * Add a document to the Users collection with the given userid.
     * @param userid    {@code String} User ID for the document
     * @param doc       {@code HashMap<String, Object>} Document
     * @return          {@code DocumentReference} DocumentReference for the User document created
     */
    DocumentReference addUsersDocument(String userid, HashMap<String, Object> doc) {
        // instantiate the document
        DocumentReference docRef = usersColRef.document(userid);

        // set the data
        docRef
                .set(doc)
                .addOnSuccessListener(new OnSuccessListener<Void>() {
                    @Override
                    public void onSuccess(Void unused) {
                        Log.d(DB_TAG, String.format("Document with id %s successfully added to %s collection", userid, usersColName));
                    }
                })
                .addOnFailureListener(new OnFailureListener() {
                    @Override
                    public void onFailure(@NonNull Exception e) {
                        Log.d(DB_TAG, String.format("Document with id %s failed to be added to %s collection. Reason: ",
                                userid, usersColName, e.toString()));
                    }
                });

        return docRef;
    }

    /**
     * Adds a habit to the Habits collection for a given userid.
     * @param userid        {@code String} User ID
     * @param habitTitle    {@code String} Key of the Habit
     * @param doc           {@code HashMap<String, Object>} Document
     * @return              {@code DocumentReference} DocumentReference for the Habit document created
     */
    DocumentReference addHabitDocument(String userid, String habitTitle, HashMap<String, Object> doc) {
        // instantiate the document
        DocumentReference docRef = usersColRef.document(userid).collection(habitsColName).document(habitTitle);

        // set the data
        docRef
                .set(doc)
                .addOnSuccessListener(new OnSuccessListener<Void>() {
                    @Override
                    public void onSuccess(Void unused) {
                        Log.d(DB_TAG, String.format("Document with habit title %s successfully added to the Habits collection",
                                habitTitle));
                    }
                })
                .addOnFailureListener(new OnFailureListener() {
                    @Override
                    public void onFailure(@NonNull Exception e) {
                        Log.d(DB_TAG, String.format("Document with habit title %s failed to be added to the Habits collection",
                                habitTitle));
                    }
                });
        return docRef;
    }

    /**
     * Adds a habit event for a given habit.
     * @param userid        {@code String} User ID
     * @param habitTitle    {@code String} Habit Title
     * @param doc           {@code HashMap<String, Object>} Document
     */
    void addHabitEventDocument(String userid, String habitTitle, HashMap<String, Object> doc) {
        // Users -> userid (key) -> Habits -> habitTitle (key) -> HabitEvents
        CollectionReference colRef = usersColRef
                .document(userid)
                .collection(habitsColName)
                .document(habitTitle)
                .collection(habitEventsColName);
        colRef
                .add(doc)
                .addOnSuccessListener(new OnSuccessListener<DocumentReference>() {
                    @Override
                    public void onSuccess(DocumentReference documentReference) {
                        Log.d(DB_TAG, String.format("HabitEvent successfully created for Habit with title %s",
                                habitTitle));
                    }
                })
                .addOnFailureListener(new OnFailureListener() {
                    @Override
                    public void onFailure(@NonNull Exception e) {
                        Log.d(DB_TAG, String.format("HabitEvent failed to be created for Habit with title %s",
                                habitTitle));
                    }
                });
    }
}
