package com.example.geodetective.activities;

import android.content.Intent;
import android.content.IntentFilter;
import android.os.Bundle;
import android.widget.Button;
import android.widget.EditText;
import android.widget.TextView;

import androidx.appcompat.app.AppCompatActivity;

import com.example.geodetective.R;
import com.example.geodetective.connectivity.ConnectivityChecker;
import com.example.geodetective.helpers.LoginEncoder;
import com.example.geodetective.singletons.ActiveUser;
import com.example.geodetective.singletons.DbConnection;
import com.google.firebase.firestore.DocumentSnapshot;

/**
 * The LoginActivity class allows users to input their credentials to connect to their account.
 */
public class LoginActivity extends AppCompatActivity {

    private final DbConnection db = DbConnection.getInstance();
    private final ActiveUser user = ActiveUser.getInstance();
    private final ConnectivityChecker connectivityChecker = new ConnectivityChecker();

    /**
     This method sets up the Login activity where users can input their credentials to connect
     to their account.
     It initializes various UI components such as buttons, text inputs, and error messages;
     It also sets up the functionality of these UI components.
     @param savedInstanceState A saved instance state of the activity, which can be null.
     */
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_login);
        //Check connectivity and register a network state change listener.
        if (!ConnectivityChecker.hasInternetConnection(this))
            ConnectivityChecker.openNoInternetDialog(this);
        if (!ConnectivityChecker.hasGPSConnection(this))
            ConnectivityChecker.openNoGPSDialog(this);

        IntentFilter filter = new IntentFilter();
        filter.addAction("android.net.conn.CONNECTIVITY_CHANGE");
        filter.addAction("android.location.PROVIDERS_CHANGED");
        registerReceiver(connectivityChecker, filter);

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
            String username = usernameWidget.getText().toString().trim();
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
        db.users.document(username).get().addOnCompleteListener(task -> {
            String errorMsg = "";
            if (task.isSuccessful()) {
                DocumentSnapshot User = task.getResult();
                if (User.exists()) {
                    assert hashPass != null;
                    if (hashPass.equals(User.get("Password"))) {
                        // User successfully logs in
                        user.setUsername(username);
                        user.setPassword(hashPass);
                        //noinspection ConstantConditions
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
        });
    }

    /**
     * This method is called when this activity is popped off the stack.
     */
    @Override
    protected void onDestroy() {
        super.onDestroy();
        unregisterReceiver(connectivityChecker);
    }
}