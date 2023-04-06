package com.example.geodetective;

import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertNotEquals;
import static org.junit.Assert.fail;

import androidx.test.core.app.ApplicationProvider;

import com.example.geodetective.listAdapters.CustomListOfQuestsAdapter;

import org.junit.Test;

import java.util.ArrayList;

public class CustomListOfQuestsAdapterTest {

    /**
     * This is a JUnit test to check if the constructor of CustomListOfQuestsAdapter throws an
     * exception when the context parameter is null.
     */
    @Test
    public void checkConstructorNoContext(){
        try{
               new CustomListOfQuestsAdapter(null, new ArrayList<>(), new ArrayList<>(), new ArrayList<>());
                fail("CustomListOfQuestsAdapter constructor did not throw an exception");
            } catch (Exception ignored) {}
    }

    /**
     * This is a JUnit test to check if the constructor of CustomListOfQuestsAdapter throws an
     * exception when the quests parameter is null.
     */
    @Test
    public void checkConstructorNoQuests(){
        try{
            new CustomListOfQuestsAdapter(ApplicationProvider.getApplicationContext(), null, new ArrayList<>(), new ArrayList<>());
            fail("CustomListOfQuestsAdapter constructor did not throw an exception");
        } catch (Exception ignored) {}
    }

    /**
     * This is a JUnit test to check if the constructor of CustomListOfQuestsAdapter throws an
     * exception when the questNames parameter is null.
     */
    @Test
    public void checkConstructorNoQuestNames(){
        try{
            new CustomListOfQuestsAdapter(ApplicationProvider.getApplicationContext(), new ArrayList<>(), null, new ArrayList<>());
            fail("CustomListOfQuestsAdapter constructor did not throw an exception");
        } catch (Exception ignored) {}
    }

    /**
     * This is a JUnit test to check if the constructor of CustomListOfQuestsAdapter throws an
     * exception when the questDescriptions parameter is null.
     */
    @Test
    public void checkConstructorNoQuestDescriptions(){
        try{
            new CustomListOfQuestsAdapter(ApplicationProvider.getApplicationContext(), new ArrayList<>(), new ArrayList<>(), null);
            fail("CustomListOfQuestsAdapter constructor did not throw an exception");
        } catch (Exception ignored) {}
    }

    /**
     * This is a unit test in Java that checks if the count of items in a custom list adapter matches
     * the expected count.
     */
    @Test
    public void getCountCorrectCount(){
        ArrayList<String> quests = new ArrayList<>();
        int count = 0;
        for(int i = 0; i < 10; i++){
            count++;
            quests.add("Quest " + i);
        }

        CustomListOfQuestsAdapter adapter = new CustomListOfQuestsAdapter(null, quests, null, null);
        assertEquals(count, adapter.getCount());
    }

    /**
     * This is a JUnit test function that checks if the count of items in a custom list adapter is
     * incorrect.
     */
    @Test
    public void getCountIncorrectCount(){
        ArrayList<String> quests = new ArrayList<>();
        int count = 0;
        for(int i = 0; i < 10; i++){
            count++;
            quests.add("Quest " + i);
        }

        CustomListOfQuestsAdapter adapter = new CustomListOfQuestsAdapter(null, quests, null, null);
        assertNotEquals(count + 1, adapter.getCount());
    }
}
