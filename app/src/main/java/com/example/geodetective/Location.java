package com.example.geodetective;

import android.annotation.SuppressLint;
import android.app.Activity;
import android.content.Context;
import android.content.DialogInterface;
import android.content.Intent;
import android.content.pm.PackageManager;
import android.location.LocationManager;
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

interface LocFunction {
    void run(Location location);
}
public class Location {
    public static final int PERMISSIONS_REQUEST = 1;
    final LocationManager manager;
    private Activity activity;
    private double latitude;
    private double longitude;
    public Location(double latitude, double longitude, @NonNull Activity activity) {
        this.latitude = latitude;
        this.longitude = longitude;
        this.activity = activity;
        this.manager = (LocationManager) activity.getSystemService(Context.LOCATION_SERVICE);
        checkLocationServices();
    }

    public Location (double latitude, double longitude) {
        this.latitude = latitude;
        this.longitude = longitude;
        this.activity = null;
        this.manager = null;
    }

    public Location(@NonNull Activity activity) {
        this.latitude = 0;
        this.longitude = 0;
        this.activity = activity;
        this.manager = (LocationManager) activity.getSystemService(Context.LOCATION_SERVICE);
        checkLocationServices();
    }

    public void checkLocationServices() {
        if (manager == null)
            throw new IllegalStateException("Location manager is not set");
        if (activity == null)
            throw new IllegalStateException("Activity is not set");

        if (!manager.isProviderEnabled(LocationManager.GPS_PROVIDER)) {
            AlertDialog.Builder builder = new AlertDialog.Builder(activity);
            builder.setTitle("Location services disabled");
            builder.setMessage("Please enable location services to use this feature.");
            builder.setPositiveButton("OK", (dialog, which) -> activity.startActivity(new Intent(Settings.ACTION_LOCATION_SOURCE_SETTINGS)));

            builder.setNegativeButton("Cancel", this::onClick);
        }
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

    public float distanceTo(@NonNull Location location) {
        float[] results = new float[3];
        android.location.Location.distanceBetween(latitude, longitude,
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
            ActivityCompat.requestPermissions(activity, new String[]{android.Manifest.permission.ACCESS_FINE_LOCATION}, PERMISSIONS_REQUEST);
        }
    }

    @SuppressLint("MissingPermission")
    public void updateCurrentLocation(LocFunction func) throws IllegalStateException {
        // Check if activity is set
        if (activityIsSet()) {
            throw new IllegalStateException("Activity is not set");
        }

        //Check location services
        checkLocationServices();


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
            func.run(this);
        });
    }

    public void onRequestPermissionsResult(int requestCode, int[] grantResults) throws IllegalStateException{
        // Check if activity is set
        if (activityIsSet()) {
            throw new IllegalStateException("Activity is not set");
        }

        if (requestCode == PERMISSIONS_REQUEST) {
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

    private void onClick(DialogInterface dialog, int which) {
        dialog.cancel();
        activity.finish();
    }

}