package com.example.habittracker;

import android.util.Log;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;

import com.example.habittracker.utils.HabitEventListCallback;
import com.example.habittracker.utils.HabitListCallback;
import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.OnFailureListener;
import com.google.android.gms.tasks.OnSuccessListener;
import com.google.android.gms.tasks.Task;
import com.google.firebase.firestore.CollectionReference;
import com.google.firebase.firestore.DocumentReference;
import com.google.firebase.firestore.EventListener;
import com.google.firebase.firestore.FirebaseFirestore;
import com.google.firebase.firestore.FirebaseFirestoreException;
import com.google.firebase.firestore.QueryDocumentSnapshot;
import com.google.firebase.firestore.QuerySnapshot;
import com.google.firebase.functions.FirebaseFunctions;
import com.google.firebase.functions.HttpsCallableReference;
import com.google.firebase.functions.HttpsCallableResult;

import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Calendar;
import java.util.Date;
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

    /**
     * Get the DatabaseManager instance
     * @return      {@code }
     */
    public static DatabaseManager get() {
        if (databaseManager == null) {
            databaseManager = new DatabaseManager();
        }
        return databaseManager;
    }

    /**
     * Get the Firestore instance.
     * @return      {@code FirebaseFirestore} Firestore instance
     */
    public FirebaseFirestore getInstance() {
        return db;
    }

    /**
     * Get a reference to the top-level collection: Users
     * @return      {@code CollectionReference} Users collection reference
     */
    public CollectionReference getUsersColRef() {
        return usersColRef;
    }

    /**
     * Get the Users collection name
     * @return      {@code String} Users collection name
     */
    public String getUsersColName() {
        return usersColName;
    }

    /**
     * Get the Habits collection name
     * @return      {@code String} Habits collection name
     */
    public String getHabitsColName() {
        return habitsColName;
    }

    /**
     * Get the Habit Events name
     * @return      {@code String} HabitEvents collection name
     */
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
        docRef.set(doc)
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
        docRef.set(doc)
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
    public void addHabitEventDocument(String userid, String habitTitle, HashMap<String, Object> doc) {
        // Users -> userid (key) -> Habits -> habitTitle (key) -> HabitEvents
        CollectionReference colRef = usersColRef
                .document(userid)
                .collection(habitsColName)
                .document(habitTitle)
                .collection(habitEventsColName);

        colRef.add(doc)
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

    /**
     * delete a habit event for a given habit
     * @param userid        {@code String} User ID
     * @param habitTitle    {@code String} Habit Title
     * @param eventID       {@code String} Event ID
     */
    public void deleteHabitEventDocument(String userid, String habitTitle, String eventID) {
        // Users -> userid (key) -> Habits -> habitTitle (key) -> HabitEvents
        CollectionReference colRef = usersColRef
                .document(userid)
                .collection(habitsColName)
                .document(habitTitle)
                .collection(habitEventsColName);

        colRef.document(eventID)
                .delete()
                .addOnSuccessListener(new OnSuccessListener<Void>() {
                    @Override
                    public void onSuccess(Void aVoid) {
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

    /**
     * edit a habit event for a given habit
     * @param userid        {@code String} User ID
     * @param habitTitle    {@code String} Habit Title
     * @param eventID       {@code String} Event ID
     * @param doc           {@code HashMap<String, Object>} Document
     */
    public void updateHabitEventDocument(String userid, String habitTitle, String eventID, HashMap<String, Object> doc) {
        // Users -> userid (key) -> Habits -> habitTitle (key) -> HabitEvents
        CollectionReference colRef = usersColRef
                .document(userid)
                .collection(habitsColName)
                .document(habitTitle)
                .collection(habitEventsColName);

        colRef.document(eventID)
                .update(doc)
                .addOnSuccessListener(new OnSuccessListener<Void>() {
                    @Override
                    public void onSuccess(Void aVoid) {
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

    /**
     * Deletes a user document along with all of its subcollections.
     * @param userid        {@code String} User ID
     */
    void deleteUserDocument(String userid) {
        // delete all habit documents for this user
        usersColRef.document(userid).collection(habitsColName)
                .get()
                .addOnCompleteListener(new OnCompleteListener<QuerySnapshot>() {
                    @Override
                    public void onComplete(@NonNull Task<QuerySnapshot> task) {
                        if (task.isSuccessful()) {
                            for (QueryDocumentSnapshot document : task.getResult()) {
                                // delete all habit events associated with this document first
                                document.getReference().collection(habitEventsColName)
                                        .get()
                                        .addOnCompleteListener(new OnCompleteListener<QuerySnapshot>() {
                                            @Override
                                            public void onComplete(@NonNull Task<QuerySnapshot> task) {
                                                if (task.isSuccessful()) {
                                                    for (QueryDocumentSnapshot eventDoc : task.getResult()) {
                                                        eventDoc.getReference().delete();
                                                    }
                                                } else {
                                                    Log.d(DB_TAG, String.format("Error getting HabitEvents documents"));
                                                }
                                            }
                                        });
                                String id = document.getId();
                                document.getReference().delete();
                                Log.d(DB_TAG, String.format("Habit document with id %s successfully deleted", id));
                            }
                        } else {
                            Log.d(DB_TAG,
                                    String.format("Error getting Habit documents for user %s: ",
                                            userid, task.getException().toString()));
                        }
                    }
                });
        // finally, delete the document for this user in the Users collection
        usersColRef.document(userid)
                .delete()
                .addOnSuccessListener(new OnSuccessListener<Void>() {
                    @Override
                    public void onSuccess(Void unused) {
                        Log.d(DB_TAG, String.format("User with user id %s successfully deleted from the Users collection", userid));
                    }
                })
                .addOnFailureListener(new OnFailureListener() {
                    @Override
                    public void onFailure(@NonNull Exception e) {
                        Log.d(DB_TAG, String.format("Delete failed on user id %s. Reason: %s", userid, e.toString()));
                    }
                });
    }
    /**
     * This uses a callback to allow another class to get the list of habit of a user
     * @param user
     * @param callback
     */
    public void getAllHabits(String user, HabitListCallback callback) {
        // Users -> userid (key) -> Habits
        CollectionReference doc = usersColRef
                .document(user)
                .collection(habitsColName);
        doc.get().addOnCompleteListener(new OnCompleteListener<QuerySnapshot>() {
            @Override
            public void onComplete(@NonNull Task<QuerySnapshot> task) {
                if (task.isSuccessful()) {
                    ArrayList<Habit> habitArray = new ArrayList<>();
                    for (QueryDocumentSnapshot doc : task.getResult()) {
                        Log.d("",""+doc.getData().get("whatDays")+"-----"+doc.getData().get("dateStarted"));
                        ArrayList<String> daysArray = (ArrayList<String>) doc.getData().get("whatDays");
                        ArrayList<Long> dateArray = (ArrayList<Long>) doc.getData().get("dateStarted");
                        Calendar cal = Calendar.getInstance();
                        if(dateArray == null || daysArray == null){
                            continue;
                        }
                        cal.set(dateArray.get(0).intValue(),dateArray.get(1).intValue()-1,dateArray.get(2).intValue());
                        Date date = cal.getTime();
                        habitArray.add(new Habit(
                                doc.getId(),
                                (String)doc.getData().get("reason"),
                                date,
                                daysArray.toArray(new String[daysArray.size()])
                        ));
                    }
                    callback.onCallbackSuccess(habitArray);
                }
                else{
                    callback.onCallbackFailed();
                }
            }
        });
    }
    /**
     * This method return an array list of all habit events for a habit using a callback function.
     * @param user
     * @param habit
     * @param callback
     */
    public void getAllHabitEvents(String user, Habit habit, HabitEventListCallback callback) {
        // Users -> userid (key) -> Habits -> habitTitle (key) -> HabitEvents
        CollectionReference doc = usersColRef
                .document(user)
                .collection(habitsColName)
                .document(habit.getTitle())
                .collection(habitEventsColName);
        doc.get().addOnCompleteListener(new OnCompleteListener<QuerySnapshot>() {
            @Override
            public void onComplete(@NonNull Task<QuerySnapshot> task) {
                if (task.isSuccessful()) {
                    ArrayList<HabitEvent> eventArray = new ArrayList<>();
                    for (QueryDocumentSnapshot doc : task.getResult()) {
                        Date date;
                        try {
                            date = new SimpleDateFormat("yyyy-MM-dd").parse((String)doc.getData().get("startDate"));
                        } catch (Exception e) {
                            date = new Date();
                        }
                        HabitEvent temp = new HabitEvent();
                        eventArray.add(new HabitEvent(
                        ));
                    }
                    callback.onCallbackSuccess(eventArray);
                }
                else{
                    callback.onCallbackFailed();
                }
            }
        });
    }
}
