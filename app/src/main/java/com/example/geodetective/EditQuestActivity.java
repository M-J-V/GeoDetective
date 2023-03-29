package com.example.geodetective;

import android.content.Intent;
import android.graphics.Bitmap;
import android.graphics.drawable.AdaptiveIconDrawable;
import android.graphics.drawable.BitmapDrawable;
import android.os.Build;
import android.os.Bundle;
import android.util.Log;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ImageButton;
import android.widget.ImageView;
import android.widget.TextView;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AlertDialog;
import androidx.appcompat.app.AppCompatActivity;

import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.Task;
import com.google.firebase.firestore.DocumentSnapshot;

import java.io.ByteArrayOutputStream;

public class EditQuestActivity extends AppCompatActivity {
    private Location location;
    private  ImageInput imageInput;
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
        setContentView(R.layout.activity_edit_quest);

        location = ActiveQuest.getInstance().getQuest().getLocation();
        imageInput = new ImageInput(this);
        // Get image from activity
        questImage = findViewById(R.id.Quest_Image);
        questImage.setDrawingCacheEnabled(true);

        // Get buttons from activity
        Button chooseImageBtn = findViewById(R.id.choose_Quest_Image_Btn);
        Button submitQuestBtn = findViewById(R.id.submit_quest_btn);
        Button deleteBtn = findViewById(R.id.deleteButton);
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

        fillInputFields(ActiveQuest.getInstance());

        submitQuestBtn.setOnClickListener(view -> {
            AlertDialog.Builder builder = new AlertDialog.Builder(this);
            builder.setTitle("Are you sure you want to edit this Quest?");
            builder.setMessage("Data and attempts connected to old quest details will not be updated");
            builder.setPositiveButton("Yes", (dialog, which) -> checkAndUploadQuest());
            builder.setNegativeButton("No", (dialog, which) -> dialog.dismiss()).show();

        });


        deleteBtn.setOnClickListener(view -> {
            AlertDialog.Builder builder = new AlertDialog.Builder(this);
            builder.setTitle("Are you sure you want to delete this Quest?");
            builder.setPositiveButton("Yes", (dialog, which) -> deleteQuest());
            builder.setNegativeButton("No", (dialog, which) -> dialog.dismiss()).show();
        });
    }

    private void deleteQuest() {
        db.deleteQuest(ActiveQuest.getInstance().getQuest());
        ActiveQuest.getInstance().disconnectActiveQuest();
        startActivity(new Intent(getApplicationContext(), HomeActivity.class));
    }

    private void checkAndUploadQuest() {
        String err = "";
        String newQuestName = questName.getText().toString();

        if(newQuestName.equals("")) {
            errorMsg.setText("Please enter a Quest Title");
        } else {
            db.quests.document(newQuestName).get().addOnCompleteListener(new OnCompleteListener<DocumentSnapshot>() {
                @Override
                public void onComplete(@NonNull Task<DocumentSnapshot> task) {
                    String err = "";
                    String previousName = ActiveQuest.getInstance().getQuest().getName();
                    if (task.isSuccessful()) {
                        DocumentSnapshot Quest = task.getResult();
                        if (newQuestName.compareTo(previousName) == 0 || !Quest.exists()) {
                            uploadQuest();
                        } else {
                            errorMsg.setText("A quest with this name already exists.");
                        }
                    }
                }
            });
        }
    }

    private void uploadQuest() {
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
            replaceQuest(ActiveQuest.getInstance().getQuest(), newQuest);
        }

        errorMsg.setText(err);
    }

    private void fillInputFields(ActiveQuest quest) {
        questName.setText(quest.getQuest().getName());
        questDescription.setText(quest.getQuest().getDescription());
        questHint.setText(quest.getQuest().getHint());
        questImage.setImageBitmap(quest.getQuest().getImage());
    }

    private void replaceQuest(Quest previousQuest, Quest newQuest) {
        // Delete quest image too
        db.storage.child("questImages").child(previousQuest.getName()).delete();

        // Replace quest
        db.quests.document(previousQuest.getName()).delete().addOnSuccessListener(aVoid -> {
            db.updateQuestListAndCreate(previousQuest.getName(), newQuest, getApplicationContext());

            ActiveQuest.getInstance().setQuest(newQuest);

        })
        .addOnFailureListener(e -> {
            String Msg = "Error when removing old Quest";
            errorMsg.setText(Msg);
        });
    }

    // Get image from camera or gallery
    protected void onActivityResult(int requestCode, int resultCode, Intent data) {
        super.onActivityResult(requestCode, resultCode, data);
        imageInput.onActivityResult(requestCode, resultCode, data, questImage, getApplicationContext());
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
