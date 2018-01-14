package com.android.group0674.onlinestore.Controller.Employee;

import android.content.Context;
import android.content.Intent;
import android.view.View;

import com.android.group0674.onlinestore.View.EmployeeSignUpActivity;
import com.android.group0674.onlinestore.View.ViewBooksActivity;

/**
 * Created by Dharmik on November 30, 2017.
 * Controller class that serves as a controller to decide the behaviour of the view books button
 */
public class ViewBooksController implements View.OnClickListener {
    private Context appContext;

    /**
     * Sets the appContext to the Context of the Activity where this controller is initialized
     * @param context
     */
    public ViewBooksController(Context context){
        this.appContext = context;
    }

    public void onClick(View view) {

        // simply transition to the activity that shows us the books
        Intent intent = new Intent(this.appContext, ViewBooksActivity.class);
        this.appContext.startActivity(intent);
    }
}
