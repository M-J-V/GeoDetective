package com.example.geodetective;

import androidx.appcompat.app.AppCompatActivity;

import android.content.Intent;
import android.os.Bundle;
import android.widget.Button;
import android.widget.ImageButton;

public class HomeActivity extends AppCompatActivity {

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_home);

        // Get buttons from activity
        ImageButton profileBtn = findViewById(R.id.Profile_Btn);
        Button createQuestBtn = findViewById(R.id.Create_Quest_Btn);
        Button joinQuestBtn = findViewById(R.id.Play_Quest_Btn);

        //Set on click listeners
        createQuestBtn.setOnClickListener(v -> {
            // Start create quest activity
            startActivity(new Intent(getApplicationContext(), CreateQuestActivity.class));
        });

        profileBtn.setOnClickListener(v -> {
            // Start profile activity
            startActivity(new Intent(getApplicationContext(), ProfileActivity.class));
        });

    }
}