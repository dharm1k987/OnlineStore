package com.android.group0674.onlinestore.Controller.Employee;

import android.content.Context;
import android.view.View;

import com.android.group0674.onlinestore.Model.database.SerializeDB;

/**
 * Created by Vishwa on 01/12/17.
 * Controller class that serves as a controller to decide the behaviour of the save database button
 */
public class saveDatabaseController implements View.OnClickListener {
    private Context appContext;

    /**
     * Sets the appContext to the Context of the Activity where this controller is initialized
     * @param context
     */
    public saveDatabaseController(Context context){
        this.appContext = context;
    }

    @Override
    public void onClick(View view) {

        // Once the save database button is clicked,
        // we want to create a serializeDB instance

        SerializeDB serializeDBController = new SerializeDB(this.appContext);
        // Database is serialized
        serializeDBController.serialize();

    }
}
