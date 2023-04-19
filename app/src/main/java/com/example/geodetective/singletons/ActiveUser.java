package com.example.geodetective.singletons;

/**
 * The ActiveUser class stores the information of the account that is currently logged in.
 */
public class ActiveUser {
    private String username;
    private String password;
    private boolean trusted;

    private static ActiveUser User = null;

    private ActiveUser() {
        this.username = null;
        this.password = null;
        this.trusted = false;
    }

    /**
     * This method returns the already existing instance of the class or a
     * new one (if it doesn't exist already).
     * @return instance of the class
     */
    public static ActiveUser getInstance() {
        if (User == null) {
            User = new ActiveUser();
        }
        return User;
    }

    /**
     * This method returns the username of the active account.
     * @return username of active account
     */
    public String getUsername() {
        return username;
    }

    /**
     * This method returns the password of the active account.
     * @return password of active account.
     */
    public String getPassword() {
        return password;
    }

    /**
     * This method returns whether the active account is trusted or not.
     * @return whether active account is trusted or not.
     */
    public boolean getTrusted() {
        return trusted;
    }

    /**
     * This method sets the new username of the active account.
     * @param username new username
     */
    public void setUsername(String username) {
        this.username = username;
    }

    /**
     * This method sets the new password of the active account.
     * @param password new password
     */
    public void setPassword(String password) {
        this.password = password;
    }

    /**
     * This method sets whether the active account is trusted or not
     * @param trust whether the active account to become trusted
     */
    public void setTrusted(boolean trust) {
        this.trusted = trust;
    }

    /**
     * This method logs user out (no current active account).
     */
    public void disconnectUser() {
        this.User = null;
        this.username = "";
        this.password = "";
        this.trusted = false;
    }

}
