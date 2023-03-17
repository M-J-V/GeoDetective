package com.example.geodetective;

import android.annotation.SuppressLint;
import android.app.Activity;
import android.content.Intent;
import android.content.pm.PackageManager;
import android.net.Uri;
import android.provider.Settings;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AlertDialog;
import androidx.core.app.ActivityCompat;

import com.google.android.gms.location.FusedLocationProviderClient;
import com.google.android.gms.location.LocationServices;
import com.google.android.gms.location.Priority;
import com.google.android.gms.tasks.CancellationToken;
import com.google.android.gms.tasks.OnTokenCanceledListener;

public class Location {
    private Activity activity;
    private double latitude;
    private double longitude;

    public Location(double latitude, double longitude, Activity activity) {
        this.latitude = latitude;
        this.longitude = longitude;
        this.activity = activity;
    }

    public Location (double latitude, double longitude) {
        this.latitude = latitude;
        this.longitude = longitude;
        this.activity = null;
    }

    public Location(Activity activity) {
        this.latitude = 0;
        this.longitude = 0;
        this.activity = activity;
    }

    public double getLatitude() {
        return latitude;
    }

    public void setLatitude(double latitude) {
        this.latitude = latitude;
    }

    public double getLongitude() {
        return longitude;
    }

    public void setLongitude(double longitude) {
        this.longitude = longitude;
    }

    public Activity getActivity() {
        return activity;
    }

    public void setActivity(Activity activity) {
        this.activity = activity;
    }

    public float distanceTo(Location location) {
        float[] results = new float[3];
        android.location.Location.distanceBetween(this.latitude, this.longitude,
                location.getLatitude(), location.getLongitude(), results);
        return results[0];
    }

    private boolean activityIsSet() {
        return activity == null;
    }

    public void requestPermissions() throws IllegalStateException {
        // Check if activity is set
        if (activityIsSet()) {
            throw new IllegalStateException("Activity is not set");
        }

        //Check if permission is granted
        if (ActivityCompat.checkSelfPermission(activity, android.Manifest.permission.ACCESS_FINE_LOCATION) != PackageManager.PERMISSION_GRANTED && ActivityCompat.checkSelfPermission(activity, android.Manifest.permission.ACCESS_COARSE_LOCATION) != PackageManager.PERMISSION_GRANTED) {
            // Send permissions request
            ActivityCompat.requestPermissions(activity, new String[]{android.Manifest.permission.ACCESS_FINE_LOCATION}, 1);
        }
    }


    @SuppressLint("MissingPermission")
    public void updateCurrentLocation() throws IllegalStateException {
        // Check if activity is set
        if (activityIsSet()) {
            throw new IllegalStateException("Activity is not set");
        }

        //Create location client
        FusedLocationProviderClient fusedLocationClient = LocationServices.getFusedLocationProviderClient(activity);

        //Get current location
        fusedLocationClient.getCurrentLocation(Priority.PRIORITY_HIGH_ACCURACY, new CancellationToken() {
            @SuppressWarnings("ConstantConditions")
            @NonNull
            @Override
            public CancellationToken onCanceledRequested(@NonNull OnTokenCanceledListener onTokenCanceledListener) {
                return null;
            }

            @Override
            public boolean isCancellationRequested() {
                return false;
            }
        }).addOnSuccessListener(location -> {
            //Set longitude and latitude
            setLongitude(location.getLongitude());
            setLatitude(location.getLatitude());
        });
    }
    public void onRequestPermissionsResult(int requestCode, int[] grantResults) throws IllegalStateException{
        // Check if activity is set
        if (activityIsSet()) {
            throw new IllegalStateException("Activity is not set");
        }

        if (requestCode == 1) {
            if (!(grantResults.length > 0 && grantResults[0] == PackageManager.PERMISSION_GRANTED)) {
                // permission not granted, show a message to the user and disable the feature
                AlertDialog.Builder builder = new AlertDialog.Builder(activity);
                builder.setTitle("Permission denied");
                builder.setMessage("You need to grant location permission to use this feature, please grant it in the settings.");
                builder.setOnDismissListener(dialog -> {
                    // Call finish() after the dialog is dismissed
                    activity.finish();
                });
                builder.setNegativeButton("No", (dialog, which) -> {
                    // Call finish() after the dialog is dismissed
                    activity.finish();
                });
                builder.setPositiveButton("OK", (dialog, which) -> {
                    // Send permissions request via settings
                    Intent intent = new Intent();
                    intent.setAction(Settings.ACTION_APPLICATION_DETAILS_SETTINGS);
                    Uri uri = Uri.fromParts("package", activity.getPackageName(), null);
                    intent.setData(uri);
                    activity.startActivity(intent);
                    //TODO when back from settings check if permission is granted
                });
                builder.show();
            }

        }
    }
}