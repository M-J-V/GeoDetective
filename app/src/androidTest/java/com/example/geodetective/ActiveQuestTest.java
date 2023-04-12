package com.example.geodetective;

import com.example.geodetective.gameComponents.Quest;
import com.example.geodetective.singletons.ActiveQuest;

import org.junit.After;
import org.junit.Before;
import org.junit.Test;
import static org.junit.Assert.*;

public class ActiveQuestTest {

    private ActiveQuest activeQuest;
    private Quest quest;

    /**
     * This function sets up activeQuest and quest so that you do not need
     * to write it every time for every method. It is created before unit
     * tests are executed.
     */
    @Before
    public void setUp() {
        activeQuest = ActiveQuest.getInstance();
        quest = new Quest("My Quest", "John Doe", "Find the hidden treasure!", "Look under the big tree", null, null);
    }

    /**
     * Disconnects active quest when all the test cases are done.
     */
    @After
    public void tearDown() {
        activeQuest.disconnectActiveQuest();
    }

    /**
     * This unit test tests if it gets the active quest from ActiveQuest
     */
    @Test
    public void testGetActiveQuest() {
        activeQuest.setQuest(quest);
        assertEquals(quest, activeQuest.getQuest());
    }

    /**
     * This function test if we have set a quest to activeQuest.
     * We make sure by checking that activeQuest is not null.
     */
    @Test
    public void testSetActiveQuest() {
        activeQuest.setQuest(quest);
        assertNotNull(activeQuest.getQuest());
    }

    /**
     * Checks if we have disconnected the activeQuest. We use 2 assert in the
     * same method to make sure it we have set a quest successfully and
     * we have disconnected the quest.
     */
    @Test
    public void testDisconnectActiveQuest() {
        activeQuest.setQuest(quest);
        activeQuest.disconnectActiveQuest();
        assertNull(activeQuest.getQuest());
    }
}
