package com.example.geodetective.activities;

import android.app.Activity;
import android.content.Intent;
import android.graphics.Bitmap;
import android.os.Bundle;
import android.widget.ImageButton;
import android.widget.ListView;

import androidx.appcompat.app.AppCompatActivity;

import com.example.geodetective.R;
import com.example.geodetective.gameComponents.Location;
import com.example.geodetective.gameComponents.Quest;
import com.example.geodetective.listAdapters.CustomListOfQuestsAdapter;
import com.example.geodetective.singletons.ActiveQuest;
import com.example.geodetective.singletons.DbConnection;
import com.example.geodetective.singletons.QuestImages;
import com.google.firebase.firestore.DocumentSnapshot;

import java.util.ArrayList;
import java.util.Objects;


public class ListOfQuestsActivity extends AppCompatActivity {

    DbConnection db = DbConnection.getInstance();
    ActiveQuest activeQuest = ActiveQuest.getInstance();
    QuestImages images = QuestImages.getInstance();
    ListView listView;
    Activity activity = this;
    int positionPressed = -1;
    ArrayList<Bitmap> questImages;
    ArrayList<String> titles;
    ArrayList<String> creators;
    CustomListOfQuestsAdapter customBaseAdapter;

    @Override
    protected void onResume() {
        super.onResume();

        if (positionPressed > -1) {
            ActiveQuest activeQuestInstance = ActiveQuest.getInstance();
            questImages.set(positionPressed, activeQuestInstance.getQuest().getImage());
            titles.set(positionPressed, activeQuestInstance.getQuest().getName());
            creators.set(positionPressed, activeQuestInstance.getQuest().getCreator());
            customBaseAdapter = new CustomListOfQuestsAdapter(getApplicationContext(), titles, creators, questImages);
            listView.setAdapter(customBaseAdapter);
            activeQuestInstance.disconnectActiveQuest();
            positionPressed = -1;
        }

    }

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_list_of_quests);
        listView = findViewById(R.id.customListView);
        questImages = images.getImages();
        titles = getIntent().getStringArrayListExtra("titles");
        creators = getIntent().getStringArrayListExtra("creators");
        CustomListOfQuestsAdapter customBaseAdapter = new CustomListOfQuestsAdapter(getApplicationContext(), titles, creators, questImages);

        listView.setAdapter(customBaseAdapter);

        ImageButton backBtn = findViewById(R.id.backimagebutton);

        backBtn.setOnClickListener(v -> {
            // Start profile activity
            startActivity(new Intent(getApplicationContext(), HomeActivity.class));
        });

        listView.setOnItemClickListener((adapterView, view, position, l) -> {

            // Get basic info from existing arraylist
            String questName = titles.get(position);
            String creator = creators.get(position);
            Bitmap imageBitmap = questImages.get(position);
            positionPressed = position;


            // Remaining info comes from quest in database
            db.quests.document(questName).get().addOnCompleteListener(task -> {
                if (task.isSuccessful()) {
                    DocumentSnapshot doc = task.getResult();
                    String questDesc = Objects.requireNonNull(doc.get("Description")).toString();
                    String questHint = Objects.requireNonNull(doc.get("Hint")).toString();
                    double questLat = (double) Objects.requireNonNull(doc.get("latitude"));
                    double questLong = (double) Objects.requireNonNull(doc.get("longitude"));
                    Location location = new Location(questLat, questLong, activity);
                    activeQuest.setQuest(new Quest(questName, creator, questDesc, questHint, imageBitmap, location));
                    startActivity(new Intent (getApplicationContext(), QuestOverviewActivity.class));
                }
            });
        });
    }
}
