package com.example.geodetective;

/**
 * A singleton class that checks that the details of the account
 * to be created are within the specifications.
 */
public class AccountDetailsChecker {

    // the instance of the class
    private static AccountDetailsChecker instance = null;

    private AccountDetailsChecker() { }

    /**
     * A static method that returns the instance of the class.
     *
     * @return an instance of the singleton class.
     */
    public static AccountDetailsChecker getInstance() {
        if (instance == null) {
            instance = new AccountDetailsChecker();
        }

        return instance;
    }

    /**
     * Checks the correctness of the inputted details.
     *
     * @param username input string
     * @param password input string
     * @param passwordAgain input string
     * @throws IllegalArgumentException if account details do not respect the specifications
     * @return true if the details are respecting the specification
     */
    public boolean checkDetails(String username, String password,
                                String passwordAgain) throws IllegalArgumentException{
        return (checkUsername(username) && checkPassword(password, passwordAgain));
    }

    private boolean checkUsername(String username) throws IllegalArgumentException {

        if (username.length() < 4) {
            throw new IllegalArgumentException("The username length should be at least 4");
        }

        if (usernameInDatabase(username)) {
            throw new IllegalArgumentException("The username is not available.");
        }

        return true;
    }

    private boolean checkPassword(String password, String passwordAgain) throws IllegalArgumentException{
        if (password.length() <= 10) {
            throw new IllegalArgumentException("The password should have more than 10 characters.");
        }

        String specialCharacters = "@#$%&*.,?!";
        boolean letterIsPresent = false;
        boolean numberIsPresent = false;
        boolean specialCharacterPresent = false;
        for (int i = 0; i < password.length(); i++) {
            if (Character.isLetter(password.charAt(i))) {
                letterIsPresent = true;
            }

            if (Character.isDigit(password.charAt(i))) {
                numberIsPresent = true;
            }

            if (specialCharacters.indexOf(password.charAt(i)) != -1) {
                specialCharacterPresent = true;
            }
        }

        if (!letterIsPresent) {
            throw new IllegalArgumentException("The password should contain a letter.");
        }

        if (!numberIsPresent) {
            throw new IllegalArgumentException("The password should contain a number.");
        }

        if (!specialCharacterPresent) {
            throw new IllegalArgumentException("The password should contain a special character.");
        }

        if (password.compareTo(passwordAgain) != 0) {
            throw new IllegalArgumentException("The two passwords do not match.");
        }

        return true;
    }

    private boolean usernameInDatabase(String username) {
        return false;
    }

    /**
     * This method offers more information on why the details are wrong.
     * It first checks the username and returns information on that (if it is wrong),
     * then check the correctness of the password, then checks that the two passwords
     * are the same.
     *
     * @param username input string
     * @param password input string
     * @param passwordAgain input string
     * @return a string with information on why the details are not correct
     * according to the specification
     */
    public void getCheckerErrorInformation(String username, String password,
                                             String passwordAgain) { }
}
