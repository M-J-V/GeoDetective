package com.example.geodetective;

import androidx.appcompat.app.AppCompatActivity;

import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;

public class HomeActivity extends AppCompatActivity {

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_home);

        // Get buttons from activity
        Button createQuestBtn = findViewById(R.id.Create_Quest_Btn);
        Button joinQuestBtn = findViewById(R.id.Play_Quest_Btn);

        //Set on click listeners
        createQuestBtn.setOnClickListener(v -> {
            // Start create quest activity
            startActivity(new Intent(getApplicationContext(), CreateQuestActivity.class));
        });

        joinQuestBtn.setOnClickListener(v -> {
            //See list of avaliable quests
            startActivity(new Intent(getApplicationContext(), ListOfQuests.class));
        });

    }
}