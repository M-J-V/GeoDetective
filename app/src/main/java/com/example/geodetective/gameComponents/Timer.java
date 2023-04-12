package com.example.geodetective.gameComponents;

import android.annotation.SuppressLint;
import android.content.Context;
import android.graphics.Color;
import android.os.Handler;
import android.util.TypedValue;
import android.view.ViewGroup;
import android.widget.TextView;

import androidx.constraintlayout.widget.ConstraintLayout;

import com.example.geodetective.R;

import java.util.Locale;

/**
 * The Timer class creates and starts a timer, updating the timer display every second, and allows for
 * the removal and stopping of the timer.
 */
public class Timer {
    private final Context context;
    private final ViewGroup layout;
    private boolean stop = false;
    private TextView timer;

    public Timer(Context context, ViewGroup layout) {
        if(context == null || layout == null)
            throw new IllegalArgumentException("Activity or layout cannot be null");

        this.context = context;
        this.layout = layout;
    }

    public Timer() {
        context = null;
        layout = null;
    }

    /**
     * The function creates a timer and starts it, updating the timer display every second.
     */
    @SuppressLint("SetTextI18n")
    public void add() {
        // Create timer
        timer = new TextView(context);
        timer.setText("00:00:00");
        timer.setTextColor(Color.BLACK);
        timer.setTextSize(TypedValue.COMPLEX_UNIT_SP, 30);

        ConstraintLayout.LayoutParams params = new ConstraintLayout.LayoutParams(
                ConstraintLayout.LayoutParams.WRAP_CONTENT,
                ConstraintLayout.LayoutParams.WRAP_CONTENT
        );
        params.topToBottom = R.id.check_result_btn; // set the text view below the start button
        params.startToStart = R.id.check_result_btn;
        params.endToEnd = R.id.check_result_btn;
        params.topMargin = 16; // set top margin to 16dp

        timer.setLayoutParams(params);

        layout.addView(timer);

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
                timer.setText(String.format(Locale.getDefault(),"%02d:%02d:%02d", hours, minutes, seconds));
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
