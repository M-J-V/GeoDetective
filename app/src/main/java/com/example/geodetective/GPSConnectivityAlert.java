package com.example.geodetective;

import android.app.Activity;
import android.content.Context;
import android.content.Intent;
import android.os.Bundle;
import android.widget.Toast;

import androidx.appcompat.app.AlertDialog;

public class GPSConnectivityAlert extends Activity {

    public void createDialog(Context context) {
        AlertDialog.Builder builder = new AlertDialog.Builder(context);

        builder.setTitle("No GPS connection");
        builder.setMessage("You need a GPS connection to use this app.");
        ConnectivityChecker.hasGPSConnection(context);
        builder.setPositiveButton("Try again", (dialog, which) -> {
            if (ConnectivityChecker.hasGPSConnection(context)) {
                Toast.makeText(context, "GPS connection established", Toast.LENGTH_SHORT).show();
                dialog.dismiss();
                startActivity(new Intent(getApplicationContext(), LoginActivity.class));
            } else {
                Toast.makeText(context, "No GPS connection", Toast.LENGTH_SHORT).show();
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
