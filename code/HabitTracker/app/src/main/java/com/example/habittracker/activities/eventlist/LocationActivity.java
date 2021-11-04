package com.example.habittracker.activities.eventlist;

import androidx.appcompat.app.AppCompatActivity;
import com.example.habittracker.R;
import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.widget.TextView;
import android.widget.Toast;



import java.sql.Struct;

public class LocationActivity extends AppCompatActivity {

    TextView textView;
    public String latitude = "";
    public String longitude = "";
    public String address = "";

    public String getLatitude() {
        return this.latitude;
    }

    public String getLongitude() {
        return this.longitude;
    }

    public String getAddress() {
        return this.address;
    }

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
    public void onClick(View v) {
        Intent intent = new Intent(this, MapsActivity.class);
        startActivity(intent);
        Toast.makeText(LocationActivity.this, "Long click to choose location", Toast.LENGTH_LONG).show();
    }
}