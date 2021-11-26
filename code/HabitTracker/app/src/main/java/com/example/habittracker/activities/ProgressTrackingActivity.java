/**
 * ProgressTrackingActivity.java
 * This is used for the progress and tracking screen which shows the user how they have done on there habits.
 */
package com.example.habittracker.activities;

import androidx.appcompat.app.AppCompatActivity;

import android.graphics.Color;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.Button;
import android.widget.ProgressBar;
import android.widget.TextView;

import com.example.habittracker.DatabaseManager;
import com.example.habittracker.Habit;
import com.example.habittracker.HabitEvent;
import com.example.habittracker.R;
import com.example.habittracker.User;
import com.example.habittracker.activities.tracking.GraphUtil;
import com.example.habittracker.activities.tracking.ProgressUtil;
import com.example.habittracker.utils.HabitEventListCallback;
import com.example.habittracker.utils.HabitListCallback;
import com.example.habittracker.utils.SharedInfo;
import com.jjoe64.graphview.GraphView;

import java.util.ArrayList;
import java.util.Calendar;
import java.util.Date;
import java.util.HashMap;
import java.util.concurrent.TimeUnit;

/**
 * This class is used in displaying progress of a habit to the user.
 */
public class ProgressTrackingActivity extends AppCompatActivity {

    private Habit habit;
    private ArrayList<HabitEvent> eventList;
    private DatabaseManager db = DatabaseManager.get();

    /**
     * Returns the current database object
     * @return
     */
    public DatabaseManager getDb() {
        return db;
    }

    /**
     * Return the current habit.
     * @return
     */
    public Habit getHabit() {
        return habit;
    }

    /**
     * Set the habit of the class
     * @param habit
     */
    public void setHabit(Habit habit) {
        this.habit = habit;
    }

    /**
     * Returns the current habit event list
     * @return
     */
    public ArrayList<HabitEvent> getEventList() {
        return eventList;
    }

    /**
     * Sets the current habit event list
     * @param eventList
     */
    public void setEventList(ArrayList<HabitEvent> eventList) {
        this.eventList = eventList;
    }

    /**
     * Ran on creation of the activity, handles initial setup.
     * @param savedInstanceState {Bundle}
     */
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_progress_tracking);

        Button back_button = findViewById(R.id.tracking_back_button);

        // button listener for closing the current activity
        // param: View
        back_button.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                finish();
            }
        });

        // Bundle for intent's extra arguments
        Bundle b = this.getIntent().getExtras();
        Habit value = null;
        if(b != null) {
            value = (Habit) b.getSerializable("habit");
        }
        if(value == null) {
            Log.d("Error Progress", "No habit given to progress activity.");
            //close activity
            finish();
        }
        //set habit
        setHabit(value);

        //Code idea from https://stackoverflow.com/questions/50650224/wait-until-firestore-data-is-retrieved-to-launch-an-activity
        Log.d("test",getHabit().getUser().getUsername());
        getDb().getAllHabitEvents(getHabit().getUser().getUsername(), getHabit().getTitle(), new HabitEventListCallback() {

            /**
             * Called when success to get habit events
             * @param eventList {ArrayList<HabitEvent>}
             */
            @Override
            public void onCallbackSuccess(ArrayList<HabitEvent> eventList) {
                //Do what you need to do with your list
                Calendar cal = Calendar.getInstance();
                cal.add(Calendar.DATE,-15);
                setEventList(eventList);
                makeGraph(habit);
            }

            /**
             * called when failed to get habit events
             */
            @Override
            public void onCallbackFailed() {
                Log.d("Error","Failed to get habit events");
            }
        });
    }

    /**
     * This makes a graph on the progress screen
     * @param habit
     */
    void makeGraph(Habit habit){
        //get all UI elements
        GraphView graph = (GraphView) findViewById(R.id.progress_graph);
        ProgressBar consistBar = findViewById(R.id.consistency_progress_bar);
        ProgressBar overallBar = findViewById(R.id.overall_progress_bar);
        TextView overallText = findViewById(R.id.overall_progress_value);
        TextView consistText = findViewById(R.id.consistency_progress_bar_value);

        //get the overall score
//        Log.d("Integer",""+eventList.get(0).getStartDate().get(0).getClass().getName());
        HashMap<String,Integer> scorePlusStats = ProgressUtil.getOverallProgress(habit,eventList,1,100);
        //display score and stats
        overallBar.setProgress(scorePlusStats.get("overall"));
        consistBar.setProgress(scorePlusStats.get("consistency"));
        overallText.setText(scorePlusStats.get("overall")+"%");
        consistText.setText(scorePlusStats.get("consistency")+"%");

        //add habit to the graph
        GraphUtil.addHabitToGraph(graph,habit,eventList,1,"You", Color.BLUE,true);
        GraphUtil.setDateAsXAxis(graph,getApplicationContext(),3);
        // add legend, label axis
        GraphUtil.addSimpleLegend(graph,Color.TRANSPARENT);
        GraphUtil.setLabelAxis(graph,"#Done","Date");
    }
}