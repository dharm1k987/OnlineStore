package com.android.group0674.onlinestore.Controller.Employee;

import android.content.Context;
import android.content.Intent;
import android.view.View;

import com.android.group0674.onlinestore.View.ViewAccountActivity;

/**
 * Created by Dharmik on November 30, 2017.
 * Controller class that serves as a controller to decide the behaviour of the view account button
 */
public class ViewAccountTypesController implements View.OnClickListener {
    private Context appContext;

    /**
     * Sets the appContext to the Context of the Activity where this controller is initialized
     * @param context
     */
    public ViewAccountTypesController(Context context){
        this.appContext = context;
    }

    @Override
    public void onClick(View view) {
        // get the information from old intent
        // simply transition to the activity that shows us the accounts

        Intent intent = new Intent(this.appContext, ViewAccountActivity.class);
        this.appContext.startActivity(intent);
    }
}
