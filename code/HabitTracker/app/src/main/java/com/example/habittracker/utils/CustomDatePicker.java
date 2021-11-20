package com.example.habittracker.utils;

import android.app.Activity;
import android.app.DatePickerDialog;
import android.content.Context;
import android.view.View;
import android.widget.EditText;

import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.Calendar;
import java.util.Date;
import java.util.Locale;

/**
 * This is a custom date picker class.
 */
public class CustomDatePicker implements DatePickerDialog.OnDateSetListener {
    final Calendar myCalendar;
    private EditText edittext;
    private Activity activity;
    private Context context;
    private View view;

    /**
     * Constructor of the customDatePicker class
     * @param context
     * @param view
     * @param et_id
     */
    public CustomDatePicker(Context context, View view, int et_id) {
        this.context = context;
        this.edittext = (EditText) view.findViewById(et_id);
        myCalendar = Calendar.getInstance();
        DatePickerDialog.OnDateSetListener theDP = this;
//        updateLabel(new Date());

        edittext.setOnClickListener(new View.OnClickListener() {
            /**
             * Override the onClick method. Set up the DatePickerDialog.
             * Once the user click the date EditText, the date picker would
             * pop up.
             * @param v
             */
            @Override
            public void onClick(View v) {
                new DatePickerDialog(context, theDP, myCalendar
                        .get(Calendar.YEAR), myCalendar.get(Calendar.MONTH),
                        myCalendar.get(Calendar.DAY_OF_MONTH)).show();
            }
        });
    }

    public CustomDatePicker(Activity act, View view, int et_id) {
        this.activity = act;
        this.edittext = (EditText) view.findViewById(et_id);
        myCalendar = Calendar.getInstance();
        DatePickerDialog.OnDateSetListener theDP = this;
//        updateLabel(new Date());

        edittext.setOnClickListener(new View.OnClickListener() {
            /**
             * Override the onClick method. Set up the DatePickerDialog.
             * Once the user click the date EditText, the date picker would
             * pop up.
             * @param v
             */
            @Override
            public void onClick(View v) {
                new DatePickerDialog(act, theDP, myCalendar
                        .get(Calendar.YEAR), myCalendar.get(Calendar.MONTH),
                        myCalendar.get(Calendar.DAY_OF_MONTH)).show();
            }
        });
    }

    /**
     * override onDateSet function to set year,month,day for the calendar object.
     * @param view
     * @param year
     * @param month
     * @param dayOfMonth
     */
    @Override
    public void onDateSet(android.widget.DatePicker view, int year, int month, int dayOfMonth) {
        myCalendar.set(Calendar.YEAR, year);
        myCalendar.set(Calendar.MONTH, month);
        myCalendar.set(Calendar.DAY_OF_MONTH, dayOfMonth);
//        updateLabel();
        updateLabel(myCalendar.getTime());
    }

    /**
     * Returns the Date the datePicker was set to.
     * @return setDate, the Date
     *         null,    if no date was selected
     */
    public Date getSetDate() {
        String myFormat = "yyyy-MM-dd";
        SimpleDateFormat sdf = new SimpleDateFormat(myFormat, Locale.US);

        Date setDate = null;
        try {
            setDate = sdf.parse(edittext.getText().toString());
        } catch (ParseException e) {
            e.printStackTrace();
        }
        return ( setDate );
    }

    private void updateLabel(Date dateLabel) {
        String myFormat = "yyyy-MM-dd";
        SimpleDateFormat sdf = new SimpleDateFormat(myFormat, Locale.US);
        edittext.setText(sdf.format(dateLabel));
    }

    /**
     * get the calendar object
     * @return Calendar
     */
    public Calendar getDate() {
        return myCalendar;
    }
}
