package com.example.geodetective;

import android.content.Intent;
import android.os.Bundle;
import android.widget.ListView;

import androidx.appcompat.app.AppCompatActivity;


public class ListOfQuests extends AppCompatActivity {
    String[] quests = {"Meta-Quest", "Audi-Quest", "Aurora-Quest"};
    String[] authors = {"Author: John Smith", "Author: John Smith", "Author: John Smith"};
    int[] questImages = {R.drawable.metaforum, R.drawable.auditorium, R.drawable.aurora};

    ListView listView;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_list_of_quests);
        listView = findViewById(R.id.customListView);
        CustomBaseAdapter customBaseAdapter = new CustomBaseAdapter(getApplicationContext(), quests, authors, questImages);
        listView.setAdapter(customBaseAdapter);

        listView.setOnItemClickListener((adapterView, view, position, l) -> {
            //startActivity(new Intent(getApplicationContext(), QuestOverviewActivity.class));

            if (position == 0) {
                String questName = "Meta-Quest";
                String questDescription = "Find the plane";
                Intent intent = new Intent (ListOfQuests.this, QuestOverviewActivity.class);
                intent.putExtra("questName", questName);
                intent.putExtra("questDescription", questDescription);
                intent.putExtra("questImage", R.drawable.metaforum);
                startActivity(intent);
            }

            if (position == 1) {
                String questName = "Audi-Quest";
                String questDescription = "Find the organ";
                Intent intent = new Intent (ListOfQuests.this, QuestOverviewActivity.class);
                intent.putExtra("questName", questName);
                intent.putExtra("questDescription", questDescription);
                intent.putExtra("questImage", R.drawable.auditorium);
                startActivity(intent);
            }

            if (position == 2) {
                String questName = "Aurora-Quest";
                String questDescription = "Find room 511";
                Intent intent = new Intent (ListOfQuests.this, QuestOverviewActivity.class);
                intent.putExtra("questName", questName);
                intent.putExtra("questDescription", questDescription);
                intent.putExtra("questImage", R.drawable.aurora);
                startActivity(intent);
            }


        });

        //TODO start QuestOverviewActivity when a quest is clicked (and pass along some information like a quest ID to load the quest information)
    }
}
