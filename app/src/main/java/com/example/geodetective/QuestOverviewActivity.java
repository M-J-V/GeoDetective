package com.example.geodetective;

import android.annotation.SuppressLint;
import android.content.Intent;
import android.graphics.Color;
import android.os.Bundle;
import android.os.Handler;
import android.util.Log;
import android.util.TypedValue;
import android.widget.Button;
import android.widget.ImageButton;
import android.widget.ImageView;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AlertDialog;
import androidx.appcompat.app.AppCompatActivity;
import androidx.constraintlayout.widget.ConstraintLayout;

import java.util.Locale;

//TODO change name, since we merged the join activity and the joined quest activity quest overview activity is not a good name anymore.
//TODO make page scrollable, longer descriptions will not fit on the screen.
//TODO hint button should be implemented
//TODO implement edit button
//TODO ask permissions first!


// TODO Bug when uploading image for quest from gallery
public class QuestOverviewActivity extends AppCompatActivity {
    private boolean isQuestStarted = false;
    private boolean isQuestCompleted = false;
    private Location location;

    private TextView timer;

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

        ActiveQuest activeQuestInstance = ActiveQuest.getInstance();

        // Update UI
        questName.setText(activeQuestInstance.getQuest().getName());
        questDescription.setText(activeQuestInstance.getQuest().getDescription());
        questImage.setImageBitmap(activeQuestInstance.getQuest().getImage());

        editButton.setOnClickListener(v -> {
            String nameOfUser = ActiveUser.getInstance().getUsername();
            String nameOfCreator = activeQuestInstance.getQuest().getCreator();
            if(nameOfCreator.compareTo(nameOfUser) == 0) {
                startActivity(new Intent(getApplicationContext(), EditQuestActivity.class));
            }
        });

        //Request permissions
        location.requestPermissions();

        //Set on click listeners
        backButton.setOnClickListener(v -> {
            // Start list of quests activity
            navigateBack();
        });

        submitQuestButton.setOnClickListener(v -> {
            if(!isQuestStarted){
                startQuest();
            }else{
                endQuest();
            }
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

        if(location.distanceTo(new Location(questLatitude, questLongitude)) < 100) {
            // Stop timer
            isQuestCompleted = true;
            isQuestStarted = false;

            //TODO submit quest to server
//            Database.questCompleted(
//                    getIntent().getExtras().getString("username"),
//                    getIntent().getExtras().getString("questName"),
//                    timer.getText().toString(),
//                    true);

            // Show success message
            AlertDialog.Builder builder = new AlertDialog.Builder(this);
            builder.setTitle("Congratulations!");
            builder.setMessage("You have completed the quest successfully in " + timer.getText().toString() + "!");
            builder.setPositiveButton("OK", (dialog, which) -> dialog.dismiss());
            AlertDialog dialog = builder.create();
            dialog.show();

            // Remove timer
            ConstraintLayout layout = findViewById(R.id.quest_overview_layout);
            layout.removeView(timer);

            Button startQuestButton = findViewById(R.id.check_result_btn);
            startQuestButton.setText("Start Quest");
        } else {
            AlertDialog.Builder builder = new AlertDialog.Builder(this);
            builder.setTitle("Quest not finished!");
            builder.setMessage("You have not completed the quest successfully.");
            builder.setPositiveButton("Continue quest", (dialog, which) -> dialog.dismiss());
            builder.setNegativeButton("Abort quest", (dialog, which) -> {
                //TODO submit quest to server
    //          Database.questCompleted(
    //                  getIntent().getExtras().getString("username"),
    //                  getIntent().getExtras().getString("questName"),
    //                  timer.getText().toString(),
    //                  false);

                // Stop timer
                isQuestCompleted = true;

                // Remove timer
                ConstraintLayout layout = findViewById(R.id.quest_overview_layout);
                layout.removeView(timer);

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
        isQuestStarted = true;

        addTimer();
        Button startQuestButton = findViewById(R.id.check_result_btn);
        startQuestButton.setText("Finish Quest");
    }

    @SuppressLint("SetTextI18n")
    private void addTimer() {
        ConstraintLayout layout = findViewById(R.id.quest_overview_layout);

        timer = new TextView(this);
        timer.setText("00:00:00");
        timer.setTextColor(Color.BLACK);
        timer.setTextSize(TypedValue.COMPLEX_UNIT_SP, 30);

        ConstraintLayout.LayoutParams params = new ConstraintLayout.LayoutParams(
                ConstraintLayout.LayoutParams.WRAP_CONTENT,
                ConstraintLayout.LayoutParams.WRAP_CONTENT
        );
        params.topToBottom = R.id.check_result_btn; // set the text view below the start button
        params.startToStart = R.id.check_result_btn;
        params.endToEnd = R.id.check_result_btn;
        params.topMargin = 16; // set top margin to 16dp

        timer.setLayoutParams(params);

        layout.addView(timer);

        // Start timer
        Handler handler = new Handler();
        Runnable runnable = new Runnable() {
            int seconds = 0;
            int minutes = 0;
            int hours = 0;
            @Override
            public void run() {
                seconds++;
                if (seconds == 60) {
                    seconds = 0;
                    minutes++;
                }
                if (minutes == 60) {
                    minutes = 0;
                    hours++;
                }
                timer.setText(String.format(Locale.getDefault(),"%02d:%02d:%02d", hours, minutes, seconds));
                if (isQuestCompleted) {
                    isQuestCompleted = false;
                    handler.removeCallbacks(this);
                } else {
                    handler.postDelayed(this, 1000);
                }
            }
        };
        handler.postDelayed(runnable, 1000);
    }

    @Override
    protected void onDestroy() {
        super.onDestroy();
        ActiveQuest activeQuestInstance = ActiveQuest.getInstance();
        activeQuestInstance.setQuest(null);
    }

    public void navigateBack() {
        super.onBackPressed();
    }

    @Override
    public void onRequestPermissionsResult(int requestCode, @NonNull String[] permissions, @NonNull int[] grantResults) {
        super.onRequestPermissionsResult(requestCode, permissions, grantResults);
        location.onRequestPermissionsResult(requestCode, grantResults);
    }
}
