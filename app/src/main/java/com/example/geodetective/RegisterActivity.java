package com.example.geodetective;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;

import android.content.Intent;
import android.os.Bundle;
import android.util.Log;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ImageButton;
import android.widget.TextView;

import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.Task;
import com.google.firebase.firestore.DocumentSnapshot;
import com.google.firebase.firestore.FirebaseFirestore;


import java.util.HashMap;
import java.util.Map;

public class RegisterActivity extends AppCompatActivity {
    DbConnection db = DbConnection.getInstance();
    AccountDetailsChecker checker = AccountDetailsChecker.getInstance();

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
            DbConnection db = DbConnection.getInstance();
            String username = usernameWidget.getText().toString();
            String password = passwordWidget.getText().toString();
            String passwordAgain = passwordAgainWidget.getText().toString();

            try {
                if (checker.checkDetails(username, password, passwordAgain)) {
                    registerUser(username, password, ErrorText);
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

    private void registerUser (String username, String password, TextView errorText) throws IllegalArgumentException {
        db.users.document(username).get().addOnCompleteListener(new OnCompleteListener<DocumentSnapshot>() {
            @Override
            public void onComplete(@NonNull Task<DocumentSnapshot> task){
                String errorMsg = "";
                if (task.isSuccessful()) {
                    DocumentSnapshot User = task.getResult();
                    if (User.exists()) {
                        errorMsg = "Username already in use";
                    } else {
                        db.createNewUser(username, password);
                        startActivity(new Intent(getApplicationContext(), LoginActivity.class));
                    }
                } else {
                    errorMsg = "Error getting data from Database";
                }
                errorText.setText(errorMsg);
            }
        });
    }
}