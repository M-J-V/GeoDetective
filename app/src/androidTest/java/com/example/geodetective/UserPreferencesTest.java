package com.example.geodetective;

import static org.junit.Assert.fail;

import androidx.test.core.app.ApplicationProvider;

import com.example.geodetective.singletons.UserPreferences;

import org.junit.Test;

public class UserPreferencesTest {

    /**
     * This function tests that the UserPreferences singleton instance is not null.
     */
    @Test
    public void getInstanceNonNull() {
        UserPreferences userPreferences = UserPreferences.getInstance(ApplicationProvider.getApplicationContext());
        if(userPreferences == null) {
            fail("UserPreferences.getInstance() returned null");
        }
    }

    /**
     * This function tests if the UserPreferences.getPreference() method returns null when the
     * preference does not exist.
     */
    @Test
    public void getPreferenceReturnsNull() {
        UserPreferences userPreferences = UserPreferences.getInstance(ApplicationProvider.getApplicationContext());
        if(userPreferences.getPreference("test") != null) {
            fail("UserPreferences.getPreference() did not return null");
        }
    }

    /**
     * This function tests if the UserPreferences class can successfully retrieve a value that was
     * previously stored.
     */
    @Test
    public void getPreferenceReturnsValue() {
        UserPreferences userPreferences = UserPreferences.getInstance(ApplicationProvider.getApplicationContext());
        userPreferences.putPreference("test1", "test");
        if(userPreferences.getPreference("test1") == null) {
            fail("UserPreferences.getPreference() returned null");
        }
    }

    /**
     * This is a unit test in Java that checks if the UserPreferences.getBoolean() method returns the
     * default value when the key is not found.
     */
    @Test
    public void getBooleanNull(){
        UserPreferences userPreferences = UserPreferences.getInstance(ApplicationProvider.getApplicationContext());
        if(!userPreferences.getBoolean("testGetBoolean", true)) {
            fail("UserPreferences.getBoolean() did not return default value");
        }
    }

    /**
     * This function tests if the UserPreferences.getBoolean() method returns the default value when
     * the stored value is not a boolean.
     */
    @Test
    public void getBooleanNotBoolean() {
        UserPreferences userPreferences = UserPreferences.getInstance(ApplicationProvider.getApplicationContext());
        userPreferences.putPreference("testGetBooleanType", "test");
        if(!userPreferences.getBoolean("testGetBooleanType", true)) {
            fail("UserPreferences.getBoolean() did not return default value");
        }
    }

    /**
     * This is a unit test in Java that checks if the UserPreferences class returns true when getting a
     * boolean preference.
     */
    @Test
    public void getBooleanTrue() {
        UserPreferences userPreferences = UserPreferences.getInstance(ApplicationProvider.getApplicationContext());
        userPreferences.putPreference("testGetBooleanTrue", true);
        if(!userPreferences.getBoolean("testGetBooleanTrue", false)) {
            fail("UserPreferences.getBoolean() did not return true");
        }
    }

    /**
     * This is a JUnit test case to verify that the `getBoolean()` method of the `UserPreferences`
     * class returns false when the stored value is false.
     */
    @Test
    public void getBooleanFalse() {
        UserPreferences userPreferences = UserPreferences.getInstance(ApplicationProvider.getApplicationContext());
        userPreferences.putPreference("testGetBooleanFalse", false);
        if(userPreferences.getBoolean("testGetBooleanFalse", true)) {
            fail("UserPreferences.getBoolean() did not return false");
        }
    }

    /**
     * This is a JUnit test case that checks if an exception is thrown when invalid input is passed to
     * the putPreference() method of the UserPreferences class.
     */
    @Test
    public void invalidInputPut(){
        UserPreferences userPreferences = UserPreferences.getInstance(ApplicationProvider.getApplicationContext());
        try {
            userPreferences.putPreference(null, null);
            fail("UserPreferences.putPreference() did not throw exception");
        } catch (Exception ignored) {
        }
    }

    /**
     * This is a unit test in Java that checks if the UserPreferences class correctly returns true when
     * checking if a preference exists.
     */
    @Test
    public void containsTrue() {
        UserPreferences userPreferences = UserPreferences.getInstance(ApplicationProvider.getApplicationContext());
        userPreferences.putPreference("testContainsTrue", "test");
        if(!userPreferences.contains("testContainsTrue")) {
            fail("UserPreferences.contains() did not return true");
        }
    }

    /**
     * This is a JUnit test to check if the UserPreferences class correctly returns false when checking
     * for a non-existent preference.
     */
    @Test
    public void containsFalse() {
        UserPreferences userPreferences = UserPreferences.getInstance(ApplicationProvider.getApplicationContext());
        if(userPreferences.contains("testContainsFalse")) {
            fail("UserPreferences.contains() did not return false");
        }
    }
}
