package com.android.group0674.onlinestore.Controller.Employee;

import android.app.AlertDialog;
import android.app.Dialog;
import android.content.Context;
import android.content.DialogInterface;
import android.content.Intent;
import android.view.LayoutInflater;
import android.view.View;
import android.widget.EditText;
import android.widget.Toast;

import com.android.group0674.onlinestore.Model.database.DatabaseInsertHelper;
import com.android.group0674.onlinestore.Model.database.DatabaseSelectHelper;
import com.android.group0674.onlinestore.Model.users.User;
import com.android.group0674.onlinestore.R;
import com.android.group0674.onlinestore.View.EmployeeActivity;
import com.android.group0674.onlinestore.View.ViewAccountActivity;

/**
 * Created by Dharmik on December 1, 2017.
 * Controller class that serves as a controller to decide the behaviour of the create account button
 */
public class MakeAccountTypesController implements View.OnClickListener {
    private Context appContext;
    private String userIdText;

    /**
     * Sets the appContext to the Context of the Activity where this controller is initialized
     * @param appContext
     */
    public MakeAccountTypesController(Context appContext) {
        this.appContext = appContext;
    }

    @Override
    public void onClick(View view) {
        // create an alert view to pop up to prompt the employee of the customerId account
        // they want to check
        View alertView = (LayoutInflater.from(this.appContext)).inflate(R.layout.activity_alertdialog, null);
        final EditText userId = (EditText) alertView.findViewById(R.id.userInput);

        // Log.d("the output that i got was ", userIdText);
        // set up alert dialog
        AlertDialog.Builder alertDialog = new AlertDialog.Builder(this.appContext)
                .setTitle("Account Information")
                .setMessage("Please Enter your Customer ID")
                .setView(alertView)
                .setCancelable(true)
                .setPositiveButton("OK", new DialogInterface.OnClickListener() {
                    @Override
                    public void onClick(DialogInterface dialogInterface, int i) {
                        userIdText = userId.getText().toString();
                        // if the employee entered an empty id, then make toast telling them
                        if(userIdText.length() == 0){
                            Toast.makeText(appContext, "Invalid input", Toast.LENGTH_LONG).show();
                        } else {
                            User userCustomer = null;
                            // otherwies valid input, so try inserting
                            int customerId = Integer.parseInt(userId.getText().toString());

                            // If it is a invalid customer Id then make toast telling them
                            if (((userCustomer = DatabaseSelectHelper.getUserDetails(customerId, appContext)) == null)
                                    || (!DatabaseSelectHelper.getRoleName(userCustomer.getRoleId(), appContext)
                                    .equals("CUSTOMER"))) {
                                // failed to create account
                                Toast.makeText(appContext, "This customer id does not exist", Toast.LENGTH_LONG).show();
                            } else {
                                // SAVE THE ID IN INTENT
                                // now go to the other activity with this id
                                Intent intent = new Intent(appContext, ViewAccountActivity.class);
                                intent.putExtra("customerId",customerId);
                                appContext.startActivity(intent);
                            }
                        }
                    }
                })
                .setNegativeButton("Cancel", new NegativeButtonListener());

        Dialog dialog = alertDialog.create();
        dialog.show();
    }

    /**
     * A class to control the negative button listener in the alert dialog
     */
    private class NegativeButtonListener implements DialogInterface.OnClickListener{

        @Override
        public void onClick(DialogInterface dialogInterface, int i) {
            // if they cancel, then simply dismiss the alert dialog.
            dialogInterface.dismiss();
        }
    }
}

