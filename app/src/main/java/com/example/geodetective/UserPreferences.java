package com.example.geodetective;

import android.app.AlertDialog;
import android.content.Context;
import android.util.Log;

import androidx.annotation.NonNull;

import org.json.JSONException;
import org.json.JSONObject;

import java.io.BufferedReader;
import java.io.BufferedWriter;
import java.io.File;
import java.io.FileReader;
import java.io.FileWriter;
import java.io.IOException;

public class UserPreferences {

    private static UserPreferences instance = null;
    private final File preferencesFile;
    private JSONObject jsonObject = null;

    private UserPreferences(@NonNull Context context) {
        preferencesFile = new File(context.getFilesDir(), "preferences.json");

        try {
            if(preferencesFile.createNewFile()) {
                BufferedWriter bufferedWriter = new BufferedWriter(new FileWriter(preferencesFile));
                bufferedWriter.write(getJSON().toString());
                bufferedWriter.close();
            }
        } catch (IOException e) {
            // Create an AlertDialog to display the error message
            AlertDialog.Builder builder = new AlertDialog.Builder(context);
            builder.setTitle("Error");
            builder.setMessage("An error occurred: " + e.getMessage());
            builder.setPositiveButton("OK", (dialog, which) -> {
                // Close the app when the dialog closes
                System.exit(0);
            });
            builder.setCancelable(false);
            builder.show();
        }
    }

    public static UserPreferences getInstance(Context context) {
        if (instance == null) {
            instance = new UserPreferences(context);
        }

        return instance;
    }

    public Object getPreference(String preference) {
        JSONObject preferences = getJSON();
        if(preferences.has(preference)){
            try {
                return preferences.get(preference);
            } catch (JSONException e) {
                return null;
            }
        } else {
            return null;
        }
    }

    public boolean getBoolean(String key, boolean defaultValue){
        Object bool = getPreference(key);
        if(bool == null)
            return defaultValue;
        if(!(bool instanceof Boolean))
            return defaultValue;

        return (boolean) bool;
    }

    public void putPreference(String key, Object value) {
        if(key == null || value == null)
            throw new IllegalArgumentException("Key or value must not be null");

        try {
            getJSON().put(key, value);
            writeJSONtoPreferences();
            jsonObject = null;
            Log.d("putPreference", getJSON().toString());
        } catch (Exception e) {
            throw new RuntimeException(e);
        }
    }

    public boolean contains(String key){
        try {
            return getJSON().has(key);
        } catch (Exception e) {
            throw new RuntimeException(e);
        }
    }

    @NonNull
    private String readFile() {
        FileReader fileReader;
        try {
            fileReader = new FileReader(preferencesFile);
            BufferedReader bufferedReader = new BufferedReader(fileReader);
            StringBuilder stringBuilder = new StringBuilder();
            String line = bufferedReader.readLine();
            while (line != null){
                stringBuilder.append(line).append("\n");
                line = bufferedReader.readLine();
            }
            bufferedReader.close();
            return stringBuilder.toString();
        } catch (IOException e) {
            return "{}";
        }
    }

    private void writeJSONtoPreferences() throws IOException {
        FileWriter fileWriter = new FileWriter(preferencesFile);
        BufferedWriter bufferedWriter = new BufferedWriter(fileWriter);
        bufferedWriter.write(getJSON().toString());
        bufferedWriter.close();
        Log.d("Written", readFile());
    }

    private JSONObject getJSON() {
        if(jsonObject == null) {
            if(preferencesFile.exists()) {
                try {
                    jsonObject = new JSONObject(readFile());
                } catch (JSONException e) {
                    jsonObject = new JSONObject();
                }
            } else {
                jsonObject = new JSONObject();
            }
        }
        return jsonObject;
    }

}
