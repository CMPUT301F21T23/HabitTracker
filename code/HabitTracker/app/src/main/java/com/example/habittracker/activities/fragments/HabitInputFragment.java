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

import com.example.habittracker.Habit;
import com.example.habittracker.R;
import com.example.habittracker.utils.CustomDatePicker;

import java.util.Locale;

/**
 * HabitInputFragment prompts the user to enter details about a Habit.
 */
public class HabitInputFragment extends DialogFragment {

    private EditText inputTitle;
    private EditText inputReason;
    HabitInputDialogListener listener;

    public interface HabitInputDialogListener {
        void onOkPressed(Habit habit);
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

    @NonNull
    @Override
    public Dialog onCreateDialog(@Nullable Bundle savedInstanceState) {
        // get the fragment view
        View view = LayoutInflater.from(getActivity()).inflate(R.layout.fragment_habit_input, null);
        inputTitle = view.findViewById(R.id.title);
        inputReason = view.findViewById(R.id.reason);

        // adds a responsive DatePicker
        CustomDatePicker datePicker = new CustomDatePicker(getActivity(), view, R.id.dateToStart);

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

                        // TODO: validation here, if bad, freeze the operation.
                        //  Leave for extra if you have time
                        Habit habit = new Habit(title, reason, datePicker.getSetDate(), new String[]{"Mon", "Tues"}, false);
                        // TODO: remember, you have to get the days of the week from the UI, this is just for testing
                        // todo: will have to make UI to make it public/private
                        listener.onOkPressed(habit);
                        alertDialog.dismiss();
                    }
                });
            }
        });
        return alertDialog;
    }
}
