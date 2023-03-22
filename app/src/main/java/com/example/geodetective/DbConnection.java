package com.example.geodetective;

import static androidx.core.content.ContextCompat.startActivity;

import android.app.ProgressDialog;
import android.content.ContentResolver;
import android.content.Context;
import android.content.Intent;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.graphics.Color;
import android.net.Uri;
import android.util.Log;
import android.webkit.MimeTypeMap;
import android.widget.TextView;
import android.widget.Toast;

import androidx.annotation.NonNull;

import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.OnFailureListener;
import com.google.android.gms.tasks.OnSuccessListener;
import com.google.android.gms.tasks.Task;
import com.google.firebase.firestore.CollectionReference;
import com.google.firebase.firestore.DocumentReference;
import com.google.firebase.firestore.DocumentSnapshot;
import com.google.firebase.firestore.FirebaseFirestore;
import com.google.firebase.firestore.Query;
import com.google.firebase.firestore.QueryDocumentSnapshot;
import com.google.firebase.firestore.QuerySnapshot;
import com.google.firebase.storage.FirebaseStorage;
import com.google.firebase.storage.StorageReference;
import com.google.firebase.storage.UploadTask;

import org.w3c.dom.Text;

import java.net.URI;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

/**
 * A singleton class that is used to connect and interface with the firebase database.
 */
public class DbConnection {

    private static DbConnection connection = null;
    FirebaseFirestore db;
    CollectionReference users;
    CollectionReference quests;
    CollectionReference attempts;
    CollectionReference questNames;
    CollectionReference requests;
    StorageReference storage;

