package com.example.habittracker;

import android.util.Log;

import androidx.annotation.NonNull;
import androidx.test.platform.app.InstrumentationRegistry;
import androidx.test.rule.ActivityTestRule;

import com.example.habittracker.activities.profile.ProfileActivity;
import com.example.habittracker.utils.CheckPasswordCallback;
import com.example.habittracker.utils.HabitEventListCallback;
import com.example.habittracker.utils.SharedInfo;
import com.example.habittracker.utils.SharingListCallback;
import com.example.habittracker.utils.UserExistsCallback;
import com.example.habittracker.utils.UserListOperationCallback;
import com.google.android.gms.common.data.DataBufferSafeParcelable;
import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.Task;
import com.google.firebase.firestore.DocumentSnapshot;
import com.google.firebase.firestore.FirebaseFirestore;
import com.google.firebase.firestore.QueryDocumentSnapshot;
import com.google.firebase.firestore.QuerySnapshot;
import com.robotium.solo.Solo;

import org.junit.After;
import org.junit.Before;
import org.junit.Rule;
import org.junit.Test;

import java.lang.reflect.Array;
import java.security.NoSuchAlgorithmException;
import java.security.spec.InvalidKeySpecException;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.HashMap;
import java.util.concurrent.CountDownLatch;
import java.util.concurrent.TimeUnit;

public class DatabaseManagerTest {

    private Solo solo;
    @Rule
    public ActivityTestRule<ProfileActivity> rule =
            new ActivityTestRule<>(ProfileActivity.class, true, true);
    FirebaseFirestore db;
    User mockUser;

    /**
     * Runs before all tests and creates solo instance.
     * @throws Exception
     */
    @Before
    public void setUp() throws Exception{
        mockUser = new User("mockUser");
        SharedInfo.getInstance().setCurrentUser(mockUser);
        solo = new Solo(InstrumentationRegistry.getInstrumentation(),rule.getActivity());
        db = FirebaseFirestore.getInstance();
        addMockUser();
    }

    /**
     * Closes the activity after each test
     * @throws Exception
     */
    @After
    public void tearDown() throws Exception{
        solo.finishOpenedActivities();
        deleteMockUser();
    }

    /**
     * Deletes the mock user added to the database.
     */
    public void deleteMockUser() {
        FirebaseFirestore db = FirebaseFirestore.getInstance();
        db.collection(DatabaseManager.get().getUsersColName()).document(mockUser.getUsername()).collection("Habits").document("habit")
                .delete();
        db.collection(DatabaseManager.get().getUsersColName()).document(mockUser.getUsername())
                .delete();

    }

    /**
     * Adds a mock user document to Firestore.
     */
    public void addMockUser() {

        HashMap<String, Object> mockDoc = new HashMap<>();
        mockDoc.put("followers", Arrays.asList("milkyman"));
        mockDoc.put("following", Arrays.asList("strangeman"));
        mockDoc.put("pendingFollowReqs", Arrays.asList("happyman"));
        mockDoc.put("pendingFollowerReqs", Arrays.asList("sadman", "stalkerman"));
        db.collection(DatabaseManager.get().getUsersColName()).document(mockUser.getUsername())
                .set(mockDoc);

    }

    @Test
    public void addUserTest() throws InterruptedException {
        HashMap<String,Object> doc = new HashMap<>();
        DatabaseManager.get().addUsersDocument("databaseTestUser",doc);
        CountDownLatch authSignal = new CountDownLatch(1);
        db.collection(DatabaseManager.get().getUsersColName()).document("databaseTestUser")
                .get().addOnCompleteListener(new OnCompleteListener<DocumentSnapshot>() {
            @Override
            public void onComplete(@NonNull Task<DocumentSnapshot> task) {
                if(task.isSuccessful()){
                    if(!task.getResult().getId().equals("databaseTestUser")) {
                        authSignal.countDown();
                        throw new RuntimeException("User wasn't added.");
                    }
                    authSignal.countDown();
                    db.collection(DatabaseManager.get().getUsersColName()).document("databaseTestUser")
                            .delete();
                }
                else{
                    authSignal.countDown();
                    throw new RuntimeException("User wasn't added.");
                }
            }
        });
        authSignal.await(10, TimeUnit.SECONDS);
    }

