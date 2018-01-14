package com.android.group0674.onlinestore.View;

import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.widget.Button;

import com.android.group0674.onlinestore.Controller.Employee.EmployeeLogOutOnClickController;
import com.android.group0674.onlinestore.Controller.Employee.EmployeeRestockController;
import com.android.group0674.onlinestore.Controller.Employee.MakeAccountTypesController;
import com.android.group0674.onlinestore.Controller.Employee.MakeNewAccOnClickController;
import com.android.group0674.onlinestore.Controller.Employee.MakeNewCustOnClickController;
import com.android.group0674.onlinestore.Controller.Employee.MakeNewEmpOnClickController;
import com.android.group0674.onlinestore.Controller.Employee.ViewBooksController;
import com.android.group0674.onlinestore.Controller.Employee.RestockListOnClickListener;
import com.android.group0674.onlinestore.Controller.Employee.loadDatabaseController;
import com.android.group0674.onlinestore.Controller.Employee.saveDatabaseController;
import com.android.group0674.onlinestore.R;

import java.io.Serializable;

/**
 * Activity that serves to display all the actions that an Employee can do after they
 * are logged in
 */
public class EmployeeActivity extends AppCompatActivity implements Serializable {

    /**
     * Runs when this activity is called
     * @param savedInstanceState
     */
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_employee);

        // employee button
        Button employeeCreateBtn = (Button)findViewById(R.id.makeNewEmployeeBtn);
        employeeCreateBtn.setOnClickListener(new MakeNewEmpOnClickController(this));

        // customer button
        Button customerCreateBtn = (Button)findViewById(R.id.makeNewCustomerBtn);
        customerCreateBtn.setOnClickListener(new MakeNewCustOnClickController(this));

        // logout button
        Button logOutBtn = (Button)findViewById(R.id.logOutBtn);
        logOutBtn.setOnClickListener(new EmployeeLogOutOnClickController(this));

        // authenticate button
        Button authBtn = (Button)findViewById(R.id.authenticateNewEmployeeBtn);
        authBtn.setOnClickListener(new EmployeeLogOutOnClickController(this));

        // account button
        Button accBtn = (Button)findViewById(R.id.makeNewAccountBtn);
        accBtn.setOnClickListener(new MakeNewAccOnClickController(this));


        // view books button
        Button viewBooksBtn = (Button)findViewById(R.id.viewBooksBtn);
        viewBooksBtn.setOnClickListener(new ViewBooksController(this));


        // restock inventory button
        Button restockBtn = (Button)findViewById(R.id.restockInventoryBtn);
        restockBtn.setOnClickListener(new EmployeeRestockController(this));

        // view types of accounts button
        Button viewAccountType = (Button)findViewById(R.id.viewAccBtn);
        viewAccountType.setOnClickListener(new MakeAccountTypesController(this));


        // Save the database button
        Button saveDatabase = (Button)findViewById(R.id.saveDatabaseBtn);
        saveDatabase.setOnClickListener(new saveDatabaseController(this));

        // Load the database button
        Button loadDatabase = (Button)findViewById(R.id.loadDatabaseBtn);
        loadDatabase.setOnClickListener(new loadDatabaseController(this));


    }

    /**
     * When the back button of the phone is pressed, nothing happens.
     */
    @Override
    public void onBackPressed(){
        // Do Nothing
    }
}
