package com.example.geodetective;

import static org.junit.Assert.fail;

import android.content.pm.PackageManager;

import androidx.test.core.app.ApplicationProvider;

import com.example.geodetective.gameComponents.Location;

import org.junit.Test;

@SuppressWarnings("ConstantConditions")
public class LocationTest {

    /**
     * This function tests if the Location constructor throws an exception when given null parameters.
     */
    @Test
    public void invalidConstructorNoActivity() {
        try{
            new Location(null);
            fail("Location constructor did not throw an exception");
        } catch (Exception ignored) {}
    }

    /**
     * This function tests if the Location constructor throws an exception when given null parameters.
     */
    @Test
    public void invalidConstructorNoActivityWithLocation() {
        try{
            new Location(0, 0, null);
            fail("Location constructor did not throw an exception");
        } catch (Exception ignored) {}
    }

    /**
     * This function tests if the Location constructor throws an exception when given invalid parameters.
     */
    @Test
    public void invalidConstructorInvalidLatitudeNegative() {
        try{
            new Location(-91, 0);
            fail("Location constructor did not throw an exception");
        } catch (Exception ignored) {}
    }

    /**
     * This function tests if the Location constructor throws an exception when given invalid parameters.
     */
    @Test
    public void invalidConstructorInvalidLatitudePositive() {
        try{
            new Location(91, 0);
            fail("Location constructor did not throw an exception");
        } catch (Exception ignored) {}
    }

    /**
     * This function tests if the Location constructor throws an exception when given invalid parameters.
     */
    @Test
    public void invalidConstructorInvalidLongitudeNegative() {
        try{
            new Location(0, -181);
            fail("Location constructor did not throw an exception");
        } catch (Exception ignored) {}
    }

    /**
     * This function tests if the Location constructor throws an exception when given invalid parameters.
     */
    @Test
    public void invalidConstructorInvalidLongitudePositive() {
        try{
            new Location(0, 181);
            fail("Location constructor did not throw an exception");
        } catch (Exception ignored) {}
    }

    /**
     * This is a JUnit test case that checks if the Location.checkLocationServices() method throws an
     * exception when called without an activity.
     */
    @Test
    public void checkLocationServicesWithoutActivity(){
        try{
            Location location = new Location(0, 0);
            location.checkLocationServices();
            fail("Location.checkLocationServices() did not throw an exception");
        } catch (Exception ignored) {}
    }

    /**
     * This function tests if an exception is thrown when setting an invalid negative latitude value
     * for a location object.
     */
    @Test
    public void checkInvalidSetLatitudeNegative(){
        try{
            Location location = new Location(0, 0);
            location.setLatitude(-91);
            fail("Location.setLatitude() did not throw an exception");
        } catch (Exception ignored) {}
    }

    /**
     * This is a JUnit test case to check if an exception is thrown when an invalid latitude value
     * (greater than 90) is set for a Location object.
     */
    @Test
    public void checkInvalidSetLatitudePositive(){
        try{
            Location location = new Location(0, 0);
            location.setLatitude(91);
            fail("Location.setLatitude() did not throw an exception");
        } catch (Exception ignored) {}
    }

    /**
     * This is a JUnit test case to check if an exception is thrown when setting an invalid negative
     * longitude value for a Location object.
     */
    @Test
    public void checkInvalidSetLongitudeNegative(){
        try{
            Location location = new Location(0, 0);
            location.setLongitude(-181);
            fail("Location.setLongitude() did not throw an exception");
        } catch (Exception ignored) {}
    }

    /**
     * This is a JUnit test case to check if an exception is thrown when an invalid longitude value
     * (greater than 180) is set for a Location object.
     */
    @Test
    public void checkInvalidSetLongitudePositive(){
        try{
            Location location = new Location(0, 0);
            location.setLongitude(181);
            fail("Location.setLongitude() did not throw an exception");
        } catch (Exception ignored) {}
    }

    /**
     * This function tests if the distanceTo() method in the Location class throws an exception when
     * given invalid input.
     */
    @Test
    public void checkDistanceToInvalidInput() {
        try{
            Location location = new Location(0, 0);
            location.distanceTo(null);
            fail("Location.distanceTo() did not throw an exception");
        } catch (Exception ignored) {}
    }

    /**
     * This function tests if an exception is thrown when calling the requestPermissions() method on a
     * Location object with an invalid activity.
     */
    @Test
    public void checkRequestPermissionsInvalidActivity() {
        try{
            Location location = new Location(0, 0);
            location.requestPermissions();
            fail("Location.requestPermissions() did not throw an exception");
        } catch (Exception ignored) {}
    }

    /**
     * This function tests if the updateCurrentLocation method throws an exception when there is no
     * activity.
     */
    @SuppressWarnings("ResultOfMethodCallIgnored")
    @Test
    public void checkUpdateCurrentLocationNoActivity() {
        try{
            Location location = new Location(0, 0);
            location.updateCurrentLocation(Location::getLatitude);
            fail("Location.updateCurrentLocation() did not throw an exception");
        } catch (Exception ignored) {}
    }

    /**
     * This function tests if an exception is thrown when an invalid input is passed to the
     * updateCurrentLocation method of the Location class in Java.
     */
    @Test
    public void checkUpdateCurrentLocationInvalidInput() {
        try{
            Location location = new Location(0, 0);
            location.updateCurrentLocation(null);
            fail("Location.updateCurrentLocation() did not throw an exception");
        } catch (Exception ignored) {}
    }

    /**
     * This function tests if an exception is thrown when calling the onRequestPermissionsResult method
     * of the Location class with no activity.
     */
    @Test
    public void checkOnRequestPermissionsResultNoActivity() {
        try{
            Location location = new Location(0, 0);
            int[] grantResults = new int[1];
            grantResults[0] = PackageManager.PERMISSION_GRANTED;
            location.onRequestPermissionsResult(0, grantResults);
            fail("Location.onRequestPermissionsResult() did not throw an exception");
        } catch (Exception ignored) {}
    }

    /**
     * This function tests if an exception is thrown when invalid input is passed to the
     * onRequestPermissionsResult method of the Location class in Java.
     */
    @Test
    public void checkOnRequestPermissionsResultInvalidInput() {
        try{
            Location location = new Location(0, 0, ApplicationProvider.getApplicationContext());
            location.onRequestPermissionsResult(0, null);
            fail("Location.onRequestPermissionsResult() did not throw an exception");
        } catch (Exception ignored) {}
    }

}
