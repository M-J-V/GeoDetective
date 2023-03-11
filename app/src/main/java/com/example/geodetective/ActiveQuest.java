package com.example.geodetective;

import android.widget.ImageView;

public class ActiveQuest {

    private static Quest quest = null;

    public static boolean isNull() {
        return (quest == null);
    }

    public static Quest getQuest() {
        return quest;
    }

    public static void setQuest(Quest quest1) {
        quest = quest1;
    }

}
