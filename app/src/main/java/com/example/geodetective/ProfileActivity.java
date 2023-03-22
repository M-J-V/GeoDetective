package com.example.geodetective;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;

import android.content.Intent;
import android.os.Bundle;
import android.util.Log;
import android.widget.Button;
import android.widget.ImageButton;
import android.widget.TextView;

import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.Task;
import com.google.firebase.firestore.Query;
import com.google.firebase.firestore.QueryDocumentSnapshot;
import com.google.firebase.firestore.QuerySnapshot;

import org.checkerframework.checker.units.qual.A;

import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Date;

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
        Button request_button = findViewById(R.id.button_permission);
        ImageButton returnBtn = findViewById(R.id.BackBtn);

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

    }

    private void getAttempts() {
        Query userAttempts = db.attempts.whereEqualTo("Username", user.getUsername());
        ArrayList<String> titles = new ArrayList<String>();
        ArrayList<String> times = new ArrayList<String>();
        // Boolean Arraylists don't play nice being passed through intents
        ArrayList<Integer> outcomes = new ArrayList<Integer>();
        userAttempts.get().addOnCompleteListener(new OnCompleteListener<QuerySnapshot>() {
            @Override
            public void onComplete(@NonNull Task<QuerySnapshot> task) {
                if(task.isSuccessful()) {
                    for (QueryDocumentSnapshot doc : task.getResult()) {
                        titles.add(doc.get("Quest").toString());

                        times.add(formatDate(doc.get("Time Completed").toString()));

                        if((Boolean)doc.get("Success")) {
                            outcomes.add(1);
                        } else {
                            outcomes.add(0);
                        }
                    }
                    loadHistoryActivity(titles,times,outcomes);
                }
            }
        });
    }

    private String formatDate(String strDate) {
        Date date;
        SimpleDateFormat df = new SimpleDateFormat("yyyyMMdd_HHmmss");
        SimpleDateFormat outputDate = new SimpleDateFormat("dd/MM/yyyy HH:mm");
        try {
            date = df.parse(strDate);
        } catch (ParseException e) {
            throw new RuntimeException("Failed to parse date: ", e);
        }
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