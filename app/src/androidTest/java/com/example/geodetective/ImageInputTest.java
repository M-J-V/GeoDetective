package com.example.geodetective;

import static org.junit.Assert.fail;

import android.app.Activity;

import androidx.test.core.app.ApplicationProvider;

import com.example.geodetective.gameComponents.ImageInput;

import org.junit.Test;

public class ImageInputTest {

    private static final int CAMERA_REQUEST = 1888;
    private static final int SELECT_PICTURE = 200;
    /**
     * This function tests if an exception is thrown when a null argument is passed to
     * the constructor of the ImageInput class.
     */
    @Test
    public void checkInvalidConstructor() {
        try {
            new ImageInput(null);
            fail("Exception not thrown");
        } catch (Exception ignored) {}
    }

    /**
     * This function tests if an IllegalArgumentException is thrown when a null argument is passed to
     * the askPermissions method of the ImageInput class.
     */
    @Test
    public void checkInvalidAskPermissions() {
        try {
            new ImageInput(ApplicationProvider.getApplicationContext()).askPermissions(false);
            fail("Exception not thrown");
        } catch (Exception ignored) {}
    }

    /**
     * This is a unit test in Java that checks for invalid input in the onActivityResult method of an
     * ImageInput class.
     */
    @Test
    public void checkOnActivityResultInvalidInput() {
        try {
            ImageInput input = new ImageInput(ApplicationProvider.getApplicationContext());
            input.onActivityResult(CAMERA_REQUEST, Activity.RESULT_OK, null, null, null);
            input.onActivityResult(CAMERA_REQUEST, Activity.RESULT_OK, null, null, ApplicationProvider.getApplicationContext());
            input.onActivityResult(SELECT_PICTURE, Activity.RESULT_OK, null, null, null);
            input.onActivityResult(SELECT_PICTURE, Activity.RESULT_OK, null, null, ApplicationProvider.getApplicationContext());
            fail("Exception not thrown");
        } catch (Exception ignored) {}
    }
}
