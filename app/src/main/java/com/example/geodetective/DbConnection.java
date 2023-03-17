package com.example.geodetective;

import static androidx.core.content.ContextCompat.startActivity;

import android.app.ProgressDialog;
import android.content.ContentResolver;
import android.content.Context;
import android.content.Intent;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.net.Uri;
import android.util.Log;
import android.webkit.MimeTypeMap;
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
    StorageReference storage;

    private DbConnection() {
        this.db = FirebaseFirestore.getInstance();
        this.users = db.collection("Users");
        this.quests = db.collection("Quests");
        this.attempts = db.collection("Attempted");
        this.questNames = db.collection("AllQuests");
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

    public void createNewUser(String username, String password) {
        // Create a new user with username and password
        Map<String, Object> user = new HashMap<>();
        user.put("Username", username);
        user.put("Password", password);

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
    public void displayAllQuests(ArrayList<String> titles, ArrayList<String> creators, ArrayList<Bitmap> images, Context context){
        // Get all titles
            // Get all creators of quests
                getImages(titles, creators, images, context);
    }

//    private void loadQuestList(ArrayList<String> titles, ArrayList<String> creators, ArrayList<Bitmap> images, Context context) {
//        Intent questList = new Intent(context, ListOfQuests.class);
//        questList.putParcelableArrayListExtra("images",images); // Passing Bitmaps like this is not very memory efficient
//        questList.putStringArrayListExtra("titles", titles);
//        questList.putStringArrayListExtra("creators", creators);
//        startActivity(context, questList);
//    }

    public void getImages(ArrayList<String> titles, ArrayList<String> creators, ArrayList<Bitmap> images, Context context) {
        StorageReference storRef = FirebaseStorage.getInstance().getReference().child("questImages").child("Vertigo Quest");

        final long ONE_MEGABYTE = 1024*1024;
        storRef.getBytes(ONE_MEGABYTE).addOnCompleteListener(new OnCompleteListener<byte[]>() {
            @Override
            public void onComplete(@NonNull Task<byte[]> task) {
                byte[] bytes = task.getResult();
                Bitmap temp = BitmapFactory.decodeByteArray(bytes, 0, bytes.length);
                images.add(temp);
                Log.d("WOAH","completed dbConnection: " + images.get(0).toString());
                //loadQuestList(titles, creators, images, context);
            }
        });
    }

    private void uploadBitmap(String title, byte[] data, Context context) {
        StorageReference storRef = FirebaseStorage.getInstance().getReference().child("questImages").child(title);
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
}
