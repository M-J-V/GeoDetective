package com.example.geodetective;

import androidx.appcompat.app.AppCompatActivity;

import android.content.Intent;
import android.media.Image;
import android.os.Bundle;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ImageButton;
import android.widget.TextView;

public class RegisterActivity extends AppCompatActivity {

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_register);

        // Get buttons from activity
        Button signupBtn = findViewById(R.id.Signup_Btn);
        ImageButton backBtn = findViewById(R.id.Back_To_Login_Btn);

        // Get text widgets
        EditText usernameWidget = findViewById(R.id.Login_Username);
        EditText passwordWidget = findViewById(R.id.Login_Password);
        EditText passwordAgainWidget = findViewById(R.id.Login_Password_Again);
        TextView ErrorText = findViewById(R.id.ErrorText);


        signupBtn.setOnClickListener(v -> {
            // Start home activity
            AccountDetailsChecker checker = AccountDetailsChecker.getInstance();
            String username = usernameWidget.getText().toString();
            String password = passwordWidget.getText().toString();
            String passwordAgain = passwordAgainWidget.getText().toString();

            try {
                if (checker.checkDetails(username, password, passwordAgain)) {
                    startActivity(new Intent(getApplicationContext(), LoginActivity.class));
                }
            } catch (IllegalArgumentException e) {
                ErrorText.setText(e.getMessage());
            }

        });

        backBtn.setOnClickListener(v -> {
            // Start login activity
            startActivity(new Intent(getApplicationContext(), LoginActivity.class));
        });

    }
}