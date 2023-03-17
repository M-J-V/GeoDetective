package com.example.geodetective;

import android.app.Activity;
import android.content.Context;
import android.content.Intent;
import android.content.pm.PackageManager;
import android.graphics.Bitmap;
import android.graphics.Canvas;
import android.graphics.drawable.AdaptiveIconDrawable;
import android.graphics.drawable.BitmapDrawable;
import android.graphics.drawable.Drawable;
import android.net.Uri;
import android.os.Build;
import android.os.Bundle;
import android.provider.MediaStore;
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
import androidx.core.app.ActivityCompat;

import com.google.android.gms.location.FusedLocationProviderClient;
import com.google.android.gms.location.LocationServices;
import com.google.android.gms.location.Priority;
import com.google.android.gms.tasks.CancellationToken;
import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.OnTokenCanceledListener;
import com.google.android.gms.tasks.Task;
import com.google.firebase.firestore.DocumentSnapshot;

import org.w3c.dom.Text;

import java.io.ByteArrayOutputStream;
import java.io.IOException;

public class CreateQuestActivity extends AppCompatActivity {

    private static final int CAMERA_REQUEST = 1888;
    private static final int SELECT_PICTURE = 200;
    private ImageView questImage;
    private Location location = new Location();

    private Uri imageUri = null;

    DbConnection db = DbConnection.getInstance();
    ActiveUser user = ActiveUser.getInstance();
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
        chooseImageBtn.setOnClickListener(v -> selectImage());

        //Get current location
        updateLocation();

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
                    err = "Please take add a photo to your Quest.";
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
                ByteArrayOutputStream baos = new ByteArrayOutputStream();
                bitmap.compress(Bitmap.CompressFormat.JPEG, 100, baos);
                byte[] data = baos.toByteArray();

                addQuest(title, desc, hint, creator, data, longitude, latitude, errorMsg);
            }

            errorMsg.setText(err);

        });
    }


    // TODO use quest class instead of multiple parameters
    public void addQuest(String title, String desc, String hint, String creator, byte[] bitmapData, double longitude, double latitude, TextView errorMsg) {
        db.quests.document(title).get().addOnCompleteListener(new OnCompleteListener<DocumentSnapshot>() {
            @Override
            public void onComplete(@NonNull Task<DocumentSnapshot> task){
                String err = "";
                if (task.isSuccessful()) {
                    DocumentSnapshot User = task.getResult();
                    if (User.exists()) {
                        err = "Quest Title already in use";
                    } else {
                        Toast.makeText(getApplicationContext(), "Starting upload", Toast.LENGTH_SHORT).show();
                        db.createNewQuest(title, desc, hint, creator, bitmapData, longitude, latitude,getApplicationContext());
                        startActivity(new Intent(getApplicationContext(), HomeActivity.class));
                    }
                } else {
                    err = "Error getting data from Database";
                }
                errorMsg.setText(err);
            }
        });
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
            this.location.setLongitude(location.getLongitude());
            this.location.setLatitude(location.getLatitude());
        });
    }

    // Select image from gallery or take a photo.
    private void selectImage() {
        final CharSequence[] options = {"Take Photo", "Choose from Gallery"};
        AlertDialog.Builder builder = new AlertDialog.Builder(CreateQuestActivity.this);
        builder.setTitle("Choose picture!");
        builder.setItems(options, (dialog, item) -> {
            if (options[item].equals("Take Photo")) {
                //Take photo
                Intent cameraIntent = new Intent(MediaStore.ACTION_IMAGE_CAPTURE);
                //noinspection deprecation
                startActivityForResult(cameraIntent, CAMERA_REQUEST);
            } else if (options[item].equals("Choose from Gallery")) {
                //Pick from gallery
                Intent i = new Intent();
                i.setType("image/*");
                i.setAction(Intent.ACTION_GET_CONTENT);

                // pass the constant to compare it
                // with the returned requestCode
                //noinspection deprecation
                startActivityForResult(Intent.createChooser(i, "Select Picture"), SELECT_PICTURE);
            }
        });
        builder.show();
    }

    // Get image from camera or gallery
    protected void onActivityResult(int requestCode, int resultCode, Intent data) {
        super.onActivityResult(requestCode, resultCode, data);
        if (requestCode == CAMERA_REQUEST && resultCode == Activity.RESULT_OK) {
            Bitmap photo = (Bitmap) data.getExtras().get("data");
            questImage.setImageBitmap(photo);
        }

        if (requestCode == SELECT_PICTURE && resultCode == Activity.RESULT_OK) {
            Bitmap photo;
            try {
                photo = MediaStore.Images.Media.getBitmap(this.getContentResolver(), data.getData());
            } catch (IOException e) {
                e.printStackTrace();
                return;
            }
            questImage.setImageBitmap(photo);
        }
    }

    // Convert drawable to bitmap
    @NonNull
    static private Bitmap getBitmapFromDrawable(@NonNull Drawable drawable) {
        final Bitmap bmp = Bitmap.createBitmap(drawable.getIntrinsicWidth(), drawable.getIntrinsicHeight(), Bitmap.Config.ARGB_8888);
        final Canvas canvas = new Canvas(bmp);
        drawable.setBounds(0, 0, canvas.getWidth(), canvas.getHeight());
        drawable.draw(canvas);
        return bmp;
    }

    // Handle permission request result
    @Override
    public void onRequestPermissionsResult(int requestCode, @NonNull String[] permissions, @NonNull int[] grantResults) {
        super.onRequestPermissionsResult(requestCode, permissions, grantResults);
        if (requestCode == 1) {
            if (!(grantResults.length > 0 && grantResults[0] == PackageManager.PERMISSION_GRANTED)) {
                // permission not granted, show a message to the user and disable the feature
                AlertDialog.Builder builder = new AlertDialog.Builder(this);
                builder.setTitle("Permission denied");
                builder.setMessage("You need to grant location permission to use this feature, please grant it in the settings.");
                builder.setOnDismissListener(dialog -> {
                    // Call finish() after the dialog is dismissed
                    finish();
                });
                builder.show();
            }

        }
    }

}
