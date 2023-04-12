package com.example.geodetective;

import static org.junit.Assert.fail;

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
}

