package com.example.geodetective;

import androidx.appcompat.app.AppCompatActivity;

import android.content.Intent;
import android.os.Bundle;
import android.widget.Button;

public class Profile extends AppCompatActivity {

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_profile);

        //get buttons from activity
        Button history_button = findViewById(R.id.button_history);
        Button edit_profile_button = findViewById(R.id.button_edit_profile);

        edit_profile_button.setOnClickListener(v -> {
            // go to edit account page
            startActivity(new Intent(getApplicationContext(), EditDetailsActivity.class));
        });

        //add link to history when history is created

    }
}