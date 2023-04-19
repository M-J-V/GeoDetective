package com.example.geodetective.singletons;

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

/**
 * The UserPreferences class stores the active preferences in the app.
 */
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

    /**
     * This method returns the already existing instance of the class or a
     * new one (if it doesn't exist already).
     * @return instance of the class
     */
    public static UserPreferences getInstance(Context context) {
        if (instance == null) {
            instance = new UserPreferences(context);
        }

        return instance;
    }

    /**
     * This method returns a selected preference.
     * @param preference the selected preference
     * @return the preference object
     */
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

    /**
     * This method returns whether a preference is allowed or not
     * @param key the preference
     * @param defaultValue default boolean value in case there is no boolean preference
     * @return whether the preference is allowed or not
     */
    public boolean getBoolean(String key, boolean defaultValue){
        Object bool = getPreference(key);
        if(bool == null)
            return defaultValue;
        if(!(bool instanceof Boolean))
            return defaultValue;

        return (boolean) bool;
    }

    /**
     * This method adds a preference to the already existing ones.
     * @param key the preference
     * @param value whether it is allowed or not
     */
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

    /**
     * This method returns whether there exists a preference with a certain key.
     * @param key the preference
     * @return whether that preference exists
     */
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
