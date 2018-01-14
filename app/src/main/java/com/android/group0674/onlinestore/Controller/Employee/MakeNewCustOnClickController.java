package com.android.group0674.onlinestore.Controller.Employee;

import android.content.Context;
import android.content.Intent;
import android.view.View;

import com.android.group0674.onlinestore.View.CustomerSignUpActivity;

/**
 * Created by Harsh on 2017-11-29.
 * Controller class that serves as a controller to decide the behaviour of the make new customer button
 */
public class MakeNewCustOnClickController implements View.OnClickListener {

    private Context appContext;

    /**
     * Sets the appContext to the Context of the Activity where this controller is initialized
     * @param context
     */
    public MakeNewCustOnClickController(Context context) {
        this.appContext = context;
    }

    @Override
    public void onClick(View view) {
        // go to the customer set up activity
        Intent intent = new Intent(this.appContext, CustomerSignUpActivity.class);
        this.appContext.startActivity(intent);
    }
}
