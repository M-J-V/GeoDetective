package com.example.geodetective;

import com.google.firebase.firestore.CollectionReference;
import com.google.firebase.firestore.FirebaseFirestore;

import java.util.HashMap;
import java.util.Map;

/**
 * A singleton class that is used to connect and interface with the firebase database.
 */
public class DbConnection {

    private static DbConnection connection = null;
    FirebaseFirestore db;
    CollectionReference users;
    CollectionReference quests;
    CollectionReference attempted;

    private DbConnection() {
        this.db = FirebaseFirestore.getInstance();
        this.users = db.collection("Users");
        this.quests = db.collection("Quests");
        this.attempted = db.collection("Attempted");
    }

    /**
     * A static method that returns the instance of the class.
     *
     * @return an instance of the singleton class.
     */
    public static DbConnection getInstance() {
        if (connection == null) {
            connection = new DbConnection();
        }

        return connection;
    }

    public void createNewUser(String username, String password) {
        // Create a new user with username and password
        Map<String, Object> user = new HashMap<>();
        user.put("Username", username);
        user.put("Password", password);

        // Add new user to database
        users.document(username).set(user);
    }

    //TODO add quest attempt to DB
    //TODO add quest to DB

}
