package com.example.geodetective;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;

import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.TextView;
import android.widget.Toast;

import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.Task;
import com.google.firebase.firestore.DocumentSnapshot;

public class LoginActivity extends AppCompatActivity {

    DbConnection db = DbConnection.getInstance();
    ActiveUser user = ActiveUser.getInstance();
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

        // Get error text fields
        TextView errorText = findViewById(R.id.ErrorTextLogin);

        loginBtn.setOnClickListener(v -> {
            // Start home activity
            String username = usernameWidget.getText().toString();
            String password = passwordWidget.getText().toString();

            try {
                loginUser(username,password,errorText);
            } catch (Throwable e) {
                errorText.setText(e.getMessage());
            }

        });

        registerBtn.setOnClickListener(v -> {
            // Start register activity
            startActivity(new Intent(getApplicationContext(), RegisterActivity.class));
        });
    }

    private void loginUser (String username, String password, TextView errorText) throws IllegalArgumentException {
        if (username.equals("") || password.equals("")) {
            throw new IllegalArgumentException("Please fill in both Username and Password");
        }
        String hashPass = LoginEncoder.hashWord(password);
        db.users.document(username).get().addOnCompleteListener(new OnCompleteListener<DocumentSnapshot>() {
            @Override
            public void onComplete(@NonNull Task<DocumentSnapshot> task){
                String errorMsg = "";
                if (task.isSuccessful()) {
                    DocumentSnapshot User = task.getResult();
                    if (User.exists()) {
                        if (hashPass.equals(User.get("Password"))) {
                            // User succesfully logs in
                            user.setUsername(username);
                            user.setPassword(hashPass);
                            user.setTrusted((boolean)User.get("Trusted"));
                            startActivity(new Intent(getApplicationContext(), HomeActivity.class));
                        } else {
                           errorMsg = "Incorrect Password";
                        }
                    } else {
                       errorMsg = "Username not found in Database";
                    }
                } else {
                    errorMsg = "Error getting data from Database";
                }
                errorText.setText(errorMsg);
            }
        });
    }

}