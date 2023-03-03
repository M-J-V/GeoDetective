package com.example.geodetective;
import androidx.appcompat.app.AppCompatActivity;

import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.ListView;


public class ListOfQuests extends AppCompatActivity {
    String quests[] = {"Meta-Quest", "Audi-Quest", "Aurora-Quest"};
    int questsimage[] = {R.drawable.metaforum, R.drawable.auditorium, R.drawable.aurora};

    ListView listView;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_list_of_quests);
        listView = (ListView) findViewById(R.id.customListView);
        CustomBaseAdapter customBaseAdapter = new CustomBaseAdapter(getApplicationContext(), quests, questsimage);
        listView.setAdapter(customBaseAdapter);
    }
}
