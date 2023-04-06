package com.example.geodetective.singletons;

import com.example.geodetective.gameComponents.Quest;

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

    public Quest getQuest() {
        return quest;
    }

    public void setQuest(Quest setterQuest) {
        quest = setterQuest;
    }

    public void disconnectActiveQuest() {
        this.instance = null;
        this.quest = null;
    }


}
