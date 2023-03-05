package com.example.geodetective;

import androidx.appcompat.app.AppCompatActivity;

import android.content.Intent;
import android.os.Bundle;
import android.widget.Button;
import android.widget.ImageButton;

public class EditDetailsActivity extends AppCompatActivity {

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_edit_details);

        // Get buttons from activity
        ImageButton returnBtn = findViewById(R.id.BackBtn);
        Button updateUsernameBtn = findViewById(R.id.changeUsernameBtn);
        Button updatePassBtn = findViewById(R.id.changePasswordBtn);


        //Set on click listeners
        returnBtn.setOnClickListener(v -> {
            // Start create profile activity
            // TODO: Change to proper profile activity once added
            startActivity(new Intent(getApplicationContext(), HomeActivity.class));
        });

    }
}
