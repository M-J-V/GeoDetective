package com.example.geodetective;

import static androidx.test.core.app.ApplicationProvider.getApplicationContext;
import static org.junit.Assert.assertFalse;
import static org.junit.Assert.assertTrue;

import android.content.Context;

import org.junit.Before;
import org.junit.Test;


public class CompareResourceTest {

    CompareResource resourceCompare;

    @Before
    public void setUp() {
        resourceCompare = new CompareResource();
    }

    @Test
    public void stringResourceSameAsPassedString_returnsTrue() {
        Context context = getApplicationContext();
        boolean result = resourceCompare.isEqual(context, R.string.app_name, "GeoDetective");
        assertTrue(result);
    }

    @Test
    public void stringResourceDifferentAsPassedString_returnsFalse() {
        Context context = getApplicationContext();
        boolean result = resourceCompare.isEqual(context, R.string.app_name, "NotGeoDetective");
        assertFalse(result);
    }

}