package com.android.group0674.onlinestore.Controller;

import android.content.Context;
import android.content.Intent;

import com.android.group0674.onlinestore.View.EmployeeSignUpActivity;

/**
 * Created by Vishwa on 21/11/17.
 */

/**
 *  Class that runs for a given amount of time and then executes transition between
 *  the InitDatabaseActivity and CustomerSignUpActivity
 */
public class TransitionRunnable implements Runnable {
    private Context context;

    /**
     * Sets the appContext to the Context of the Activity where this controller is initialized
     * @param appContext
     */
    public TransitionRunnable(Context appContext){
        this.context = appContext;
    }

    @Override
    public void run() {
        Intent intent = new Intent(context, EmployeeSignUpActivity.class);
        context.startActivity(intent);
    }
}
