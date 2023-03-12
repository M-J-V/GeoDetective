package com.example.geodetective;

import androidx.appcompat.app.AppCompatActivity;

import android.os.Bundle;
import android.widget.ImageButton;
import android.widget.TextView;

public class HistoryActivity extends AppCompatActivity {

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_history);

        //get buttons
        ImageButton backBtn = findViewById(R.id.backimagebutton);

        //get text view
        TextView historyText = findViewById(R.id.textHistoryQuest);
    }


}