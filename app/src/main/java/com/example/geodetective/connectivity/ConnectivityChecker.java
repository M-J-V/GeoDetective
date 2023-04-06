package com.example.geodetective.connectivity;

import android.content.BroadcastReceiver;
import android.content.Context;
import android.content.Intent;
import android.location.LocationManager;
import android.net.ConnectivityManager;

import androidx.annotation.NonNull;

/**
 * The ConnectivityChecker class in Java checks for internet and GPS connectivity and opens
 * corresponding dialogs if there is no connection.
 */
public class ConnectivityChecker extends BroadcastReceiver {


    /**
     * The function checks if there is an active internet connection in the given context.
     *
     * @param context The context parameter is a reference to the current state of the application or
     * activity. It provides access to resources, preferences, and other system-level services. In this
     * case, it is used to get a reference to the ConnectivityManager service, which is used to check
     * for an active network connection.
     * @return The method `hasInternetConnection` returns a boolean value indicating whether there is
     * an active internet connection or not. It returns `true` if there is an active network connection
     * and `false` otherwise.
     */
    public static boolean hasInternetConnection(@NonNull Context context) {
        ConnectivityManager cm = (ConnectivityManager) context.getSystemService(Context.CONNECTIVITY_SERVICE);
        return cm.getActiveNetworkInfo() != null && cm.getActiveNetworkInfo().isConnected();
    }

    /**
     * The function checks if the GPS provider is enabled in the given context.
     *
     * @param context The context parameter is a reference to the current state of the application or
     * activity. It provides access to resources, preferences, and other system-level services. In this
     * case, it is used to get the location manager service and check if the GPS provider is enabled.
     * @return The method `hasGPSConnection` returns a boolean value indicating whether the GPS
     * provider is enabled or not. It returns `true` if the GPS provider is enabled and `false`
     * otherwise.
     */
    public static boolean hasGPSConnection(@NonNull Context context) {
        LocationManager lm = (LocationManager) context.getSystemService(Context.LOCATION_SERVICE);
        return lm != null && lm.isProviderEnabled(LocationManager.GPS_PROVIDER);
    }

    /**
     * This function opens a dialog box to alert the user of no internet connectivity in an Android
     * app.
     *
     * @param context The context parameter is a reference to the current state of the application or
     * activity. It provides access to resources, preferences, and other application-specific
     * information. In this case, it is used to start a new activity (InternetConnectivityAlert) when
     * there is no internet connectivity.
     */
    public static void openNoInternetDialog(Context context) {
        Intent i = new Intent(context, InternetConnectivityAlert.class);
        i.addFlags(Intent.FLAG_ACTIVITY_NEW_TASK);
        context.startActivity(i);
    }

    /**
     * This function opens a dialog box to alert the user about GPS connectivity in an Android app.
     *
     * @param context The context parameter is a reference to the current state of the application or
     * activity. It provides access to resources, preferences, and other application-specific
     * information. In this case, it is used to start a new activity (GPSConnectivityAlert) with the
     * FLAG_ACTIVITY_NEW_TASK flag.
     */
    public static void openNoGPSDialog(Context context) {
        Intent i = new Intent(context, GPSConnectivityAlert.class);
        i.addFlags(Intent.FLAG_ACTIVITY_NEW_TASK);
        context.startActivity(i);
    }

    /**
     * This function checks for changes in internet and GPS connectivity and opens a dialog if there is
     * no connection.
     *
     * @param context The context parameter is a reference to the current state of the application or
     * activity. It provides access to resources, preferences, and other application-specific
     * information.
     * @param intent The intent that triggered the broadcast receiver. It contains information about
     * the event that occurred, such as the action that was performed and any data associated with it.
     */
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
