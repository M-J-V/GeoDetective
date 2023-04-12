package com.example.geodetective;

import static org.junit.Assert.*;

import org.junit.Before;
import org.junit.Test;

import java.util.ArrayList;

import android.graphics.Bitmap;
import com.example.geodetective.singletons.QuestImages;

public class QuestImagesTest {

    private QuestImages questImages;

    @Before
    public void setUp() {
        questImages = QuestImages.getInstance();
    }

    @Test
    public void testGetInstance() {
        QuestImages newImages = QuestImages.getInstance();
        assertEquals(questImages, newImages);
    }

    @Test
    public void testSetImages() {
        ArrayList<Bitmap> images = new ArrayList<>();
        Bitmap image1 = Bitmap.createBitmap(100, 100, Bitmap.Config.ARGB_8888);
        images.add(image1);
        questImages.setImages(images);
        ArrayList<Bitmap> retrievedImages = questImages.getImages();
        assertNotNull(retrievedImages);
    }

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
