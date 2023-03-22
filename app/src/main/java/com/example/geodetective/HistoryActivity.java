package com.example.geodetective;

import static com.example.geodetective.R.id.customListView;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;

import android.content.Intent;
import android.graphics.Bitmap;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.AdapterView;
import android.widget.ImageButton;
import android.widget.ListView;
import android.widget.TextView;

import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.Task;
import com.google.firebase.firestore.DocumentSnapshot;

import java.util.ArrayList;

public class HistoryActivity extends AppCompatActivity {

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
        CustomHistoryAdapter customHistoryAdapter = new CustomHistoryAdapter(getApplicationContext(),titles, times, outcomes);
        listView.setAdapter(customHistoryAdapter);
        //get buttons
        ImageButton backBtn = findViewById(R.id.backimagebutton);

        //get text view
        TextView historyText = findViewById(R.id.textHistoryQuest);

        backBtn.setOnClickListener(v -> {
            // Start profile activity
            startActivity(new Intent(getApplicationContext(), ProfileActivity.class));
        });
    }



}