package com.example.geodetective;

import android.app.Activity;
import android.content.Context;
import android.os.Bundle;
import android.widget.Toast;

import androidx.appcompat.app.AlertDialog;

public class ConnectivityAlert extends Activity {
    public void createDialog(Context context) {
        AlertDialog.Builder builder = new AlertDialog.Builder(context);

        builder.setTitle("No internet connection");
        builder.setMessage("You need an internet connection to use this app.");

        builder.setPositiveButton("Try again", (dialog, which) -> {
            if (ConnectivityChecker.hasInternetConnection(context)) {
                Toast.makeText(context, "Internet connection established", Toast.LENGTH_SHORT).show();
                dialog.dismiss();
                this.finish();
            } else {
                Toast.makeText(context, "Still no internet connection", Toast.LENGTH_SHORT).show();
                dialog.dismiss();
                createDialog(context);
            }
        });
        builder.setNegativeButton("Exit app", (dialog, which) -> {
            this.finishAffinity();
            System.exit(0);
        });

        AlertDialog dialog = builder.create();
        dialog.setCanceledOnTouchOutside(false);
        dialog.setCancelable(false);
        dialog.show();
    }

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        createDialog(this);
    }
}
