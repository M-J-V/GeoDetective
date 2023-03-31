package com.example.geodetective;

import static org.junit.Assert.fail;

import org.junit.Test;

public class TimerTest {

    @Test
    public void invalidConstructor() {
        try{
            Timer timer = new Timer(null, null);
            fail("Timer constructor did not throw an exception");
        } catch (Exception ignored) {}
    }
}
