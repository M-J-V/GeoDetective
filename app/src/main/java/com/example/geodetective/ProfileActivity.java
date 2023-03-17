package com.example.geodetective;

import androidx.appcompat.app.AppCompatActivity;

import android.content.Intent;
import android.os.Bundle;
import android.widget.Button;
import android.widget.ImageButton;
import android.widget.TextView;

public class ProfileActivity extends AppCompatActivity {

    ActiveUser user = ActiveUser.getInstance();
    DbConnection db = DbConnection.getInstance();
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_profile);

        //get buttons from activity
        Button history_button = findViewById(R.id.button_history);
        Button edit_profile_button = findViewById(R.id.button_edit_profile);
        Button delete_button = findViewById(R.id.button_delete);
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

        history_button.setOnClickListener(v -> {
            // go to history page
            startActivity(new Intent(getApplicationContext(), HistoryActivity.class));
        });

        delete_button.setOnClickListener(v -> {
            // delete current user
            db.deleteUserAndQuests(user.getUsername(), user.getPassword());

            // Go back to login page
            startActivity(new Intent(getApplicationContext(), LoginActivity.class));

            // remove current active user
            user.disconnectUser();
        });

    }
}