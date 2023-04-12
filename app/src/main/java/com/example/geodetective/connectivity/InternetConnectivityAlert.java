package com.example.geodetective.connectivity;

import android.app.Activity;
import android.content.Context;
import android.content.Intent;
import android.os.Bundle;
import android.widget.Toast;

import androidx.appcompat.app.AlertDialog;

import com.example.geodetective.activities.LoginActivity;

/**
 * The InternetConnectivityAlert class creates a dialog box that prompts the user to establish an
 * internet connection before using the app.
 */
public class InternetConnectivityAlert extends Activity {
    /**
     * The function creates an alert dialog that prompts the user to establish an internet connection
     * to use the app, with options to try again or exit the app.
     *
     * @param context The context parameter is a reference to the current state of the application or
     * activity. It provides access to resources, preferences, and other application-specific
     * information. In this case, it is used to create an AlertDialog and to check for internet
     * connectivity.
     */
    private void createDialog(Context context) {
        AlertDialog.Builder builder = new AlertDialog.Builder(context);
        builder.setTitle("No internet connection");
        builder.setMessage("You need an internet connection to use this app.");
        ConnectivityChecker.hasInternetConnection(context);
        builder.setPositiveButton("Try again", (dialog, which) -> {
            if (ConnectivityChecker.hasInternetConnection(context)) {
                Toast.makeText(context, "Internet connection established", Toast.LENGTH_SHORT).show();
                dialog.dismiss();
                startActivity(new Intent(getApplicationContext(), LoginActivity.class));
            } else {
                Toast.makeText(context, "No internet connection", Toast.LENGTH_SHORT).show();
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
