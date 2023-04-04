package com.example.geodetective.activities;

import androidx.appcompat.app.AppCompatActivity;

import android.content.Intent;
import android.os.Bundle;
import android.widget.ImageButton;
import android.widget.ListView;
import android.widget.TextView;

import com.example.geodetective.listAdapters.CustomHistoryAdapter;
import com.example.geodetective.R;

import java.util.ArrayList;

public class HistoryActivity extends AppCompatActivity {
    CustomHistoryAdapter customHistoryAdapter;
    ListView listView;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_history);

        listView = (ListView) findViewById(R.id.customListView);
        // Get all information for the list
        ArrayList<String> titles = getIntent().getStringArrayListExtra("titles");
        ArrayList<String> times = getIntent().getStringArrayListExtra("timesCompleted");
        ArrayList<Integer> outcomes = getIntent().getIntegerArrayListExtra("outcomes");
        customHistoryAdapter = new CustomHistoryAdapter(getApplicationContext(),titles, times, outcomes);
        listView.setAdapter(customHistoryAdapter);
        //get buttons
        ImageButton backBtn = findViewById(R.id.backimagebutton);

        backBtn.setOnClickListener(v -> {
            // Start profile activity
            startActivity(new Intent(getApplicationContext(), ProfileActivity.class));
        });
    }



}