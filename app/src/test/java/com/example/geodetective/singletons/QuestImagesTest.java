package com.example.geodetective.singletons;

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
    public void testSetAndGetImages() {
        ArrayList<Bitmap> images = new ArrayList<>();
        Bitmap image1 = null;
        Bitmap image2 = null;
        images.add(image1);
        images.add(image2);
        questImages.setImages(images);
        ArrayList<Bitmap> retrievedImages = questImages.getImages();
        assertNotNull(retrievedImages);
    }

}