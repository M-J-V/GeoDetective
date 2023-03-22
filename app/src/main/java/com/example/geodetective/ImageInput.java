package com.example.geodetective;

import android.app.Activity;
import android.content.Context;
import android.content.Intent;
import android.content.SharedPreferences;
import android.graphics.Bitmap;
import android.provider.MediaStore;
import android.widget.ImageView;
import android.widget.Toast;

import androidx.appcompat.app.AlertDialog;

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
        SharedPreferences sharedPref = activity.getPreferences(Context.MODE_PRIVATE);
        SharedPreferences.Editor editor = sharedPref.edit();

        boolean permissionsAsked = sharedPref.contains("cameraPermissions") && sharedPref.contains("galleryPermissions");
        if(permissionsAsked && !triedToUseResourceWithoutPermission)
            return;

        final CharSequence[] options = {"Give both gallery and camera permissions", "Give camera permissions", "Give gallery permissions", "Don't give permissions"};
        AlertDialog.Builder builder = new AlertDialog.Builder(activity);
        builder.setTitle("Camera Permissions");
        builder.setItems(options, (dialog, item) -> {
            if (options[item].equals("Give both gallery and camera permissions")) {
                //Give both permissions
                editor.putBoolean("cameraPermissions", true);
                editor.putBoolean("galleryPermissions", true);
                editor.apply();
            } else if (options[item].equals("Give camera permissions")) {
                //Give camera permission
                editor.putBoolean("cameraPermissions", true);
                editor.putBoolean("galleryPermissions", false);
                editor.apply();
            } else if (options[item].equals("Give gallery permissions")) {
                //Give gallery permission
                editor.putBoolean("cameraPermissions", false);
                editor.putBoolean("galleryPermissions", true);
                editor.apply();
            } else if (options[item].equals("Don't give permissions")) {
                //Don't give permissions
                editor.putBoolean("cameraPermissions", false);
                editor.putBoolean("galleryPermissions", false);
                editor.apply();
            }
        });
        builder.show();
    }

    // Select image from gallery or take a photo.
    public void selectImage() {
        SharedPreferences sharedPref = activity.getPreferences(Context.MODE_PRIVATE);

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
                if(!sharedPref.getBoolean("cameraPermissions",false)) {
                    Toast.makeText(activity, "You need to give permissions to access your camera to add images to your quests.", Toast.LENGTH_LONG).show();
                    permissionsBuilder.show();
                    return;
                }

                Intent cameraIntent = new Intent(MediaStore.ACTION_IMAGE_CAPTURE);
                activity.startActivityForResult(cameraIntent, CAMERA_REQUEST);
            } else if (options[item].equals("Choose from Gallery")) {
                if(!sharedPref.getBoolean("galleryPermissions",false)) {
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
        protected void onActivityResult(int requestCode, int resultCode, Intent data, ImageView questImage) {
            if (requestCode == CAMERA_REQUEST && resultCode == Activity.RESULT_OK) {
                Bitmap photo = (Bitmap) data.getExtras().get("data");
                questImage.setImageBitmap(photo);
            }

            if (requestCode == SELECT_PICTURE && resultCode == Activity.RESULT_OK) {
                Bitmap photo;
                try {
                    photo = MediaStore.Images.Media.getBitmap(activity.getContentResolver(), data.getData());
                } catch (IOException e) {
                    e.printStackTrace();
                    return;
                }
                questImage.setImageBitmap(photo);
            }
        }
}