    private DbConnection() {
        this.db = FirebaseFirestore.getInstance();
        this.users = db.collection("Users");
        this.quests = db.collection("Quests");
        this.attempts = db.collection("Attempted");
        this.questNames = db.collection("AllQuests");
        this.requests = db.collection("Requests");
        this.storage = FirebaseStorage.getInstance().getReference();
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

    public void sendRequest(String username) {
        Map<String, Object> request = new HashMap<>();
        request.put("Username", username);
        request.put("Request", username + " has requested permission to create quests");
        requests.document(username).set(request);
    }

    public void createNewUser(String username, String password) {
        // Create a new user with username and password
        Map<String, Object> user = new HashMap<>();
        user.put("Username", username);
        user.put("Password", password);
        user.put("Trusted", false);

        // Add new user to database
        users.document(username).set(user);
    }

    public void createNewQuest(String title, String description, String hint, String creatorUser, byte[] bitmapData, Location location, Context context){
        // Create a new user with username and password
        Map<String, Object> quest = new HashMap<>();
        quest.put("Title", title);
        quest.put("Description", description);
        quest.put("Hint", hint);
        quest.put("Creator",creatorUser);
        quest.put("longitude",location.getLongitude());
        quest.put("latitude",location.getLatitude());

        // Add new user to database
        quests.document(title).set(quest);

        // Upload image to storage
        uploadBitmap(title, bitmapData, context);

        // Due to the way firebase interacts with collecttions, we must keep a list of all quest names.
        addToAllQuestsList(title);
    }

    private void addToAllQuestsList(String title) {
        questNames.document("questsID").get().addOnCompleteListener(new OnCompleteListener<DocumentSnapshot>() {
            @Override
            public void onComplete(@NonNull Task<DocumentSnapshot> task) {
                if (task.isSuccessful()) {
                    DocumentSnapshot doc = task.getResult();
                    List<String> titles = (List<String>) doc.get("quests");
                    titles.add(title);
                    Map<String, Object> questTitles = new HashMap<>();
                    questTitles.put("quests", titles);
                    questNames.document("questsID").set(questTitles);

                }
            }
        });
    }

    private void uploadBitmap(String title, byte[] data, Context context) {
        StorageReference storRef = storage.child("questImages").child(title);
        UploadTask uploadTask = storRef.putBytes(data);
        uploadTask.addOnFailureListener(new OnFailureListener() {
            @Override
            public void onFailure(@NonNull Exception exception) {
                // Handle unsuccessful uploads
            }
        }).addOnSuccessListener(new OnSuccessListener<UploadTask.TaskSnapshot>() {
            @Override
            public void onSuccess(UploadTask.TaskSnapshot taskSnapshot) {
                Toast.makeText(context, "Quest uploaded!", Toast.LENGTH_SHORT).show();
            }
        });
    }

    private void getAndDeleteCreatedQuests (String username, ArrayList<String> questsCreated) {
        Query createdQuests = quests.whereEqualTo("Creator", username);
        createdQuests.get().addOnCompleteListener(new OnCompleteListener<QuerySnapshot>() {
            @Override
            public void onComplete(@NonNull Task<QuerySnapshot> task) {
                if(task.isSuccessful()) {
                    for (QueryDocumentSnapshot doc : task.getResult()) {
                        questsCreated.add(doc.get("Title").toString());
                    }

                    // Delete all quests from Quests collection
                    deleteQuests(questsCreated);
                    // Delete all quests from list of existing quests
                    deleteFromAllQuestsListMultiple(questsCreated);
                    // Delete all quest images from storage
                    deleteQuestImages(questsCreated);
                    // Delete user from Users collection
                    deleteUser(username);
                }
            }
        });
    }

    private void deleteQuestImages(ArrayList<String> questList){
        StorageReference storRef = storage.child("questImages");
        StorageReference image;
        for (String title: questList) {
            image = storRef.child(title);
            image.delete();
        }
    }

    private void deleteUser(String username){
        users.document(username).delete();
    }

    private void deleteQuests(ArrayList<String> questList) {
        for (String title : questList) {
            quests.document(title).delete();
        }
    }

    private void deleteFromAllQuestsListMultiple(ArrayList<String> questTitles) {
        questNames.document("questsID").get().addOnCompleteListener(new OnCompleteListener<DocumentSnapshot>() {
            @Override
            public void onComplete(@NonNull Task<DocumentSnapshot> task) {
                if (task.isSuccessful()) {
                    DocumentSnapshot doc = task.getResult();
                    List<String> titles = (List<String>) doc.get("quests");
                    for (String title : questTitles) {
                        titles.remove(title);
                    }
                    Map<String, Object> questTitles = new HashMap<>();
                    questTitles.put("quests", titles);
                    questNames.document("questsID").set(questTitles);
                }
            }
        });
    }

    private void deleteFromAllQuestsList(String deletedQuest, String newQuest, String newDescription,
                                         String newHint, String creator, byte[] bitmapImage, Location location,  Context context, TextView msg) {
        questNames.document("questsID").get().addOnCompleteListener(new OnCompleteListener<DocumentSnapshot>() {
            @Override
            public void onComplete(@NonNull Task<DocumentSnapshot> task) {
                if (task.isSuccessful()) {
                    DocumentSnapshot doc = task.getResult();
                    List<String> titles = (List<String>) doc.get("quests");
                    titles.remove(deletedQuest);
                    Map<String, Object> questTitles = new HashMap<>();
                    questTitles.put("quests", titles);
                    questNames.document("questsID").set(questTitles);

                    createNewQuest(newQuest, newDescription, newHint,
                            creator, bitmapImage, location, context);
                }
            }
        });
    }

    public void updateQuestListAndCreate(String deletedQuest, String newQuest, String newDescription, String newHint, String creator, Context context, byte[] bitmapImage, Location location, TextView msg) {
        deleteFromAllQuestsList(deletedQuest, newQuest, newDescription, newHint, creator, bitmapImage, location, context, msg);
    }


    public void deleteUserAndQuests(String username, String password){
        // Get all quest titles created by the user
        ArrayList<String> questsCreated = new ArrayList<String>();
        getAndDeleteCreatedQuests(username, questsCreated);
    };

}
