package com.example.habittracker.utils;

import java.security.NoSuchAlgorithmException;
import java.security.spec.InvalidKeySpecException;

public interface CheckPasswordCallback {
    /**
     * success callback
     * @param username
     * @param hashedPassword
     */
    void onCallbackSuccess(String username,String hashedPassword) throws InvalidKeySpecException, NoSuchAlgorithmException;

    /**
     * called when task is unsuccessful
     */
    void onCallbackFailed();
}
