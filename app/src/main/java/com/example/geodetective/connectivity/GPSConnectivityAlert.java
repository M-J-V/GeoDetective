package com.example.geodetective.connectivity;

import android.app.Activity;
import android.content.Context;
import android.content.Intent;
import android.os.Bundle;
import android.widget.Toast;

import androidx.appcompat.app.AlertDialog;

import com.example.geodetective.activities.LoginActivity;

/**
 * The GPSConnectivityAlert class creates a dialog that prompts the user to establish a GPS connection
 * before using the app.
 */
public class GPSConnectivityAlert extends Activity {

    /**
     * The function creates an alert dialog with options to try again or exit the app based on the
     * availability of GPS connection.
     *
     * @param context The context parameter is a reference to the current state of the application or
     * activity. It provides access to resources, preferences, and other application-specific
     * information. In this case, it is used to display the dialog box and check for GPS connectivity.
     */
    private void createDialog(Context context) {
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

    /**
     * This function creates a dialog in the onCreate method of an Android activity.
     *
     * @param savedInstanceState savedInstanceState is a Bundle object that contains the previous state
     * of the activity. It is used to restore the activity to its previous state in case it is
     * destroyed and recreated, such as during a configuration change (e.g. screen rotation) or when
     * the system needs to free up memory. The savedInstanceState bundle
     */
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        createDialog(this);
    }
}
