package com.example.geodetective;

import static org.junit.Assert.fail;

import com.example.geodetective.gameComponents.Quest;
import com.example.geodetective.singletons.DbConnection;

import org.junit.Test;

public class DbConnectionTest {
    /*
    Note that we can not check if DbConnection actually sends files to the server,
    because firebase only works from within the app and we can not check it with JUnit.
    */

    /**
     * This function tests if the DbConnection.getInstance() method returns a non-null object.
     */
    @Test
    public void getInstanceNotNull() {
        DbConnection dbConnection = DbConnection.getInstance();
        if (dbConnection == null) {
            fail("DbConnection.getInstance() returned null");
        }
    }

    /**
     * This is a JUnit test case that checks if an exception is thrown when an invalid input is passed
     * to the sendRequest method of the DbConnection class.
     */
    @Test
    public void sendRequestInvalidInput() {
        DbConnection dbConnection = DbConnection.getInstance();
        try {
            dbConnection.sendRequest(null);
            fail("DbConnection.sendRequest() did not threw an exception");
        } catch (Exception ignored) {
        }
    }
    /**
     * This is a JUnit test case that checks if an exception is thrown when invalid input is passed to
     * the createAttempt() method of the DbConnection class in Java.
     */
    @Test
    public void createAttemptInvalidInput() {
        DbConnection dbConnection = DbConnection.getInstance();
        try {
            dbConnection.createAttempt(null, null, false);
            fail("DbConnection.createAttempt() did not threw an exception");
        } catch (Exception ignored) {
        }
    }

    /**
     * This is a JUnit test case to check if an exception is thrown when invalid input is passed to the
     * createNewUser() method of the DbConnection class in Java.
     */
    @Test
    public void createUserInvalidInput() {
        DbConnection dbConnection = DbConnection.getInstance();
        try {
            dbConnection.createNewUser(null, null, false);
            fail("DbConnection.createNewUser() did not threw an exception");
        } catch (Exception ignored) {
        }
    }

    /**
     * This is a JUnit test case that checks if an exception is thrown when invalid input is passed to
     * the createNewQuest() method of the DbConnection class in Java.
     */
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

    /**
     * This is a JUnit test case for the updateQuestListAndCreate() method in the DbConnection class,
     * which tests for invalid input.
     */
    @Test
    public void updateQuestListAndCreateInvalidInput() {
        DbConnection dbConnection = DbConnection.getInstance();
        try {
            dbConnection.updateQuestListAndCreate(null, new Quest(null, null, null, null, null, null), null);
            fail("DbConnection.updateQuestListAndCreate() did not threw an exception");
        } catch (Exception ignored) {
        }
    }

    /**
     * This is a JUnit test case that checks if an exception is thrown when null input is passed to the
     * deleteUserQuests method of the DbConnection class.
     */
    @Test
    public void deleteUserQuestsInvalidInput() {
        DbConnection dbConnection = DbConnection.getInstance();
        try {
            dbConnection.deleteUserQuests(null);
            fail("DbConnection.deleteUserAndQuests() did not throw an exception");
        } catch (Exception ignored) {
        }
    }

    /**
     * This is a JUnit test case to check if an exception is thrown when null input is passed to the
     * deleteQuest method of the DbConnection class.
     */
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
