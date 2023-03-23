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

public class CreateQuestActivity extends AppCompatActivity {
    private Location location;
    private ImageInput imageInput;
    DbConnection db = DbConnection.getInstance();
    ActiveUser user = ActiveUser.getInstance();
    private ImageView questImage;
    private EditText questName;
    private EditText questDescription;
    private EditText questHint;
    private TextView errorMsg;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_create_quest);

        location = new Location(this);
        imageInput = new ImageInput(this);
        // Get image from activity
        questImage = findViewById(R.id.Quest_Image);
        questImage.setDrawingCacheEnabled(true);

        // Get buttons from activity
        Button chooseImageBtn = findViewById(R.id.choose_Quest_Image_Btn);
        Button submitQuestBtn = findViewById(R.id.submit_quest_btn);
        ImageButton backBtn = findViewById(R.id.BackBtn);

        // get text inputs from activity
        questName = findViewById(R.id.quest_name_input);
        questDescription = findViewById(R.id.quest_description_input);
        questHint = findViewById(R.id.quest_hint_input);

        // get error text view from activity
        errorMsg = findViewById(R.id.errorText);

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
        //location.updateCurrentLocation();

//        shouldReplaceQuest = getIntent().getExtras().getBoolean("replace");

//        if(shouldReplaceQuest) {
//            fillInputFields(ActiveQuest.getInstance());
//        }

        submitQuestBtn.setOnClickListener(view -> uploadQuest());
    }

    //TODO authenticate that the quest is valid, title not already used, non empty desc
    private void uploadQuest() {
        // update location again
        location.updateCurrentLocation();

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
            Quest newQuest = new Quest(title, creator, desc, hint, bitmap, location);

            addQuest(newQuest);
        }

        errorMsg.setText(err);
    }


    // TODO use quest class instead of multiple parameters
    public void addQuest(Quest newQuest) {
        db.quests.document(newQuest.getName()).get().addOnCompleteListener(task -> {
            String err = "";
            if (task.isSuccessful()) {
                DocumentSnapshot User = task.getResult();
                if (User.exists()) {
                    err = "Quest Title already in use";
                } else {
                    Toast.makeText(getApplicationContext(), "Starting upload", Toast.LENGTH_SHORT).show();

                    db.createNewQuest(newQuest,getApplicationContext());
                    startActivity(new Intent(getApplicationContext(), HomeActivity.class));
                }
            } else {
                err = "Error getting data from Database";
            }
            errorMsg.setText(err);
        });
    }

//    private void replaceQuest(Quest previousQuest, Quest newQuest) {
//        db.quests.document(previousQuest.getName()).delete().addOnSuccessListener(aVoid -> {
//                    ByteArrayOutputStream baos = new ByteArrayOutputStream();
//                    newQuest.getImage().compress(Bitmap.CompressFormat.JPEG, 100, baos);
//                    byte[] data = baos.toByteArray();
//
//                    db.updateQuestListAndCreate(previousQuest.getName(), newQuest.getName(), newQuest.getDescription(), newQuest.getHint(), ActiveUser.getInstance().getUsername(), getApplicationContext() , data, location, errorMsg);
//
//                    ActiveQuest.getInstance().setQuest(newQuest);
//
//                })
//                .addOnFailureListener(e -> {
//                    String Msg = "Error when removing old Quest";
//                    errorMsg.setText(Msg);
//                });
//    }

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
