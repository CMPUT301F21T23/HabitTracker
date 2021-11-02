package com.example.habittracker.activities.eventlist;


import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.appcompat.app.AppCompatActivity;

import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.ListView;
import android.widget.TextView;

import com.example.habittracker.DatabaseManager;
import com.example.habittracker.Habit;
import com.example.habittracker.HabitEvent;
import com.example.habittracker.NavBarManager;
import com.example.habittracker.R;
import com.example.habittracker.activities.fragments.AddEventFragment;
import com.example.habittracker.activities.fragments.OnFragmentInteractionListener;
import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.Task;
import com.google.android.material.floatingactionbutton.FloatingActionButton;
import com.google.firebase.firestore.CollectionReference;
import com.google.firebase.firestore.EventListener;
import com.google.firebase.firestore.FirebaseFirestore;
import com.google.firebase.firestore.FirebaseFirestoreException;
import com.google.firebase.firestore.Query;
import com.google.firebase.firestore.QueryDocumentSnapshot;
import com.google.firebase.firestore.QuerySnapshot;

import java.math.BigDecimal;
import java.util.ArrayList;

/**
 * The standard MainActivity class that extends AppCompatActivity and implements
 * a custom interface called OnFragmentInteractionListener. Some of the codes are
 * from CMPUT 301 Lab 3 instructions.
 * @author Yongquan Zhang
 */
public class EventListActivity extends AppCompatActivity implements OnFragmentInteractionListener {
    private ListView eventList;
    private ArrayAdapter<HabitEvent> eventAdapter;
    private ArrayList<HabitEvent> eventDataList;
    private String delete_event=null;
    private Boolean flag = false;

    /************************************/
    /*************************************/
    /**
     * Override the OnCreate method. Set up a list of event objects and display them
     * in a ListView.
     * @param savedInstanceState
     */
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_event_list);

        NavBarManager nav = new NavBarManager(this,findViewById(R.id.bottom_navigation));
        eventList = findViewById(R.id.event_list);
        eventDataList = new ArrayList<>();
        String []habits={"Habit 1","Habit 1","Habit 1"};
        String []dates={"2020-01-01","2020-01-01","2020-01-01"};
        String []commentss={"123","123","123"};
        for(int i = 0; i < habits.length;i++){
            HabitEvent temp_1 = new HabitEvent();
            temp_1.setHabit(habits[i]);
            temp_1.setStartDate(dates[i]);
            temp_1.setComment(commentss[i]);
            eventDataList.add(temp_1);
//            temp_1.updateDB();
        }
        eventAdapter = new CustomList(this, eventDataList);
        eventList.setAdapter(eventAdapter);
        /************/
//
//        String usersColName = "Users";
//        String habitsColName = "Habits";
//        String habitEventsColName = "HabitEvents";
//        String DB_TAG = "DatabaseManager";
//
//        DatabaseManager dm = DatabaseManager.get();
//
//        CollectionReference colRef = dm.getUsersColRef()
//                .document("user1")
//                .collection(habitsColName)
//                .document("Habit 1")
//                .collection(habitEventsColName);
//
//
//        colRef.addSnapshotListener(new EventListener<QuerySnapshot>() {
//            @Override
//            public void onEvent(@Nullable QuerySnapshot queryDocumentSnapshots, @Nullable
//                    FirebaseFirestoreException error) {
//
//                eventDataList.clear();
//                for(QueryDocumentSnapshot doc: queryDocumentSnapshots)
//                {
//                    Log.d(DB_TAG, String.valueOf(doc.getData().get("Habit")));
//                    String eventID = doc.getId();
//                    String habitID = (String) doc.getData().get("Habit");
//                    String startDate = (String) doc.getData().get("startDate");
//                    String comments = (String) doc.getData().get("comment");
//                    String location = (String) doc.getData().get("location");
//                    String image = (String) doc.getData().get("image");
//                    eventDataList.add(new HabitEvent(habitID, eventID,comments, startDate, location, image));
//
//                }
//                eventAdapter.notifyDataSetChanged(); // Notifying the adapter to render any new data fetched
//                //from the cloud
//            }
//        });

        /************/

