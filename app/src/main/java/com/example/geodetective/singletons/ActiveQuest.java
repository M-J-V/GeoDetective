package com.example.geodetective.singletons;

import com.example.geodetective.gameComponents.Quest;

/**
 * The ActiveQuest class is a singleton that stores the information of the active quest (the quest
 * that we are currently exploring from the list of quests)
 */
public class ActiveQuest {

    private static ActiveQuest instance = null;

    private static Quest quest = null;

    private ActiveQuest(){ }

    /**
     * This method returns the already existing instance of the class or a
     * new one (if it doesn't exist already).
     * @return instance of the class
     */
    public static ActiveQuest getInstance() {
        if (instance == null) {
            instance = new ActiveQuest();
        }
        return instance;
    }

    /**
     * This method returns the quest that is now active
     * @return the active quest object.
     */
    public Quest getQuest() {
        return quest;
    }

    /**
     * This method sets a new active quest.
     * @param setterQuest new active quest
     */
    public void setQuest(Quest setterQuest) {
        quest = setterQuest;
    }

    /**
     * This method disconnects the active quest (no active quest).
     */
    public void disconnectActiveQuest() {
        this.instance = null;
        this.quest = null;
    }


}
