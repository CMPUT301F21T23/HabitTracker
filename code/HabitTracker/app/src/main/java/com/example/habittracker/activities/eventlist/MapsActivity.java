package com.example.habittracker.activities.eventlist;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;
import androidx.core.app.ActivityCompat;
import androidx.core.content.ContextCompat;

import android.Manifest;
import android.content.Intent;
import android.content.pm.PackageManager;
import android.location.Address;
import android.location.Geocoder;
import android.os.Bundle;
import com.example.habittracker.R;

import android.view.View;
import android.widget.TextView;

import com.google.android.gms.maps.GoogleMap;
import com.google.android.gms.maps.OnMapReadyCallback;
import com.google.android.gms.maps.SupportMapFragment;
import com.google.android.gms.maps.model.LatLng;
import com.google.android.gms.maps.model.Marker;
import com.google.android.gms.maps.model.MarkerOptions;
import com.example.habittracker.databinding.ActivityMapsBinding;
import com.google.android.gms.maps.model.PointOfInterest;

import java.io.IOException;
import java.util.List;
import java.util.Locale;


public class MapsActivity extends AppCompatActivity implements OnMapReadyCallback {
    /**
     * initialize variables
     */
    private static final int REQUEST_LOCATION_PERMISSION = 0;
    private GoogleMap mMap;
    private ActivityMapsBinding binding;
    String myAddress = "No identified address";
    Double myLatitude = 0.0;
    Double myLongitude = 0.0;

    /**
     * ask for permission
     */
    private void enableMyLocation() {
        if (ContextCompat.checkSelfPermission(this,
                Manifest.permission.ACCESS_FINE_LOCATION)
                == PackageManager.PERMISSION_GRANTED) {
            mMap.setMyLocationEnabled(true);
        } else {
            ActivityCompat.requestPermissions(this, new String[]
                            {Manifest.permission.ACCESS_FINE_LOCATION},
                    REQUEST_LOCATION_PERMISSION);
        }
    }

    /**
     * enable location
     * @param requestCode
     * @param permissions
     * @param grantResults
     */
    @Override
    public void onRequestPermissionsResult(int requestCode,
                                           @NonNull String[] permissions,
                                           @NonNull int[] grantResults) {
        // Check if location permissions are granted and if so enable the
        // location data layer.
        super.onRequestPermissionsResult(requestCode, permissions, grantResults);
        switch (requestCode) {
            case REQUEST_LOCATION_PERMISSION:
                if (grantResults.length > 0
                        && grantResults[0]
                        == PackageManager.PERMISSION_GRANTED) {
                    enableMyLocation();
                    break;
                }
        }
    }

    /**
     * Long click to select location
     * @param map
     */
    private void setMapLongClick(final GoogleMap map) {

        map.setOnMapLongClickListener(new GoogleMap.OnMapLongClickListener() {
            /**
             * Override long click function
             * @param latLng
             */
            @Override
            public void onMapLongClick(LatLng latLng) {
                String snippet = String.format(Locale.getDefault(),
                        "Lat: %1$.5f, Long: %2$.5f",
                        latLng.latitude,
                        latLng.longitude);

                Geocoder geocoder;
                List<Address> addresses;
                geocoder = new Geocoder(MapsActivity.this, Locale.getDefault());
                try {
                    addresses = geocoder.getFromLocation(latLng.latitude, latLng.longitude, 1);
                    String fullAddress = addresses.get(0).getAddressLine(0);
                    myAddress = fullAddress;
                } catch (IOException e) {
                    e.printStackTrace();
                }

                //Toast.makeText(MapsActivity.this, myAddress, Toast.LENGTH_LONG).show();
                myLatitude = latLng.latitude;
                myLongitude = latLng.longitude;
                TextView tView = findViewById(R.id.locationDisplay);
                tView.setText(myAddress);
                map.addMarker(new MarkerOptions().position(latLng)
                        .title(myAddress));
            }
        });
    }

    /**
     * set poi click
     * @param map
     */
    private void setPoiClick(final GoogleMap map) {
        map.setOnPoiClickListener(new GoogleMap.OnPoiClickListener() {
            /**
             * Override onPoiClick
             * @param poi
             */
            @Override
            public void onPoiClick(PointOfInterest poi) {
                Marker poiMarker = mMap.addMarker(new MarkerOptions()
                        .position(poi.latLng)
                        .title(poi.name));
                poiMarker.showInfoWindow();
                poiMarker.setTag("poi");
            }

        });
    }

    /**
     * Override onCreate fucntion
     * @param savedInstanceState
     */
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

        binding = ActivityMapsBinding.inflate(getLayoutInflater());
        setContentView(binding.getRoot());

        // Obtain the SupportMapFragment and get notified when the map is ready to be used.
        SupportMapFragment mapFragment = (SupportMapFragment) getSupportFragmentManager()
                .findFragmentById(R.id.map);
        mapFragment.getMapAsync(this);
    }

    @Override
    public void onMapReady(GoogleMap googleMap) {
        mMap = googleMap;
        mMap.setMapType(GoogleMap.MAP_TYPE_NORMAL);
        float zoom = 100;
        setMapLongClick(mMap);

        setPoiClick(googleMap);
        enableMyLocation();
    }

    /**
     * switches back with captured location
     */
    public void onClick(View view) {
        Intent intent = new Intent();
        intent.putExtra("location",myAddress);
        setResult(2,intent);
        finish();
    }

    /**
     * switch back with captured location
     */
    public void onBackPressed(){
        super.onBackPressed();
        Intent intent = new Intent();
        intent.putExtra("location",myAddress);
        setResult(2,intent);

        finish();
    }

}