    @Test
    public void deleteHabitDocument() throws InterruptedException {
        CountDownLatch authSignal = new CountDownLatch(1);
        HashMap<String,Object> doc = new HashMap<>();
        DatabaseManager dbm = DatabaseManager.get();
        dbm.addHabitDocument(mockUser.getUsername(), "Habit1", doc);
        dbm.deleteHabitDocument(mockUser.getUsername(),"Habit1");
        db.collection(DatabaseManager.get().getUsersColName()).document(mockUser.getUsername()).collection(dbm.getHabitsColName()).document("Habit1")
                .get().addOnCompleteListener(new OnCompleteListener<DocumentSnapshot>() {
            @Override
            public void onComplete(@NonNull Task<DocumentSnapshot> task) {
                if(task.isSuccessful()){
                    if(task.getResult().exists()) {
                        db.collection(DatabaseManager.get().getUsersColName()).document(mockUser.getUsername()).collection(dbm.getHabitsColName()).document("Habit1")
                                .delete();
                        authSignal.countDown();
                        throw new RuntimeException("Habit wasn't deleted.");
                    }
                }
                authSignal.countDown();
            }
        });
        authSignal.await(10, TimeUnit.SECONDS);
    }

    @Test
    public void deleteHabitEventTest() throws InterruptedException {
        CountDownLatch authSignal = new CountDownLatch(1);
        HashMap<String,Object> doc = new HashMap<>();
        doc.put("test","test");
        DatabaseManager dbm = DatabaseManager.get();
        dbm.addHabitDocument(mockUser.getUsername(), "Habit1", doc);
        dbm.addHabitEventDocument(mockUser.getUsername(), "Habit1", doc);
        dbm.getAllHabitEvents(mockUser.getUsername(), "Habit1", new HabitEventListCallback() {
            @Override
            public void onCallbackSuccess(ArrayList<HabitEvent> eventArrayList) {
                if(eventArrayList.size()==0){
                    authSignal.countDown();
                    throw new RuntimeException("No Habit Event");
                }
                for(HabitEvent he: eventArrayList){
                    dbm.deleteHabitEventDocument(mockUser.getUsername(), "Habit1", he.getEventId());
                }
                authSignal.countDown();
            }

            @Override
            public void onCallbackFailed() {
                authSignal.countDown();
                throw new RuntimeException("Error");
            }
        });
        authSignal.await(10, TimeUnit.SECONDS);
        CountDownLatch authSignal2 = new CountDownLatch(1);
        db.collection(DatabaseManager.get().getUsersColName()).document(mockUser.getUsername()).collection(dbm.getHabitsColName()).document("Habit1").collection(dbm.getHabitEventsColName())
                .get().addOnCompleteListener(new OnCompleteListener<QuerySnapshot>() {
            @Override
            public void onComplete(@NonNull Task<QuerySnapshot> task) {
                for(QueryDocumentSnapshot doc: task.getResult()){
                    if(doc.exists()) {
                        authSignal2.countDown();
                        throw new RuntimeException("Habit Event wasn't deleted.");
                    }
                    db.collection(DatabaseManager.get().getUsersColName()).document(mockUser.getUsername()).collection(dbm.getHabitsColName()).document("Habit1")
                            .delete();
                }
                authSignal2.countDown();
            }
        });
        authSignal2.await(10, TimeUnit.SECONDS);
    }

    @Test
    public void addHabitTest() throws InterruptedException {
        CountDownLatch authSignal = new CountDownLatch(1);
        HashMap<String,Object> doc = new HashMap<>();
        DatabaseManager dbm = DatabaseManager.get();
        dbm.addHabitDocument(mockUser.getUsername(), "Habit1", doc);
        db.collection(DatabaseManager.get().getUsersColName()).document(mockUser.getUsername()).collection(dbm.getHabitsColName()).document("Habit1")
                .get().addOnCompleteListener(new OnCompleteListener<DocumentSnapshot>() {
            @Override
            public void onComplete(@NonNull Task<DocumentSnapshot> task) {
                if(task.isSuccessful()){
                    if(!task.getResult().getId().equals("Habit1")) {
                        authSignal.countDown();
                        throw new RuntimeException("Habit wasn't added.");
                    }
                    db.collection(DatabaseManager.get().getUsersColName()).document(mockUser.getUsername()).collection(dbm.getHabitsColName()).document("Habit1")
                            .delete();
                    authSignal.countDown();

                }
                else{
                    authSignal.countDown();
                    throw new RuntimeException("failed to get habit.");
                }
            }
        });
        authSignal.await(10, TimeUnit.SECONDS);
    }

