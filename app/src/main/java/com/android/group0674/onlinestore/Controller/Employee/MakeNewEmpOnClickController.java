package com.android.group0674.onlinestore.Controller.Employee;

import android.content.Context;
import android.content.Intent;
import android.view.View;

import com.android.group0674.onlinestore.View.EmployeeSignUpActivity;

/**
 * Created by Harsh on 2017-11-29.
 * Controller class that serves as a controller to decide the behaviour of the make new emplyee button
 */
public class MakeNewEmpOnClickController implements View.OnClickListener {
    private Context appContext;

    /**
     * Sets the appContext to the Context of the Activity where this controller is initialized
     * @param context
     */
    public MakeNewEmpOnClickController(Context context){
        this.appContext = context;
    }

    @Override
    public void onClick(View view) {
        //take user to employee set up activity
        Intent intent = new Intent(this.appContext, EmployeeSignUpActivity.class);
        this.appContext.startActivity(intent);
    }
}
