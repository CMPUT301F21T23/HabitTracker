package com.example.habittracker.activities;

import androidx.appcompat.app.AppCompatActivity;

import android.content.Intent;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.ArrayAdapter;
import android.widget.EditText;
import android.widget.ListView;
import android.widget.Toast;

import com.example.habittracker.DatabaseManager;
import com.example.habittracker.HabitActivity;
import com.example.habittracker.NavBarManager;
import com.example.habittracker.R;
import com.example.habittracker.User;
import com.example.habittracker.utils.BooleanCallback;
import com.example.habittracker.utils.CheckPasswordCallback;
import com.example.habittracker.utils.SharedInfo;
import com.example.habittracker.utils.UserExistsCallback;
import com.google.firebase.firestore.DocumentReference;

import java.math.BigInteger;
import java.security.NoSuchAlgorithmException;
import java.security.SecureRandom;
import java.security.spec.InvalidKeySpecException;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.HashMap;

import javax.crypto.SecretKeyFactory;
import javax.crypto.spec.PBEKeySpec;

public class LoginActivity extends AppCompatActivity {
    // Variables
    ListView profileList;
    ArrayAdapter<String> profileListAdapter;
    ArrayList<String> profileDataList;

    // Login page stuff
    EditText usernameField;
    EditText passwordField;
    boolean loggedIn = false;

    SecretKeyFactory keyFactory = null;

    // Hashing code taken from https://howtodoinjava.com/java/java-security/how-to-generate-secure-password-hash-md5-sha-pbkdf2-bcrypt-examples/#PBKDF2WithHmacSHA1
    // Never roll your own crypto!

    public static String generateStrongPasswordHash(String password) throws NoSuchAlgorithmException, InvalidKeySpecException {
        // Keep the iterations low enough so that the delay is negligible. 10000 iterations is about a second of hashing.
        int iterations = 1000;
        char[] chars = password.toCharArray();
        byte[] salt = getSalt();

        PBEKeySpec spec = new PBEKeySpec(chars, salt, iterations, 64 * 8);
        SecretKeyFactory skf = SecretKeyFactory.getInstance("PBKDF2WithHmacSHA1");
        byte[] hash = skf.generateSecret(spec).getEncoded();
        return iterations + ":" + toHex(salt) + ":" + toHex(hash);
    }

    private static byte[] getSalt() throws NoSuchAlgorithmException {
        SecureRandom sr = SecureRandom.getInstance("SHA1PRNG");
        byte[] salt = new byte[16];
        sr.nextBytes(salt);
        return salt;
    }

    private static String toHex(byte[] array) throws NoSuchAlgorithmException {
        BigInteger bi = new BigInteger(1, array);
        String hex = bi.toString(16);
        int paddingLength = (array.length * 2) - hex.length();
        if(paddingLength > 0)
        {
            return String.format("%0"  +paddingLength + "d", 0) + hex;
        }else{
            return hex;
        }
    }

    public static boolean validatePassword(String originalPassword, String storedPassword) throws NoSuchAlgorithmException, InvalidKeySpecException {
        String[] parts = storedPassword.split(":");
        int iterations = Integer.parseInt(parts[0]);
        byte[] salt = fromHex(parts[1]);
        byte[] hash = fromHex(parts[2]);

        PBEKeySpec spec = new PBEKeySpec(originalPassword.toCharArray(), salt, iterations, hash.length * 8);
        SecretKeyFactory skf = SecretKeyFactory.getInstance("PBKDF2WithHmacSHA1");
        byte[] testHash = skf.generateSecret(spec).getEncoded();

        int diff = hash.length ^ testHash.length;
        for(int i = 0; i < hash.length && i < testHash.length; i++)
        {
            diff |= hash[i] ^ testHash[i];
        }
        return diff == 0;
    }

    private static byte[] fromHex(String hex) throws NoSuchAlgorithmException {
        byte[] bytes = new byte[hex.length() / 2];
        for(int i = 0; i<bytes.length ;i++)
        {
            bytes[i] = (byte)Integer.parseInt(hex.substring(2 * i, 2 * i + 2), 16);
        }
        return bytes;
    }

    // Use the DatabaseManager class to create a document
    void dbCreateUser(String username, String hashedPassword) {
        HashMap<String, Object> userDocument = new HashMap<>();

        // Add data for the user document
        String userid = username;
        ArrayList<String> following = new ArrayList<>();
        ArrayList<String> followers = new ArrayList<>();
        ArrayList<String> pendingFollowReqs = new ArrayList<>();
        ArrayList<String> pendingFollowerReqs = new ArrayList<>();
        userDocument.put("following", following);
        userDocument.put("followers", followers);
        userDocument.put("pendingFollowReqs", pendingFollowReqs);
        userDocument.put("pendingFollowerReqs", pendingFollowerReqs);
        userDocument.put("hashedPassword", hashedPassword);

        // Use database manager
        DatabaseManager.get().addUsersDocument(userid, userDocument);
    }

