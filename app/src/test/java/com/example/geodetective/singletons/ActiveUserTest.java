package com.example.geodetective.singletons;

import static org.junit.Assert.*;

import org.junit.Before;
import org.junit.Test;

public class ActiveUserTest {

    private ActiveUser activeUser;

    @Before
    public void setUp() {
        activeUser = ActiveUser.getInstance();
    }

    @Test
    public void testGetInstance() {
        ActiveUser newUser = ActiveUser.getInstance();
        assertEquals(activeUser, newUser);
    }

    @Test
    public void testSetAndGetUsername() {
        String username = "testuser";
        activeUser.setUsername(username);
        assertEquals(username, activeUser.getUsername());
    }

    @Test
    public void testSetAndGetPassword() {
        String password = "testpassword";
        activeUser.setPassword(password);
        assertEquals(password, activeUser.getPassword());
    }

    @Test
    public void testSetAndGetTrusted() {
        boolean trusted = true;
        activeUser.setTrusted(trusted);
        assertEquals(trusted, activeUser.getTrusted());
    }

    @Test
    public void testDisconnectUser() {
        activeUser.disconnectUser();
        assertNotNull(activeUser.getUsername(), "");
        assertEquals(activeUser.getPassword(), "");
        assertEquals(activeUser.getTrusted(), false);
        //assertNull(ActiveUser.getInstance());
    }

}