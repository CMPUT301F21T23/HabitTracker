package com.example.habittracker.activities.eventlist;

import androidx.appcompat.app.AppCompatActivity;
import com.example.habittracker.R;
import com.example.habittracker.activities.HomeActivity;

import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.widget.TextView;
import android.widget.Toast;
import java.sql.Struct;

/**
 * Location activity class helps display user's current location
 */
public class LocationActivity extends AppCompatActivity {
    /**
     * initialoize varaibles
     */
    TextView textView;
    public String latitude = "";
    public String longitude = "";
    public String address = "";

    /**
     * getter for latitude
     * @return String, latitude
     */
    public String getLatitude() {
        return this.latitude;
    }

    /**
     * getter for longitude
     * @return String longitude
     */
    public String getLongitude() {
        return this.longitude;
    }

    /**
     * getter for address
     * @return String address
     */
    public String getAddress() {
        return this.address;
    }

    /**
     * Override the OnCreate method
     * @param savedInstanceState
     */
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_location);

        textView = findViewById(R.id.textView);

        Intent intent = getIntent();
        latitude = intent.getStringExtra("map_latitude");
        longitude = intent.getStringExtra("map_longitude");
        address = intent.getStringExtra("map_address");

        if (latitude == null || longitude == null || address == null) {
            textView.setText("Please choose location");
        }

        textView.setText("Latitude: " + latitude + "\n" +
                "Longitude: " + longitude + "\n" +
                "Address: " + address);

    }

    /**
     * switches to the Maps Activity
     * @param v
     */
    public void onClick(View v) {
        Intent intent = new Intent(this, MapsActivity.class);
        startActivity(intent);
        Toast.makeText(LocationActivity.this, "Long click to choose location", Toast.LENGTH_LONG).show();
    }

    /**
     * Switches back from Location Activity
     */
    public void onBackPressed() {
        super.onBackPressed();
        Intent myIntent = new Intent(LocationActivity.this, HomeActivity.class);
        startActivity(myIntent);
    }
}