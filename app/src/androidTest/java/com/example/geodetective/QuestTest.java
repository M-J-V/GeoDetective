package com.example.geodetective;

import static org.junit.Assert.*;
import android.graphics.Bitmap;
import org.junit.Test;

public class QuestTest {

    @Test
    public void testQuestProperties() {
        String name = "My Quest";
        String creator = "John Doe";
        String description = "Find the hidden treasure!";
        String hint = "Look under the big tree";
        Bitmap image = null;
//        Location location = new Location(37.7749, -122.4194);

        Quest quest = new Quest(name, creator, description, hint, image, null);

        assertEquals(name, quest.getName());
        assertEquals(creator, quest.getCreator());
        assertEquals(description, quest.getDescription());
        assertEquals(hint, quest.getHint());
        assertEquals(image, quest.getImage());
//        assertEquals(location, quest.getLocation());

        String newName = "My New Quest";
        quest.setName(newName);
        assertEquals(newName, quest.getName());

        String newCreator = "Jane Smith";
        quest.setCreator(newCreator);
        assertEquals(newCreator, quest.getCreator());

        String newDescription = "Find the hidden key!";
        quest.setDescription(newDescription);
        assertEquals(newDescription, quest.getDescription());

        String newHint = "It's buried in the ground";
        quest.setHint(newHint);
        assertEquals(newHint, quest.getHint());

        Bitmap newImage = null;
        quest.setImage(newImage);
        assertEquals(newImage, quest.getImage());

        Location newLocation = new Location(38.9072, -77.0369);
        quest.setLocation(newLocation);
        assertEquals(newLocation, quest.getLocation());
    }

    @Test
    public void testQuestStarted() {
        Quest quest = new Quest("My Quest", "John Doe", "Find the hidden treasure!", "Look under the big tree", null, null);
        assertFalse(quest.isStarted());

        quest.start();
        assertTrue(quest.isStarted());

        quest.stop();
        assertFalse(quest.isStarted());
    }
}
