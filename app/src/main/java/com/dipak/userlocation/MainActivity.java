package com.dipak.userlocation;

import android.Manifest;
import android.content.pm.PackageManager;
import android.location.Location;
import android.os.Bundle;
import android.os.Looper;
import android.util.Log;
import android.widget.Button;

import androidx.appcompat.app.AppCompatActivity;
import androidx.core.app.ActivityCompat;

import com.google.android.gms.location.FusedLocationProviderClient;
import com.google.android.gms.location.LocationCallback;
import com.google.android.gms.location.LocationRequest;
import com.google.android.gms.location.LocationResult;
import com.google.android.gms.location.LocationServices;

public class MainActivity extends AppCompatActivity {

    // TAG
    private static final String TAG = "MainActivity";

    // UI Parts
    private Button btnStartTracking;
    private Button btnStopTracking;

    // Internal functionality
    private int REQUEST_LOCATION_PERMISSION = 100;
    private String[] locationPermission = {Manifest.permission.ACCESS_FINE_LOCATION};

    // Location classes
    private boolean isTracking;
    private FusedLocationProviderClient fusedLocationProviderClient;
    private LocationCallback locationCallback;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        btnStartTracking = findViewById(R.id.start_track);
        btnStopTracking = findViewById(R.id.stop_tracking);

        btnStartTracking.setOnClickListener(v -> {
            startTracking();
        });

        btnStopTracking.setOnClickListener(v -> {
            stopTracking();
        });

        fusedLocationProviderClient = LocationServices.getFusedLocationProviderClient(this);

        locationCallback = new LocationCallback() {
            @Override
            public void onLocationResult(LocationResult locationResult) {
                Location lastLocation = locationResult.getLastLocation();
                Log.d(TAG, "onLocationResult: lat " + lastLocation.getLatitude());
                Log.d(TAG, "onLocationResult: lng " + lastLocation.getLongitude());
                Log.d(TAG, "onLocationResult: time " + System.currentTimeMillis());
            }
        };
    }

    private void startTracking() {
        if (ActivityCompat.checkSelfPermission(this, Manifest.permission.ACCESS_FINE_LOCATION) != PackageManager.PERMISSION_GRANTED) {
            ActivityCompat.requestPermissions(this, locationPermission, REQUEST_LOCATION_PERMISSION);
        } else {
            fusedLocationProviderClient.requestLocationUpdates(getLocationRequest(), locationCallback, Looper.myLooper());
        }
    }

    private void stopTracking() {
        fusedLocationProviderClient.removeLocationUpdates(locationCallback);
    }

    private LocationRequest getLocationRequest() {
        int MAX_INTERVAL = 10000;
        LocationRequest locationRequest = new LocationRequest();
        locationRequest.setInterval(MAX_INTERVAL);
        locationRequest.setFastestInterval(10000);
        locationRequest.setPriority(LocationRequest.PRIORITY_HIGH_ACCURACY);
        return locationRequest;
    }
}