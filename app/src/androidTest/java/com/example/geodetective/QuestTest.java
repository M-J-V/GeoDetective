package com.example.geodetective;

import static androidx.test.core.app.ApplicationProvider.getApplicationContext;
import static org.junit.Assert.*;

import android.content.Context;
import android.content.res.Resources;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;

import org.junit.Before;
import org.junit.Test;

import java.io.InputStream;

public class QuestTest {

    @Test
    public void testGetName() {
        InputStream is = getApplicationContext().getResources().openRawResource(R.drawable.auditorium);
        Bitmap image = BitmapFactory.decodeStream(is);
        Location location = new Location(38.8951, -77.0364);
        Quest quest = new Quest("Vertigo 3.19", "Divo", "Floor 3", "Elevator", image, location);
        boolean nameComparison = quest.getName().equals("Vertigo 3.19");
        assertTrue(nameComparison);
    }

}