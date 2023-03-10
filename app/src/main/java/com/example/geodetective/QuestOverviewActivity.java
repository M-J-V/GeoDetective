package com.example.geodetective;

import android.os.Bundle;
import android.widget.Button;
import android.widget.ImageButton;
import android.widget.ImageView;
import android.widget.TextView;

import androidx.appcompat.app.AppCompatActivity;

//TODO change name, since we merged the join activity and the joined quest activity quest overview activity is not a good name anymore.
public class QuestOverviewActivity extends AppCompatActivity {

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_quest_overview);

        // Get buttons from activity
        Button submitQuestButton = findViewById(R.id.check_result_btn);
        Button hintButton = findViewById(R.id.reveal_hint_btn);
        ImageButton backButton = findViewById(R.id.BackBtn);

        // Get ImageView from activity
        ImageView questImage = findViewById(R.id.Quest_Image);

        // Get text fields from activity
        TextView imageClue = findViewById(R.id.image_hint);
        TextView questDescription = findViewById(R.id.quest_description);
        TextView questName = findViewById(R.id.quest_name);

        String questNameValue = getIntent().getStringExtra("questName");
        String questDescriptionValue = getIntent().getStringExtra("questDescription");

        questName.setText(questNameValue);
        questDescription.setText(questDescriptionValue);
        questImage.setImageResource(getIntent().getExtras().getInt("questImage"));

        //Set on click listeners
        backButton.setOnClickListener(v -> {
            // Start list of quests activity
            navigateBack();
        });

    }

    public void navigateBack() {
        super.onBackPressed();
    }
}
