/*
 * AddMedFragment
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
package com.example.habittracker.activities.util;

import android.Manifest;
import android.app.AlertDialog;
import android.app.DatePickerDialog;
import android.app.Dialog;
import android.content.Context;
import android.content.DialogInterface;
import android.content.pm.PackageManager;
import android.location.Address;
import android.location.Geocoder;
import android.location.Location;
import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.Button;
import android.widget.DatePicker;
import android.widget.EditText;
import android.widget.Spinner;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.core.app.ActivityCompat;
import androidx.fragment.app.DialogFragment;


import com.example.habittracker.R;
import com.example.habittracker.activities.eventlist.Medicine;
import com.google.android.gms.location.FusedLocationProviderClient;
import com.google.android.gms.location.LocationServices;
import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.Task;

import java.io.IOException;
import java.text.SimpleDateFormat;
import java.util.Calendar;
import java.util.List;
import java.util.Locale;

/**
 * This class specified the behavior of the AddMedFragment fragment.
 * It is a subclass of DialogFragment.
 * Some of the codes are from CMPUT 301 Lab 3 instructions.
 * @author Yongquan Zhang
 */
public class AddEventFragment extends DialogFragment {

    private EditText medName;
    private EditText dose;
    private EditText date;
    private Spinner s;
    private int spinnerIdx;
    private String unit;
    private EditText freq;
    private OnFragmentInteractionListener listener;
    private boolean editFlag = false;
    FusedLocationProviderClient mFusedLocationClient;

