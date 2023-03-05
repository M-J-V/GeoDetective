package com.example.geodetective;

import android.graphics.Bitmap;
import android.widget.EditText;

public class Quest {

    private Bitmap questImage;
    private String questName;
    private String questDescription;
    private String questHint;

    public Quest(Bitmap image, String questName, String questDescription, String questHint){
        this.questImage = image;
        this.questName = questName;
        this.questDescription = questDescription;
        this.questHint = questHint;
    }

    public Bitmap getQuestImage() {
        return questImage;
    }

    public void setQuestImage(Bitmap questImage) {
        this.questImage = questImage;
    }

    public String getQuestName() {
        return questName;
    }

    public void setQuestName(String questName) {
        this.questName = questName;
    }

    public String getQuestDescription() {
        return questDescription;
    }

    public void setQuestDescription(String questDescription) {
        this.questDescription = questDescription;
    }

    public String getQuestHint() {
        return questHint;
    }

    public void setQuestHint(String questHint) {
        this.questHint = questHint;
    }
}
