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
import java.util.ArrayList;

public class EventDetailActivity extends AppCompatActivity {
    private HabitEvent event;
    OnFragmentInteractionListener listener;
    private String location;

    EditText location1 = findViewById(R.id.address);


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_event_detail);
        EditText habit_title = findViewById(R.id.habitTitle);
        EditText editText1 = findViewById(R.id.comment_body);
        EditText date = findViewById(R.id.date_editText);

        ImageView event_image = (ImageView) findViewById(R.id.img_f);
        Button addImage = findViewById(R.id.addPhoto);
        Button addLocation = findViewById(R.id.getPermission);
        Button edit_bt = findViewById(R.id.edit);
        Button delete_bt = findViewById(R.id.delete);
        Button cancel_bt = findViewById(R.id.cancel);
        Button confirm_bt = findViewById(R.id.confirm);




        Intent intent = getIntent();
        event = (HabitEvent) intent.getSerializableExtra("event");

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
            habit_title.setEnabled(false);
            editText1.setEnabled(false);
            date.setEnabled(false);
            location1.setEnabled(false);
        }
        CustomDatePicker dp = new CustomDatePicker(EventDetailActivity.this, findViewById(android.R.id.content).getRootView(), R.id.date_editText);

        delete_bt.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                event.deleteDB();
                Intent temp_intent = new Intent(getApplicationContext(), EventListActivity.class);
                startActivity(temp_intent);
            }
        });

        addLocation.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent intent = new Intent(getApplicationContext(), MapsActivity.class);
                startActivityForResult(intent, 2);
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
}
