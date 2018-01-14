package com.android.group0674.onlinestore.View;

import android.content.Intent;
import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;
import android.widget.Button;

import com.android.group0674.onlinestore.Controller.CustomerSignUpOnClickController;
import com.android.group0674.onlinestore.R;

/**
 *  Activity that serves to help the Admin sign up for the online store
 */
public class CustomerSignUpActivity extends AppCompatActivity {

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_customer_sign_up);

        // Sets up the sign up button for Admin to allow admins to sign up
        Button customerSignUpBtn = (Button)findViewById(R.id.customerSignUpBtn);

        // Sets the behaviour of the button when clicked
        customerSignUpBtn.setOnClickListener(new CustomerSignUpOnClickController(this));

    }

    /**
     * When the back button is pressed, do the the EmployeeActivity
     */
    @Override
    public void onBackPressed(){
        Intent intent = new Intent(this, EmployeeActivity.class);
        this.startActivity(intent);
    }
}