    // Find the user with id "username" and return the hashed password from that
    String dbGetUserPasswordHash(String username) {
        //DocumentReference doc = DatabaseManager.get().getUsersColRef().document("Users/a").get().the;
        return "yo";
    }

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

        setContentView(R.layout.activity_login);

        usernameField = findViewById(R.id.editTextUsername);
        passwordField = findViewById(R.id.editTextPassword);

        //NavBarManager nav = new NavBarManager(this,findViewById(R.id.bottom_navigation));

    }

    public void loginPressed(View view) throws InvalidKeySpecException, NoSuchAlgorithmException {
        String username = usernameField.getText().toString();

        if (username.length() == 0) {
            Toast.makeText(LoginActivity.this, "Username cannot be empty!", Toast.LENGTH_SHORT).show();
            return;
        }

        DatabaseManager db = DatabaseManager.get();

        db.userExists(username, new UserExistsCallback() {
            @Override
            public void onCallbackSuccess(String username) {
                Log.d("User","exists");

                System.out.println("User " + username + " exists!");

                db.checkPassword(username, new CheckPasswordCallback() {
                    @Override
                    public void onCallbackSuccess(String username, String hashedPassword) throws InvalidKeySpecException, NoSuchAlgorithmException {

                        boolean match = validatePassword(passwordField.getText().toString(), hashedPassword);
                        if (match) {
                            System.out.println("Hashes match!");
                            Log.d("Password match", "" + match);

                            // Password is correct and the user is logged in
                            Toast.makeText(LoginActivity.this, "Logged in!", Toast.LENGTH_SHORT).show();
                            loggedIn = true;

                            // Set the global user instance
                            User loggedInUser = new User(username);
                            SharedInfo.getInstance().setCurrentUser(loggedInUser);

                            // Go to the home screen once logged in
                            switchToHomeActivity();
                        } else {
                            System.out.println("Hashes DON'T match!");
                            // Password doesn't match
                            Toast.makeText(LoginActivity.this, "Incorrect password!", Toast.LENGTH_SHORT).show();
                        }
                    }

                    @Override
                    public void onCallbackFailed() {
                        Log.d("Error", "Failed to check for password match");
                    }
                });
            }

            @Override
            public void onCallbackFailed() {
                Log.d("Error","Failed to get user");

                System.out.println("User " + username + " does not exist!");

                // Username doesn't exist
                Toast.makeText(LoginActivity.this, "This user doesn't exist. Please register.", Toast.LENGTH_SHORT).show();
            }
        });
    }

    public void registerPressed(View view) throws InvalidKeySpecException, NoSuchAlgorithmException {
        //DatabaseManager.get().getUsersColName()

        String username = usernameField.getText().toString();

        if (username.length() == 0) {
            Toast.makeText(LoginActivity.this, "Username cannot be empty!", Toast.LENGTH_SHORT).show();
            return;
        }
        if (passwordField.getText().toString().length() < 6) {
            Toast.makeText(LoginActivity.this, "Password must be at least 6 characters long!", Toast.LENGTH_SHORT).show();
            return;
        }
        if (username.equals(passwordField.getText().toString())) {
            Toast.makeText(LoginActivity.this, "Username cannot be identical to the password!", Toast.LENGTH_SHORT).show();
            return;
        }

        String hashedPassword = generateStrongPasswordHash(passwordField.getText().toString());

        DatabaseManager db = DatabaseManager.get();

        db.userExists(username, new UserExistsCallback() {
            @Override
            public void onCallbackSuccess(String s) {
                // User exists
                System.out.println("User " + username + " exists!");
                Log.d("User","User exists");

                // This user already exists
                Toast.makeText(LoginActivity.this, "User already exists. Please log in.", Toast.LENGTH_SHORT).show();
            }

            @Override
            public void onCallbackFailed() {
                Log.d("Error","Failed to get user");

                System.out.println("User " + username + " does not exist!");

                // Register this new user
                System.err.println(username + " " + hashedPassword);
                //userdb.put(username, hashedPassword);
                Toast.makeText(LoginActivity.this, "New user registered.", Toast.LENGTH_SHORT).show();
                loggedIn = true;

                // Push this stuff into the cloud database
                dbCreateUser(username, hashedPassword);
                SharedInfo.getInstance().setCurrentUser(new User(username));

                switchToHomeActivity();
            }
        });
    }

    public void switchToHomeActivity() {
        Intent intent = new Intent(this, HomeActivity.class);
        startActivity(intent);
    }
/*
    public void logoutPressed(View view) {
        if (loggedIn) {
            loggedIn = false;
            Toast.makeText(LoginActivity.this, "Logged out!", Toast.LENGTH_SHORT).show();
        } else {
            Toast.makeText(LoginActivity.this, "You cannot log out if you aren't logged in to begin with.", Toast.LENGTH_SHORT).show();
        }
    }*/
}