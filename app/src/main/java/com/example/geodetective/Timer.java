package com.example.geodetective;

import android.annotation.SuppressLint;
import android.app.Activity;
import android.graphics.Color;
import android.os.Handler;
import android.util.TypedValue;
import android.view.ViewGroup;
import android.widget.TextView;

import androidx.constraintlayout.widget.ConstraintLayout;

import java.util.Locale;

public class Timer {
    private final Activity activity;
    private final ViewGroup layout;
    private boolean stop = false;
    private TextView timer;

    public Timer(Activity activity, ViewGroup layout) {
        this.activity = activity;
        this.layout = layout;
    }

    @SuppressLint("SetTextI18n")
    public void add() {
        // Create timer
        timer = new TextView(activity);
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

    public void remove() {
        layout.removeView(timer);
    }

    public void stop() {
        stop = true;
    }

    public CharSequence getTime() {
        return timer.getText();
    }
}
