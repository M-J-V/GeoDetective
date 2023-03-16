package com.example.geodetective;

import android.annotation.SuppressLint;
import android.content.Intent;
import android.content.pm.PackageManager;
import android.graphics.Bitmap;
import android.graphics.Canvas;
import android.graphics.Color;
import android.graphics.drawable.Drawable;
import android.location.Location;
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
import androidx.core.app.ActivityCompat;

import com.google.android.gms.location.FusedLocationProviderClient;
import com.google.android.gms.location.LocationServices;
import com.google.android.gms.location.Priority;
import com.google.android.gms.tasks.CancellationToken;
import com.google.android.gms.tasks.OnTokenCanceledListener;

import java.util.Locale;

//TODO change name, since we merged the join activity and the joined quest activity quest overview activity is not a good name anymore.
//TODO make page scrollable, longer descriptions will not fit on the screen.
//TODO hint button should be implemented
//TODO implement edit button
//TODO ask permissions first!

// TODO Bug when uploading image for quest from gallery
public class QuestOverviewActivity extends AppCompatActivity {
    ActiveQuest activeQuest = ActiveQuest.getInstance();
    private boolean isQuestStarted = false;
    private boolean isQuestCompleted = false;

    private double longitude = 0;
    private double latitude = 0;

    private double questLongitude;
    private double questLatitude;

    private TextView timer;

    // Convert drawable to bitmap
    @NonNull
    static private Bitmap getBitmapFromDrawable(@NonNull Drawable drawable) {
        final Bitmap bmp = Bitmap.createBitmap(drawable.getIntrinsicWidth(), drawable.getIntrinsicHeight(), Bitmap.Config.ARGB_8888);
        final Canvas canvas = new Canvas(bmp);
        drawable.setBounds(0, 0, canvas.getWidth(), canvas.getHeight());
        drawable.draw(canvas);
        return bmp;
    }

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
        TextView imageClue = findViewById(R.id.image_hint);
        TextView questDescription = findViewById(R.id.quest_description);
        TextView questName = findViewById(R.id.quest_name);

        String questNameValue = getIntent().getStringExtra("questName");
        String questDescriptionValue = getIntent().getStringExtra("questDescription");
        String questCreatorValue = getIntent().getStringExtra("creator");
        questLongitude = getIntent().getDoubleExtra("longitude", 0);
        questLatitude = getIntent().getDoubleExtra("latitude", 0);
        String questHintValue = "";

        editButton.setOnClickListener(v -> {
            String nameOfUser = ActiveUser.getInstance().getUsername();
            String nameOfCreator = activeQuest.getQuest().getCreator();
            if(nameOfCreator.compareTo(nameOfUser) == 0) {
                startActivity(new Intent(getApplicationContext(), EditQuestActivity.class));
            }
        });

        questName.setText(questNameValue);
        questDescription.setText(questDescriptionValue);
        questImage.setImageBitmap(activeQuest.getQuest().getImage());

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
        updateLocation();

        float[] results = new float[1];
        Location.distanceBetween(latitude, longitude, questLatitude, questLongitude, results);
        Log.d("WOAH","lat: " + latitude);
        Log.d("WOAH","long: " + longitude);
        Log.d("WOAH","questLat: " + questLatitude);
        Log.d("WOAH","questLong: " + questLongitude);

        float distance = results[0];

        if(distance < 100) {
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

    private void updateLocation() {
        //Create location client
        FusedLocationProviderClient fusedLocationClient = LocationServices.getFusedLocationProviderClient(this);

        //Check if permission is granted
        if (ActivityCompat.checkSelfPermission(this, android.Manifest.permission.ACCESS_FINE_LOCATION) != PackageManager.PERMISSION_GRANTED && ActivityCompat.checkSelfPermission(this, android.Manifest.permission.ACCESS_COARSE_LOCATION) != PackageManager.PERMISSION_GRANTED) {
            // Send permissions request
            ActivityCompat.requestPermissions(this, new String[]{android.Manifest.permission.ACCESS_FINE_LOCATION}, 1);
            return;
        }

        //Get current location
        fusedLocationClient.getCurrentLocation(Priority.PRIORITY_HIGH_ACCURACY, new CancellationToken() {
            @SuppressWarnings("ConstantConditions")
            @NonNull
            @Override
            public CancellationToken onCanceledRequested(@NonNull OnTokenCanceledListener onTokenCanceledListener) {
                return null;
            }

            @Override
            public boolean isCancellationRequested() {
                return false;
            }
        }).addOnSuccessListener(location -> {
            //Set longitude and latitude
            longitude = location.getLongitude();
            latitude = location.getLatitude();
        });
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
}
