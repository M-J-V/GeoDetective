package com.example.geodetective.activities;

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

import com.example.geodetective.R;
import com.example.geodetective.gameComponents.ImageInput;
import com.example.geodetective.gameComponents.Location;
import com.example.geodetective.gameComponents.Quest;
import com.example.geodetective.singletons.ActiveUser;
import com.example.geodetective.singletons.DbConnection;
import com.google.firebase.firestore.DocumentSnapshot;

/**
 * This is a Java class for creating a quest in an Android app, which includes functionality for
 * uploading a quest with a title, description, hint, image, and location.
 */
public class CreateQuestActivity extends AppCompatActivity {
    private final DbConnection db = DbConnection.getInstance();
    private final ActiveUser user = ActiveUser.getInstance();
    private Location location;
    private ImageInput imageInput;
    private ImageView questImage;
    private EditText questName;
    private EditText questDescription;
    private EditText questHint;
    private TextView errorMsg;

    private Quest questUpload;

    /**
     * This function sets up the activity for creating a quest, including initializing various UI
     * elements and setting their functionality.
     *
     * @param savedInstanceState savedInstanceState is a Bundle object that contains the activity's
     * previously saved state. It is used to restore the activity's state when it is recreated, such as
     * when the device is rotated or the activity is destroyed and recreated due to a configuration
     * change.
     */
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

        submitQuestBtn.setOnClickListener(view -> uploadQuest());
    }

    /**
     * The function uploads a quest with a title, description, hint, creator, and image, after checking
     * for valid input and updating the location.
     */
    private void uploadQuest() {
        // update location again
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
            questUpload = new Quest(title, creator, desc, hint, bitmap, location);

            location.updateCurrentLocation(this::addQuest);
        }

        errorMsg.setText(err);
    }


    /**
     * This function adds a new quest to a database and checks if the quest title already exists.
     *
     * @param location The location parameter is an object of the Location class that represents the
     * location of a quest being added. It is used to set the location of the questUpload object before
     * checking if the quest title already exists in the database.
     */
    public void addQuest(Location location) {
        questUpload.setLocation(location);
        db.quests.document(questUpload.getName()).get().addOnCompleteListener(task -> {
            String err = "";
            if (task.isSuccessful()) {
                DocumentSnapshot User = task.getResult();
                if (User.exists()) {
                    err = "Quest Title already in use";
                } else {
                    Toast.makeText(getApplicationContext(), "Starting upload", Toast.LENGTH_SHORT).show();

                    db.createNewQuest(questUpload,getApplicationContext());
                    startActivity(new Intent(getApplicationContext(), HomeActivity.class));
                }
            } else {
                err = "Error getting data from Database";
            }
            errorMsg.setText(err);
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
     * was started for a result. It can have one of three values: RESULT_OK, RESULT_CANCELED, or any
     * custom result code set by the activity that was started. RESULT_OK indicates that the activity
     * completed successfully, RESULT_CANCELED indicates
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
     * to match the request with the corresponding result.
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
