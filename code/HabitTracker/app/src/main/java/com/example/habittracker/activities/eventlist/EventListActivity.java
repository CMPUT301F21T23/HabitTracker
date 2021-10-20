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
import com.example.habittracker.activities.util.AddEventFragment;
import com.example.habittracker.activities.util.OnFragmentInteractionListener;
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
    private ListView medList;
    private ArrayAdapter<Medicine> medAdapter;
    private ArrayList<Medicine> medDataList;
    private Medicine delete_city=null;
    private Boolean flag = false;
    private TextView sumText;

    /**
     * Override the OnCreate method. Set up a list of Medicine objects and display them
     * in a ListView.
     * @param savedInstanceState
     */
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_event_list);

        NavBarManager nav = new NavBarManager(this,findViewById(R.id.bottom_navigation));
        medList = findViewById(R.id.med_list);
        /* initialize medicine list. */
        String []meds={"Riding","Eat a sandwich","Sleep"};
        Double []doses={1.0,1.0,1.0};
        String []dates={"2020-01-01","2020-01-01","2020-01-01"};
        String []units={"mg","mg","mg"};
        int []freqs={1,1,1};
        medDataList = new ArrayList<>();

        for(int i = 0; i < meds.length;i++) {
            medDataList.add((new Medicine(dates[i],meds[i],doses[i],units[i],freqs[i])));
        }

        medAdapter = new CustomList(this,medDataList);
        medList.setAdapter(medAdapter);

        /* initialize total daily frequency count. */
        sumText = findViewById(R.id.sum);
        BigDecimal doseSum = BigDecimal.valueOf(0);
        for(int i = 0; i < meds.length;i++){
            String tempUnit = medDataList.get(i).getDoseUnit();
            doseSum = doseSum.add(BigDecimal.valueOf(medDataList.get(i).getFrequency()));
        }
        sumText.setText("Total number of doses: " + String.valueOf(doseSum) + " mg.");

        /* Add floating button fragment. */
        final FloatingActionButton addMedButton = findViewById(R.id.add_med_button);
        addMedButton.setOnClickListener(new View.OnClickListener() {
            /**
             * Override the onClick method for the "+" button located bottom end of the screen.
             * @param v
             */
            @Override
            public void onClick(View v) {
                new AddEventFragment().show(getSupportFragmentManager(),"ADD_MED");
            }
        });

        medList.setOnItemClickListener(new AdapterView.OnItemClickListener() {
            /**
             * Override the onItemClick method. Once an item on the list is clicked,
             * we pass the selected Medicine object to the floating fragment.
             * @param adapter
             * @param v
             * @param position
             * @param arg3
             */
            public void onItemClick(AdapterView<?> adapter, View v, int position,
                                    long arg3) {
                Medicine editC = (Medicine) adapter.getItemAtPosition(position);
//                Medicine editC = null;
                new AddEventFragment().newInstance(editC).show(getSupportFragmentManager(),"EDIT_MED");
            }
        });

    }

    /**
     * Override the onOkPressed method of OnFragmentInteractionListener. This method is
     * used for 2 functions: add new medicine and edit existed medicine. If a medicine object
     * is passed to the fragment then the method would edit the passed medicine, otherwise the
     * method would add new medicine to the list.
     * @param newMed    The new Medicine object added to the list
     * @param editFlag if we edited an item in the list then it shall be set to true
     */
    @Override
    public void onOkPressed(Medicine newMed, boolean editFlag){
        if(!editFlag) {
            medAdapter.add(newMed);
            onResume();
        }
        else{
            medAdapter.notifyDataSetChanged();
            onResume();
        }
    }

    /**
     * Override the onDeletePressed method of OnFragmentInteractionListener.
     * The method could delete the selected object from the adapter.
     * @param newMed The object user wants to delete.
     */
    @Override
    public void onDeletePressed(Medicine newMed) {
        medAdapter.remove(newMed);
        medAdapter.notifyDataSetChanged();
        onResume();
    }
    /**
     * Override the onResume method of OnFragmentInteractionListener.
     * Update the sum of frequency in the bottom
     */
    @Override
    public void onResume() {
        super.onResume();
        sumText = findViewById(R.id.sum);
        BigDecimal doseSum = BigDecimal.valueOf(0);
        for(int i = 0; i < medDataList.size();i++){
            String tempUnit = medDataList.get(i).getDoseUnit();
            doseSum = doseSum.add(BigDecimal.valueOf(medDataList.get(i).getFrequency()));
        }
        sumText.setText("Daily doses amount: " + String.valueOf(doseSum));
    }
}