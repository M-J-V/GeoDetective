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
import com.example.geodetective.guiListAdapters.CustomListOfQuestsAdapter;
import com.example.geodetective.singletons.ActiveQuest;
import com.example.geodetective.singletons.DbConnection;
import com.example.geodetective.singletons.QuestImages;
import com.google.firebase.firestore.DocumentSnapshot;

import java.util.ArrayList;
import java.util.Objects;

/**
 *  The ListOfQuestsActivity allows users to explore the available quests, showing their photo,
 *  their name and the name of their creator.
 */
public class ListOfQuestsActivity extends AppCompatActivity {

    private final DbConnection db = DbConnection.getInstance();
    private final ActiveQuest activeQuest = ActiveQuest.getInstance();
    private final QuestImages images = QuestImages.getInstance();
    private final Activity activity = this;
    private ListView listView;
    private int positionPressed = -1;
    private ArrayList<Bitmap> questImages;
    private ArrayList<String> titles;
    private ArrayList<String> creators;

    /**
     * This method is called whenever the user returns to this activity, after initiating it.
     * In our case, this method updates the quests list (in the case in which one was edited).
     */
    @Override
    protected void onResume() {
        super.onResume();

        if (positionPressed > -1) {
            ActiveQuest activeQuestInstance = ActiveQuest.getInstance();
            questImages.set(positionPressed, activeQuestInstance.getQuest().getImage());
            titles.set(positionPressed, activeQuestInstance.getQuest().getName());
            creators.set(positionPressed, activeQuestInstance.getQuest().getCreator());
            CustomListOfQuestsAdapter customBaseAdapter = new CustomListOfQuestsAdapter(getApplicationContext(), titles, creators, questImages);
            listView.setAdapter(customBaseAdapter);
            activeQuestInstance.disconnectActiveQuest();
            positionPressed = -1;
        }

    }

    /**
     This method sets up the List of Quests activity where users can see the quests that are available.
     It initializes various UI components such as buttons, text inputs, and error messages;
     It also sets up the functionality of these UI components.
     @param savedInstanceState A saved instance state of the activity, which can be null.
     */
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
