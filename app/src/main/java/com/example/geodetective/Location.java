package com.example.geodetective;

public class Location {
    private double latitude;
    private double longitude;

    public Location(double latitude, double longitude) {
        this.latitude = latitude;
        this.longitude = longitude;
    }

    public Location() {
        this.latitude = 0;
        this.longitude = 0;
    }

    public double getLatitude() {
        return latitude;
    }

    public double getLongitude() {
        return longitude;
    }

    public void setLatitude(double latitude) {
        this.latitude = latitude;
    }

    public void setLongitude(double longitude) {
        this.longitude = longitude;
    }

    public float distanceTo(Location location) {
        float[] results = new float[3];
        android.location.Location.distanceBetween(this.latitude, this.longitude,
                location.getLatitude(), location.getLongitude(), results);
        return results[0];
    }
}
