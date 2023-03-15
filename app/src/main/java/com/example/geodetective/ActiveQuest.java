package com.example.geodetective;

import android.widget.ImageView;

public class ActiveQuest {

    private static ActiveQuest instance = null;

    private static Quest quest = null;

    private ActiveQuest(){ }

    public static ActiveQuest getInstance() {
        if (instance == null) {
            instance = new ActiveQuest();
        }
        return instance;
    }

    public boolean isNull() {
        return (quest == null);
    }

    public Quest getQuest() {
        return quest;
    }

    public void setQuest(Quest quest1) {
        quest = quest1;
    }

}
