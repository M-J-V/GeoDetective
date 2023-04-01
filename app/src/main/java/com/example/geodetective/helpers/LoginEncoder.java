package com.example.geodetective.helpers;
import java.security.MessageDigest;
import java.security.NoSuchAlgorithmException;

public class LoginEncoder {

    // Method to hash a password using SHA-256 algorithm
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
