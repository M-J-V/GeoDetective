package com.example.geodetective.activities;

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

import com.example.geodetective.R;
import com.example.geodetective.gameComponents.Location;
import com.example.geodetective.gameComponents.Timer;
import com.example.geodetective.singletons.ActiveQuest;
import com.example.geodetective.singletons.ActiveUser;
import com.example.geodetective.singletons.DbConnection;

/**
 * The QuestOverviewActivity class sets up the UI and handles button clicks for the quest overview
 * activity in an Android app.
 */
public class QuestOverviewActivity extends AppCompatActivity {
    private final ActiveUser user = ActiveUser.getInstance();
    private final ActiveQuest activeQuestInstance = ActiveQuest.getInstance();
    private final DbConnection db = DbConnection.getInstance();
    private Location location;
    private Timer timer = null;

    /**
     * This function sets up the UI and handles button clicks for the quest overview activity in an
     * Android app.
     *
     * @param savedInstanceState savedInstanceState is a Bundle object that contains the data that was
     * saved in the previous state of the activity. It is used to restore the state of the activity
     * when it is recreated, for example, when the device is rotated or when the activity is destroyed
     * and recreated due to a configuration change.
     */
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_quest_overview);

        ActiveQuest activeQuestInstance = ActiveQuest.getInstance();

        Log.d("WOAH", "Overview created");

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
            if(userIsAllowedToEdit(nameOfUser, nameOfCreator)) {
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
            //activeQuestInstance.disconnectActiveQuest();
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

    /**
     * The function checks if the active user is trusted or if the name of the creator matches the name
     * of the user trying to edit.
     *
     * @param nameOfUser The name of the user who wants to edit something.
     * @param nameOfCreator The name of the user who created the content that is being edited.
     * @return A boolean value is being returned.
     */
    private boolean userIsAllowedToEdit(String nameOfUser, String nameOfCreator) {
        return ActiveUser.getInstance().getTrusted() ||
                nameOfCreator.compareTo(nameOfUser) == 0;
    }

    /**
     * The function "endQuest()" updates the current location and calls the "endQuestLambda()"
     * function.
     */
    @SuppressLint("SetTextI18n")
    private void endQuest() {
        location.updateCurrentLocation(this::endQuestLambda);
    }

    /**
     * This method checks if the user has completed a quest successfully and displays a message
     * accordingly.
     *
     * @param location The current location of the user.
     */
    @SuppressLint("SetTextI18n")
    private void endQuestLambda(Location location) {

        //location.compareToQuest(ActiveQuest.getInstance());
        double questLatitude = ActiveQuest.getInstance().getQuest().getLocation().getLatitude();
        double questLongitude = ActiveQuest.getInstance().getQuest().getLocation().getLongitude();

        if(location.distanceTo(new Location(questLatitude, questLongitude, this)) < 25) {
            // Stop timer
            ActiveQuest.getInstance().getQuest().stop();
            timer.stop();

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
            AlertDialog.Builder builder = new AlertDialog.Builder(this);
            builder.setTitle("Quest not finished!");
            builder.setMessage("You have not completed the quest successfully.");
            builder.setPositiveButton("Continue quest", (dialog, which) -> {
                db.createAttempt(user.getUsername(), activeQuestInstance.getQuest().getName(), false);
                dialog.dismiss();
            });
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

    /**
     * This function starts a quest, sets a timer, and changes the text of a button.
     */
    @SuppressLint("SetTextI18n")
    private void startQuest() {
        ActiveQuest.getInstance().getQuest().start();

        timer = new Timer(this, findViewById(R.id.quest_overview_layout));
        timer.add();
        Button startQuestButton = findViewById(R.id.check_result_btn);
        startQuestButton.setText("Finish Quest");
    }

    /**
     * This function updates the UI and location when the activity resumes.
     */
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

        // Update Location
        location.updateCurrentLocation((location) -> this.location = location);
    }

    /**
     * This function sets the active quest instance to null when the activity is destroyed in Java.
     */
    @Override
    protected void onDestroy() {
        super.onDestroy();
    }

    /**
     * This function stops a timer if it is running and calls the superclass method to handle the back
     * button press event in an Android app.
     */
    @Override
    public void onBackPressed() {
        if(timer != null) {
            timer.stop();
        }
        ActiveQuest activeQuestInstance = ActiveQuest.getInstance();
        activeQuestInstance.setQuest(null);
        super.onBackPressed();
    }

    /**
     * This function handles the result of a permission request for location access.
     *
     * @param requestCode An integer value that identifies the request code that was passed to
     * requestPermissions() method when requesting permission.
     * @param permissions An array of strings representing the permissions requested by the app. These
     * permissions are requested by the user at runtime and are used to access certain features or data
     * on the device, such as location, camera, contacts, etc.
     * @param grantResults grantResults is an integer array that contains the results of the permission
     * requests. Each element in the array corresponds to the permission request at the same index in
     * the permissions array. The value of the element can be either PackageManager.PERMISSION_GRANTED
     * or PackageManager.PERMISSION_DENIED, depending on whether the user granted or denied the
     * permission
     */
    @Override
    public void onRequestPermissionsResult(int requestCode, @NonNull String[] permissions, @NonNull int[] grantResults) {
        super.onRequestPermissionsResult(requestCode, permissions, grantResults);
        location.onRequestPermissionsResult(requestCode, grantResults);
    }
}
