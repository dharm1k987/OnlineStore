package com.android.group0674.onlinestore.Controller.Employee;

import android.content.Context;
import android.content.Intent;
import android.support.v7.app.AppCompatActivity;
import android.util.Log;
import android.view.View;
import android.widget.EditText;
import android.widget.Toast;

import com.android.group0674.onlinestore.Model.database.DatabaseSelectHelper;
import com.android.group0674.onlinestore.Model.users.User;
import com.android.group0674.onlinestore.R;
import com.android.group0674.onlinestore.View.EmployeeActivity;
import com.android.group0674.onlinestore.View.CustomerActivity;

/**
 * Created by Harsh on 2017-11-29.
 * Controller class that serves as a controller to decide the behaviour of the log in button
 */
public class AuthenticateOnClickController implements View.OnClickListener{

    private Context appContext;

    /**
     * Sets the appContext to the Context of the Activity where this controller is initialized
     * @param context
     */
    public AuthenticateOnClickController(Context context){
        this.appContext = context;
    }

    @Override
    public void onClick(View view) {
        // get the user id and their password
        EditText userId = (EditText)((AppCompatActivity) appContext).findViewById(R.id.usernameIdETxt);
        EditText userPwd = (EditText)((AppCompatActivity) appContext).findViewById(R.id.passwordId);

        // get the string version
        String userIdTxt = userId.getText().toString();
        String userPwdTxt = userPwd.getText().toString();

        // now we check to see if they match anything in the database
        User userEmployee = null;
        User userCustomer = null;
        try {
            // checks if the user actually entered something in the field
            if (userIdTxt.length() == 0 || userPwdTxt.length() == 0) {
                Toast.makeText(appContext, "One or more of the inputs is empty", Toast.LENGTH_SHORT).show();
            }
            // make boolean values to check whether or not the id entered belonged to a customer
            // or an employee
            boolean isEmployee = ((userEmployee = DatabaseSelectHelper.getUserDetails(
                    Integer.parseInt(userIdTxt), appContext)) != null) &&
                    (DatabaseSelectHelper.getRoleName(userEmployee.getRoleId(), appContext)
                    .equals("EMPLOYEE"));
            boolean isCustomer = ((userCustomer = DatabaseSelectHelper.getUserDetails(
                    Integer.parseInt(userIdTxt), appContext)) != null) &&
                    (DatabaseSelectHelper.getRoleName(userCustomer.getRoleId(), appContext)
                            .equals("CUSTOMER"));

            // If the id does not belong to an employee or customer, then make toast
            // informing the user
            if (isCustomer == false && isEmployee == false) {
                Toast.makeText(appContext, "This id does not belong to a user", Toast.LENGTH_SHORT).show();
            } else {
                if (isCustomer) {
                    // customer
                    // otherwise we just verify their password
                    if (userCustomer.authenticate(userPwdTxt)) {
                        // successful, set authentication to true
                        userCustomer.setAuthenticated(true);

                        // take them to the employee interface activity
                        Intent initialize = new Intent(appContext, CustomerActivity.class);
                        initialize.putExtra("customerId", userCustomer.getId());
                        appContext.startActivity(initialize);

                    } else {
                        // incorrect pwd
                        Toast.makeText(appContext, "Wrong password", Toast.LENGTH_SHORT).show();
                    }
                } else {
                    // employee
                    // otherwise we just verify their password
                    if (userEmployee.authenticate(userPwdTxt)) {
                        // successful, set authentication to true
                        userEmployee.setAuthenticated(true);
                        // take them to the employee interface activity
                        Intent initialize = new Intent(appContext, EmployeeActivity.class);
                        appContext.startActivity(initialize);
                    } else {
                        // incorrect pwd
                        Toast.makeText(appContext, "Wrong password", Toast.LENGTH_SHORT).show();
                    }
                }
            }
        }
        catch (Exception e) {
            // nothing else can be the error
        }
    }
}
