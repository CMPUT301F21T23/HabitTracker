package com.example.habittracker.activities.fragments;

import android.app.AlertDialog;
import android.app.Dialog;
import android.content.Context;
import android.content.DialogInterface;
import android.os.Bundle;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.fragment.app.DialogFragment;

import android.view.LayoutInflater;
import android.view.View;
import android.widget.Button;
import android.widget.CheckBox;
import android.widget.EditText;
import android.widget.Switch;

import com.example.habittracker.Habit;
import com.example.habittracker.R;
import com.example.habittracker.utils.CustomDatePicker;
import com.example.habittracker.utils.SharedInfo;

import java.util.ArrayList;

/**
 * HabitInputFragment prompts the user to enter details about a Habit.
 */
public class HabitInputFragment extends DialogFragment {

    private EditText inputTitle;
    private EditText inputReason;
    HabitInputDialogListener listener;

    /**
     * This interface, HabitInputDialogListener,
     * should be implemented by anybody trying to listen into a habit input fragment
     */
    public interface HabitInputDialogListener {
        void onOkPressed(Habit habit, String prevTitle);
    }

    /**
     * Performs an action (initi a listener) on attach
     * @param context {@code Context} the pplication context
     */
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

    /**
     * Created dialog options in popup dialog
     * @param savedInstanceState {@code Bundle} the bundle containing information
     * @return alertDialog
     */
    @NonNull
    @Override
    public Dialog onCreateDialog(@Nullable Bundle savedInstanceState) {
        // get the fragment view
        View view = LayoutInflater.from(getActivity()).inflate(R.layout.fragment_habit_input, null);
        inputTitle = view.findViewById(R.id.title);
        inputReason = view.findViewById(R.id.reason);
        Switch inputShare = view.findViewById(R.id.shared);

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

            /**
             * Displays user interface to enter all fields to make a Habit.
             * @param dialogInterface {@code DialogInterface} the dialog interface
             */
            @Override
            public void onShow(DialogInterface dialogInterface) {
                Button button = alertDialog.getButton(AlertDialog.BUTTON_POSITIVE);
                button.setOnClickListener(new View.OnClickListener() {

                    /**
                     * Returns an arraylist containing the days of the week that have been checked
                     * @return the arraylist
                     */
                    private ArrayList <String> getWeekDaysChecked() {
                        CheckBox mondayCheck = view.findViewById(R.id.monday);
                        CheckBox tuesdayCheck = view.findViewById(R.id.tuesday);
                        CheckBox wednesdayCheck = view.findViewById(R.id.wednesday);
                        CheckBox thursdayCheck = view.findViewById(R.id.thursday);
                        CheckBox fridayCheck = view.findViewById(R.id.friday);
                        CheckBox saturdayCheck = view.findViewById(R.id.saturday);
                        CheckBox sundayCheck = view.findViewById(R.id.sunday);

                        ArrayList <String> weekDays = new ArrayList<>();
                        CheckBox [] boxes = {
                                mondayCheck,
                                tuesdayCheck,
                                wednesdayCheck,
                                thursdayCheck,
                                fridayCheck,
                                saturdayCheck,
                                sundayCheck
                        };

                        for (CheckBox box : boxes) {
                            if (box.isChecked()) {
                                weekDays.add(box.getText().toString());
                            }
                        }
                        return (weekDays);
                    }

                    /**
                     * Action performed when the habit is done modifying.
                     * @param view
                     */
                    @Override
                    public void onClick(View view) {
                        String title = inputTitle.getText().toString();
                        String reason = inputReason.getText().toString();
                        boolean isPublic = inputShare.isChecked();

                        ArrayList<String> weekDays = getWeekDaysChecked();

                        Bundle bundle = getArguments();
                        String oldTitle = null;
                        // editing case
                        if (bundle!= null) {
                            oldTitle = bundle.getString("old_habit_title");
                        }
                        Habit habit = new Habit(title, reason, datePicker.getSetDate(), weekDays, isPublic, SharedInfo.getInstance().getCurrentUser());
                        // todo: will have to make UI to make it public/private

                        listener.onOkPressed(habit, oldTitle);
                        alertDialog.dismiss();
                    }
                });
            }
        });
        return alertDialog;
    }
}
