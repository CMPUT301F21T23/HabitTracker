package com.example.habittracker.activities;

import androidx.appcompat.app.AppCompatActivity;

import android.content.Intent;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.EditText;
import android.widget.Toast;

import com.example.habittracker.DatabaseManager;
import com.example.habittracker.Habit;
import com.example.habittracker.R;
import com.example.habittracker.User;
import com.example.habittracker.activities.tracking.ProgressUpdater;
import com.example.habittracker.utils.CheckPasswordCallback;
import com.example.habittracker.utils.HabitListCallback;
import com.example.habittracker.utils.SharedInfo;
import com.example.habittracker.utils.UserExistsCallback;

import java.math.BigInteger;
import java.security.NoSuchAlgorithmException;
import java.security.SecureRandom;
import java.security.spec.InvalidKeySpecException;
import java.util.ArrayList;
import java.util.HashMap;

import javax.crypto.SecretKeyFactory;
import javax.crypto.spec.PBEKeySpec;

/**
 * This login activity is the first screen you are greeted with when opening the app, and
 * the user must log in or register.
 * After logging out from within the app, the user is brought back here.
 */

public class LoginActivity extends AppCompatActivity {
    EditText usernameField;
    EditText passwordField;
    boolean loggedIn = false;

    // Hashing code taken from https://howtodoinjava.com/java/java-security/how-to-generate-secure-password-hash-md5-sha-pbkdf2-bcrypt-examples/#PBKDF2WithHmacSHA1
    // Functions generateStrongPasswordHash, getSalt, toHex, validatePassword, fromHex are copied from the link.
    // Never roll your own crypto!

    /**
     * From a password, derive a string containing a hash, a random salt, and the number of iterations used to derive the hash
     * @param password
     * @return
     * @throws NoSuchAlgorithmException
     * @throws InvalidKeySpecException
     */
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

    /**
     * Generate a random salt
     * @return
     * @throws NoSuchAlgorithmException
     */
    private static byte[] getSalt() throws NoSuchAlgorithmException {
        SecureRandom sr = SecureRandom.getInstance("SHA1PRNG");
        byte[] salt = new byte[16];
        sr.nextBytes(salt);
        return salt;
    }

    /**
     * Convert a byte array to a hexadecimal string
     * @param array
     * @return
     * @throws NoSuchAlgorithmException
     */
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

    /**
     * Given a password and a hash of the password, verify that the password hashes into the hash
     * @param originalPassword
     * @param storedPassword
     * @return
     * @throws NoSuchAlgorithmException
     * @throws InvalidKeySpecException
     */
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

    /**
     * Convert a hexadecimal string to a byte array
     * @param hex
     * @return
     * @throws NoSuchAlgorithmException
     */
    private static byte[] fromHex(String hex) throws NoSuchAlgorithmException {
        byte[] bytes = new byte[hex.length() / 2];
        for(int i = 0; i<bytes.length ;i++)
        {
            bytes[i] = (byte)Integer.parseInt(hex.substring(2 * i, 2 * i + 2), 16);
        }
        return bytes;
    }

    /**
     * Use the DatabaseManager class to create a document
     * @param username
     * @param hashedPassword
     */
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

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

        setContentView(R.layout.activity_login);

        usernameField = findViewById(R.id.editTextUsername);
        passwordField = findViewById(R.id.editTextPassword);
    }

    /**
     * Login button pressed
     * @param view
     * @throws InvalidKeySpecException
     * @throws NoSuchAlgorithmException
     */
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

    /**
     * Register button pressed
     * @param view
     * @throws InvalidKeySpecException
     * @throws NoSuchAlgorithmException
     */
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

    /**
     * This cannot be called from within the callback, so it is brought out here.
     */
    public void switchToHomeActivity() {

        DatabaseManager.get().getAllHabits(SharedInfo.getInstance().getCurrentUser().getUsername(), new HabitListCallback() {
            @Override
            public void onCallbackSuccess(ArrayList<Habit> habitList) {
                Intent intent = new Intent(getApplicationContext(), HomeActivity.class);
                int order = 0;
                for(Habit h:habitList){
                    ProgressUpdater updater = new ProgressUpdater(h,order);
                    updater.update();
                    order++;
                }
                startActivity(intent);
            }

            @Override
            public void onCallbackFailed() {
                Intent intent = new Intent(getApplicationContext(), HomeActivity.class);
                startActivity(intent);
            }
        });

    }
}