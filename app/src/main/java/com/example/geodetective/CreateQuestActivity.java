package com.example.geodetective;

import android.content.Intent;
import android.graphics.Bitmap;
import android.graphics.drawable.AdaptiveIconDrawable;
import android.graphics.drawable.BitmapDrawable;
import android.os.Build;
import android.os.Bundle;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ImageButton;
import android.widget.ImageView;
import android.widget.TextView;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;

import com.google.firebase.firestore.DocumentSnapshot;

import java.io.ByteArrayOutputStream;

//TODO Use scroll layout instead of constraint, because long descriptions/titles/images don't fit on screen.
public class CreateQuestActivity extends AppCompatActivity {
    private final Location location = new Location(this);
    private final ImageInput imageInput = new ImageInput(this);
    DbConnection db = DbConnection.getInstance();
    ActiveUser user = ActiveUser.getInstance();
    private ImageView questImage;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_create_quest);

        // Get image from activity
        questImage = findViewById(R.id.Quest_Image);
        questImage.setDrawingCacheEnabled(true);

        // Get buttons from activity
        Button chooseImageBtn = findViewById(R.id.choose_Quest_Image_Btn);
        Button submitQuestBtn = findViewById(R.id.submit_quest_btn);
        ImageButton backBtn = findViewById(R.id.BackBtn);

        // get text inputs from activity
        EditText questName = findViewById(R.id.quest_name_input);
        EditText questDescription = findViewById(R.id.quest_description_input);
        EditText questHint = findViewById(R.id.quest_hint_input);

        // get error text view from activity
        TextView errorMsg = findViewById(R.id.errorText);

        // Set back button functionality
        backBtn.setOnClickListener(v -> {
            // return to home activity
            super.onBackPressed();
        });

        // Select image from gallery or take a photo.
        chooseImageBtn.setOnClickListener(v -> imageInput.selectImage());

        // Request location permissions
        location.requestPermissions();

        // Get current location
        location.updateCurrentLocation();

        submitQuestBtn.setOnClickListener(view -> {

            String title = questName.getText().toString();
            String desc = questDescription.getText().toString();
            String hint = questHint.getText().toString();
            String creator = user.getUsername();

            String err = "";
            boolean validImageAndDesc = false;
            boolean emptyStrings = desc.equals("") || title.equals("");

            if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.O) {
                if (questImage.getDrawable() instanceof  AdaptiveIconDrawable) {
                    err = "Please take or add a photo to your Quest.";
                } else {
                    if (emptyStrings) {
                        err = "Please enter a Quest Description and Title";
                    } else {
                        validImageAndDesc = true;
                    }
                }
            } else {
                err = "Invalid Build Version SDK";
            }

            if(validImageAndDesc) {
                Bitmap bitmap = ((BitmapDrawable) questImage.getDrawable()).getBitmap();
                ByteArrayOutputStream bAOS = new ByteArrayOutputStream();
                bitmap.compress(Bitmap.CompressFormat.JPEG, 100, bAOS);
                byte[] data = bAOS.toByteArray();

                addQuest(title, desc, hint, creator, data, location, errorMsg);
            }

            errorMsg.setText(err);

        });
    }

    // TODO use quest class instead of multiple parameters
    public void addQuest(String title, String desc, String hint, String creator, byte[] bitmapData, Location location, TextView errorMsg) {
        db.quests.document(title).get().addOnCompleteListener(task -> {
            String err = "";
            if (task.isSuccessful()) {
                DocumentSnapshot User = task.getResult();
                if (User.exists()) {
                    err = "Quest Title already in use";
                } else {
                    Toast.makeText(getApplicationContext(), "Starting upload", Toast.LENGTH_SHORT).show();
                    db.createNewQuest(title, desc, hint, creator, bitmapData, location,getApplicationContext());
                    startActivity(new Intent(getApplicationContext(), HomeActivity.class));
                }
            } else {
                err = "Error getting data from Database";
            }
            errorMsg.setText(err);
        });
    }

    // Get image from camera or gallery
    protected void onActivityResult(int requestCode, int resultCode, Intent data) {
        super.onActivityResult(requestCode, resultCode, data);
        imageInput.onActivityResult(requestCode, resultCode, data, questImage);
    }

    // Handle permission request result
    @Override
    public void onRequestPermissionsResult(int requestCode, @NonNull String[] permissions, @NonNull int[] grantResults) {
        super.onRequestPermissionsResult(requestCode, permissions, grantResults);
        if(requestCode == Location.PERMISSIONS_REQUEST){
            location.onRequestPermissionsResult(requestCode, grantResults);
        }
    }

}
