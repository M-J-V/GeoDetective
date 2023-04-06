package com.example.geodetective;

import static org.junit.Assert.*;

import android.app.Application;

import org.junit.Test;

public class ActiveQuestTest {

    Quest quest = new Quest("John Doe", "Divo", "Find the hidden treasure!", "Look under the big tree", null, null);

    @Test
    public void getInstanceNull() {
        ActiveQuest activeQuest = ActiveQuest.getInstance();
        assertEquals(activeQuest, null);
    }

    @Test
    public void testQuestNotNullAfterSetting() {
        ActiveQuest activeQuest = ActiveQuest.getInstance();
        activeQuest.setQuest(quest);
        assertNotNull(activeQuest);
    }

    @Test
    public void testDisconnectActiveQuest() {
        ActiveQuest activeQuest = ActiveQuest.getInstance();
        activeQuest.setQuest(quest);
        activeQuest.disconnectActiveQuest();
        assertNull(activeQuest);
    }

}