/*
 * AddEventFragment
 *
 * Version 1.0.0
 *
 * Date: 2021-09-25
 *
 * Copyright (c) 2021 Yongquan Zhang.
 * Permission is hereby granted, free of charge, to any person obtaining a copy
 * of this software and associated documentation files (the "Software"), to deal
 * in the Software without restriction, including without limitation the rights
 * to use, copy, modify, merge, publish, distribute, sublicense, and/or sell copies
 * of the Software, and to permit persons to whom the Software is furnished to do
 * so, subject to the following conditions:
 * The above copyright notice and this permission notice shall be included in all
 * copies or substantial portions of the Software.
 * THE SOFTWARE IS PROVIDED "AS IS", WITHOUT WARRANTY OF ANY KIND, EXPRESS OR IMPLIED,
 * INCLUDING BUT NOT LIMITED TO THE WARRANTIES OF MERCHANTABILITY, FITNESS FOR A PARTICULAR
 * PURPOSE AND NONINFRINGEMENT. IN NO EVENT SHALL THE AUTHORS OR COPYRIGHT HOLDERS
 * BE LIABLE FOR ANY CLAIM, DAMAGES OR OTHER LIABILITY, WHETHER IN AN ACTION OF CONTRACT,
 * TORT OR OTHERWISE, ARISING FROM, OUT OF OR IN CONNECTION WITH THE SOFTWARE OR THE USE
 * OR OTHER DEALINGS IN THE SOFTWARE.
 */
package com.example.habittracker.activities.fragments;

import static com.example.habittracker.utils.DateConverter.arrayListToString;
import static com.example.habittracker.utils.DateConverter.stringToArraylist;
import com.example.habittracker.utils.SharedInfo;

import android.app.AlertDialog;
import android.app.Dialog;
import android.content.Context;
import android.content.DialogInterface;
import android.content.Intent;
import android.os.Build;
import android.os.Bundle;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.Spinner;
import android.widget.TextView;

import androidx.annotation.Nullable;
import androidx.annotation.RequiresApi;
import androidx.fragment.app.DialogFragment;


import com.example.habittracker.DatabaseManager;
import com.example.habittracker.Habit;
import com.example.habittracker.HabitEvent;
import com.example.habittracker.R;
import com.example.habittracker.activities.eventlist.MapsActivity;
import com.example.habittracker.utils.CustomDatePicker;
import com.example.habittracker.utils.HabitListCallback;
import com.google.android.gms.location.FusedLocationProviderClient;
import com.google.android.gms.location.LocationServices;
import com.google.firebase.firestore.CollectionReference;

import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Calendar;
import java.util.Locale;

/**
 * This class specified the behavior of the AddEventFragment fragment.
 * It is a subclass of DialogFragment.
 * Some of the codes are from CMPUT 301 Lab 3 instructions.
 * @author Yongquan Zhang
 */
public class AddEventFragment extends DialogFragment {

    private TextView eventTitle;
    private EditText location1;
    private String location;
    private EditText editText1;
    private EditText date;
    private Spinner s;
    private ImageView event_image;
    private int spinnerIdx;
    private String attachedHabit;
    private OnFragmentInteractionListener listener;
    private boolean editFlag = false;
    FusedLocationProviderClient mFusedLocationClient;



    /**
     * Override the onAttach method of DialogFragment.
     * @param context       {@code Context} required context
     */
    @Override
    public void onAttach(Context context) {
        super.onAttach(context);
        if (context instanceof OnFragmentInteractionListener) {
            listener = (OnFragmentInteractionListener) context;
        } else {
            throw new RuntimeException(context.toString()
                    + "Must implement the listener.");
        }
    }


    @Override
    public void onActivityResult(int requestCode, int resultCode, Intent data){
        super.onActivityResult(requestCode, resultCode, data);
        if (requestCode == 2){
            String message = data.getStringExtra("location");
            location = message;
            location1.setText(location);
        }
    }