//        String eventID = document.getId();
//        String habitID = (String) document.getData().get("Habit");
//        String startDate = (String) document.getData().get("startDate");
//        String comments = (String) document.getData().get("comment");
//        String location = (String) document.getData().get("location");
//        String image = (String) document.getData().get("image");
//        eventDataList.add(new HabitEvent(habitID, eventID,comments, startDate, location, image));

//        eventAdapter = new CustomList(this, eventDataList);
//        eventList.setAdapter(eventAdapter);
//        eventAdapter.notifyDataSetChanged();

        /* Add floating button fragment. */
        final FloatingActionButton addEventButton = findViewById(R.id.add_event_button);
        addEventButton.setOnClickListener(new View.OnClickListener() {
            /**
             * Override the onClick method for the "+" button located bottom end of the screen.
             * @param v
             */
            @Override
            public void onClick(View v) {
                new AddEventFragment().show(getSupportFragmentManager(),"ADD_EVENT");
            }
        });

        eventList.setOnItemClickListener(new AdapterView.OnItemClickListener() {
            /**
             * Override the onItemClick method. Once an item on the list is clicked,
             * we pass the selected event object to the floating fragment.
             * @param adapter
             * @param v
             * @param position
             * @param arg3
             */
            public void onItemClick(AdapterView<?> adapter, View v, int position,
                                    long arg3) {
                HabitEvent editE = (HabitEvent) adapter.getItemAtPosition(position);
                new AddEventFragment().newInstance(editE).show(getSupportFragmentManager(),"EDIT_EVENT");
            }
        });





        /************/

        String usersColName = "Users";
        String habitsColName = "Habits";
        String habitEventsColName = "HabitEvents";
        String DB_TAG = "DatabaseManager";

        DatabaseManager dm = DatabaseManager.get();

        CollectionReference colRef = dm.getUsersColRef()
                .document("user1")
                .collection(habitsColName)
                .document("Habit 1")
                .collection(habitEventsColName);
//        CollectionReference colRef = dm.getUsersColRef()
//                .document("user1")
//                .collection(habitEventsColName);
        Query query = colRef.orderBy("date", Query.Direction.ASCENDING);
        colRef.addSnapshotListener(new EventListener<QuerySnapshot>() {
            @Override
            public void onEvent(@Nullable QuerySnapshot queryDocumentSnapshots, @Nullable
                    FirebaseFirestoreException error) {

                eventDataList.clear();
                for(QueryDocumentSnapshot doc: queryDocumentSnapshots)
                {
                    Log.d(DB_TAG, String.valueOf(doc.getData().get("Habit")));
                    String eventID = doc.getId();
//                    String habitID = (String) doc.getData().get("Habit");
                    String startDate = (String) doc.getData().get("startDate");
                    String comments = (String) doc.getData().get("comment");
//                    String location = (String) doc.getData().get("location");
//                    String image = (String) doc.getData().get("image");
                    HabitEvent temp = new HabitEvent();
                    temp.setStartDate(startDate);
                    temp.setComment(comments);
                    temp.setEventId(eventID);
                    eventDataList.add(temp);

                }
                eventAdapter.notifyDataSetChanged(); // Notifying the adapter to render any new data fetched
                //from the cloud
            }
        });

        /************/

    }

    @Override
    public void onOkPressed(HabitEvent newEvent, boolean editFlag) {
        if(!editFlag) {
            eventAdapter.add(newEvent);
            newEvent.updateDB();
            onResume();
        }
        else{
            eventAdapter.notifyDataSetChanged();
            newEvent.editDB();
            onResume();
        }
    }

    @Override
    public void onDeletePressed(HabitEvent newEvent) {
        eventAdapter.remove(newEvent);
        eventAdapter.notifyDataSetChanged();
        newEvent.deleteDB();
        onResume();
    }

    /**
     * Override the onResume method of OnFragmentInteractionListener.
     * Update the sum of frequency in the bottom
     */
    @Override
    public void onResume() {
        super.onResume();
    }
}