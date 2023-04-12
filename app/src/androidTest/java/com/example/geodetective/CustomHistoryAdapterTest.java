package com.example.geodetective.guiListAdapters;

import android.content.Context;
import android.view.View;

import androidx.test.core.app.ApplicationProvider;

import org.junit.Before;
import org.junit.Test;

import java.util.ArrayList;

import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertNotNull;


public class CustomHistoryAdapterTest {

    private Context context;

    @Before
    public void setUp() {
        context = ApplicationProvider.getApplicationContext();
    }

    @Test
    public void testGetCount() {
        ArrayList<String> titles = new ArrayList<>();
        ArrayList<String> times = new ArrayList<>();
        ArrayList<Integer> outcomes = new ArrayList<>();
        titles.add("Quest 1");
        titles.add("Quest 2");
        times.add("10:00 AM");
        times.add("11:00 AM");
        outcomes.add(1);
        outcomes.add(0);

        CustomHistoryAdapter adapter = new CustomHistoryAdapter(context, titles, times, outcomes);
        assertEquals(2, adapter.getCount());
    }

    @Test
    public void testGetView() {
        ArrayList<String> titles = new ArrayList<>();
        ArrayList<String> times = new ArrayList<>();
        ArrayList<Integer> outcomes = new ArrayList<>();
        titles.add("Quest 1");
        times.add("10:00 AM");
        outcomes.add(1);

        CustomHistoryAdapter adapter = new CustomHistoryAdapter(context, titles, times, outcomes);
        assertNotNull(adapter.getView(0, null, null));
    }

}
