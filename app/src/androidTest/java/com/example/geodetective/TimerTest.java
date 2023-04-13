package com.example.geodetective;

import static org.junit.Assert.assertNotEquals;
import static org.junit.Assert.fail;

import android.content.Context;
import android.widget.TextView;

import androidx.constraintlayout.widget.ConstraintLayout;
import androidx.test.core.app.ApplicationProvider;

import com.example.geodetective.gameComponents.Timer;

import org.junit.Test;

public class TimerTest {

    /**
     * This function tests if the Timer constructor throws an exception when given null parameters.
     */
    @Test
    public void invalidConstructor() {
        try{
            new Timer(null, null);
            fail("Timer constructor did not throw an exception");
        } catch (Exception ignored) {}
    }

    @Test
    public void testAddAddsTimerToLayout() {
        Context context = ApplicationProvider.getApplicationContext();
        TextView timer = new TextView(context);
        ConstraintLayout layout = new ConstraintLayout(context);
        int amountOfChildren = layout.getChildCount();

        ConstraintLayout.LayoutParams params = new ConstraintLayout.LayoutParams(
                ConstraintLayout.LayoutParams.WRAP_CONTENT,
                ConstraintLayout.LayoutParams.WRAP_CONTENT
        );

        Timer timerObject = new Timer(layout, timer);
        timerObject.add(params);

        assertNotEquals(amountOfChildren, layout.getChildCount());
    }

    //TODO Check if textview of timer updates every second, remove removes timer from layout, stop stops timer, getTime.
}

