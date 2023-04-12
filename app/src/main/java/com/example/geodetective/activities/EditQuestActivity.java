package com.example.geodetective.activities;

import android.annotation.SuppressLint;
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

import androidx.annotation.NonNull;
import androidx.appcompat.app.AlertDialog;
import androidx.appcompat.app.AppCompatActivity;

import com.example.geodetective.R;
import com.example.geodetective.gameComponents.ImageInput;
import com.example.geodetective.gameComponents.Location;
import com.example.geodetective.gameComponents.Quest;
import com.example.geodetective.singletons.ActiveQuest;
import com.example.geodetective.singletons.ActiveUser;
import com.example.geodetective.singletons.DbConnection;
import com.google.firebase.firestore.DocumentSnapshot;
import com.google.firebase.firestore.Query;

/*

Note: This class contains a lot of code that is similar/identical to the CreateQuestActivity class.
These classes can be merged together to reduce code duplication, and was implemented in commit ca398e54.
However, this was reverted in commit 670e04a1 because there were plans to change EditQuestActivity to an extend where merging would not be practical.
Due to time constraints, these plans are not implemented, and the code duplication remains.

 */

/**
 * The EditQuestActivity class allows users to edit an existing quest by filling in input fields,
 * selecting an image, and submitting or deleting the quest.
 */
public class EditQuestActivity extends AppCompatActivity {
    private final DbConnection db = DbConnection.getInstance();
    private final ActiveUser user = ActiveUser.getInstance();
    private final ActiveQuest activeQuest = ActiveQuest.getInstance();
    private Location location;
    private ImageInput imageInput;
    private ImageView questImage;
    private EditText questName;
    private EditText questDescription;
    private EditText questHint;
    private TextView errorMsg;

    /**
     This method sets up the Edit Quest activity where users can edit an existing Quest. It initializes various UI components such as buttons, text inputs, and error messages;
     It also sets up the functionality of these UI components including selecting an image, filling input fields, and submitting or deleting a Quest.
     @param savedInstanceState A saved instance state of the activity, which can be null.
     */
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_edit_quest);

        location = activeQuest.getQuest().getLocation();
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

        fillInputFields(activeQuest);

        submitQuestBtn.setOnClickListener(view -> {
            AlertDialog.Builder builder = new AlertDialog.Builder(this);
            builder.setTitle("Are you sure you want to edit this Quest?");
            if (!questName.getText().toString().equals(activeQuest.getQuest().getName())){
                builder.setMessage("Changing the Quest Name will delete attempts made so far on this Quest.");
            }
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

    /**
     * The function deletes a quest from the database and disconnects the active quest before starting
     * the HomeActivity.
     */
    private void deleteQuest() {
        db.deleteQuest(activeQuest.getQuest());
       activeQuest.disconnectActiveQuest();
        startActivity(new Intent(getApplicationContext(), HomeActivity.class));
    }

    /**
     * The function checks if a new quest name is valid and unique before uploading it to a database.
     */
    @SuppressLint("SetTextI18n")
    private void checkAndUploadQuest() {
        String newQuestName = questName.getText().toString().trim();

        if(newQuestName.equals("")) {
            errorMsg.setText("Please enter a Quest Title");
        } else {
            db.quests.document(newQuestName).get().addOnCompleteListener(task -> {
                String previousName = activeQuest.getQuest().getName();
                if (task.isSuccessful()) {
                    DocumentSnapshot Quest = task.getResult();
                    if (newQuestName.compareTo(previousName) == 0 || !Quest.exists()) {
                        uploadQuest();
                    } else {
                        errorMsg.setText("A quest with this name already exists.");
                    }
                }
            });
        }
    }

    /**
     * The function uploads a quest with a title, description, hint, creator, and image, and displays
     * an error message if any of the required fields are missing.
     */
    private void uploadQuest() {
        String title = questName.getText().toString().trim();
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
            replaceQuest(activeQuest.getQuest(), newQuest);
        }

        errorMsg.setText(err);
    }

    /**
     * The function fills input fields with data from an ActiveQuest object.
     *
     * @param quest The parameter "quest" is an object of type ActiveQuest, which contains information
     * about a quest that is currently active in the application.
     */
    private void fillInputFields(ActiveQuest quest) {
        questName.setText(quest.getQuest().getName());
        questDescription.setText(quest.getQuest().getDescription());
        questHint.setText(quest.getQuest().getHint());
        questImage.setImageBitmap(quest.getQuest().getImage());
    }

    /**
     * This function replaces a previous quest with a new quest, deleting the old quest image and any
     * attempts on the old quest name if it has changed.
     *
     * @param previousQuest The Quest object that is being replaced by the newQuest object.
     * @param newQuest The new quest that will replace the previous quest.
     */
    private void replaceQuest(Quest previousQuest, Quest newQuest) {
        // Delete quest image too
        db.storage.child("questImages").child(previousQuest.getName()).delete();

        // Delete attempts on old questName if changed
        if(!previousQuest.getName().equals(newQuest.getName())) {
            Query questAttempts = db.attempts.whereEqualTo("Quest", previousQuest.getName());
            db.deleteAttempts(questAttempts);
        }

        // Replace quest
        db.quests.document(previousQuest.getName()).delete().addOnSuccessListener(aVoid -> {
            db.updateQuestListAndCreate(previousQuest.getName(), newQuest, getApplicationContext());

            activeQuest.setQuest(newQuest);

        })
        .addOnFailureListener(e -> {
            String Msg = "Error when removing old Quest";
            errorMsg.setText(Msg);
        });
    }

    /**
     * This function calls the onActivityResult method of the imageInput object with the given
     * parameters.
     *
     * @param requestCode requestCode is an integer value that is used to identify the request made by
     * the calling activity. It is passed as a parameter to the startActivityForResult() method when
     * the calling activity starts the target activity. The same value is returned to the calling
     * activity when the target activity finishes and calls the setResult() method. This
     * @param resultCode resultCode is an integer value that represents the result of the activity that
     * was started for a particular request code. It can have one of three values: RESULT_OK,
     * RESULT_CANCELED, or any custom result code set by the activity. RESULT_OK indicates that the
     * activity completed successfully, RESULT_CANCELED indicates that
     * @param data The `data` parameter in the `onActivityResult` method is an `Intent` object that
     * contains the result data returned from the activity launched by the `startActivityForResult`
     * method. This data can include information such as the user's selected image, text input, or
     * other data depending on the activity
     */
    protected void onActivityResult(int requestCode, int resultCode, Intent data) {
        super.onActivityResult(requestCode, resultCode, data);
        imageInput.onActivityResult(requestCode, resultCode, data, questImage, getApplicationContext());
    }

    /**
     * This function handles the result of a permission request for location access.
     *
     * @param requestCode An integer code that identifies the request for permission. This code is used
     * to match the request with the response.
     * @param permissions An array of permissions requested by the app. Each permission is represented
     * as a string.
     * @param grantResults grantResults is an integer array that contains the results of the permission
     * requests. Each element in the array corresponds to the permission request at the same index in
     * the permissions array. The value of the element can be either PackageManager.PERMISSION_GRANTED
     * or PackageManager.PERMISSION_DENIED, depending on whether the user granted or denied the
     * permission
     */
    @Override
    public void onRequestPermissionsResult(int requestCode, @NonNull String[] permissions, @NonNull int[] grantResults) {
        super.onRequestPermissionsResult(requestCode, permissions, grantResults);
        if(requestCode == Location.PERMISSIONS_REQUEST){
            location.onRequestPermissionsResult(requestCode, grantResults);
        }
    }
}
