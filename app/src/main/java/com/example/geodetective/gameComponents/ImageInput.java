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

/**
 * The ImageInput class handles selecting and compressing images from the camera or gallery, checking
 * for necessary permissions, and displaying the selected image in an ImageView.
 */
public class ImageInput {

    private static final int CAMERA_REQUEST = 1888;
    private static final int SELECT_PICTURE = 200;
    private final Activity activity;

    private UserPreferences preferences;

    public ImageInput(Activity activity) {
        this.activity = activity;
        askPermissions(false);
    }

    /**
     * The function asks for camera and gallery permissions and stores the user's response in
     * preferences.
     *
     * @param triedToUseResourceWithoutPermission A boolean value indicating whether the user has
     * already tried to use a resource (such as the camera or gallery) without having the necessary
     * permissions.
     */
    public void askPermissions(boolean triedToUseResourceWithoutPermission) {
        preferences = UserPreferences.getInstance(activity);

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

    /**
     * The function allows the user to select an image from either the camera or gallery, but first
     * checks if the necessary permissions have been granted.
     */
    public void selectImage() {
        preferences = UserPreferences.getInstance(activity);

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

    /**
     * This function handles the result of either taking a photo or selecting an image from the
     * gallery, compresses the image, checks if the image size is too large, and sets the image to an
     * ImageView if it is not too large.
     *
     * @param requestCode An integer code that identifies the type of request being made. It is used to
     * distinguish between different types of requests when the result is returned.
     * @param resultCode The result code returned by the activity that was started for result using
     * startActivityForResult(). It indicates whether the operation was successful or not.
     * @param data The Intent data parameter is the data returned from the activity launched for a
     * result. It can contain various types of data, depending on the activity that was launched and
     * the data that was requested. In this case, it is used to retrieve the image data captured by the
     * camera or selected from the gallery.
     * @param questImage An ImageView object that will display the selected image.
     * @param context The context parameter is a reference to the current context of the application.
     * It is used to access resources and services related to the application, such as Toast messages
     * and the content resolver.
     */
    public void onActivityResult(int requestCode, int resultCode, Intent data, ImageView questImage, Context context) {
        if (requestCode == CAMERA_REQUEST && resultCode == Activity.RESULT_OK) {
            Bitmap photo = (Bitmap) data.getExtras().get("data");
            ByteArrayOutputStream bAOS = new ByteArrayOutputStream();

            photo.compress(Bitmap.CompressFormat.JPEG, 90, bAOS);
            byte[] imageInByte = bAOS.toByteArray();
            float imageSize = (float) imageInByte.length/1000;
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
                ByteArrayOutputStream bAOS = new ByteArrayOutputStream();

                photo.compress(Bitmap.CompressFormat.JPEG, 90, bAOS);
                byte[] imageInByte = bAOS.toByteArray();
                float imageSize = (float) imageInByte.length/1000;
                if (imageSize > 3000) {
                    Toast.makeText(context, "Image is too large", Toast.LENGTH_LONG).show();
                } else {
                    questImage.setImageBitmap(photo);
                }
            } catch (IOException e) {
                e.printStackTrace();
            }
        }
    }
}

