package com.example.geodetective;

import static androidx.test.core.app.ApplicationProvider.getApplicationContext;

import android.app.Instrumentation;
import android.content.Context;
import android.content.res.Resources;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;

import androidx.test.InstrumentationRegistry;
import androidx.test.ext.junit.runners.AndroidJUnit4;

import junit.framework.TestCase;

import org.junit.Before;
import org.junit.Test;

import java.util.HashMap;
import java.util.Map;


public class DbConnectionTest extends TestCase {

//    DbConnection dbConnection;
//
//    @Before
//    public void setDbConnection() {
//        dbConnection = new DbConnection();
//    }

    @Test
    public void createNewQuestTest() {
        DbConnection dbConnection = DbConnection.getInstance();
        Context context = getApplicationContext();
        Bitmap image = BitmapFactory.decodeResource(Resources.getSystem(), R.drawable.auditorium);
        Location location = new Location(38.8951, -77.0364);
        Quest oneQuest = new Quest("Utrecht Quest", "Divo", "Find station", "Go To Utrecht",
                image, location);
        dbConnection.createNewQuest(oneQuest, context);
        assertTrue(dbConnection.quests.equals(oneQuest));

    }

//    @Test
//    public void createNewUserTest() {
//        DbConnection dbConnection = DbConnection.getInstance();
//        dbConnection.createNewUser("divo", "divo123!");
//        assertTrue(dbConnection.users.equals())
//    }


}