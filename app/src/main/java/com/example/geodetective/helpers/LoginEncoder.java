package com.example.geodetective.helpers;
import java.security.MessageDigest;
import java.security.NoSuchAlgorithmException;

/**
 * The LoginEncoder class provides a method to hash a string input using the SHA-256 algorithm.
 */
public class LoginEncoder {

    /**
     * The function takes a string input, hashes it using SHA-256 algorithm, and returns the hashed
     * value as a string.
     *
     * @param word The input word that needs to be hashed using the SHA-256 algorithm.
     * @return The method is returning a String representation of the SHA-256 hash of the input word.
     */
    public static String hashWord(String word) {
        try {
            MessageDigest md = MessageDigest.getInstance("SHA-256");
            byte[] hashedWord = md.digest(word.getBytes());
            StringBuilder sb = new StringBuilder();
            for (byte b : hashedWord) {
                sb.append(String.format("%02x", b));
            }
            return sb.toString();
        } catch (NoSuchAlgorithmException e) {
            e.printStackTrace();
        }
        return null;
    }
}
