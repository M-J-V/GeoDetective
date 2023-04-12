package com.example.geodetective;

import static org.junit.Assert.*;

import org.junit.Before;
import org.junit.Test;

import java.util.ArrayList;

import android.graphics.Bitmap;
import com.example.geodetective.singletons.QuestImages;

public class QuestImagesTest {

    private QuestImages questImages;

    /**
     * This function initializes questImages to avoid declaring
     * and initializing it for every test cases made.
     */
    @Before
    public void setUp() {
        questImages = QuestImages.getInstance();
    }

    /**
     * This function tests if the instance of QuestImages is the same
     * as the new instance
     */
    @Test
    public void testGetInstance() {
        QuestImages newImages = QuestImages.getInstance();
        assertEquals(questImages, newImages);
    }

    /**
     * This function tests if QuestImages' setImages() method
     * successfully sets the list of images to the provided image.
     */
    @Test
    public void testSetImages() {
        ArrayList<Bitmap> images = new ArrayList<>();
        Bitmap image1 = Bitmap.createBitmap(100, 100, Bitmap.Config.ARGB_8888);
        images.add(image1);
        questImages.setImages(images);
        ArrayList<Bitmap> retrievedImages = questImages.getImages();
        assertNotNull(retrievedImages);
    }

    /**
     * This function tests if the getImages() method retrieves the same set
     * of images that was set using the setImages() method
     * in the QuestImages class.
     */
    @Test
    public void testGetImages() {
        ArrayList<Bitmap> images = new ArrayList<>();
        Bitmap image1 = Bitmap.createBitmap(100, 100, Bitmap.Config.ARGB_8888);
        images.add(image1);
        questImages.setImages(images);
        ArrayList<Bitmap> retrievedImages = questImages.getImages();
        assertEquals(image1, retrievedImages.get(0));
    }
}
