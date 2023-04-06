package com.example.geodetective.gameComponents;

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

/**
 * The Location class provides methods for managing and obtaining location information, including
 * checking and requesting location permissions, updating the current location, and calculating the
 * distance between two locations.
 */
public class Location {
    public static final int PERMISSIONS_REQUEST = 1;
    private final LocationManager manager;
    private Activity activity;
    private double latitude;
    private double longitude;
    public Location(double latitude, double longitude, @NonNull Activity activity) {
        if (latitude < -90 || latitude > 90)
            throw new IllegalArgumentException("Latitude must be between -90 and 90");
        if (longitude < -180 || longitude > 180)
            throw new IllegalArgumentException("Longitude must be between -180 and 180");

        this.latitude = latitude;
        this.longitude = longitude;
        this.activity = activity;
        this.manager = (LocationManager) activity.getSystemService(Context.LOCATION_SERVICE);
        checkLocationServices();
    }

    public Location (double latitude, double longitude) {
        if (latitude < -90 || latitude > 90)
            throw new IllegalArgumentException("Latitude must be between -90 and 90");
        if (longitude < -180 || longitude > 180)
            throw new IllegalArgumentException("Longitude must be between -180 and 180");

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

    /**
     * The function checks if the location services are enabled and prompts the user to enable them if
     * they are not.
     */
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

    /**
     * The function returns the latitude value as a double.
     *
     * @return The method is returning a double value which represents the latitude.
     */
    public double getLatitude() {
        return latitude;
    }

    /**
     * This function sets the latitude value of an object.
     *
     * @param latitude latitude is a variable of type double that represents the geographic latitude of
     * a location. It is used in a method to set the value of the latitude instance variable of an
     * object.
     */
    public void setLatitude(double latitude) {
        if (latitude < -90 || latitude > 90)
            throw new IllegalArgumentException("Latitude must be between -90 and 90");
        this.latitude = latitude;
    }

    /**
     * The function returns the longitude value as a double.
     *
     * @return The method is returning a double value which represents the longitude.
     */
    public double getLongitude() {
        return longitude;
    }

    /**
     * This function sets the longitude value of an object.
     *
     * @param longitude longitude is a variable of type double that represents the geographic longitude
     * coordinate of a location. This method sets the value of the longitude variable for an object
     * instance.
     */
    public void setLongitude(double longitude) {
        if (longitude < -180 || longitude > 180)
            throw new IllegalArgumentException("Longitude must be between -180 and 180");

        this.longitude = longitude;
    }

    /**
     * The function returns the activity object.
     *
     * @return The method `getActivity()` is returning an object of type `Activity`.
     */
    public Activity getActivity() {
        return activity;
    }

    /**
     * This function sets the activity for a Java class.
     *
     * @param activity The parameter "activity" is an object of the class "Activity". The method
     * "setActivity" sets the value of the instance variable "activity" to the value passed as a
     * parameter. This is commonly used in Android development to set the current activity context for
     * a class.
     */
    public void setActivity(Activity activity) {
        this.activity = activity;
    }

    /**
     * This Java function calculates the distance between two locations using their latitude and
     * longitude coordinates.
     *
     * @param location The "location" parameter is an object of the "Location" class, which represents
     * a geographic location with latitude and longitude coordinates. It is used to calculate the
     * distance between the current location (represented by the latitude and longitude instance
     * variables of the current object) and the location passed as a parameter.
     * @return The `distanceTo` method is returning a `float` value which represents the distance
     * between the current location and the location passed as a parameter. The distance is calculated
     * using the `distanceBetween` method of the `android.location.Location` class. The distance is
     * stored in the first element of the `results` array, which is then returned by the method.
     */
    public float distanceTo(@NonNull Location location) {
        float[] results = new float[3];
        android.location.Location.distanceBetween(latitude, longitude,
                location.getLatitude(), location.getLongitude(), results);
        return results[0];
    }

    /**
     * This function checks if the activity variable is null or not.
     *
     * @return The method is returning a boolean value. It returns `true` if the `activity` variable is
     * `null`, and `false` otherwise.
     */
    private boolean activityNotSet() {
        return activity == null;
    }

    /**
     * This function checks if the activity is set and if the permission to access location is granted,
     * and requests the permission if not.
     */
    public void requestPermissions() throws IllegalStateException {
        // Check if activity is set
        if (activityNotSet()) {
            throw new IllegalStateException("Activity is not set");
        }

        //Check if permission is granted
        if (ActivityCompat.checkSelfPermission(activity, android.Manifest.permission.ACCESS_FINE_LOCATION) != PackageManager.PERMISSION_GRANTED && ActivityCompat.checkSelfPermission(activity, android.Manifest.permission.ACCESS_COARSE_LOCATION) != PackageManager.PERMISSION_GRANTED) {
            // Send permissions request
            ActivityCompat.requestPermissions(activity, new String[]{android.Manifest.permission.ACCESS_FINE_LOCATION}, PERMISSIONS_REQUEST);
        }
    }

    /**
     * This Java function updates the current location of the device and runs a specified function with
     * the updated location information.
     *
     * @param func `func` is an instance of the `LocFunction` interface, which has a single method
     * `run()` that takes an argument of type `LocationHelper`. This interface is used to pass a
     * function that will be executed after the current location is successfully obtained. The
     * `LocationHelper` object is passed
     */
    @SuppressLint("MissingPermission")
    public void updateCurrentLocation(LocFunction func) throws IllegalStateException {
        // Check if activity is set
        if (activityNotSet()) {
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

    /**
     * This function checks if a permission request has been granted and shows a dialog to the user if
     * it has not been granted.
     *
     * @param requestCode An integer code that identifies the request for permission. This code is used
     * to match the request with the response received in the onRequestPermissionsResult() method.
     * @param grantResults An array of integers representing the results of the permission requests.
     * Each element in the array corresponds to a permission requested in the original
     * requestPermissions() call. The value of the element indicates whether the permission was granted
     * or denied. A value of PackageManager.PERMISSION_GRANTED indicates that the permission was
     * granted, while a value
     */
    public void onRequestPermissionsResult(int requestCode, int[] grantResults) throws IllegalStateException{
        // Check if activity is set
        if (activityNotSet()) {
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

    /**
     * This function cancels a dialog and finishes the activity when clicked.
     *
     * @param dialog The dialog parameter is the dialog that triggered the click event. It is of type
     * DialogInterface, which is an interface used to create dialogs and manage their events. In this
     * case, the onClick method is called when a button in the dialog is clicked, and the dialog
     * parameter represents that dialog.
     * @param which The "which" parameter in this method represents the button that was clicked by the
     * user in the dialog box. It is an integer value that corresponds to the position of the button in
     * the dialog box. For example, if there are two buttons in the dialog box, "OK" and "Cancel",
     */
    private void onClick(DialogInterface dialog, int which) {
        dialog.cancel();
        activity.finish();
    }

}