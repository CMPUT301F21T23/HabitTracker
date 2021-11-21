package com.example.habittracker.activities.eventlist;

import static com.example.habittracker.utils.DateConverter.arrayListToString;
import static com.example.habittracker.utils.DateConverter.stringToArraylist;

import static java.security.AccessController.getContext;

import android.content.Intent;
import android.os.Bundle;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.TextView;

import androidx.appcompat.app.AppCompatActivity;

import com.example.habittracker.Habit;
import com.example.habittracker.HabitEvent;
import com.example.habittracker.R;
import com.example.habittracker.activities.ListActivity;
import com.example.habittracker.activities.fragments.OnFragmentInteractionListener;
import com.example.habittracker.utils.CustomDatePicker;
import com.google.android.gms.location.LocationServices;
import com.example.habittracker.activities.eventlist.EventListActivity;

import java.io.Serializable;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Calendar;
import java.util.Locale;

public class EventDetailActivity extends AppCompatActivity {
    private HabitEvent event;
    private Habit habit;
    OnFragmentInteractionListener listener;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_event_detail);
        EditText habit_title = findViewById(R.id.habitTitle);
        EditText editText1 = findViewById(R.id.comment_body);
        EditText date = findViewById(R.id.date_editText);
        EditText location1 = findViewById(R.id.address);
        ImageView event_image = (ImageView) findViewById(R.id.img_f);
        Button addImage = findViewById(R.id.addPhoto);
        Button addLocation = findViewById(R.id.getPermission);
        Button edit_bt = findViewById(R.id.edit);
        Button delete_bt = findViewById(R.id.delete);
        Button cancel_bt = findViewById(R.id.cancel);
        Button confirm_bt = findViewById(R.id.confirm);
        Button add_bt = findViewById(R.id.add);
        Button back_bt = findViewById(R.id.back);



        Intent intent = getIntent();
        event = (HabitEvent) intent.getSerializableExtra("event");
        habit = (Habit) intent.getSerializableExtra("habit");

        if(event != null) {
            ArrayList<Integer> temp_date = event.getStartDate();

            date.setText(arrayListToString(temp_date));
            Log.d("date", date.getText().toString());
            editText1.setText(String.valueOf(event.getComment()));
            habit_title.setText(event.getHabit());
            location1.setText(event.getLocation());
            event_image.setImageResource(R.drawable.riding);
            addImage.setVisibility(View.INVISIBLE);
            addLocation.setVisibility(View.INVISIBLE);
            cancel_bt.setVisibility(View.INVISIBLE);
            confirm_bt.setVisibility(View.INVISIBLE);
            add_bt.setVisibility(View.INVISIBLE);
            habit_title.setEnabled(false);
            editText1.setEnabled(false);
            date.setEnabled(false);
            location1.setEnabled(false);
        }
        else {
            cancel_bt.setVisibility(View.INVISIBLE);
            confirm_bt.setVisibility(View.INVISIBLE);
            edit_bt.setVisibility(View.INVISIBLE);
            delete_bt.setVisibility(View.INVISIBLE);

        }
        CustomDatePicker dp = new CustomDatePicker(EventDetailActivity.this, findViewById(android.R.id.content).getRootView(), R.id.date_editText);

        delete_bt.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                event.deleteDB();
                Intent temp_intent = new Intent(getApplicationContext(), EventListActivity.class);
                Bundle bundle = new Bundle();
                bundle.putSerializable("habit", (Serializable) habit);
                temp_intent.putExtras(bundle);
                startActivity(temp_intent);
                finish();
            }
        });

        edit_bt.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                addImage.setVisibility(View.VISIBLE);
                addLocation.setVisibility(View.VISIBLE);
                editText1.setEnabled(true);
                date.setEnabled(true);
                location1.setEnabled(true);
                cancel_bt.setVisibility(View.VISIBLE);
                confirm_bt.setVisibility(View.VISIBLE);
                edit_bt.setVisibility(View.INVISIBLE);
                delete_bt.setVisibility(View.INVISIBLE);

                cancel_bt.setOnClickListener(new View.OnClickListener() {
                    @Override
                    public void onClick(View v) {
                        addImage.setVisibility(View.INVISIBLE);
                        addLocation.setVisibility(View.INVISIBLE);
                        editText1.setEnabled(false);
                        date.setEnabled(false);
                        location1.setEnabled(false);
                        cancel_bt.setVisibility(View.INVISIBLE);
                        confirm_bt.setVisibility(View.INVISIBLE);
                        edit_bt.setVisibility(View.VISIBLE);
                        delete_bt.setVisibility(View.VISIBLE);
                    }
                });
                confirm_bt.setOnClickListener(new View.OnClickListener() {
                    @Override
                    public void onClick(View v) {

                        event.setStartDate(stringToArraylist(date.getText().toString()));
                        event.setLocation(location1.getText().toString());
                        event.setComment(editText1.getText().toString());
                        event.editDB();
                        addImage.setVisibility(View.INVISIBLE);
                        addLocation.setVisibility(View.INVISIBLE);
                        editText1.setEnabled(false);
                        date.setEnabled(false);
                        location1.setEnabled(false);
                        cancel_bt.setVisibility(View.INVISIBLE);
                        confirm_bt.setVisibility(View.INVISIBLE);
                        edit_bt.setVisibility(View.VISIBLE);
                        delete_bt.setVisibility(View.VISIBLE);
                    }
                });
            }
        });

        back_bt.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent intent = new Intent(getApplicationContext(), EventListActivity.class);
                Bundle bundle = new Bundle();
                bundle.putSerializable("habit", (Serializable) habit);
                intent.putExtras(bundle);
                startActivity(intent);
                finish();
            }
        });

        add_bt.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                ArrayList<Integer> tempDate;
                HabitEvent tempE = new HabitEvent();
                tempE.setHabit(habit.getTitle());
                String temp_dateString = date.getText().toString();
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
                tempE.setStartDate(tempDate);
                tempE.setLocation(location1.getText().toString());
                tempE.setComment(editText1.getText().toString());
                tempE.updateDB();

                Intent intent = new Intent(getApplicationContext(), EventListActivity.class);
                Bundle bundle = new Bundle();
                bundle.putSerializable("habit", (Serializable) habit);
                intent.putExtras(bundle);
                startActivity(intent);
                finish();
            }
        });


    }
}