    /**
     * Override the onCreateDialog method of DialogFragment. Set up a list of EditText for event
     * information and a spinner for doseUnit.
     * @param savedInstanceState
     * @return a AlertDialog object
     */
    @RequiresApi(api = Build.VERSION_CODES.N)
    @Nullable
    @Override
    public Dialog onCreateDialog(@Nullable Bundle savedInstanceState) {
        View view = LayoutInflater.from(getActivity()).inflate(R.layout.fragment_add_event, null);
        editText1 = view.findViewById(R.id.comment_body);
        date = view.findViewById(R.id.date_editText);
        location1 = view.findViewById(R.id.address);
        event_image = (ImageView) view.findViewById(R.id.img_f);
        mFusedLocationClient = LocationServices.getFusedLocationProviderClient(requireContext());


        Button get_location = view.findViewById(R.id.getPermission);
        get_location.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent intent = new Intent(getActivity(), MapsActivity.class);
                startActivityForResult(intent, 2);
            }



        });



        /* Set up the spinner. */
        s = view.findViewById(R.id.event_spinner); // The spinner is used for dose unit
//        String[] items = new String[]{"Habit 1", "Habit 2", "Habit 3"};
        ArrayList<String> items = new ArrayList<>();
        items.add("Habit 1");
        items.add("Habit 2");
        items.add("Habit 3");
        ArrayAdapter<String> spinnerAdapter = new ArrayAdapter<String>(requireContext(),
                android.R.layout.simple_spinner_dropdown_item, items);
        s.setAdapter(spinnerAdapter);

        s.setOnItemSelectedListener(new AdapterView.OnItemSelectedListener() {
            /**
             * Override the onItemSelected method. Record selected spinner item and index.
             *
             * @param parent
             * @param view
             * @param position
             * @param id
             */
            @Override
            public void onItemSelected(AdapterView<?> parent, View view,
                                       int position, long id) {
                attachedHabit = items.get(position); // The selected dose unit
                spinnerIdx = position; // The index of selected dose unit
            }

            /**
             * Override the onNothingSelected method (no actual modification).
             *
             * @param parent
             */
            @Override
            public void onNothingSelected(AdapterView<?> parent) {
            }
        });


        // set up snapshot listener
        String usersColName = "Users";
        String habitsColName = "Habits";
        String habitEventsColName = "HabitEvents";
        String DB_TAG = "DatabaseManager";

        ArrayList<String> habit_list = new ArrayList<>();

        DatabaseManager dm = DatabaseManager.get();
        CollectionReference colRef;