    @Test
    public void addHabitEventTest() throws InterruptedException {
        CountDownLatch authSignal = new CountDownLatch(1);
        HashMap<String,Object> doc = new HashMap<>();
        doc.put("test","test");
        DatabaseManager dbm = DatabaseManager.get();
        dbm.addHabitDocument(mockUser.getUsername(), "Habit1", doc);
        dbm.addHabitEventDocument(mockUser.getUsername(), "Habit1", doc);
        db.collection(DatabaseManager.get().getUsersColName()).document(mockUser.getUsername()).collection(dbm.getHabitsColName()).document("Habit1").collection(dbm.getHabitEventsColName())
                .get().addOnCompleteListener(new OnCompleteListener<QuerySnapshot>() {
            @Override
            public void onComplete(@NonNull Task<QuerySnapshot> task) {
                for(QueryDocumentSnapshot doc: task.getResult()){
                    if(!doc.exists()) {
                        authSignal.countDown();
                        throw new RuntimeException("Habit Event wasn't added.");
                    }
                    db.collection(DatabaseManager.get().getUsersColName()).document(mockUser.getUsername()).collection(dbm.getHabitsColName()).document("Habit1")
                            .delete();
                    authSignal.countDown();

                }
            }
        });
        authSignal.await(10, TimeUnit.SECONDS);
    }

    @Test
    public void editHabitTest() throws InterruptedException {
        CountDownLatch authSignal = new CountDownLatch(1);
        HashMap<String,Object> doc = new HashMap<>();
        doc.put("test","first");
        DatabaseManager dbm = DatabaseManager.get();
        dbm.addHabitDocument(mockUser.getUsername(), "Habit1", doc);
        doc.put("test","second");
        dbm.updateHabitDocument(mockUser.getUsername(), "Habit1", "Habit1", doc);
        db.collection(DatabaseManager.get().getUsersColName()).document(mockUser.getUsername()).collection(dbm.getHabitsColName()).document("Habit1")
                .get().addOnCompleteListener(new OnCompleteListener<DocumentSnapshot>() {
            @Override
            public void onComplete(@NonNull Task<DocumentSnapshot> task) {
                if(task.isSuccessful()){
                    if(!task.getResult().getData().get("test").equals("second")) {
                        authSignal.countDown();
                        throw new RuntimeException("Habit wasn't changed.");
                    }
                    db.collection(DatabaseManager.get().getUsersColName()).document(mockUser.getUsername()).collection(dbm.getHabitsColName()).document("Habit1")
                            .delete();
                    authSignal.countDown();
                }
                else{
                    authSignal.countDown();
                    throw new RuntimeException("Error accessing.");
                }
            }
        });
        authSignal.await(10, TimeUnit.SECONDS);
    }

    @Test
    public void userExistsTest() throws InterruptedException {
        HashMap<String,Object> doc = new HashMap<>();
        DatabaseManager.get().addUsersDocument("databaseTestUser",doc);
        CountDownLatch authSignal = new CountDownLatch(1);
        DatabaseManager.get().userExists("databaseTestUser", new UserExistsCallback() {
            @Override
            public void onCallbackSuccess(String username) {
                db.collection(DatabaseManager.get().getUsersColName()).document(username)
                        .delete();
                authSignal.countDown();
            }

            @Override
            public void onCallbackFailed() {
                authSignal.countDown();
                throw new RuntimeException("User doesn't exist.");
            }
        });
        authSignal.await(10, TimeUnit.SECONDS);
    }

    @Test
    public void checkPasswordTest() throws InterruptedException {
        HashMap<String,Object> doc = new HashMap<>();
        doc.put("hashedPassword","123456");
        DatabaseManager.get().addUsersDocument("databaseUser", doc);
        CountDownLatch authSignal = new CountDownLatch(1);
        DatabaseManager.get().checkPassword("databaseUser", new CheckPasswordCallback() {
            @Override
            public void onCallbackSuccess(String username, String hashedPassword) throws InvalidKeySpecException, NoSuchAlgorithmException {
                if(!(hashedPassword.equals("123456"))){
                    authSignal.countDown();
                    db.collection(DatabaseManager.get().getUsersColName()).document(username)
                            .delete();
                    throw new RuntimeException("password doesn't match");
                }
                db.collection(DatabaseManager.get().getUsersColName()).document(username)
                        .delete();
                authSignal.countDown();
            }

            @Override
            public void onCallbackFailed() {
                authSignal.countDown();
                throw new RuntimeException("Failed to get habit.");
            }
        });
        authSignal.await(10, TimeUnit.SECONDS);
    }

