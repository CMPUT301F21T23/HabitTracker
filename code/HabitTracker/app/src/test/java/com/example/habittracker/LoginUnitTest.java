package com.example.habittracker;

import static com.example.habittracker.activities.LoginActivity.generateStrongPasswordHash;
import static com.example.habittracker.activities.LoginActivity.validatePassword;

import static org.junit.Assert.assertFalse;
import static org.junit.Assert.assertTrue;

//import org.junit.Test;
import org.junit.jupiter.api.Test;

import java.security.NoSuchAlgorithmException;
import java.security.spec.InvalidKeySpecException;

/**
 * Test to make sure that a password which gets hashed can then be verified as a match
 */

public class LoginUnitTest {
    @Test
    public void testPasswordHashMatching() throws InvalidKeySpecException, NoSuchAlgorithmException {
        String password = "monkey123456p@s5w0rd";
        String hashedPassword = "";
        try {
            hashedPassword = generateStrongPasswordHash(password);
        } catch (NoSuchAlgorithmException e) {
            e.printStackTrace();
        } catch (InvalidKeySpecException e) {
            e.printStackTrace();
        }

        assertTrue(validatePassword(password, hashedPassword));
        assertFalse(validatePassword(password + " ", hashedPassword));
    }
}
