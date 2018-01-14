package com.android.group0674.onlinestore.View;

import android.Manifest;
import android.app.KeyguardManager;
import android.content.Context;
import android.content.Intent;
import android.content.SharedPreferences;
import android.content.pm.PackageManager;
import android.hardware.fingerprint.FingerprintManager;
import android.os.Build;
import android.security.keystore.KeyGenParameterSpec;
import android.security.keystore.KeyPermanentlyInvalidatedException;
import android.security.keystore.KeyProperties;
import android.support.v4.app.ActivityCompat;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.Button;
import android.widget.TextView;

import com.android.group0674.onlinestore.Controller.Employee.AuthenticateOnClickController;
import com.android.group0674.onlinestore.Controller.Employee.EmployeeSignUpOnClickController;
import com.android.group0674.onlinestore.Controller.FingerPrintController;
import com.android.group0674.onlinestore.Model.database.DatabaseInsertHelper;
import com.android.group0674.onlinestore.Model.database.DatabaseSelectHelper;
import com.android.group0674.onlinestore.R;

import java.io.IOException;
import java.security.InvalidAlgorithmParameterException;
import java.security.InvalidKeyException;
import java.security.KeyStore;
import java.security.KeyStoreException;
import java.security.NoSuchAlgorithmException;
import java.security.NoSuchProviderException;
import java.security.UnrecoverableKeyException;
import java.security.cert.CertificateException;

import javax.crypto.Cipher;
import javax.crypto.KeyGenerator;
import javax.crypto.NoSuchPaddingException;
import javax.crypto.SecretKey;

/**
 * Activity that serves as the User's entry point into the application
 * containing the log in information for a user
 */
public class MainActivity extends AppCompatActivity {

    //declaring variables for the buttons
    private Button btnAdmin;
    private Button btnEmployee;
    private Button btnCustomer;

    // variables for shared preferences
    public static final String MyPREFERENCES = "MyPrefs";
    public static final String setup = "setUp";
    SharedPreferences sharedPreferences;

    // Declare a string variable for the key to use in the fingerprint authentication
    private static final String KEY_NAME = "yourKey";
    private Cipher cipher;
    private KeyStore keyStore;
    private KeyGenerator keyGenerator;
    private TextView textView;
    private FingerprintManager.CryptoObject cryptoObject;
    private FingerprintManager fingerprintManager;
    private KeyguardManager keyguardManager;

    /**
     * Runs when this activity is first created.
     * @param savedInstanceState
     */
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        // Must use sharedPreferences to find whether setup needs to run or not
        sharedPreferences = getSharedPreferences(MyPREFERENCES, Context.MODE_PRIVATE);

        SharedPreferences.Editor editor = sharedPreferences.edit();

        // Checks if the initial setup has been done
        if(sharedPreferences.getInt(setup, -1) != 1) {
            // If not then stores information onto the device's storage
            editor.putInt(setup, 1);
            editor.apply();
            // Switching activites to initialize the database
            Intent initialize = new Intent(this, InitDatabaseActivity.class); // com.questoid.sqlitebrowser.jar
            startActivity(initialize);
        }

        // Sets up the sign up button for Employee to allow employees to sign up
        Button userLogInBtn = (Button)findViewById(R.id.btnLogIn);

        // Sets the behaviour of the employee button when clicked
        userLogInBtn.setOnClickListener(new AuthenticateOnClickController(this));

        // We are in the login activity at this point for Users




        // create employee
       /* int id = DatabaseInsertHelper.insertRole("CUSTOMER", this);
        Log.d("the id is ", Integer.toString(id));*/

        //int x = DatabaseSelectHelper.getRole

        // in the edge case where a user quits the app during the initialization process, we want to
        // check every time we load the main activity whether or not there is at least 1 user.
        // if there are no users, redirect the app straight to the employee sign up page
        retryInitialization();

