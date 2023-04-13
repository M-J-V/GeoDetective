package com.example.geodetective.gameComponents;

import android.annotation.SuppressLint;
import android.os.Handler;
import android.view.ViewGroup;
import android.widget.TextView;

import androidx.constraintlayout.widget.ConstraintLayout;

import java.util.Locale;

/**
 * The Timer class creates and starts a timer, updating the timer display every second, and allows for
 * the removal and stopping of the timer.
 */
public class Timer {
    private final ViewGroup layout;
    private final TextView timer;
    private boolean stop = false;

    public Timer(ViewGroup layout, TextView timerObject) {
        if(layout == null || timerObject == null)
            throw new IllegalArgumentException("Activity, layout, or timerObject cannot be null");

        this.layout = layout;
        this.timer = timerObject;
    }

    /**
     * The function creates a timer and starts it, updating the timer display every second.
     */
    @SuppressLint("SetTextI18n")
    public void add(ConstraintLayout.LayoutParams params) {
        timer.setLayoutParams(params);
        layout.addView(timer);
        startTimer(timer);
    }

    private void startTimer(TextView timerObject){
        // Start timer
        Handler handler = new Handler();
        Runnable runnable = new Runnable() {
            int seconds = 0;
            int minutes = 0;
            int hours = 0;
            @Override
            public void run() {
                seconds++;
                if (seconds == 60) {
                    seconds = 0;
                    minutes++;
                }
                if (minutes == 60) {
                    minutes = 0;
                    hours++;
                }
                timerObject.setText(String.format(Locale.getDefault(),"%02d:%02d:%02d", hours, minutes, seconds));
                if (stop) {
                    stop = false;
                    handler.removeCallbacks(this);
                } else {
                    handler.postDelayed(this, 1000);
                }
            }
        };
        handler.postDelayed(runnable, 1000);
    }
    /**
     * The function removes a view called "timer" from a layout.
     */
    public void remove() {
        layout.removeView(timer);
    }

    /**
     * The function sets a boolean variable "stop" to true.
     */
    public void stop() {
        stop = true;
    }

    /**
     * The function returns the text value of a timer as a CharSequence.
     *
     * @return The method `getTime()` is returning a `CharSequence` object, which represents a sequence
     * of characters. Specifically, it is returning the text value of the `timer` object.
     */
    public CharSequence getTime() {
        return timer.getText();
    }
}
