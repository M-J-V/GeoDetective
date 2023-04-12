package com.example.geodetective;

import static androidx.test.core.app.ApplicationProvider.getApplicationContext;
import static org.junit.Assert.assertFalse;
import static org.junit.Assert.assertTrue;

import android.content.Context;

import org.junit.Before;
import org.junit.Test;


public class CompareResourceTest {

    private CompareResource resourceCompare;

    /**
     * This is a setup method in a Java test class that initializes a new instance of the
     * CompareResource class.
     */
    @Before
    public void setUp() {
        resourceCompare = new CompareResource();
    }

    /**
     * This function tests if a string resource in an Android app is equal to a passed string.
     */
    @Test
    public void stringResourceSameAsPassedString_returnsTrue() {
        Context context = getApplicationContext();
        boolean result = resourceCompare.isEqual(context, R.string.app_name, "GeoDetective");
        assertTrue(result);
    }

    /**
     * This function tests if a string resource in an Android application is equal to a passed string
     * and returns false if they are different.
     */
    @Test
    public void stringResourceDifferentAsPassedString_returnsFalse() {
        Context context = getApplicationContext();
        boolean result = resourceCompare.isEqual(context, R.string.app_name, "NotGeoDetective");
        assertFalse(result);
    }

}