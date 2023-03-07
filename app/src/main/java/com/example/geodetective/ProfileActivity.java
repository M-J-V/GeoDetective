package com.example.geodetective;

import androidx.appcompat.app.AppCompatActivity;

import android.content.Intent;
import android.os.Bundle;
import android.widget.Button;
import android.widget.ImageButton;

public class ProfileActivity extends AppCompatActivity {

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_profile);

        //get buttons from activity
        Button history_button = findViewById(R.id.button_history);
        Button edit_profile_button = findViewById(R.id.button_edit_profile);
        ImageButton returnBtn = findViewById(R.id.BackBtn);

        //Set on click listeners
        returnBtn.setOnClickListener(v -> {
            // Start create profile activity
            startActivity(new Intent(getApplicationContext(), HomeActivity.class));
        });

        edit_profile_button.setOnClickListener(v -> {
            // go to edit account page
            startActivity(new Intent(getApplicationContext(), EditDetailsActivity.class));
        });

        //add link to history when history is created

    }
}