package com.example.geodetective;

/**
 * A singleton class that checks two conditions (exclusively):
 *  - that the details of the account to be created are within
 *  the specifications.
 *  - that the login details are existent and correct in the database.
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

    boolean checkUsername(String username) throws IllegalArgumentException {

        if (username.length() < 4) {
            throw new IllegalArgumentException("The username length should be at least 4");
        }

        return true;
    }

    boolean checkPassword(String password, String passwordAgain) throws IllegalArgumentException{
        if (password.length() <= 5) {
            throw new IllegalArgumentException("The password should have more than 5 characters.");
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

}
