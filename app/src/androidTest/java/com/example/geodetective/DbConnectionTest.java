package com.example.geodetective;

import static org.junit.Assert.fail;

import com.example.geodetective.gameComponents.Quest;
import com.example.geodetective.singletons.DbConnection;

import org.junit.Test;

public class DbConnectionTest {

    @Test
    public void getInstanceNotNull() {
        DbConnection dbConnection = DbConnection.getInstance();
        if (dbConnection == null) {
            fail("DbConnection.getInstance() returned null");
        }
    }

    @Test
    public void sendRequestInvalidInput() {
        DbConnection dbConnection = DbConnection.getInstance();
        try {
            dbConnection.sendRequest(null);
            fail("DbConnection.sendRequest() did not threw an exception");
        } catch (Exception ignored) {
        }
    }
    @Test
    public void createAttemptInvalidInput() {
        DbConnection dbConnection = DbConnection.getInstance();
        try {
            dbConnection.createAttempt(null, null, false);
            fail("DbConnection.createAttempt() did not threw an exception");
        } catch (Exception ignored) {
        }
    }

    @Test
    public void createUserInvalidInput() {
        DbConnection dbConnection = DbConnection.getInstance();
        try {
            dbConnection.createNewUser(null, null, false);
            fail("DbConnection.createNewUser() did not threw an exception");
        } catch (Exception ignored) {
        }
    }

    @Test
    public void createNewQuestInvalidInput() {
        DbConnection dbConnection = DbConnection.getInstance();
        try {
            //noinspection ConstantConditions
            dbConnection.createNewQuest(null, null);
            fail("DbConnection.createNewQuest() did not threw an exception");
        } catch (Exception ignored) {
        }
    }

    @Test
    public void updateQuestListAndCreateInvalidInput() {
        DbConnection dbConnection = DbConnection.getInstance();
        try {
            dbConnection.updateQuestListAndCreate(null, new Quest(null, null, null, null, null, null), null);
            fail("DbConnection.updateQuestListAndCreate() did not threw an exception");
        } catch (Exception ignored) {
        }
    }

    @Test
    public void deleteUserQuestsInvalidInput() {
        DbConnection dbConnection = DbConnection.getInstance();
        try {
            dbConnection.deleteUserQuests(null);
            fail("DbConnection.deleteUserAndQuests() did not throw an exception");
        } catch (Exception ignored) {
        }
    }

    @Test
    public void deleteQuestInvalidInput() {
        DbConnection dbConnection = DbConnection.getInstance();
        try {
            //noinspection ConstantConditions
            dbConnection.deleteQuest(null);
            fail("DbConnection.deleteUserAndQuests() did not threw an exception");
        } catch (Exception ignored) {
        }
    }
}
