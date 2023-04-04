package com.example.geodetective;

import android.content.Context;

public class CompareResource {

    public boolean isEqual(Context context, int resourceId, String string) {

        return context.getString(resourceId).equals(string);
    }

}
