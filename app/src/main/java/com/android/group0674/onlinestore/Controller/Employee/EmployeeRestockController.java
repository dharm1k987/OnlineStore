package com.android.group0674.onlinestore.Controller.Employee;

import android.content.Context;
import android.content.Intent;
import android.view.View;

import com.android.group0674.onlinestore.View.RestockInventoryActivity;

/**
 * Created by Ishan on 01/12/17.
 * Controller class that serves as a controller to decide the behaviour of the restock inventory button
 */
public class EmployeeRestockController implements View.OnClickListener {

    private Context appContext;

    /**
     * Sets the appContext to the Context of the Activity where this controller is initialized
     * @param context
     */
    public EmployeeRestockController(Context context){
        this.appContext = context;
    }

    @Override
    public void onClick(View view) {
        // move to the restock inventory class
        Intent intent = new Intent(appContext, RestockInventoryActivity.class);
        appContext.startActivity(intent);
    }
}
