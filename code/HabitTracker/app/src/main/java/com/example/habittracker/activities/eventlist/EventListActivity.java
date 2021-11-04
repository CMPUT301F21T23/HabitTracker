package com.example.habittracker.activities.eventlist;


import androidx.appcompat.app.AppCompatActivity;

import android.os.Bundle;
import android.view.View;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.ListView;
import android.widget.TextView;

import com.example.habittracker.NavBarManager;
import com.example.habittracker.R;
import com.example.habittracker.activities.fragments.AddEventFragment;
import com.example.habittracker.activities.fragments.OnFragmentInteractionListener;
import com.google.android.material.floatingactionbutton.FloatingActionButton;

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
    private ArrayAdapter<String> listAdapter;
    private ArrayList<String> habitEvents;
    private String delete_city=null;
    private Boolean flag = false;
    private TextView sumText;

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
        habitEvents = new ArrayList<>();

        habitEvents.add("Test");

        listAdapter = new CustomList(this, habitEvents);
        eventList.setAdapter(listAdapter);

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
                new AddEventFragment().newInstance().show(getSupportFragmentManager(),"EDIT_EVENT");
            }
        });

    }

    /**
     * Override the onOkPressed method of OnFragmentInteractionListener. This method is
     * used for 2 functions: add new event and edit existed event. If a event object
     * is passed to the fragment then the method would edit the passed event, otherwise the
     * method would add new event to the list.
     */
    @Override
    public void onOkPressed(){
        onResume();
    }

    /**
     * Override the onDeletePressed method of OnFragmentInteractionListener.
     * The method could delete the selected object from the adapter.
     */
    @Override
    public void onDeletePressed() {
        onResume();
    }

    /**
     * Override the onResume method of OnFragmentInteractionListener
     */
    @Override
    public void onResume() {
        super.onResume();
    }
}