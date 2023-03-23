package com.example.geodetective;

import android.content.BroadcastReceiver;
import android.content.Context;
import android.content.Intent;
import android.net.ConnectivityManager;
import android.widget.Toast;

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

        builder.setPositiveButton("Try again", (dialog, which) -> {
            if (hasInternetConnection(context)) {
                Toast.makeText(context, "Internet connection established", Toast.LENGTH_SHORT).show();
                dialog.dismiss();
            } else {
                Toast.makeText(context, "Still no internet connection", Toast.LENGTH_SHORT).show();
                dialog.dismiss();
                createDialog(context);
            }
        });
        builder.setNegativeButton("Exit", (dialog, which) -> {
            dialog.dismiss();
            System.exit(0);
        });

        AlertDialog dialog = builder.create();
        dialog.setCanceledOnTouchOutside(false);
        dialog.setCancelable(false);
        dialog.show();
    }

    @Override
    public void onReceive(Context context, Intent intent) {
//        Toast.makeText(context, "Network state changed", Toast.LENGTH_SHORT).show();
        if (intent.getAction().equals("android.net.conn.CONNECTIVITY_CHANGE")) {
            if (intent.getBooleanExtra(ConnectivityManager.EXTRA_NO_CONNECTIVITY, false)) {
                createDialog(context);
            }
        }
    }
}
