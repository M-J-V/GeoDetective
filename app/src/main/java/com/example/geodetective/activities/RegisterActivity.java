package com.example.geodetective.activities;

import android.content.Intent;
import android.os.Bundle;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ImageButton;
import android.widget.TextView;

import androidx.appcompat.app.AppCompatActivity;

import com.example.geodetective.R;
import com.example.geodetective.helpers.AccountDetailsChecker;
import com.example.geodetective.helpers.LoginEncoder;
import com.example.geodetective.singletons.DbConnection;
import com.google.firebase.firestore.DocumentSnapshot;

/**
 * The RegisterActivity class sets up the registration process for new users, checks for valid account
 * details, and registers the user if the username is not already in use.
 */
public class RegisterActivity extends AppCompatActivity {
    private final DbConnection db = DbConnection.getInstance();
    private final AccountDetailsChecker checker = AccountDetailsChecker.getInstance();

    /**
     * This function sets up the Register activity, gets the necessary widgets, and sets up listeners
     * for the signup and back buttons.
     *
     * @param savedInstanceState The savedInstanceState parameter is a Bundle object that contains the
     * activity's previously saved state. It is used to restore the activity's state when it is
     * recreated, such as when the device is rotated or the activity is destroyed and recreated due to
     * a configuration change.
     */
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
            String username = usernameWidget.getText().toString().trim();
            String password = passwordWidget.getText().toString();
            String passwordAgain = passwordAgainWidget.getText().toString();

            try {
                if (checker.checkDetails(username, password, passwordAgain)) {
                    registerUser(username, LoginEncoder.hashWord(password), ErrorText);
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

    /**
     * This function registers a new user by checking if the username is already in use and creating a
     * new user if it is not.
     *
     * @param username The username of the user trying to register.
     * @param password The password parameter is a String variable that represents the password entered
     * by the user during the registration process.
     * @param errorText A TextView object that will display any error messages that occur during the
     * registration process.
     */
    private void registerUser (String username, String password, TextView errorText) throws IllegalArgumentException {

        db.users.document(username).get().addOnCompleteListener(task -> {
            String errorMsg = "";
            if (task.isSuccessful()) {
                DocumentSnapshot User = task.getResult();
                if (User.exists()) {
                    errorMsg = "Username already in use";
                } else {
                    db.createNewUser(username, password, false);
                    startActivity(new Intent(getApplicationContext(), LoginActivity.class));
                }
            } else {
                errorMsg = "Error getting data from Database";
            }
            errorText.setText(errorMsg);
        });
    }
}