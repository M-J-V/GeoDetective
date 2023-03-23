package com.example.geodetective;

import android.content.BroadcastReceiver;
import android.content.Context;
import android.content.Intent;
import android.location.LocationManager;
import android.net.ConnectivityManager;
import android.widget.Toast;

import androidx.annotation.NonNull;

public class ConnectivityChecker extends BroadcastReceiver {


    public static boolean hasInternetConnection(@NonNull Context context) {
        ConnectivityManager cm = (ConnectivityManager) context.getSystemService(Context.CONNECTIVITY_SERVICE);
        return cm.getActiveNetworkInfo() != null && cm.getActiveNetworkInfo().isConnected();
    }

    public static boolean hasGPSConnection(@NonNull Context context) {
        LocationManager lm = (LocationManager) context.getSystemService(Context.LOCATION_SERVICE);
        return lm != null && lm.isProviderEnabled(LocationManager.GPS_PROVIDER);
    }

    public static void openNoInternetDialog(Context context) {
        Intent i = new Intent(context, InternetConnectivityAlert.class);
        i.addFlags(Intent.FLAG_ACTIVITY_NEW_TASK);
        context.startActivity(i);
    }

    public static void openNoGPSDialog(Context context) {
        Intent i = new Intent(context, GPSConnectivityAlert.class);
        i.addFlags(Intent.FLAG_ACTIVITY_NEW_TASK);
        context.startActivity(i);
    }

    @Override
    public void onReceive(Context context, Intent intent) {
        if (intent.getAction().equals("android.net.conn.CONNECTIVITY_CHANGE")) {
            if (!hasInternetConnection(context)) {
                openNoInternetDialog(context);
            }

        } else if (intent.getAction().equals("android.location.PROVIDERS_CHANGED")) {
            if (!hasGPSConnection(context)) {
                openNoGPSDialog(context);
            }
        }
    }
}
