package com.example.geodetective;

import android.os.Bundle;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ImageView;

import androidx.appcompat.app.AppCompatActivity;

public class CreateQuestActivity extends AppCompatActivity {

        @Override
        protected void onCreate(Bundle savedInstanceState) {
            super.onCreate(savedInstanceState);
            setContentView(R.layout.activity_create_quest);

            // Get image from activity
            ImageView questImage = findViewById(R.id.Quest_Image);

            // Get buttons from activity
            Button chooseImageBtn = findViewById(R.id.choose_Quest_Image_Btn);
            Button submitQuestBtn = findViewById(R.id.submit_quest_btn);

            // get text inputs from activity
            EditText questName = findViewById(R.id.quest_name_input);
            EditText questDescription = findViewById(R.id.quest_description_input);
            EditText questHint = findViewById(R.id.quest_hint_input);

            //TODO: let choose image button load a image from camera/gallery and update questImage
            //TODO: let submit quest button create a quest object, this object should include all information supplied by the user, and send it to the database
        }

}
