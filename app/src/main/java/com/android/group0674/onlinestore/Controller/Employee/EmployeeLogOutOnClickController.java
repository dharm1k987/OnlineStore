package com.android.group0674.onlinestore.Controller.Employee;

import android.content.Context;
import android.content.Intent;
import android.view.View;

import com.android.group0674.onlinestore.View.MainActivity;

/**
 * Created by Harsh on 2017-11-29.
 * Controller class that serves as a controller to decide the behaviour of the make log out button
 */
public class EmployeeLogOutOnClickController implements View.OnClickListener {

    private Context appContext;

    /**
     * Sets the appContext to the Context of the Activity where this controller is initialized
     * @param context
     */
    public EmployeeLogOutOnClickController(Context context){
        this.appContext = context;
    }

    @Override
    public void onClick(View view) {
        // If the logout button is clicked, we move back to the main login screen
        Intent intent = new Intent(this.appContext, MainActivity.class);
        this.appContext.startActivity(intent);
    }
}
