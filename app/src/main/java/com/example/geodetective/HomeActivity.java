package com.example.geodetective;

import androidx.appcompat.app.AppCompatActivity;

import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.ImageButton;
import android.widget.Toast;

public class HomeActivity extends AppCompatActivity {

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_home);

        // Get buttons from activity
        Button createQuestBtn = findViewById(R.id.Create_Quest_Btn);
        Button joinQuestBtn = findViewById(R.id.Play_Quest_Btn);
        Button logoutBtn = findViewById(R.id.Logout_btn);
        ImageButton returnBtn = findViewById(R.id.Profile_Btn);

        //Set on click listeners
        createQuestBtn.setOnClickListener(v -> {
            // Start create quest activity
            startActivity(new Intent(getApplicationContext(), CreateQuestActivity.class).putExtra("username", getIntent().getExtras().getString("username")));
        });

        returnBtn.setOnClickListener(v -> {
            // Start create profile activity
            startActivity(new Intent(getApplicationContext(), ProfileActivity.class));
        });

        joinQuestBtn.setOnClickListener(v -> {
            // Start create profile activity
            startActivity(new Intent(getApplicationContext(), ListOfQuests.class));
        });

        logoutBtn.setOnClickListener(v -> {
            // Start create profile activity
            startActivity(new Intent(getApplicationContext(), LoginActivity.class));
        });

    }
}