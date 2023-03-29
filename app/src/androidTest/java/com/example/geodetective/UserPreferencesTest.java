package com.example.geodetective;

import static org.junit.Assert.fail;

import androidx.test.core.app.ApplicationProvider;

import org.junit.Test;

public class UserPreferencesTest {

    @Test
    public void getInstanceNonNull() {
        UserPreferences userPreferences = UserPreferences.getInstance(ApplicationProvider.getApplicationContext());
        if(userPreferences == null) {
            fail("UserPreferences.getInstance() returned null");
        }
    }

    @Test
    public void getPreferenceReturnsNull() {
        UserPreferences userPreferences = UserPreferences.getInstance(ApplicationProvider.getApplicationContext());
        if(userPreferences.getPreference("test") != null) {
            fail("UserPreferences.getPreference() did not return null");
        }
    }

    @Test
    public void getPreferenceReturnsValue() {
        UserPreferences userPreferences = UserPreferences.getInstance(ApplicationProvider.getApplicationContext());
        userPreferences.putPreference("test1", "test");
        if(userPreferences.getPreference("test1") == null) {
            fail("UserPreferences.getPreference() returned null");
        }
    }

    @Test
    public void getBooleanNull(){
        UserPreferences userPreferences = UserPreferences.getInstance(ApplicationProvider.getApplicationContext());
        if(!userPreferences.getBoolean("testGetBoolean", true)) {
            fail("UserPreferences.getBoolean() did not return default value");
        }
    }

    @Test
    public void getBooleanNotBoolean() {
        UserPreferences userPreferences = UserPreferences.getInstance(ApplicationProvider.getApplicationContext());
        userPreferences.putPreference("testGetBooleanType", "test");
        if(!userPreferences.getBoolean("testGetBooleanType", true)) {
            fail("UserPreferences.getBoolean() did not return default value");
        }
    }

    @Test
    public void getBooleanTrue() {
        UserPreferences userPreferences = UserPreferences.getInstance(ApplicationProvider.getApplicationContext());
        userPreferences.putPreference("testGetBooleanTrue", true);
        if(!userPreferences.getBoolean("testGetBooleanTrue", false)) {
            fail("UserPreferences.getBoolean() did not return true");
        }
    }

    @Test
    public void getBooleanFalse() {
        UserPreferences userPreferences = UserPreferences.getInstance(ApplicationProvider.getApplicationContext());
        userPreferences.putPreference("testGetBooleanFalse", false);
        if(userPreferences.getBoolean("testGetBooleanFalse", true)) {
            fail("UserPreferences.getBoolean() did not return false");
        }
    }

    @Test
    public void invalidInputPut(){
        UserPreferences userPreferences = UserPreferences.getInstance(ApplicationProvider.getApplicationContext());
        try {
            userPreferences.putPreference(null, null);
            fail("UserPreferences.putPreference() did not throw exception");
        } catch (Exception ignored) {
        }
    }

    @Test
    public void containsTrue() {
        UserPreferences userPreferences = UserPreferences.getInstance(ApplicationProvider.getApplicationContext());
        userPreferences.putPreference("testContainsTrue", "test");
        if(!userPreferences.contains("testContainsTrue")) {
            fail("UserPreferences.contains() did not return true");
        }
    }

    @Test
    public void containsFalse() {
        UserPreferences userPreferences = UserPreferences.getInstance(ApplicationProvider.getApplicationContext());
        if(userPreferences.contains("testContainsFalse")) {
            fail("UserPreferences.contains() did not return false");
        }
    }
}
