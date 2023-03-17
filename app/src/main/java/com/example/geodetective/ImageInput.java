package com.example.geodetective;

import android.app.Activity;
import android.content.Intent;
import android.graphics.Bitmap;
import android.provider.MediaStore;
import android.widget.ImageView;

import androidx.appcompat.app.AlertDialog;

import java.io.IOException;

public class ImageInput {

    private static final int CAMERA_REQUEST = 1888;
    private static final int SELECT_PICTURE = 200;
    private final Activity activity;

    public ImageInput(Activity activity) {
        this.activity = activity;
    }

    // Select image from gallery or take a photo.
    public void selectImage() {
        final CharSequence[] options = {"Take Photo", "Choose from Gallery"};
        AlertDialog.Builder builder = new AlertDialog.Builder(activity);
        builder.setTitle("Choose picture!");
        builder.setItems(options, (dialog, item) -> {
            if (options[item].equals("Take Photo")) {
                //Take photo
                Intent cameraIntent = new Intent(MediaStore.ACTION_IMAGE_CAPTURE);
                activity.startActivityForResult(cameraIntent, CAMERA_REQUEST);
            } else if (options[item].equals("Choose from Gallery")) {
                //Pick from gallery
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

