package com.android.group0674.onlinestore.Controller.User;

import android.content.Context;
import android.content.Intent;
import android.view.View;

import com.android.group0674.onlinestore.View.MainActivity;

/**
 * Created by Harsh on 2017-11-29.
 * Controller class that serves as a controller to decide the behaviour of the log out button
 */
public class UserLogOutOnClickController implements View.OnClickListener {
    private Context appContext;

    /**
     * Sets the appContext to the Context of the Activity where this controller is initialized
     * @param context
     */
    public UserLogOutOnClickController(Context context) {
        this.appContext = context;
    }

    @Override
    public void onClick(View view) {
        // go back to the main activity
        Intent intent = new Intent(appContext, MainActivity.class);
        appContext.startActivity(intent);
    }
}
