package com.example.geodetective;

import android.graphics.Bitmap;

import org.junit.Before;
import org.junit.Test;

import static org.junit.Assert.*;

import com.example.geodetective.gameComponents.Location;
import com.example.geodetective.gameComponents.Quest;

public class QuestTest {

    private Quest quest;

    /**
     * This function initializes image, location and quest to avoid declaring
     * and initializing it for every test cases made.
     */
    @Before
    public void setUp() {
        Bitmap image = Bitmap.createBitmap(100, 100, Bitmap.Config.ARGB_8888);
        Location location = new Location(37.7749, -77.429);
        quest = new Quest("Test Quest", "Test Creator", "Test Description",
                "Test Hint", image, location);
    }

    /**
     * This method checks if the correct latitude and longitude is retrieved.
     */
    @Test
    public void testGetLocation() {
        assertEquals(37.7749, quest.getLocation().getLatitude(), 0.0);
        assertEquals(-77.429, quest.getLocation().getLongitude(), 0.0);
    }

    /**
     * This method checks if the correct name is retrieved.
     */
    @Test
    public void testGetName() {
        assertEquals("Test Quest", quest.getName());
    }

    /**
     * This method checks if the correct creator is retrieved.
     */
    @Test
    public void testGetCreator() {
        assertEquals("Test Creator", quest.getCreator());
    }

    /**
     * This method checks if the correct description is retrieved.
     */
    @Test
    public void testGetDescription() {
        assertEquals("Test Description", quest.getDescription());
    }

    /**
     * This method checks if the correct hint is retrieved.
     */
    @Test
    public void testGetHint() {
        assertEquals("Test Hint", quest.getHint());
    }

    /**
     * This method checks if the correct image is retrieved.
     */
    @Test
    public void testGetImage() {
        assertNotNull(quest.getImage());
    }

    /**
     * This method checks if the newly set name is correctly retrieved.
     */
    @Test
    public void testSetName() {
        quest.setName("New Name");
        assertEquals("New Name", quest.getName());
    }

    /**
     * This method checks if the newly set creator is correctly retrieved.
     */
    @Test
    public void testSetCreator() {
        quest.setCreator("New Creator");
        assertEquals("New Creator", quest.getCreator());
    }

    /**
     * This method checks if the newly set description is correctly retrieved.
     */
    @Test
    public void testSetDescription() {
        quest.setDescription("New Description");
        assertEquals("New Description", quest.getDescription());
    }

    /**
     * This method checks if the newly set hint is correctly retrieved.
     */
    @Test
    public void testSetHint() {
        quest.setHint("New Hint");
        assertEquals("New Hint", quest.getHint());
    }

    /**
     * This method checks if the newly set image is correctly retrieved.
     */
    @Test
    public void testSetImage() {
        Bitmap newImage = Bitmap.createBitmap(200, 200, Bitmap.Config.ARGB_8888);
        quest.setImage(newImage);
        assertEquals(newImage, quest.getImage());
    }

    /**
     * This method checks if the newly set location is correctly retrieved.
     */
    @Test
    public void testSetLocation() {
        Location newLocation = new Location(40.7128, -74.0060);
        quest.setLocation(newLocation);
        assertEquals(newLocation, quest.getLocation());
    }

    /**
     * This method checks if the quest starts when you start it.
     */
    @Test
    public void testStart() {
        quest.start();
        assertTrue(quest.isStarted());

    }

    /**
     * This method checks if the quest stops when you stop it.
     */
    @Test
    public void testStop() {
        quest.start();
        quest.stop();
        assertFalse(quest.isStarted());
    }
}