//        dm.getAllHabits("John_test_user", new HabitListCallback() {
        dm.getAllHabits(SharedInfo.getInstance().getCurrentUser().getUsername(), new HabitListCallback() {
            @Override
            public void onCallbackSuccess(ArrayList<Habit> habitList) {
                items.clear();
                for (int i = 0; i < habitList.size(); i++) {
                    System.out.println(habitList.get(i).getTitle());
                    items.add(habitList.get(i).getTitle());
                    Log.d("Here", items.get(i));
                }
                spinnerAdapter.notifyDataSetChanged();
            }

            @Override
            public void onCallbackFailed() {

            }
        });
        /* If we received HabitEvent object passed from the main activity.
         * If yes, then we display its information.
         * If not, we display empty EditTexts.
         */
        if (getArguments() != null) {
            HabitEvent selectedEvent = (HabitEvent) getArguments().getSerializable("event"); // get the HabitEvent object.
            ArrayList<Integer> temp_date = selectedEvent.getStartDate();

            date.setText(arrayListToString(temp_date));
            Log.d("date", date.getText().toString());
            editText1.setText(String.valueOf(selectedEvent.getComment()));
            spinnerIdx = spinnerAdapter.getPosition(selectedEvent.getHabit());
            s.setSelection(spinnerIdx);
            location1.setText(selectedEvent.getLocation());
            event_image.setImageResource(R.drawable.riding);
        }
        CustomDatePicker dp = new CustomDatePicker(getContext(), view, R.id.date_editText);

        AlertDialog.Builder builder = new AlertDialog.Builder(getContext());
        Bundle b = getArguments();

        /*
         * Check if we received an object from MainActivity or not. If we received the
         * object we should do edit/delete operation. If we did not receive the object
         * we should do add operation.
         */
        if (b != null) {
            return builder
                    .setView(view)
                    .setTitle("Edit event/Delete event")
                    .setNegativeButton("Cancel", null)
                    /* Neutral button is for delete operaion */
                    .setNeutralButton("Delete", new DialogInterface.OnClickListener() {
                        public void onClick(DialogInterface dialogInterface, int i) {
                            HabitEvent editE = (HabitEvent) getArguments().getSerializable("event");
                            editFlag = true;
                            listener.onDeletePressed(editE);
                        }
                    })
                    /* Positive button is for edit operation */
                    .setPositiveButton("Edit", new DialogInterface.OnClickListener() {
                        public void onClick(DialogInterface dialogInterface, int i) {
                            HabitEvent editM = (HabitEvent) getArguments().getSerializable("event");

                            editM.setStartDate(stringToArraylist(date.getText().toString()));
                            editM.setLocation(location1.getText().toString());
                            editM.setHabit(attachedHabit);
                            editM.setComment(editText1.getText().toString());
//                                editM.setCalendar(date_calendar);
                            editFlag = true;

                            listener.onOkPressed(editM, editFlag);
                        }
                    }).create();
        }
        else {
            return builder
                    .setView(view)
                    .setTitle("Add Event")
                    .setNegativeButton("Cancel", null)
                    .setPositiveButton("Add", new DialogInterface.OnClickListener() {
                        public void onClick(DialogInterface dialogInterface, int i) {
//                            String title = eventTitle.getText().toString();
                            String temp_dateString = date.getText().toString();
                            ArrayList<Integer> tempDate;
                            String tempHabit = attachedHabit;
                            String tempLocation = location1.getText().toString();
                            String tempComments = editText1.getText().toString();
                            HabitEvent tempHabitEvent = new HabitEvent();
                            if(temp_dateString.isEmpty()) {
                                String myFormat = "yyyy-MM-dd";
                                SimpleDateFormat sdf = new SimpleDateFormat(myFormat, Locale.US);
                                tempDate = stringToArraylist(sdf.format(Calendar.getInstance().getTime()));
//                                tempHabitEvent.setCalendar(Calendar.getInstance().getTime());
                            }
                            else {
//                                tempHabitEvent.setCalendar(date_calendar);
                                tempDate = stringToArraylist(date.getText().toString());
                            }
                            tempHabitEvent.setLocation(tempLocation);
                            tempHabitEvent.setStartDate(tempDate);
                            tempHabitEvent.setComment(tempComments);
                            tempHabitEvent.setHabit(tempHabit);

                            listener.onOkPressed(tempHabitEvent, editFlag);
                        }
                    }).create();
        }
    }


    /**
     * create a new instance of the selected habit event
     * @param event
     * @return
     */
    public static AddEventFragment newInstance(HabitEvent event) {
        Bundle args = new Bundle();
        args.putSerializable("event", event);
        AddEventFragment fragment = new AddEventFragment();
        fragment.setArguments(args);
        return fragment;
    }

    /**
     * Check is the input string could be parsed into double
     * @param str
     * @return boolean
     */
    private boolean isDouble(String str) {
        try {
            Double.parseDouble(str);
            return true;
        } catch (NumberFormatException e) {
            return false;
        }
    }

    /**
     * Check is the input string could be parsed into integer
     * @param str
     * @return boolean
     */
    private boolean isInt(String str) {
        try {
            Integer.parseInt(str);
            return true;
        } catch (NumberFormatException e) {
            return false;
        }
    }





    /**
     * get location function, should be replaced by Aparna's function in the future.
     */
//    private void getLocation() {
//        mFusedLocationClient.getLastLocation().addOnCompleteListener(new OnCompleteListener<Location>() {
//            @Override
//            public void onComplete(@NonNull Task<Location> task) {
//                Location location = task.getResult();
//                if (location != null) {
//                    try {
//                        Geocoder geocoder = new Geocoder(requireContext(),
//                                Locale.getDefault());
//                        List<Address> addresses = geocoder.getFromLocation(location.getLatitude(), location.getLongitude(), 1);
//                        location1.setText(addresses.get(0).getAddressLine(0).toString());
//                    } catch (IOException e) {
//                        e.printStackTrace();
//                    }
//                }
//            }
//        });
//    }
}
