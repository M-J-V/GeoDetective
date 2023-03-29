package com.example.geodetective;

import static org.junit.Assert.fail;

import org.junit.Test;

public class DbConnectionTest {

    //TODO: For some reason we can not send data to the database from the tests. We need to figure out why.
    @Test
    public void getInstanceNotNull(){
        DbConnection dbConnection = DbConnection.getInstance();
        if(dbConnection == null) {
            fail("DbConnection.getInstance() returned null");
        }
    }

    @Test
    public void sendRequestInvalidInput() {
        DbConnection dbConnection = DbConnection.getInstance();
        try {
            dbConnection.sendRequest(null);
            fail("DbConnection.sendRequest() did not threw an exception");
        } catch (Exception ignored) {}
    }

//    @Test
//    public void sendRequestValidInput() {
//        DbConnection dbConnection = DbConnection.getInstance();
//        dbConnection.sendRequest("test137");
//        //Check if the message arrived
////        dbConnection.requests.get().addOnSuccessListener((OnSuccessListener<QuerySnapshot>) queryDocumentSnapshots -> {
////            for (QueryDocumentSnapshot document : queryDocumentSnapshots) {
////                if (document.get("test").equals("test has requested permission to create quests")) {
////                    fail("DbConnection.sendRequest() did not send the correct message");
////                }
////            }
////        });
//        //Remove the request
////        dbConnection.removeRequest("test");
//    }

}