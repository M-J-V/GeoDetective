package com.example.geodetective;

import androidx.appcompat.app.AppCompatActivity;

import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;

public class LoginActivity extends AppCompatActivity {

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_login);

        // Get buttons from activity
        Button loginBtn = findViewById(R.id.Login_Btn);
        Button registerBtn = findViewById(R.id.Register_Btn);

        loginBtn.setOnClickListener(v -> {
            // Start home activity
            startActivity(new Intent(getApplicationContext(), HomeActivity.class));
        });
    }


}