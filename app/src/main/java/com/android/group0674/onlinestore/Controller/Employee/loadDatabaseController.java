package com.android.group0674.onlinestore.Controller.Employee;

import android.content.Context;
import android.content.Intent;
import android.view.View;

import com.android.group0674.onlinestore.Model.database.DeserializeDB;
import com.android.group0674.onlinestore.View.MainActivity;

/**
 * Created by Vishwa on 01/12/17.
 * Controller class that serves as a controller to decide the behaviour of the load button
 */
public class loadDatabaseController implements View.OnClickListener {
    private Context appContext;

    /**
     * Sets the appContext to the Context of the Activity where this controller is initialized
     * @param context
     */
    public loadDatabaseController(Context context){
        this.appContext = context;
    }

    @Override
    public void onClick(View view) {

        // Once the save database button is clicked,
        // we want to create a serializeDB instance

        DeserializeDB deserializeDBController = new DeserializeDB(
                this.appContext);
        // Database is deserialized and we are done loading the database
        deserializeDBController.deserialize();
        // go back to the sign in activity
        Intent intent = new Intent(appContext, MainActivity.class);
        appContext.startActivity(intent);

    }
}