    /**
     * Override the onAttach method of DialogFragment.
     * @param context
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

    /**
     * Override the onCreateDialog method of DialogFragment. Set up a list of EditText for medicine
     * information and a spinner for doseUnit.
     * @param savedInstanceState
     * @return a AlertDialog object
     */
    @Nullable
    @Override
    public Dialog onCreateDialog(@Nullable Bundle savedInstanceState) {
        View view = LayoutInflater.from(getActivity()).inflate(R.layout.fragment_add_event, null);
//        medName = view.findViewById(R.id.med_name_editText);
        dose = view.findViewById(R.id.dose_editText);
//        date = view.findViewById(R.id.date_editText);
//        freq = view.findViewById(R.id.freq_editText);

        mFusedLocationClient = LocationServices.getFusedLocationProviderClient(requireContext());

        Button get_location = view.findViewById(R.id.getPermission);
        get_location.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if (ActivityCompat.checkSelfPermission(requireContext(), Manifest.permission.ACCESS_FINE_LOCATION) == PackageManager.PERMISSION_GRANTED) {
                    getLocation();
                } else {
                    ActivityCompat.requestPermissions(requireActivity(), new String[]{Manifest.permission.ACCESS_FINE_LOCATION}, 123);
//                    requestPermissions(new String[]{Manifest.permission.ACCESS_FINE_LOCATION}, 44);
                }
//                ActivityCompat.requestPermissions(requireActivity(), new String[]{Manifest.permission.ACCESS_FINE_LOCATION}, 1);
            }
        });

        /* Set up the spinner. */
        s = view.findViewById(R.id.spinner1); // The spinner is used for dose unit
        String[] items = new String[]{"event 1", "event 2", "event 3"};
        ArrayAdapter<String> spinnerAdapter = new ArrayAdapter<String>(requireContext(),
                android.R.layout.simple_spinner_dropdown_item, items);
        s.setAdapter(spinnerAdapter);

        s.setOnItemSelectedListener(new AdapterView.OnItemSelectedListener() {
            /**
             * Override the onItemSelected method. Record selected spinner item and index.
             * @param parent
             * @param view
             * @param position
             * @param id
             */
            @Override
            public void onItemSelected(AdapterView<?> parent, View view,
                                       int position, long id) {
                unit = items[position]; // The selected dose unit
                spinnerIdx = position; // The index of selected dose unit
            }

            /**
             * Override the onNothingSelected method (no actual modification).
             * @param parent
             */
            @Override
            public void onNothingSelected(AdapterView<?> parent) {
            }
        });
        /* If we received Medicine object passed from the main activity.
         * If yes, then we display its information.
         * If not, we display empty EditTexts.
         */
        if (getArguments() != null) {
//            Medicine selectedMed = (Medicine) getArguments().getSerializable("med"); // get the Medicine object.
//            date.setText(selectedMed.getDate());
//            medName.setText(selectedMed.getName());
//            dose.setText(String.valueOf(selectedMed.getDose()));
//            /* Display dose unit by using spinner */
//            spinnerIdx = spinnerAdapter.getPosition(selectedMed.getDoseUnit());
//            s.setSelection(spinnerIdx);
//            freq.setText(String.valueOf(selectedMed.getFrequency()));
        }
        /*
         * Set up the DatePickerDialog. Once the user click the EditText widget for Date,
         * the DatePickerDialog should pop up and allow user to select different date.
         */
        final Calendar myCalendar = Calendar.getInstance();
        EditText edittext = (EditText) view.findViewById(R.id.date_editText);

        DatePickerDialog.OnDateSetListener dateD = new DatePickerDialog.OnDateSetListener() {
            @Override
            public void onDateSet(DatePicker view, int year, int monthOfYear,
                                  int dayOfMonth) {
                myCalendar.set(Calendar.YEAR, year);
                myCalendar.set(Calendar.MONTH, monthOfYear);
                myCalendar.set(Calendar.DAY_OF_MONTH, dayOfMonth);
                updateLabel();
            }

            private void updateLabel() {
                String myFormat = "yyyy-MM-dd";
                SimpleDateFormat sdf = new SimpleDateFormat(myFormat, Locale.US);
                edittext.setText(sdf.format(myCalendar.getTime()));
            }
        };

        edittext.setOnClickListener(new View.OnClickListener() {
            /**
             * Override the onClick method. Set up the DatePickerDialog.
             * Once the user click the date EditText, the date picker would
             * pop up.
             * @param v
             */
            @Override
            public void onClick(View v) {
                new DatePickerDialog(requireContext(), dateD, myCalendar
                        .get(Calendar.YEAR), myCalendar.get(Calendar.MONTH),
                        myCalendar.get(Calendar.DAY_OF_MONTH)).show();
            }
        });

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
                            Medicine editM = (Medicine) getArguments().getSerializable("med");
                            editFlag = true;
                            listener.onDeletePressed(editM);
                        }
                    })
                    /* Positive button is for edit operation */
                    .setPositiveButton("Edit", new DialogInterface.OnClickListener() {
                        public void onClick(DialogInterface dialogInterface, int i) {
                            Medicine editM = (Medicine) getArguments().getSerializable("med");
                            /* Prevent empty inputs */
                            if (date.getText().toString().isEmpty() || medName.getText().toString().isEmpty() ||
                                    dose.getText().toString().isEmpty() || freq.getText().toString().isEmpty()) {
                                Toast toast = Toast.makeText(requireContext(), "Some fields are empty.", Toast.LENGTH_LONG);
                                toast.show();
                            }
                            /* Prevent inputs that are not numbers (double or int)*/
                            else if (!isDouble(dose.getText().toString()) || !isInt(freq.getText().toString())) {
                                Toast toast = Toast.makeText(requireContext(), "Please enter NUMBER for Dose Amount(a float) and Daily Frequency(an integer).", Toast.LENGTH_LONG);
                                toast.show();
                            } else if (Double.parseDouble(dose.getText().toString()) <= 0 || Integer.parseInt(freq.getText().toString()) <= 0) {
                                Toast toast = Toast.makeText(requireContext(), "Please enter positive number for Dose Amount and Daily Frequency.", Toast.LENGTH_LONG);
                                toast.show();
                            } else if (medName.getText().toString().length() > 40) {
                                Toast toast = Toast.makeText(requireContext(), "Medicine name needs to be less than 40 characters.", Toast.LENGTH_LONG);
                                toast.show();
                            } else {
                                editM.setDate(date.getText().toString());
                                editM.setName(medName.getText().toString());
                                editM.setDose(Double.parseDouble(dose.getText().toString()));
                                editM.setDoseUnit(unit);
                                editM.setFreq(Integer.parseInt(freq.getText().toString()));
                                editFlag = true;
                                listener.onOkPressed(editM, editFlag);
                            }
                        }
                    }).create();
        }
        /* No Medicine object is received, start add operation */
        else {
            return builder
                    .setView(view)
                    .setTitle("Add Event")
                    .setNegativeButton("Cancel", null)
                    .setPositiveButton("Add", new DialogInterface.OnClickListener() {
                        public void onClick(DialogInterface dialogInterface, int i) {
                            if (date.getText().toString().isEmpty() || medName.getText().toString().isEmpty() ||
                                    dose.getText().toString().isEmpty() || freq.getText().toString().isEmpty()) {
                                Toast toast = Toast.makeText(requireContext(), "Some fields are empty.", Toast.LENGTH_LONG);
                                toast.show();
                            } else if (!isDouble(dose.getText().toString()) || !isInt(freq.getText().toString())) {
                                Toast toast = Toast.makeText(requireContext(), "Please enter numbers for Dose Amount and Daily Frequency.", Toast.LENGTH_LONG);
                                toast.show();
                            } else if (Double.parseDouble(dose.getText().toString()) <= 0 || Integer.parseInt(freq.getText().toString()) <= 0) {
                                Toast toast = Toast.makeText(requireContext(), "Please enter positive number for Dose Amount and Daily Frequency.", Toast.LENGTH_LONG);
                                toast.show();
                            } else if (medName.getText().toString().length() > 40) {
                                Toast toast = Toast.makeText(requireContext(), "Medicine name needs to be less than 40 characters.", Toast.LENGTH_LONG);
                                toast.show();
                            } else {
                                String med = medName.getText().toString();
                                Double doseNum = Double.parseDouble(dose.getText().toString());
                                String tempDate = date.getText().toString();
                                String tempUnit = unit;
                                int tempFreq = Integer.parseInt(freq.getText().toString());
                                listener.onOkPressed(new Medicine(tempDate, med, doseNum, tempUnit, tempFreq), editFlag);
                            }
                        }
                    }).create();
        }
    }

    /**
     * This method allows to pass Medicine object between MainActivity
     * and AddMedFragment.
     * @param med
     * @return fragment
     */
    public static AddEventFragment newInstance(Medicine med) {
        Bundle args = new Bundle();
        args.putSerializable("med", med);

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

    private void getLocation() {
        if (ActivityCompat.checkSelfPermission(requireContext(), Manifest.permission.ACCESS_FINE_LOCATION) != PackageManager.PERMISSION_GRANTED && ActivityCompat.checkSelfPermission(requireContext(), Manifest.permission.ACCESS_COARSE_LOCATION) != PackageManager.PERMISSION_GRANTED) {
            // TODO: Consider calling
            //    ActivityCompat#requestPermissions
            // here to request the missing permissions, and then overriding
            //   public void onRequestPermissionsResult(int requestCode, String[] permissions,
            //                                          int[] grantResults)
            // to handle the case where the user grants the permission. See the documentation
            // for ActivityCompat#requestPermissions for more details.
            return;
        }
        mFusedLocationClient.getLastLocation().addOnCompleteListener(new OnCompleteListener<Location>() {
            @Override
            public void onComplete(@NonNull Task<Location> task) {
                Location location = task.getResult();
                if (location != null) {
                    try {
                        Geocoder geocoder = new Geocoder(requireContext(),
                                Locale.getDefault());
                        List<Address> addresses = geocoder.getFromLocation(location.getLatitude(), location.getLongitude(), 1);
                    } catch (IOException e) {
                        e.printStackTrace();
                    }
                }
            }
        });
    }
}
