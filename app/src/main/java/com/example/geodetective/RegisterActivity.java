package com.example.geodetective;

import androidx.appcompat.app.AppCompatActivity;

import android.content.Intent;
import android.media.Image;
import android.os.Bundle;
import android.widget.Button;
import android.widget.ImageButton;

public class RegisterActivity extends AppCompatActivity {

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_register);

        // Get buttons from activity
        Button signupBtn = findViewById(R.id.Signup_Btn);
        ImageButton backBtn = findViewById(R.id.Back_To_Login_Btn);

        signupBtn.setOnClickListener(v -> {
            // Start home activity
            startActivity(new Intent(getApplicationContext(), HomeActivity.class));
        });

        backBtn.setOnClickListener(v -> {
            // Start login activity
            startActivity(new Intent(getApplicationContext(), LoginActivity.class));
        });

    }
}