package com.example.geodetective;

import android.content.Intent;
import android.graphics.Bitmap;
import android.graphics.Canvas;
import android.graphics.drawable.Drawable;
import android.os.Bundle;
import android.util.Log;
import android.widget.Button;
import android.widget.ImageButton;
import android.widget.ImageView;
import android.widget.TextView;

import androidx.activity.OnBackPressedCallback;
import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;

public class QuestOverviewActivity extends AppCompatActivity {

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_quest_overview);

        // Get buttons from activity
        Button submitQuestButton = findViewById(R.id.check_result_btn);
        Button hintButton = findViewById(R.id.reveal_hint_btn);
        ImageButton backButton = findViewById(R.id.BackBtn);
        ImageButton editButton = findViewById(R.id.EditBtn);

        // Get ImageView from activity
        ImageView bitmap = findViewById(R.id.Quest_Image);

        // Get text fields from activity
        TextView imageClue = findViewById(R.id.image_hint);
        TextView questDescription = findViewById(R.id.quest_description);
        TextView questName = findViewById(R.id.quest_name);
        ImageView questImage = findViewById(R.id.Quest_Image);

        String questNameValue = getIntent().getStringExtra("questName");
        String questDescriptionValue = getIntent().getStringExtra("questDescription");
        String questCreatorValue = getIntent().getStringExtra("creator");
        String questHintValue = "";

        Bundle bundle = getIntent().getExtras();
        if (bundle != null) {
            int res_image = bundle.getInt("questImage");
            questImage.setImageResource(res_image);
        }

        Quest activeQuest = new Quest(questNameValue, questCreatorValue,
                questDescriptionValue, questHintValue,
                getBitmapFromDrawable(questImage.getDrawable()));

        ActiveQuest activeQuestInstance = ActiveQuest.getInstance();
        activeQuestInstance.setQuest(activeQuest);

        editButton.setOnClickListener(v -> {
            String nameOfUser = ActiveUser.getInstance().getUsername();
            String nameOfCreator = activeQuestInstance.getQuest().getCreator();
            if(nameOfCreator.compareTo(nameOfUser) == 0) {
                startActivity(new Intent(getApplicationContext(), EditQuestActivity.class));
            }
        });

        questName.setText(questNameValue);
        questDescription.setText(questDescriptionValue);

        //Set on click listeners
        backButton.setOnClickListener(v -> {
            // Start list of quests activity
            navigateBack();
        });

    }

    // Convert drawable to bitmap
    @NonNull
    static private Bitmap getBitmapFromDrawable(@NonNull Drawable drawable) {
        final Bitmap bmp = Bitmap.createBitmap(drawable.getIntrinsicWidth(), drawable.getIntrinsicHeight(), Bitmap.Config.ARGB_8888);
        final Canvas canvas = new Canvas(bmp);
        drawable.setBounds(0, 0, canvas.getWidth(), canvas.getHeight());
        drawable.draw(canvas);
        return bmp;
    }

    @Override
    protected void onDestroy() {
        super.onDestroy();
        ActiveQuest activeQuestInstance = ActiveQuest.getInstance();
        activeQuestInstance.setQuest(null);
    }

    public void navigateBack() {
        super.onBackPressed();
    }
}
