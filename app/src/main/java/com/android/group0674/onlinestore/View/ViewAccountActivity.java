package com.android.group0674.onlinestore.View;

import android.content.Intent;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.widget.Button;

import com.android.group0674.onlinestore.Controller.Employee.ActiveAccountsController;
import com.android.group0674.onlinestore.Controller.Employee.UnactiveAccountsController;
import com.android.group0674.onlinestore.R;

public class ViewAccountActivity extends AppCompatActivity {

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_view_account);

        // get the customerId from intent
        Intent myIntent = getIntent(); // gets the previously created intent
        int customerId = myIntent.getIntExtra("customerId", 0);

        // get the active and inactive, and set on click listeners for those
        Button activeAccountsBtn = (Button)findViewById(R.id.viewActiveBtn);
        Button unactiveAccountsBtn = (Button) findViewById(R.id.viewUnactiveBtn);

        // set their on click listeners
        activeAccountsBtn.setOnClickListener(new ActiveAccountsController(this, customerId));
        unactiveAccountsBtn.setOnClickListener(new UnactiveAccountsController(this, customerId));
    }
}
