package com.example.geodetective.singletons;

import android.content.Context;
import android.graphics.Bitmap;
import android.util.Log;
import android.widget.Toast;

import androidx.annotation.NonNull;

import com.example.geodetective.gameComponents.Quest;
import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.Task;
import com.google.firebase.firestore.CollectionReference;
import com.google.firebase.firestore.DocumentSnapshot;
import com.google.firebase.firestore.FirebaseFirestore;
import com.google.firebase.firestore.Query;
import com.google.firebase.firestore.QueryDocumentSnapshot;
import com.google.firebase.firestore.QuerySnapshot;
import com.google.firebase.storage.FirebaseStorage;
import com.google.firebase.storage.StorageReference;
import com.google.firebase.storage.UploadTask;

import java.io.ByteArrayOutputStream;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Date;
import java.util.HashMap;
import java.util.List;
import java.util.Locale;
import java.util.Map;
import java.util.Objects;

/**
 * A singleton class that is used to connect and interface with the firebase database.
 */
public class DbConnection {

    private static DbConnection connection = null;
    public FirebaseFirestore db;
    public CollectionReference users;
    public CollectionReference quests;
    public CollectionReference attempts;
    public CollectionReference questNames;
    public CollectionReference requests;
    public StorageReference storage;

    DbConnection() {
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
        Log.d("Test", "Test");
    }

    public void createAttempt(String username, String quest, boolean win) {
        if(username == null || quest == null)
            throw new IllegalArgumentException("Username or quest is null");

        SimpleDateFormat sdf = new SimpleDateFormat("yyyyMMdd_HHmmss", Locale.getDefault());
        String timeCompleted = sdf.format(new Date());

        Map<String, Object> attempt = new HashMap<>();
        attempt.put("Username", username);
        attempt.put("Quest", quest);
        attempt.put("Time Completed", timeCompleted);
        attempt.put("Success", win);

        attempts.document(username+"_"+quest+"_"+timeCompleted).set(attempt);
    }

    public void createNewUser(String username, String password, boolean trusted) {
        // Create a new user with username and password
        Map<String, Object> user = new HashMap<>();
        user.put("Username", username);
        user.put("Password", password);
        user.put("Trusted", trusted);

        // Add new user to database
        users.document(username).set(user);
    }

    public void createNewQuest(Quest newQuest, Context context){
        // Create a new user with username and password
        Map<String, Object> quest = new HashMap<>();
        quest.put("Title", newQuest.getName());
        quest.put("Description", newQuest.getDescription());
        quest.put("Hint", newQuest.getHint());
        quest.put("Creator",newQuest.getCreator());
        quest.put("longitude", newQuest.getLocation().getLongitude());
        quest.put("latitude",newQuest.getLocation().getLatitude());

        // Add new user to database
        quests.document(newQuest.getName()).set(quest);

        // Upload image to storage
        ByteArrayOutputStream ByArOuST = new ByteArrayOutputStream();

        newQuest.getImage().compress(Bitmap.CompressFormat.JPEG, 90, ByArOuST);
        byte[] data = ByArOuST.toByteArray();
        float imageSize = (float) data.length/1000;

        if (imageSize > 1000) {
            newQuest.getImage().compress(Bitmap.CompressFormat.JPEG, 25, ByArOuST);
        } else {
            newQuest.getImage().compress(Bitmap.CompressFormat.JPEG, 90, ByArOuST);
        }
        data = ByArOuST.toByteArray();
        uploadBitmap(newQuest.getName(), data, context);


        // Due to the way firebase interacts with collections, we must keep a list of all quest names.
        addToAllQuestsList(newQuest.getName());
    }

    private void addToAllQuestsList(String title) {
        if(title == null)
            throw new IllegalArgumentException("Title is null");

        questNames.document("questsID").get().addOnCompleteListener(task -> {
            if (task.isSuccessful()) {
                DocumentSnapshot doc = task.getResult();
                @SuppressWarnings("unchecked")
                List<String> titles = (List<String>) doc.get("quests");
                assert titles != null;
                titles.add(title);
                Map<String, Object> questTitles = new HashMap<>();
                questTitles.put("quests", titles);
                questNames.document("questsID").set(questTitles);

            }
        });
    }

