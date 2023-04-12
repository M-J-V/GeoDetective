package com.example.geodetective.gameComponents;

import android.content.Context;
import android.view.ViewGroup;
import android.widget.TextView;

import androidx.constraintlayout.widget.ConstraintLayout;

import com.example.geodetective.R;

import org.junit.Before;
import org.junit.Test;

import static org.junit.Assert.*;

public class TimerTest {
    private Context context;
    private ViewGroup layout;
    private Timer timer;

    @Before
    public void setUp() {
        context = null;
        layout = new ConstraintLayout(context);
        timer = new Timer(context, layout);
    }

    @Test(expected = IllegalArgumentException.class)
    public void testConstructorWithNullContext() {
        new Timer(null, layout);
    }

    @Test(expected = IllegalArgumentException.class)
    public void testConstructorWithNullLayout() {
        new Timer(context, null);
    }

    @Test
    public void testAddTimer() {
        timer.add();
        TextView textView = layout.findViewById(R.id.timer);
        assertNotNull(textView);
    }

    @Test
    public void testRemoveTimer() {
        timer.add();
        timer.remove();
        TextView textView = layout.findViewById(R.id.timer);
        assertNull(textView);
    }

    @Test
    public void testStopTimer() {
        timer.add();
        CharSequence initialTime = timer.getTime();
        timer.stop();
        assertEquals(initialTime, timer.getTime());
    }

    @Test
    public void testGetTime() {
        timer.add();
        assertNotNull(timer.getTime());
    }
}
