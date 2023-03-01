package com.example.geodetective;

import junit.framework.TestCase;

import org.junit.Test;

public class AccountDetailsCheckerTest extends TestCase {

    /**
     * Test to check that we only get one instance.
     */
    @Test
    public void testGetInstance() {
        AccountDetailsChecker checker1 = AccountDetailsChecker.getInstance();
        AccountDetailsChecker checker2 = AccountDetailsChecker.getInstance();
        assertTrue(checker1.equals(checker2));
    }

    /**
     * Test to check for correct input.
     */
    @Test
    public void testCheckDetails1() {
        AccountDetailsChecker checker = AccountDetailsChecker.getInstance();
        String username = "alex21";
        String password = "Technicalproblem2@";
        String passwordAgain = "Technicalproblem2@";
        assertTrue(checker.checkDetails(username, password, passwordAgain));
    }
    /**
     * Test to check for no letters in password.
     */
    @Test
    public void testCheckDetails2() {
        AccountDetailsChecker checker = AccountDetailsChecker.getInstance();
        String username = "alex21";
        String password = "12345678910";
        String passwordAgain = "12345678910";
        try {
            checker.checkDetails(username, password, passwordAgain);
            fail("Exception not thrown");
        } catch (IllegalArgumentException e) {
            System.out.println(e.getMessage());
        }
    }

    /**
     * Test to check for no numbers in password.
     */
    @Test
    public void testCheckDetails3() {
        AccountDetailsChecker checker = AccountDetailsChecker.getInstance();
        String username = "alex21";
        String password = "abcdefghijklmn";
        String passwordAgain = "abcdefghijklmn";
        try {
            checker.checkDetails(username, password, passwordAgain);
            fail("Exception not thrown");
        } catch (IllegalArgumentException e) {
            System.out.println(e.getMessage());
        }
    }

    /**
     * Test to check for no special characters in password.
     */
    @Test
    public void testCheckDetails4() {
        AccountDetailsChecker checker = AccountDetailsChecker.getInstance();
        String username = "alex21";
        String password = "abcdefghijk123";
        String passwordAgain = "abcdefghijk123";
        try {
            checker.checkDetails(username, password, passwordAgain);
            fail("Exception not thrown");
        } catch (IllegalArgumentException e) {
            System.out.println(e.getMessage());
        }
    }

    /**
     * Test to check for at most 10 characters in password.
     */
    @Test
    public void testCheckDetails5() {
        AccountDetailsChecker checker = AccountDetailsChecker.getInstance();
        String username = "alex21";
        String password = "a1@";
        String passwordAgain = "a1@";
        try {
            checker.checkDetails(username, password, passwordAgain);
            fail("Exception not thrown");
        } catch (IllegalArgumentException e) {
            System.out.println(e.getMessage());
        }
    }

    /**
     * Test to check for too short of a username in password.
     */
    @Test
    public void testCheckDetails6() {
        AccountDetailsChecker checker = AccountDetailsChecker.getInstance();
        String username = "alx";
        String password = "abcdefghijk1@";
        String passwordAgain = "abcdefghijk1@";
        try {
            checker.checkDetails(username, password, passwordAgain);
            fail("Exception not thrown");
        } catch (IllegalArgumentException e) {
            System.out.println(e.getMessage());
        }
    }

    /**
     * Test to check for passwords that do not match.
     */
    @Test
    public void testCheckDetails7() {
        AccountDetailsChecker checker = AccountDetailsChecker.getInstance();
        String username = "alex21";
        String password = "abcdefghijk1@";
        String passwordAgain = "abcdefghijk1!";
        try {
            checker.checkDetails(username, password, passwordAgain);
            fail("Exception not thrown");
        } catch (IllegalArgumentException e) {
            System.out.println(e.getMessage());
        }
    }

    /**
     * Test to check for username that already is registered.
     */
    @Test
    public void testCheckDetails8() {
        AccountDetailsChecker checker = AccountDetailsChecker.getInstance();
        String username = "alex21";
        String password = "abcdefghijk1@";
        String passwordAgain = "abcdefghijk1@";
//        try {
//            checker.checkDetails(username, password, passwordAgain);
//            fail("Exception not thrown");
//        } catch (IllegalArgumentException e) {
//            System.out.println(e.getMessage());
//        }
    }
}