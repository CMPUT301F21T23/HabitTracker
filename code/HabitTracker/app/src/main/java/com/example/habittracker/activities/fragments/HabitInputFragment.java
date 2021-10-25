package com.example.habittracker.activities.fragments;

import android.app.AlertDialog;
import android.app.DatePickerDialog;
import android.app.Dialog;
import android.content.Context;
import android.content.DialogInterface;
import android.icu.util.Calendar;
import android.os.Build;
import android.os.Bundle;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.annotation.RequiresApi;
import androidx.fragment.app.DialogFragment;
import androidx.fragment.app.Fragment;

import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.DatePicker;
import android.widget.EditText;

import com.example.habittracker.R;

/**
 * HabitInputFragment prompts the user to enter details about a Habit.
 */
public class HabitInputFragment extends DialogFragment implements DatePickerDialog.OnDateSetListener {

    private EditText inputTitle;
    private EditText inputReason;
    private EditText inputDate;
    HabitInputDialogListener listener;

    // this will be called when a date is selected
    @Override
    public void onDateSet(DatePicker datePicker, int year, int month, int dayOfMonth) {
        Log.i("Year Selected", String.valueOf(year));
        Log.i("Month Selected", String.valueOf(month));
        Log.i("Day Selected", String.valueOf(dayOfMonth));
    }

    public interface HabitInputDialogListener {
        void onOkPressed();
    }

    @Override
    public void onAttach(@NonNull Context context) {
        // this method automatically gets called when a fragment attaches to an activity
        super.onAttach(context);
        if (context instanceof HabitInputDialogListener) {
            listener = (HabitInputDialogListener) context;
        } else {
            throw new RuntimeException(context.toString() + "must implement HabitInputDialogListener");
        }
    }

    @RequiresApi(api = Build.VERSION_CODES.N)
    private void showDatePickerDialog() {
        DatePickerDialog datePickerDialog = new DatePickerDialog(
                getActivity(),
                (DatePickerDialog.OnDateSetListener) getParentFragment(),
                Calendar.getInstance().get(Calendar.YEAR),          // year to show
                Calendar.getInstance().get(Calendar.MONTH),         // month to show
                Calendar.getInstance().get(Calendar.DAY_OF_MONTH)   // day to show
        );
        datePickerDialog.show();
    }

    @NonNull
    @Override
    public Dialog onCreateDialog(@Nullable Bundle savedInstanceState) {
        // get the fragment view
        View view = LayoutInflater.from(getActivity()).inflate(R.layout.fragment_habit_input, null);
        inputTitle = view.findViewById(R.id.title);
        inputReason = view.findViewById(R.id.reason);
        inputDate = view.findViewById(R.id.dateToStart);

        // show the date picker dialog when the user clicks the date to start field
        inputDate.setOnClickListener(new View.OnClickListener() {
            @RequiresApi(api = Build.VERSION_CODES.N)
            @Override
            public void onClick(View view) {
                showDatePickerDialog();
            }
        });

        // build the dialog
        AlertDialog.Builder builder = new AlertDialog.Builder(getContext());
        AlertDialog alertDialog = builder
                .setView(view)
                .setTitle("Add/Edit Habit")
                .setNegativeButton("Cancel", null)
                .setPositiveButton("OK", null)
                .create();

        // process the data entered through the dialog
        alertDialog.setOnShowListener(new DialogInterface.OnShowListener() {
            @Override
            public void onShow(DialogInterface dialogInterface) {
                Button button = alertDialog.getButton(AlertDialog.BUTTON_POSITIVE);
                button.setOnClickListener(new View.OnClickListener() {
                    @Override
                    public void onClick(View view) {
                        String title = inputTitle.getText().toString();
                        String reason = inputReason.getText().toString();
                        listener.onOkPressed();
                        alertDialog.dismiss();
                    }
                });
            }
        });
        return alertDialog;
    }
}
