package com.example.geodetective;

import android.content.Context;

public class CompareResource {

    /**
     * The function checks if a given string is equal to the string value of a resource ID in a given
     * context.
     *
     * @param context The context parameter is an object that provides access to application-specific
     * resources and classes, as well as information about the application's environment. It is
     * typically used to access resources such as strings, colors, and dimensions defined in the
     * application's resources.
     * @param resourceId The resourceId parameter is an integer value that represents the ID of a
     * string resource in the Android application. It is used to retrieve the string value from the
     * resources folder of the application.
     * @param string The string parameter is a string value that we want to compare with the string
     * value of a resource ID in the given context.
     * @return A boolean value is being returned.
     */
    public boolean isEqual(Context context, int resourceId, String string) {
        return context.getString(resourceId).equals(string);
    }

}
