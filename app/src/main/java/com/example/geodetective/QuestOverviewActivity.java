package com.example.geodetective;

import android.os.Bundle;
import android.widget.Button;
import android.widget.ImageButton;
import android.widget.ImageView;
import android.widget.TextView;

import androidx.appcompat.app.AppCompatActivity;

public class QuestOverviewActivity extends AppCompatActivity {

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_quest_overview);

        // Get buttons from activity
        Button startQuestButton = findViewById(R.id.join_quest_btn);
        Button hintButton = findViewById(R.id.reveal_hint_btn);
        ImageButton backButton = findViewById(R.id.BackBtn);

        // Get ImageView from activity
        ImageView bitmap = findViewById(R.id.Quest_Image);

        // Get text fields from activity
        TextView imageClue = findViewById(R.id.image_hint);
        TextView questDescription = findViewById(R.id.quest_description);
        TextView questName = findViewById(R.id.quest_name);
        ImageView questImage = findViewById(R.id.Quest_Image);

        String questNameValue = getIntent().getStringExtra("questName");
        String questDescriptionValue = getIntent().getStringExtra("questDescription");

        Bundle bundle = getIntent().getExtras();
        if (bundle != null) {
            int res_image = bundle.getInt("questImage");
            questImage.setImageResource(res_image);
        }

        questName.setText(questNameValue);
        questDescription.setText(questDescriptionValue);


    }

}
