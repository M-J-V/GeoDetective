package com.example.geodetective;

import android.annotation.SuppressLint;
import android.content.Intent;
import android.os.Bundle;
import android.widget.Button;
import android.widget.ImageButton;
import android.widget.TextView;
import android.widget.Toast;

import androidx.appcompat.app.AppCompatActivity;
import androidx.appcompat.widget.SwitchCompat;

import com.google.firebase.firestore.Query;
import com.google.firebase.firestore.QueryDocumentSnapshot;

import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Date;
import java.util.Locale;
import java.util.Objects;

public class ProfileActivity extends AppCompatActivity {

    ActiveUser user = ActiveUser.getInstance();
    DbConnection db = DbConnection.getInstance();
    @SuppressLint("SetTextI18n")
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_profile);

        //get buttons from activity
        Button history_button = findViewById(R.id.button_history);
        Button edit_profile_button = findViewById(R.id.button_edit_profile);
        Button delete_button = findViewById(R.id.button_delete);
        Button request_button = findViewById(R.id.button_permission);
        ImageButton returnBtn = findViewById(R.id.BackBtn);

        // Get switches from activity
        SwitchCompat cameraSwitch = findViewById(R.id.cameraSwitch);
        SwitchCompat gallerySwitch = findViewById(R.id.gallerySwitch);

        UserPreferences preferences = UserPreferences.getInstance(this);

        cameraSwitch.setChecked(preferences.getBoolean("cameraPermissions", false));
        gallerySwitch.setChecked(preferences.getBoolean("galleryPermissions", false));

        //get user details textView
        TextView usernameTxt = findViewById(R.id.usernameInfo);
        TextView trustTxt = findViewById(R.id.trustedInfo);
        TextView permissionTxt = findViewById(R.id.Text);

        //update user details
        usernameTxt.setText(user.getUsername());

        String permission;
        if(user.getTrusted()) {
            permission = "Yes";
        } else {
            permission = "No";
        }
        trustTxt.setText(permission);

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
            getAttempts();
        });

        request_button.setOnClickListener(v -> {
            // send request to database

            if(!user.getTrusted()) {
                db.sendRequest(user.getUsername());
                permissionTxt.setText("Creator Permission Request sent to admins");
            } else {
                permissionTxt.setText("You already have permission to create quests");
            }

        });

        delete_button.setOnClickListener(v -> {
            // delete current user
            db.deleteUserAndQuests(user.getUsername(), user.getPassword());

            // Go back to login page
            startActivity(new Intent(getApplicationContext(), LoginActivity.class));

            // remove current active user
            user.disconnectUser();
        });

        cameraSwitch.setOnCheckedChangeListener((buttonView, isChecked) -> {
            preferences.putPreference("cameraPermissions", isChecked);

            Toast.makeText(this, "Changed camera permissions to " + preferences.getBoolean("cameraPermissions", false), Toast.LENGTH_SHORT).show();
        });

        gallerySwitch.setOnCheckedChangeListener((buttonView, isChecked) -> {
            preferences.putPreference("galleryPermissions", isChecked);

            Toast.makeText(this, "Changed gallery permissions to "+preferences.getBoolean("galleryPermissions", false), Toast.LENGTH_SHORT).show();
        });

    }

    private void getAttempts() {
        Query userAttempts = db.attempts.whereEqualTo("Username", user.getUsername());
        ArrayList<String> titles = new ArrayList<>();
        ArrayList<String> times = new ArrayList<>();
        // Boolean Arraylists don't play nice being passed through intents
        ArrayList<Integer> outcomes = new ArrayList<>();
        userAttempts.get().addOnCompleteListener(task -> {
            if(task.isSuccessful()) {
                for (QueryDocumentSnapshot doc : task.getResult()) {
                    titles.add(Objects.requireNonNull(doc.get("Quest")).toString());

                    times.add(formatDate(Objects.requireNonNull(doc.get("Time Completed")).toString()));

                    //noinspection ConstantConditions
                    if((Boolean)doc.get("Success")) {
                        outcomes.add(1);
                    } else {
                        outcomes.add(0);
                    }
                }
                loadHistoryActivity(titles,times,outcomes);
            }
        });
    }

    private String formatDate(String strDate) {
        Date date;
        SimpleDateFormat df = new SimpleDateFormat("yyyyMMdd_HHmmss", Locale.getDefault());
        SimpleDateFormat outputDate = new SimpleDateFormat("dd/MM/yyyy HH:mm", Locale.getDefault());
        try {
            date = df.parse(strDate);
        } catch (ParseException e) {
            throw new RuntimeException("Failed to parse date: ", e);
        }
        assert date != null;
        return outputDate.format(date);
    }

    private void loadHistoryActivity(ArrayList<String> titles, ArrayList<String> times, ArrayList<Integer> outcomes) {
        Intent historyIntent = new Intent(getApplicationContext(), HistoryActivity.class);
        historyIntent.putStringArrayListExtra("titles", titles);
        historyIntent.putStringArrayListExtra("timesCompleted", times);
        historyIntent.putIntegerArrayListExtra("outcomes",outcomes);
        startActivity(historyIntent);
    }
}