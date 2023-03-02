package com.example.geodetective;

import androidx.appcompat.app.AppCompatActivity;

import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.TextView;

public class LoginActivity extends AppCompatActivity {

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_login);

        // Get buttons from activity
        Button loginBtn = findViewById(R.id.Login_Btn);
        Button registerBtn = findViewById(R.id.Register_Btn);

        // Get text fields
        EditText usernameWidget = findViewById(R.id.Login_Username);
        EditText passwordWidget = findViewById(R.id.Login_Password);

        // Get error text fiels
        TextView errorText = findViewById(R.id.ErrorTextLogin);

        loginBtn.setOnClickListener(v -> {
            // Start home activity
            String username = usernameWidget.getText().toString();
            String password = passwordWidget.getText().toString();

            AccountDetailsChecker checker = AccountDetailsChecker.getInstance();
            try {
                checker.checkLogin(username, password);
                startActivity(new Intent(getApplicationContext(), HomeActivity.class));
            } catch (IllegalArgumentException e) {
                errorText.setText(e.getMessage());
            }

        });

        registerBtn.setOnClickListener(v -> {
            // Start register activity
            startActivity(new Intent(getApplicationContext(), RegisterActivity.class));
        });
    }


}