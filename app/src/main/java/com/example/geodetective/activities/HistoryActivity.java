package com.example.geodetective.activities;

import android.content.Intent;
import android.os.Bundle;
import android.widget.ImageButton;
import android.widget.ListView;

import androidx.appcompat.app.AppCompatActivity;

import com.example.geodetective.R;
import com.example.geodetective.guiListAdapters.CustomHistoryAdapter;

import java.util.ArrayList;

/**
 * The HistoryActivity class allows users to see information about quests that the user took part in.
 */
public class HistoryActivity extends AppCompatActivity {
    /**
     This method sets up the History activity where users can see the quests they competed in.
     It initializes various UI components such as buttons, text inputs, and error messages;
     It also sets up the functionality of these UI components.
     @param savedInstanceState A saved instance state of the activity, which can be null.
     */
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_history);

        ListView listView = findViewById(R.id.customListView);
        // Get all information for the list
        ArrayList<String> titles = getIntent().getStringArrayListExtra("titles");
        ArrayList<String> times = getIntent().getStringArrayListExtra("timesCompleted");
        ArrayList<Integer> outcomes = getIntent().getIntegerArrayListExtra("outcomes");
        CustomHistoryAdapter customHistoryAdapter = new CustomHistoryAdapter(getApplicationContext(), titles, times, outcomes);
        listView.setAdapter(customHistoryAdapter);
        //get buttons
        ImageButton backBtn = findViewById(R.id.backimagebutton);

        backBtn.setOnClickListener(v -> {
            // Start profile activity
            startActivity(new Intent(getApplicationContext(), ProfileActivity.class));
        });
    }
}