        // Fingerprint Code
        textView = (TextView) findViewById(R.id.fingerprintTxt);
        // has to be proper sdk version
        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.M) {
            // Get an instance of KeyguardManager and FingerprintManager
            keyguardManager =
                    (KeyguardManager) getSystemService(KEYGUARD_SERVICE);
            fingerprintManager =
                    (FingerprintManager) getSystemService(FINGERPRINT_SERVICE);

            // Check whether the device has a fingerprint sensor
            if (!fingerprintManager.isHardwareDetected()) {
                // If a fingerprint sensor isn’t available, then inform the user
                textView.setText("Your device doesn't support fingerprint authentication");
            }
            // Check whether the user has granted the USE_FINGERPRINT permission
            if (ActivityCompat.checkSelfPermission(this, Manifest.permission.USE_FINGERPRINT) != PackageManager.PERMISSION_GRANTED) {
                // If your app doesn't have this permission, then display the following text//
                textView.setText("Please enable the fingerprint permission");
            }

            // Check that the user has registered at least one fingerprint
            if (!fingerprintManager.hasEnrolledFingerprints()) {
                // If the user hasn’t configured any fingerprints, then display the following message
                textView.setText("No fingerprint configured. Please register at least one fingerprint in your device's Settings");
            }

            // Check that the lockscreen is secured
            if (!keyguardManager.isKeyguardSecure()) {
                // If the user hasn’t secured their lockscreen with a PIN password or pattern, then display the following text
                textView.setText("Please enable lockscreen security in your device's Settings");
            } else {
                try {
                    generateKey();
                } catch (FingerprintException e) {
                    e.printStackTrace();
                }

                if (initCipher()) {
                    // If the cipher is initialized successfully, then create a CryptoObject instance
                    cryptoObject = new FingerprintManager.CryptoObject(cipher);

                    // for starting the authentication process (via the startAuth method) and processing the authentication process events
                    FingerPrintController helper = new FingerPrintController(this);
                    helper.startAuth(fingerprintManager, cryptoObject);
                }
            }
        }
    }

    // Create the generateKey method to access keystore and generate the encryption key
    private void generateKey() throws FingerprintException {
        try {
            // Obtain a reference to the Keystore using the standard Android keystore container identifier
            keyStore = KeyStore.getInstance("AndroidKeyStore");
            keyGenerator = KeyGenerator.getInstance(KeyProperties.KEY_ALGORITHM_AES, "AndroidKeyStore");
            keyStore.load(null);
            keyGenerator.init(new

                    // Specify the operations this key can be used for
                    KeyGenParameterSpec.Builder(KEY_NAME,
                    KeyProperties.PURPOSE_ENCRYPT |
                            KeyProperties.PURPOSE_DECRYPT)
                    .setBlockModes(KeyProperties.BLOCK_MODE_CBC)

                    // Configure this key so that the user has to confirm their identity with a
                    // fingerprint each time they want to use it
                    .setUserAuthenticationRequired(true)
                    .setEncryptionPaddings(
                            KeyProperties.ENCRYPTION_PADDING_PKCS7)
                    .build());

            // Generate key
            keyGenerator.generateKey();

        } catch (KeyStoreException
                | NoSuchAlgorithmException
                | NoSuchProviderException
                | InvalidAlgorithmParameterException
                | CertificateException
                | IOException e) {
            e.printStackTrace();
            throw new FingerprintException(e);
        }
    }

    // initialzize cipher
    public boolean initCipher() {
        try {
            // Obtain a cipher instance and configure it with the
            // properties required for fingerprint authentication
            cipher = Cipher.getInstance(
                    KeyProperties.KEY_ALGORITHM_AES + "/"
                            + KeyProperties.BLOCK_MODE_CBC + "/"
                            + KeyProperties.ENCRYPTION_PADDING_PKCS7);
        } catch (NoSuchAlgorithmException |
                NoSuchPaddingException e) {
            throw new RuntimeException("Failed to get Cipher", e);
        }

        try {
            keyStore.load(null);
            SecretKey key = (SecretKey) keyStore.getKey(KEY_NAME,
                    null);
            cipher.init(Cipher.ENCRYPT_MODE, key);
            return true;
        } catch (KeyPermanentlyInvalidatedException e) {
             return false;
        } catch (KeyStoreException | CertificateException
                | UnrecoverableKeyException | IOException
                | NoSuchAlgorithmException | InvalidKeyException e) {
            throw new RuntimeException("Failed to init Cipher", e);
        }
    }
    // Custom exception class for when the fingerprint is not recognised
    private class FingerprintException extends Exception {
        public FingerprintException(Exception e) {
            super(e);
        }
    }

    /**
     * When the user presses the back key on the phone
     */
    @Override
    public void onBackPressed() {
        // if they are here they should not be able to go back
    }

    /**
     * If there are no users present in the database (in some edge case), redirects the user
     * to the database intialization activity in order to properly create the first employee.
     */
    public void retryInitialization() {
        // get the number of total users in the database
        int numOfUsers = DatabaseSelectHelper.getUsersDetails(this).size();
        // if there are no users at all (employee, customer included), we want to redirect the user
        // back to the appropriate activity to create an employee.
        if (numOfUsers <= 0) {
            // create a new intent to move us back to the initDatabaseActivity
            Intent initialize = new Intent(this, InitDatabaseActivity.class);
            startActivity(initialize);
        }
    }
}
