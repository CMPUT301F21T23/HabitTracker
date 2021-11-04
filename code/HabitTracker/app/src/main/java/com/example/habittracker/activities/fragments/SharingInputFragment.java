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
public class SharingInputFragment extends DialogFragment{

    private EditText inputName;
    SharingInputDialogListener listener;


    public interface SharingInputDialogListener {
        void onOkPressed();
    }

    @Override
    public void onAttach(@NonNull Context context) {
        // this method automatically gets called when a fragment attaches to an activity
        super.onAttach(context);
        if (context instanceof SharingInputDialogListener) {
            listener = (SharingInputDialogListener) context;
        } else {
            throw new RuntimeException(context.toString() + "must implement HabitInputDialogListener");
        }
    }

    @NonNull
    @Override
    public Dialog onCreateDialog(@Nullable Bundle savedInstanceState) {
        // get the fragment view
        View view = LayoutInflater.from(getActivity()).inflate(R.layout.fragment_sharing_input, null);
        inputName = view.findViewById(R.id.sharing_input_edit_text);


        // build the dialog
        AlertDialog.Builder builder = new AlertDialog.Builder(getContext());
        AlertDialog alertDialog = builder
                .setView(view)
                .setTitle("")
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
                        String title = inputName.getText().toString();
                        listener.onOkPressed();
                        alertDialog.dismiss();
                    }
                });
            }
        });
        return alertDialog;
    }
}