    @Test
    public void getPendingTest() throws InterruptedException {
        CountDownLatch authSignal = new CountDownLatch(1);
        DatabaseManager.get().getPendingFollowers(mockUser.getUsername(), new SharingListCallback() {
            @Override
            public void onCallbackSuccess(ArrayList<String> dataList) {
                ArrayList<String> testList = new ArrayList<>();
                testList.add("sadman");
                testList.add("stalkerman");
                if(!dataList.containsAll(testList)){
                    authSignal.countDown();
                    throw new RuntimeException("Not all requests for found");
                }
                authSignal.countDown();
            }

            @Override
            public void onCallbackFailure(String reason) {
                authSignal.countDown();
                throw new RuntimeException("Error accessing.");
            }
        });
        authSignal.await(10, TimeUnit.SECONDS);
    }

    @Test
    public void removeUserListItemTest() throws InterruptedException {
        CountDownLatch authSignal = new CountDownLatch(1);
        DatabaseManager.get().removeUserListItem(mockUser.getUsername(), "sadman", "pendingFollerReqs", new UserListOperationCallback() {
            @Override
            public void onCallbackSuccess(String userid) {
                authSignal.countDown();
            }

            @Override
            public void onCallbackFailure(String reason) {
                authSignal.countDown();
                throw new RuntimeException("Error accessing.");
            }
        });
        authSignal.await(10, TimeUnit.SECONDS);
        CountDownLatch authSignal2 = new CountDownLatch(1);
        DatabaseManager.get().getPendingFollowers(mockUser.getUsername(), new SharingListCallback() {
            @Override
            public void onCallbackSuccess(ArrayList<String> dataList) {
                if(!dataList.contains("sadman")){
                    authSignal2.countDown();
                    throw new RuntimeException("Not all requests for found");
                }
                authSignal2.countDown();
            }

            @Override
            public void onCallbackFailure(String reason) {
                authSignal2.countDown();
                throw new RuntimeException("Error accessing.");
            }
        });
        authSignal2.await(10, TimeUnit.SECONDS);
    }

    @Test
    public void acceptFollowRequestTest() throws InterruptedException {
        CountDownLatch authSignal = new CountDownLatch(1);
        DatabaseManager.get().acceptFollowRequest(mockUser.getUsername(), "happyman", new UserListOperationCallback() {
            @Override
            public void onCallbackSuccess(String userid) {
                DatabaseManager.get().getUserListItems(mockUser.getUsername(), "followers", new SharingListCallback() {
                    @Override
                    public void onCallbackSuccess(ArrayList<String> dataList) {
                        if(!dataList.contains("happyman")){
                            authSignal.countDown();
                            throw new RuntimeException("Didn't add user to followers.");
                        }
                        authSignal.countDown();
                    }

                    @Override
                    public void onCallbackFailure(String reason) {
                        authSignal.countDown();
                        throw new RuntimeException("Error accessing.");
                    }
                });
            }

            @Override
            public void onCallbackFailure(String reason) {
                authSignal.countDown();
                throw new RuntimeException("Error accessing.");
            }
        });
        authSignal.await(10, TimeUnit.SECONDS);
    }

    @Test
    public void getUserListItemsTest() throws InterruptedException {
        CountDownLatch authSignal = new CountDownLatch(1);
        DatabaseManager.get().getUserListItems(mockUser.getUsername(), "following", new SharingListCallback() {
            @Override
            public void onCallbackSuccess(ArrayList<String> dataList) {
                ArrayList<String> testList = new ArrayList<>();
                testList.add("strangeman");
                if(!dataList.containsAll(testList)){
                    authSignal.countDown();
                    throw new RuntimeException("Didn't get proper following.");
                }
                authSignal.countDown();
            }

            @Override
            public void onCallbackFailure(String reason) {

            }
        });
        authSignal.await(10, TimeUnit.SECONDS);
    }
}
