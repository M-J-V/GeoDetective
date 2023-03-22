package com.example.geodetective;

import static org.junit.Assert.assertEquals;
import org.junit.Test;

public class LoginEncoderTest {

    @Test
    public void testHashWord() {
        String word = "password123";
        String expectedHashedWord = "ef92b778bafe771e89245b89ecbc08a44a4e166c06659911881f383d4473e94f";
        String hashedWord = LoginEncoder.hashWord(word);
        assertEquals(expectedHashedWord, hashedWord);
    }
}