    private void uploadBitmap(String title, byte[] data, Context context) {
        StorageReference storeRef = storage.child("questImages").child(title);
        UploadTask uploadTask = storeRef.putBytes(data);
        uploadTask.addOnFailureListener(exception -> {
            // Handle unsuccessful uploads
        }).addOnSuccessListener(taskSnapshot -> Toast.makeText(context, "Quest uploaded!", Toast.LENGTH_SHORT).show());
    }

    private void getAndDeleteCreatedQuests (String username) {
        ArrayList<String> questsCreated = new ArrayList<String>();
        Query createdQuests = quests.whereEqualTo("Creator", username);
        createdQuests.get().addOnCompleteListener(task -> {
            if(task.isSuccessful()) {
                for (QueryDocumentSnapshot doc : task.getResult()) {
                    questsCreated.add(Objects.requireNonNull(doc.get("Title")).toString());
                }

                // Delete all quests from Quests collection
                deleteQuests(questsCreated);
                // Delete all quests from list of existing quests
                deleteFromAllQuestsListMultiple(questsCreated);
                // Delete all quest images from storage
                deleteQuestImages(questsCreated);
            }
        });
    }

    private void deleteQuestImages(ArrayList<String> questList){
        StorageReference storeRef = storage.child("questImages");
        StorageReference image;
        for (String title: questList) {
            image = storeRef.child(title);
            image.delete();
        }
    }

    private void deleteQuestImage(String questTitle){
        StorageReference storeRef = storage.child("questImages");
        storeRef.child(questTitle).delete();
    }

    public void deleteUser(String username){
        users.document(username).delete();
    }

    private void deleteQuests(ArrayList<String> questList) {
        for (String title : questList) {
            quests.document(title).delete();
        }
    }

    private void deleteFromAllQuestsListMultiple(ArrayList<String> questTitles) {
        questNames.document("questsID").get().addOnCompleteListener(task -> {
            if (task.isSuccessful()) {
                DocumentSnapshot doc = task.getResult();
                @SuppressWarnings("unchecked")
                List<String> titles = (List<String>) doc.get("quests");
                for (String title : questTitles) {
                    assert titles != null;
                    titles.remove(title);
                }
                Map<String, Object> questTitles1 = new HashMap<>();
                questTitles1.put("quests", titles);
                questNames.document("questsID").set(questTitles1);
            }
        });
    }

    private void deleteFromAllQuestsList(String deletedQuest, Quest newQuest, Context context, boolean create) {
        questNames.document("questsID").get().addOnCompleteListener(task -> {
            if (task.isSuccessful()) {
                DocumentSnapshot doc = task.getResult();
                @SuppressWarnings("unchecked")
                List<String> titles = (List<String>) doc.get("quests");
                assert titles != null;
                titles.remove(deletedQuest);
                Map<String, Object> questTitles = new HashMap<>();
                questTitles.put("quests", titles);
                questNames.document("questsID").set(questTitles);

                if (create) {
                    createNewQuest(newQuest, context);
                }
            }
        });
    }

    public void updateQuestListAndCreate(String deletedQuest, Quest quest, Context context) {
        deleteQuestImage(quest.getName());
        deleteFromAllQuestsList(deletedQuest, quest, context, true);
    }

    public void deleteUserQuests(String username){
        if(username == null)
            throw new IllegalArgumentException("Username cannot be null");

        // Get all quest titles created by the user
        getAndDeleteCreatedQuests(username);
    }

    public void deleteQuest(Quest quest) {
        quests.document(quest.getName()).delete().addOnSuccessListener(aVoid -> {
            deleteFromAllQuestsList(quest.getName(),null, null,false);
            deleteQuestImage(quest.getName());
        });
    }

    public void deleteAttempts (Query query){
        query.get().addOnCompleteListener(new OnCompleteListener<QuerySnapshot>() {
            @Override
            public void onComplete(@NonNull Task<QuerySnapshot> task) {
                for(DocumentSnapshot doc : task.getResult().getDocuments()) {
                    doc.getReference().delete();
                }

            }
        });
    }

    public void updateQuestCreator(String oldUsername, String newUsername) {
        Query userAttempts = quests.whereEqualTo("Creator", oldUsername);
        userAttempts.get().addOnCompleteListener(new OnCompleteListener<QuerySnapshot>() {
            @Override
            public void onComplete(@NonNull Task<QuerySnapshot> task) {
                for(DocumentSnapshot doc : task.getResult().getDocuments()) {
                    doc.getReference().update("Creator", newUsername);

                }

            }
        });
    }

}
