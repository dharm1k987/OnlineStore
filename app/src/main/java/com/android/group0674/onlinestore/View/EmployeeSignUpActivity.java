package com.android.group0674.onlinestore.View;

import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.widget.Button;
import android.widget.EditText;

import com.android.group0674.onlinestore.Controller.Employee.EmployeeSignUpOnClickController;
import com.android.group0674.onlinestore.R;

/**
 * Activity that serves for employees to sign up for the online store
 */
public class EmployeeSignUpActivity extends AppCompatActivity {

    /**
     * Runs when this activity is called
     * @param savedInstanceState
     */
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_employee_sign_up);

        // Sets up the sign up button for Employee to allow employees to sign up
        Button employeeSignUpBtn = (Button)findViewById(R.id.employeeSignUpBtn);

        // Sets the behaviour of the employee button when clicked
        employeeSignUpBtn.setOnClickListener(new EmployeeSignUpOnClickController(this));
    }
}
