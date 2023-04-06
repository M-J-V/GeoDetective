package com.example.geodetective.activities;

import android.content.Intent;
import android.os.Bundle;
import android.widget.ImageButton;
import android.widget.ListView;

import androidx.appcompat.app.AppCompatActivity;

import com.example.geodetective.R;
import com.example.geodetective.guiListAdapters.CustomHistoryAdapter;

import java.util.ArrayList;

public class HistoryActivity extends AppCompatActivity {
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