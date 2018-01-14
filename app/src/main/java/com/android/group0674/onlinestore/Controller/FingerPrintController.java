package com.android.group0674.onlinestore.Controller;

import android.Manifest;
import android.app.AlertDialog;
import android.app.Dialog;
import android.content.Context;
import android.content.DialogInterface;
import android.content.Intent;
import android.content.pm.PackageManager;
import android.hardware.fingerprint.FingerprintManager;
import android.os.CancellationSignal;
import android.support.v4.app.ActivityCompat;
import android.support.v7.app.AppCompatActivity;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.widget.EditText;
import android.widget.Toast;

import com.android.group0674.onlinestore.Model.database.DatabaseSelectHelper;
import com.android.group0674.onlinestore.Model.users.User;
import com.android.group0674.onlinestore.R;
import com.android.group0674.onlinestore.View.EmployeeActivity;
import com.android.group0674.onlinestore.View.MainActivity;

/**
 * Created by Harsh on 2017-12-02.
 */

/**
 * Decides what happens when the user uses the fingerprint sensor.
 */
public class FingerPrintController extends FingerprintManager.AuthenticationCallback{

    private CancellationSignal cancellationSignal;
    private Context context;
    private FingerprintManager manager;
    private FingerprintManager.CryptoObject cryptoObject;

    /**
     * Constructor method.
     * @param mContext
     */
    public FingerPrintController(Context mContext) {
        context = mContext;
    }

    /**
     * This method is responsible for starting the fingerprint authentication process.
     * @param manager
     * @param cryptoObject
     */
    public void startAuth(FingerprintManager manager, FingerprintManager.CryptoObject cryptoObject) {
        this.manager = manager;
        this.cryptoObject = cryptoObject;
        cancellationSignal = new CancellationSignal();
        if (ActivityCompat.checkSelfPermission(context, Manifest.permission.USE_FINGERPRINT) != PackageManager.PERMISSION_GRANTED) {
            return;
        }
        manager.authenticate(cryptoObject, cancellationSignal, 0, this, null);
    }

    /**
     * Called when a fatal error has occured.
     * @param errMsgId provides the error code
     * @param errString provides the error message
     */
    @Override
    public void onAuthenticationError(int errMsgId, CharSequence errString) {
        // nothing to do
        Log.d("auth error" , "asd");
    }

    /**
     * Called when the fingerprint doesn't match with any of the
     * fingerprints registered on the device
     */
    @Override
    public void onAuthenticationFailed() {
        Toast.makeText(context, "Authentication failed", Toast.LENGTH_LONG).show();
        this.startAuth(this.manager, this.cryptoObject);
    }

    /**
     * Called when a non-fatal error has occurred
     * @param helpMsgId
     * @param helpString
     */
    @Override
    public void onAuthenticationHelp(int helpMsgId, CharSequence helpString) {
        // nothing to do
        Log.d("auth erro22222r" , "asd");
    }

    /**
     * When the fingerprint successfully matched to the one stored in the phone
     * @param result
     */
    @Override
    public void onAuthenticationSucceeded(FingerprintManager.AuthenticationResult result) {

        // Make to toast to let the user know that the fingerprint matched
        Toast.makeText(context, "Fingerprint Matched", Toast.LENGTH_LONG).show();

        // create a dialog box
        View alertView = (LayoutInflater.from(this.context)).inflate(R.layout.activity_alertdialog, null);
        final EditText userId = (EditText) alertView.findViewById(R.id.userInput);
        userId.setHint("Employee ID");

        // set up alert dialog
        AlertDialog.Builder alertDialog = new AlertDialog.Builder(this.context)
                .setTitle("Employee")
                .setMessage("Enter Your Employee ID:")
                .setView(alertView)
                .setCancelable(true)
                // when the user presses the OK option
                .setPositiveButton("OK", new DialogInterface.OnClickListener() {
                    @Override
                    public void onClick(DialogInterface dialogInterface, int i) {
                        // get the input
                        String userIdTxt = userId.getText().toString();
                        User userEmployee = null;
                        // if the input is empty
                        if(userIdTxt.length() == 0){
                            Toast.makeText(context, "Invalid input", Toast.LENGTH_LONG).show();
                        // if the input is not empty
                        } else {
                            // check if the ID entered is a valid employee id.
                            boolean isEmployee = ((userEmployee = DatabaseSelectHelper.getUserDetails(
                                    Integer.parseInt(userIdTxt), context)) != null) &&
                                    (DatabaseSelectHelper.getRoleName(userEmployee.getRoleId(), context)
                                            .equals("EMPLOYEE"));
                            // If it is a valid ID
                            if (isEmployee) {
                                // Move on to the employeeActivity
                                Intent intent = new Intent(context, EmployeeActivity.class);
                                context.startActivity(intent);
                            // If it isnt a valid ID
                            } else {
                                // Tell the user the id is not valid
                                Toast.makeText(context, "This id does not belong to an employee", Toast.LENGTH_SHORT).show();
                                // Check for the fingerprint again.
                                FingerPrintController.this.startAuth(FingerPrintController.this.manager,
                                        FingerPrintController.this.cryptoObject);
                            }
                        }
                    }
                })
                // If the user presses Cancel
                .setNegativeButton("Cancel", new NegativeButtonListener());

        Dialog dialog = alertDialog.create();
        dialog.show();
    }

    /**
     * Runs when the user presses the cancel button in the alert dialog.
     */
    private class NegativeButtonListener implements DialogInterface.OnClickListener{
        /**
         * if the user presses cancel, the dialog box dismisses.
         * @param dialogInterface
         * @param i
         */
        @Override
        public void onClick(DialogInterface dialogInterface, int i) {
            dialogInterface.dismiss();
            // check for fingerprint again
            FingerPrintController.this.startAuth(FingerPrintController.this.manager,
                    FingerPrintController.this.cryptoObject);

        }
    }
}
