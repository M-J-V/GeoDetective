package com.example.geodetective.activities;

import android.app.ProgressDialog;
import android.content.Intent;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.os.Bundle;
import android.util.Log;
import android.widget.Button;
import android.widget.ImageButton;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;

import com.example.geodetective.singletons.ActiveUser;
import com.example.geodetective.singletons.DbConnection;
import com.example.geodetective.singletons.QuestImages;
import com.example.geodetective.R;
import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.Task;
import com.google.firebase.firestore.DocumentSnapshot;
import com.google.firebase.storage.StorageReference;

import java.util.ArrayList;
import java.util.List;
import java.util.Objects;

//TODO you can open an activity multiple times by clicking a button twice before it loads, especially if database requests need to be made for the list of quests this is quite slow.
public class HomeActivity extends AppCompatActivity {

    DbConnection db = DbConnection.getInstance();
    ActiveUser user = ActiveUser.getInstance();
    QuestImages images = QuestImages.getInstance();
    ProgressDialog pd;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_home);
        updateTrust();

        // Get buttons from activity
        Button createQuestBtn = findViewById(R.id.Create_Quest_Btn);
        Button joinQuestBtn = findViewById(R.id.Play_Quest_Btn);
        Button logoutBtn = findViewById(R.id.Logout_btn);
        ImageButton returnBtn = findViewById(R.id.Profile_Btn);

        // Get Text from activity
        TextView errorTxt1 = findViewById(R.id.ErrorTextHome1);
        TextView errorTxt2 = findViewById(R.id.ErrorTextHome2);

        pd = new ProgressDialog(this);

        //Set on click listeners
        createQuestBtn.setOnClickListener(v -> {
            if (user.getTrusted()) {
                // Start create quest activity
                startActivity(new Intent(getApplicationContext(), CreateQuestActivity.class));
            } else {
                errorTxt1.setText("You do not have permission to create quests.");
                errorTxt2.setText("Ask to be granted creator role on your profile page!");
            }

        });

        returnBtn.setOnClickListener(v -> {
            // Start profile activity
            startActivity(new Intent(getApplicationContext(), ProfileActivity.class));
        });

        joinQuestBtn.setOnClickListener(v -> {
            // Prepare information to load list of all quests
            getQuests();
        });

        logoutBtn.setOnClickListener(v -> {
            // Start login activity
            ActiveUser user = ActiveUser.getInstance();
            user.disconnectUser();
            startActivity(new Intent(getApplicationContext(), LoginActivity.class));
        });

    }

    private void getQuests() {
        pd.setTitle("Loading Quests!");
        pd.show();
        db.questNames.document("questsID").get().addOnCompleteListener(task -> {
            if (task.isSuccessful()) {
                DocumentSnapshot doc = task.getResult();
                List<String> questTitles = (List<String>) doc.get("quests");
                ArrayList<String> titles = new ArrayList<>(Objects.requireNonNull(questTitles));
                ArrayList <String> creators = new ArrayList<>();
                getCreators(titles, creators, 0);
            }
        });
    }

    // The function uses recursion rather than a for loop to ensure there are no timing issues since get() is asynchronous
    private void getCreators(ArrayList<String> titles, ArrayList<String> creators, int pos) {
        int numQuests = titles.size();
        db.quests.document(titles.get(pos)).get().addOnCompleteListener(task -> {
            if (task.isSuccessful()) {
                DocumentSnapshot doc = task.getResult();
                String creatorUser = Objects.requireNonNull(doc.get("Creator")).toString();
                creators.add(creatorUser);
                if ( pos == numQuests - 1 ) {
                    ArrayList<Bitmap> questImages = new ArrayList<>();
                    getImages(titles, creators, questImages,0);
                } else {
                    getCreators(titles, creators, pos + 1);
                }
            }
        });
    }

    // The function uses recursion rather than a for loop to ensure there are no timing issues since get() is asynchronous
    private void getImages(ArrayList<String> titles, ArrayList<String> creators, ArrayList<Bitmap> questImages, int pos) {
        final long ONE_MEGABYTE = 1024*1024*9;
        int numQuests = titles.size();

        StorageReference storeRef = db.storage.child("questImages").child(titles.get(pos));
        storeRef.getBytes(ONE_MEGABYTE).addOnCompleteListener(task -> {
            if (task.isSuccessful()) {
                byte[] bytes = task.getResult();

                Bitmap image = BitmapFactory.decodeByteArray(bytes, 0, bytes.length);
                Log.d("WOAH", pos + " kilobytes: " + bytes.length/1000 + " for " + titles.get(pos));
                questImages.add(image);
                if ( pos == numQuests - 1 ) {
                    loadQuestListActivity(titles, creators, questImages);
                } else {
                    getImages(titles, creators, questImages, pos + 1);
                }
            }

        });

    }

    private void loadQuestListActivity(ArrayList<String> titles, ArrayList<String> creators, ArrayList<Bitmap> questImages) {
        Intent questList = new Intent(getApplicationContext(), ListOfQuestsActivity.class);
        //questList.putParcelableArrayListExtra("images",questImages); // Passing Bitmaps like this is not very memory efficient
        images.setImages(questImages);
        questList.putStringArrayListExtra("titles", titles);
        questList.putStringArrayListExtra("creators", creators);
        pd.dismiss();
        startActivity(questList);
    }

    private void updateTrust() {
        db.users.document(user.getUsername()).get().addOnCompleteListener(new OnCompleteListener<DocumentSnapshot>() {
            @Override
            public void onComplete(@NonNull Task<DocumentSnapshot> task){
                String errorMsg = "";
                if (task.isSuccessful()) {
                    DocumentSnapshot User = task.getResult();
                    if (User.exists()) {
                        user.setTrusted((boolean) User.get("Trusted"));
                    }
                }
            }
        });
    }
}