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

import android.view.Menu;
import android.view.MenuInflater;
import android.view.MenuItem;
import android.widget.Toast;

import com.example.habittracker.activities.eventlist.LocationActivity;
import com.google.android.gms.maps.CameraUpdateFactory;
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

/**
 * Maps Activity helps get user's current location
 */
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
     * request permissions from user
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
     * Checks if location permissions are granted
     * and if so it enables the location data layer.
     * @param requestCode
     * @param permissions
     * @param grantResults
     */
    @Override
    public void onRequestPermissionsResult(int requestCode,
                                           @NonNull String[] permissions,
                                           @NonNull int[] grantResults) {

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
     * gets location on long click on map
     * @param map
     */
    private void setMapLongClick(final GoogleMap map) {

        map.setOnMapLongClickListener(new GoogleMap.OnMapLongClickListener() {
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

                Toast.makeText(MapsActivity.this, myAddress, Toast.LENGTH_LONG).show();
                myLatitude = latLng.latitude;
                myLongitude = latLng.longitude;
                map.addMarker(new MarkerOptions().position(latLng)
                        .title(myAddress));
            }
        });
    }

    /**
     * sets markers at points of interest
     * @param map
     */
    private void setPoiClick(final GoogleMap map) {
        map.setOnPoiClickListener(new GoogleMap.OnPoiClickListener() {
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
     * overrides the OnCreate method
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

    /**
     * map is ready to be used
     * helps set default location,zoom, set markers
     * If Google Play services is not installed on the device, the user will be prompted to install
     * it inside the SupportMapFragment. This method will only be triggered once the user has
     * installed Google Play services and returned to the app.
     * @param googleMap
     */
    @Override
    public void onMapReady(GoogleMap googleMap) {
        mMap = googleMap;
        mMap.setMapType(GoogleMap.MAP_TYPE_NORMAL);
        float zoom = 100;
        setMapLongClick(mMap);
        // Add a marker in ETLC and move the camera
        LatLng home = new LatLng(53.5271, -113.5289);
        mMap.addMarker(new MarkerOptions().position(home).title("Marker in ETLC"));
        mMap.moveCamera(CameraUpdateFactory.newLatLngZoom(home, zoom));
        mMap.moveCamera(CameraUpdateFactory.newLatLng(home));

        setPoiClick(googleMap);
        enableMyLocation();
    }

    /**
     * switches back to Location Activity with captured location
     */
    public void onBackPressed() {
        super.onBackPressed();
        Intent myIntent = new Intent(MapsActivity.this, LocationActivity.class);

        if (myLatitude == 0.0 || myLongitude == 0.0 || myAddress.equals("No identified address")) {
            Toast.makeText(this, "Please choose different location", Toast.LENGTH_LONG).show();
        }

        myIntent.putExtra("map_latitude", myLatitude.toString());
        myIntent.putExtra("map_longitude", myLongitude.toString());
        myIntent.putExtra("map_address", myAddress);
        startActivity(myIntent);
    }
}