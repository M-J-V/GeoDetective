package com.example.geodetective;

import android.app.Activity;
import android.content.Intent;
import android.graphics.Bitmap;
import android.os.Bundle;
import android.view.View;
import android.widget.AdapterView;
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
    questImages imgs = questImages.getInstance();
    ListView listView;
    Activity activity = this;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_list_of_quests);
        listView = (ListView) findViewById(R.id.customListView);
        ArrayList<Bitmap> questImages = imgs.getImages();
        ArrayList<String> titles = getIntent().getStringArrayListExtra("titles");
        ArrayList<String> creators = getIntent().getStringArrayListExtra("creators");
        CustomBaseAdapter customBaseAdapter = new CustomBaseAdapter(getApplicationContext(), titles, creators, questImages);

        listView.setAdapter(customBaseAdapter);

        listView.setOnItemClickListener(new AdapterView.OnItemClickListener() {
            @Override
            public void onItemClick(AdapterView<?> adapterView, View view, int position, long l) {

                // Get basic info from existing arraylist
                String questName = titles.get(position);
                String creator = creators.get(position);
                Bitmap imageBitmap = questImages.get(position);


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
                            //loadQuestOverview();
                            startActivity(new Intent (ListOfQuests.this, QuestOverviewActivity.class));
                        }
                    }
                });
            }
        });
    }

    private void loadQuestOverview() {
        Intent intent = new Intent (ListOfQuests.this, QuestOverviewActivity.class);
        intent.putExtra("questName", activeQuest.getQuest().getName());
        intent.putExtra("creator", activeQuest.getQuest().getCreator());
        intent.putExtra("questDescription", activeQuest.getQuest().getDescription());
        intent.putExtra("questImage", activeQuest.getQuest().getImage());
        intent.putExtra("longitude", activeQuest.getQuest().getLocation().getLongitude());
        intent.putExtra("latitude", activeQuest.getQuest().getLocation().getLatitude());
        startActivity(intent);
    }
}
