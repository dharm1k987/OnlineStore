package com.android.group0674.onlinestore.Controller.Employee;

import android.app.AlertDialog;
import android.app.Dialog;
import android.content.Context;
import android.content.DialogInterface;
import android.content.Intent;
import android.support.v7.app.AppCompatActivity;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.widget.EditText;
import android.widget.Toast;

import com.android.group0674.onlinestore.Model.database.DatabaseInsertHelper;
import com.android.group0674.onlinestore.Model.database.DatabaseSelectHelper;
import com.android.group0674.onlinestore.Model.database.DatabaseUpdateHelper;
import com.android.group0674.onlinestore.Model.users.Account;
import com.android.group0674.onlinestore.Model.users.Customer;
import com.android.group0674.onlinestore.R;
import com.android.group0674.onlinestore.View.EmployeeActivity;

import java.util.List;

/**
 * Created by Harsh on 2017-11-29.
 * Controller class that serves as a controller to decide the behaviour of the make new account button
 */
public class MakeNewAccOnClickController implements View.OnClickListener {
    private Context appContext;
    private String userIdText;

    /**
     * Sets the appContext to the Context of the Activity where this controller is initialized
     * @param appContext
     */
    public MakeNewAccOnClickController(Context appContext) {
        this.appContext = appContext;
    }

    @Override
    public void onClick(View view) {

        View alertView = (LayoutInflater.from(this.appContext)).inflate(R.layout.activity_alertdialog, null);
        final EditText userId = (EditText) alertView.findViewById(R.id.userInput);

       // Log.d("the output that i got was ", userIdText);
        // set up alert dialog
        AlertDialog.Builder alertDialog = new AlertDialog.Builder(this.appContext)
                .setTitle("Account Setup")
                .setMessage("Please Enter your Customer ID")
                .setView(alertView)
                .setCancelable(true)
                .setPositiveButton("Create Account", new DialogInterface.OnClickListener() {
                    @Override
                    public void onClick(DialogInterface dialogInterface, int i) {
                        userIdText = userId.getText().toString();
                        if(userIdText.length() == 0){
                            Toast.makeText(appContext, "Invalid input", Toast.LENGTH_LONG).show();
                        } else {
                            // otherwies valid input, so try inserting
                            int customerId = Integer.parseInt(userId.getText().toString());

                            int accountId = DatabaseInsertHelper.insertAccount(customerId, true, appContext);
                            // check if valid or not
                            if (accountId == -1) {
                                // failed to create account
                                Toast.makeText(appContext, "This customer id does not exist", Toast.LENGTH_LONG).show();
                            } else {
                                // If valid, then creating a customer account and setting the

                                // set all the other accounts that this customer has to inactive
                                List<Account> listOfActiveAcc = DatabaseSelectHelper.getUserActiveAccounts(customerId, appContext);
                                // loop through it, and set all the previous accounts to unactive
                                for (Account account : listOfActiveAcc) {
                                    if (account.getAccountId() != accountId) {
                                        // if the account id is not the same, then set this one to unactive
                                        DatabaseUpdateHelper.updateAccountStatus(account.getAccountId(), false, appContext);
                                    }
                                }
                                /*
                                Customer customer = (Customer) DatabaseSelectHelper.getUserDetails(customerId, appContext);
                                Account account = new Account(accountId, customerId, appContext);
                                account.setAccountStatus(true);
                                customer.setAccount(account);     */


                                Toast.makeText(appContext, "Successfully created account id: " + accountId,
                                        Toast.LENGTH_SHORT).show();

                            }
                            dialogInterface.dismiss();

                        }

                    }
                })
                .setNegativeButton("Cancel", new NegativeButtonListener());

        Dialog dialog = alertDialog.create();
        dialog.show();




    }

    private class NegativeButtonListener implements DialogInterface.OnClickListener{

        @Override
        public void onClick(DialogInterface dialogInterface, int i) {
            dialogInterface.dismiss();
        }
    }
}
