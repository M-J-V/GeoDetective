package com.example.geodetective;

import android.content.BroadcastReceiver;
import android.content.Context;
import android.content.Intent;
import android.net.ConnectivityManager;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AlertDialog;

public class ConnectivityChecker extends BroadcastReceiver {


    public static boolean hasInternetConnection(@NonNull Context context) {
        ConnectivityManager cm = (ConnectivityManager) context.getSystemService(Context.CONNECTIVITY_SERVICE);
        return cm.getActiveNetworkInfo() != null && cm.getActiveNetworkInfo().isConnected();
    }
    public static void createDialog(Context context) {
        AlertDialog.Builder builder = new AlertDialog.Builder(context);

        builder.setTitle("No internet connection");
        builder.setMessage("You need an internet connection to use this app.");
        builder.setOnDismissListener(dialog -> System.exit(0));

        builder.setPositiveButton("Try again", (dialog, which) -> {
            if (hasInternetConnection(context))
                dialog.dismiss();
            else {
                dialog.dismiss();
                createDialog(context);
            }
        });
        builder.setNegativeButton("Exit", (dialog, which) -> {
            dialog.dismiss();
            System.exit(0);
        });

        builder.show();
    }

    @Override
    public void onReceive(Context context, Intent intent) {
        if (intent.getAction().equals("android.net.conn.CONNECTIVITY_CHANGE")) {
            if (intent.getBooleanExtra(ConnectivityManager.EXTRA_NO_CONNECTIVITY, false)) {
                createDialog(context);
            }
        }
    }
}
