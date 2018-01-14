package com.android.group0674.onlinestore.Controller;

import android.content.Context;
import android.content.Intent;
import android.support.v7.app.AppCompatActivity;
import android.view.View;
import android.widget.EditText;
import android.widget.Toast;

import com.android.group0674.onlinestore.Model.database.DatabaseInsertHelper;
import com.android.group0674.onlinestore.Model.database.DatabaseSelectHelper;
import com.android.group0674.onlinestore.R;
import com.android.group0674.onlinestore.View.CustomerSignUpActivity;
import com.android.group0674.onlinestore.View.EmployeeActivity;
import com.android.group0674.onlinestore.View.EmployeeSignUpActivity;
import com.android.group0674.onlinestore.View.MainActivity;

import java.util.Calendar;

/**
 * Created by Vishwa on 21/11/17.
 */

/**
 * Controller class that serves as a controller to decide the behaviour of the button in customer
 * set up activity
 */
public class CustomerSignUpOnClickController implements View.OnClickListener {

    private Context appContext;

    /**
     * Sets the appContext to the Context of the Activity where this controller is initialized
     * @param appContext
     */
    public CustomerSignUpOnClickController(Context appContext){
        this.appContext = appContext;
    }

    @Override
    public void onClick(View view) {
        // name and pwd
        EditText customerName = (EditText)((AppCompatActivity) appContext).findViewById(R.id.customerNameETxt);
        EditText customerPwd =(EditText)((AppCompatActivity) appContext).findViewById(R.id.customerPassword);

        // birthday
        EditText customerMonth = (EditText)((AppCompatActivity) appContext).findViewById(R.id.customerMonthETxt);
        EditText customerDay = (EditText)((AppCompatActivity) appContext).findViewById(R.id.customerDayETxt);
        EditText customerYear = (EditText)((AppCompatActivity) appContext).findViewById(R.id.customerYearETxt);

        // address
        EditText customerAddress = (EditText)((AppCompatActivity) appContext).findViewById(R.id.customerAddressETxt);

        String customerNameTxt = customerName.getText().toString();
        String customerPasswordTxt = customerPwd.getText().toString();
        String customerAddressTxt = customerAddress.getText().toString();

        Integer userId;
        // get the current year
        Integer currentYear = Calendar.getInstance().get(Calendar.YEAR);
        try {
            Integer customerAge = currentYear - Integer.parseInt(customerYear.getText().toString());
            if (Integer.parseInt(customerMonth.getText().toString()) > 12 ||
                    Integer.parseInt(customerDay.getText().toString()) > 31) {
                throw new Exception();
            }
            userId = DatabaseInsertHelper.insertNewUser(customerNameTxt, customerAge, customerAddressTxt
                    , customerPasswordTxt, appContext);

            if (userId == -1) {
                throw new Exception();
            }
            Toast.makeText(appContext, "Successfully created customer: id " + userId, Toast.LENGTH_LONG).show();
            // insert the user role
            DatabaseInsertHelper.insertUserRole(userId, DatabaseSelectHelper.getRoleIdByName("CUSTOMER", appContext),
                    appContext);
            // If the employee is successfully created then move to the login activity

            // Switches Activity from Employee sign up to Main activity
            Intent intent = new Intent(this.appContext, EmployeeActivity.class);
            this.appContext.startActivity(intent);


        } catch (Exception e) {
            Toast.makeText(appContext, "Failed to create customer", Toast.LENGTH_LONG).show();
        }

    }
}
