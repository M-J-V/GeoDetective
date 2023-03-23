package com.example.geodetective;

import android.content.BroadcastReceiver;
import android.content.Context;
import android.content.Intent;
import android.net.ConnectivityManager;
import android.widget.Toast;

import androidx.annotation.NonNull;

public class ConnectivityChecker extends BroadcastReceiver {


    public static boolean hasInternetConnection(@NonNull Context context) {
        ConnectivityManager cm = (ConnectivityManager) context.getSystemService(Context.CONNECTIVITY_SERVICE);
        return cm.getActiveNetworkInfo() != null && cm.getActiveNetworkInfo().isConnected();
    }

    public static void openNoInternetDialog(Context context) {
        Intent i = new Intent(context, ConnectivityAlert.class);
        i.addFlags(Intent.FLAG_ACTIVITY_NEW_TASK);
        context.startActivity(i);
    }

    @Override
    public void onReceive(Context context, Intent intent) {
        if (intent.getAction().equals("android.net.conn.CONNECTIVITY_CHANGE")) {
            Toast.makeText(context, "Network state changed", Toast.LENGTH_SHORT).show();
            if (!hasInternetConnection(context)) {
                Toast.makeText(context, "No internet connection", Toast.LENGTH_SHORT).show();
                openNoInternetDialog(context);
            }
        }
    }
}
