package com.example.geodetective.gameComponents;

import android.app.Activity;
import android.content.Context;
import android.content.Intent;
import android.graphics.Bitmap;
import android.provider.MediaStore;
import android.widget.ImageView;
import android.widget.Toast;

import androidx.appcompat.app.AlertDialog;

import com.example.geodetective.singletons.UserPreferences;

import java.io.ByteArrayOutputStream;
import java.io.IOException;

public class ImageInput {

    private static final int CAMERA_REQUEST = 1888;
    private static final int SELECT_PICTURE = 200;
    private final Activity activity;

    public ImageInput(Activity activity) {
        this.activity = activity;
        askPermissions(false);
    }

    private void askPermissions(boolean triedToUseResourceWithoutPermission) {
        //TODO Not both permissions should have to be granted, but is required now!
        UserPreferences preferences = UserPreferences.getInstance(activity);

        boolean permissionsAsked = preferences.contains("cameraPermissions") && preferences.contains("galleryPermissions");
        if(permissionsAsked && !triedToUseResourceWithoutPermission)
            return;

        final CharSequence[] options = {"Give both gallery and camera permissions", "Give camera permissions", "Give gallery permissions", "Don't give permissions"};
        AlertDialog.Builder builder = new AlertDialog.Builder(activity);
        builder.setTitle("Camera Permissions");
        builder.setItems(options, (dialog, item) -> {
            if (options[item].equals("Give both gallery and camera permissions")) {
                //Give both permissions
                preferences.putPreference("cameraPermissions", true);
                preferences.putPreference("galleryPermissions", true);
            } else if (options[item].equals("Give only camera permissions")) {
                //Give camera permission
                preferences.putPreference("cameraPermissions", true);
                preferences.putPreference("galleryPermissions", false);
            } else if (options[item].equals("Give only gallery permissions")) {
                //Give gallery permission
                preferences.putPreference("cameraPermissions", false);
                preferences.putPreference("galleryPermissions", true);
            } else if (options[item].equals("Don't give permissions")) {
                //Don't give permissions
                preferences.putPreference("cameraPermissions", false);
                preferences.putPreference("galleryPermissions", false);
            }
        });
        builder.show();
    }

    // Select image from gallery or take a photo.
    public void selectImage() {
        UserPreferences preferences = UserPreferences.getInstance(activity);

        AlertDialog.Builder permissionsBuilder = new AlertDialog.Builder(activity);
        permissionsBuilder.setTitle("Permissions missing");
        permissionsBuilder.setMessage("You need to give permissions to access your camera and/or gallery to add images to your quests.");
        permissionsBuilder.setPositiveButton("OK", (dialog, which) -> askPermissions(true));
        permissionsBuilder.setNegativeButton("Cancel", (dialog, which) -> dialog.dismiss());

        final CharSequence[] options = {"Take Photo", "Choose from Gallery"};
        AlertDialog.Builder builder = new AlertDialog.Builder(activity);
        builder.setTitle("Choose picture!");
        builder.setItems(options, (dialog, item) -> {
            if (options[item].equals("Take Photo")) {
                if(!preferences.getBoolean("cameraPermissions", false)) {
                    Toast.makeText(activity, "You need to give permissions to access your camera to add images to your quests.", Toast.LENGTH_LONG).show();
                    permissionsBuilder.show();
                    return;
                }
                Intent cameraIntent = new Intent(MediaStore.ACTION_IMAGE_CAPTURE);
                activity.startActivityForResult(cameraIntent, CAMERA_REQUEST);
            } else if (options[item].equals("Choose from Gallery")) {
                if(!preferences.getBoolean("galleryPermissions",false)) {
                    Toast.makeText(activity, "You need to give permissions to access your gallery to add images to your quests.", Toast.LENGTH_LONG).show();
                    permissionsBuilder.show();
                    return;
                }

                Intent i = new Intent();
                i.setType("image/*");
                i.setAction(Intent.ACTION_GET_CONTENT);

                activity.startActivityForResult(Intent.createChooser(i, "Select Picture"), SELECT_PICTURE);
            }
        });
        builder.show();
    }
    // Get image from camera or gallery
    public void onActivityResult(int requestCode, int resultCode, Intent data, ImageView questImage, Context context) {
        if (requestCode == CAMERA_REQUEST && resultCode == Activity.RESULT_OK) {
            Bitmap photo = (Bitmap) data.getExtras().get("data");
            ByteArrayOutputStream baos = new ByteArrayOutputStream();

            photo.compress(Bitmap.CompressFormat.JPEG, 90, baos);
            byte[] imageInByte = baos.toByteArray();
            float imageSize = imageInByte.length/1000;
            if (imageSize > 3000) {
                Toast.makeText(context, "Image is too large", Toast.LENGTH_LONG).show();
            } else {
                questImage.setImageBitmap(photo);
            }
            //questImage.setImageBitmap(photo);
        }

        if (requestCode == SELECT_PICTURE && resultCode == Activity.RESULT_OK) {
            Bitmap photo;
            try {
                photo = MediaStore.Images.Media.getBitmap(activity.getContentResolver(), data.getData());
                Toast.makeText(context, "Loading Image", Toast.LENGTH_LONG).show();
                ByteArrayOutputStream baos = new ByteArrayOutputStream();

                photo.compress(Bitmap.CompressFormat.JPEG, 90, baos);
                byte[] imageInByte = baos.toByteArray();
                float imageSize = imageInByte.length/1000;
                if (imageSize > 3000) {
                    Toast.makeText(context, "Image is too large", Toast.LENGTH_LONG).show();
                } else {
                    questImage.setImageBitmap(photo);
                }
            } catch (IOException e) {
                e.printStackTrace();
                return;
            }
            //questImage.setImageBitmap(photo);
        }
    }
}

