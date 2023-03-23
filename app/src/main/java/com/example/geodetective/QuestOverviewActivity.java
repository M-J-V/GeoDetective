package com.example.geodetective;

import android.annotation.SuppressLint;
import android.content.Intent;
import android.os.Bundle;
import android.util.Log;
import android.widget.Button;
import android.widget.ImageButton;
import android.widget.ImageView;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AlertDialog;
import androidx.appcompat.app.AppCompatActivity;

// TODO: there are still some time-related issues when updating location.
//  if you press finish quest before the method updateCurrentLocation in onResume has
//  finished executing, you will get a wrong location of the user.

public class QuestOverviewActivity extends AppCompatActivity {
    private Location location;
    private Timer timer = null;
    ActiveUser user = ActiveUser.getInstance();
    ActiveQuest activeQuestInstance = ActiveQuest.getInstance();
    DbConnection db = DbConnection.getInstance();

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_quest_overview);

        // Get buttons from activity
        Button submitQuestButton = findViewById(R.id.check_result_btn);
        Button hintButton = findViewById(R.id.reveal_hint_btn);
        ImageButton backButton = findViewById(R.id.BackBtn);
        ImageButton editButton = findViewById(R.id.EditBtn);

        // Get ImageView from activity
        ImageView questImage = findViewById(R.id.Quest_Image);

        // Get text fields from activity
        TextView questDescription = findViewById(R.id.quest_description);
        TextView questName = findViewById(R.id.quest_name);

        location = new Location(this);

        // Update UI
        questName.setText(activeQuestInstance.getQuest().getName());
        questDescription.setText(activeQuestInstance.getQuest().getDescription());
        questImage.setImageBitmap(activeQuestInstance.getQuest().getImage());

        editButton.setOnClickListener(v -> {
            String nameOfUser = user.getUsername();
            String nameOfCreator = activeQuestInstance.getQuest().getCreator();
            if(nameOfCreator.compareTo(nameOfUser) == 0) {
                Log.d("DEBUG","1. Going into edit Quest");
                startActivity((new Intent(getApplicationContext(), EditQuestActivity.class)));
            }
        });

        //Request permissions
        location.requestPermissions();

        //Set on click listeners
        backButton.setOnClickListener(v -> {
            // Start list of quests activity
            if (timer != null) {
                timer.stop();
            }
            activeQuestInstance.disconnectActiveQuest();
            finish();
        });

        submitQuestButton.setOnClickListener(v -> {
            if(!activeQuestInstance.getQuest().isStarted()){
                startQuest();
            }else{
                endQuest();
            }
        });

        hintButton.setOnClickListener(v -> {
            AlertDialog.Builder builder = new AlertDialog.Builder(this);
            builder.setTitle("Hint");
            builder.setMessage(activeQuestInstance.getQuest().getHint());
            builder.setPositiveButton("OK", (dialog, which) -> dialog.dismiss());
            builder.show();
        });

    }

    @SuppressLint("SetTextI18n")
    private void endQuest() {
        location.updateCurrentLocation();

        double latitude = location.getLatitude();
        double longitude = location.getLongitude();

        double questLatitude = ActiveQuest.getInstance().getQuest().getLocation().getLatitude();
        double questLongitude = ActiveQuest.getInstance().getQuest().getLocation().getLongitude();

        Log.d("WOAH","lat: " + latitude);
        Log.d("WOAH","long: " + longitude);
        Log.d("WOAH","questLat: " + questLatitude);
        Log.d("WOAH","questLong: " + questLongitude);

        if(location.distanceTo(new Location(questLatitude, questLongitude, this)) < 100) {
            // Stop timer
            ActiveQuest.getInstance().getQuest().stop();
            timer.stop();
            Log.d("WOAH","distance: " + location.distanceTo(new Location(questLatitude, questLongitude, this)));

            // Submit attempt to database
            db.createAttempt(user.getUsername(), activeQuestInstance.getQuest().getName(), true);

            // Show success message
            AlertDialog.Builder builder = new AlertDialog.Builder(this);
            builder.setTitle("Congratulations!");
            builder.setMessage("You have completed the quest successfully in " + timer.getTime().toString() + "!");
            builder.setPositiveButton("OK", (dialog, which) -> dialog.dismiss());
            AlertDialog dialog = builder.create();
            dialog.show();

            // Remove timer
            timer.remove();

            Button startQuestButton = findViewById(R.id.check_result_btn);
            startQuestButton.setText("Start Quest");
        } else {
            Log.d("WOAH","distance: " + location.distanceTo(new Location(questLatitude, questLongitude, this)));
            AlertDialog.Builder builder = new AlertDialog.Builder(this);
            builder.setTitle("Quest not finished!");
            builder.setMessage("You have not completed the quest successfully.");
            builder.setPositiveButton("Continue quest", (dialog, which) -> dialog.dismiss());
            builder.setNegativeButton("Abort quest", (dialog, which) -> {
                // Submit attempt to database
                db.createAttempt(user.getUsername(), activeQuestInstance.getQuest().getName(), false);

                // Stop timer
                ActiveQuest.getInstance().getQuest().stop();
                timer.stop();

                // Remove timer
                timer.remove();

                Button startQuestButton = findViewById(R.id.check_result_btn);
                startQuestButton.setText("Start Quest");

                dialog.dismiss();
            });
            AlertDialog dialog = builder.create();
            dialog.show();
        }
    }

    @SuppressLint("SetTextI18n")
    private void startQuest() {
        ActiveQuest.getInstance().getQuest().start();

        timer = new Timer(this, findViewById(R.id.quest_overview_layout));
        timer.add();
        Button startQuestButton = findViewById(R.id.check_result_btn);
        startQuestButton.setText("Finish Quest");
    }

    @Override
    protected void onResume() {
        super.onResume();
        // Get ImageView from activity
        ImageView questImage = findViewById(R.id.Quest_Image);

        // Get text fields from activity
        TextView questDescription = findViewById(R.id.quest_description);
        TextView questName = findViewById(R.id.quest_name);

        ActiveQuest activeQuestInstance = ActiveQuest.getInstance();

        // Update UI
        questName.setText(activeQuestInstance.getQuest().getName());
        questDescription.setText(activeQuestInstance.getQuest().getDescription());
        questImage.setImageBitmap(activeQuestInstance.getQuest().getImage());
        location.updateCurrentLocation();
    }

    @Override
    protected void onDestroy() {
        super.onDestroy();
        ActiveQuest activeQuestInstance = ActiveQuest.getInstance();
        activeQuestInstance.setQuest(null);
    }

    @Override
    public void onBackPressed() {
        if(timer != null) {
            timer.stop();
        }
        super.onBackPressed();
    }

    @Override
    public void onRequestPermissionsResult(int requestCode, @NonNull String[] permissions, @NonNull int[] grantResults) {
        super.onRequestPermissionsResult(requestCode, permissions, grantResults);
        location.onRequestPermissionsResult(requestCode, grantResults);
    }
}
