//package com.example.geodetective;
//
//<<<<<<< HEAD
//import static androidx.test.core.app.ApplicationProvider.getApplicationContext;
//
//import static com.example.geodetective.DbConnection.getInstance;
//
//import android.app.Instrumentation;
//import android.content.Context;
//import android.content.res.Resources;
//import android.graphics.Bitmap;
//import android.graphics.BitmapFactory;
//
//import androidx.test.InstrumentationRegistry;
//import androidx.test.ext.junit.runners.AndroidJUnit4;
//
//import junit.framework.TestCase;
//
//import org.junit.Before;
//import org.junit.Test;
//
//import java.util.HashMap;
//import java.util.Map;
//
//
//public class DbConnectionTest extends TestCase {
//
//    DbConnection dbConnection;
//
//    @Before
//    public void setDbConnection() {
//        dbConnection = getInstance();
//    }
//
//    @Test
//    public void createNewQuestTest() {
//        Context context = getApplicationContext();
//        Bitmap image = BitmapFactory.decodeResource(Resources.getSystem(), R.drawable.auditorium);
//        Location location = new Location(38.8951, -77.0364);
//        Quest oneQuest = new Quest("Utrecht Quest", "Divo", "Find station", "Go To Utrecht",
//                image, location);
//        dbConnection.createNewQuest(oneQuest, context);
//        assertTrue(dbConnection.quests.equals(oneQuest));
//
//    }
//
//    @Test
//    public void testSendRequest() {
//        dbConnection.sendRequest("dìvo");
//    }
//
////    @Test
////    public void createNewUserTest() {
////        DbConnection dbConnection = DbConnection.getInstance();
////        dbConnection.createNewUser("divo", "divo123!");
////        assertTrue(dbConnection.users.equals())
////    }
//
//
//import static org.junit.Assert.fail;
//
//import org.junit.Test;
//
//public class DbConnectionTest {
//
//    //TODO: For some reason we can not send data to the database from the tests. We need to figure out why.
//    @Test
//    public void getInstanceNotNull(){
//        DbConnection dbConnection = DbConnection.getInstance();
//        if(dbConnection == null) {
//            fail("DbConnection.getInstance() returned null");
//        }
//    }
//
//    @Test
//    public void sendRequestInvalidInput() {
//        DbConnection dbConnection = DbConnection.getInstance();
//        try {
//            dbConnection.sendRequest(null);
//            fail("DbConnection.sendRequest() did not threw an exception");
//        } catch (Exception ignored) {}
//    }
//
////    @Test
////    public void sendRequestValidInput() {
////        DbConnection dbConnection = DbConnection.getInstance();
////        dbConnection.sendRequest("test137");
////        //Check if the message arrived
//////        dbConnection.requests.get().addOnSuccessListener((OnSuccessListener<QuerySnapshot>) queryDocumentSnapshots -> {
//////            for (QueryDocumentSnapshot document : queryDocumentSnapshots) {
//////                if (document.get("test").equals("test has requested permission to create quests")) {
//////                    fail("DbConnection.sendRequest() did not send the correct message");
//////                }
//////            }
//////        });
////        //Remove the request
//////        dbConnection.removeRequest("test");
////    }
