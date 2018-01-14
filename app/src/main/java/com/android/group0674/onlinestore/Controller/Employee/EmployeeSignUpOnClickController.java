package com.android.group0674.onlinestore.Controller.Employee;

import android.content.Context;
import android.content.Intent;
import android.support.v7.app.AppCompatActivity;
import android.view.View;
import android.widget.EditText;
import android.widget.Toast;

import com.android.group0674.onlinestore.Model.database.DatabaseInsertHelper;
import com.android.group0674.onlinestore.Model.database.DatabaseSelectHelper;
import com.android.group0674.onlinestore.R;
import com.android.group0674.onlinestore.View.MainActivity;

import java.util.Calendar;

/**
 * Created by Vishwa on 22/11/17.
 * Controller class that decides the behaviour of the sign up button in employee sign up activity
 */
public class EmployeeSignUpOnClickController implements View.OnClickListener {

    private Context appContext;

    /**
     * Sets the appContext to the Context of the Activity where this controller is initialized
     * @param context
     */
    public EmployeeSignUpOnClickController(Context context){
        this.appContext = context;
    }

    @Override
    public void onClick(View view) {

        // name and pwd
        EditText employeeName = (EditText)((AppCompatActivity) appContext).findViewById(R.id.employeeNameETxt);
        EditText employeePwd =(EditText)((AppCompatActivity) appContext).findViewById(R.id.employeePasswordETxt);

        // birthday
        EditText employeeMonth = (EditText)((AppCompatActivity) appContext).findViewById(R.id.employeeMonthETxt);
        EditText employeeDay = (EditText)((AppCompatActivity) appContext).findViewById(R.id.employeeDayETxt);
        EditText employeeYear = (EditText)((AppCompatActivity) appContext).findViewById(R.id.employeeYearETxt);

        // address
        EditText employeeAddress = (EditText)((AppCompatActivity) appContext).findViewById(R.id.employeeAddressETxt);

        String employeeNameTxt = employeeName.getText().toString();
        String employeePasswordTxt = employeePwd.getText().toString();
        String employeeAddressTxt = employeeAddress.getText().toString();

        Integer userId;
        // get the current year
        Integer currentYear = Calendar.getInstance().get(Calendar.YEAR);
        try {
            // calculate the employee age
            Integer employeeAge = currentYear - Integer.parseInt(employeeYear.getText().toString());
            if (Integer.parseInt(employeeMonth.getText().toString()) > 12 ||
                    Integer.parseInt(employeeDay.getText().toString()) > 31) {
                throw new Exception();
            }

            // add the employee to the database and retrieve its userId
            userId = DatabaseInsertHelper.insertNewUser(employeeNameTxt, employeeAge, employeeAddressTxt
            , employeePasswordTxt, appContext);

            if (userId == -1) {
                throw new Exception();
            }
            Toast.makeText(appContext, "Successfully created employee: id " + userId, Toast.LENGTH_LONG).show();

            // insert the user role as an employee
            DatabaseInsertHelper.insertUserRole(userId, DatabaseSelectHelper.getRoleIdByName("EMPLOYEE", appContext),
                    appContext);

            // Switches Activity from Employee sign up to Main activity
            Intent intent = new Intent(this.appContext, MainActivity.class);
            this.appContext.startActivity(intent);

        } catch (Exception e) {
            Toast.makeText(appContext, "One or more inputs are incorrect", Toast.LENGTH_LONG).show();
        }
        //DatabaseInsertHelper.insertNewUser()
    }
}
