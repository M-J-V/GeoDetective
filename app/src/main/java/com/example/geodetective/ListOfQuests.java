package com.example.geodetective;

import android.app.Activity;
import android.content.Intent;
import android.graphics.Bitmap;
import android.os.Bundle;
import android.view.View;
import android.widget.AdapterView;
import android.widget.ImageButton;
import android.widget.ListView;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;

import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.Task;
import com.google.firebase.firestore.DocumentSnapshot;

import java.util.ArrayList;


public class ListOfQuests extends AppCompatActivity {

    DbConnection db = DbConnection.getInstance();
    ActiveQuest activeQuest = ActiveQuest.getInstance();
    QuestImages imgs = QuestImages.getInstance();
    ListView listView;
    Activity activity = this;
    int positionPressed = -1;
    ArrayList<Bitmap> questImages;
    ArrayList<String> titles;
    ArrayList<String> creators;

    @Override
    protected void onResume() {
        super.onResume();

        if (positionPressed > -1) {
            ActiveQuest activeQuestInstance = ActiveQuest.getInstance();
            questImages.set(positionPressed, activeQuestInstance.getQuest().getImage());
            titles.set(positionPressed, activeQuestInstance.getQuest().getName());
            creators.set(positionPressed, activeQuestInstance.getQuest().getCreator());
            CustomBaseAdapter customBaseAdapter = new CustomBaseAdapter(getApplicationContext(), titles, creators, questImages);
            listView.setAdapter(customBaseAdapter);
            activeQuestInstance.disconnectActiveQuest();
            positionPressed = -1;
        }

    }

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_list_of_quests);
        listView = (ListView) findViewById(R.id.customListView);
        questImages = imgs.getImages();
        titles = getIntent().getStringArrayListExtra("titles");
        creators = getIntent().getStringArrayListExtra("creators");
        CustomBaseAdapter customBaseAdapter = new CustomBaseAdapter(getApplicationContext(), titles, creators, questImages);

        listView.setAdapter(customBaseAdapter);

        ImageButton backBtn = findViewById(R.id.backimagebutton);

        backBtn.setOnClickListener(v -> {
            // Start profile activity
            startActivity(new Intent(getApplicationContext(), ProfileActivity.class));
        });

        listView.setOnItemClickListener(new AdapterView.OnItemClickListener() {
            @Override
            public void onItemClick(AdapterView<?> adapterView, View view, int position, long l) {

                // Get basic info from existing arraylist
                String questName = titles.get(position);
                String creator = creators.get(position);
                Bitmap imageBitmap = questImages.get(position);
                positionPressed = position;


                // Remaining info comes from quest in database
                db.quests.document(questName).get().addOnCompleteListener(new OnCompleteListener<DocumentSnapshot>() {
                    @Override
                    public void onComplete(@NonNull Task<DocumentSnapshot> task) {
                        if (task.isSuccessful()) {
                            DocumentSnapshot doc = task.getResult();
                            String questDesc = doc.get("Description").toString();
                            String questHint = doc.get("Hint").toString();
                            double questLat = (double) doc.get("latitude");
                            double questLong = (double) doc.get("longitude");
                            Location location = new Location(questLat, questLong, activity);
                            activeQuest.setQuest(new Quest(questName, creator, questDesc, questHint, imageBitmap, location));
                            startActivity(new Intent (getApplicationContext(), QuestOverviewActivity.class));
                        }
                    }
                });
            }
        });
    }
}
