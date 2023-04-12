package com.example.geodetective;

import org.junit.After;
import org.junit.Before;
import org.junit.Test;

import static org.junit.Assert.*;

import com.example.geodetective.singletons.ActiveUser;

public class ActiveUserTest {

    private ActiveUser user;

    /**
     * This function initializes the user. This is done to avoid initializing
     * user for every test functions made.
     */
    @Before
    public void setUp() {
        user = ActiveUser.getInstance();
    }

    /**
     * This disconnects the user after all unit tests are executed.
     */
    @After
    public void tearDown() {
        user.disconnectUser();
    }

    /**
     * This function tests if the instances are the same
     */
    @Test
    public void testgetInstance() {
        ActiveUser anotherUser = ActiveUser.getInstance();
        assertSame(user, anotherUser);
    }

    /**
     * This function tests if the username is the same as the attribute user
     * after we have set a username into user.
     */
    @Test
    public void testSetAndGetUsername() {
        String username = "testuser";
        user.setUsername(username);
        assertEquals(username, user.getUsername());
    }

    /**
     * This function tests if the password is the same as the password in
     * user after we have set a password into user.
     */
    @Test
    public void testSetAndGetPassword() {
        String password = "testpassword";
        user.setPassword(password);
        assertEquals(password, user.getPassword());
    }

    /**
     * This function tests if the boolean trusted is the same
     * as the trusted in user after we have set a
     * new trusted into user.
     */
    @Test
    public void testSetAndGetTrusted() {
        boolean trusted = true;
        user.setTrusted(trusted);
        assertEquals(trusted, user.getTrusted());
    }

    /**
     * This function tests if the boolean trusted is the same
     * as the trusted in user after we have set a
     * new trusted into user.
     */
    @Test
    public void testDisconnectUserEmptyPassword() {
        user.setPassword("testpassword");
        user.disconnectUser();
        assertEquals(user.getPassword(), "");
    }

    /**
     * This function tests that the "disconnectUser" method sets
     * the "trusted" field
     * of the ActiveUser instance to false when
     * it was previously set to true.
     */
    @Test
    public void testDisconnectUserTrustedIsFalse() {
        user.setTrusted(true);
        user.disconnectUser();
        assertFalse(user.getTrusted());
    }

    /**
     * This function tests that the username is reset to an empty string
     * after calling the disconnectUser() method.
     */
    @Test
    public void testDisconnectUserEmptyUserName() {
        user.setUsername("testuser");
        user.disconnectUser();
        assertEquals(user.getUsername(), "");
    }

}
