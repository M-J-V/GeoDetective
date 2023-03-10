package com.example.geodetective;

import androidx.appcompat.app.AppCompatActivity;

import android.content.Intent;
import android.os.Bundle;
import android.widget.Button;
import android.widget.ImageButton;
import android.widget.TextView;

public class ProfileActivity extends AppCompatActivity {

    ActiveUser user = ActiveUser.getInstance();
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_profile);

        //get buttons from activity
        Button history_button = findViewById(R.id.button_history);
        Button edit_profile_button = findViewById(R.id.button_edit_profile);
        ImageButton returnBtn = findViewById(R.id.BackBtn);

        //get user details textView
        TextView usernameTxt = findViewById(R.id.usernameInfo);
        TextView passwordTxt = findViewById(R.id.passwordInfo);

        //update user details
        usernameTxt.setText(user.getUsername());
        passwordTxt.setText(user.getPassword());

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