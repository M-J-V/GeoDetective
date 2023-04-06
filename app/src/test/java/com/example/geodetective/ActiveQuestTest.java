package com.example.geodetective;

import static org.junit.Assert.*;

import android.app.Application;

import com.example.geodetective.gameComponents.Quest;
import com.example.geodetective.singletons.ActiveQuest;

import org.junit.Test;

public class ActiveQuestTest {

    Quest quest = new Quest("John Doe", "Divo", "Find the hidden treasure!", "Look under the big tree", null, null);

    @Test
    public void testGetInstance() {
        ActiveQuest activeQuest = ActiveQuest.getInstance();
        ActiveQuest newActiveQuest = ActiveQuest.getInstance();
        assertEquals(activeQuest, newActiveQuest);
        activeQuest.disconnectActiveQuest();
    }

    @Test
    public void testSetAndGetQuest() {
        ActiveQuest activeQuest = ActiveQuest.getInstance();
        activeQuest.setQuest(quest);
        assertEquals(quest, activeQuest.getQuest());
        activeQuest.disconnectActiveQuest();
    }

    @Test
    public void testDisconnectActiveQuest() {
        ActiveQuest activeQuest = ActiveQuest.getInstance();
        activeQuest.disconnectActiveQuest();
        assertEquals(activeQuest.getQuest(), null);
        //assertNotNull(ActiveQuest.getInstance());
        activeQuest.disconnectActiveQuest();
    }

}