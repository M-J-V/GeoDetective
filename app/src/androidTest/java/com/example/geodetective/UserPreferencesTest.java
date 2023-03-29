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
        if(userPreferences.getPreference("test") == null) {
            return;
        } else {
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
        if(userPreferences.getBoolean("testGetBoolean", true)) {
            return;
        } else {
            fail("UserPreferences.getBoolean() did not return default value");
        }
    }
}
