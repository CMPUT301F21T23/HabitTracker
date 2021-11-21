package com.example.habittracker.activities.eventlist;


import static com.example.habittracker.utils.DateConverter.arrayListToString;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.appcompat.app.AppCompatActivity;

import android.content.Intent;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.Button;
import android.widget.ListView;
import android.widget.TextView;

import com.example.habittracker.DatabaseManager;
import com.example.habittracker.Habit;
import com.example.habittracker.HabitEvent;
import com.example.habittracker.NavBarManager;
import com.example.habittracker.R;
import com.example.habittracker.activities.HabitViewActivity;
import com.example.habittracker.activities.fragments.AddEventFragment;
import com.example.habittracker.activities.fragments.OnFragmentInteractionListener;
import com.example.habittracker.utils.HabitEventListCallback;
import com.example.habittracker.utils.HabitListCallback;
import com.example.habittracker.utils.SharedInfo;
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

import java.io.Serializable;
import java.math.BigDecimal;
import java.util.ArrayList;
import java.util.Arrays;

/**
 * The EventListActivity class that extends AppCompatActivity and implements
 * a custom interface called OnFragmentInteractionListener. Some of the codes are
 * from CMPUT 301 Lab 3 instructions.
 * @author Yongquan Zhang
 */
public class EventListActivity extends AppCompatActivity implements OnFragmentInteractionListener {
    public ListView eventList;
    private ArrayAdapter<HabitEvent> eventAdapter;
    private ArrayList<HabitEvent> eventDataList;
    private String delete_event=null;
    private Boolean flag = false;
    private Habit temp_h;

    /**
     * Override the OnCreate method. Set up a list of event objects and display them
     * in a ListView.
     * @param savedInstanceState        {@code Bundle} savedInstanceState
     */
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_event_list);

        NavBarManager nav = new NavBarManager(this,findViewById(R.id.bottom_navigation));
        eventList = findViewById(R.id.event_list);
        eventDataList = new ArrayList<>();

        eventAdapter = new CustomList(this, eventDataList);
        eventList.setAdapter(eventAdapter);


        Intent intent = getIntent();
        temp_h = (Habit) intent.getSerializableExtra("habit");

        Button back = findViewById(R.id.list_back);
        back.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                finish();
            }
        });

        /* Add floating button fragment. */
        final FloatingActionButton addEventButton = findViewById(R.id.add_event_button);
        addEventButton.setOnClickListener(new View.OnClickListener() {
            /**
             * Override the onClick method for the "+" button located bottom end of the screen.
             * @param v         {@code View} view
             */
            @Override
            public void onClick(View v) {
//                new AddEventFragment().show(getSupportFragmentManager(),"ADD_EVENT");
                Intent intent = new Intent(getApplicationContext(), EventDetailActivity.class);
                Bundle bundle = new Bundle();
                bundle.putSerializable("habit", (Serializable) temp_h);
                intent.putExtras(bundle);
                startActivity(intent);
                finish();
            }
        });

        eventList.setOnItemClickListener(new AdapterView.OnItemClickListener() {
            /**
             * Override the onItemClick method. Once an item on the list is clicked,
             * we pass the selected event object to the floating fragment.
             * @param adapter       {@code AdapterView<?>} the habit event adapter
             * @param v             {@code View} view
             * @param position      {@code int} index of selected item
             * @param arg3          {@code long} arg3
             */
            public void onItemClick(AdapterView<?> adapter, View v, int position,
                                    long arg3) {
                HabitEvent editE = (HabitEvent) adapter.getItemAtPosition(position);
//                new EventDetailActivity().newInstance(editE).show(getSupportFragmentManager(),"EDIT_EVENT");
                Intent intent = new Intent(getApplicationContext(), EventDetailActivity.class);
                Bundle bundle = new Bundle();
                bundle.putSerializable("event", (Serializable) editE);
                bundle.putSerializable("habit", (Serializable) temp_h);
                intent.putExtras(bundle);
                startActivity(intent);
                finish();

            }

        });

        getEvents();
    }

    /**
     * Override the onOkPressed method of OnFragmentInteractionListener.
     * @param newEvent       {@code HabitEvent} the habit event need to be added or edited
     * @param editFlag       {@code boolean} if need to edit: editFlag = true; otherwise false.
     */
    @Override
    public void onOkPressed(HabitEvent newEvent, boolean editFlag) {
        if(!editFlag) {
//            eventAdapter.add(newEvent);
//            eventAdapter.notifyDataSetChanged();
            newEvent.updateDB();
            onResume();
            getEvents();
        }
        else{
//            eventAdapter.notifyDataSetChanged();
            newEvent.editDB();
            onResume();
            getEvents();
        }
    }

    /**
     * Override the onDeletePressed method of OnFragmentInteractionListener.
     * @param deleteEvent      {@code HabitEvent} the habit event need to be deleted
     */
    @Override
    public void onDeletePressed(HabitEvent deleteEvent) {
//        eventAdapter.remove(deleteEvent);
//        eventAdapter.notifyDataSetChanged();
        deleteEvent.deleteDB();
        onResume();
        getEvents();
    }

    /**
     * Override the onResume method of OnFragmentInteractionListener.
     * Update the sum of frequency in the bottom
     */
    @Override
    public void onResume() {
        super.onResume();
    }

    private void getEvents() {
        // set up snapshot listener
        String usersColName = "Users";
        String habitsColName = "Habits";
        String habitEventsColName = "HabitEvents";
        String DB_TAG = "DatabaseManager";

        ArrayList<String> habit_list = new ArrayList<>();

        DatabaseManager dm = DatabaseManager.get();
        CollectionReference colRef;
//        dm.getAllHabits("John_test_user", new HabitListCallback() {
        // Replace with this line when sharedInfo is up
//        dm.getAllHabits(SharedInfo.getInstance().getCurrentUser().getUsername(), new HabitListCallback() {
//            /**
//             * Set callback to get all Habit IDs
//             * @param habitList
//             */
//            @Override
//            public void onCallbackSuccess(ArrayList<Habit> habitList) {
//                for(int i = 0;i<habitList.size();i++) {
//                    System.out.println(habitList.get(i).getTitle());
//                    habit_list.add(habitList.get(i).getTitle());
//                    Log.d("Here", habit_list.get(i));
//                    Log.d("list size", String.valueOf(habitList.size()));
//                }
                eventDataList.clear();

//                for (int i = 0;i<habit_list.size();i++) {
                dm.getAllHabitEvents(SharedInfo.getInstance().getCurrentUser().getUsername(), temp_h, new HabitEventListCallback() {
                        @Override
                        public void onCallbackSuccess(ArrayList<HabitEvent> eventArrayList) {
                            for (HabitEvent doc : eventArrayList) {
                                boolean duplicate_flag = false;
                                for (int i = 0;i<eventDataList.size();i++) {
                                    if(eventDataList.get(i).getEventId() == doc.getEventId()) {
                                        duplicate_flag = true;
                                    }
                                }
                                if(duplicate_flag) {
                                    continue;
                                }
                                if(!(doc.getHabit()=="")) {
                                    eventDataList.add(doc);
                                }

                            }
                            eventAdapter.notifyDataSetChanged(); // Notifying the adapter to render any new data fetched
                            //from the cloud
                        }

                        @Override
                        public void onCallbackFailed() {

                        }
                });
    }
}